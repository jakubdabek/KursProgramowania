#pragma once

#include "Shape.hpp"

class Hexagon : public Shape
{
public:
    Hexagon(double sideLength);
    const char* name() noexcept override { return "Hexagon"; }
    double surface_area() noexcept override;
    double perimeter() noexcept override;

public:
    const double sideLength;
};