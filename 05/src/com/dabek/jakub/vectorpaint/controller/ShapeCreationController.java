package com.dabek.jakub.vectorpaint.controller;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.List;

/**
 * Class consisting of static methods for creating shapes
 * Currently supported: rectangle, circle, polygon
 */
public class ShapeCreationController {

    private static Shape currentShape = null;

    /**
     * Cancels currently drawn shape
     * @param drawingPane pane on which the shape is being drawn
     */
    public static void cancelDrawing(Pane drawingPane) {
        if(currentShape != null) {
            drawingPane.getChildren().remove(currentShape);
            currentShape = null;
        }
    }

    /**
     * Begins drawing a rectangle
     * One corner is in the position of {@code originalEvent}, the other is put down when the user clicks a second time
     * @param controller the scenes controller, contains handler for each shape
     * @param originalEvent the event which started the drawing action, usually onMouseClick
     * @param currentColor color of the shape to be drawn
     */
    public static void beginRectangle(Controller controller, MouseEvent originalEvent, Color currentColor) {
        Pane drawingPane = controller.drawingArea;
        Rectangle rectangle = new Rectangle(originalEvent.getX(), originalEvent.getY(), 0, 0);
        rectangle.setFill(currentColor);

        rectangle.setOnMouseClicked(controller.new ShapeOnMouseClickHandler(rectangle));
        currentShape = rectangle;
        drawingPane.getChildren().add(rectangle);

        final EventHandler<? super MouseEvent> onMouseMovedEventHandler = drawingPane.getOnMouseMoved();
        drawingPane.setOnMouseMoved(event -> {
            onMouseMovedEventHandler.handle(event);
            if(currentShape != null) {
                rectangle.setX(Math.min(originalEvent.getX(), event.getX()));
                rectangle.setWidth(Math.abs(originalEvent.getX() - event.getX()));
                rectangle.setY(Math.min(originalEvent.getY(), event.getY()));
                rectangle.setHeight(Math.abs(originalEvent.getY() - event.getY()));
            } else {
                drawingPane.getOnMouseClicked().handle(event);
            }
        });

        final EventHandler<? super MouseEvent> onMouseClickedEventHandler = drawingPane.getOnMouseClicked();
        drawingPane.setOnMouseClicked(event -> {
            currentShape = null;
            drawingPane.setOnMouseMoved(onMouseMovedEventHandler);
            drawingPane.setOnMouseClicked(onMouseClickedEventHandler);
        });
    }

    /**
     * Begins drawing a circle
     * The center is in the position of {@code originalEvent}, the edge is put down when the user clicks a second time
     * @param controller the scenes controller, contains handler for each shape
     * @param originalEvent the event which started the drawing action, usually onMouseClick
     * @param currentColor color of the shape to be drawn
     */
    public static void beginCircle(Controller controller, MouseEvent originalEvent, Color currentColor) {
        Pane drawingPane = controller.drawingArea;
        Circle circle = new Circle(originalEvent.getX(), originalEvent.getY(), 0, currentColor);
        circle.setOnMouseClicked(controller.new ShapeOnMouseClickHandler(circle));
        currentShape = circle;
        drawingPane.getChildren().add(circle);

        final EventHandler<? super MouseEvent> onMouseMovedEventHandler = drawingPane.getOnMouseMoved();
        drawingPane.setOnMouseMoved(event -> {
            onMouseMovedEventHandler.handle(event);
            if (currentShape != null) {
                circle.setRadius(new Point2D(event.getX(), event.getY()).distance(circle.getCenterX(), circle.getCenterY()));
            } else {
                drawingPane.getOnMouseClicked().handle(event);
            }
        });

        final EventHandler<? super MouseEvent> onMouseClickedEventHandler = drawingPane.getOnMouseClicked();
        drawingPane.setOnMouseClicked(event -> {
            currentShape = null;
            drawingPane.setOnMouseMoved(onMouseMovedEventHandler);
            drawingPane.setOnMouseClicked(onMouseClickedEventHandler);
        });

    }

    /**
     * Begins drawing a polygon
     * The first point is in the position of {@code originalEvent}, subsequent points are put on click
     * When a point is close enough to the beginning, the shape is closed
     * @param controller the scenes controller, contains handler for each shape
     * @param originalEvent the event which started the drawing action, usually onMouseClick
     * @param currentColor color of the shape to be drawn
     */
    public static void beginPolygon(Controller controller, MouseEvent originalEvent, Color currentColor) {
        Pane drawingPane = controller.drawingArea;
        Polyline polyline = new Polyline(originalEvent.getX(), originalEvent.getY(), originalEvent.getX(), originalEvent.getY());
        polyline.setFill(currentColor);
        currentShape = polyline;
        drawingPane.getChildren().add(polyline);

        final EventHandler<? super MouseEvent> onMouseMovedEventHandler = drawingPane.getOnMouseMoved();
        drawingPane.setOnMouseMoved(event -> {
            onMouseMovedEventHandler.handle(event);
            if (currentShape != null) {
                int last = polyline.getPoints().size() - 1;
                polyline.getPoints().set(last - 1, event.getX());
                polyline.getPoints().set(last, event.getY());
            } else {
                drawingPane.getOnMouseClicked().handle(event);
            }
        });

        final EventHandler<? super MouseEvent> onMouseClickedEventHandler = drawingPane.getOnMouseClicked();
        drawingPane.setOnMouseClicked(event -> {
            if(currentShape == null) {
                drawingPane.setOnMouseMoved(onMouseMovedEventHandler);
                drawingPane.setOnMouseClicked(onMouseClickedEventHandler);
            } else if (Math.abs(event.getX() - polyline.getPoints().get(0)) > 10 || Math.abs(event.getY() - polyline.getPoints().get(1)) > 10) {
                polyline.getPoints().addAll(event.getX(), event.getY());
            } else {
                Polygon polygon = new Polygon();
                polygon.setFill(currentColor);
                List<Double> points = polygon.getPoints();
                points.addAll(polyline.getPoints());
                int last = polygon.getPoints().size() - 1;
                points.set(last - 1, points.get(0));
                points.set(last, points.get(1));
                polygon.setOnMouseClicked(controller.new ShapeOnMouseClickHandler(polygon));
                drawingPane.getChildren().set(drawingPane.getChildren().size() - 1, polygon);
                currentShape = null;
                drawingPane.setOnMouseMoved(onMouseMovedEventHandler);
                drawingPane.setOnMouseClicked(onMouseClickedEventHandler);
            }
        });
    }
}
