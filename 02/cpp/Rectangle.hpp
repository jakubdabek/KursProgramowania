#pragma once

#include "Tetragon.hpp"

class Rectangle : public Tetragon
{
public:
    Rectangle(double side1, double side2);
    const char* name() noexcept override { return "Rectangle"; }
    double surface_area() noexcept override;
    double perimeter() noexcept override;
};