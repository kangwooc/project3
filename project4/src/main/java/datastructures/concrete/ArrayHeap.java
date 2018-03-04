package datastructures.concrete;

import java.util.NoSuchElementException;

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
    private int heapSize;

    public ArrayHeap() {
        this.heap = this.makeArrayOfT(21);
        this.heapSize = 0;
    }

    /**
     * This method will return a new, empty array of the given heapSize that can
     * contain elements of type T.
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
        if (this.heapSize == 0) {
            throw new EmptyContainerException();
        }
        T min = this.heap[0];
        this.heap[0] = this.heap[this.heapSize - 1];
        this.heap[this.heapSize - 1] = null;
        this.heapSize--;
        // empty or one element afterwards don't need to do anything
        if (this.heapSize > 1) {
            percolateDown(0);
        }
        return min;
    }

    private void percolateDown(int index) {
        T min = null;
        int childNum = index;
        for (int i = 1; i <= NUM_CHILDREN; i++) {
            if (NUM_CHILDREN * index + i < this.heapSize && heap[NUM_CHILDREN * index + i] != null
                    && heap[NUM_CHILDREN * index + i].compareTo(heap[index]) < 0) {
                if (min == null || min != null 
                        && heap[index * NUM_CHILDREN + i].compareTo(min) < 0) {
                    min = heap[index * NUM_CHILDREN + i];
                    childNum = i;
                }
            }
        }
        if (min != null) {
            heap[index * NUM_CHILDREN + childNum] = heap[index];
            heap[index] = min;
            percolateDown(index * NUM_CHILDREN + childNum);
        }
    }

    @Override
    public T peekMin() {
        if (this.heapSize == 0) {
            throw new EmptyContainerException();
        }
        return this.heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.heapSize == this.heap.length) {
            this.doubleArrayheapSize();
        }
        this.heap[this.heapSize] = item;
        this.heapSize++;
        if (this.heapSize > 1) {
            percolateUp(this.heapSize - 1);
        }
    }

    private void doubleArrayheapSize() {
        T[] tempHeap = this.makeArrayOfT(this.heap.length * 2);
        for (int i = 0; i < this.heapSize; i++) {
            tempHeap[i] = this.heap[i];
        }
        this.heap = tempHeap;
    }

    private void percolateUp(int index) {
        if (index > 0) {
            T parent = this.heap[(index - 1) / NUM_CHILDREN];
            T child = this.heap[index];
            if (parent.compareTo(child) >= 0) {
                T temp = this.heap[index]; // child
                this.heap[index] = this.heap[(index - 1) / NUM_CHILDREN]; // child = parent
                this.heap[(index - 1) / NUM_CHILDREN] = temp; // parent = child
                percolateUp((index - 1) / NUM_CHILDREN);
            }
        }
    }

    @Override
    public int size() {
        return this.heapSize;
    }
    
    @Override
    public void remove(T item) {
        if (item == null) {
            throw new NoSuchElementException();
        }
    }
}
