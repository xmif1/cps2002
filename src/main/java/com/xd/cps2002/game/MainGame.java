package com.xd.cps2002.game;

import com.xd.cps2002.game.game_exceptions.InvalidMapSizeException;
import com.xd.cps2002.game.game_exceptions.InvalidNumberOfPlayersException;
import com.xd.cps2002.game.game_exceptions.SetupOperationPrecedenceException;
import com.xd.cps2002.map.Map;
import com.xd.cps2002.map.MapCreator;
import com.xd.cps2002.player.Player;
import com.xd.cps2002.player.PlayerStatus;
import com.xd.cps2002.player.Position;
import com.xd.cps2002.player.Team;
import com.xd.cps2002.player.player_exceptions.MoveException;
import com.xd.cps2002.player.player_exceptions.TeamOverrideException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Scanner;

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
 * [label:start setupPlayers(int n_players) -{@literal >} setupMap(int map_size) -{@literal >} setPlayerPositions()]
 * |-{@literal >} reset() goto:start
 *
 * @author Xandru Mifsud
 */
public class MainGame{
    private static MainGame instance = null; // the singleton instance
    public boolean initialized = false;

    public Player[] players = null;
    public Team[] teams = null;
    public Map map = null;

    public String dir = null;
    public HTMLGenerator htmlGenerator = HTMLGenerator.getHTMLGenerator();

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
        dir = null;
        initialized = false;
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
            teams = new Team[n_players]; // initialize if within range

