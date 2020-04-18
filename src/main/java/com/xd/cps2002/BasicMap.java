package com.xd.cps2002;

import java.util.Random;

public class BasicMap extends Map {

    /**
     * Constant value defining the ratio of water tiles to the total number of tiles in the map. For this map type 10%
     * of all of the map tiles are set to be water tiles.
     */
    private final double waterTileRatio = 0.10;

    /**
     * Constructor used to initialize an empty {@code BasicMap} object. It uses the constructor of the {@link Map} super
     * class.
     * @param n size of the {@code n} x {@code n} square map
     * @throws IllegalArgumentException if the method is given an invalid size parameter (outside of the range 5-50)
     */
    public BasicMap(int n) {
        // Initialize Map object using the Map class constructor
        super(n);
    }

    /**
     * Constructor used to initialize a {@code BasicMap} object with a pre-generated map. It also uses the constructor
     * of the {@link Map} super class.
     * @param tiles Pre-generated 2D array of {@code n} x {@code n} tiles to be used in the map.
     * @throws IllegalArgumentException if the method is given an invalid size parameter (outside of the range 5-50)
     */

    public BasicMap(TileType[][] tiles) {
        // Initialize Map object using the Map class constructor
        super(tiles);
    }

    /**
     * Generates the tiles for the {@code BasicMap} randomly. The function generates one treasure tile and a number of
     * water tiles according to {@link BasicMap#waterTileRatio}. It also makes sure that none of the water tiles that
     * are generated are placed adjacent to the treasure tile.
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

        tiles[treasureX][treasureY] = TileType.Treasure;

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
     * Function to check if the player can reach the treasure starting from any grass tile.
     * @return true if the player can reach the treasure from any grass tile and false otherwise.
     */
    @Override
    boolean isPlayable() {
        return true;
    }
}
