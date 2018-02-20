public class Pentagon extends Shape {

    public static double AREA_CONSTANT = Math.sqrt(25 + 10 * Math.sqrt(5)) / 4;

    public double sideLength;

    public Pentagon(int sideLength) {
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
        return 5 * sideLength;
    }

}