package com.xd.cps2002.game.game_exceptions;

/**
 * Exception intended to be thrown whenever an invalid number of teams is passed.
 */
public class InvalidNumberOfTeamsException extends Exception{
    public InvalidNumberOfTeamsException(int n_teams){
        System.out.println("Invalid input provided: Cannot have " + n_teams + " teams." +
                           " The minimum number of teams is 2 while the maximum is the number of players.");
    }
}
