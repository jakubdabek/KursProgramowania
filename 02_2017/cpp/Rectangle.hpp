#pragma once

#include "Tetragon.hpp"

class Rectangle : public Tetragon
{
  public:
    Rectangle(double side1, double side2);
    const char* name() const noexcept override { return "Rectangle"; }
    double surface_area() const noexcept override;
    double perimeter() const noexcept override;
};