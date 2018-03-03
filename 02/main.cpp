#include "RomanArabic.hpp"

#include <stdexcept>
#include <iostream>
#include <sstream>
#include <iomanip>
#include <string>

using std::literals::operator""s;

int parse_int(const std::string &str)
{
    static std::stringstream stream;
    stream.clear();
    stream.str(str);
    int n;
    stream >> n;
    if (!(!stream.fail() && stream.eof()))
        throw std::invalid_argument(str + " is not an integer");

    return n;
}

char mode(const std::string&);

int main(int argc, char *argv[])
{
    using std::cout; using std::endl;

    if (argc < 2)
    {
        cout << "Too few arguments" << endl;
        return 0;
    }

    char mode = argv[1][0];
    if (mode != 'R' && mode != 'A')
    {
        std::cout << "Wrong arguments\n"
                "Put mode as the first argument: "
                "'R' for roman to arabic, 'A' for arabic to roman" << std::endl;
        return 0;
    }

    for (int i = 2; i < argc; i++)
    {
        try
        {
            if (mode == 'R')
            {
                auto value = RomanArabic::Roman2Arabic(argv[i]);
                cout << "Value of " << argv[i] << " is " << value << endl;
            }
            else
            {
                auto str = RomanArabic::Arabic2Roman(parse_int(argv[i]));
                cout << argv[i] << " in roman is " << str << endl;
            }
        }
        catch (std::invalid_argument &e)
        {
            cout << e.what() << endl;
        }
    }
}