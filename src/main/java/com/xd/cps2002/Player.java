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

    /* The boolean move(String input) function returns either true or false, depending if the input is invalid or valid.
     * The criteria for returning false are three, all checked in the function boolean validateInput(String input):
     *  (i) String input is null
     *  (ii) the input is either "" or of length greater than 1 i.e. not a character
     *  (iii) the input is a character, but not from the valid set of characters (u, d, l, r)
     *
     * If none of the criteria above are met, then the function returns true. Before doing so, the player position is
     * updated accordingly. [TO-DO]
     *
     * The function is intentionally designed not to ask for input. This is to allow flexibility to the calling client,
     * eg. by choosing to impose a limitation on the number of times a player is asked for input.*/
    public boolean move(String input){
        if(!validateInput(input)){ // check input validation criteria
            System.out.println("Invalid input entered."); // print that input is invalid, then return false
            return false;
        }
        else{
            switch(Character.toLowerCase(input.charAt(0))){ // test against cases to carry out necessary logic
                case 'u': return true; // [TO-DO]
                case 'd': return true; // [TO-DO]
                case 'l': return true; // [TO-DO]
                case 'r': return true; // [TO-DO]

                default: System.out.println("Invalid input entered."); return false;
            }
        }
    }

    // simple convenience function to validate String input
    private boolean validateInput(String input){
        if(input == null || input.length() != 1){ // handle when input String is null or not a single character
            return false;
        }
        else{
            char c = Character.toLowerCase(input.charAt(0)); // case insensitive eg. to ignore CAPS-LOCK incidents
            return c == 'u' || c == 'd' || c == 'l' || c == 'r'; // else return false if not a character from u, d, l, r
        }
    }
}
