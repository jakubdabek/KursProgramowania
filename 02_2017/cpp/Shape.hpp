#pragma once

class Shape
{
  public:
    virtual const char* name() const noexcept { return "Shape"; }
    virtual double surface_area() const noexcept = 0;
    virtual double perimeter() const noexcept = 0;

    virtual ~Shape() = default;
};