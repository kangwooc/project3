package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

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
    
    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new EmptyContainerException();
        }
        T min = this.heap[0];
        this.heap[0] = this.heap[this.size - 1];
        this.heap[this.size - 1] = null;
        this.size--;
        // empty or one element afterwards don't need to do anything
        if (this.size > 1) {
            percolateDown(0);
        }
        return min;
    }

    // base case does nothing
    // still confused???
    // null children?
    private void percolateDown(int index) {
        T temp = this.heap[index];  // parent
        int newIndex = index;
        int min = Integer.MAX_VALUE;
        for (int j = 1; j <= NUM_CHILDREN; j++) {
            int childIndex = NUM_CHILDREN * index + j;
            int difference = this.heap[index].compareTo(this.heap[childIndex]);
            if (difference < min) {
                newIndex = childIndex;
            }
        }
        this.heap[index] = this.heap[newIndex];  // parent = child
        this.heap[newIndex] = temp;  // child = parent
        percolateDown(newIndex);
    }
    
    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new EmptyContainerException();
        }
        return this.heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.size == this.heap.length) {
            this.doubleArraySize();
        }
        this.heap[this.size] = item;
        this.size++;
        if (this.size > 1) {
            percolateUp(this.size - 1);
        }
    }
    
    private void doubleArraySize() {
        T[] tempHeap = this.makeArrayOfT(this.heap.length * 2);
        for (int i = 0; i < this.size; i++) {
            tempHeap[i] = this.heap[i];
        }
        this.heap = tempHeap;
    }
    
    // base case does nothing
    // can temp store the data?
    private void percolateUp(int index) {
        T parent = this.heap[(index - 1) / NUM_CHILDREN];
        T child = this.heap[index];
        if (parent.compareTo(child) >= 0) {
            T temp = this.heap[index];  // child
            this.heap[index] = this.heap[(index - 1) / NUM_CHILDREN];  // child = parent
            this.heap[(index - 1) / NUM_CHILDREN] = temp;  // parent = child
            percolateUp((index - 1) / NUM_CHILDREN);
        }
    }

    @Override
    public int size() {
        return this.size;
    }
}