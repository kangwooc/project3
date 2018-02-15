package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout = SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout = SECOND)
    public void testSortingReversely() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 20; i >= 0; i--) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout = SECOND)
    public void testSortingSameValues() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(1);
        }
        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(1, top.get(i));
        }
    }

    @Test(timeout = SECOND)
    public void testSortingOneValue() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(1, list);
        assertEquals(1, top.size());
        assertEquals(19, top.get(0));
    }

    @Test(timeout = SECOND)
    public void testHandlingError() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        try {
            Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {

        }
    }

    @Test(timeout = SECOND)
    public void testSortingRandomList() {
        IList<Integer> list1 = new DoubleLinkedList<>();
        list1.add(3);
        list1.add(4);
        list1.add(1);
        list1.add(5);
        list1.add(0);
        List<Integer> list2 = new LinkedList<>();
        list2.add(3);
        list2.add(4);
        list2.add(1);
        list2.add(5);
        list2.add(0);
        IList<Integer> top = Searcher.topKSort(5, list1);
        Collections.sort(list2);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(list2.get(i), top.get(i));
        }
    }
}
