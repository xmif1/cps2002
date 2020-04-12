package com.xd.cps2002;

public class Player{
    private static int global_player_count = 0; // maintains count of the number of Player instances created
    private int player_id; // auto-incrementing upon instantiation
    private Position position = null; // maintain the current player position
    private Position start_position = null; // maintain the start position of the player

    public Player(){
        this.player_id = global_player_count++; // auto-incrementation
    }

    // getter for private static int global_player_count
    public static int get_global_player_count(){
        return global_player_count;
    }

    // getter for the unique player id
    public int get_pID(){
        return this.player_id;
    }

    // getter for the player Position position
    public Position getPosition(){
        return position;
    }

    // setter for the player Position position, only allowed if start_position has been set beforehand
    public void setPosition(Position position) throws NullPositionException{
        if(start_position == null){
            throw new NullPositionException(player_id);
        }
        else{
            this.position = position;
        }
    }

    // getter for the player Position start_position
    public Position getStartPosition(){
        return start_position;
    }

    // setter for the player Position start_position, and hence also the position
    public void setStartPosition(Position start_position){
        this.start_position = start_position;
        this.position = start_position;
    }

    /* The boolean move(char input) function returns true if a character from the set {'u', 'd', 'r', 'l'} (case-
     * insensitive) is passed as input. Before doing so, the player position is updated accordingly, given that the
     * position has been set. If the position is null, a NullPositionException is thrown.
     *
     * The function is intentionally designed not to ask for input. This is to allow flexibility to the calling client,
     * eg. by choosing to impose a limitation on the number of times a player is asked for input.
     *
     * If an invalid character is passed, a MoveException is thrown.
     *
     * While being agnostic from the map, it is assumed throughout this project that the (i,j) index of a map is the
     * (i,j) cartesian coordinate. This is important to note when shifting indices to represent moves.*/
    public Position move(char input) throws NullPositionException, MoveException{
        // in case position is not set, throw exception
        if(position == null){
            throw new NullPositionException(player_id);
        }
        switch(Character.toLowerCase(input)){ // test against cases to carry out necessary logic for shifting position
            case 'u': return new Position(position.x, position.y - 1);
            case 'd': return new Position(position.x, position.y + 1);
            case 'l': return new Position(position.x - 1, position.y);
            case 'r': return new Position(position.x + 1, position.y);

            default: throw new MoveException();
        }
    }
}

// simple exception to handle when a player's Position position has not been set
class NullPositionException extends Exception{
    public NullPositionException(int player_id){
        System.err.println("Player #" + player_id + " does not have position initialised.");
    }
}

// simple exception to handle when a character beyond {'u', 'd', 'l', r'} has been supplied to move()
class MoveException extends Exception{
    public MoveException(){
        System.err.println("Invalid input provided. Only the characters [U|D|L|R] allowed.");
    }
}
