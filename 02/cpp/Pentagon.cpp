#include "Pentagon.hpp"

#include <cmath>

static const double area_constant = std::sqrt(25 + 10 * std::sqrt(5)) / 4;

Pentagon::Pentagon(double sideLength) : sideLength {sideLength} {}

double Pentagon::surface_area() noexcept { return area_constant * sideLength * sideLength; }

double Pentagon::perimeter() noexcept { return 5 * sideLength; }