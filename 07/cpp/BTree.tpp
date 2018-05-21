#pragma once
#include "BTree.hpp"

#include <algorithm>
#include <iostream>
#include <utility>
#include <memory>


namespace
{
template <typename T, typename U>
inline void print(const std::list<std::pair<T, U>> &list)
{
    std::cout << "[";
    char delim[] = "\0 ";
    for (auto &&element : list)
    {
        std::cout << delim << element.first;
        delim[0] = ',';
    }
    std::cout << "]" << std::endl;
}
} // namespace


template <typename T, typename Comparer>
inline void BTree<T, Comparer>::print(std::ostream &os) const noexcept
{
    if (root)
    {
        std::cout << "root" << std::endl;
        root->print(os);
    }
    else
        os << "Empty tree";
}

template <typename T, typename Comparer>
inline bool BTree<T, Comparer>::contains(const T &value) const noexcept
{
    return root && root->contains(value);
}

template <typename T, typename Comparer>
inline decltype(auto) BTree<T, Comparer>::insert(T &&value)
    noexcept(noexcept(insert_impl(std::declval<T>())))
{
    return insert_impl(std::move(value));
}

template <typename T, typename Comparer>
inline decltype(auto) BTree<T, Comparer>::insert(const T &value)
    noexcept(noexcept(insert_impl(std::declval<const T&>())))
{
    return insert_impl(value);
}

template <typename T, typename Comparer>
template <typename U>
inline decltype(auto) BTree<T, Comparer>::insert_impl(U &&value)
    noexcept(std::is_nothrow_constructible<T, U&&>::value && noexcept(root->insert(std::declval<U>(), root)))
{
    if (!root)
    {
        root = std::make_shared<Node>(maxCapacity);
        root->elements.emplace_front(value, nullptr);
        return true;
    }

    return root->insert(std::forward<U>(value));
}