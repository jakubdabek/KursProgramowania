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
    int current_index = 0;
    bool was_compound = false; // for combinations of symbols with one of lower value before one of higher e.g. IX, IV

    for (int i = 0; i < numbers.size() / 3; i++)
    {
        bool row_used = false; // a row is 3 elements, see definition of numbers (if it is still formatted right)
        for (int j = 0; j < 3; j++)
        {
            const auto& [roman, value] = numbers[3 * i + j];
            if (str.find(roman, current_index) == current_index)
            {
                if (row_used || was_compound)
                    throw Exception{"The roman numerals " + str + " have a wrong format"};
                row_used = true;
                was_compound = i % 2 == 1 && j != 1;
                current_index += roman.length();
                sum += value;
            }
        }
        was_compound = was_compound && i % 2 == 1;
    }

    if (current_index != str.size())
        throw Exception{"The roman numerals " + str + " have a wrong format"};

    return sum;
}

const std::array<std::pair<std::string, int>, 21> RomanArabic::numbers
    {{{"MMM", 3000}, {"MM", 2000}, { "M", 1000},
      { "CM",  900}, { "D",  500}, {"CD",  400},
      {"CCC",  300}, {"CC",  200}, { "C",  100},
      { "XC",   90}, { "L",   50}, {"XL",   40},
      {"XXX",   30}, {"XX",   20}, { "X",   10},
      { "IX",    9}, { "V",    5}, {"IV",    4},
      {"III",    3}, {"II",    2}, { "I",    1}}};