package com.dabek.jakub.shapes;

public abstract class Tetragon extends Shape {

    public double[] sideLengths;
    public double angle;

    public Tetragon(double side1, double side2, double side3, double side4, double angle) {
        sideLengths = new double[4];
        sideLengths[0] = side1;
        sideLengths[1] = side2;
        sideLengths[2] = side3;
        sideLengths[3] = side4;
        this.angle = angle;

        for (int i = 0; i < 4; i++) {
            if (sideLengths[i] <= 0) {
                throw new UnsupportedOperationException("Side length must be positive");
            }
        }
    }

    static boolean areDoublesMoreOrLessEqual(double a, double b) {
        return Math.abs(a - b) < 0.000000001;
    }

    public static Tetragon makeTetragon(
        double side1,
        double side2,
        double side3,
        double side4,
        double angleInDegrees) {

        if (areDoublesMoreOrLessEqual(side1, side3) && areDoublesMoreOrLessEqual(side2, side4)) {
            if (areDoublesMoreOrLessEqual(side1, side2)) {
                if (areDoublesMoreOrLessEqual(angleInDegrees, 90.0)) {
                    return new Square(side1);
                }
                else {
                    return new Rhombus(side1, angleInDegrees / 180.0 * Math.PI);
                }
            }
            else if (areDoublesMoreOrLessEqual(angleInDegrees, 90.0)) {
                return new Rectangle(side1, side2);
            }
        }

        throw new UnsupportedOperationException("This is not a square, rectangle, or rhombus: \n"
            + side1 + " "
            + side2 + " "
            + side3 + " "
            + side4 + " "
            + angleInDegrees + " ");
    }

}
