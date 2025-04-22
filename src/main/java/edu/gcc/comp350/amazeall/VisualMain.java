package edu.gcc.comp350.amazeall;

import edu.gcc.comp350.amazeall.gui.*;

import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class VisualMain extends Application {
    private Scene scene;
    private Driver driver;
    GUISceneSingleton guiDriverInstance;

    @Override
    public void start(Stage primaryStage) throws Exception {
        driver = new Driver();

        //Create and set up the GUISceneSingleton.
        guiDriverInstance = GUISceneSingleton.getInstance();
        guiDriverInstance.setRefs(this, driver);

        //Set the scene and start the window.
        guiDriverInstance.switchScene("GUILandingPage");
        scene = new Scene(guiDriverInstance.getRoot(), 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Amaze-All Course Scheduler");
        primaryStage.show();
    }

    public void setRoot(AnchorPane root) {
        scene.setRoot(root);
    }

    /*
    NOTE: You cannot launch the visual application in the same way as a terminal app.
    Instead, go into the Gradle menu on the right, and click: Tasks/application/run
    Alternatively, open terminal and type ./gradlew run
     */
    public static void main(String[] args) {
        launch(args);
    }
}
