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
        std::list<std::pair<T, std::shared_ptr<Node>>> elements;
        std::weak_ptr<Node> parent;

        Node() = default;

        void print(std::ostream&, std::string prefix = " ") const noexcept;
        bool contains(const T&) const noexcept;

        template<typename U>
        bool insert(U&&, std::shared_ptr<Node>&& = {});

        void split(bool update = false);

        static bool is_leaf(const std::shared_ptr<Node>&);
        static bool is_leaf(const Node&);
        static const T& index(const std::shared_ptr<Node>&);
        static const T& index(const Node&);
    };

public:
    decltype(auto) insert(T&&);
    decltype(auto) insert(const T&);
    bool contains(const T&) const noexcept;

    void print(std::ostream&) const noexcept;
    friend std::ostream& operator<<(std::ostream &os, const BTree &tree)
    {
        tree.print(os);
        return os;
    }

private:
    template<typename U>
    decltype(auto) insert_impl(U&&);

private:
    std::shared_ptr<Node> root;
};

#include "BTree.tpp"