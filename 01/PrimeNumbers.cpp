#include <iostream>
#include <string>
#include <vector>
#include <exception>
#include <sstream>
#include <cmath>

class PrimeNumbers
{
    std::vector<bool> values;

  public:
    PrimeNumbers(int n) : values(n + 1, true)
    {
        if (n < 0)
            throw std::domain_error(std::to_string(n) + " - Wrong index");
        values[0] = false;
        values[1] = false;

        for (int i = 2; i < std::sqrt(n); i++) {
            if (values[i]) {
                for (int j = i * i; j < n; j += i) {
                    values[j] = false;
                }
            }
        }
    }

    int number(int m) const
    {
        int counter = m;
        for (int i = 0; i < values.size(); i++)
        {
            if (values[i] && --counter == -1)
                return i;
        }
        
        throw std::domain_error(std::to_string(m) + " - Number index out of bounds");
    }
};

bool parse_int(const char* str, int &n)
{
    static std::stringstream stream;
    stream.clear();
    stream.str(str);
    stream >> n;
    return !stream.fail();
}

int main(int argc, char *argv[])
{
    using std::cout;

    int n;
    if (parse_int(argv[1], n))
    {
        try
        {
            PrimeNumbers row(n);
            for (int i = 2; i < argc; i++)
            {
                try
                {
                    int m;
                    if (parse_int(argv[i], m))
                    {
                        int tmp = row.number(m);
                        cout << m << " - " << tmp << "\n";
                    }
                    else
                    {
                        cout << argv[i] << " is not an integer\n";
                    }
                }
                catch(std::domain_error e)
                {
                    cout << e.what() << "\n";
                }
            }
        }
        catch(std::domain_error e)
        {
            cout << e.what() << "\n";
        }
    }
    else
    {
        cout << argv[1] << " is not an integer\n";
    }
}