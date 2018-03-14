#pragma once

#include "Tetragon.hpp"

class Rhombus : public Tetragon
{
  public:
    using radian = double;
    Rhombus(double sideLength, radian angle);
    const char* name() const noexcept override { return "Rhombus"; }
    double surface_area() const noexcept override;
    double perimeter() const noexcept override;
};