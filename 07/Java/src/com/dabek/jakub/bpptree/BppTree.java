package com.dabek.jakub.bpptree;

import java.util.*;
import java.util.function.Predicate;

public class BppTree<T extends Comparable<T>> extends AbstractCollection<T> {
    private final int maxCapacity;
    private int size;
    private Node root;

    private class BppTreeIterator implements Iterator<T> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }

        @Override
        public void remove() {

        }
    }

    @Override
    public Iterator<T> iterator() {
        return new BppTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }

    private class Pair implements Comparable<Pair> {
        T value;
        Node child;

        Pair(T value) {
            this(value, null);
        }

        Pair(T value, Node child) {
            this.value = value;
            this.child = child;
        }

        @Override
        public int compareTo(Pair o) {
            return value.compareTo(o.value);
        }
    }

    static <T> int indexOf(List<T> list, Predicate<T> predicate) {
        for (int i = 0; i < list.size(); i++) {
            if (predicate.test(list.get(i)))
                return i;
        }
        return -1;
    }

    private class Node {
        List<Pair> children = new ArrayList<>(maxCapacity + 1);
        Node parent;

        boolean isLeaf() {
            return children.isEmpty() || children.get(0).child == null;
        }

        T getIndex() {
            return children.isEmpty() ? null : children.get(0).value;
        }

        boolean add(T value) {
            return add(value, null);
        }

        boolean add(T value, Node node) {
            if (node != null)
                node.parent = this;
            if (isLeaf() || node != null) {
                if (indexOf(children, pair -> (pair.value.compareTo(value) == 0)) != -1)
                    return false;
                children.add(new Pair(value, node));
                children.sort(null);
                if (children.size() > maxCapacity) {
                    Node newNode = new Node();
                    for (int i = (maxCapacity + 1) / 2 + 1; i < children.size(); i++) {
                        newNode.children.add(children.get(i));
                    }
                    children.subList((maxCapacity + 1) / 2 + 1, children.size()).clear();
                    if (parent != null) {
                        parent.add(newNode.getIndex(), newNode);
                    } else {
                        Node newRoot = new Node();
                        newRoot.children.add(new Pair(this.getIndex(), this));
                        parent = newRoot;
                        newRoot.children.add(new Pair(newNode.getIndex(), newNode));
                        newNode.parent = newRoot;
                        root = newRoot;
                    }
                }
                return true;
            } else {
                int index = -1;
                for (int i = 0; i < children.size(); i++) {
                    if (value.compareTo(children.get(i).value) >= 0) {
                        index = i;
                        break;
                    }
                }
                if (index == -1) {
                    children.get(0).value = value;
                    index = 0;
                }
                return children.get(index).child.add(value);
            }
        }

        boolean remove(T value) {
            return true;
        }

        void toString(StringBuilder sb, int indent) {
            for (Pair pair : children) {
                for (int i = 0; i < indent; i++) sb.append("  ");
                sb.append(pair.value);
                sb.append('\n');
                if (pair.child != null)
                    pair.child.toString(sb, indent + 1);
            }
        }
    }

    BppTree() {
        this.maxCapacity = 4;
    }

    BppTree(int maxCapacity) {
        if (maxCapacity <= 0) throw new IllegalArgumentException("Max capacity must be positive");
        this.maxCapacity = maxCapacity;
    }

    BppTree(Collection<? extends T> collection) {
        this.maxCapacity = 4;
        addAll(collection);
    }

    BppTree(Collection<? extends T> collection, int maxCapacity) {
        this.maxCapacity = maxCapacity;
        addAll(collection);
    }



    @Override
    public boolean add(T value) {
        if (root == null) {
            root = new Node();
            root.children.add(new Pair(value));
            return true;
        }
        return root.add(value);
    }

    @Override
    public boolean remove(Object o) {
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null)
            return false;

        return true;
    }

    @Override
    public String toString() {
        if (root == null) {
            return "empty tree";
        }
        StringBuilder sb = new StringBuilder(size() * 10);
        sb.append("root");
        sb.append('\n');
        root.toString(sb, 1);
        return sb.toString();
    }
}