            for(int i = 0; i < n_players; i++){
                players[i] = new Player();

                // TEMPORARY
                teams[i] = new Team();
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

    // TEMPORARY
    public void allocateTeams(){
        for(int i = 0; i < players.length; i++) {
            try {
                teams[i].join(players[i]);
            }catch(TeamOverrideException toe) {
                throw new SetupOperationPrecedenceException("Invalid attempt to join a team when already has been allocated" +
                        " to a team");
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

    /**
     * The following is an initialization sequence, obeying the precedence rules described above. If implementing one's
     * own initialization sequence, the following may serve as a template and guide in doing so.
     *
     * The function consists of minimal conditional logic constructs, and is mainly composed of looping constructs. In
     * this manner, all logic has been encapsulated within the MainGame methods called: setupPlayers, setupMap,
     * setHTMLDirectory, and setPlayerPositions. All of which have been extensively tested.
     *
     * For this reason, and since this methods primarily handles input by means of the Scanner class, no testing is
     * required beyond that of mocking, which is beyond the scope of this assignment specification.
     *
     * Invalid input is handled by an extensive collection of custom exceptions. These exceptions carry out any necessary
     * handling to return the game to a correct state. When caught, they provide the programmer with the opportunity to
     * take further action, eg. by re-prompting for input.
     */
    public void initializeGame(){
        Scanner scanner = new Scanner(System.in); // for input by user

        // print a welcome message
        System.out.println("Welcome! Ready for a treasure hunt? The rules of the game are simple:\n\n" +

                           "Each player must use the U(p), D(down), L(eft), and R(ight) keys to move\n" +
                           "along the map. Each player gets one (valid) move per round. The first to\n" +
                           "find the treasure, wins! Beware however - land on a water tile, and you\n" +
                           "have to start all over again! Are you up to the challenge?\n" +
                           "-------------------------------------------------------------------------");

        // repeatedly ask for integer input for the number of players, until valid input is provided
        while(true){
            System.out.print("Kindly enter the number of players between 2 and 8: ");

            while(!scanner.hasNextInt()){
                System.out.print("Kindly enter the number of players between 2 and 8: ");
                scanner.next();
            }

            try{
                setupPlayers(scanner.nextInt());
                break; // if InvalidNumberOfPlayersException is not thrown, break
            }
            catch(InvalidNumberOfPlayersException ignored){ }
        }

        scanner = new Scanner(System.in); // resetting scanner to forcefully clear buffer

        // determine the minimum valid map size
        int min_map_size = (players.length < 5) ? 5 : 8;

        // repeatedly ask for integer input for the map size, until valid input is provided
        while(true){
            System.out.print("Kindly enter a map size between " + min_map_size + " and 50: ");

            while(!scanner.hasNextInt()){
                System.out.print("Kindly enter a map size between " + min_map_size + " and 50: ");
                scanner.next();
            }

            try{
                setupMap(scanner.nextInt());
                break; // if InvalidMapSizeException is not thrown, break
            }
            catch(InvalidMapSizeException ignored){ }
        }

        scanner = new Scanner(System.in); // resetting scanner to forcefully clear buffer

        // repeatedly ask for string input for the path to write the HTML files to, until valid input is provided
        while(true){
            System.out.print("Kindly enter a directory path in which to write the HTML files: ");

            try{
                setHTMLDirectory(scanner.next());
                break; // if IOException is not thrown, break
            }
            catch(IOException ignored){
                System.out.println("Invalid file path specified.");
            }
        }

        setPlayerPositions(); // initialize the player positions

        // TEMPORARY
        allocateTeams();

        initialized = true; // allows startGame() to be called successfully;
    }

    /* --------------------------------------------- Game Play Sequence ----------------------------------------------
     * The following is a sequence of functions which define the actual game play. If implementing one's own game play
     * sequence, the following may serve as a template and guide in doing so.
     * ---------------------------------------------------------------------------------------------------------------
     */

    /**
     * Asks for input from each and every single player, until all enter a valid move meeting a number of criteria:
     * (i) The move results in a position within the boundary of the map.
     * (ii) The textual input is valid, i.e. any one of the characters 'u', 'd', 'l' or 'r'.
     *
     * The function consists of minimal conditional logic constructs, and is mainly composed of looping constructs. In
     * this manner, almost all logic has been encapsulated within Player.move(), Map.isValid() and Player.setPosition().
     * All of which have been extensively tested.
     *
     * For this reason, and since this methods primarily handles input by means of the Scanner class, no testing is
     * required beyond that of mocking, which is beyond the scope of this assignment specification.
     *
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to getMoves() before a call
     * to initializeGame()
     */
    public void getMoves(){
        if(!initialized){
            throw new SetupOperationPrecedenceException("Attempted call to getMoves() before initializeGame().");
        }
        else {
            System.out.println("------------------------------------------------------------------------\n");

            for (Player player : players) {
                Scanner scanner = new Scanner(System.in); // resetting scanner to forcefully clear buffer

                System.out.println("Player #" + player.get_pID() + ", it's your turn!\n");

                Position new_position;

                // repeatedly ask for input until the move specified is valid
                while (true) {
                    System.out.print("Do you wish to move U(p), D(own), L(eft), or R(ight)? : ");

                    try {
                        // if MoveException is thrown, then the character input is invalid
                        new_position = player.move(scanner.next().charAt(0));

                        // if character input is valid and move within map boundary
                        if (map.isValidPosition(new_position)) {
                            player.setPosition(new_position); // set the position and break
                            break;
                        } else { // else if character input is valid but the move is outside the map boundary, loop again
                            System.out.println("Invalid input provided. The move is outside of the map boundary.");
                        }
                    } catch (MoveException ignored) {
                    }
                }

                System.out.println("------------------------------------------------------------------------\n");
            }
        }
    }

    /**
     * The following is a simple function to update the state of the game variables, intended to be called after each
     * move. It is responsible for taking the necessary action when a player either dies by landing on a water tile,
     * or wins by landing on the treasure tile.
     *
     * @return ArrayList of type Integer, of the winners if any, containing their unique identifier.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to updateGameState() before a
     * call to initializeGame()
     */
    public ArrayList<Integer> updateGameState(){
        if(!initialized){
            throw new SetupOperationPrecedenceException("Attempted call to updateGameState() before initializeGame().");
        }
        else {
            ArrayList<Integer> winners = new ArrayList<Integer>();

            for(Player player : players){
                PlayerStatus status = map.getTileType(player.getPosition()).statusAfterMove; // get status of player

                if (status.equals(PlayerStatus.Death)) { // if dead, reset player
                    System.out.println("\n\u001B[34m" + "Better be careful, or you'll drown!" + "\u001B[0m");
                    player.reset();
                } else if (status.equals(PlayerStatus.Win)) { // else if won, break outside while loop by setting win = true
                    winners.add(player.get_pID());
                }
            }

            return winners;
        }
    }

    /**
     * Define the main game game sequence, through a number of calls to getMoves() and updateGameState(), as well as
     * writeHTMLFiles().
     *
     * @return ArrayList of type Integer, of the winners if any, containing their unique identifier.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to startGame() before a call
     * to initializeGame()
     */
    public ArrayList<Integer> startGame(){
        if(!initialized){
            throw new SetupOperationPrecedenceException("Attempted call to startGame() before initializeGame().");
        }
        else{
            ArrayList<Integer> winners = new ArrayList<Integer>();

            // this is the main game sequence
            while(winners.size() == 0){ // loop until a player lands on the Treasure tile
                // generate current maps
                try{
                    writeHTMLFiles();
                }
                // if persistence to disk fails, this is generally a fatal error beyond the scope of the program
                catch(IOException ioe){
                    ioe.printStackTrace();

                    System.out.println("Fatal error occurred during file persistence. Sorry! Exiting...");
                    System.exit(1);
                }

                getMoves(); // ask players to play their respective moves
                winners = updateGameState(); // then update the game state variables accordingly
            }

            return winners;
        }
    }
}