#include "Hexagon.hpp"

#include <cmath>

static const double area_constant = 3 * std::sqrt(3) / 2;

Hexagon::Hexagon(double sideLength) : sideLength{sideLength} {}

double Hexagon::surface_area() const noexcept { return area_constant * sideLength * sideLength; }

double Hexagon::perimeter() const noexcept { return 6 * sideLength; }