#pragma once

#include "Tetragon.hpp"

class Square : public Tetragon
{
  public:
    explicit Square(double sideLength);
    const char* name() const noexcept override { return "Square"; }
    double surface_area() const noexcept override;
    double perimeter() const noexcept override;
};