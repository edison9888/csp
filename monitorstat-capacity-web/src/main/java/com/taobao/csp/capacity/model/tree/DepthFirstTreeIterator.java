package com.taobao.csp.capacity.model.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class DepthFirstTreeIterator <T> implements Iterator<Node<T>> {

    private static final int ROOT = 0;

    private LinkedList<Node<T>> _queue;

    public DepthFirstTreeIterator(ArrayList<Node<T>> tree) {
        _queue = new LinkedList<Node<T>>();
        this.buildQueue(tree, ROOT);
    }

    /*
       See URL:

       1. http://en.wikipedia.org/wiki/Depth-first_search
    */
    private void buildQueue(ArrayList<Node<T>> tree, int index) {
        _queue.add(tree.get(index));
        ArrayList<Integer> forwardPointers = tree.get(index).getForwardPointers();
        for (Integer forwardPointer : forwardPointers) {
            
            // Recursive call.
            this.buildQueue(tree, forwardPointer);
        }
    }

    @Override
    public boolean hasNext() {
        return !_queue.isEmpty();
    }

    @Override
    public Node<T> next() {
        return _queue.poll();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
