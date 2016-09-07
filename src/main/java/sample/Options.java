package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sample.models.Student;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Max on 2/23/2016.
 */
public class Options {
    //Options Resources
    @FXML
    Button closeButton;
    @FXML
    Button returnButton;
    @FXML
    Label optionsLabel;
    @FXML
    TextField signInOutSearch;
    @FXML
    TextField studentSearch;
    @FXML
    Button signInOutButton;
    @FXML
    Button studentButton;
    @FXML
    TableView studentTable;
    @FXML
    TableView signInOutTable;

    @FXML
    public void initialize(){
    setupVisuals();
    }

    @FXML
    private void setupVisuals() {
    optionsLabel.setFont(Controller.FONT);
    }

    private void displayStudents(){
        
    }
    private void displayTimeLogs(){

    }

    @FXML
    private void searchStudentDB(){
        Student student = Student.find.where().eq("lastName", studentSearch.getText()).findUnique();
        student.getFirstName();
        student.getLastName();
        student.getStudentID();
    }
    @FXML
    public void closeApplication() throws IOException {
        DBToFileWriter.terminateAllLiveClients();
        DBToFileWriter.initFolder();
        DBToFileWriter.writeSignIns();
        DBToFileWriter.writeStudents();
        Platform.exit();
        System.exit(0);
    }
    @FXML
    public void goToApplication() {
        try {
            Util.replaceSceneContent("sample.fxml");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
