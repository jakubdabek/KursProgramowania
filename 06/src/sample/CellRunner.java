package sample;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


/**
 * Class responsible for managing the color of one cell
 *
 * @see CellRunner#run()
 */
public class CellRunner implements Runnable {

    private static final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * Sets the running state of all created cells.
     * <p>
     * Has to be set to true before starting any threads managing the cells.
     */
    public static void setRunning(boolean newValue) {
        running.set(newValue);
    }

    private final int id;
    private final Rectangle rectangle;
    private final Rectangle[] neighbours;
    private final long defaultDelay;
    private final double randomColorChance;

    public CellRunner(int id, Rectangle rectangle, Rectangle[] neighbours, long defaultDelay, double randomColorChance) {
        if (rectangle == null) throw new NullPointerException("\"rectangle\" can't be null");
        if (neighbours == null) throw new NullPointerException("\"neighbours\" array can't be null");

        this.id = id;
        this.rectangle = rectangle;
        this.neighbours = neighbours;
        this.defaultDelay = defaultDelay;
        this.randomColorChance = randomColorChance;
    }

    /**
     * Creates a random color.
     *
     * @return a random color
     */
    static Color getRandomColor() {
        return Color.color(
                ThreadLocalRandom.current().nextDouble(),
                ThreadLocalRandom.current().nextDouble(),
                ThreadLocalRandom.current().nextDouble()
        );
    }

    /**
     * Takes the average of the given colors by taking the square average of their components.
     *
     * @param colors an iterable of {@link Color Colors}
     * @return the average of the given colors
     */
    private static Color getAverageColor(Iterable<Color> colors) {
        double red = 0.0, green = 0.0, blue = 0.0;
        int count = 0;
        for (Color c : colors) {
            red += c.getRed() * c.getRed();
            green += c.getGreen() * c.getGreen();
            blue += c.getBlue() * c.getBlue();
            count++;
        }
        red /= count;
        green /= count;
        blue /= count;

        return Color.color(Math.sqrt(red), Math.sqrt(green), Math.sqrt(blue));
    }

    /**
     * Sets a random color to the cell or an average of its neighbours' colors
     * with a random delay within [0.5{@link CellRunner#defaultDelay d}, 1.5{@link CellRunner#defaultDelay d}]
     */
    @Override
    public void run() {
        synchronized (System.err) {
            System.err.format("Thread %d started\n", id);
        }
        while (running.get() && !Thread.interrupted()) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextLong(defaultDelay + 1L) + defaultDelay / 2L);
            } catch (InterruptedException ex) {
                synchronized (System.err) {
                    System.err.format("Thread %d interrupted\n", id);
                }
                Thread.currentThread().interrupt();
                break;
            }
            Platform.runLater(() -> rectangle.setStroke(null));
            if (ThreadLocalRandom.current().nextDouble() < randomColorChance) {
                Platform.runLater(() -> rectangle.setFill(getRandomColor()));
            } else {
                Platform.runLater(() -> {
                    List<Color> colors =
                            Arrays.stream(neighbours)
                                    .map(r -> (Color) r.getFill())
                                    .collect(Collectors.toList());

                    rectangle.setFill(getAverageColor(colors));
                });
            }
        }
        synchronized (System.err) {
            System.err.format("Thread %d finished\n", id);
        }
    }
}
