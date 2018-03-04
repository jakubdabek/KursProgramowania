#include "RomanArabic.hpp"

#include <stdexcept>
#include <utility>
#include <string>
#include <array>

std::string RomanArabic::Arabic2Roman(int number)
{
    if (number <= 0 || number > 3999)
        throw Exception{std::to_string(number) + " is out of range"};

    std::string result;
    for (const auto& [roman, value] : numbers)
    {
        if (number - value >= 0)
        {
            result += roman;
            number -= value;
        }
    }

    if (number != 0)
        throw "Something very wrong happened";

    return result;
}

int RomanArabic::Roman2Arabic(const std::string &str)
{
    int sum = 0;
    int currentIndex = 0;

    bool canExist = true;
    for (int i = 0; i < numbers.size() / 3; i++) {
        bool rowUsed = false;
        for(int j = 0; j < 3; j++) {
            if (auto [roman, value] = numbers[3*i+j]; str.find(roman, currentIndex) == currentIndex) {
                if (rowUsed || !canExist)
                    throw Exception{"The roman numerals " + str + " have a wrong format"};
                rowUsed = true;
                canExist = j == 1 || i % 2 == 0;
                currentIndex += roman.length();
                sum += value;
            }
        }
        canExist = canExist || i % 2 == 0;
    }

    if (currentIndex != str.size())
        throw Exception{"The roman numerals " + str + " have a wrong format"};

    return sum;
}

std::array<std::pair<std::string, int>, 21> RomanArabic::numbers
    {{{"MMM", 3000}, {"MM", 2000}, { "M", 1000},
      { "CM",  900}, { "D",  500}, {"CD",  400},
      {"CCC",  300}, {"CC",  200}, { "C",  100},
      { "XC",   90}, { "L",   50}, {"XL",   40},
      {"XXX",   30}, {"XX",   20}, { "X",   10},
      { "IX",    9}, { "V",    5}, {"IV",    4},
      {"III",    3}, {"II",    2}, { "I",    1}}};