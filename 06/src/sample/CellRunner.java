package sample;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class CellRunner implements Runnable {

    private static final AtomicBoolean running = new AtomicBoolean(false);

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
     * Creates a random color
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

    @Override
    public void run() {
        synchronized (System.err) {
            System.err.format("Thread %d started\n", id);
        }
        while (running.get()) {
            if (ThreadLocalRandom.current().nextDouble() < randomColorChance) {
                Platform.runLater(() -> rectangle.setFill(getRandomColor()));
            } else {
                double red = 0, green = 0, blue = 0;
                for (Rectangle r : neighbours) {
                    Color c = (Color) r.getFill();
                    red += c.getRed();
                    green += c.getGreen();
                    blue += c.getBlue();
                }
                red /= 4;
                green /= 4;
                blue /= 4;
                Color newColor = Color.color(red, green, blue);
                Platform.runLater(() -> rectangle.setFill(newColor));
            }
            try {
                Thread.sleep(ThreadLocalRandom.current().nextLong(defaultDelay) + defaultDelay / 2);
            } catch (InterruptedException ex) {
                synchronized (System.err) {
                    System.err.format("Thread %d interrupted. Well, it happens ¯\\_(ツ)_/¯", id);
                    System.err.println(ex.getLocalizedMessage());
                }
            }
        }

        synchronized (System.err) {
            System.err.format("Thread %d finished\n", id);
        }

    }
}
