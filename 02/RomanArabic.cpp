#include "RomanArabic.hpp"

#include <stdexcept>
#include <exception>
#include <string>
#include <array>

std::string RomanArabic::Arabic2Roman(int value)
{
    throw Exception{std::to_string(value) + " - dunno what to do with that :/"};
}

int RomanArabic::Roman2Arabic(const std::string &str)
{
    int sum = 0, multiplier = 1000;
    int currentIndex = 0;

    bool canExist = true;
    for (int i = 0; i < numbers.size() / 3; i++) {
        bool rowUsed = false;
        for(int j = 0; j < 3; j++) {
            if (str.find(numbers[3 * i + j], currentIndex) == currentIndex) {
                if (rowUsed || !canExist)
                    throw Exception{"The roman numerals " + str + " have a wrong format"};
                rowUsed = true;
                canExist = j == 1 || i % 2 == 0;
                currentIndex += numbers[3 * i + j].length();

                if (i % 2 == 0) {
                    sum += multiplier * (3 - j);
                } else {
                    switch (j) {
                    case 0:
                        sum += multiplier / 10 * 9;
                        break;
                    case 1:
                        sum += multiplier / 2;
                        break;
                    case 2:
                        sum += multiplier / 10 * 4;
                        break;
                    }
                }
            }

        }
        if (i % 2 == 1)
            multiplier /= 10;
        canExist = true;
    }

    if (currentIndex != str.size())
        throw Exception{"The roman numerals " + str + " have a wrong format"};

    return sum;
}

std::array<std::string, 21> RomanArabic::numbers =
    { "MMM", "MM", "M" ,
      "CM" , "D" , "CD",
      "CCC", "CC", "C" ,
      "XC" , "L" , "XL",
      "XXX", "XX", "X" ,
      "IX" , "V" , "IV",
      "III", "II", "I" };