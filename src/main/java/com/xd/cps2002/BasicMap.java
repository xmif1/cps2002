package com.xd.cps2002;

public class BasicMap implements Map {
    private int size;
    private int noOfPlayers;
    private TileType[][] tiles;

    /**
     * Constructor used to initialize an empty BasicMap object.
     * @param n size of the n x n square map
     */
    public BasicMap(int n) {
        // If map size is invalid (not between 5-50) throw an exception
        if(n < 5 || n > 50) {
            throw new IllegalArgumentException("BasicMap was initialized with an invalid size argument.");
        }

        // Otherwise set the map size as normal
        this.size=n;
    }

    @Override
    public boolean setSize(int n) {
        return false;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void generate() {

    }

    @Override
    public boolean isValidPosition(int x, int y) {
        return false;
    }

    @Override
    public TileType getTileType(int x, int y) {
        return null;
    }
}
