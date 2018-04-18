package com.dabek.jakub.vectorpaint.controller;

import com.dabek.jakub.vectorpaint.model.ShapeOutline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.concurrent.Callable;


public class Controller {

    /**
     * Represents the current mode, dependent on the currently selected button
     */
    public enum PaintMode {
        MOVE, FILL, RECTANGLE, CIRCLE, POLYGON
    }

    @FXML
    private BorderPane borderPane;
    @FXML
    private ToggleGroup toolbarToggleGroup;
    @FXML
    private ImageView paintBucketImageView;
    @FXML
    private ImageView paintImageView;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    public Pane drawingArea;

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(drawingArea.getScene().getWindow());
        alert.show();
    }

    private ShapeOutline currentOutline = null;


    /**
     * Instances of this class are to be added to each shape drawn as event handler for mouse click
     * It handles selection and coloring
     */
    public class ShapeOnMouseClickHandler implements EventHandler<MouseEvent> {

        final Shape shape;

        public ShapeOnMouseClickHandler(Shape shape) {
            this.shape = shape;
        }

        @Override
        public void handle(MouseEvent event) {
            switch (getPaintMode()) {
                case MOVE:
                    if (currentOutline != null) {
                        drawingArea.getChildren().remove(currentOutline);
                    }
                    currentOutline = new ShapeOutline(shape);
                    ShapeInfo shapeInfo = new ShapeInfo(shape);
                    additionalInfoProperty().bind(
                            Bindings.createStringBinding(
                                    shapeInfo,
                                    shape.boundsInParentProperty()
                            )
                    );

                    drawingArea.getChildren().add(currentOutline);
                    event.consume();
                    break;

                case FILL:
                    shape.setFill(colorPicker.getValue());
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * Class that allows display of shape info
     */
    private static class ShapeInfo implements Callable<String> {

        private final Shape shape;

        ShapeInfo(Shape shape) { this.shape = shape; }

        /**
         * @return String representation of {@code shape} given during {@link ShapeInfo#ShapeInfo(Shape) construction}
         */
        @Override
        public String call() {
            StringBuilder sb = new StringBuilder();
            Bounds bounds = shape.getBoundsInParent();
            if(shape instanceof Rectangle) {
                Rectangle rectangle = (Rectangle)shape;
                sb.append("Rectangle(");
                sb.append("position: (");
                sb.append(Math.round(bounds.getMinX())); sb.append(", "); sb.append(Math.round(bounds.getMinY()));
                sb.append(") size: ");
                sb.append(Math.round(bounds.getWidth())); sb.append("x"); sb.append(Math.round(bounds.getHeight()));
                sb.append(")");
            } else if(shape instanceof Circle) {
                Circle circle = (Circle)shape;
                sb.append("Circle(");
                long radius = Math.round(bounds.getWidth() / 2.0);
                sb.append("center position: (");
                sb.append(Math.round(bounds.getMinX() + radius)); sb.append(", "); sb.append(Math.round(bounds.getMinY() + radius));
                sb.append(") radius: ");
                sb.append(radius);
                sb.append(")");
            } else if(shape instanceof Polygon) {
                Polygon polygon = (Polygon)shape;
                sb.append("Polygon(");
                sb.append("position: (");
                sb.append(Math.round(bounds.getMinX())); sb.append(", "); sb.append(Math.round(bounds.getMinY()));
                sb.append(") size: ");
                sb.append(Math.round(bounds.getWidth())); sb.append("x"); sb.append(Math.round(bounds.getHeight()));
                sb.append(")");
            }

            return sb.toString();
        }
    }

    private PaintMode getPaintMode() { return (PaintMode)toolbarToggleGroup.getSelectedToggle().getUserData(); }

    /**
     * Handler for onMouseClick for the drawing area
     */
    @FXML
    private void beginDraw(MouseEvent mouseEvent) {
        switch (getPaintMode()) {
            case RECTANGLE:
                ShapeCreationController.beginRectangle(this, mouseEvent, colorPicker.getValue());
                break;
            case CIRCLE:
                ShapeCreationController.beginCircle(this, mouseEvent, colorPicker.getValue());
                break;
            case POLYGON:
                ShapeCreationController.beginPolygon(this, mouseEvent, colorPicker.getValue());
                break;
            default:
                removeOutline();
                break;
        }
    }

    private void removeOutline() {
        if(currentOutline != null)
            drawingArea.getChildren().remove(currentOutline);
    }

    /**
     * Responsible for updating information about the cursors position on the info bar
     */
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
    }

    private StringProperty basicInfo = new SimpleStringProperty("");
    public StringProperty basicInfoProperty() {
        return basicInfo;
    }
    private void setBasicInfo(String value) { basicInfo.setValue(value); }
    public final String getBasicInfo() { return basicInfo.get(); }

    private StringProperty additionalInfo = new SimpleStringProperty("");
    public StringProperty additionalInfoProperty() {
        return additionalInfo;
    }
    private void setAdditionalInfo(String value) { additionalInfo.setValue(value); }
    public final String getAdditionalInfo() { return additionalInfo.get(); }

    @FXML
    public void initialize() {
        toolbarToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) oldValue.setSelected(true);
            removeOutline();
            ShapeCreationController.cancelDrawing(drawingArea);
        });
        drawingArea.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
    }

    /**
     * Event handler for cancelling drawing and selection
     */
    public EventHandler<KeyEvent> escapeKeyEventHandler = event -> {
        if (event.getCode() == KeyCode.ESCAPE) {
            ShapeCreationController.cancelDrawing(drawingArea);
            removeOutline();
        }
    };


    /**
     * Clears the drawing field
     */
    public void menu_new_onAction() {
        drawingArea.getChildren().clear();
    }

    /**
     * Exits the application
     */
    public void menu_exit_onAction() {
        Platform.exit();
    }

    /**
     * Displays information about the application
     */
    public void menu_about_onAction() {
        showAlert(Alert.AlertType.INFORMATION, "VectorPaint",
                "A simple application for vector graphics\n" +
                        "Creator: Jakub Dąbek");
    }

    /**
     * Opens a file dialog and loads the chosen file to the drawing area
     */
    public void menu_open_onAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FXML files", "*.fxml"));
        File file = fileChooser.showOpenDialog(drawingArea.getScene().getWindow());
        if (file != null) {
            try {
                Pane newDrawingArea = FXMLLoader.load(file.toURI().toURL());
                newDrawingArea.setOnMouseClicked(this::beginDraw);
                newDrawingArea.setOnMouseMoved(this::updateBasicInfo);
                newDrawingArea.setOnMouseDragged(this::updateBasicInfo);
                newDrawingArea.setOnMouseExited(this::updateBasicInfo);
                Rectangle clipRectangle = new Rectangle();
                clipRectangle.widthProperty().bind(newDrawingArea.widthProperty());
                clipRectangle.heightProperty().bind(newDrawingArea.heightProperty());
                newDrawingArea.setClip(clipRectangle);
                newDrawingArea.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));

                for (Node child : newDrawingArea.getChildren()) {
                    child.setOnMouseClicked(new ShapeOnMouseClickHandler((Shape) child));
                }

                drawingArea = newDrawingArea;
                borderPane.setCenter(drawingArea);

            } catch (IOException ex) {
                System.err.print(Arrays.toString(ex.getStackTrace()));
                showAlert(Alert.AlertType.ERROR, "Error loading file", "An IO error has occurred:\n" + ex.getMessage());
            } catch (ClassCastException ex) {
                System.err.print(Arrays.toString(ex.getStackTrace()));
                showAlert(Alert.AlertType.ERROR, "Error loading file", "The given FXML file didn't contain correct data:\n" + ex.getMessage());
            }
        }
    }

    /**
     * Opens a file dialog and saves the current state of the drawing area to a file
     */
    public void menu_save_onAction() {
        String serializedPane = serializePane(drawingArea);
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FXML files", "*.fxml"));
        File file = fileChooser.showSaveDialog(drawingArea.getScene().getWindow());
        if (file != null) {
            try (PrintWriter out = new PrintWriter(file)) {
                out.print(serializedPane);
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Error loading file", "An IO error has occurred:\n" + ex.getMessage());
            }
        }
    }

    /**
     * Serializes a {@link Pane} with {@link Shape}s
     * Currently supports {@link Rectangle}, {@link Circle}, {@link Polygon}
     * @param pane {@code Pane} to be serialized
     * @return serialized {@code pane's} state in a {@link String}
     */
    private static String serializePane(Pane pane) {
        StringBuilder sb = new StringBuilder("<?import javafx.scene.control.*?>\n" +
                "<?import javafx.scene.layout.Pane?>\n" +
                "<?import javafx.scene.paint.Color?>\n" +
                "<?import javafx.scene.shape.*?>\n" +
                "<?import java.lang.*?>\n"
        );
        sb.append("<Pane prefWidth=\"600\" prefHeight=\"800\" " +
                "xmlns:fx=\"http://javafx.com/fxml/1\" xmlns=\"http://javafx.com/javafx/8.0.121\">\n");
        for(Node node : pane.getChildren()) {
            sb.append("    ");
            if (node instanceof Rectangle) {
                Rectangle rectangle = (Rectangle)node;
                sb.append("<Rectangle");
                sb.append(" width=\""); sb.append(rectangle.getWidth()); sb.append("\"");
                sb.append(" height=\""); sb.append(rectangle.getHeight()); sb.append("\"");
                sb.append(" fill=\""); sb.append(rectangle.getFill().toString()); sb.append("\"");
                sb.append(" x=\""); sb.append(rectangle.getX()); sb.append("\"");
                sb.append(" y=\""); sb.append(rectangle.getY()); sb.append("\"");
                sb.append(" translateX=\""); sb.append(rectangle.getTranslateX()); sb.append("\"");
                sb.append(" translateY=\""); sb.append(rectangle.getTranslateY()); sb.append("\"");
                sb.append(" scaleX=\""); sb.append(rectangle.getScaleX()); sb.append("\"");
                sb.append(" scaleY=\""); sb.append(rectangle.getScaleY()); sb.append("\"");
                sb.append("/>\n");
            } else if (node instanceof Circle) {
                Circle circle = (Circle)node;
                sb.append("<Circle");
                sb.append(" radius=\""); sb.append(circle.getRadius()); sb.append("\"");
                sb.append(" fill=\""); sb.append(circle.getFill().toString()); sb.append("\"");
                sb.append(" centerX=\""); sb.append(circle.getCenterX()); sb.append("\"");
                sb.append(" centerY=\""); sb.append(circle.getCenterY()); sb.append("\"");
                sb.append(" translateX=\""); sb.append(circle.getTranslateX()); sb.append("\"");
                sb.append(" translateY=\""); sb.append(circle.getTranslateY()); sb.append("\"");
                sb.append(" scaleX=\""); sb.append(circle.getScaleX()); sb.append("\"");
                sb.append(" scaleY=\""); sb.append(circle.getScaleY()); sb.append("\"");
                sb.append("/>\n");
            } else if (node instanceof Polygon) {
                Polygon polygon = (Polygon)node;
                sb.append("<Polygon");
                sb.append(" fill=\""); sb.append(polygon.getFill().toString()); sb.append("\"");
                sb.append(" layoutX=\""); sb.append(polygon.getLayoutX()); sb.append("\"");
                sb.append(" layoutY=\""); sb.append(polygon.getLayoutY()); sb.append("\"");
                sb.append(" translateX=\""); sb.append(polygon.getTranslateX()); sb.append("\"");
                sb.append(" translateY=\""); sb.append(polygon.getTranslateY()); sb.append("\"");
                sb.append(" scaleX=\""); sb.append(polygon.getScaleX()); sb.append("\"");
                sb.append(" scaleY=\""); sb.append(polygon.getScaleY()); sb.append("\"");
                sb.append(">\n");
                sb.append("        <points>\n");
                for(Double point : polygon.getPoints()) {
                    sb.append("            ");
                    sb.append("<Double fx:value=\""); sb.append(point); sb.append("\"/>\n");
                }
                sb.append("        </points>\n");
                sb.append("    </Polygon>\n");
            }

        }
        sb.append("</Pane>");

        return sb.toString();
    }
}
