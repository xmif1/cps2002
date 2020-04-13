package com.xd.cps2002;

public class BasicMap extends Map {
    private int size;
    private int noOfPlayers;
    private TileType[][] tiles;

    /**
     * Constructor used to initialize an empty {@code BasicMap} object. Uses the constructor of the {@link Map} super
     * class.
     * @param n size of the {@code n} x {@code n} square map
     * @throws IllegalArgumentException if the method is given an invalid size parameter (outside of the range 5-50)
     */
    public BasicMap(int n) {
        // Initialize Map object using the Map class constructor
        super(n);
    }

    @Override
    public void generate() {
        // TODO add map generation
    }
}
