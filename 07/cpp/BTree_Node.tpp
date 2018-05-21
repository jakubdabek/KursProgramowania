#pragma once

#include "BTree.hpp"

#include <algorithm>
#include <iostream>
#include <utility>
#include <memory>


namespace
{
template <typename T, typename U, typename Comparer>
inline bool equal(T &&left, U &&right, Comparer &&cmp = {})
{
    return (!cmp(left, right) && !cmp(right, left));
}
} // namespace

template <typename T, typename Comparer>
inline const T& BTree<T, Comparer>::Node::index(const std::shared_ptr<Node> &nodePtr) noexcept
{
    return index(*nodePtr);
}

template <typename T, typename Comparer>
inline const T& BTree<T, Comparer>::Node::index(const Node &node) noexcept
{
    return node.elements.front().first;
}

template <typename T, typename Comparer>
inline bool BTree<T, Comparer>::Node::is_leaf(const std::shared_ptr<Node> &nodePtr) noexcept
{
    return is_leaf(*nodePtr);
}

template <typename T, typename Comparer>
inline bool BTree<T, Comparer>::Node::is_leaf(const Node &node) noexcept
{
    //     root can be empty
    //     vvvvvvvvvvvvvvvvvvvvv vv
    return node.elements.empty() || node.elements.front().second == nullptr;
}

template <typename T, typename Comparer>
inline void BTree<T, Comparer>::Node::print(std::ostream &os, std::string prefix) const noexcept
{
    bool isLeaf = is_leaf(*this);
    for (auto &&element : elements)
    {
        os << prefix << element.first << "\n";
        if (!isLeaf)
            element.second->print(os, prefix + "  ");
    }
}

template <typename T, typename Comparer>
inline bool BTree<T, Comparer>::Node::contains(const T &value) const noexcept
{
    Comparer cmp;
    auto it = std::find_if(elements.begin(), elements.end(), [&](auto &&elem) { return cmp(value, elem.first); });

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

template <typename T, typename Comparer>
template <typename U>
inline bool BTree<T, Comparer>::Node::insert(
    U &&value,
    std::weak_ptr<Node> parentPtr,
    std::shared_ptr<Node> &&nodePtr
) noexcept(std::is_nothrow_constructible<T, U&&>::value && noexcept(this->split()))
{
    Comparer cmp;
    auto it = std::find_if(elements.begin(), elements.end(), [&](auto &&elem) { return cmp(value, elem.first); });
    bool smallest = false;
    if (it != elements.begin())
        --it;
    else
        smallest = true;

    std::cerr << "insert: " << value << " " << nodePtr << std::endl;
    parent = parentPtr;
    if (is_leaf(*this) || nodePtr != nullptr)
    {
        std::cerr << "leaf" << std::endl;
        if (::equal(value, it->first, cmp))
            return false;

        if (nodePtr) nodePtr->parent = this->weak_from_this();
        elements.emplace(smallest ? it : ++it, std::forward<U>(value), std::move(nodePtr));

        if (elements.size() > maxCapacity)
        {
            this->split();
        }

        return true;
    }
    else
    {
        if (smallest)
            it->first = value;
        return it->second->insert(std::forward<U>(value), this->weak_from_this(), std::move(nodePtr));
    }
}

template <typename T, typename Comparer>
inline void BTree<T, Comparer>::Node::split()
    noexcept(std::is_nothrow_copy_assignable<T>::value && std::is_nothrow_copy_constructible<T>::value)
{
    std::cerr << "split" << std::endl;
    auto newNode = std::make_shared<Node>(maxCapacity);
    auto it = elements.begin();
    std::advance(it, maxCapacity / 2 + 1);
    newNode->elements.splice(newNode->elements.begin(), elements, it, elements.end());
    newNode->update_parents_of_elements();

    if (auto _parent = parent.lock())
    {
        _parent->insert(index(newNode), _parent->parent, std::move(newNode));
    }
    else // this is root
    {
        auto newLeftNode = std::make_shared<Node>(std::move(*this));
        newLeftNode->update_parents_of_elements();
        elements.emplace_front(index(newLeftNode), std::move(newLeftNode));
        elements.emplace_back(index(newNode), std::move(newNode));
    }
}

template <typename T, typename Comparer>
inline void BTree<T, Comparer>::Node::update_parents_of_elements() noexcept
{
    if (!is_leaf(*this))
    {
        for (auto &element : elements)
        {
            element.second->parent = this->weak_from_this();
        }
    }
}