package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private static final int INITIAL_CAPACITY = 16;

    private IDictionary<K, V>[] chains;
    private float threshold;
    private int chainSize;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        chains = makeArrayOfChains(INITIAL_CAPACITY);
        threshold = 0.75f;
        chainSize = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
        int index = getHashOfKey(key) % chains.length;
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        return chains[index].get(key);
    }

    @Override
    public void put(K key, V value) {
        if (!containsKey(key)) {
            chainSize++;
        }
        if (loadFactor() > threshold) {
            chains = doubleArrayCapacity();
        }
        int index = getHashOfKey(key) % chains.length;
        if (chains[index] == null) {
            chains[index] = new ArrayDictionary<K, V>();
        }
        chains[index].put(key, value);
    }

    private float loadFactor() {
        return (float) chainSize / chains.length;
    }

    private IDictionary<K, V>[] doubleArrayCapacity() {
        IDictionary<K, V>[] result = makeArrayOfChains(chains.length << 1);
        for (int i = 0; i < chains.length; i++) {
            if (chains[i] != null) {
                for (KVPair<K, V> pair : chains[i]) {
                    int index = getHashOfKey(pair.getKey()) % result.length;
                    if (result[index] == null) {
                        result[index] = new ArrayDictionary<K, V>();
                    }
                    result[index].put(pair.getKey(), pair.getValue());
                }
            }
        }
        return result;
    }

    @Override
    public V remove(K key) {
        int index = getHashOfKey(key) % chains.length;
        if (chains[index] == null) {
            throw new NoSuchKeyException();
        }
        chainSize--;
        return chains[index].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        int index = getHashOfKey(key) % chains.length;
        if (chains[index] == null) {
            return false;
        }
        return chains[index].containsKey(key);
    }

    private int getHashOfKey(K key) {
        int hash = key != null ? key.hashCode() : 0;
        hash = Math.abs((hash << 5) - hash);
        return hash;
    }

    @Override
    public int size() {
        return chainSize;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private Iterator<KVPair<K, V>> iterator;
        private int index;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            iterator = null;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            if (iterator == null || !iterator.hasNext()) {
                if (index == chains.length) {
                    return false;
                } else {
                    getIterator();
                    return hasNext();
                }
            } else {
                return true;
            }

        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return iterator.next();
        }

        private void getIterator() {
            IDictionary<K, V> dictionary = chains[index++];
            if (dictionary == null) {
                iterator = null;
            } else {
                iterator = dictionary.iterator();
            }
        }
    }
}
