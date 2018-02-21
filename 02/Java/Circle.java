public class Circle extends Shape {

    public double radius;

    public Circle(double radius) {
        this.radius = radius;
        if (radius < 0)
            throw new IllegalArgumentException("Radius cannot be negative");
    }

    @Override
    public double surfaceArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }

}
