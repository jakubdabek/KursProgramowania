#pragma once

#include <utility>
#include <memory>
#include <vector>
#include <array>
#include <list>
#include <set>


template<typename T, size_t N = 4, typename Comparer = std::less<T>>
class BTree
{
private:
    struct Node
    {
        std::list<T> values;
        std::list<std::shared_ptr<Node>> children;
        std::weak_ptr<Node> parent;

        Node() = default;

        void insert(T&&);

        template<typename... Args>
        auto insert(Args&&...) -> std::enable_if_t<std::is_constructible<T, Args...>::value, std::shared_ptr<Node>>;
    };

public:
    BTree();

    void insert(T&&);

    template<typename... Args>
    auto insert(Args&&... args)
        -> decltype(std::declval<Node>().insert(args...));
        //-> std::enable_if_t<std::is_constructible<T, Args...>::value, std::shared_ptr<Node>>;

private:
    std::shared_ptr<Node> root;
    Comparer comparer;
};

#include "BTree.tpp"