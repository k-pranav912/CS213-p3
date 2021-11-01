package com.studentroster;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

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
