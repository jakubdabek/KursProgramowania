#include "BTree.hpp"

#include <iostream>
#include <string>

int main()
{
    BTree<std::string> tree;
    std::string str = "str";
    tree.insert(std::as_const(str));
    tree.insert(str);
    tree.insert({"testing", 2});
    tree.insert(6, 'a');
}