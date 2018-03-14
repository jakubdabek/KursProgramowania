#pragma once

#include "Shape.hpp"

class Pentagon : public Shape
{
  public:
    explicit Pentagon(double sideLength);
    const char* name() const noexcept override { return "Pentagon"; }
    double surface_area() const noexcept override;
    double perimeter() const noexcept override;

  public:
    const double sideLength;
};