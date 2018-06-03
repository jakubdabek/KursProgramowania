package com.dabek.jakub.bpptree.clientserver;

import com.dabek.jakub.bpptree.Utility;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

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
        if (!good) {
            Utility.createAlert(
                    Alert.AlertType.ERROR,
                    "Wrong port number",
                    "Port must be a positive integer. Leave empty for default (" + DEFAULT_PORT + ")"
            ).showAndWait();
        }
        return good;
    }

    private ClientThread currentClientThread;

    private class ClientThread extends Thread {
        AtomicBoolean ready = new AtomicBoolean(false);
        volatile BufferedReader reader;
        volatile PrintWriter writer;

        @Override
        public void run() {
            messageReceived("Opening connection...\n");
            try (Socket socket = new Socket(hostAddress, port);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
                 PrintWriter writer = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()))) {
                this.reader = reader;
                this.writer = writer;
                ready.set(true);
                String line = null;
                messageReceived("Connection established\n");
                try {
                    while (!Thread.interrupted() && (line = reader.readLine()) != null) {
                        messageReceived(line + "\n");
                    }
                    if (line == null)
                        messageReceived("Server closed the connection");
                } catch (IOException e) {
                    messageReceived("Error occurred in the connection");
                }
            } catch (Exception e) {
                messageReceived("Error occurred while trying to connect\n");
                Platform.runLater(() -> Utility.createAlert(Alert.AlertType.ERROR, "Error creating client", e.getMessage()).show());
            } finally {
                Platform.runLater(ClientController.this::stopClient);
            }
            System.err.println("Client closing");
        }

        public synchronized void sendCommand(String command) {
            if (ready.get() && !command.isEmpty()) {
                writer.println(command);
                writer.flush();
            }
        }

        private void messageReceived(String message) {
            Platform.runLater(() -> {
                outputTextField.appendText(message);
            });
        }
    }

    @FXML
    private void sendCommand() {
        if (currentClientThread != null) {
            currentClientThread.sendCommand(commandTextField.getText());
            commandTextField.selectAll();
        }
    }

    private void changeDisabled(boolean clientStarted) {
        portNumberTextField.setDisable(clientStarted);
        hostAddressTextField.setDisable(clientStarted);
        startButton.setDisable(clientStarted);
        commandTextField.setDisable(!clientStarted);
        outputTextField.setDisable(!clientStarted);
        if (clientStarted)
            commandTextField.requestFocus();
        else
            portNumberTextField.requestFocus();
    }

    @FXML
    private void startClient() {
        if (checkPort()) {
            commandTextField.clear();
            outputTextField.clear();
            currentClientThread = new ClientThread();
            currentClientThread.start();
            changeDisabled(true);
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
            commandTextField.clear();
            currentClientThread = null;
        }
        changeDisabled(false);
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
