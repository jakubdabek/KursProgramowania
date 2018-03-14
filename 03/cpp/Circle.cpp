#include "Circle.hpp"

#include <cmath>

static const double pi = std::acos(-1);

Circle::Circle(double radius) : radius{radius} {}

double Circle::surface_area() const noexcept { return pi * radius * radius; }

double Circle::perimeter() const noexcept { return 2.0 * pi * radius; }
