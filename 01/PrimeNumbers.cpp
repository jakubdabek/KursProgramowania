#include <iostream>
#include <string>
#include <vector>
#include <exception>
#include <sstream>
#include <cmath>

class PrimeNumbers
{
  private:
    std::vector<int> values;

  public:
    explicit PrimeNumbers(int n)
    {
        if (n < 0)
            throw std::domain_error(std::to_string(n) + " - Wrong index");

        std::vector<bool> sieve(n + 1, true);
        sieve[0] = false;
        sieve[1] = false;

        for (int i = 2; i <= std::sqrt(n + 1); i++)
        {
            if (sieve[i])
            {
                for (int j = i * i; j <= n; j += i)
                {
                    sieve[j] = false;
                }
            }
        }

        for (int i = 0; i < sieve.size(); i++)
        {
            if (sieve[i])
                values.emplace_back(i);
        }
    }

    int number(int m) const
    {
        if (m < 0 || m >= values.size())
            throw std::domain_error(std::to_string(m) + " - Number index out of bounds");
        return values[m];
    }
};

bool parse_int(std::string str, int &n)
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

    if (int n; parse_int(argv[1], n))
    {
        try
        {
            PrimeNumbers numbers(n);
            for (int i = 2; i < argc; i++)
            {
                try
                {
                    if (int m; parse_int(argv[i], m))
                    {
                        auto tmp = numbers.number(m);
                        cout << m << " - " << tmp << "\n";
                    }
                    else
                    {
                        cout << argv[i] << " is not an integer\n";
                    }
                }
                catch (std::domain_error &e)
                {
                    cout << e.what() << "\n";
                }
            }
        }
        catch (std::domain_error &e)
        {
            cout << e.what() << "\n";
        }
    }
    else
    {
        cout << argv[1] << " is not an integer\n";
    }
}