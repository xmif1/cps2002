package com.xd.cps2002.map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.xd.cps2002.map.MapCreator.createMap;
import static org.junit.Assert.*;

public class MapCreatorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void creatorMethod_returnsBasicMapWithCorrectSize_ifGivenMapTypeBasic() {
        // Create a "BasicMap" object of size 6 using "MapCreator"
        int size = 8;
        Map map = createMap("basic", size);

        // Check that the created map is of type BasicMap
        assertTrue(map instanceof BasicMap);

        // Check that the created map has the specified size
        assertEquals(size, map.getSize());
    }

    @Test
    public void creatorMethod_returnsBasicMapWithCorrectSize_ifGivenMapTypeBasicWithDifferentCasing() {
        // Create a "BasicMap" object of size 6 using "MapCreator"
        // The "mapType" argument is now given in a different casing to see if this affects "MapCreator"
        int size = 8;
        Map map = createMap("BaSiC", size);

        // Check that the created map is of type BasicMap
        assertTrue(map instanceof BasicMap);

        // Check that the created map has the specified size
        assertEquals(size, map.getSize());
    }

    @Test
    public void creatorMethod_throwsIllegalArgumentException_ifMapTypeDoesNotExist() {
        // Expect the method to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid map type.");

        // Create a non-existing map type "unknown" using "MapCreator"
        int size = 8;
        Map map = createMap("unknown", size);
    }
}
