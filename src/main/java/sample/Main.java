package sample;

import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.avaje.agentloader.AgentLoader;

public class Main extends Application {

    public static Stage stage;
    public static Scene currentScene;
    public static boolean statusBool = false;

    public static void main(String[] args) throws Exception {
        if (!AgentLoader.loadAgentFromClasspath("avaje-ebeanorm-agent", "debug=1;packages=sample.models.*")) {
            System.out.println("avaje-ebeanorm-agent not found in classpath - not dynamically loaded");
        }
        eBeanInit();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle(Config.WINDOW_TITLE);
        Scene scene = new Scene(root, Config.PREF_WIDTH, Config.PREF_HEIGHT);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/MainCS.css").toExternalForm());
//        Platform.setImplicitExit(false);
//        primaryStage.setOnCloseRequest(Event::consume);
        primaryStage.getScene().getAccelerators().put(
                new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN),
                () -> Util.replaceSceneContent("password.fxml")
        );
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.centerOnScreen();
//        primaryStage.setResizable(false);
//        primaryStage.setFullScreen(true);
        Main.stage = primaryStage;
        currentScene = scene;
        //uncomment on final
        //primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    public static void eBeanInit(){
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName("sqlite");

        //Define datasource parameters
        DataSourceConfig sqliteDB = new DataSourceConfig();
        sqliteDB.setDriver("org.sqlite.JDBC");
        sqliteDB.setUsername("");
        sqliteDB.setPassword("");
        sqliteDB.setUrl("jdbc:sqlite:mydb.db");
        sqliteDB.setIsolationLevel(1);
        serverConfig.setDataSourceConfig(sqliteDB);

        // set DDL options...
        serverConfig.setDdlRun(true);
        serverConfig.setDefaultServer(true);

        //Specify jar to search for entity beans
        serverConfig.addPackage("sample.models");

        // create the EbeanServer instance
        EbeanServerFactory.create(serverConfig);
    }

    public static boolean getBoolean() {
        return statusBool;
    }
    public void setBoolean(boolean bool) {
        statusBool = bool;
    }
}
