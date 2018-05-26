#include <iostream>
#include <string>

namespace
{
template <typename T, typename U>
inline void print(std::ostream &os, const std::list<std::pair<T, U>> &list)
{
    os << "[";
    char delim[] = "\0 ";
    for (auto &&element : list)
    {
        os << delim << element.first;
        delim[0] = ',';
    }
    os << "]" << std::endl;
}
} // namespace