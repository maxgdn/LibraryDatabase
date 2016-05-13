package sample;

import com.avaje.ebean.Ebean;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import sample.models.SignIn;
import sample.models.Student;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controller {

    public static final Font FONT = Font.font(24);

    public String input = "000000000";
    public String invalidInput = "invalidInput";
    public String validInput;
    public HashMap<String, String> markBookMap;
    public List<String> firstNameList;
    public List<String> lastNameList;
    public String firstName;
    public String lastName;
    public Period currentPeriod;

    //Application Resources
    @FXML
    AnchorPane basePane;
    @FXML
    AnchorPane photoPane;
    @FXML
    Label clock;
    @FXML
    Label welcomeLabel;
    @FXML
    Label nameLabel;
    @FXML
    Label signInLabel;
    @FXML
    Button swapButton;
    @FXML
    TextField barcodeInput;

    @FXML
    public void initialize() {
        photoPane.getStyleClass().add("photoPane");
        setupComponents();
        setPeriod();

        // Set up a timer so our clock does clock stuff
        DateFormat dateTimeInstance = SimpleDateFormat.getDateTimeInstance();
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0), event -> {
            clock.setText(dateTimeInstance.format(new Date()));
        }), new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Set up the barcode input
        barcodeInput.setOnKeyReleased((ae) -> {
            if (ae.getCode() == KeyCode.ENTER) {
                input = barcodeInput.getText();
                checkBarcode(input);
                barcodeInput.clear();
            }
        });
        // Update the current period
        Timeline periodChecker = new Timeline(new KeyFrame(Duration.minutes(1), event -> {
            if (setPeriod()) {
                List<SignIn> all = SignIn.find.all();
                all.stream().filter(signIn1 -> signIn1.getTimeIn().toLocalTime().isAfter(currentPeriod.endTime)).forEach(signIn1 -> {
                    signIn1.setTimeOut(LocalDateTime.from(LocalDate.from(currentPeriod.endTime)));
                    signIn1.setWasManual(true);
                });
            }
        }));
        periodChecker.setCycleCount(Animation.INDEFINITE);
        periodChecker.play();
    }

    @FXML
    private void setupComponents() {
        signInLabel.setText("");
        nameLabel.setText("");
        welcomeLabel.setFont(FONT);
        nameLabel.setFont(Font.font(16));
        Platform.runLater(() -> barcodeInput.requestFocus());
    }

    public boolean setPeriod() {
        LocalTime localTime = LocalTime.now();
        for (int i = 0; i < Period.values().length; i++) {
            Period period = Period.values()[i];
            if (localTime.isBefore(period.endTime)) {
                setCurrentPeriod(period);
                return true;
            }
        }
        if (currentPeriod == null) {
            setCurrentPeriod(Period.BEFORE_CLASS);
            return true;
        }
        return false;
    }

    @FXML
    public void loadPhotoPane() {
        File f = new File("/studentphotos/" + validInput + ".JPG");
        if(f.isFile() && !f.isDirectory()) {
            photoPane.setStyle("-fx-background-image: url(\"/studentphotos/" + validInput + ".JPG\");\n" +
                    "-fx-background-size: contain;");
        } else {
            photoPane.setStyle("-fx-background-image: url(\"/studentphotos/bealuser.JPG\");\n" +
                    "-fx-background-size: contain;\n" + "-fx-background-size: 180 225;");
        }
    }

    @FXML
    public void goToOptions() {
        try {
            Util.replaceSceneContent("options.fxml");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String checkBarcode(String studentInput) {
        if (isInteger(studentInput, 10)) {
            if ((studentInput.length() != 9)) {
                barcodeInput.clear();
                return invalidInput;
            } else {
                validInput = studentInput;
                handleUserEntry();
                return validInput;
            }
        } else {
            return invalidInput;
        }
    }

    private boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    public void handleUserEntry() {
        ArrayList<String> queueList = new ArrayList<>();
        queueList.add(0, validInput);
        if (queueList.size() >= 2) {
            queueList.remove(0);
        } else {
            loadPhotoPane();
            try {
                findUserInMarkBook();
                loadUserName();

                Ebean.execute(() -> {
                    Student student = Student.find.where().eq("studentID", validInput).findUnique();
                    if (student == null) {
                        student = newStudentEntry(firstName, lastName, Integer.parseInt(validInput));
                    }
                    SignIn signIn = SignIn.find.where().eq("student.id", student.id).orderBy("timeIn desc").setMaxRows(1).findUnique();
                    if (signIn == null || signIn.getTimeOut() != null) {
                        newLibrarySignIn(student);
                        signInUpdate();
                    } else {
                        signIn.setTimeOut(LocalDateTime.now());
                        signIn.save();
                        signOutUpdate();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                resetDisplay();
                queueList.remove(0);
            })).play();
        }
    }

    private void signInUpdate() {
        final DateFormat format = DateFormat.getInstance();
        final Calendar cal = Calendar.getInstance();

        if (!(markBookMap.containsValue("Invalid"))) {
            signInLabel.setText("Signed In at: " + format.format(cal.getTime()));
        } else {
            signInLabel.setText("Invalid entry at: " + format.format(cal.getTime()));
        }

    }

    private void signOutUpdate() {
        final DateFormat format = DateFormat.getInstance();
        final Calendar cal = Calendar.getInstance();
        signInLabel.setText("Signed Out at: " + format.format(cal.getTime()));
        welcomeLabel.setText("Goodbye!");
    }

    private void resetDisplay() {
        photoPane.setStyle("-fx-background-color: white;");
        nameLabel.setText("");
        signInLabel.setText("");
        welcomeLabel.setText("Welcome");
    }

    private void findUserInMarkBook() throws IOException {
        MarkBookReader.setCurrentUser(validInput);
        markBookMap = MarkBookReader.readMarkBook();
        firstNameList = new ArrayList<>(markBookMap.values());
        lastNameList = new ArrayList<>(markBookMap.keySet());
        firstName = firstNameList.get(0);
        lastName = lastNameList.get(0);

    }

    private Student newStudentEntry(String prFirstName, String prLastName, int prStudentID) {
        Student student = new Student();
        student.setFirstName(prFirstName);
        student.setLastName(prLastName);
        student.setStudentID(prStudentID);
        student.save();
        return student;
    }

    private void newLibrarySignIn(Student student) {
        SignIn signIn = new SignIn();
        student.signIns.add(signIn);
        signIn.setTimeIn(LocalDateTime.now());
        signIn.setStudent(student);
        signIn.save();
        student.save();
    }

    private void loadUserName() {
        nameLabel.setText(firstName + ", " + lastName);
    }

    public void setCurrentPeriod(Period inputedPeriod) {
        this.currentPeriod = inputedPeriod;
    }
}


