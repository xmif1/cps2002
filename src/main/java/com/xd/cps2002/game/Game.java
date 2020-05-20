package com.xd.cps2002.game;

import com.xd.cps2002.game.game_exceptions.*;
import com.xd.cps2002.map.*;
import com.xd.cps2002.player.*;
import com.xd.cps2002.player.player_exceptions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;

/**
 * The MainGame class is responsible for coordinating the main logic of the game, and the primary interface for user
 * input and provision of output to the user. It is designed to be the primary interface to the game API, providing
 * descriptive and detailed errors. Moreover, it handles the majority of setup and is designed to prevent the execution
 * of setup steps in an invalid procedural order, if done so through this class. Otherwise access to the sub-systems is
 * left unhindered by this class. It follows the Singleton design pattern, allowing better granular control over the
 * creation of entities, especially Map.
 *
 * The setup sequence via this class is intended to be executed in the following manner,
 *
 * [label:start genPlayers(int n_players) -{@literal >} genMap(int map_size, Player[] players) -{@literal >}
 * genPlayerPositions(Player[] players, Map map)] -{@literal >} genTeams(int n_teams, Player[] players)
 *
 * With the specification of n_players, n_teams and map_size, a call to initialise() will execute the above sequence
 * accordingly. Setters for the state variables are not provided intentionally, to enforce use of the Game class only
 * through the initialise() method. However, all of the aforementioned functions are public, so that anyone can create
 * their own setup sequence, at their own responsibility.
 *
 * @author Xandru Mifsud
 */
public class Game{
    private static Game instance = null; // the singleton instance

    private Player[] players = null;
    private Team[] teams = null;
    private Map map = null;
    private HTMLGenerator htmlGenerator = HTMLGenerator.getHTMLGenerator();
    private boolean init_players, init_map, init_positions, init_teams, is_set;

    public String dir = null;

    /**
     * Private constructor to initialize an Game instance (if one does not already exist).
     */
    private Game(){init_players = init_map = init_positions = init_teams = is_set = false;}

    /**
     * Returns a Game instance; in the case that an instance already exists, it returns the exisiting one.
     * Else it creates a new instance and returns it.
     * @return Game instance is the singleton to be returned.
     */
    public static Game getGame(){
        if(instance == null){
            instance = new Game();
        }
        return instance;
    }

    /**
     * Resets the game state.
     */
    public void reset(){
        players = null;
        map = null;
        dir = null;
        init_players = init_map = init_positions = init_teams = is_set = false;
    }

    /**
     * @return boolean which is True only when all of init_players, init_map, init_positions and init_teams are true.
     */
    public boolean isInitialised(){
        return init_players && init_map && init_positions && init_teams && is_set;
    }

    // ----- GETTERS -----

    /**
     * Simple getter for the Game instance's players array.
     * @return Player[] array with all player instances set for the current Game instance.
     */
    public Player[] getPlayers(){
        return players;
    }

    /**
     * Simple getter for the Game instance's Map instance.
     * @return Map with the Map instance set for the current Game instance.
     */
    public Map getMap(){
        return map;
    }

    /**
     * Simple getter for the Game instance's teams array.
     * @return Teams[] array with all the team instances set for the current Game instance.
     */
    public Team[] getTeams(){
        return teams;
    }

    // ----- SETTERS -----

    /**
     * Simple function to set the directory path at which to write the generated HTML maps.
     * @param dir is the directory path specified by the user, in which to write the HTML files.
     * @throws IOException is thrown whenever the path specified is not a directory.
     */
    public void setHTMLDirectory(String dir) throws IOException{
        Path path;

        try {
            path = Paths.get(dir);
        }
        catch(Exception e){
            throw new IOException(); // throw an IOException
        }

        if(Files.exists(path) && Files.isDirectory(path)){ // if valid directory, set dir to specified path
            if(System.getProperty("file.separator").charAt(0) == (dir.charAt(dir.length()-1))){
                this.dir = dir.substring(0, dir.length() - 1);
            }
            else {
                this.dir = dir;
            }
        }
        else{
            throw new IOException(); // else throw an IOException
        }
    }

    // -------VALIDATION CHECKS--------

    public boolean isValidNPlayers(int n_players){
        return 2 <= n_players && n_players <= 8;
    }

    public boolean isValidMapSize(int map_size, int n_players){
        // else if the map_size is too small for 5 or more players
        if(51 <= map_size || map_size <= 4){ // else if map_size is outside the global minimum and maximum size
            return false;
        }
        else return 5 > n_players || map_size > 7;
    }

    public boolean isValidNTeams(int n_teams, int n_players){
        return 2 <= n_teams && n_teams < n_players;
    }

    // --------SETUP FUNCTIONS--------

