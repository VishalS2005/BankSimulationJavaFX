package com.example.project3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class runs the GUI.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class Main extends Application {

    /**
     * Runs the Application.
     *
     * @param stage represents a Stage object
     * @throws IOException if I/O error occurs
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Transaction Manager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the Application.
     *
     * @param args takes command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}