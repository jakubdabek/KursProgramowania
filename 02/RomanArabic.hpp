#pragma once

#include <stdexcept>
#include <utility>
#include <string>
#include <array>

class RomanArabic
{
  public:
    static int Roman2Arabic(const std::string&);
    static std::string Arabic2Roman(int);

    struct Exception : public std::invalid_argument { using invalid_argument::invalid_argument; };

  private:
    static const std::array<std::pair<std::string, int>, 21> numbers;
};