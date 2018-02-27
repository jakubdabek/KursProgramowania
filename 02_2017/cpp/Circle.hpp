#pragma once

#include "Shape.hpp"

class Circle : public Shape
{
  public:
    explicit Circle(double radius);
    const char* name() const noexcept override { return "Circle"; }
    double surface_area() const noexcept override;
    double perimeter() const noexcept override;

  public:
    const double radius;
};