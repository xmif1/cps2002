package com.xd.cps2002.game.game_exceptions;

/**
 * Exception intended to be thrown whenever the map size is too small or too large, depending on a number of criteria.
 */
public class InvalidMapSizeException extends Exception{
    public InvalidMapSizeException(int map_size){
        System.out.println("Invalid input provided: Cannot have map of size " + map_size + "x" + map_size + "." +
                           " The minimum map size is 5x5 while the maximum is 50x50.");
    }

    public InvalidMapSizeException(int map_size, String s){
        System.out.println("Invalid input provided: Cannot have map of size " + map_size + "x" + map_size + "." +
                           " The minimum map size is 5x5 while the maximum is 50x50. " + s);
    }
}
