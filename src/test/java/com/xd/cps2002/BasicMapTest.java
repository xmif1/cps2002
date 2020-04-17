package com.xd.cps2002;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * This class contains unit tests used to test both the methods of the abstract {@link Map} class and the concrete
 * {@link BasicMap} class.
 */
public class BasicMapTest {

    // BasicMap object used to test different methods
    private BasicMap basicMap;
    private final int defaultSize = 5;
    // Default pre-generated map used in tests requiring a priori knowledge of the tiles
    private TileType[][] defaultTiles;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public  void setup() {
        // Create a map with alternating rows of grass and water tiles
        defaultTiles = new TileType[defaultSize][defaultSize];
        for(int i = 0; i < defaultSize; i++) {
            TileType tile = (i % 2 == 0) ? TileType.Grass : TileType.Water;

            for(int j = 0; j < defaultSize; j++) {
                defaultTiles[i][j] = tile;
            }
        }

        // Add a treasure tile at the very first tile
        defaultTiles[0][0] = TileType.Treasure;

        // Create a new empty BasicMap object with a pre-generated set of tiles
        basicMap = new BasicMap(defaultTiles);
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
    public void BasicMap_storesAnArrayOf2DObjects_IfGivenA2DArrayofTilesWithEqualDimensions() {
        // Try to initialize the map with a pre-generated 2D array of tiles with size 5x5 (both lists have equal length)
        basicMap = new BasicMap(defaultTiles);

        // Check that each tile in the map matches those in the "defaultTiles" array passed to the object
        for(int i = 0; i < defaultSize; i++) {
            for(int j = 0; j < defaultSize; j++) {
                assertEquals(defaultTiles[i][j], basicMap.getTileType(i,j));
            }
        }
    }

    @Test
    public void BasicMap_inferrsMapSizeCorrectly_IfGivenA2DArrayofTilesWithEqualDimensions() {
        // Try to initialize the map with a pre-generated 2D array of tiles with size 5x5 (both lists have equal length)
        basicMap = new BasicMap(defaultTiles);

        // Check that the map size was set correctly
        assertEquals(defaultSize, basicMap.getSize());
    }

    @Test
    public void BasicMap__throwsIllegalArgumentException_IfGivenA2DArrayofTilesWithoutEqualDimensions() {
        // Expect BasicMap to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The lists in the 2D array of tiles must have equal lengths " +
                "(they must form a square).");

        // Try to initialize the map with a 2D array of tiles with size 3x5 (the lists have unequal lengths)
        TileType[][] tiles = new TileType[3][5];
        basicMap = new BasicMap(tiles);
    }

    @Test
    public void BasicMap__throwsIllegalArgumentException_IfGivenA2DArrayofTilesWithoutATreasureTile() {
        // Expect BasicMap to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The 2D array of tiles must include 1 treasure tile.");

        // Create a 2D array of tiles with alternating rows of grass and water tiles without a treasure tile
        TileType[][] tiles = new TileType[defaultSize][defaultSize];
        for(int i = 0; i < defaultSize; i++) {
            TileType tile = (i % 2 == 0) ? TileType.Grass : TileType.Water;

            for(int j = 0; j < defaultSize; j++) {
                defaultTiles[i][j] = tile;
            }
        }

        // Try to initialize the map with the 2D array of tiles
        basicMap = new BasicMap(tiles);
    }

    @Test
    public void BasicMap__throwsIllegalArgumentException_IfGivenA2DArrayofTilesWithTooManyTreasureTiles() {
        // Expect BasicMap to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The 2D array of tiles must include 1 treasure tile.");

        // Create a 2D array of tiles with alternating rows of grass and water tiles
        TileType[][] tiles = new TileType[defaultSize][defaultSize];
        for(int i = 0; i < defaultSize; i++) {
            TileType tile = (i % 2 == 0) ? TileType.Grass : TileType.Water;

            for(int j = 0; j < defaultSize; j++) {
                defaultTiles[i][j] = tile;
            }
        }

        // Add 2 treasure tiles to the map
        tiles[0][0] = TileType.Treasure;
        tiles[0][1] = TileType.Treasure;

        // Try to initialize the map with the 2D array of tiles
        basicMap = new BasicMap(tiles);
    }

    @Test
    public void BasicMap__throwsIllegalArgumentException_IfGivenANull2DArray() {
        // Expect BasicMap to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The lists in the 2D array of tiles cannot be null.");

        // Try to initialize the map with a null 2D array
        TileType[][] tiles = null;
        basicMap = new BasicMap(tiles);
    }

    @Test
    public void BasicMap__throwsIllegalArgumentException_IfGivenAnEmpty2DArray() {
        // Expect BasicMap to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The lists in the 2D array of tiles cannot be empty.");

        // Try to initialize the map with an empty 2D array (has 0 elements in the first list)
        TileType[][] tiles = new TileType[0][0];
        basicMap = new BasicMap(tiles);
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

    /**
     * The {@link Map#getTileType(int, int)} method is only tested for error cases since it is already used in the test
     * for the method {@code BasicMap_storesAnArrayOf2DObjects_IfGivenA2DArrayofTilesWithEqualDimensions}
     */
    @Test
    public void getTileType_throwsNullPointerException_IfMapHasNotYetBeenGenerated() {
        // Expect getTileType to throw an IllegalArgumentException
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Map tiles have not been generated yet");

        // Create a new empty map with no tiles
        basicMap = new BasicMap(defaultSize);

        // Try to get a tile from a position in the map
        int x = 1, y = 3;
        basicMap.getTileType(x,y);
    }

    @Test
    public void getTileType_throwsIllegalArgumentException_IfGivenPositionIsInvalid() {
        // Expect getTileType to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Given tile position is not valid.");

        // Try to get a tile from a position which does not exist in the map
        int x = -2, y = 5;
        basicMap.getTileType(x,y);
    }

    @Test
    public void isValidPosition_returnsTrue_ifPositionIsValid() {
        // Try to check if a valid position (x/y coordinates in range 0-4) exists within the 5x5 map
        int x = 3, y = 2;

        // Expect the function to return true
        assertTrue(basicMap.isValidPosition(x,y));
    }

    @Test
    public void isValidPosition_returnsFalse_ifXPositionIsNegative() {
        // Try to check if a position with a negative x coordinate exists within the 5x5 map
        int x = -3, y = 1;

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(x,y));
    }

    @Test
    public void isValidPosition_returnsFalse_ifYPositionIsNegative() {
        // Try to check if a position with a negative y coordinate exists within the 5x5 map
        int x = 2, y = -1;

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(x,y));
    }

    @Test
    public void isValidPosition_returnsFalse_ifXPositionIsTooBig() {
        // Try to check if a position with an x coordinate >=5 exists within the 5x5 map
        int x = 5, y = 1;

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(x,y));
    }

    @Test
    public void isValidPosition_returnsFalse_ifYPositionIsTooBig() {
        // Try to check if a position with an y coordinate >=5 exists within the 5x5 map
        int x = 2, y = 5;

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(x,y));
    }
}
