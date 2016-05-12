package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sample.models.Student;

import java.io.IOException;
import java.net.URL;
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
        DBToFileWriter.writeSignIns();
        DBToFileWriter.writeStudents();
        Platform.exit();
        System.exit(0);
    }
    @FXML
    public void goToApplication() {
        try {
            replaceSceneContent("sample.fxml");
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


}
