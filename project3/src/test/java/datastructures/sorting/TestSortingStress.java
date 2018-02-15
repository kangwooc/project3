package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

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
        assertEquals(rep, heap.size());
        assertEquals(0, heap.peekMin());

        for (int i = rep; i > 0; i--) {
            heap.removeMin();
        }
        assertTrue(heap.isEmpty());
    }

    @Test(timeout = 10 * SECOND)
    public void testInsertAndremoveMinManyReversely() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        int rep = 150000;
        for (int i = rep; i >= 0; i--) {
            heap.insert(i);
        }
        assertEquals(0, heap.peekMin());
        assertEquals(150001, heap.size());
        for (int i = 0; i < rep; i++) {
            heap.removeMin();
        }
        assertTrue(heap.isEmpty());
    }

    @Test(timeout = 10 * SECOND)
    public void testInsertIsEfficient() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        int cap = 500000;
        for (int i = 0; i < cap; i++) {
            heap.insert(i * 2);
        }
        assertEquals(cap, heap.size());
    }

    @Test(timeout = 10 * SECOND)
    public void testInsertReverselyIsEfficient() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        int cap = 500000;
        for (int i = cap; i > 0; i++) {
            heap.insert(i * 2);
        }
        assertEquals(cap, heap.size());
    }

    @Test(timeout = 10 * SECOND)
    public void testSortingManyList() {
        IList<Integer> list = new DoubleLinkedList<>();
        int rep = 500000;
        for (int i = 0; i < rep; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(rep, list);
        assertEquals(5, top.size());
    }
}
