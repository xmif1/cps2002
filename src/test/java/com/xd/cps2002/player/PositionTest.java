package com.xd.cps2002.player;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tester class for the Position class.
 * @author Xandru Mifsud
 */
public class PositionTest{
    private Position p = new Position(0,0);

    /**
     * Testing if object being compared to is null, then false is returned.
     */
    @Test
    public void nullObject_equalityTest(){
        assertNotEquals(p, null);
    }

    /**
     * Testing if object being compared to is the instance itself, then true is returned.
     */
    @Test
    public void selfComparison_equalityTest(){
        assertEquals(p, p);
    }

    /**
     * Testing if object being compared to is of a different instance, then false is returned.
     */
    @Test
    public void uncastableComparison_equalityTest(){

        int[] xy = new int[]{0, 0};
        assertNotEquals(p, xy);
    }

    /**
     * Testing if two different position instances with the same coordinates return true on comparison.
     */
    @Test
    public void trueComparison_equalityTest(){
        assertEquals(p, new Position(0,0));
    }

    /**
     * Testing if two different position instances with different coordinates return false on comparison.
     */
    @Test
    public void falseComparison_equalityTest(){
        assertNotEquals(p, new Position(0,1));
    }
}
