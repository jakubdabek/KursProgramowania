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
        std::cerr << "Too few arguments" << endl;
        return EXIT_FAILURE;
    }

    char mode;
    if ((mode = ::mode(argv[1])) == 'X')
    {
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
            std::cerr << e.what() << endl;
        }
    }
}

void TEST(bool);
void TEST_WRONG(bool);

char mode(const std::string &mode)
{
    if (mode == "TEST" || mode == "TEST_PRINTALL")
    {
        TEST(mode == "TEST_PRINTALL");
        return 'X';
    }
    else if (mode == "TEST_WRONG" || mode == "TEST_WRONG_PRINTALL")
    {
        TEST_WRONG(mode == "TEST_WRONG_PRINTALL");
        return 'X';
    }
    else if (mode != "R" && mode != "A")
    {
        std::cerr << "Wrong arguments\n"
                "Put mode as the first argument: "
                "'R' for roman to arabic, 'A' for arabic to roman" << std::endl;
        return 'X';
    }

    return mode == "R" ? 'R' : 'A';
}

void TEST(bool print)
{
    using namespace std;

    bool passed = true;

    cerr << "Testing if the functions are reverse of each other on numbers 1-3999" << endl;
    for (int i = 1; i < 4000; i++)
    {
        try
        {
            auto parsed_roman_numeral = RomanArabic::Arabic2Roman(i);
            auto parsed_value = RomanArabic::Roman2Arabic(parsed_roman_numeral);
            if (parsed_value != i)
            {
                passed = false;
                cerr << "ERROR - The results don't match: "
                     << "i = " << quoted(to_string(i)) << " "
                     << "converted to roman = " << quoted(parsed_roman_numeral) << " "
                     << "converted back to arabic = " << quoted(to_string(parsed_value)) << endl;
            }
        }
        catch (RomanArabic::Exception &e)
        {
            passed = false;
            cerr << "ERROR - Exception caught: " << quoted(e.what()) << endl;
        }
    }

    if (!passed)
    {
        cerr << "Test failed, aborting further testing" << endl;
        return;
    }
    else
    {
        cerr << "Test passed, next test with known values" << endl;
    }

    int expected_value;
    char comma;
    string expected_roman_numeral;
    while (cin >> expected_value >> comma >> expected_roman_numeral)
    {
        try
        {
            auto parsed_value = RomanArabic::Roman2Arabic(expected_roman_numeral);
            auto parsed_roman_numeral = RomanArabic::Arabic2Roman(expected_value);
            bool this_passed = true;
            if (parsed_value != expected_value)
            {
                this_passed = passed = false;
                cerr << "ERROR - values don't match: "
                     << "expected = " << quoted(to_string(expected_value)) << " "
                     << "actual = "   << quoted(to_string(parsed_value)) << endl;
            }
            if (parsed_roman_numeral != expected_roman_numeral)
            {
                this_passed = passed = false;
                cerr << "ERROR - roman numerals don't match: "
                     << "expected = " << quoted(expected_roman_numeral) << " "
                     << "actual = "   << quoted(parsed_roman_numeral) << endl;
            }
            if (print && this_passed)
            {
                cerr << "OK - " << setw(6) << quoted(to_string(parsed_value)) << " = " << quoted(parsed_roman_numeral) << endl;
            }
        }
        catch (RomanArabic::Exception &e)
        {
            passed = false;
            cerr << "ERROR - Exception caught: " << quoted(e.what()) << endl;
        }
    }

    if (!passed)
    {
        cerr << "Test failed" << endl;
    }
    else
    {
        cerr << "All tests passed" << endl;
    }
}

void TEST_WRONG(bool print)
{
    using namespace std;
    
    string line;
    bool rom2arab = false, passed = true;
    while (getline(cin, line))
    {
        if (line.empty())
        {
            rom2arab = !rom2arab;
            continue;
        }

        bool this_passed = false;
        try
        {
            if (rom2arab)
            {
                auto parsed_value = RomanArabic::Roman2Arabic(line);
                cerr << "ERROR - value somehow parsed: " 
                     << setw(18) << quoted(line) << " = " << quoted(to_string(parsed_value)) << endl;
            }
            else
            {
                auto parsed_roman_numeral = RomanArabic::Arabic2Roman(parse_int(line));
                cerr << "ERROR - value somehow parsed: " 
                     << setw(10) << quoted(line) << " = " << quoted(parsed_roman_numeral) << endl;
            }
        }
        catch (RomanArabic::Exception &e)
        {
            this_passed = true;
            if (print)
            {
                cerr << "OK - Exception caught: " << quoted(e.what()) << endl;
            }
        }
        catch (invalid_argument &e)
        {
            cerr << "FATAL ERROR - testing aborted, wrong test input. " << endl;
            cerr << e.what() << endl;
            cerr << quoted(line) << " size " << line.size() << endl;
            return;
        }
        passed = passed && this_passed;
    }

    if (!passed)
    {
        cerr << "Test failed" << endl;
    }
    else
    {
        cerr << "All tests passed" << endl;
    }
}