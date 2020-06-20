package com.xd.cps2002.player.player_exceptions;

/**
 * Simple exception to handle when a player's Team team has been set and there is an attempt to change it.
 */
public class TeamOverrideException extends Exception{
    public TeamOverrideException(int player_id){
        System.err.println("Player #" + player_id + " already has a team initialised.");
    }
}
