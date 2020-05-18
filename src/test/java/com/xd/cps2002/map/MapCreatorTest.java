package com.xd.cps2002.map;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static com.xd.cps2002.map.MapCreator.createMap;
import static org.junit.Assert.*;

/**
 * This class contains unit tests used to test the methods.
 */
public class MapCreatorTest {

    // Map array used to test map creation with an array of tiles
    private final TileType[][] testTiles =
            {{TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass, TileType.Treasure},
                    {TileType.Grass, TileType.Grass, TileType.Water, TileType.Water, TileType.Water},
                    {TileType.Water, TileType.Grass, TileType.Grass, TileType.Water, TileType.Water},
                    {TileType.Water, TileType.Grass, TileType.Grass, TileType.Water, TileType.Water},
                    {TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass, TileType.Grass}};

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        // Reset the singleton instance in "MapCreator" to null before each test
        MapCreator.instance = null;
    }

    /**
     * This teardown method is important to avoid the static map instances created in these tests from interfering with
     * the unit tests of other classes. This is important since most unit tests assume that "MapCreator" will always
     * return the map instance that they requested. However, this may not be the case if the unit tests are not run in
     * in order; for example, if a unit test from {@link com.xd.cps2002.game.HTMLGeneratorTest} is run right after one
     * of the tests in this class.
     */

    @After
    public void teardown() {
        // Reset the singleton instance in "MapCreator" to null after each test
        MapCreator.instance = null;
    }

    /* Tests for the version of the "createMap" function which take map size as an argument */

    @Test
    public void createMapOfSize_returnsBasicMapWithCorrectSize_ifGivenMapTypeBasic() {
        // Create a "BasicMap" object of size 8 using "MapCreator"
        int size = 8;
        Map map = createMap("basic", size);

        // Check that the created map is of type BasicMap
        assertTrue(map instanceof BasicMap);

        // Check that the created map has the specified size
        assertEquals(size, map.getSize());
    }

    @Test
    public void createMapOfSize_returnsBasicMapWithCorrectSize_ifGivenMapTypeBasicWithDifferentCasing() {
        // Create a "BasicMap" object of size 6 using "MapCreator"
        // The "mapType" argument is now given in a different casing to see if this affects "MapCreator"
        int size = 12;
        Map map = createMap("BaSiC", size);

        // Check that the created map is of type BasicMap
        assertTrue(map instanceof BasicMap);

        // Check that the created map has the specified size
        assertEquals(size, map.getSize());
    }

    @Test
    public void createMapOfSize_returnsBasicMapWithPlayableTiles_ifGivenMapTypeBasic() {
        // Create a "BasicMap" object of size 13 using "MapCreator"
        int size = 13;
        Map map = createMap("basic", size);

        // Check that the created map is of type BasicMap
        assertTrue(map instanceof BasicMap);

        // Check that the created map is playable (and hence the map tiles have been generated successfully)
        assertTrue(map.isPlayable());
    }

    @Test
    public void createMapOfSize_returnsSameInstance_ifCalledTwice() {
        // Create "BasicMap" objects of size 12 using "MapCreator"
        int size = 12;

        // Try to create two map instances
        Map firstMap = createMap("Basic", size);
        Map secondMap = createMap("Basic", size);

        // Check that the two map instances are actually the same
        assertSame(firstMap, secondMap);
    }

    @Test
    public void createMapOfSize_throwsIllegalArgumentException_ifMapTypeDoesNotExist() {
        // Expect the method to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid map type.");

        // Create a non-existing map type "unknown" using "MapCreator"
        int size = 8;
        createMap("unknown", size);
    }

    /* Tests for the version of the "createMap" function which take an array of tiles as an argument */

    @Test
    public void createMapWithTiles_returnsBasicMapWithCorrectSize_ifGivenMapTypeBasic() {
        // Create a "BasicMap" object using "MapCreator" with an array of tiles as one of the parameters
        Map map = createMap("basic", testTiles);

        // Check that the created map is of type BasicMap
        assertTrue(map instanceof BasicMap);

        // Check that the created map has the same size as the array of tiles passed in the constructor
        assertEquals(testTiles.length, map.getSize());
    }

    @Test
    public void createMapWithTiles_returnsBasicMapWithCorrectSize_ifGivenMapTypeBasicWithDifferentCasing() {
        // Create a "BasicMap" object using "MapCreator" with an array of tiles as one of the parameters
        // The "mapType" argument is now given in a different casing to see if this affects "MapCreator"
        Map map = createMap("BaSiC", testTiles);

        // Check that the created map is of type BasicMap
        assertTrue(map instanceof BasicMap);

        // Check that the created map has the same size as the array of tiles passed in the constructor
        assertEquals(testTiles.length, map.getSize());
    }

    @Test
    public void createMapWithTiles_returnsBasicMapWithSetTiles_ifGivenMapTypeBasic() {
        // Create a "BasicMap" object using "MapCreator" with an array of tiles as one of the parameters
        Map map = createMap("basic", testTiles);

        // Check that the created map is of type BasicMap
        assertTrue(map instanceof BasicMap);

        // Get each tile in the map using getTileType and make sure the type matches that of the pre-generated tiles
        for(int i = 0; i < testTiles.length; i++) {
            for(int j = 0; j < testTiles.length; j++) {
                assertEquals(testTiles[i][j], map.getTileType(i,j));
            }
        }
    }

    @Test
    public void createMapWithTiles_returnsSameInstance_ifCalledTwice() {
        // Try to create two map instances with a pre-generated array of tiles
        Map firstMap = createMap("Basic", testTiles);
        Map secondMap = createMap("Basic", testTiles);

        // Check that the two map instances are actually the same
        assertSame(firstMap, secondMap);
    }

    @Test
    public void createMapWithTiles_throwsIllegalArgumentException_ifMapTypeDoesNotExist() {
        // Expect the method to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid map type.");

        // Create a non-existing map type "unknown" using "MapCreator" with a pre-generated array of tiles
        Map map = createMap("unknown", testTiles);
    }
}
