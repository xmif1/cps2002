package com.xd.cps2002.map;

import com.xd.cps2002.player.Position;
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

    /**
     * BasicMap object used to test different methods
     */
    private BasicMap basicMap;
    /**
     * Map size used the {@link BasicMapTest#defaultTiles} set of tiles.
     */
    private final int defaultSize = 5;
    /**
     * Treasure position used the {@link BasicMapTest#defaultTiles} set of tiles.
     */
    private Position defaultTreasurePos;
    /**
     * Default pre-generated set of tiles used in tests requiring a priori knowledge of the tiles
     */
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
        defaultTreasurePos = new Position(0,0);
        defaultTiles[defaultTreasurePos.x][defaultTreasurePos.y] = TileType.Treasure;

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
    public void BasicMap_setsTreasurePosMember_IfGivenA2DArrayofTilesWithEqualDimensions() {
        // Try to initialize the map with a pre-generated 2D array of tiles with size 5x5 (both lists have equal length)
        basicMap = new BasicMap(defaultTiles);

        //Check that the "treasurePos" member is set to the expected position
        assertEquals(defaultTreasurePos.x, basicMap.treasurePos.x);
        assertEquals(defaultTreasurePos.y, basicMap.treasurePos.y);
    }

    @Test
    public void BasicMap_throwsIllegalArgumentException_IfGivenA2DArrayofTilesWithoutEqualDimensions() {
        // Expect BasicMap to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The lists in the 2D array of tiles must have equal lengths " +
                "(they must form a square).");

        // Try to initialize the map with a 2D array of tiles with size 3x5 (the lists have unequal lengths)
        TileType[][] tiles = new TileType[3][5];
        basicMap = new BasicMap(tiles);
    }

    @Test
    public void BasicMap_throwsIllegalArgumentException_IfGivenA2DArrayofTilesWithoutATreasureTile() {
        // Expect BasicMap to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The 2D array of tiles must include 1 treasure tile.");

        // Create a 2D array of tiles with alternating rows of grass and water tiles without a treasure tile
        TileType[][] tiles = new TileType[defaultSize][defaultSize];
        for(int i = 0; i < defaultSize; i++) {
            TileType tile = (i % 2 == 0) ? TileType.Grass : TileType.Water;

            for(int j = 0; j < defaultSize; j++) {
                tiles[i][j] = tile;
            }
        }

        // Try to initialize the map with the 2D array of tiles
        basicMap = new BasicMap(tiles);
    }

    @Test
    public void BasicMap_throwsIllegalArgumentException_IfGivenA2DArrayofTilesWithTooManyTreasureTiles() {
        // Expect BasicMap to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The 2D array of tiles must include 1 treasure tile.");

        // Create a 2D array of tiles with alternating rows of grass and water tiles
        TileType[][] tiles = new TileType[defaultSize][defaultSize];
        for(int i = 0; i < defaultSize; i++) {
            TileType tile = (i % 2 == 0) ? TileType.Grass : TileType.Water;

            for(int j = 0; j < defaultSize; j++) {
                tiles[i][j] = tile;
            }
        }

        // Add 2 treasure tiles to the map
        tiles[0][0] = TileType.Treasure;
        tiles[0][3] = TileType.Treasure;

        // Try to initialize the map with the 2D array of tiles
        basicMap = new BasicMap(tiles);
    }

    @Test
    public void BasicMap_throwsIllegalArgumentException_IfGivenA2DArrayofTilesWithNullTiles() {
        // Expect BasicMap to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The 2D array of tiles cannot have tiles which are null.");

        // Create a 2D array with only 1 treasure tiles and all other elements set to null
        TileType[][] tiles = new TileType[defaultSize][defaultSize];
        tiles[0][0] = TileType.Treasure;

        // Try to initialize the map with the 2D array of tiles
        basicMap = new BasicMap(tiles);
    }

    @Test
    public void BasicMap_throwsIllegalArgumentException_IfGivenANull2DArray() {
        // Expect BasicMap to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The lists in the 2D array of tiles cannot be null.");

        // Try to initialize the map with a null 2D array
        TileType[][] tiles = null;
        basicMap = new BasicMap(tiles);
    }

    @Test
    public void BasicMap_throwsIllegalArgumentException_IfGivenAnEmpty2DArray() {
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

    /*
     * The tests below test the version of getTileType which takes individual x and y coordinates.
     */

    /**
     * Note that this test is very similar to the unit test
     * {@link BasicMapTest#BasicMap_storesAnArrayOf2DObjects_IfGivenA2DArrayofTilesWithEqualDimensions()}. However the
     * test was added for the sake of completeness.
     */
    @Test
    public void getTileTypeXY_returnsCorrectTileType_ifTileExists() {
        // Try to initialize the map with a pre-generated 2D array of tiles with size 5x5
        basicMap = new BasicMap(defaultTiles);

        // Get each tile in the map using getTileType and make sure the type matches that of the pre-generated tiles
        for(int i = 0; i < defaultSize; i++) {
            for(int j = 0; j < defaultSize; j++) {
                assertEquals(defaultTiles[i][j], basicMap.getTileType(i,j));
            }
        }
    }

    @Test
    public void getTileTypeXY_throwsNullPointerException_IfMapHasNotYetBeenGenerated() {
        // Expect getTileType to throw a NullPointerException
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Map tiles have not been generated yet");

        // Create a new empty map with no tiles
        basicMap = new BasicMap(defaultSize);

        // Try to get a tile from a position in the map
        int x = 1, y = 3;
        basicMap.getTileType(x,y);
    }

    @Test
    public void getTileTypeXY_throwsIllegalArgumentException_IfGivenPositionIsInvalid() {
        // Expect getTileType to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Given tile position is not valid.");

        // Try to get a tile from a position which does not exist in the map
        int x = -2, y = 5;
        basicMap.getTileType(x,y);
    }

    /*
     * The tests below test the version of isValidPosition which takes a Position object.
     */

    @Test
    public void getTileTypePos_returnsCorrectTileType_ifTileExists() {
        // Try to initialize the map with a pre-generated 2D array of tiles with size 5x5
        basicMap = new BasicMap(defaultTiles);

        // Get each tile in the map using getTileType and make sure the type matches that of the pre-generated tiles
        for(int i = 0; i < defaultSize; i++) {
            for(int j = 0; j < defaultSize; j++) {
                Position pos = new Position(i,j);
                assertEquals(defaultTiles[i][j], basicMap.getTileType(pos));
            }
        }
    }

    @Test
    public void getTileTypePos_throwsNullPointerException_IfPositionIsNull() {
        // Expect getTileType to throw a NullPointerException
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Given tile position cannot be null.");

        // Try to get a tile from a null position in the map
        Position pos = null;
        basicMap.getTileType(pos);
    }

    @Test
    public void getTileTypePos_throwsNullPointerException_IfMapHasNotYetBeenGenerated() {
        // Expect getTileType to throw a NullPointerException
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Map tiles have not been generated yet.");

        // Create a new empty map with no tiles
        basicMap = new BasicMap(defaultSize);

        // Try to get a tile from a position in the map
        Position pos = new Position(1,3);
        basicMap.getTileType(pos);
    }

    @Test
    public void getTileTypePos_throwsIllegalArgumentException_IfGivenPositionIsInvalid() {
        // Expect getTileType to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Given tile position is not valid.");

        // Try to get a tile from a position which does not exist in the map
        Position pos = new Position(-2,5);
        basicMap.getTileType(pos);
    }

    /*
     * The tests below test the version of isValidPosition which takes individual x and y coordinates.
     */

    @Test
    public void isValidPositionXY_returnsTrue_ifPositionIsValid() {
        // Try to check if a valid position (x/y coordinates in range 0-4) exists within the 5x5 map
        int x = 3, y = 2;

        // Expect the function to return true
        assertTrue(basicMap.isValidPosition(x,y));
    }

    @Test
    public void isValidPositionXY_returnsFalse_ifXPositionIsNegative() {
        // Try to check if a position with a negative x coordinate exists within the 5x5 map
        int x = -3, y = 1;

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(x,y));
    }

    @Test
    public void isValidPositionXY_returnsFalse_ifYPositionIsNegative() {
        // Try to check if a position with a negative y coordinate exists within the 5x5 map
        int x = 2, y = -1;

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(x,y));
    }

    @Test
    public void isValidPositionXY_returnsFalse_ifXPositionIsTooBig() {
        // Try to check if a position with an x coordinate >=5 exists within the 5x5 map (which is indexed from 0 to 4)
        int x = 5, y = 1;

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(x,y));
    }

    @Test
    public void isValidPositionXY_returnsFalse_ifYPositionIsTooBig() {
        // Try to check if a position with an y coordinate >=5 exists within the 5x5 map (which is indexed from 0 to 4)
        int x = 2, y = 5;

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(x,y));
    }

    /*
     * The tests below test the version of isValidPosition which takes a Position object.
     */

    @Test
    public void isValidPositionPos_returnsTrue_ifPositionIsValid() {
        // Try to check if a valid position (x/y coordinates in range 0-4) exists within the 5x5 map
        Position pos = new Position(3,2);

        // Expect the function to return true
        assertTrue(basicMap.isValidPosition(pos));
    }

    @Test
    public void isValidPositionPos_returnsFalse_ifPositionObjectIsNull() {
        // Try to check if a null position exists within the 5x5 map
        Position pos = null;

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(pos));
    }

    @Test
    public void isValidPositionPos_returnsFalse_ifXPositionIsNegative() {
        // Try to check if a position with a negative x coordinate exists within the 5x5 map
        Position pos = new Position(-3, 1);

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(pos));
    }

    @Test
    public void isValidPositionPos_returnsFalse_ifYPositionIsNegative() {
        // Try to check if a position with a negative y coordinate exists within the 5x5 map
        Position pos = new Position(2, -1);

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(pos));
    }

    @Test
    public void isValidPositionPos_returnsFalse_ifXPositionIsTooBig() {
        // Try to check if a position with an x coordinate >=5 exists within the 5x5 map (which is indexed from 0 to 4)
        Position pos = new Position(5, 1);

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(pos));
    }

    @Test
    public void isValidPositionPos_returnsFalse_ifYPositionIsTooBig() {
        // Try to check if a position with a y coordinate >=5 exists within the 5x5 map (which is indexed from 0 to 4)
        Position pos = new Position(2, 5);

        // Expect the function to return false
        assertFalse(basicMap.isValidPosition(pos));
    }

    @Test
    public void generate_generatesOneTreasureTile_whenCalled() {
        // Create a new empty 8 x 8 tile map and randomly generate the tiles
        int size = 8;
        basicMap = new BasicMap(size);
        basicMap.generate();

        // Count the number of treasure tiles generated
        int treasureCount = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(basicMap.getTileType(i,j) == TileType.Treasure) treasureCount++;
            }
        }

        // Check that only one treasure tile has been found in the map
        assertEquals(1, treasureCount);
    }

    @Test
    public void generate_generatesCorrectNumberOfWaterTiles_whenCalledUsingDefaultWaterToTilePercentages() {
        // Create a new empty 12 x 12 tile map and randomly generate the tiles
        int size = 12;
        basicMap = new BasicMap(size);
        basicMap.generate();

        // Count the number of water tiles generated
        int waterCount = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(basicMap.getTileType(i,j) == TileType.Water) waterCount++;
            }
        }

        // Find the actual ratio of water tiles to map tiles that were generated
        double actualRatio = waterCount/((double) size * size);

        // Check that the actual ratio is in the default range (between 1% and 10 percent)
        assertTrue(0.01 <= actualRatio && actualRatio <= 0.10);
    }

    @Test
    public void generate_DoesNotGenerateWaterTilesNextToTheTreasureTile_whenCalled() {
        // Create a new empty 5 x 5 tile map and randomly generate the tiles
        int size = 5;
        basicMap = new BasicMap(size);
        basicMap.generate();

        // Find the position of the treasure tile generated in the map
        int x = -2,y = -2;
        treasureSearch:
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(basicMap.getTileType(i,j) == TileType.Treasure) {
                    x=i;
                    y=j;
                    // Stop searching for the treasure tile
                    break treasureSearch;
                }
            }
        }

        // Check that all tiles immediately surrounding the treasure tile not water tiles
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                // Find the new adjacent position
                int newX = x + i;
                int newY = y + j;

                // Check that the new generated position is different from the treasure tile position and check that the
                // new position is valid on the map
                if((newX != x || newY != y) && basicMap.isValidPosition(newX,newY)) {
                    // Check that tile is not a water tile
                    assertNotEquals(TileType.Water, basicMap.getTileType(newX, newY));
                }
            }
        }
    }

    @Test
    public void generate_generatesAllTilesInMap_whenCalled() {
        // Create a new empty 7 x 7 tile map and randomly generate the tiles
        int size = 7;
        basicMap = new BasicMap(size);
        basicMap.generate();

        // Check that none of the tiles in the map are set to null
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                assertNotNull(basicMap.getTileType(i, j));
            }
        }
    }

    @Test
    public void generate_setsTreasurePosMember_whenCalled() {
        // Create a new empty 7 x 7 tile map and randomly generate the tiles
        int size = 7;
        basicMap = new BasicMap(size);
        basicMap.generate();

        // Check that the "treasurePos" member is set
        assertNotNull(basicMap.treasurePos);
        // Check that the position at "treasurePos" is a treasure tile
        assertEquals(TileType.Treasure, basicMap.getTileType(basicMap.treasurePos));
    }

    /**
     * This unit checks that the function {@link BasicMap#generate()} correctly adjusts the number of water tiles that
     * it generates when the function {@link BasicMap#setWaterTilePercentage(int, int)} is used to change the minimum
     * and maximum percentages of water tiles.
     */
    @Test
    public void generate_generatesCorrectNumberOfWaterTiles_whenWaterToTilePercentagesHaveBeenChanged() {
        // Create a new empty 12 x 12 tile map
        int size = 12;
        basicMap = new BasicMap(size);

        // Change the minimum and maximum percentages of water tiles to map tiles in the map to 30-60%
        basicMap.setWaterTilePercentage(30, 60);

        // Randomly generate the tiles in the map
        basicMap.generate();

        // Count the number of water tiles generated
        int waterCount = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(basicMap.getTileType(i,j) == TileType.Water) waterCount++;
            }
        }

        // Find the actual ratio of water tiles to map tiles that were generated
        double actualRatio = waterCount/((double) size * size);

        // Check that the actual ratio is in the range between 30% to 60%
        assertTrue(0.3 <= actualRatio && actualRatio <= 0.6);
    }

    /**
     * This unit test checks the edge case when the function {@link BasicMap#generate()} is fixed at a specific ratio of
     * water tiles by setting both of the parameters in {@link BasicMap#setWaterTilePercentage(int, int)} to the same
     * percentage.
     */
    @Test
    public void generate_generatesCorrectNumberOfWaterTiles_whenWaterToTilePercentagesHaveBeenChangedToBeEqual() {
        // Create a new empty 12 x 12 tile map
        int size = 12;
        basicMap = new BasicMap(size);

        // Change the minimum and maximum percentages of water tiles to map tiles in the map to be both 53%
        // A prime number was chosen to see how the function handles a number which is not an exact divisor of the
        // number of tiles in the map
        basicMap.setWaterTilePercentage(53, 53);

        // Randomly generate the tiles in the map
        basicMap.generate();

        // Count the number of water tiles generated
        int waterCount = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(basicMap.getTileType(i,j) == TileType.Water) waterCount++;
            }
        }

        // Find the actual ratio of water tiles to map tiles that were generated
        double actualRatio = waterCount/((double) size * size);

        // Check that the actual ratio is within 1% of the percentage it was set to (due to rounding)
        assertEquals(0.53, actualRatio, 0.01);
    }

    /**
     * This unit test checks the edge case when the function {@link BasicMap#generate()} is set to not generate any
     * water tiles by setting both of the parameters in {@link BasicMap#setWaterTilePercentage(int, int)} to 0%.
     */
    @Test
    public void generate_generatesNoWaterTiles_whenWaterToTilePercentagesAreBothZero() {
        // Create a new empty 12 x 12 tile map
        int size = 12;
        basicMap = new BasicMap(size);

        // Change the minimum and maximum percentages of water tiles to map tiles in the map to be both 0%
        basicMap.setWaterTilePercentage(0, 0);

        // Randomly generate the tiles in the map
        basicMap.generate();

        // Count the number of water tiles generated
        int waterCount = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(basicMap.getTileType(i,j) == TileType.Water) waterCount++;
            }
        }

        // Expect the map to have no water tiles
        assertEquals(0, waterCount);
    }

    /**
     * This unit test is meant to show the generate still works in the absolute worst case: when you have a 5 x 5 sized
     * map and want to have 64% of the water tiles in the map be water tiles. Even with the
     * restriction that water tiles cannot be placed next to the treasure tile, this case should always work. However,
     * if the user could set the percentage to even 1% higher, the function might deadlock since it may not have enough
     * space to place the remaining tile (assuming that 9 tiles are taken up by the treasure tile and its adjacent
     * tiles).
     */
    @Test
    public void generate_generatesCorrectNumberOfWaterTiles_whenPercentagesHaveBeenSetToMaximumInASmallMap() {
        // Create a new empty 5 x 5 tile map (smallest available map)
        int size = 5;
        basicMap = new BasicMap(size);

        // Change the minimum and maximum percentages of water tiles to map tiles in the map to be both 53%
        // A prime number was chosen to see how the function handles a number which is not an exact divisor of the
        // number of tiles in the map
        basicMap.setWaterTilePercentage(64, 64);

        // Randomly generate the tiles in the map
        basicMap.generate();

        // Count the number of water tiles generated
        int waterCount = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(basicMap.getTileType(i,j) == TileType.Water) waterCount++;
            }
        }

        // Find the actual ratio of water tiles to map tiles that were generated
        double actualRatio = waterCount/((double) size * size);

        // Check that the actual ratio is within 1% of the percentage it was set to (due to rounding)
        assertEquals(0.64, actualRatio, 0.01);
    }

    @Test
    public void setWaterTilePercentage_throwsIllegalArgumentException_ifMinimumPercentageIsNegative() {
        // Expect the unit test to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The minimum and maximum percentage of water tiles must both be in " +
                "the range from 0 to 64 (inclusive).");

        // Create a new empty 12 x 12 tile map
        int size = 12;
        basicMap = new BasicMap(size);

        // Try to change the minimum and maximum percentages of water tiles such that the minimum is negative
        basicMap.setWaterTilePercentage(-30, 50);
    }

    @Test
    public void setWaterTilePercentage_throwsIllegalArgumentException_ifMinimumPercentageIsLargerThan64() {
        // Expect the unit test to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The minimum and maximum percentage of water tiles must both be in " +
                "the range from 0 to 64 (inclusive).");

        // Create a new empty 12 x 12 tile map
        int size = 12;
        basicMap = new BasicMap(size);

        // Try to change the minimum and maximum percentages of water tiles such that the minimum is larger than 64%
        basicMap.setWaterTilePercentage(101, 50);
    }

    @Test
    public void setWaterTilePercentage_throwsIllegalArgumentException_ifMaximumPercentageIsNegative() {
        // Expect the unit test to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The minimum and maximum percentage of water tiles must both be in " +
                "the range from 0 to 64 (inclusive).");

        // Create a new empty 12 x 12 tile map
        int size = 12;
        basicMap = new BasicMap(size);

        // Try to change the minimum and maximum percentages of water tiles such that the maximum is negative
        basicMap.setWaterTilePercentage(10, -90);
    }

    @Test
    public void setWaterTilePercentage_throwsIllegalArgumentException_ifMaximumPercentageIsLargerThan64() {
        // Expect the unit test to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The minimum and maximum percentage of water tiles must both be in " +
                "the range from 0 to 64 (inclusive).");

        // Create a new empty 12 x 12 tile map
        int size = 12;
        basicMap = new BasicMap(size);

        // Try to change the minimum and maximum percentages of water tiles such that the maximum is larger than 64%
        basicMap.setWaterTilePercentage(10, 65);
    }

    @Test
    public void setWaterTilePercentage_throwsIllegalArgumentException_ifMinimumPercentageIsLargerThanMaximumPercentage() {
        // Expect the unit test to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The minimum percentage of water tiles cannot be larger than the " +
                "maximum percentage.");

        // Create a new empty 12 x 12 tile map
        int size = 12;
        basicMap = new BasicMap(size);

        // Try to change the minimum and maximum percentages of water tiles such that the minimum is larger than the
        // maximum
        basicMap.setWaterTilePercentage(20, 19);
    }

    @Test
    public void isPlayable_returnsTrue_IfTheTreasureCanBeReachedFrom75PercentOfTiles() {
        // Create a new 10x10 map with a strip of water tiles dividing it
        // Note, that the water tiles dividing the map take up 10% of the map. Thus, the map has two grass sections:
        // one which makes up 80% of the map, and one which makes up 10% of the map.
        int size = 10;
        TileType[][] tiles = new TileType[size][size];
        for(int i = 0; i < size; i++) {
            // Create a strip of water tiles in row 8 (the penultimate row)
            TileType tile = (i == 8) ? TileType.Water : TileType.Grass;
            for(int j = 0; j < size; j++) {
                tiles[i][j] = tile;
            }
        }

        // Put the treasure tile in the larger (80%) section
        tiles[0][0] = TileType.Treasure;

        // Create a new map with the tiles created above
        basicMap = new BasicMap(tiles);

        // Check that the function returns true (given that 75% of the tiles should be playable by default)
        assertTrue(basicMap.isPlayable());
    }

    @Test public void isPlayable_returnsFalse_IfTheTreasureCannotBeReachedFrom75PercentOfTiles() {
        // Create a new 10x10 map with a strip of water tiles dividing it
        // Note, that the water tiles dividing the map take up 10% of the map. Thus, the map has two grass sections:
        // one which makes up 80% of the map, and one which makes up 10% of the map.
        int size = 10;
        TileType[][] tiles = new TileType[size][size];
        for(int i = 0; i < size; i++) {
            // Create a strip of water tiles in row 8 (the penultimate row)
            TileType tile = (i == 8) ? TileType.Water : TileType.Grass;
            for(int j = 0; j < size; j++) {
                tiles[i][j] = tile;
            }
        }

        // Put the treasure tile in the smaller (10%) section
        tiles[9][9] = TileType.Treasure;

        // Create a new map with the tiles created above
        basicMap = new BasicMap(tiles);

        // Check that the function returns false (given that 75% of the tiles should be playable by default)
        assertFalse(basicMap.isPlayable());
    }

    /**
     * This unit test is meant to check that changing the minimum percentage of playable tiles using
     * {@link BasicMap#setWaterTilePercentage(int, int)} has an effect on the result of the function
     * {@link BasicMap#isPlayable()}. The test case is almost exactly the same as
     * {@link BasicMapTest#isPlayable_returnsFalse_IfTheTreasureCannotBeReachedFrom75PercentOfTiles()},
     *  except for the fact that {@link BasicMap#setWaterTilePercentage(int, int)} is called. Thus, any
     *  any changes in the outcome of calling {@link BasicMap#isPlayable()} should be only due to
     *  calling this function.
     */
    @Test
    public void isPlayable_correctlyAdjustsItsResult_WhenTheMinimumPercentageOfPlayableTilesIsChanged() {
        // Create a new 10x10 map with a strip of water tiles dividing it
        // Note, that the water tiles dividing the map take up 10% of the map. Thus, the map has two grass sections:
        // one which makes up 80% of the map, and one which makes up 10% of the map.
        int size = 10;
        TileType[][] tiles = new TileType[size][size];
        for(int i = 0; i < size; i++) {
            // Create a strip of water tiles in row 8 (the penultimate row)
            TileType tile = (i == 8) ? TileType.Water : TileType.Grass;
            for(int j = 0; j < size; j++) {
                tiles[i][j] = tile;
            }
        }

        // Put the treasure tile in the smaller (10%) section
        tiles[9][9] = TileType.Treasure;

        // Create a new map with the tiles created above
        basicMap = new BasicMap(tiles);

        // Set the minimum number of playable tiles to 10% (just enough for the map to be considered playable)
        basicMap.setMinPlayableTilesPercentage(10);

        // Check that the function returns true (instead of of false like when the default percentage is used)
        assertTrue(basicMap.isPlayable());
    }

    @Test
    public void isPlayable_throwsANullPointerException_ifTreasurePosMemberHasNotBeenSet() {
        // Expect the function to throw the NullPointerException
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Treasure position has not been set yet.");

        // Create a new empty 8 x 8 tile map
        int size = 8;
        basicMap = new BasicMap(size);

        // Try to check if the map is playable without generating the tiles first
        basicMap.isPlayable();
    }

    @Test public void isPositionWinnable_correctlyReturnsIfATileIsWinnable_ifGivenAValidPosition() {
        // Create a new 10x10 map with a strip of water tiles dividing it (80% of the map is playable)
        int size = 10;
        TileType[][] tiles = new TileType[size][size];
        for(int i = 0; i < size; i++) {
            // Create a strip of water tiles in row 8
            TileType tile = (i == 8) ? TileType.Water : TileType.Grass;
            for(int j = 0; j < size; j++) {
                tiles[i][j] = tile;
            }
        }

        // Put the treasure tile in the larger (80%) section
        tiles[0][0] = TileType.Treasure;

        // Create a new map with the tiles created above
        basicMap = new BasicMap(tiles);

        // Perform a DFA traversal to find winnable tiles
        basicMap.isPlayable();

        // Check that the treasure tile is not winnable
        assertFalse(basicMap.isPositionWinnable(new Position(0,0)));

        // Check that all tiles in the first 8 rows (except the treasure tile at (0,0)) are be winnable
        for(int i = 1; i < size; i++) {
            boolean expected = i < 8;
            for(int j = 0; j < size; j++) {
                Position position = new Position(i,j);
                assertEquals(expected, basicMap.isPositionWinnable(position));
            }
        }
    }

    @Test public void isPositionWinnable_ThrowsNullPointerException_ifGivenANullPosition() {
        // Expect getTileType to throw a NullPointerException
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Given tile position cannot be null.");

        // Perform a DFA traversal to find winnable tiles
        basicMap.isPlayable();

        // Try to check if a player can win from a null position
        Position pos = null;
        basicMap.isPositionWinnable(pos);
    }

    @Test public void isPositionWinnable_ThrowsNullPointerException_ifIsPlayableHasNotBeenRunYet() {
        // Expect getTileType to throw a NullPointerException
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("The isPlayable function must be run before isPositionWinnable.");

        // Try to check if a player can win from a position in the map without running isPlayable first
        Position pos = new Position(3,4);
        basicMap.isPositionWinnable(pos);
    }

    @Test public void isPositionWinnable_ThrowsInvalidArgumentException_ifGivenAnInvalidPosition() {
        // Expect getTileType to throw an IllegalArgumentException
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Given tile position is not valid.");

        // Perform a DFA traversal to find winnable tiles
        basicMap.isPlayable();

        // Try to check if a player can win from a position outside of the map bounds
        Position pos = new Position(-6,7);
        basicMap.isPositionWinnable(pos);
    }
}
