#include "Tetragon.hpp"
#include "Rectangle.hpp"
#include "Square.hpp"
#include "Rhombus.hpp"

#include <memory>
#include <exception>
#include <string>
#include <cmath>

static const double pi = std::acos(-1);

Tetragon::Tetragon(double side1, double side2, double side3, double side4, radian angle) :
    sideLengths{{side1, side2, side3, side4}}, angle{angle}
{
    for (int i = 0; i < 4; i++)
    {
        if (sideLengths[i] <= 0)
        {
            throw std::length_error("Side length must be positive");
        }
    }
}

static bool are_doubles_moreorless_equal(double a, double b) { return std::abs(a - b) < 0.000000001; }

std::unique_ptr<Tetragon> Tetragon::make_tetragon(
    double side1,
    double side2,
    double side3,
    double side4,
    double angleInDegrees)
{
    if (are_doubles_moreorless_equal(side1, side3) && are_doubles_moreorless_equal(side2, side4))
    {
        if (are_doubles_moreorless_equal(side1, side2))
        {
            if (are_doubles_moreorless_equal(angleInDegrees, 90.0))
            {
                return std::make_unique<Square>(side1);
            }
            else
            {
                return std::make_unique<Rhombus>(side1, angleInDegrees / 180.0 * pi);
            }
        }
        else if (are_doubles_moreorless_equal(angleInDegrees, 90.0))
        {
            return std::make_unique<Rectangle>(side1, side2);
        }
    }

    throw std::invalid_argument("This is not a square, rectangle, or rhombus: \n"
        + std::to_string(side1) + " "
        + std::to_string(side2) + " "
        + std::to_string(side3) + " "
        + std::to_string(side4) + " "
        + "   "
        + std::to_string(angleInDegrees) + " ");
}