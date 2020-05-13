package com.xd.cps2002.player.player_exceptions;

/**
 * Simple exception to handle when a player's Team team has not been set.
 */
public class NullTeamException extends RuntimeException{
    public NullTeamException(int player_id){
        System.err.println("Player #" + player_id + " has not been joined with a team.");
    }
}
