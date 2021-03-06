#include "Square.hpp"

#include <cmath>

static const double pi = std::acos(-1);

Square::Square(double sideLength) : Tetragon(sideLength, sideLength, sideLength, sideLength, pi / 2) {}

double Square::surface_area() const noexcept { return sideLengths[0] * sideLengths[0]; }

double Square::perimeter() const noexcept { return 4 * sideLengths[0]; }