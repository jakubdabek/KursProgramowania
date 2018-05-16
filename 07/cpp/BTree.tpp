#pragma once
#include "BTree.hpp"

#include <utility>
#include <memory>


template<typename T, size_t N, typename Comparer>
inline BTree<T, N, Comparer>::BTree() : root(std::make_shared<Node>())
{

}

template<typename T, size_t N, typename Comparer>
inline void BTree<T, N, Comparer>::insert(T &&value)
{
    root->values.emplace_back(std::move(value));
}

template<typename T, size_t N, typename Comparer>
template<typename... Args>
inline auto BTree<T, N, Comparer>::insert(Args&&... args)
    -> decltype(std::declval<Node>().insert(args...))
    //-> std::enable_if_t<std::is_constructible<T, Args...>::value, std::shared_ptr<Node>>
{
    root->values.emplace_back(std::forward<Args>(args)...);
    // root->elements.emplace_back(
    //     std::piecewise_construct,
    //     std::forward_as_tuple(std::forward<Args>(args)...),
    //     std::make_tuple(nullptr)
    // );
    return root;
}

template<typename T, size_t N, typename Comparer>
inline void BTree<T, N, Comparer>::Node::insert(T &&value)
{
    
}