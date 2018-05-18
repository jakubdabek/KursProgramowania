#pragma once
#include "BTree.hpp"

#include <algorithm>
#include <iostream>
#include <utility>
#include <memory>


namespace
{
    template<typename T, typename U, typename Comparer>
    inline bool equal(T &&left, U &&right, Comparer &&cmp = {})
    {
        return (!cmp(left, right) && !cmp(right, left));
    }

    template<typename T, typename U>
    inline void print(const std::list<std::pair<T, U>> &list)
    {
        std::cout << "[";
        char delim[] = "\0 ";
        for(auto &&element : list)
        {
            std::cout << delim << element.first;
            delim[0] = ',';
        }
        std::cout << "]" << std::endl;
    }
}

template<typename T, size_t N, typename Comparer>
inline void BTree<T, N, Comparer>::print(std::ostream &os) const noexcept
{
    if (root)
    {
        std::cout << "root" << std::endl;
        root->print(os);
    }
    else
        os << "Empty tree";
}

template<typename T, size_t N, typename Comparer>
inline bool BTree<T, N, Comparer>::contains(const T &value) const noexcept
{
    return root && root->contains(value);
}

template<typename T, size_t N, typename Comparer>
inline decltype(auto) BTree<T, N, Comparer>::insert(T &&value)
{
    return insert_impl(std::move(value));
}

template<typename T, size_t N, typename Comparer>
inline decltype(auto) BTree<T, N, Comparer>::insert(const T &value)
{
    return insert_impl(value);
}

template<typename T, size_t N, typename Comparer>
template<typename U>
inline decltype(auto) BTree<T, N, Comparer>::insert_impl(U &&value)
{
    if (!root)
    {
        root = std::make_shared<Node>();
        root->elements.emplace_front(value, nullptr);
        return true;
    }

    return root->insert(std::forward<U>(value));
}


template<typename T, size_t N, typename Comparer>
inline const T& BTree<T, N, Comparer>::Node::index(const std::shared_ptr<Node> &nodePtr)
{
    return index(*nodePtr);
}

template<typename T, size_t N, typename Comparer>
inline const T& BTree<T, N, Comparer>::Node::index(const Node &node)
{
    return node.elements.front().first;
}

template<typename T, size_t N, typename Comparer>
inline bool BTree<T, N, Comparer>::Node::is_leaf(const std::shared_ptr<Node> &nodePtr)
{
    return is_leaf(*nodePtr);
}

template<typename T, size_t N, typename Comparer>
inline bool BTree<T, N, Comparer>::Node::is_leaf(const Node &node)
{
    //     root can be empty
    //     vvvvvvvvvvvvvvvvvvvvv vv
    return node.elements.empty() || node.elements.front().second == nullptr;
}


template<typename T, size_t N, typename Comparer>
inline void BTree<T, N, Comparer>::Node::print(std::ostream &os, std::string prefix) const noexcept
{
    bool isLeaf = is_leaf(*this);
    for (auto &&element : elements)
    {
        os << prefix << element.first << "\n";
        if (!isLeaf)
            element.second->print(os, prefix + "  ");
    }
}

template<typename T, size_t N, typename Comparer>
inline bool BTree<T, N, Comparer>::Node::contains(const T &value) const noexcept
{
    Comparer cmp;
    auto it = std::find_if(elements.begin(), elements.end(), [&](auto &&elem){ return cmp(value, elem.first); });

    std::cout << "Looking for \"" << value << "\" in ";
    ::print(elements);
    std::cout << "Found \"" << it->first << "\"" << std::endl;
    
    if (it == elements.begin())
        return false;

    --it;
    
    if (is_leaf(*this))
    {
        return ::equal(it->first, value, cmp);
    }
    else
    {
        return it->second->contains(value);
    }
}

template<typename T, size_t N, typename Comparer>
template<typename U>
inline bool BTree<T, N, Comparer>::Node::insert(U &&value, std::shared_ptr<Node> &&nodePtr)
{
    Comparer cmp;
    auto it = std::find_if(elements.begin(), elements.end(), [&](auto &&elem){ return cmp(value, elem.first); });
    bool smallest = false;
    if (it != elements.begin())
        --it;
    else
        smallest = true;

    if (is_leaf(*this))
    {
        if (::equal(value, it->first, cmp))
            return false;

        elements.emplace(smallest ? it : ++it, std::forward<U>(value), std::move(nodePtr));

        if (elements.size() > N)
        {
            this->split(smallest);
        }

        return true;
    }
    else
    {
        if (smallest)
            it->first = value;
        return it->second->insert(std::forward<U>(value), std::move(nodePtr));
    }
}

template<typename T, size_t N, typename Comparer>
inline void BTree<T, N, Comparer>::Node::split(bool update)
{
    auto newNode = std::make_shared<Node>();
    auto it = elements.begin();
    std::advance(it, N / 2 + 1);
    std::cout << "before" << std::endl;
    ::print(elements); ::print(newNode->elements);
    newNode->elements.splice(newNode->elements.begin(), elements, it, elements.end());

    std::cout << "after splice" << std::endl;
    std::cout << "this:"; ::print(elements);
    std::cout << "new:"; ::print(newNode->elements);

    if (auto _parent = parent.lock())
    {
        if (update)
        {
            auto it = std::find_if(
                _parent->elements.begin(),
                _parent->elements.end(),
                [&](auto &&val){ return val.second.get() == this; }
            );
            it->first = elements.front().first;
        }
        _parent->insert(index(newNode), std::move(newNode));
    }
    else // this is root
    {
        auto newLeftNode = std::make_shared<Node>(std::move(*this));
        elements.emplace_front(index(newLeftNode), std::move(newLeftNode));
        elements.emplace_back(index(newNode), std::move(newNode));
    }
}