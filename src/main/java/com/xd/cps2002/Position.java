package com.xd.cps2002;

/**
 * Simple convenience class to maintain (x,y)--coordinates (representative of indices).
 */
public class Position{
    public int x; // the x-coordinate, as an integer (to serve as an array index)
    public int y; // the y-coordinate, as an integer (to serve as an array index)

    /**
     * Basic constructor to set the (x, y)--coordinates upon instantiation.
     * @param x The x-coordinate, as an integer (to serve as an array index).
     * @param y The y-coordinate, as an integer (to serve as an array index).
     */
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }
}
