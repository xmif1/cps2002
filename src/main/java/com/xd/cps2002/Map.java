package com.xd.cps2002;

public abstract class Map {
    /**
     * The {@code size} member stores the dimension of the square map (size x size tiles).
     */
    private int size;
    /**
     * The {@code tiles} member stores the actual types for each tile on the map that the player can walk on.
     */
    private TileType[][] tiles;

    /**
     * Constructor used to initialize an empty {@code Map} object.
     * @param n size of the {@code n} x {@code n} square map
     * @throws IllegalArgumentException if the method is given an invalid size parameter (outside of the range 5-50)
     */
    public Map(int n)
    {
        // If map size is invalid (not between 5-50) throw an exception
        if(n < 5 || n > 50) {
            throw new IllegalArgumentException("BasicMap was initialized with an invalid size argument.");
        }

        // Otherwise set the map size as normal
        this.size=n;

        // Initialize the "tiles" array to null
        tiles = null;
    }

    /**
     * Constructor used to initialize a {@code Map} object with a pre-generated set of tiles.
     * @param tiles a 2D array of {@link TileType} elements which represents the placement of the tiles in the map
     * @throws IllegalArgumentException if {@code tiles} is null, empty or if the lists in the 2D array do not have the
     * same lengths.
     */
    public Map(TileType[][] tiles) {
        // If the given 2D tile array is actually null, throw an exception
        if(tiles == null) {
            throw new IllegalArgumentException("The lists in the 2D array of tiles cannot be null.");
        }

        // If the given 2D tile array is empty, throw an exception
        if(tiles.length == 0) {
            throw new IllegalArgumentException("The lists in the 2D array of tiles cannot be empty.");
        }

        // If the lists in the 2D tile array do not have equal lengths (i.e. it does not have dimensions n x n) throw an
        // exception
        if(tiles.length != tiles[0].length) {
            throw new IllegalArgumentException("The lists in the 2D array of tiles must have equal lengths " +
                    "(they must form a square).");
        }

        // Set the size of the map to the size of the array
        size = tiles.length;

        // Store the given 2D tile array in the "tiles" member
        this.tiles = tiles;
    }

    /**
     * Getter method used to get the {@code size} attribute of the {@code Map} object.
     * @return the size of the map
     */
    public int getSize() {
        return size;
    }

    /**
     * Method used to randomly generate the map. This method should be implemented by each subclass of the Map class to
     * allow different methods of map generation.
     */
    abstract void generate();

    /**
     * Function used to check if the given coordinate is a valid position which exists in the map.
     * @param x x-coordinate in the map
     * @param y y-coordinate in the map
     * @return true if the given (x,y) coordinate is a valid tile position in the map and false otherwise.
     */
    boolean isValidPosition(int x, int y) {
        // Check if the x and y coordinates are within the ranges of the "tiles" array (from 0 to size)
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    /**
     * Function used to check the type of the tile at the given coordinate in the map.
     * @param x x-coordinate in the map
     * @param y y-coordinate in the map
     * @return the type of the tile at position (x,y)
     */
    TileType getTileType(int x, int y) {
        // If the given position is invalid throw an exception
        if(!isValidPosition(x,y)) {
            throw new IllegalArgumentException("Given tile position is not valid.");
        }

        // Get the tile at the given position from the "tiles" 2D array
        return tiles[x][y];
    }
}
