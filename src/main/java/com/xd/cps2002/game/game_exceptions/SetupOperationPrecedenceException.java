package com.xd.cps2002.game.game_exceptions;

/**
 * Simple unchecked exception intended to be thrown when setup is carried out in an incorrect order, resulting in some unstable
 * state of the system variables.
 */
public class SetupOperationPrecedenceException extends RuntimeException{
    public SetupOperationPrecedenceException(String s){
        System.err.println("Setup Operations Executed In Wrong Sequence: " + s);
    }
}
