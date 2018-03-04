package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    private Pair<K, V>[] pairs;
    // You're encouraged to add extra fields (and helper methods) though!
    private static final int INITIAL_CAPACITY = 3;

    private int size;

    public ArrayDictionary() {
        pairs = makeArrayOfPairs(INITIAL_CAPACITY);
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    @Override
    public V get(K key) {
        for (int i = 0; i < size; i++) {
            if (isSameKey(pairs[i].key, key)) {
                return pairs[i].value;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
        int index = getIndexOfKey(key);
        if (index > -1) {
            pairs[index].value = value;
        } else {
            if (size == pairs.length) {
                pairs = doubleArrayCapacity();
            }
            pairs[size] = new Pair<K, V>(key, value);
            size++;
        }
    }

    private Pair<K, V>[] doubleArrayCapacity() {
        Pair<K, V>[] result = makeArrayOfPairs(pairs.length * 2);
        for (int i = 0; i < size; i++) {
            result[i] = pairs[i];
        }
        return result;
    }

    @Override
    public V remove(K key) {
        int index = getIndexOfKey(key);
        if (index > -1) {
            V removed = pairs[index].value;
            for (int i = index; i < size - 1; i++) {
                pairs[i] = pairs[i + 1];
            }
            size--;
            return removed;
        } else {
            throw new NoSuchKeyException();
        }
    }

    private int getIndexOfKey(K key) {
        for (int i = 0; i < size; i++) {
            if (isSameKey(pairs[i].key, key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < size; i++) {
            if (isSameKey(pairs[i].key, key)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSameKey(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<K, V>(pairs, size);
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] pairs;
        private int size;
        private int index;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.pairs = pairs;
            this.size = size;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return new KVPair<K, V>(pairs[index].key, pairs[index++].value);
        }
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