    /**
     * Initializes n_players Player instances, if n_players is not less than 2 or greater then 8.
     * @param n_players is the number of players to be initialized.
     * @return players is the array of initialised Player instances.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8.
     */
    public Player[] genPlayers(int n_players) throws InvalidNumberOfPlayersException{
        Player[] players;

        if(2 <= n_players && n_players <= 8){ // validation check
            players = new Player[n_players]; // initialize if within range

            for(int i = 0; i < n_players; i++){
                players[i] = new Player();
            }

            return players;
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
     * @param players is an array of Player instances.
     * @return map is the initialised Map instance.
     * @throws InvalidMapSizeException is thrown if the map_size is invalid. [Criteria ii. {@literal &} iii. above]
     */
    public Map genMap(int map_size, Player[] players) throws InvalidMapSizeException{
        if(51 <= map_size || map_size <= 4){ // else if map_size is outside the global minimum and maximum size
            throw new InvalidMapSizeException(map_size);
        }
        else if(5 <= players.length && map_size <= 7){ // else if the map_size is too small for 5 or more players
            throw new InvalidMapSizeException(map_size, "For 5 to 8 players, the minimum map size is 8x8.");
        }
        else{ // else initialize map
            Map map;

            do{
                map = MapCreator.createMap("basic", map_size);
                map.generate();
            } while(!map.isPlayable()); // attempt map creation until generated map is playable

            return map;
        }
    }

    /**
     * Initializes the starting position for all the Player instances players, based on the passed Map instance map.
     * @param players is an array of Player instances for which the starting positions are to be initialised.
     * @param map is a Map instance on which the starting positions are to be initialised.
     * @return players array with starting positions initialised.
     */
    public Player[] genPlayerPositions(Player[] players, Map map){
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

        init_positions = true; // mark as initialised
        return players;
    }

    public void genTeams(int n_teams, Player[] players){
        teams = new Team[n_teams]; // initialize if within range
        shufflePlayers(players);

        int players_idx = 0; // maintains an index to the players array
        int team_size = (int) Math.floor((double) players.length / n_teams); // number of players (on average) per team

        for(int i = 0; i < n_teams; i++){ // begin populating teams
            teams[i] = new Team();

            // how many players in the team - if the last team, must contain the remainder of the players
            int team_n_players = (i < n_teams - 1) ? team_size : players.length - (n_teams - 1)*team_size;

            try{
                for(int j = players_idx; j < players_idx + team_n_players; j++){
                    teams[i].join(players[j]); // join players to team
                }

                players_idx += team_n_players;

            }catch(TeamOverrideException toe){
                throw new SetupOperationPrecedenceException("Invalid attempt to join a team when players has already " +
                                                            "joined to a team");
            }catch(NullPositionException npe){ // wrap around SetupOperationPrecedenceException for better context
                throw new SetupOperationPrecedenceException("Invalid attempt to join a team when player does not have " +
                        "an initialised starting position.");
            }
        }

        init_teams = true;
    }

    // -------- UTILITY FUNCTIONS ---------

    /**
     * Shuffling of the players array, to randomise turn sequence. For example, it is also used to randomise team allocation.
     * This is a variant of Fisher-Yates shuffle, known as Durstenfeld's Shuffle (as mentioned in Knuth's The Art of
     * Computer Programming), with the Java adaptation based on https://stackoverflow.com/a/1520212.
     *
     * @param players is the array of Player instances to be shuffled.
     */
    private void shufflePlayers(Player[] players){
        Random rnd = ThreadLocalRandom.current();
        for (int i = players.length - 1; i > 0; i--){
            int j = rnd.nextInt(i + 1);

            // shuffling swap
            Player p = players[j];
            players[j] = players[i];
            players[i] = p;
        }
    }

    /**
     * Convenience function for generating and persisting to disk the HTML map file for each player instance in players.
     * @param players is the array of Player instances for which an HTML map is to be generated.
     * @param map is a Map instance on which the HTML maps are to be built.
     * @throws IOException is thrown when there is a failure in persisting to disk [generally fatal].
     */
    public void writeHTMLFiles(Player[] players, Map map) throws IOException{
        if(dir == null){ // if directory not set, throw a SetupOperationPrecedenceException
            throw new SetupOperationPrecedenceException("Directory to write HTML files not specified.");
        }
        else{
            for(Player player : players){ // generate and persist to disk the HTML map for each player instance
                FileWriter writer = new FileWriter(dir + System.getProperty("file.separator") +
                                                   "player_" + player.get_pID() + "_map.html");

                // iterate through the html ArrayList and persist
                for(String ln : htmlGenerator.genPlayerMap(player, map)){
                    writer.write(ln);
                }
                writer.close();
            }
        }
    }
}