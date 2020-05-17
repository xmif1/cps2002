package com.xd.cps2002.map;

import com.xd.cps2002.player.Position;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

/**
 * The {@code BasicMap} class is a concrete implementation of the {@link} Map class, and it contains the implementations
 * of the {@link Map#generate()} and {@link Map#isPlayable()} functions particular to this type of map.
 */
public class BasicMap extends Map {

    /**
     * Constant value defining the ratio of water tiles to the total number of tiles in the map. For this map type 10%
     * of all of the map tiles are set to be water tiles.
     */
    private final double waterTileRatio = 0.10;

    /**
     * Defines the minimum percentage of tiles in the map from which a player needs to be able to reach the treasure.
     * For this type of map at least 75% of tiles should be reachable
     */
    private final double minimumWinnableTiles = 0.75;

    /**
     * Constructor used to initialize an empty {@code BasicMap} object. It uses the constructor of the {@link Map} super
     * class.
     * @param n size of the {@code n} x {@code n} square map
     * @throws IllegalArgumentException if the method is given an invalid size parameter (outside of the range 5-50)
     *
     * @implNote This constructor has a {@code protected} access modifier to stop client code from instantiating the
     * class directly. However, unit tests can still call the constructor normally.
     */
    protected BasicMap(int n) {
        // Initialize Map object using the Map class constructor
        super(n);
    }

    /**
     * Constructor used to initialize a {@code BasicMap} object with a pre-generated map. It also uses the constructor
     * of the {@link Map} super class.
     * @param tiles Pre-generated 2D array of {@code n} x {@code n} tiles to be used in the map.
     * @throws IllegalArgumentException if {@code tiles} is null, empty or if the lists in the 2D array do not have the
     * same lengths.
     */

    protected BasicMap(TileType[][] tiles) {
        // Initialize Map object using the Map class constructor
        super(tiles);
    }

