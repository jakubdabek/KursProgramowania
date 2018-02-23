#pragma once

#include "Shape.hpp"

class Pentagon : public Shape
{
public:
    Pentagon(double sideLength);
    const char* name() noexcept override { return "Pentagon"; }
    double surface_area() noexcept override;
    double perimeter() noexcept override;

public:
    const double sideLength;
};