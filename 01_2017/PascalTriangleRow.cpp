#include <iostream>
#include <string>
#include <vector>
#include <exception>
#include <sstream>

class PascalTriangleRow
{
    std::vector<int> values;

  public:
    int row_index;

    PascalTriangleRow(int n) : row_index{n}
    {
        if (n < 0)
            throw std::domain_error(std::to_string(n) + " - Wrong row index");
        row_index = n;
        values.resize(n + 1);
        values[0] = 1;
        values[values.size() - 1] = 1;
        for (int i = 1; i < n / 2 + 1; i++)
        {
            values[i] = values[i - 1] * n / i;
            values[values.size() - i] = values[i];
            n--;
        }
    }

    int column(int m)
    {
        if (m < 0 || m >= values.size())
            throw std::domain_error(std::to_string(m) + " - Column index out of bounds");
        return values[m];
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
            PascalTriangleRow row(n);
            for (int i = 2; i < argc; i++)
            {
                try
                {
                    int m;
                    if (parse_int(argv[i], m))
                    {
                        int tmp = row.column(m);
                        cout << m << " - " << tmp << "\n";
                    }
                    else
                    {
                        cout << argv[i] << " is not a number\n";
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
        cout << argv[1] << " is not a number\n";
    }
}