package com.dabek.jakub.bpptree;

import javafx.scene.control.Alert;

public class Utility {
    public static Alert createAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        return alert;
    }
}
