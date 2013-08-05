package com.taobao.csp.capacity.model.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class BreadthFirstTreeIterator <T> implements Iterator<Node<T>> {

    private static final int ROOT = 0;

    private LinkedList<Node<T>> _queue;
    private Map<Integer, ArrayList<Integer>> _levels;

    public BreadthFirstTreeIterator(ArrayList<Node<T>> tree) {
        _queue = new LinkedList<Node<T>>();
        _levels = new HashMap<Integer, ArrayList<Integer>>();
        this.buildTreeLevels(tree, ROOT, 0);
        for (Map.Entry<Integer, ArrayList<Integer>> entry : _levels.entrySet()) {
            for (Integer forwardPointer : entry.getValue()) {
                _queue.add(tree.get(forwardPointer));
            }
        }
    }

    /*
       See URL:

       1. http://en.wikipedia.org/wiki/Breadth-first_search
    */
    private void buildTreeLevels(ArrayList<Node<T>> tree, int index, int level) {
        if (level == ROOT) {
            _queue.add(tree.get(index));
        }
        ArrayList<Integer> forwardPointers = tree.get(index).getForwardPointers();

        if (!_levels.containsKey(level)) {
            _levels.put(level, new ArrayList<Integer>());
        }
        for (Integer forwardPointer : forwardPointers) {
            _levels.get(level).add(forwardPointer);

            // Recursive call.
            this.buildTreeLevels(tree, forwardPointer, level + 1);
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
