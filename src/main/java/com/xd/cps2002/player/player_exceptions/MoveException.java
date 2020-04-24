package com.xd.cps2002.player.player_exceptions;

/**
 * Simple exception to handle when a character beyond {'u', 'd', 'l', r'} has been supplied to move().
 */
public class MoveException extends Exception{
    public MoveException(){
        System.out.println("Invalid input provided. Only the characters [U|D|L|R] allowed.");
    }
}
