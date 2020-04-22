package com.xd.cps2002;

import com.xd.cps2002.map.Map;
import com.xd.cps2002.map.MapCreator;

import java.util.Random;

/**
 * The MainGame class is responsible for coordinating the main logic of the game, and the primary interface for user
 * input and provision of output to the user. It implements a Singleton design pattern, and is designed to be the
 * primary interface to the game API, providing descriptive and detailed errors. Moreover, it handles the majority of
 * setup and is designed to prevent the execution of setup steps in an invalid procedural order, if done so through this
 * class. Otherwise access to the sub-systems is left unhindered by this class.
 *
 * The setup sequence via this class is intended to be executed in the following manner, with a number of checks to
 * ensure so:
 *
 * [label:start setupPlayers(int n_players) -> setupMap(int map_size) -> setPlayerPositions()] |-> reset() goto:start
 *
 * @author Xandru Mifsud
 */
public class MainGame{
    private static MainGame instance = null; // the singleton instance
    public Player[] players = null;
    public Map map = null;

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
     * Sets the MainGame.players and MainGame.map to null, allowing for the setup functions to be called again sequentially.
     */
    public void reset(){
        players = null;
        map = null;
    }

    /**
     * Initializes n_players Player instances, if n_players is not less than 2 or greater then 8.
     * @param n_players is the number of players to be initialized.
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is < 2 or > 8.
     */
    public void setupPlayers(int n_players) throws InvalidNumberOfPlayersException{
        if(map != null){
            throw new SetupOperationPrecedenceException("Invalid attempt to setup Player instances after a Map instance" +
                    " has already been initialized.");
        }
        else if(2 <= n_players && n_players <= 8){ // validation check
            players = new Player[n_players]; // initialize if within range

            for(int i = 0; i < n_players; i++){
                players[i] = new Player();
            }
        }
        else{
            throw new InvalidNumberOfPlayersException(n_players); // else throw exception
        }
    }

    /**
     * Initializes a Map of type map_type and size map_size, provided that:
     * i.   Any players in the game have been initialized (i.e. MainGame.players is not null)
     * ii.  The map_size is not less than 5 and not greater than 50.
     * iii. If the number of players is at least 5, than the minimum map_size is at least 8.
     * @param map_size is the size of the map.
     * @throws SetupOperationPrecedenceException is thrown if MainGame.players is null. [Criteria i. above]
     * @throws InvalidMapSizeException is thrown if the map_size is invalid. [Criteria ii. & iii. above]
     */
    public void setupMap(int map_size) throws InvalidMapSizeException{

        if(players == null){ // if players have not been initialized i.e. MainGame.players is null
            throw new SetupOperationPrecedenceException("Invalid attempt to setup Map instance before Player instances.");
        }
        else if(51 <= map_size || map_size <= 4){ // else if map_size is outside the global minimum and maximum size
            throw new InvalidMapSizeException(map_size);
        }
        else if(5 <= players.length && map_size <= 7){ // else if the map_size is too small for 5 or more players
            throw new InvalidMapSizeException(map_size, "For 5 to 8 players, the minimum map size is 8x8.");
        }
        else{ // else initialize map
            do{
                map = MapCreator.createMap("basic", map_size);
                map.generate();
            } while(!map.isPlayable()); // attempt map creation until generated map is playable
        }
    }

    /**
     * Initializes the starting position for all the Player instance in MainGame.players.
     * @throws SetupOperationPrecedenceException is thrown if a Map instance has not already been created, or if
     *         MainGame.players is null.
     */
    public void setPlayerPositions(){

        if(players == null){ // if players have not been initialized i.e. MainGame.players is null
            throw new SetupOperationPrecedenceException("Invalid attempt to set players positions when no Players " +
                    "initialized.");
        }
        else if(map == null){ // else if a Map instance has not already been created
            throw new SetupOperationPrecedenceException("Invalid attempt to set players positions when no Map has been" +
                    " initialized.");
        }
        else{
            // for each player, initialize starting position
            for(Player player : players){
                Position starting_position;
                do{
                    // randomly generate position within map size
                    starting_position = new Position(new Random().nextInt(map.getSize()),
                                                     new Random().nextInt(map.getSize()));
                }while(!map.isPositionWinnable(starting_position)); // check that the treasure tile is reachable

                player.setStartPosition(starting_position); // set position
            }
        }
    }
}

/**
 * Simple unchecked exception intended to be thrown when setup is carried out in an incorrect order, resulting in some unstable
 * state of the system variables.
 */
class SetupOperationPrecedenceException extends RuntimeException{
    public SetupOperationPrecedenceException(String s){
        System.err.println("Setup Operations Executed In Wrong Sequence: " + s);
    }
}

/**
 * Exception intended to be thrown whenever an invalid number of players is passed.
 */
class InvalidNumberOfPlayersException extends Exception{
    public InvalidNumberOfPlayersException(int n_players){
        System.out.println("Invalid input provided: Cannot have " + n_players + " players." +
                " The minimum number of players is 2 while the maximum is 8.");
    }
}

/**
 * Exception intended to be thrown whenever the map size is too small or too large, depending on a number of criteria.
 */
class InvalidMapSizeException extends Exception{
    public InvalidMapSizeException(int map_size){
        System.out.println("Invalid input provided: Cannot have map of size " + map_size + "x" + map_size + "." +
                " The minimum map size is 5x5 while the maximum is 50x50.");
    }

    public InvalidMapSizeException(int map_size, String s){
        System.out.println("Invalid input provided: Cannot have map of size " + map_size + "x" + map_size + "." +
                " The minimum map size is 5x5 while the maximum is 50x50. " + s);
    }
}