    /**
     * Generates the tiles for the {@code BasicMap} object randomly.
     *
     * @apiNote Note that this function should also set the {@link Map#treasurePos} member so that it can be used later
     * in the function {@link Map#isPlayable()}.
     *
     * @implNote The function generates one treasure tile and a number of water tiles according to
     * {@link BasicMap#waterTileRatio} and the total number of tiles in the map. It also makes sure that none of the
     * water tiles that are generated are placed adjacent to the treasure tile.
     */
    @Override
    public void generate() {
        // Create a new 2D array of tiles
        tiles = new TileType[size][size];

        // Randomly choose the position of the treasure tile
        int treasureX,treasureY;
        Random r = new Random();
        treasureX = r.nextInt(size);
        treasureY = r.nextInt(size);

        // Set the tile at the chosen position to be a treasure tile
        tiles[treasureX][treasureY] = TileType.Treasure;

        // Store the position of the treasure tile in the "treasurePos" member
        treasurePos = new Position(treasureX, treasureY);

        // Calculate the number of water tiles that need to be generated
        int waterTilesQuota = (int) Math.floor(size * size * waterTileRatio);

        // Try to place all of the water tiles in randomly generated positions
        while(waterTilesQuota > 0) {
            // Randomly choose a position on the map
            int waterX = r.nextInt(size);
            int waterY = r.nextInt(size);

            // If the position is currently empty and is not next to a treasure tile, place the water tile
            if(tiles[waterX][waterY] == null && !isNextToTreasureTile(waterX, waterY)) {
                tiles[waterX][waterY] = TileType.Water;
                waterTilesQuota--;
            }
        }

        // Fill in the rest of the tiles with grass tiles
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                // If the tile is still null, set it to a grass tile
                if(tiles[i][j] == null) tiles[i][j] = TileType.Grass;
            }
        }
    }

    /**
     * Helper function used to check if a tile at a specific coordinate is adjacent to a {@link TileType#Treasure}
     * tile.
     * @param x x-coordinate of the tile to check
     * @param y y-coordinate of the tile to check
     * @return returns true if the tile is next to a {@link TileType#Treasure} tile and false otherwise.
     */
    private boolean isNextToTreasureTile(int x, int y) {
        // Check if the chosen position has a treasure tile near it
        boolean isNextToTreasure = false;

        adjacentTilesSearch:
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                // Find a new position adjacent to the current one
                int newX = x + i;
                int newY = y + j;

                // Check that the new generated position is different from the treasure tile position and check that
                // the new position is valid on the map
                if((newX != x || newY != y) && isValidPosition(newX,newY)) {
                    // If the adjacent tile is a treasure tile, return true and stop searching
                    if(tiles[newX][newY] == TileType.Treasure) {
                        isNextToTreasure = true;
                        break adjacentTilesSearch;
                    }
                }
            }
        }
        return isNextToTreasure;
    }

    /**
     * Function to check if the player can reach the treasure starting from at least 75% of the grass tiles. The
     * function also computes from which tiles the player can reach the treasure.
     * @return true if the player can reach the treasure from at least 75% of the grass tiles and false otherwise.
     * @implNote The function carries out a Depth First Search (DFS) traversal of the map starting from the treasure
     * tile to check which grass tiles are actually connected to the treasure tile. The function also creates an array
     * of booleans ({@link Map#winnableTiles}) representing the tiles which are connected to the treasure tile. The
     * elements of this array can be accessed using the {@link Map#isPositionWinnable(Position)} function.
     */
    @Override
    public boolean isPlayable() {
        // If "treasurePos" has not been set, throw an exception
        if(treasurePos == null) {
            throw new NullPointerException("Treasure position has not been set yet.");
        }

        // Initialize "winnableTiles" with all tiles set to false
        winnableTiles = new boolean[size][size];
        for(boolean[] row : winnableTiles) {
            Arrays.fill(row, false);
        }

        // Store a stack of tile positions which still need to be checked
        Stack<Position> uncheckedPositions = new Stack<>();
        // Start checking from the treasure tile
        uncheckedPositions.push(treasurePos);

        /* Perform a DFS traversal of the 2D tile array to check which grass tiles can reach the treasure */
        // Keep a count of grass tiles which have been reached
        int reachableCount = 0;

        // Keep traversing until no more possible positions remain
        while(!uncheckedPositions.empty()) {
            // Get the top position on the stack and get the type of the tile
            Position currentPos = uncheckedPositions.pop();

            // Check all of the neighbouring tiles
            for(int xOffset = -1; xOffset <= 1; xOffset++) {
                for(int yOffset = -1; yOffset <= 1; yOffset++) {
                    // Check the the move is either up, down, left, or right (i.e. at least one co-ordinate must remain
                    // the same)
                    if(xOffset == 0 ^ yOffset == 0) {
                        Position adjacentPos = new Position(currentPos.x + xOffset, currentPos.y + yOffset);
                        // Check if the adjacent position is valid, has not been visited yet and is not a water tile
                        if(isValidPosition(adjacentPos)
                            && !winnableTiles[adjacentPos.x][adjacentPos.y]
                            && getTileType(adjacentPos) != TileType.Water) {
                            // If so, add the position to the stack, so that its neighbouring tiles can also be
                            // traversed.
                            uncheckedPositions.push(adjacentPos);

                            // Mark the adjacent tile as winnable and update the count of tiles reached
                            winnableTiles[adjacentPos.x][adjacentPos.y] = true;
                            reachableCount++;
                        }
                    }
                }
            }
        }
        // Set the treasure tile to unreachable (since the player should not be able to start playing on this tile)
        winnableTiles[treasurePos.x][treasurePos.y] = false;

        // Calculate the minimum number of reachable tiles needed
        int minReachableTiles = (int) Math.floor(size * size * minimumWinnableTiles);

        // Check that the minimum number of reachable tiles is met.
        return reachableCount >= minReachableTiles;
    }
}
