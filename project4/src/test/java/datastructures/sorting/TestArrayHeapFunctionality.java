package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout = SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }

    // new tests below
    @Test(timeout = SECOND)
    public void testInsertandremoveMinBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 3; i++) {
            heap.insert(i);
        }

        for (int i = 0; i < 3; i++) {
            assertEquals(i, heap.removeMin());
        }
        assertTrue(heap.isEmpty());
    }

    @Test(timeout = SECOND)
    public void testInsertAndremoveMinReversely() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 100; i >= 0; i--) {
            heap.insert(i);
        }
        assertEquals(0, heap.peekMin());
        assertEquals(101, heap.size());
        int i = 0;
        while (!heap.isEmpty()) {
            assertEquals(i, heap.removeMin());
            i++;
        }
    }

    @Test(timeout = SECOND)
    public void testRemoveMinErrorHandling() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // This is ok: do nothing
        }
        heap.insert(3);
        heap.removeMin();
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // This is ok: do nothing
        }
        assertTrue(heap.isEmpty());
    }

    @Test(timeout = SECOND)
    public void testInsertErrorHandling() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // This is ok: do nothing
        }
        for (int i = 100; i >= 0; i--) {
            heap.insert(i);
        }
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // This is ok: do nothing
        }
    }

    @Test(timeout = SECOND)
    public void testpeekMinBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 3; i++) {
            heap.insert(i);
        }
        assertEquals(0, heap.peekMin());
        assertEquals(3, heap.size());
        assertTrue(!heap.isEmpty());
    }

    @Test(timeout = SECOND)
    public void testpeekMinErrorHandling() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // This is ok: do nothing
        }
    }

    @Test(timeout = SECOND)
    public void testpeekMinAndInsert() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = -5; i < 5; i++) {
            heap.insert(i);
        }
        assertEquals(-5, heap.peekMin());
        assertEquals(10, heap.size());
    }

    @Test(timeout = SECOND)
    public void testpeekMinAndInsertReversely() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 100; i >= 0; i--) {
            heap.insert(i);
        }
        assertEquals(0, heap.peekMin());
        assertEquals(101, heap.size());
    }

    @Test(timeout = SECOND)
    public void testInsertRandomly() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(7);
        heap.insert(9);
        heap.insert(1);
        heap.insert(0);
        heap.insert(1);
        heap.insert(6);

        assertEquals(0, heap.peekMin());
        assertEquals(6, heap.size());
    }

    @Test(timeout = SECOND)
    public void testDifferentType() {
        IPriorityQueue<Character> heap = this.makeInstance();
        for (char i = 'a'; i <= 'z'; i++) {
            heap.insert(i);
        }
        assertEquals('a', heap.peekMin());
        assertEquals(26, heap.size());
    }
}
