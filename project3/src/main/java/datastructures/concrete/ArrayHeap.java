package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Feel free to add more fields and constants.
    private int size;

    public ArrayHeap() {
        this.heap = this.makeArrayOfT(21);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }
    
    // empty or one element afterwards don't need to do anything
    @Override
    public T removeMin() {
        if (this.size == 0) {
            throw new EmptyContainerException();
        }
        T min = this.heap[0];
        this.heap[0] = this.heap[this.size - 1];
        this.heap[this.size - 1] = null;
        this.size--;
        if (this.size > 1) {
            percolateDown(0);
        }
        return min;
    }

    // null children?
    // base case?
    private void percolateDown(int index) {
        int newIndex = index;
        int min = 10000;
        for (int j = 1; j <= NUM_CHILDREN; j++) {
            int childIndex = NUM_CHILDREN * index + j;
            int difference = this.heap[index].compareTo(this.heap[childIndex]);
            if (difference < min) {
                newIndex = childIndex;
            }
        }
        percolateDown(newIndex);
    }
    
    @Override
    public T peekMin() {
        if (this.size == 0) {
            throw new EmptyContainerException();
        }
        return this.heap[0];
    }

    // percolate up
    @Override
    public void insert(T item) {
        // T parent = Math.floor((index - 1) / NUM_CHILDREN);
        throw new NotYetImplementedException();
    }

    @Override
    public int size() {
        return this.size;
    }
}