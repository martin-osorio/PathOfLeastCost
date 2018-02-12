package com.example.pathoflowestcost;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for the Pathfinder class
 */
public class PathfinderTest {
    @Test
    public void findPath() throws Exception {
        String s = "1,1,1,1\n1,1,1,1\n1,1,1,1\n1,1,1,1\n";
        assertEquals(Pathfinder.findPath(s).getTotalCost(), 4);
    }

    @Test
    public void getTableFromCommaDelimitedString() throws Exception {
        String s = "1,1,1,1\n1,1,1,1\n1,1,1,1\n1,1,1,1\n";
        List<Integer> row = new ArrayList<>();
        row.add(1);
        row.add(1);
        row.add(1);
        row.add(1);
        List<List<Integer>> l = new ArrayList<>();
        l.add(row);
        l.add(row);
        l.add(row);
        l.add(row);
        assertEquals(Pathfinder.getTableFromCommaDelimitedString(s), l);
    }

    @Test
    public void getNextYAbove() throws Exception {
        assertEquals(Pathfinder.getNextYAbove(0, 5), 1);
        assertEquals(Pathfinder.getNextYAbove(4, 5), 0);
    }

    @Test
    public void getNextYBelow() throws Exception {
        assertEquals(Pathfinder.getNextYBelow(0, 5), 4);
        assertEquals(Pathfinder.getNextYBelow(4, 5), 3);
    }

    @Test
    public void testSample1() throws Exception {
        String s = "3,4,1,2,8,6\n6,1,8,2,7,4\n5,9,3,9,9,5\n8,4,1,3,2,6\n3,7,2,8,6,4";
        assertTrue(Pathfinder.findPath(s).isSuccess());
        assertEquals(16, Pathfinder.findPath(s).getTotalCost());
    }

    @Test
    public void testSample2() throws Exception {
        String s = "3,4,1,2,8,6\n6,1,8,2,7,4\n5,9,3,9,9,5\n8,4,1,3,2,6\n3,7,2,1,2,3";
        assertTrue(Pathfinder.findPath(s).isSuccess());
        assertEquals(11, Pathfinder.findPath(s).getTotalCost());
    }

    @Test
    public void testSample3() throws Exception {
        String s = "19,10,19,10,19\n21,23,20,19,12\n20,12,20,11,10";
        assertFalse(Pathfinder.findPath(s).isSuccess());
        assertEquals(48, Pathfinder.findPath(s).getTotalCost());
    }

    @Test
    public void testSample4() throws Exception {
        String s = "5,8,5,3,5";
        assertTrue(Pathfinder.findPath(s).isSuccess());
        assertEquals(26, Pathfinder.findPath(s).getTotalCost());
    }

    @Test
    public void testSample5() throws Exception {
        String s = "5\n8\n5\n3\n5";
        assertTrue(Pathfinder.findPath(s).isSuccess());
        assertEquals(3, Pathfinder.findPath(s).getTotalCost());
    }

    //TODO

    @Test
    public void testSample8() throws Exception {
        String s = "69,10,19,10,19\n51,23,20,19,12\n60,12,20,11,10";
        assertFalse(Pathfinder.findPath(s).isSuccess());
        assertEquals(0, Pathfinder.findPath(s).getTotalCost());
    }

    @Test
    public void testSample9() throws Exception {
        String s = "60,3,3,6\n6,3,7,9\n5,6,8,3";
        assertTrue(Pathfinder.findPath(s).isSuccess());
        assertEquals(14, Pathfinder.findPath(s).getTotalCost());
    }

    @Test
    public void testSample10() throws Exception {
        String s = "6,3,-5,9\n-5,2,4,10\n3,-2,6,10\n6,-1,-2,10";
        assertTrue(Pathfinder.findPath(s).isSuccess());
        assertEquals(0, Pathfinder.findPath(s).getTotalCost());
    }

    @Test
    public void testSample11() throws Exception {
        String s = "51,51\n0,51\n51,51\n5,5";
        assertTrue(Pathfinder.findPath(s).isSuccess());
        assertEquals(10, Pathfinder.findPath(s).getTotalCost());
    }

    @Test
    public void testSample12() throws Exception {
        String s = "51,51,51\n0,51,51\n51,51,51\n5,5,51";
        assertFalse(Pathfinder.findPath(s).isSuccess());
        assertEquals(10, Pathfinder.findPath(s).getTotalCost());
    }

    @Test
    public void testSample13() throws Exception {
        String s = "1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1\n2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2";
        assertTrue(Pathfinder.findPath(s).isSuccess());
        assertEquals(20, Pathfinder.findPath(s).getTotalCost());
    }
}
