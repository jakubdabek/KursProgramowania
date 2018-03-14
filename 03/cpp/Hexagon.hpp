#pragma once

#include "Shape.hpp"

class Hexagon : public Shape
{
  public:
    explicit Hexagon(double sideLength);
    const char* name() const noexcept override { return "Hexagon"; }
    double surface_area() const noexcept override;
    double perimeter() const noexcept override;

  public:
    const double sideLength;
};