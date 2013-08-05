package com.taobao.csp.capacity.model.tree;

import java.util.ArrayList;
import java.util.Iterator;

public class Tree <T> implements Iterable<Node<T>> {

    private static final int NOT_FOUND = -1;

    private ArrayList<Node<T>> _nodes;
    private TraversalStrategy _traversalStrategy;

    // constructors
    public Tree() {
        this(TraversalStrategy.BREADTH_FIRST);
    }

    public Tree(TraversalStrategy traversalStrategy) {
        _nodes = new ArrayList<Node<T>>();
        _traversalStrategy = traversalStrategy;
    }

    // properties
    public ArrayList<Node<T>> getNodes() {
        return _nodes;
    }

    public TraversalStrategy getTraversalStrategy() {
        return _traversalStrategy;
    }

    public void setTraversalStrategy(TraversalStrategy traversalStrategy) {
        _traversalStrategy = traversalStrategy;
    }

    // public interface
    public int indexOf(T identifier) {
        int result = NOT_FOUND;
        int index = 0;
        for (Node<T> node : _nodes) {
            if (node.getIdentifier().equals(identifier)) {
                result = index;
                break;
            }
            index++;
        }
        return result;
    }

    public Node<T> createNode(T identifier) {
        return this.createNode(identifier, null);
    }

    public Node<T> createNode(T identifier, T parent) {
        Node<T> node = new Node<T>(identifier);
        _nodes.add(node);
        Node<T> parantNode = this.setForwardPointer(identifier, parent);
        node.set_parent(parantNode);
        
        return node;
    }

    public int size() {
        return _nodes.size();
    }

    public Iterator<Node<T>> iterator() {
        return _traversalStrategy == TraversalStrategy.BREADTH_FIRST 
            ? new BreadthFirstTreeIterator<T> (_nodes) : new DepthFirstTreeIterator<T>(_nodes);
    }

    // private
    private Node<T> setForwardPointer(T identifier, T parent) {
        if (parent != null) {
            int parentIndex = this.indexOf(parent);
            _nodes.get(parentIndex).addForwardPointer(this.indexOf(identifier));
            return _nodes.get(parentIndex);
        }
        
        return null;
    }
}
