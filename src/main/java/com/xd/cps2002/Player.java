package com.xd.cps2002;

public class Player{
    private static int global_player_count = 0; // maintains count of the number of Player instances created
    private int player_id; // auto-incrementing upon instantiation

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

    /* The boolean move(char input) function returns true if a character from the set {'u', 'd', 'r', 'l'} (case-
     * insensitive) is passed as input. Before doing so, the player position is updated accordingly. [TO-DO]
     *
     * The function is intentionally designed not to ask for input. This is to allow flexibility to the calling client,
     * eg. by choosing to impose a limitation on the number of times a player is asked for input.
     *
     * If an invalid character is passed, a MoveException is thrown.*/
    public boolean move(char input) throws Exception{
        switch(Character.toLowerCase(input)){ // test against cases to carry out necessary logic
            case 'u': return true; // [TO-DO]
            case 'd': return true; // [TO-DO]
            case 'l': return true; // [TO-DO]
            case 'r': return true; // [TO-DO]

            default: throw new Exception("Invalid input provided. Only the characters [U|D|L|R] allowed.");
        }
    }
}
