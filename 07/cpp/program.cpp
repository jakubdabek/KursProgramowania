#include "BTree.hpp"

#include <iostream>
#include <iomanip>
#include <random>
#include <string>

template<typename T>
void user_interface(BTree<T> tree = decltype(tree){});

int main(int argc, char *argv[])
{
    std::string arg;
    if (argc > 1) arg = argv[1];
    if (arg.size() >= 1 && arg[0] == '1')
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
    }

    if (arg.size() >= 2 && arg[1] == '1')
    {
        BTree<int> intTree;
        std::mt19937 engine(std::random_device{}());
        std::uniform_int_distribution<> dist(0, 100);
        for (int i = 0; i < 20; i++)
        {
            int newVal = dist(engine);
            if (!intTree.insert(newVal))
            {
                std::cout << "Couldn't insert " << i << std::endl;
            }
        }

        std::cout << intTree << std::endl;
    }
    {
        std::cout << std::string(50, '-') << std::endl;
        std::cout << "What type of data do you want to use?\n"
                  << "1. int\n"
                  << "2. double\n"
                  << "3. string\n";
        
        int answer;
        while (((std::cin >> answer), !std::cin.eof()) && answer != 1 && answer != 2 && answer != 3)
        {
            std::cout << "Wrong choice" << std::endl;
            std::cin.clear();
        }

        if (std::cin)
        {
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            switch (answer)
            {
            case 1:
                std::cout << "Tree with int" << std::endl;
                user_interface<int>();
                break;
            case 2:
                std::cout << "Tree with double" << std::endl;
                user_interface<double>();
                break;
            case 3:
                std::cout << "Tree with string" << std::endl;
                user_interface<std::string>();
                break;
            }
        }
    }
}

template<typename T>
void user_interface(BTree<T> tree)
{
    std::string query;
    T value;
    while (std::cin >> query)
    {
        if (query == "i" || query == "insert")
        {
            if (!(std::getline(std::cin, query)))
                break;

            std::istringstream stream{query};
            while(stream >> value)
            {
                if (tree.insert(value))
                    std::cout << value << " inserted" << std::endl;
                else
                    std::cout << "Could not insert " << value << std::endl;
            }
        }
        else if (query == "f" || query == "find" || query == "contains")
        {
            if (!(std::getline(std::cin, query)))
                break;

            std::istringstream stream{query};
            while(stream >> value)
            {
                if(tree.contains(value))
                    std::cout << value << " found" << std::endl;
                else
                    std::cout << value << " not found" << std::endl;
            }
        }
        else if (query == "remove")
        {
            if (!(std::getline(std::cin, query)))
                break;

            std::istringstream stream{query};
            while(stream >> value)
            {
                tree.remove(value);
                std::cout << value << " removed" << std::endl;
            }
        }
        else if (query == "clear")
        {
            std::cout << "Do you really want to clear the whole tree? [Yes/No]" << std::endl;
            std::string confirmation;
            std::cin >> confirmation;
            if (confirmation == "Yes")
                tree.clear();
        }
        else if (query == "p" || query == "print")
        {
            std::cout << tree << std::endl;
        }
        else if (query == "q" || query == "x" || query == "quit" || query == "exit")
        {
            return;
        }
        else
        {
            std::cout << "Enter (\"(i)nsert\"|\"(f)ind\"|\"contains\") and a value, \"clear\" or \"(p)rint\"." << std::endl;
            std::cout << "\"(q)uit\" or \"e(x)it\" to end" << std::endl;
        }
    }
}