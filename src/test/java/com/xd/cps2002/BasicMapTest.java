package com.xd.cps2002;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class BasicMapTest {

    BasicMap basicMap;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public  void setup() {
        // Create a new empty BasicMap object with size 5
        basicMap = new BasicMap(5);
    }

    @After
    public void teardown() {
        // Remove the reference to the previous BasicMap object.
        basicMap = null;
    }

    @Test
    public void BasicMap_setsMapSize_IfGivenAValidMapSize() {
        // Try to initialize the map with some valid size (between 5-50)
        int size = 19;
        basicMap = new BasicMap(size);

        // Check that the map size was set correctly
        assertEquals(size, basicMap.getSize());
    }

    @Test
    public void BasicMap_throwsIllegalArgumentException_IfGivenMapSizeIsTooSmall() {
        // Expect BasicMap to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("BasicMap was initialized with an invalid size argument.");

        // Try to initialize the map with a size below the valid size range (between 5-50)
        int size = 4;
        basicMap = new BasicMap(size);
    }

    @Test
    public void BasicMap_throwsIllegalArgumentException_IfGivenMapSizeIsTooBig() {
        // Expect BasicMap to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("BasicMap was initialized with an invalid size argument.");

        // Try to initialize the map with a size above the valid size range (between 5-50)
        int size = 51;
        basicMap = new BasicMap(size);
    }

    @Test
    public void isValidPosition_returnsTrue_ifPositionIsValid() {
        // Try to check if a valid position (x/y coordinates in range 0-4) exists within the 5x5 map
        int x = 3, y = 2;

        // Expect the function to return true
        assertTrue(basicMap.isValidPosition(x,y));
    }
}
