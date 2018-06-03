package com.dabek.jakub.bpptree;

import com.dabek.jakub.bpptree.clientserver.ClientController;
import com.dabek.jakub.bpptree.clientserver.ServerController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Scanner;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);

        BppTree<Integer> tree = new BppTree<>(Arrays.asList(1, 10, 20, 30, 40));
        System.out.println(tree);
        Scanner scanner = new Scanner(System.in);

//        while (scanner.hasNextInt()) {
//            int value = scanner.nextInt();
//            tree.add(value);
//            System.out.println(tree);
//        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        root.setPadding(new Insets(20, 20, 20, 20));
        root.setPrefWidth(200);
        root.setPrefHeight(100);
        RadioButton clientRadioButton = new RadioButton("Client");
        clientRadioButton.setSelected(true);
        RadioButton serverRadioButton = new RadioButton("Server");
        ToggleGroup group = new ToggleGroup();
        clientRadioButton.setToggleGroup(group);
        serverRadioButton.setToggleGroup(group);
        Button startButton = new Button("Start");
        startButton.setOnAction(event -> {
            if (group.getSelectedToggle() == serverRadioButton) {
                ServerController.create();
            } else {
                ClientController.create();
            }
            primaryStage.close();
        });
        startButton.setDefaultButton(true);
        HBox fuck = new HBox();
        fuck.getChildren().add(startButton);
        fuck.setAlignment(Pos.CENTER);
        root.getChildren().addAll(clientRadioButton, serverRadioButton, fuck);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Tree");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
