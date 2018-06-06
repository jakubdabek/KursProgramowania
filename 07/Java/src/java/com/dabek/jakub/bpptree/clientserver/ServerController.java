package com.dabek.jakub.bpptree.clientserver;

import com.dabek.jakub.bpptree.BppTree;
import com.dabek.jakub.bpptree.Utility;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ServerController {

    static final int DEFAULT_PORT = 7171;
    private static final List<String> VALID_COMMANDS = Arrays.asList(
            "print",
            "clear",
            "exit",
            "contains",
            "add",
            "remove"
    );

    private static final List<String> NO_ARGUMENT_COMMANDS = VALID_COMMANDS.subList(0, 3);

    @FXML
    private TextField portNumberTextField;
    @FXML
    private ComboBox typeComboBox;
    @FXML
    private Spinner capacitySpinner;
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

    private ServerThread<?> currentServerThread;

    private class ServerThread<T extends Comparable<T>> extends Thread {
        AtomicBoolean ready = new AtomicBoolean(false);
        volatile BufferedReader reader;
        volatile PrintWriter writer;
        volatile ServerSocket serverSocket;
        volatile Socket socket;

        BppTree<T> tree;
        Function<String, T> converter;

        ServerThread(Function<String, T> converter) {
            tree = new BppTree<>((Integer)capacitySpinner.getValue());
            this.converter = converter;
        }

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

        private void sendMessage(String message) {
            sendMessage(message, true);
        }

        private void sendMessage(String message, boolean flush) {
            if (ready.get() && !message.isEmpty()) {
                writer.println(message);
                if (flush)
                    writer.flush(); //TODO: non-blocking flush
            }
        }

        void processCommand(String command) {
            if (ready.get())
                processCommand(command, false);
        }

        private String commandAwaitingConfirmation = null;

        private void processCommand(String line, boolean external) {
            try {
                String command;
                Consumer<String> callback = external ? this::sendMessage : this::logMessage;
                String[] words = line.split("\\s+");
                System.err.println(Arrays.toString(words));
                if (commandAwaitingConfirmation == null) {
                    logMessage("Command received: " + line);
                    if (words.length == 0) {
                        logMessage("Empty command, doing nothing");
                        callback.accept("Invalid empty command\n" + "Enter one of the following: " + VALID_COMMANDS);
                        return;
                    }
                    if (!VALID_COMMANDS.contains(words[0])) {
                        logMessage("Unrecognized command, doing nothing");
                        callback.accept("Unrecognized command\n" + "Enter one of the following: " + VALID_COMMANDS);
                        return;
                    }
                    if (words.length == 1 && !NO_ARGUMENT_COMMANDS.contains(words[0])) {
                        logMessage("No arguments, doing nothing");
                        callback.accept("Command should have arguments\n" + "Enter them after the command, separated by spaces e.g. \"insert a b c\"");
                        return;
                    }

                    command = words[0];
                } else {
                    command = commandAwaitingConfirmation;
                }

                List<T> arguments = Arrays.asList(words).subList(1, words.length).stream().map(converter).collect(Collectors.toList());

                switch (command) {
                    case "print":
                        String stringRepresentation = tree.toString();
                        logMessage("Printing tree");
                        logMessage(stringRepresentation);
                        if (external)
                            callback.accept(stringRepresentation);
                        return;
                    case "clear":
                        if (commandAwaitingConfirmation == null) {
                            logMessage("Clear requested, awaiting confirmation");
                            callback.accept("Enter \"Confirm\" to clear tree");
                            commandAwaitingConfirmation = "clear";
                        } else if (line.equals("Confirm")) {
                            tree.clear();
                            logMessage("Tree cleared");
                            if (external)
                                callback.accept("Tree cleared");
                            commandAwaitingConfirmation = null;
                        } else {
                            logMessage("Operation canceled");
                            commandAwaitingConfirmation = null;
                        }
                        return;
                    case "exit":
                        if (commandAwaitingConfirmation == null) {
                            logMessage("Exit requested, awaiting confirmation");
                            callback.accept("Enter \"Confirm\" to exit");
                            commandAwaitingConfirmation = "exit";
                        } else if (line.equals("Confirm")) {
                            logMessage("Exiting...");
                            Platform.runLater(ServerController.this::stopServer);
                            commandAwaitingConfirmation = null;
                        } else {
                            logMessage("Operation canceled");
                            commandAwaitingConfirmation = null;
                        }
                        return;
                }
                for (T argument : arguments) {
                    switch (command) {
                        case "contains":
                            logMessage("Checking whether " + argument + " exists in the tree");
                            if (tree.contains(argument)) {
                                callback.accept(argument + " exists within the tree");
                            } else {
                                callback.accept(argument + " doesn't exist within the tree");
                            }
                            break;
                        case "add":
                            logMessage("Adding " + argument);
                            if (tree.add(argument)) {
                                callback.accept(argument + " added");
                            } else {
                                callback.accept("Could not add " + argument);
                            }
                            break;
                        case "remove":
                            logMessage("Removing " + argument);
                            if (tree.add(argument)) {
                                callback.accept(argument + " removed");
                            } else {
                                callback.accept("Could not remove " + argument);
                            }
                            break;
                    }
                }
            } catch (Exception e) {
                logMessage("Error occurred while processing command");
                logMessage(e.toString());
                e.printStackTrace();
                if (external) {
                    sendMessage("Error occurred while processing command");
                }
            }
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
        typeComboBox.setDisable(serverStarted);
        capacitySpinner.setDisable(serverStarted);
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
            switch ((String)typeComboBox.getValue()) {
                case "Integer":
                    currentServerThread = new ServerThread<>(Integer::parseInt);
                    break;
                case "Double":
                    currentServerThread = new ServerThread<>(Double::parseDouble);
                    break;
                case "String":
                    currentServerThread = new ServerThread<>(Function.identity());
                    break;
                default:
                    Utility.createAlert(Alert.AlertType.ERROR, "What", "Wrong type. Contact FBI and tell them how you did that.").showAndWait();
                    return;
            }
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
