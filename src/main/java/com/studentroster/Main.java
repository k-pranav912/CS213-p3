package com.studentroster;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The Main class, which handles setting up and running the GUI
 * @author Neel Prabhu, Saipranav Kalapala
 */
public class Main extends Application {

    /**
     * Calls launch(args) to launch the GUI
     * @param args Arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the GUI, sets up the scene and stage, and loads the FXML file
     * @param primaryStage Stage to be displayed to the user
     * @throws IOException Errors from GUI I/O
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 550);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Tuition Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
