package sample;

import com.avaje.ebean.Ebean;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import sample.models.SignIn;
import sample.models.Student;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
        initTimedTasks();
        setPeriod();
    }


    @FXML
    private void updateBarcode() {
            barcodeInput.positionCaret(0);
            input = barcodeInput.getText();
            System.out.println(input.length());
            checkBarcode(input);
            barcodeInput.clear();

    }

    @FXML
    private void setupComponents() {
        signInLabel.setText("");
        nameLabel.setText("");
        welcomeLabel.setFont(FONT);
        nameLabel.setFont(Font.font(16));
        Platform.runLater(() -> barcodeInput.requestFocus());
    }

    public void setPeriod(){
        LocalTime localTime = LocalTime.now();
        for (int i = 0; i < Period.values().length; i++) {
            Period period = Period.values()[i];
            if(localTime.isBefore(period.endTime)){
                setCurrentPeriod(period);
                break;
            }
        }
        if(currentPeriod == null) {
            setCurrentPeriod(Period.BEFORE_CLASS);
        }
        System.out.println(currentPeriod.endTime);
    }

    public void initTimedTasks(){
        //Recursive Tasks
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    setupTime();
                    openAdminCheck();
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Timeline timeline2;
        timeline2 = new Timeline(new KeyFrame(
                Duration.millis(5000),
                ae -> updateBarcode()));
        timeline2.setCycleCount(Animation.INDEFINITE);
        timeline2.play();

        Timeline periodChecker = new Timeline(new KeyFrame(
                Duration.millis(120000),
                ae -> setPeriod()));
        periodChecker.setCycleCount(Animation.INDEFINITE);
        periodChecker.play();

        Timeline checkSignIn = new Timeline(new KeyFrame(
                Duration.millis(9000),
                ae -> checkSignIns()));
        checkSignIn.setCycleCount(Animation.INDEFINITE);
        checkSignIn.play();
    }

    private void checkSignIns() {
        List<SignIn> all = SignIn.find.all();
        all.stream().filter(signIn1 -> signIn1.timeIn.toLocalTime().isAfter(currentPeriod.endTime)).forEach(signIn1 -> {
            signIn1.timeOut = LocalDateTime.from(LocalDate.from(currentPeriod.endTime));
            signIn1.wasManual = true;
            printSignIns();
        });
    }

    @FXML
    public void setupTime() {
        final DateFormat format = DateFormat.getInstance();
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            final Calendar cal = Calendar.getInstance();
            clock.setText(format.format(cal.getTime()));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    public void loadPhotoPane() {
        photoPane.setStyle("-fx-background-image: url(\"/studentphotos/" + validInput + ".JPG\");\n" +
                "-fx-background-size: contain;");
    }

    @FXML
    public void goToOptions() {
        try {
            replaceSceneContent("options.fxml");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Parent replaceSceneContent(String fxml) throws Exception {
        URL resource = this.getClass().getClassLoader().getResource(fxml);
        Parent page = FXMLLoader.load(resource, null, new JavaFXBuilderFactory());
        Scene scene = Main.stage.getScene();
        if (scene == null) {
            scene = new Scene(page, 700, 450);
            Main.stage.setScene(scene);
        } else {
            Main.stage.getScene().setRoot(page);
        }
        Main.stage.sizeToScene();
        return page;
    }

    private String checkBarcode(String studentInput) {
        if (isInteger(studentInput, 10)) {
            if ((studentInput.length() != 9)) {
                barcodeInput.clear();
                return invalidInput;
            } else {
                System.out.println("Good Barcode: " + studentInput);
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

    public void openAdminCheck() {
        if (Main.getBoolean()) {
            try {
                replaceSceneContent("password.fxml");
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void handleUserEntry() {
        ArrayList<String> queueList = new ArrayList<>();
        queueList.add(0, validInput);
        System.out.println(queueList.toString());
        if (!(queueList.size() >= 2)) {
            loadPhotoPane();
            try {
                findUserInMarkBook();
                loadUserName();

                Ebean.execute(()->{
                    Student student = Student.find.where().eq("studentID", validInput).findUnique();
                    if(student == null){
                        student = newStudentEntry(firstName,lastName,Integer.parseInt(validInput));
                    }
                    SignIn signIn = SignIn.find.where().eq("student.id", student.id).orderBy("timeIn desc").setMaxRows(1).findUnique();
                    if(signIn == null || signIn.timeOut != null){
                        newLibrarySignIn(student);
                        signInUpdate();
                    } else{
                        signIn.timeOut = LocalDateTime.now();
                        signIn.save();
                        signIn.student.save();
                        signOutUpdate();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            Timeline displayTimeOut = new Timeline(new KeyFrame(
                    Duration.millis(5000),
                    ae -> {
                        resetDisplay();
                        queueList.remove(0);
                    }));
            displayTimeOut.play();
        } else {
            queueList.remove(0);
        }
    }

    private void signInUpdate() {
        final DateFormat format = DateFormat.getInstance();
        final Calendar cal = Calendar.getInstance();

        if(!(markBookMap.containsValue("Invalid"))) {
            signInLabel.setText("Signed In at: "+ format.format(cal.getTime()));
        } else {
            signInLabel.setText("Invalid entry at: "+ format.format(cal.getTime()));
        }

    }
    private void signOutUpdate(){
        final DateFormat format = DateFormat.getInstance();
        final Calendar cal = Calendar.getInstance();
            signInLabel.setText("Signed Out at: "+ format.format(cal.getTime()));
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

    private Student newStudentEntry(String prFirstName,String prLastName, int prStudentID){
        Student student = new Student();
        student.firstName = prFirstName;
        student.lastName = prLastName;
        student.studentID = prStudentID;
        student.save();
        return student;
    }
    private void newLibrarySignIn(Student student){
        SignIn signIn = new SignIn();
        signIn.timeIn = LocalDateTime.now();
        signIn.student = student;
        signIn.save();
    }
    private void printStudents(){
        List<Student> all = Student.find.all();
        for (Student student1 : all) {
            System.out.println(student1.firstName);
            System.out.println(student1.lastName);
            System.out.println(student1.id);
            System.out.println(student1.studentID);
        }
    }

    private void printSignIns() {
    List<SignIn> all = SignIn.find.all();
        for(SignIn signin1 : all){
            System.out.println(signin1.id);
            System.out.println(signin1.student);
            System.out.println(signin1.timeIn);
            System.out.println(signin1.timeOut);
            System.out.println(signin1.wasManual);
        }
    }


    private void loadUserName(){
        nameLabel.setText(firstName + ", " + lastName);
    }


    public Period getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(Period inputedPeriod) {
        this.currentPeriod = inputedPeriod;
    }
}


