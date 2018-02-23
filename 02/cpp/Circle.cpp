#include "Circle.hpp"

#include <cmath>

static const double pi = std::acos(-1);

Circle::Circle(double radius) : radius{radius} {}

double Circle::surface_area() noexcept { return pi * radius * radius; }

double Circle::perimeter() noexcept { return 2.0 * pi * radius; }
