package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see the source
 * code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (size == 0) {
            front = new Node<T>(item);
            back = front;
        } else {
            back.next = new Node<T>(back, item, null);
            back.next.prev = back;
            back = back.next;
        }
        size++;
    }

    @Override
    public T remove() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        T removed = back.data;
        if (size == 1) {
            back = null;
            front = null;
        } else {
            back = back.prev;
            back.next = null;
        }
        size--;
        return removed;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return getNode(index).data;
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            front = new Node<T>(null, item, null);
            back = front;
        } else if (index == 0) {
            front.next.prev = new Node<T>(null, item, front.next);
            front = front.next.prev;
        } else if (index == size - 1) {
            back.prev.next = new Node<T>(back.prev, item, null);
            back = back.prev.next;
        } else {
            Node<T> current = getNode(index);
            current.prev.next = new Node<T>(current.prev, item, current.next);
            current.next.prev = current.prev.next;
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= size + 1) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 0 || index == size) { // check the size twice
            add(item);
        } else {
            if (index == 0) {
                front = new Node<T>(null, item, front);
                front.next.prev = front;
            } else if (index == size - 1) {
                back.prev = new Node<T>(back.prev, item, back);
                back.prev.prev.next = back.prev;
            } else {
                Node<T> current = getNode(index);
                current.prev = new Node<T>(current.prev, item, current);
                current.prev.prev.next = current.prev;
            }
            size++;
        }
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1 || index == size - 1) {
            return remove();
        } else {
            T deleted = front.data;
            if (index == 0) {
                front = front.next;
                front.prev = null;
            } else {
                Node<T> current = getNode(index);
                current.prev.next = current.next;
                current.next.prev = current.prev;
                deleted = current.data;
            }
            size--;
            return deleted;
        }
    }
    
    // More efficient way of accessing node from given index
    private Node<T> getNode(int index) {
        Node<T> current = front;
        int count = 0;
        if (index <= (size / 2)) {
            while (count != index) {
                current = current.next;
                count++;
            }
        } else {
            count = size - 1;
            current = back;
            while (count != index) {
                current = current.prev;
                count--;
            }
        }
        return current;
    }

    @Override
    public int indexOf(T item) {
        Node<T> current = front;
        int index = 0;
        while (current != null) {
            if (isSameItem(current.data, item)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        Node<T> current = front;
        while (current != null) {
            if (isSameItem(current.data, other)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    
    private boolean isSameItem(T item1, T item2) {
        return item1 == item2 || item1 != null && item1.equals(item2);
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.

    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at; returns 'false'
         * otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the iterator to
         * advance one element forward.
         *
         * @throws NoSuchElementException
         *             if we have reached the end of the iteration and there are no more
         *             elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T item = current.data;
            current = current.next;
            return item;
        }
    }
}
