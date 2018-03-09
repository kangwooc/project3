package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.
    private static final int INITIAL_CAPACITY = 6;
    private int size;
    private IDictionary<T, Integer> dic;

    public ArrayDisjointSet() {
        this.pointers = new int[INITIAL_CAPACITY];
        this.size = 0;
        dic = new ChainedHashDictionary<>();
    }

    @Override
    public void makeSet(T item) {
        if (dic.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        if (this.size == INITIAL_CAPACITY) {
            pointers = this.doubleArrayCapacity(pointers);
        }
        dic.put(item, this.size);
        this.size++;
    }

    private int[] doubleArrayCapacity(int[] oldArray) {
        int[] newArray = new int[INITIAL_CAPACITY * 2];
        for (int i = 0; i < oldArray.length; i++) {
            newArray[i] = oldArray[i];
        }
        return newArray;
    }

    @Override
    public int findSet(T item) {
        if (!dic.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        return dic.get(item);
    }

    @Override
    public void union(T item1, T item2) {
        if (!dic.containsKey(item1) && !dic.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int firstRep = this.findSet(item1);
        int secondRep = this.findSet(item2);

        if (firstRep == secondRep) {
            throw new IllegalArgumentException();
        } else if (firstRep > secondRep) {
            firstRep = secondRep;
            dic.put(item1, firstRep);
        } else {
            secondRep = firstRep;
            dic.put(item2, secondRep);
        }
    }
}
