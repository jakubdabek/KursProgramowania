#pragma once

#include "Tetragon.hpp"

class Square : public Tetragon
{
public:
    Square(double sideLength);
    const char* name() noexcept override { return "Square"; }
    double surface_area() noexcept override;
    double perimeter() noexcept override;
};