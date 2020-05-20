package com.xd.cps2002.game.game_exceptions;

/**
 * Exception intended to be thrown whenever an invalid number of players is passed.
 */
public class InvalidNumberOfPlayersException extends Exception{
    public InvalidNumberOfPlayersException(int n_players){
        System.out.println("Invalid input provided: Cannot have " + n_players + " players." +
                           " The minimum number of players is 2 while the maximum is 8.");
    }
}
