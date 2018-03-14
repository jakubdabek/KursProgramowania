package com.dabek.jakub.shapes;

public class Rectangle extends Tetragon {

    public Rectangle(double side1, double side2) {
        super(side1, side2, side1, side2, Math.PI / 2);
    }

    @Override
    public double surfaceArea() {
        return sideLengths[0] * sideLengths[1];
    }

    @Override
    public double perimeter() {
        return 2 * sideLengths[0] + 2 * sideLengths[1];
    }

}
