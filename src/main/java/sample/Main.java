package sample;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.avaje.agentloader.AgentLoader;
import sample.models.Student;

import java.util.Date;
import java.util.List;


public class Main extends Application {

    private static final int PREF_HEIGHT = 720;
    private static final int PREF_WIDTH = 1024;

    private Parent root = null;
    private static Main instance;
    public static Stage stage;
    public static Scene currentScene;
    public static boolean statusBool = false;
    private ObservableList<ObservableList> data;
    private TableView tableview;


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
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setTitle("Library Sign-In");
        Scene scene = new Scene(root, PREF_WIDTH, PREF_HEIGHT);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/MainCS.css").toExternalForm());
        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(Event::consume);
        primaryStage.getScene().getAccelerators().put(
                new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN),
                () -> {
                    setBoolean(true);
                    if(statusBool) {
                        Timeline timeline = new Timeline(new KeyFrame(
                                Duration.millis(3000),
                                ae -> setBoolean(false)));
                        timeline.play();
                    }
                }
        );
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        Main.instance = this;
        Main.stage = primaryStage;
        currentScene = scene;
        //uncomment on final
        //primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
        test();
    }

    public static void test(){
        // Create a new student
        Student student = new Student();
        student.firstName = "Ian";
        student.lastName = "Willis";
        student.studentID = 999999999;
        student.save();

        // Create a new signIn
       /* SignIn signIn = new SignIn();
        signIn.timeIn = new LocalDateTime();
        signIn.timeOut = new LocalDateTime();
        signIn.student = student;
        signIn.save();*/

        // Query all students
        List<Student> all = Student.find.all();
        for (Student student1 : all) {
            System.out.println(student1.firstName);
            System.out.println(student1.lastName);
            System.out.println(student1.id);
            System.out.println(student1.studentID);
        }
        // Query students by first name
        Student unique = Student.find.where().eq("firstName", "Max").findUnique();
        // Find student by who have signed in before now and have not signed out
        List<Student> kek = Student.find.where()
                .le("signIns.timeIn", new Date())
                .eq("signIns.timeOut", null)
                .findList();
    }

    public static void eBeanInit(){
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName("h2");

        //Define datasource parameters
        DataSourceConfig h2Db = new DataSourceConfig();
        h2Db.setDriver("org.h2.Driver");
        h2Db.setUsername("username");
        h2Db.setPassword("");
        h2Db.setUrl("jdbc:h2:./h2database;CREATE=true;AUTO_RECONNECT=TRUE;DB_CLOSE_DELAY=-1");
        h2Db.setHeartbeatSql("select 1");
        serverConfig.setDataSourceConfig(h2Db);

        // set DDL options...
        serverConfig.setDdlGenerate(true);
        serverConfig.setDdlRun(true);

        serverConfig.setDefaultServer(true);

        //Setup derby specific identity 'stuff'.
//        DatabasePlatform dbPlatform = new DatabasePlatform();
//        dbPlatform.getDbIdentity().setIdType(IdType.IDENTITY);
//        dbPlatform.getDbIdentity().setSupportsGetGeneratedKeys(true);
//        dbPlatform.getDbIdentity().setSupportsSequence(false);
//        dbPlatform.getDbIdentity().setSupportsIdentity(true);
//        dbPlatform.createDdlHandler(serverConfig);
//        serverConfig.setDatabasePlatform(dbPlatform);

        //Specify jar to search for entity beans
        serverConfig.addPackage("sample.models");

        // create the EbeanServer instance

        EbeanServer server = EbeanServerFactory.create(serverConfig);
    }

    public static Main getInstance() {
        return instance;
    }
    public static boolean getBoolean() {
        return statusBool;
    }
    public void setBoolean(boolean bool) {
        statusBool = bool;
    }
    public static Scene getScene(){return currentScene;}
    public static Stage getStage(){return stage;}
}
