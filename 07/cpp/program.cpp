#include "BTree.hpp"

#include <iostream>
#include <iomanip>
#include <random>
#include <string>

int main()
{
    BTree<std::string> tree;
    std::string str = "str";
    std::cout << std::boolalpha;
    std::cout << "adding \"str\": "               << tree.insert(std::as_const(str)) << std::endl;
    std::cout << "adding \"str\": "               << tree.insert(str) << std::endl;
    std::cout << "adding {\"testing\", 2}: "      << tree.insert({"testing", 2}) << std::endl;
    std::cout << "adding \"te\": "                << tree.insert("te") << std::endl;
    std::cout << "adding {6, 'a'}: "              << tree.insert({6, 'a'}) << std::endl;
    std::cout << "adding std::string(6, 'a'): "   << tree.insert(std::string(6, 'a')) << std::endl;
    std::cout << "adding \"xd\": "                << tree.insert("xd") << std::endl;
    std::cout << "adding \"xdd\": "               << tree.insert("xdd") << std::endl;
    std::cout << "adding \"xddd\": "              << tree.insert("xddd") << std::endl;

    std::cout << tree << std::endl;
    std::cout << "adding \"xdddd\": "              << tree.insert("xdddd") << std::endl;
    std::cout << tree << std::endl;
   
    std::cout << "contains \"xddd\": "            << tree.contains("xddd") << std::endl;
    std::cout << "contains \"te\": "              << tree.contains("te") << std::endl;
    std::cout << "contains {\"testing\", 2}: "    << tree.contains({"testing", 2}) << std::endl;
    std::cout << "contains \"testing\": "         << tree.contains("testing") << std::endl;
    std::cout << "contains \"lol\": "             << tree.contains("lol") << std::endl;
    std::cout << "contains {6, 'a'}: "            << tree.contains({6, 'a'}) << std::endl;
    std::cout << "contains std::string(6, 'a'): " << tree.contains(std::string(6, 'a')) << std::endl;
    std::cout << "contains \"aaaaaa\": "          << tree.contains("aaaaaa") << std::endl;


    BTree<int> intTree;
    std::mt19937 engine(std::random_device{}());
    std::uniform_int_distribution<> dist(0, 500);
    for (int i = 0; i < 50; i++)
    {
        int newVal = dist(engine);
        if (!intTree.insert(newVal))
        {
            std::cout << "Couldn't insert " << i << std::endl;
        }
    }

    std::cout << intTree << std::endl;
}