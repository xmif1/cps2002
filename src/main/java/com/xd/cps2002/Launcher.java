package com.xd.cps2002;

import com.xd.cps2002.game.Game;
import com.xd.cps2002.game.game_exceptions.*;
import com.xd.cps2002.player.*;
import com.xd.cps2002.player.player_exceptions.MoveException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Launches a maze-game session, defines the runtime routine.
 *
 * @author Xandru Mifsud
 */
public class Launcher{

    public static void main(String[] args){
        Game game = Game.getMainGame(); // get instance
        initialiseGame(game); // setup game
        ArrayList<Integer> winners = startGame(game); // start game

        // print all the winners, and then exit
        System.out.println("Congratulations to the following winners!");
        for(int p_id : winners){
            System.out.println("Player #" + p_id);
        }

        System.out.println("Thank you for playing...bye bye!\nExiting...");
        System.exit(0);
    }

    /**
     * The following is an initialisation sequence, obeying the precedence rules described above. If implementing one's
     * own initialisation sequence, the following may serve as a template and guide in doing so.
     *
     * The function consists of minimal conditional logic constructs, and is mainly composed of looping constructs. In
     * this manner, all logic has been encapsulated within the MainGame methods called: setupPlayers, setupMap,
     * setHTMLDirectory, setPlayerPositions, and allocateTeams. All of which have been extensively tested.
     *
     * For this reason, and since this methods primarily handles input by means of the Scanner class, no testing is
     * required beyond that of mocking, which is beyond the scope of this assignment specification.
     *
     * Invalid input is handled by an extensive collection of custom exceptions. These exceptions carry out any necessary
     * handling to return the game to a correct state. When caught, they provide the programmer with the opportunity to
     * take further action, eg. by re-prompting for input.
     *
     * @param game is an instance of (singleton) Game which maintains the game state variables and updates them.
     */
    public static void initialiseGame(Game game){
        boolean team_mode = false;
        int n_players;
        int n_teams;

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
                n_players = scanner.nextInt();
                n_teams = n_players;

                game.setPlayers(n_players);
                break; // if InvalidNumberOfPlayersException is not thrown, break
            }
            catch(InvalidNumberOfPlayersException ignored){ }
        }

        scanner = new Scanner(System.in); // resetting scanner to forcefully clear buffer

        // repeatedly ask for integer input for the map size, until valid input is provided
        while(true){
            System.out.print("Kindly enter a map size between 5 and 50, or 8 and 50 if more than 4 players: ");

            while(!scanner.hasNextInt()){
                System.out.print("Kindly enter a map size between 5 and 50, or 8 and 50 if more than 4 players: ");
                scanner.next();
            }

            try{
                game.setMap(scanner.nextInt(), game.getPlayers());
                break; // if InvalidMapSizeException is not thrown, break
            }
            catch(InvalidMapSizeException ignored){ }
        }

        scanner = new Scanner(System.in); // resetting scanner to forcefully clear buffer

        // repeatedly ask for string input for the path to write the HTML files to, until valid input is provided
        while(true){
            System.out.print("Kindly enter a directory path in which to write the HTML files: ");

            try{
                game.setHTMLDirectory(scanner.next());
                break; // if IOException is not thrown, break
            }
            catch(IOException ignored){
                System.out.println("Invalid file path specified.");
            }
        }

        game.setPlayerPositions(game.getPlayers(), game.getMap()); // initialize the player positions

        scanner = new Scanner(System.in); // resetting scanner to forcefully clear buffer

        char in_char;
        boolean valid_mode = false;

        // repeatedly ask if playing in team mode or not
        while(!valid_mode){
            System.out.print("Do you wish to play in team mode, Y(es) or N(o)? : ");

            in_char = scanner.next().charAt(0);

            switch(Character.toLowerCase(in_char)){ // test against cases to carry out necessary logic
                case 'n': team_mode = false; valid_mode = true; break;
                case 'y': team_mode = true; valid_mode = true; break;
            }
        }

        scanner = new Scanner(System.in); // resetting scanner to forcefully clear buffer

        // repeatedly ask for integer input for the number of teams, until valid input is provided
        while(team_mode){
            System.out.print("Kindly enter the number of teams between 2 and " + (n_players - 1) + " : ");

            while(!scanner.hasNextInt()){
                System.out.print("Kindly enter the number of teams between 2 and " + (n_players - 1) + " : ");
                scanner.next();
            }

            n_teams = scanner.nextInt();
            if(2 <= n_teams && n_teams < n_players) {
                break;
            }
            System.out.println("Invalid number of teams entered.");
        }

        game.allocateTeams(n_teams, game.getPlayers());
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
     * @param game is an instance of (singleton) Game which maintains the game state variables and updates them.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to getMoves() before a call
     * to initializeGame()
     */
    public static void getMoves(Game game){
        if(!game.isInitialised()){
            throw new SetupOperationPrecedenceException("Attempted call to getMoves() before initializeGame().");
        }
        else {
            System.out.println("------------------------------------------------------------------------\n");

            for(Player player : game.getPlayers()) {
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
                        if(game.getMap().isValidPosition(new_position)) {
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
     * @param game is an instance of (singleton) Game which maintains the game state variables and updates them.
     * @return ArrayList of type Integer, of the winners if any, containing their unique identifier.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to updateGameState() before a
     * call to initializeGame()
     */
    public static ArrayList<Integer> updateGameState(Game game){
        if(!game.isInitialised()){
            throw new SetupOperationPrecedenceException("Attempted call to updateGameState() when game variables have not" +
                    " been initialized.");
        }
        else {
            ArrayList<Integer> winners = new ArrayList<>();

            for(Player player : game.getPlayers()){
                PlayerStatus status = game.getMap().getTileType(player.getPosition()).statusAfterMove; // get status of player

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
     * @param game is an instance of (singleton) Game which maintains the game state variables and updates them.
     * @return ArrayList of type Integer, of the winners if any, containing their unique identifier.
     * @throws SetupOperationPrecedenceException is thrown when there is an attempted call to startGame() before a call
     * to initializeGame()
     */
    public static ArrayList<Integer> startGame(Game game){
        if(!game.isInitialised()){
            throw new SetupOperationPrecedenceException("Attempted call to startGame() before initializeGame().");
        }
        else{
            ArrayList<Integer> winners = new ArrayList<>();

            // this is the main game sequence
            while(winners.size() == 0){ // loop until a player lands on the Treasure tile
                // generate current maps
                try{
                    game.writeHTMLFiles(game.getPlayers(), game.getMap());
                }
                // if persistence to disk fails, this is generally a fatal error beyond the scope of the program
                catch(IOException ioe){
                    ioe.printStackTrace();

                    System.out.println("Fatal error occurred during file persistence. Sorry! Exiting...");
                    System.exit(1);
                }

                getMoves(game); // ask players to play their respective moves
                winners = updateGameState(game); // then update the game state variables accordingly
            }

            return winners;
        }
    }
}