public class Square extends Tetragon {

    public Square(double sideLength) {
        super(sideLength, sideLength, sideLength, sideLength, Math.PI / 2);
    }

    @Override
    public surfaceArea() {
        return sideLengths[0] * sideLengths[0];
    }

    @Override
    public perimeter() {
        return 4 * sideLengths[0];
    }

}