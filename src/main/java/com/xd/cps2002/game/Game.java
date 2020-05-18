package com.xd.cps2002.game;

import com.xd.cps2002.game.game_exceptions.InvalidMapSizeException;
import com.xd.cps2002.game.game_exceptions.InvalidNumberOfPlayersException;
import com.xd.cps2002.game.game_exceptions.SetupOperationPrecedenceException;
import com.xd.cps2002.map.Map;
import com.xd.cps2002.map.MapCreator;
import com.xd.cps2002.player.Player;
import com.xd.cps2002.player.Position;
import com.xd.cps2002.player.Team;
import com.xd.cps2002.player.player_exceptions.NullPositionException;
import com.xd.cps2002.player.player_exceptions.TeamOverrideException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The MainGame class is responsible for coordinating the main logic of the game, and the primary interface for user
 * input and provision of output to the user. It is designed to be the primary interface to the game API, providing
 * descriptive and detailed errors. Moreover, it handles the majority of setup and is designed to prevent the execution
 * of setup steps in an invalid procedural order, if done so through this class. Otherwise access to the sub-systems is
 * left unhindered by this class. It follows the Singleton design pattern, allowing better granular control over the
 * creation of entities, especially Map.
 *
 * The setup sequence via this class is intended to be executed in the following manner, with a number of checks to
 * ensure so:
 *
 * [label:start setupPlayers(int n_players) -{@literal >} setupMap(int map_size) -{@literal >} setPlayerPositions()]
 * -{@literal >} allocateTeams(int n_teams)
 *
 * @author Xandru Mifsud
 */
public class Game {
    private static Game instance = null; // the singleton instance
    public boolean initialized = false;

    public Player[] players = null;
    public Team[] teams = null;
    public Map map = null;

    public String dir = null;
    public HTMLGenerator htmlGenerator = HTMLGenerator.getHTMLGenerator();

    /**
     * Private constructor to initialize an MainGame instance (if one does not already exist).
     */
    private Game(){ }

    /**
     * Returns an MainGame instance; in the case that an instance already exists, it returns the exisiting one.
     * Else it creates a new instance and returns it.
     * @return MainGame instance is the singleton to be returned.
     */
    public static Game getMainGame(){
        if(instance == null){
            instance = new Game();
        }
        return instance;
    }

    /**
     * Initializes n_players Player instances, if n_players is not less than 2 or greater then 8.
     * @param n_players is the number of players to be initialized.
     * @throws SetupOperationPrecedenceException is thrown if a Map instances has already been created.
     * @throws InvalidNumberOfPlayersException is thrown if the number of players is {@literal <} 2 or {@literal >} 8.
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
     * @throws InvalidMapSizeException is thrown if the map_size is invalid. [Criteria ii. {@literal &} iii. above]
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

    /**
     * Shuffling of the players array, to randomise turn sequence. For example, it is also used to randomise team allocation.
     * This is a variant of Fisher-Yates shuffle, known as Durstenfeld's Shuffle (as mentioned in Knuth's The Art of
     * Computer Programming), with the Java adaptation based on https://stackoverflow.com/a/1520212.
     */
    public void shufflePlayers(){
        Random rnd = ThreadLocalRandom.current();
        for (int i = players.length - 1; i > 0; i--){
            int j = rnd.nextInt(i + 1);

            // shuffling swap
            Player p = players[j];
            players[j] = players[i];
            players[i] = p;
        }
    }

    public void allocateTeams(int n_teams){
        teams = new Team[n_teams]; // initialize if within range
        shufflePlayers();

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

    /**
     * Convenience function for generating and persisting to disk the HTML map file for each player instance in players.
     * @throws IOException is thrown when there is a failure in persisting to disk [generally fatal].
     */
    public void writeHTMLFiles() throws IOException{
        if(map == null){ // if map is not set, throw a SetupOperationPrecedenceException
            throw new SetupOperationPrecedenceException("Cannot call generation of HTML file before map creation.");
        } // since players has precedence over map, map being initialized => players being initialized
        else if(dir == null){ // if directory not set, throw a SetupOperationPrecedenceException
            throw new SetupOperationPrecedenceException("Directory to write HTML files not specified.");
        }
        else{

            for(Player player : players){ // generate and persist to disk the HTML map for each player instance
                FileWriter writer = new FileWriter(dir + System.getProperty("file.separator") +
                                                   "player_" + player.get_pID() + "_map.html");

                // iterate through the html ArrayList and persist
                for (String ln : htmlGenerator.genPlayerMap(player, map)){
                    writer.write(ln);
                }
                writer.close();
            }
        }
    }
}