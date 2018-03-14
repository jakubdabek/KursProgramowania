#include "Shape.hpp"
#include "Tetragon.hpp"
#include "Pentagon.hpp"
#include "Hexagon.hpp"
#include "Circle.hpp"

#include <exception>
#include <iostream>
#include <typeinfo>
#include <sstream>
#include <memory>
#include <vector>

using std::literals::operator""s;

template <typename T>
bool parse(const std::string &str, T &x)
{
    static std::stringstream stream;
    stream.clear();
    stream.str(str);
    stream >> x;
    return !stream.fail();
}

template <typename T>
void parse_with_validation(const std::string &str, T &x)
{
    if (!parse(str, x))
    {
        throw std::invalid_argument(str + " couldn't be parsed as " + typeid(T).name());
    }
}

int main(int argc, char *argv[])
{
    std::vector<std::unique_ptr<Shape>> shapes;

    try
    {
        int i = 2;
        for (auto c : std::string{argv[1]})
        {
            switch (double tmp[5]; c)
            {
            case 't':
                for (int j = 0; j < 5; j++)
                {
                    parse_with_validation(argv[i + j], tmp[j]);
                }
                i += 5;
                shapes.push_back(Tetragon::make_tetragon(tmp[0], tmp[1], tmp[2], tmp[3], tmp[4]));
                break;
            case 'p':
                parse_with_validation(argv[i], tmp[0]);
                i++;
                shapes.push_back(std::make_unique<Pentagon>(tmp[0]));
                break;
            case 'h':
                parse_with_validation(argv[i], tmp[0]);
                i++;
                shapes.push_back(std::make_unique<Hexagon>(tmp[0]));
                break;
            case 'c':
                parse_with_validation(argv[i], tmp[0]);
                i++;
                shapes.push_back(std::make_unique<Circle>(tmp[0]));
                break;
            default:
                std::cerr << "'" + std::string(1, c) + "'" + " doesn't denote any shape";
                return EXIT_FAILURE;
            }
        }
    }
    catch (std::invalid_argument &e)
    {
        std::cerr << e.what();
    }

    for (auto &shape : shapes)
    {
        std::cout << shape->name()
             << " with area " << shape->surface_area()
             << " and perimeter " << shape->perimeter() << "\n";
    }
}