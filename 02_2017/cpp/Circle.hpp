#pragma once

#include "Shape.hpp"

class Circle : public Shape
{
public:
    Circle(double radius);
    const char* name() noexcept override { return "Circle"; }
    double surface_area() noexcept override;
    double perimeter() noexcept override;

public:
    const double radius;
};