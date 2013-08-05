package com.taobao.csp.capacity.model.tree;

import java.util.ArrayList;

public class Node<T> {
    private T _identifier;
    private ArrayList<Integer> _forwardPointers;
    private Node<T> _parent;

    // constructor
    public Node(T identifier) {
        _identifier = identifier;
        _forwardPointers = new ArrayList<Integer>();
    }

    // properties
    public T getIdentifier() {
        return _identifier;
    }

    public ArrayList<Integer> getForwardPointers() {
        return _forwardPointers;
    }
    
    public Node<T> get_parent() {
		return _parent;
	}

	public void set_parent(Node<T> _parent) {
		this._parent = _parent;
	}

	// public interface
    public void addForwardPointer(int index) {
        _forwardPointers.add(index);
    }
    
    // public interface
    public boolean isLeaf() {
    	if ( _forwardPointers.isEmpty()) {
    		return true;
    	} else {
    		return false;
    	}
    }
}
