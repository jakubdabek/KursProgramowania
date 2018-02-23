#pragma once

class Shape
{
public:
    virtual const char* name() noexcept { return "Shape"; }
    virtual double surface_area() noexcept = 0;
    virtual double perimeter() noexcept = 0;
};