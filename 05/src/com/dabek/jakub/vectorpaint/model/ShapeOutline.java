package com.dabek.jakub.vectorpaint.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

/**
 * This class manages the outline of the active shape
 * It is responsible for moving the shape and resizing it
 */
public class ShapeOutline extends Pane {

    public ShapeOutline(Node shape) {

        // bindings for size and position of the outline
        maxWidthProperty().bind(Bindings.createDoubleBinding(() -> shape.getBoundsInParent().getWidth() + 20, shape.boundsInParentProperty()));
        minWidthProperty().bind(Bindings.createDoubleBinding(() -> shape.getBoundsInParent().getWidth() + 20, shape.boundsInParentProperty()));
        maxHeightProperty().bind(Bindings.createDoubleBinding(() -> shape.getBoundsInParent().getHeight() + 20, shape.boundsInParentProperty()));
        minHeightProperty().bind(Bindings.createDoubleBinding(() -> shape.getBoundsInParent().getHeight() + 20, shape.boundsInParentProperty()));
        layoutXProperty().bind(Bindings.createDoubleBinding(
                () -> shape.getBoundsInParent().getMinX() - this.getLayoutBounds().getMinX() - 10,
                shape.boundsInParentProperty(), shape.layoutBoundsProperty())
        );
        layoutYProperty().bind(Bindings.createDoubleBinding(
                () -> shape.getBoundsInParent().getMinY() - this.getLayoutBounds().getMinY() - 10,
                shape.boundsInParentProperty(), shape.layoutBoundsProperty())
        );

        setPickOnBounds(true);
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.DASHED, null, null)));

        // movement behaviour
        setOnDragDetected(event -> {
            eventX = event.getSceneX();
            eventY = event.getSceneY();
            translateX = shape.getTranslateX();
            translateY = shape.getTranslateY();

            setOnMouseDragged(innerEvent -> {
                shape.setTranslateX(innerEvent.getSceneX() - eventX + translateX);
                shape.setTranslateY(innerEvent.getSceneY() - eventY + translateY);
            });
        });
        setOnMouseReleased(event -> setOnMouseDragged(null));

        // resizing behaviour
        setOnScroll(event -> {
            double multiplier = 0.004;
            double newScaleX = shape.getScaleX() + multiplier * event.getDeltaY();
            double newScaleY = shape.getScaleY() + multiplier * event.getDeltaY();
            if(newScaleX > 0 && newScaleY > 0) {
                shape.setScaleX(newScaleX);
                shape.setScaleY(newScaleY);
            }
        });

        // counteracts clicking the drawing area cancelling selection
        setOnMouseClicked(Event::consume);

        //addCircles();
    }

    private double translateX, translateY;
    private double eventX, eventY;

    /**
     * Sets up circles on corners and middles of edges
     * Dragging the circles enables resizing
     *
     * Currently unused
     */
    private void addCircles() {
        List<Node> children = getChildren();
        final Color circleColor = Color.ALICEBLUE;
        final double radius = 5;
        final double strokeWidth = 2;

        // top left
        Circle circle = new Circle(0, 0, radius);
        circle.setFill(circleColor);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(strokeWidth);
        circle.centerXProperty().bind(new SimpleDoubleProperty(0));
        circle.centerYProperty().bind(new SimpleDoubleProperty(0));
        children.add(circle);

        // top middle
        circle = new Circle(getWidth() / 2, radius, radius);
        circle.setFill(circleColor);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(strokeWidth);
        circle.centerXProperty().bind(widthProperty().divide(2.0));
        circle.centerYProperty().bind(new SimpleDoubleProperty(0));
        children.add(circle);

        // top right
        circle = new Circle(getWidth(), 0, radius);
        circle.setFill(circleColor);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(strokeWidth);
        circle.centerXProperty().bind(widthProperty());
        circle.centerYProperty().bind(new SimpleDoubleProperty(0));
        children.add(circle);

        // middle right
        circle = new Circle(getWidth(), getHeight() / 2, radius);
        circle.setFill(circleColor);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(strokeWidth);
        circle.centerXProperty().bind(widthProperty());
        circle.centerYProperty().bind(heightProperty().divide(2.0));
        children.add(circle);

        // bottom right
        circle = new Circle(getWidth(), getHeight(), radius);
        circle.setFill(circleColor);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(strokeWidth);
        circle.centerXProperty().bind(widthProperty());
        circle.centerYProperty().bind(heightProperty());
        children.add(circle);

        // bottom middle
        circle = new Circle(getWidth() / 2, getHeight(), radius);
        circle.setFill(circleColor);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(strokeWidth);
        circle.centerXProperty().bind(widthProperty().divide(2.0));
        circle.centerYProperty().bind(heightProperty());
        children.add(circle);

        // bottom left
        circle = new Circle(0, getHeight(), radius);
        circle.setFill(circleColor);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(strokeWidth);
        circle.centerXProperty().bind(new SimpleDoubleProperty(0));
        circle.centerYProperty().bind(heightProperty());
        children.add(circle);

        // middle left
        circle = new Circle(getWidth() / 2, getHeight() / 2, radius);
        circle.setFill(circleColor);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(strokeWidth);
        circle.centerXProperty().bind(new SimpleDoubleProperty(0));
        circle.centerYProperty().bind(heightProperty().divide(2.0));
        children.add(circle);
    }
}
