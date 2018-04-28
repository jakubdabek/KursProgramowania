package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * The main class
 * @author Jakub Dąbek
 */
public class Main extends Application {

    private static final Random random = new Random();

    private static synchronized Color getRandomColor() {
        synchronized (random) {
            return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }
    }

    private static int mod(int v, int m) {
        return ((v % m) + m) % m;
    }

    private static AtomicBoolean running = new AtomicBoolean(true);
    private static List<Thread> threads;

    @Override
    public void start(Stage primaryStage) throws NumberFormatException {
        GridPane gridPane = new GridPane();
        List<String> parameters = this.getParameters().getRaw();
        int rectangleSize = Integer.parseInt(parameters.get(0));
        int width = Integer.parseInt(parameters.get(1));
        int height = Integer.parseInt(parameters.get(2));
        int delay = Integer.parseInt(parameters.get(3));
        double chanceOfRandom = Double.parseDouble(parameters.get(4));
        threads = new ArrayList<>(width * height);
        Rectangle[][] rectangles = new Rectangle[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                final Rectangle rect = new Rectangle(rectangleSize, rectangleSize);
                rect.setFill(getRandomColor());
                rectangles[i][j] = rect;
                gridPane.add(rect, i, j);
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                final Rectangle[] neighbours = new Rectangle[4];
                neighbours[0] = rectangles[mod(i - 1, width)][j];
                neighbours[1] = rectangles[i][mod(j - 1, height)];
                neighbours[2] = rectangles[mod(i + 1, width)][j];
                neighbours[3] = rectangles[i][mod(j + 1, height)];
                final Rectangle thisRect = rectangles[i][j];
                final int tmpI = i, tmpJ = j;
                threads.add(new Thread(() -> {
                    final int threadId = tmpI * height + tmpJ;
                    System.out.format("Thread %d started\n", threadId);

                    final Random localRandom;
                    synchronized (random) {
                        localRandom = new Random(random.nextLong());
                    }
                    while (running.get()) {
                        if (localRandom.nextDouble() < chanceOfRandom) {
                            Platform.runLater(() -> thisRect.setFill(getRandomColor()));
                        } else {
                            int red = 0, green = 0, blue = 0;
                            for (Rectangle r : neighbours) {
                                Color c = (Color) r.getFill();
                                red += c.getRed() * 255;
                                green += c.getGreen() * 255;
                                blue += c.getBlue() * 255;
                                //System.err.format("%d %d %d\n", red, green, blue);
                            }
                            red /= 4;
                            green /= 4;
                            blue /= 4;
                            Color newColor = Color.rgb(red, green, blue);
                            //System.err.println(newColor);
                            Platform.runLater(() -> thisRect.setFill(newColor));
                        }
                        try {
                            Thread.sleep((long) (localRandom.nextInt(delay) + delay / 2));
                        } catch (InterruptedException ex) {
                            System.err.println("Thread interrupted. Well, it happens ¯\\_(ツ)_/¯");
                        }
                    }

                    System.out.format("Thread %d finished\n", threadId);
                }));
            }
        }

        primaryStage.setTitle("Color Field");
        primaryStage.setScene(new Scene(gridPane));
        primaryStage.show();

        for (Thread thread : threads) {
            thread.start();
        }
    }

    @Override
    public void stop() throws Exception {
        running.set(false);
        for (Thread thread : threads) {
            thread.join();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}