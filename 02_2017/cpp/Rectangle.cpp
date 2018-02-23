#include "Rectangle.hpp"

#include <cmath>

static const double pi = std::acos(-1);

Rectangle::Rectangle(double side1, double side2) :
    Tetragon(side1, side2, side1, side2, pi / 2) {}

double Rectangle::surface_area() noexcept { return sideLengths[0] * sideLengths[1]; }

double Rectangle::perimeter() noexcept { return 2 * sideLengths[0] + 2 * sideLengths[1]; }