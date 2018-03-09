package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    @Test(timeout = 10 * SECOND)
    public void testInsertandremoveMinMany() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        int rep = 150000;
        for (int i = 0; i < rep; i++) {
            heap.insert(i);
        }
        assertEquals(0, heap.peekMin());
        assertEquals(rep, heap.size());
        for (int i = 0; i < rep; i++) {
            assertEquals(i, heap.removeMin());
        }
    }

    @Test(timeout = 10 * SECOND)
    public void testInsertReverselyAndremoveMinMany() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        int rep = 150000;
        for (int i = rep; i >= 0; i--) {
            heap.insert(i);
        }
        assertEquals(0, heap.peekMin());
        assertEquals(rep + 1, heap.size());
        for (int i = 0; i < rep; i++) {
            assertEquals(i, heap.removeMin());
        }
    }

    @Test(timeout = 10 * SECOND)
    public void testInsertIsEfficient() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        int cap = 500000;
        for (int i = 0; i < cap; i++) {
            heap.insert(i);
        }
        assertEquals(cap, heap.size());
    }

    @Test(timeout = 10 * SECOND)
    public void testInsertReverselyIsEfficient() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        int cap = 100000;
        for (int i = cap; i > 0; i--) {
            heap.insert(i);
        }
        assertEquals(cap, heap.size());
    }

    @Test(timeout = 10 * SECOND)
    public void testAddingManyandSorting() {
        IList<Integer> list = new DoubleLinkedList<>();
        int rep = 500000;
        for (int i = 0; i < rep; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(499995 + i, top.get(i));
        }
    }

    @Test(timeout = 10 * SECOND)
    public void testSortingManyNumbers() {
        IList<Integer> list = new DoubleLinkedList<>();
        int rep = 500000;
        for (int i = rep; i > 0; i--) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(rep, list);
        assertEquals(rep, top.size());
        int i = 1;
        for (int num : top) {
            assertEquals(i, num);
            i++;
        }
    }
}
