package com.dabek.jakub.bpptree;

import java.util.*;
import java.util.function.Predicate;

public class BppTree<T extends Comparable<T>> extends AbstractCollection<T> {
    private final int maxCapacity;
    private int size;
    private Node root;


    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
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

        int indexOfRelevantNodeForValue(T value) {
            int index = indexOf(children, pair -> value.compareTo(pair.value) < 0);
            if (index == -1) {
                index = children.size() - 1;
            } else if (index != 0){
                index--;
            }
            return index;
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
                if (node != null)
                    node.parent = this;
                children.sort(null);
                if (children.size() > maxCapacity) {
                    Node newNode = new Node();
                    List<Pair> toTransfer = children.subList((maxCapacity + 1) / 2, children.size());
                    newNode.children.addAll(toTransfer);
                    newNode.updateParentsOfElements();
                    toTransfer.clear();
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
                int index = indexOfRelevantNodeForValue(value);
                if (index == 0) {
                    children.get(index).value = value;
                }
                return children.get(index).child.add(value);
            }
        }

        private void updateParentsOfElements() {
            if (!isLeaf()) {
                for (Pair pair : children) {
                    pair.child.parent = this;
                }
            }
        }

        private void updateIndex() {
            for (Pair pair : children) {
                T newIndex = pair.child.getIndex();
                if (newIndex.compareTo(pair.value) != 0){
                    pair.value = newIndex;
                    if (parent != null)
                        parent.updateIndex();
                }
            }
        }

        boolean remove(T value) {
            return remove(value, false);
        }


        boolean remove(T value, boolean inner) {
            if (isLeaf() || inner) {
                int index = indexOf(children, pair -> value.compareTo(pair.value) == 0);
                if (index == -1)
                    return false;
                children.remove(index);
                if (parent != null)
                    parent.updateIndex();
                if (children.size() < ((maxCapacity + 1) / 2) && parent != null) {
                    int indexOfThisInParent = indexOf(parent.children, pair -> pair.child == this);
                    if (indexOfThisInParent != parent.children.size() - 1) {
                        Pair siblingPair = parent.children.get(indexOfThisInParent + 1);
                        int toTransferAmount = siblingPair.child.children.size() - ((maxCapacity + 1) / 2);
                        if(toTransferAmount > 0) {
                            List<Pair> toTransfer = siblingPair.child.children.subList(0, toTransferAmount);
                            children.addAll(toTransfer);
                            updateParentsOfElements();
                            toTransfer.clear();
                            siblingPair.value = siblingPair.child.getIndex();
                        } else {
                            children.addAll(siblingPair.child.children);
                            updateParentsOfElements();
                            parent.remove(siblingPair.value, true);
                        }
                    } else {
                        Pair siblingPair = parent.children.get(indexOfThisInParent - 1);
                        int toTransferAmount = siblingPair.child.children.size() - ((maxCapacity + 1) / 2);
                        if(toTransferAmount > 0) {
                            List<Pair> toTransfer = siblingPair.child.children.subList(toTransferAmount - 1, siblingPair.child.children.size());
                            children.addAll(toTransfer);
                            updateParentsOfElements();
                            toTransfer.clear();
                            siblingPair.value = siblingPair.child.getIndex();
                        } else {
                            children.addAll(siblingPair.child.children);
                            updateParentsOfElements();
                            parent.remove(siblingPair.value, true);
                            parent.updateIndex();
                        }
                    }
                    return true;
                }
                return true;
            } else {
                return children.get(indexOfRelevantNodeForValue(value)).child.remove(value);
            }
        }

        boolean contains(T value) {
            int index = -1;
            for (int i = 0; i < children.size(); i++) {
                if (value.compareTo(children.get(i).value) == 0) {
                    return true;
                } else if (value.compareTo(children.get(i).value) > 0) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                return false;
            }
            return isLeaf() && children.get(index).child.contains(value);
        }

        void toString(StringBuilder sb, int indent) {
            for (Pair pair : children) {
                for (int i = 0; i < indent; i++) sb.append("    ");
                sb.append(pair.value);
                sb.append('\n');
                if (pair.child != null)
                    pair.child.toString(sb, indent + 1);
            }
        }
    }

    public BppTree() {
        this.maxCapacity = 4;
    }

    public BppTree(int maxCapacity) {
        if (maxCapacity <= 0) throw new IllegalArgumentException("Max capacity must be positive");
        this.maxCapacity = maxCapacity;
    }

    public BppTree(Collection<? extends T> collection) {
        this.maxCapacity = 4;
        addAll(collection);
    }

    public BppTree(Collection<? extends T> collection, int maxCapacity) {
        this.maxCapacity = maxCapacity;
        addAll(collection);
    }

    @Override
    public boolean add(T value) {
        if (root == null) {
            root = new Node();
            root.children.add(new Pair(value));
            size++;
            return true;
        }
        if (root.add(value)) {
            size++;
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        if (root == null) return false;

        if (root.remove((T) o)) {
            size--;
            if (root.children.isEmpty())
                root = null;
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        if (o == null || root == null)
            return false;

        return root.contains((T) o);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
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
