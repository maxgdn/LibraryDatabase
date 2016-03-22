package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Max on 3/5/2016.
 */
public class PassPage {
    @FXML
    Button returnButton;
    @FXML
    Button submitButton;
    @FXML
    PasswordField passField;

    @FXML
    public void initialize(){
        Platform.runLater(() -> passField.requestFocus());

    }

    @FXML
    public void checkPassword(){
        String input = passField.getText().toLowerCase();
        if(input.equals(Config.PASSWORD)){
            goToOptions();
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

    @FXML
    public void goToApplication() {
        try {
            Util.replaceSceneContent("sample.fxml");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
