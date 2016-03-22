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

    private String pass = "password";

    @FXML
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run(){ passField.requestFocus();
            }
        });

    }

    @FXML
    public void checkPassword(){
        String input = passField.getText().toLowerCase();
        if(input.equals(pass)){
            goToOptions();
        }
    }

    @FXML
    public void goToOptions() {
        try {
            replaceSceneContent("options.fxml");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
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
