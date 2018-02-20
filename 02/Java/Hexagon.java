public class Hexagon extends Shape {

    public static double AREA_CONSTANT = 3 * Math.sqrt(3) / 2;

    public double sideLength;

    public Hexagon(double sideLength) {
        this.sideLength = sideLength;
        if (sideLength < 0)
            throw new IllegalArgumentException("Side length cannot be negative");
    }

    @Override
    public double surfaceArea() {
        return AREA_CONSTANT * sideLength * sideLength;
    }

    @Override
    public double perimeter() {
        return 6 * sideLength;
    }

}