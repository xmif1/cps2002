package com.xd.cps2002;

public class BasicMap extends Map {
    private int size;
    private int noOfPlayers;
    private TileType[][] tiles;

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

    @Override
    public void generate() {
        // TODO add map generation
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
