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
    private static final int INITIAL_CAPACITY = 12;
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
        int rank = 0;
        int index = this.size;
        this.size++;
        if (index == this.pointers.length) {
            pointers = this.doubleArrayCapacity();
        }
        this.dic.put(item, index);
        this.pointers[index] = -1 * rank - 1;
    }

    private int[] doubleArrayCapacity() {
        int[] newArray = new int[this.pointers.length * 2];
        for (KVPair<T, Integer> pair : dic) {
            newArray[pair.getValue()] = pointers[pair.getValue()];
        }
        return newArray;
    }

    @Override
    public int findSet(T item) {
        if (!dic.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int index = this.dic.get(item);
        return findSet(index);
    }

    private int findSet(int index) {
        if (this.pointers[index] < 0) {
            return index;
        } else {
            return findSet(this.pointers[index]);
        }
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
        } else {
            int firstRank = -1 * this.pointers[firstRep] - 1;
            int secondRank = -1 * this.pointers[secondRep] - 1;
            if (firstRank < secondRank) {
                this.pointers[firstRep] = secondRep;
            } else if (firstRank > secondRank) {
                this.pointers[secondRep] = firstRep;
            } else {
                this.pointers[firstRep] = secondRep;
                this.pointers[secondRep] = -1 * (secondRank + 1) - 1;
            }
        }
    }
}