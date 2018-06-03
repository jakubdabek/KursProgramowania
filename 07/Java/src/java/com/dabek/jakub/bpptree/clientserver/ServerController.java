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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerController {

    static final int DEFAULT_PORT = 7171;

    @FXML
    private TextField portNumberTextField;
    @FXML
    private Button startButton;

    @FXML
    private TextArea outputTextField;
    @FXML
    private TextField commandTextField;

    private int port = DEFAULT_PORT;

    public static void create() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(ServerController.class.getClassLoader().getResource("fxml/server_view.fxml"));
            Parent root = loader.load();
            ServerController controller = loader.getController();
            stage.setTitle("Server");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event -> {
                controller.stopServer();
                stage.close();
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

    private ServerThread currentServerThread;

    private class ServerThread extends Thread {
        AtomicBoolean ready = new AtomicBoolean(false);
        volatile BufferedReader reader;
        volatile PrintWriter writer;
        volatile ServerSocket serverSocket;
        volatile Socket socket;

        @Override
        public void run() {
            logMessage("Opening connection...");
            try (ServerSocket serverSocket = this.serverSocket = new ServerSocket(port);
                 Socket socket = this.socket = serverSocket.accept();
                 BufferedReader reader = this.reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
                 PrintWriter writer = this.writer = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()))) {
                ready.set(true);
                String line = null;
                logMessage("Connection established\n");
                try {
                    while (!Thread.interrupted() && (line = reader.readLine()) != null) {
                        processCommand(line, true);
                    }
                    if (line == null)
                        logMessage("\nClient closed the connection");
                } catch (IOException e) {
                    logMessage("\nError occurred in the connection");
                }
            } catch (SocketException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                logMessage("\nError occurred while trying to connect");
                Platform.runLater(() -> Utility.createAlert(Alert.AlertType.ERROR, "\nError creating server", e.getMessage()).show());
            } finally {
                Platform.runLater(ServerController.this::stopServer);
            }
            System.err.println("Client closing");
        }

        private void sendMessage(String command) {
            if (ready.get() && !command.isEmpty()) {
                writer.println(command);
                writer.flush(); //TODO: non-blocking flush
            }
        }

        void processCommand(String command) {
            if (ready.get())
                processCommand(command, false);
        }

        private void processCommand(String command, boolean external) {
            logMessage("Command received: " + command);

        }

        private void logMessage(String message) {
            Platform.runLater(() -> {
                outputTextField.appendText(message + "\n");
            });
        }

        @Override
        public void interrupt() {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                    serverSocket = null;
                }
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
            } catch (IOException e) { e.printStackTrace(); }
            super.interrupt();
        }
    }

    @FXML
    private void processCommand() {
        if (currentServerThread != null) {
            currentServerThread.processCommand(commandTextField.getText());
            commandTextField.selectAll();
        }
    }

    private void changeDisabled(boolean serverStarted) {
        portNumberTextField.setDisable(serverStarted);
        startButton.setDisable(serverStarted);
        commandTextField.setDisable(!serverStarted);
        outputTextField.setDisable(!serverStarted);
        if (serverStarted)
            commandTextField.requestFocus();
        else
            portNumberTextField.requestFocus();
    }

    @FXML
    private void startServer() {
        if (checkPort()) {
            commandTextField.clear();
            outputTextField.clear();
            currentServerThread = new ServerThread();
            currentServerThread.start();
            changeDisabled(true);
        }
    }

    @FXML
    private void stopServer() {
        if (currentServerThread != null) {
            currentServerThread.interrupt();
            while (currentServerThread.isAlive()) {
                try {
                    currentServerThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            commandTextField.clear();
            currentServerThread = null;
        }
        changeDisabled(false);
    }

    public void newClientMenuItem_onAction(ActionEvent actionEvent) {
        ClientController.create();
    }

    public void newServerMenuItem_onAction(ActionEvent actionEvent) {
        ServerController.create();
    }

}
