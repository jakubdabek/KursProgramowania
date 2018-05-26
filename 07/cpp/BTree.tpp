#pragma once
#include "BTree.hpp"

#include <algorithm>
#include <iostream>
#include <utility>
#include <memory>


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

template <typename T, typename Comparer>
inline void BTree<T, Comparer>::remove(const T &value)
{
    if (maxCapacity <= 2)
    {
        std::cerr << "Removing elements doesn't work when max capacity is less than 3" << std::endl;
        return;
    }
    if (root)
    {
        root->remove(value);
        while (root->elements.size() == 1)
            root = root->elements.front().second;
    }
}