package datastructures.sorting;

import misc.BaseTest;
import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    @Test(timeout = 10 * SECOND)
    public void testPlaceholder() {
        
        assertTrue(true);
    }

    @Test(timeout = 10 * SECOND)
    public void testInsertandremoveMinMany() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        int rep = 15000;
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
        int rep = 15000;
        for (int i = rep; i >= 0; i--) {
            heap.insert(i);
        }
        assertEquals(0, heap.peekMin());
        assertEquals(101, heap.size());
        for (int i = 0; i < rep; i++) {
            heap.removeMin();
        }
        assertTrue(heap.isEmpty());
    }
}
