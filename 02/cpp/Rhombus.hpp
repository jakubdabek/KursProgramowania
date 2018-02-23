#pragma once

#include "Tetragon.hpp"

class Rhombus : public Tetragon
{
public:
    using radian = double;
    Rhombus(double sideLength, radian angle);
    const char* name() noexcept override { return "Rhombus"; }
    double surface_area() noexcept override;
    double perimeter() noexcept override;
};