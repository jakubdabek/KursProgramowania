package com.dabek.jakub.vectorpaint;

import com.dabek.jakub.vectorpaint.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main.fxml"));
        Parent root = loader.load();
        root.setOnKeyPressed(loader.<Controller>getController().escapeKeyEventHandler);

        primaryStage.setTitle("VectorPaint");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        BorderPane borderPane = new BorderPane();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
