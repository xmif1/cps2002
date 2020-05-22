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
 * accordingly. Setters for the state variables are provided, however they cannot be called if initialise() has been
 * called prior, to enforce use of the Game class only through the initialise() method. However, all of the
 * aforementioned functions are public, so that anyone can create their own setup sequence, at their own responsibility
 * for maintaining state consistency.
 *
 * @author Xandru Mifsud
 */
public class Game{
    private static Game instance = null; // the singleton instance

    private Player[] players = null;
    private Team[] teams = null;
    private Map map = null;
    private HTMLGenerator htmlGenerator = HTMLGenerator.getHTMLGenerator();
    private boolean is_set;

    public String dir = null;

    /**
     * Private constructor to initialize an Game instance (if one does not already exist).
     */
    private Game(){is_set = false;}

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
        teams = null;
        dir = null;
        is_set = false;
    }

    /**
     * Simple function to check whether a Game instance has been initialised. is_set is only set by the initialise()
     * method, and hence isInitialised() only returns true if initialise() has been called.
     * @return boolean which is True only when is_set os true.
     */
    public boolean isInitialised(){
        return is_set;
    }

    /**
     * Initialises the state variables of the Game instance in a consistent manner, else an exception is thrown if this
     * cannot be done.
     * @param n_players is the number of Player instances to initialise (2 <= n_players <= 8).
     * @param n_teams is the number of teams to initialise and divide the players into (2 <= n_teams <= n_players - 1).
     * @param map_size is the grid size of the map (5 <= map_size <= 50 if n_players < 5, 8 <= map_size <= 50 otherwise).
     * @throws InvalidNumberOfPlayersException is propagated forward from genPlayers(n_players).
     * @throws InvalidMapSizeException is propagated forward from genMap(map_size, players).
     * @throws InvalidNumberOfTeamsException is propagated forward from genTeams(n_teams, players).
     */
    public void initialise(int n_players, int n_teams, int map_size) throws InvalidNumberOfPlayersException,
            InvalidMapSizeException, InvalidNumberOfTeamsException{

        players = genPlayers(n_players); // generate n_players Player instances
        map = genMap(map_size, players); // generate map of size map_size, based on number of players in players
        players = genPlayerPositions(players, map); // generate starting positions based on players and map
        teams = genTeams(n_teams, players); // generate n_teams Team instances

        is_set = true;
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
     * Setter for the Player[] players array, given that setPlayers() or initialise() have not been called prior.
     * @param players is the Player[] array to attempt to set this.players to.
     * @throws SetupOperationPrecedenceException is thrown when setPlayer() or initialise() was called prior.
     */
    public void setPlayers(Player[] players) throws SetupOperationPrecedenceException{
        if(this.players != null){ // if setPlayer() called explicitly or via initialise() prior
            throw new SetupOperationPrecedenceException("Cannot set Game.players after call to initialise().");
        }
        else{
            this.players = players;
        }
    }

    /**
     * Setter for the Map map instance, given that setMap() or initialise() have not been called prior.
     * @param map is the Map instance to attempt to set this.map to.
     * @throws SetupOperationPrecedenceException is thrown when setMap() or initialise() was called prior.
     */
    public void setMap(Map map) throws SetupOperationPrecedenceException{
        if(this.map != null){ // if setMap() called explicitly or via initialise() prior
            throw new SetupOperationPrecedenceException("Cannot set Game.map after call to initialise().");
        }
        else {
            this.map = map;
        }
    }

    /**
     * Setter for the Team[] teams array, given that setTeams() or initialise() have not been called prior.
     * @param teams is the Team[] teams array to attempt to set this.teams to.
     * @throws SetupOperationPrecedenceException is thrown when initialise() was called prior i.e. is_set == true;
     */
    public void setTeams(Team[] teams) throws SetupOperationPrecedenceException{
        if(this.teams != null){ // if setTeams() called explicitly or via initialise() prior
            throw new SetupOperationPrecedenceException("Cannot set Game.teams after call to initialise().");
        }
        else {
            this.teams = teams;
        }
    }

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

    /**
     * @param n_players is an integer to be checked if it falls within the range 2 <= n_players <= 8;
     * @return true if in range, false otherwise.
     */
    public boolean isValidNPlayers(int n_players){
        return 2 <= n_players && n_players <= 8;
    }

    /**
     * If n_players < 5, and 5 <= map_size <= 50, or 5 <= n_players and 8 <= map_size <= 50, return true.
     * @param map_size is an integer to be checked if it falls within one of the two ranges above, given a value of n_players.
     * @param n_players is the number of players, which shall determine the minimum map size.
     * @return true if in range, false otherwise.
     */
    public boolean isValidMapSize(int map_size, int n_players){
        // else if the map_size is too small for 5 or more players
        if(51 <= map_size || map_size <= 4){ // else if map_size is outside the global minimum and maximum size
            return false;
        }
        else return 5 > n_players || map_size > 7;
    }

    /**
     * @param n_teams is the number of teams, which must fall between 2 and n_players - 1 (both inclusive).
     * @param n_players the number of players to be partitioned into teams.
     * @return true if in range, false otherwise.
     */
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

        if(isValidNPlayers(n_players)){ // validation check
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
            return MapCreator.createMap("basic", map_size);
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

        return players;
    }

    /**
     * Initialises n_teams instances of Team and allocates players proportionally between the Team instances.
     * Allocation of the number of players per team is done greedily, by taking the floor of n_teams/players.length,
     * and the last team having the remainder.
     * If 2*n_teams > n_players, then n_teams - 1 will contain 1 players, and the last team will contain
     *                                   n_players - n_teams + 1 players
     * This is not restricted since it is not (technically) an invalid case. One can add further external checks if they
     * choose to restrict 2*n_teams <= n_players.
     * @param n_teams is the number of teams to create.
     * @param players is a Player[] array of Player instances to be grouped into n_teams teams.
     * @return teams array with n_team initialised Team instances.
     * @throws InvalidNumberOfTeamsException if the number of teams is invalid (not in range 2 <= n_teams < players.length).
     * @throws SetupOperationPrecedenceException if player instances do not have an initialised starting position or if
     * they have already been joined to a team.
     */
    public Team[] genTeams(int n_teams, Player[] players) throws InvalidNumberOfTeamsException{
        if(isValidNTeams(n_teams, players.length) || n_teams == players.length){
            teams = new Team[n_teams]; // initialize if within range
            shufflePlayers(players);

            int players_idx = 0; // maintains an index to the players array
            int team_size = (int) Math.floor((double) players.length / n_teams); // number of players (on average) per team

            for(int i = 0; i < n_teams; i++) { // begin populating teams
                teams[i] = new Team();

                // how many players in the team - if the last team, must contain the remainder of the players
                int team_n_players = (i < n_teams - 1) ? team_size : players.length - (n_teams - 1) * team_size;

                try {
                    for (int j = players_idx; j < players_idx + team_n_players; j++) {
                        teams[i].join(players[j]); // join players to team
                    }

                    players_idx += team_n_players;

                } catch (TeamOverrideException toe) {
                    throw new SetupOperationPrecedenceException("Invalid attempt to join a team when players has already"
                            + " joined to a team");
                } catch (NullPositionException npe) { // wrap around SetupOperationPrecedenceException for better context
                    throw new SetupOperationPrecedenceException("Invalid attempt to join a team when player does not have"
                            + " an initialised starting position.");
                }
            }

            return teams;
        }
        else{
            throw new InvalidNumberOfTeamsException(n_teams); // else throw exception
        }
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