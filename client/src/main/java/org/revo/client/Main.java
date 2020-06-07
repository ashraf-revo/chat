package org.revo.client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.revo.client.controller.Controller;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sample.fxml"));

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(loader.load(), 620, 450));
        primaryStage.setOnCloseRequest(event -> ((Controller) loader.getController()).close());
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
