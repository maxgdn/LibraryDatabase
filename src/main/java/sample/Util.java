package sample;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public class Util {
    public static Parent replaceSceneContent(String fxml)  {
        URL resource = Util.class.getClassLoader().getResource(fxml);
        Parent page = null;
        try {
            page = FXMLLoader.load(resource, null, new JavaFXBuilderFactory());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
