package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


/**
 * The main class
 * <p>
 * Field is initialized and all threads are started in {@link Main#start(Stage)}
 * </p>
 *
 * @author Jakub Dąbek
 */
public class Main extends Application {

    /**
     * Mod operation always returning positive values
     *
     * @return {@code value} mod {@code modulus}, positive even when {@code value} is negative
     */
    private static int mod(int value, int modulus) {
        return ((value % modulus) + modulus) % modulus;
    }

    /**
     * Computes the neighbours of a given cell in a 2D array treating it as a torus
     * @param rectangles the whole field
     * @return array containing neighbours of the cell in the given position
     */
    private static Rectangle[] getNeighbours(Rectangle[][] rectangles, int rowIndex, int columnIndex) {
        final int rowCount = rectangles.length;
        final int columnCount = rectangles[0].length;
        final Rectangle[] neighbours = new Rectangle[4];

        neighbours[0] = rectangles[mod(rowIndex - 1, rowCount)][columnIndex];
        neighbours[1] = rectangles[rowIndex][mod(columnIndex - 1, columnCount)];
        neighbours[2] = rectangles[mod(rowIndex + 1, rowCount)][columnIndex];
        neighbours[3] = rectangles[rowIndex][mod(columnIndex + 1, columnCount)];

        return neighbours;
    }

    private static List<Thread> threads;

    /**
     * Parses arguments, creates the cells initiates threads and starts their execution
     * @param primaryStage {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) {
        List<String> parameters = this.getParameters().getRaw();
        if (parameters.size() < 5) {
            System.err.println("Too few arguments");
            System.err.println("The first 5 arguments should be:");
            System.err.println("\tinitial size of rectangles (both width and height)");
            System.err.println("\tnumber of columns");
            System.err.println("\tnumber of rows");
            System.err.println("\taverage delay of operations");
            System.err.println("\tthe probability of a cell changing to a random color");
            Platform.exit();
            return;
        }
        final int rectangleSize;
        final int columnCount;
        final int rowCount;
        final long defaultDelay;
        final double randomColorChance;
        try {
            rectangleSize = Integer.parseInt(parameters.get(0));
            columnCount = Integer.parseInt(parameters.get(1));
            rowCount = Integer.parseInt(parameters.get(2));
            defaultDelay = Long.parseLong(parameters.get(3));
            randomColorChance = Double.parseDouble(parameters.get(4));
            if (randomColorChance < 0.0 || randomColorChance > 1.0) {
                throw new NumberFormatException("Probability should be in range [0.0, 1.0]");
            }
        } catch (NumberFormatException ex) {
            System.err.println("Not all arguments are correct");
            System.err.println("The first 4 arguments should be integers, the 5th should be a floating-point number");
            System.err.println(ex.getLocalizedMessage());

            Platform.exit();
            return;
        }

        Pane root = new Pane();
        threads = new ArrayList<>(columnCount * rowCount);
        Rectangle[][] rectangles = new Rectangle[rowCount][columnCount];

        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                final Rectangle rect = new Rectangle(rectangleSize, rectangleSize, CellRunner.getRandomColor());
                rectangles[j][i] = rect;
                rect.layoutXProperty().bind(
                        root.widthProperty()
                                .divide(columnCount)
                                .multiply(i)
                                .subtract(Bindings.createDoubleBinding(() -> rect.getLayoutBounds().getMinX(), rect.layoutBoundsProperty()))
                );
                rect.layoutYProperty().bind(
                        root.heightProperty()
                                .divide(rowCount)
                                .multiply(j)
                                .subtract(Bindings.createDoubleBinding(() -> rect.getLayoutBounds().getMinY(), rect.layoutBoundsProperty()))
                );
                root.getChildren().add(rect);
                rect.widthProperty().bind(root.widthProperty().divide(columnCount));
                rect.heightProperty().bind(root.heightProperty().divide(rowCount));
            }
        }

        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                threads.add(new Thread(new CellRunner(
                        i * rowCount + j,
                        rectangles[j][i],
                        getNeighbours(rectangles, j, i),
                        defaultDelay,
                        randomColorChance
                )));
            }
        }

        primaryStage.setTitle("Color Field");
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.show();

        CellRunner.setRunning(true);
        for (Thread thread : threads) {
            thread.start();
        }
    }

    @Override
    public void stop() {
        if(threads != null) {
            CellRunner.setRunning(false);
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException ex) {
                    System.err.println(ex.getLocalizedMessage());
                }
            }
            System.err.println("All threads finished");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}