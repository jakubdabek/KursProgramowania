package com.dabek.jakub.shapes;

public class Rhombus extends Tetragon {

    public Rhombus(double sideLength, double angle) {
        super(sideLength, sideLength, sideLength, sideLength, angle);
    }

    @Override
    public double surfaceArea() {
        return sideLengths[0] * sideLengths[0] * Math.sin(angle);
    }

    @Override
    public double perimeter() {
        return 4 * sideLengths[0];
    }

}