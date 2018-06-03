package com.dabek.jakub.bpptree.clientserver;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

import static com.dabek.jakub.bpptree.clientserver.ServerController.DEFAULT_PORT;

public class ClientController {

    @FXML
    private TextField portNumberTextField;
    @FXML
    private TextField hostAddressTextField;
    @FXML
    private Button startButton;

    @FXML
    private TextArea outputTextField;
    @FXML
    private TextField commandTextField;
    private String hostAddress;
    private int port = DEFAULT_PORT;

    public static void create() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(ClientController.class.getClassLoader().getResource("fxml/client_view.fxml"));
            Parent root = loader.load();
            ClientController controller = loader.getController();
            stage.setTitle("Client");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event -> {
                controller.stopClient();
                Platform.exit();
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        portNumberTextField.setText(Integer.toString(DEFAULT_PORT));
        portNumberTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            System.err.println(newValue);
            if (!newValue) {
                checkPort();
            } else {
                portNumberTextField.selectAll();
            }
        });
    }

    @FXML
    private boolean checkPort() {
        String text = portNumberTextField.getText();
        boolean good = true;
        if (text.isEmpty()) {
            port = DEFAULT_PORT;
        } else {
            try {
                port = Integer.parseInt(text);
                if (port < 0) {
                    port = DEFAULT_PORT;
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                good = false;
            }
        }

        portNumberTextField.setText(Integer.toString(port));
        System.err.println(port);
        System.err.println(portNumberTextField.getText());
        if (!good) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Port must be a positive integer. Leave empty for default (" + DEFAULT_PORT + ")");
            alert.setTitle("Wrong port number");
            alert.showAndWait();
        }
        return good;
    }

    private class ClientThread extends Thread {

        private Socket socket;

        public ClientThread() throws IOException {
            socket = new Socket(hostAddress, port);
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {

            }
        }
    }

    private ClientThread currentClientThread;

    @FXML
    private void startClient() {
        System.err.println("Begin start");
        if (checkPort()) {
            System.err.println("Start");
            try {
                currentClientThread = new ClientThread();
                currentClientThread.start();
                portNumberTextField.setDisable(true);
                hostAddressTextField.setDisable(true);
                startButton.setDisable(true);
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.setTitle("Error creating client socket");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void stopClient() {
        if (currentClientThread != null) {
            currentClientThread.interrupt();
            while (currentClientThread.isAlive()) {
                try {
                    currentClientThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void newClientMenuItem_onAction(ActionEvent actionEvent) {
        ClientController.create();
    }

    @FXML
    private void newServerMenuItem_onAction(ActionEvent actionEvent) {
        ServerController.create();
    }
}
