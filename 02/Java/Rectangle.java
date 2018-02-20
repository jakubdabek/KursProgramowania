public class Rectangle extends Tetragon {

    public Rectangle(double side1, double side2) {
        super(side1, side2, side1, side2, Math.PI / 2);
    }

    @Override
    public surfaceArea() {
        return sideLengths[0] * sideLengths[1];
    }

    @Override
    public perimeter() {
        return 2 * sideLengths[0] + 2 * sideLengths[1];
    }

}