package com.xd.cps2002.player;

/**
 * Simple convenience class to maintain (x,y)--coordinates (representative of indices).
 *
 * @author Xandru Mifsud
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

    /**
     * Basic equality operator to check if two Positions are the same with regards
     * to their coordinates on the plane.
     * @param o The object to compare against.
     */
    @Override
    public boolean equals(Object o){
        if(o == null){ // if null, return false
            return false;
        }
        else if(getClass() != o.getClass()){ // if derived from a different class, return false
            return false;
        }

        Position position = (Position) o; // else cast

        return (position.x == this.x) && (position.y == this.y); // and return equality check
    }
}
