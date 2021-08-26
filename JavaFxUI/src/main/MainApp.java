package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("main.fxml");
        loader.setLocation(mainFXML);
        BorderPane root = loader.load();

        primaryStage.setTitle("Evolutionary system - javaFX");
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        MainController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.show();
    }
}
