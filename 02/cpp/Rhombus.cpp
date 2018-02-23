#include "Rhombus.hpp"

#include <cmath>

static const double pi = std::acos(-1);

Rhombus::Rhombus(double sideLength, radian angle) : 
    Tetragon(sideLength, sideLength, sideLength, sideLength, angle) {}

double Rhombus::surface_area() noexcept { return sideLengths[0] * sideLengths[0] * std::sin(angle); }

double Rhombus::perimeter() noexcept { return 4 * sideLengths[0]; }