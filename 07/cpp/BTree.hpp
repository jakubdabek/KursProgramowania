#pragma once

#include <utility>
#include <memory>
#include <vector>
#include <array>
#include <list>
#include <set>


template <typename T, typename Comparer = std::less<T>>
class BTree
{
  private:
    struct Node : std::enable_shared_from_this<Node>
    {
        std::list<std::pair<T, std::shared_ptr<Node>>> elements;
        std::weak_ptr<Node> parent;

        Node(size_t maxCapacity) : maxCapacity(maxCapacity) {}

        void print(std::ostream&, std::string prefix = " ") const noexcept;
        bool contains(const T&) const noexcept;

        template <typename U>
        bool insert(U&&, std::weak_ptr<Node> = {}, std::shared_ptr<Node>&& = {})
            noexcept(std::is_nothrow_constructible<T, U&&>::value && noexcept(this->split()));

        void split() 
            noexcept(std::is_nothrow_copy_assignable<T>::value && std::is_nothrow_copy_constructible<T>::value);
        void update_parents_of_elements() noexcept;

        void remove(const T&, bool = false); // TODO: noexcept
        void update_index() noexcept;

        static bool is_leaf(const std::shared_ptr<Node>&) noexcept;
        static bool is_leaf(const Node&) noexcept;
        static const T& index(const std::shared_ptr<Node>&) noexcept;
        static const T& index(const Node&) noexcept;

        size_t capacity() const noexcept { return maxCapacity; }
        // TODO: changing capacity during runtime
        //void capacity(size_t newValue);
      private:
        size_t maxCapacity;
    };

  public:
    explicit BTree(size_t maxCapacity = 4) : maxCapacity(maxCapacity) {}

    decltype(auto) insert(T&&) noexcept(noexcept(insert_impl(std::declval<T>())));
    decltype(auto) insert(const T&) noexcept(noexcept(insert_impl(std::declval<const T&>())));
    void remove(const T&); // TODO: noexcept
    void clear() noexcept { root.reset(); }
    bool contains(const T&) const noexcept;

    void print(std::ostream&) const noexcept;
    friend std::ostream& operator<<(std::ostream &os, const BTree &tree)
    {
        tree.print(os);
        return os;
    }

    size_t capacity() const noexcept { return maxCapacity; }
    // TODO: changing capacity during runtime
    //void capacity(size_t newValue);
  private:
    template <typename U>
    decltype(auto) insert_impl(U&&) noexcept(std::is_nothrow_constructible<T, U&&>::value && noexcept(root->insert(std::declval<U>(), root)));

  private:
    size_t maxCapacity;
    std::shared_ptr<Node> root;
};

#include "utility.hpp"
#include "BTree_Node.tpp"
#include "BTree.tpp"