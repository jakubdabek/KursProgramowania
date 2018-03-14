#pragma once

#include "Shape.hpp"

#include <array>
#include <memory>

class Tetragon : public Shape
{
  public:
    using radian = double;
    Tetragon(double side1, double side2, double side3, double side4, radian angle);
    virtual const char* name() const noexcept override { return "Tetragon"; }

    static std::unique_ptr<Tetragon> make_tetragon(double side1,
                                                   double side2,
                                                   double side3,
                                                   double side4,
                                                   double angleInDegrees);

  public:
    std::array<double, 4> sideLengths;
    double angle;
};