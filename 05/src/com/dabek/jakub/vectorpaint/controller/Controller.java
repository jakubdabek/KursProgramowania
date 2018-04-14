package com.dabek.jakub.vectorpaint.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Window;

public class Controller {
    public ToolBar toolbar;
    public Button paintButton;
    public ImageView paintBucketImageView;
    public ImageView paintImageView;
    public ColorPicker colorPicker;
    public HBox statusBar;
    public Pane drawingArea;
    public Label infoLabel;
    public Circle circleXD;

    @FXML
    private void what_onAction(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, "XDDDDDD", ButtonType.OK);
        a.showAndWait();
    }

    @FXML
    private void rectangleButton_onAction(ActionEvent actionEvent) {
        showAlert(Alert.AlertType.INFORMATION, ((Button)actionEvent.getSource()).getScene().getWindow(),
                "Rectangle", "You clicked the rectangle button xdddddddd");
    }
    @FXML
    private void circleButton_onAction(ActionEvent actionEvent) {
        showAlert(Alert.AlertType.INFORMATION, ((Button)actionEvent.getSource()).getScene().getWindow(),
                "Rectangle", "You clicked the circle button xdddddddd");
    }
    @FXML
    private void polygonButton_onAction(ActionEvent actionEvent) {
        showAlert(Alert.AlertType.INFORMATION, ((Button)actionEvent.getSource()).getScene().getWindow(),
                "Rectangle", "You clicked the polygon button xdddddddd");
    }


    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    @FXML
    private void colorPicker_onAction(ActionEvent actionEvent) {

    }

    @FXML
    private void moveButton_onAction(ActionEvent actionEvent) {

    }

    @FXML
    private void paintButton_onAction(ActionEvent actionEvent) {

    }

    @FXML
    private void drawingArea_onMouseClicked(MouseEvent mouseEvent) {
        //drawingArea.getChildren().add(new Circle(mouseEvent.getX(), mouseEvent.getY(), 100));
    }

    @FXML
    private void drawingArea_onMouseDragged(MouseEvent mouseEvent) {
        updateBasicInfo(mouseEvent);
    }

    @FXML
    private void updateBasicInfo(MouseEvent mouseEvent) {
        long x = Math.round(mouseEvent.getX());
        long y = Math.round(mouseEvent.getY());
        Pane source = (Pane)mouseEvent.getSource();
        if(x >= 0 && x <= source.getWidth() && y >= 0 && y <= source.getHeight()) {
            setBasicInfo(x + ", " + y);
        } else {
            setBasicInfo("");
        }
        paintButton.getInsets();
    }

    public StringProperty basicInfoProperty() {
        return basicInfo;
    }

    private StringProperty basicInfo = new SimpleStringProperty("");

    private final void setBasicInfo(String value) { basicInfo.setValue(value); }
    public final String getBasicInfo() { return basicInfo.get(); }

    public void circleXD_onMouseClicked(MouseEvent mouseEvent) {
        showAlert(Alert.AlertType.INFORMATION, ((Circle)mouseEvent.getSource()).getScene().getWindow(), "XD", "XD");
    }
}
