package com.xd.cps2002.player.player_exceptions;

/**
 * Simple exception to handle when a player's Position position has not been set.
 */
public class NullPositionException extends RuntimeException{
    public NullPositionException(int player_id){
        System.err.println("Player #" + player_id + " does not have position initialised.");
    }
}
