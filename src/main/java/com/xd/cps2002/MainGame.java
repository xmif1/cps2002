package com.xd.cps2002;

/**
 * The MainGame class is responsible for coordinating the main logic of the game, and the primary interface for user
 * input and provision of output to the user.
 *
 * @author Xandru Mifsud
 */
public class MainGame{
    private static MainGame instance = null; // the singleton instance
    public static Player[] players = null;
    public static Map map = null;

    /**
     * Private constructor to initialize an MainGame instance (if one does not already exist).
     */
    private MainGame(){ }

    /**
     * Returns an MainGame instance; in the case that an instance already exists, it returns the exisiting one.
     * Else it creates a new instance and returns it.
     * @return MainGame instance is the singleton to be returned.
     */
    public static MainGame getMainGame(){
        if(instance == null){
            instance = new MainGame();
        }
        return instance;
    }

    /**
     * Dereferences the MainGame instance to null.
     */
    public static void dereferenceMainGame(){
        instance = null;
    }

    /**
     * Initializes n_players Player instances, if n_players is not less than 2 or greater then 8.
     * @param n_players is the number of players to be initialized.
     * @return boolean true if MainGame.players has been set.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is < 2 or > 8.
     */
    public boolean setupPlayers(int n_players) throws InvalidNumberOfPlayersException{
        if(2 <= n_players && n_players <= 8){ // validation check
            players = new Player[n_players]; // initialize if within range
            return true;
        }
        else{
            throw new InvalidNumberOfPlayersException(n_players); // else throw exception
        }
    }

    /**
     * Initializes a Map of size map_size, provided that:
     * i.   Any players in the game have been initialized (i.e. MainGame.players is not null)
     * ii.  The map_size is not less than 5 and not greater than 50.
     * iii. If the number of players is at least 5, than the minimum map_size is at least 8.
     * @param map_size is the size of the map.
     * @return boolean true if MainGame.map has been set.
     * @throws InvalidNumberOfPlayersException is thrown if MainGame.players is null. [Criteria i. above]
     * @throws InvalidMapSizeException is thrown if the map_size is invalid. [Criteria ii. & iii. above]
     */
    public boolean setupMap(int map_size) throws InvalidNumberOfPlayersException, InvalidMapSizeException{
        if(players == null){ // if players have not been initialized
            throw new InvalidNumberOfPlayersException(0);
        }
        else if(51 <= map_size || map_size <= 4){ // else if map_size is outside the global minimum and maximum size
            throw new InvalidMapSizeException(map_size);
        }
        else if(5 <= players.length && map_size <= 7){ // else if the map_size is too small for 5 or more players
            throw new InvalidMapSizeException(map_size, "For 5 to 8 players, the minimum map size is 8x8.");
        }
        else{ // else initialize map
            // TO-DO
            return true;
        }
    }
}

class InvalidNumberOfPlayersException extends Exception{
    public InvalidNumberOfPlayersException(int n_players){
        if(n_players == 0){
            System.err.println("No players have been initialised.");
        }
        else{
            System.err.println("Invalid input provided: Cannot have " + n_players + " players." +
                    " The minimum number of players is 2 while the maximum is 8.");
        }
    }
}

class InvalidMapSizeException extends Exception{
    public InvalidMapSizeException(int map_size){
        System.err.println("Invalid input provided: Cannot have map of size " + map_size + "x" + map_size + "." +
                " The minimum map size is 5x5 while the maximum is 50x50.");
    }

    public InvalidMapSizeException(int map_size, String s){
        System.err.println("Invalid input provided: Cannot have map of size " + map_size + "x" + map_size + "." +
                " The minimum map size is 5x5 while the maximum is 50x50. " + s);
    }
}
