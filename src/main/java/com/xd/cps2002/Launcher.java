package com.xd.cps2002;

import java.io.IOException;
import java.util.Scanner;

/**
 * Launches a maze-game session, defines the runtime routine.
 */
public class Launcher{
    static MainGame mainGame; // hold singleton instance

    public static void main(String[] args){
        mainGame = MainGame.getMainGame(); // get instance
        boolean win = false; // will be true when a player lands on a Treasure tile

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

            while (!scanner.hasNextInt()) scanner.next();
            try{
                mainGame.setupPlayers(scanner.nextInt());
                break; // if InvalidNumberOfPlayersException is not thrown, break
            }
            catch(InvalidNumberOfPlayersException ignored){ }
        }

        // determine the minimum valid map size
        int min_map_size = (mainGame.players.length < 5) ? 5 : 8;

        // repeatedly ask for integer input for the map size, until valid input is provided
        while(true){
            System.out.print("Kindly enter a map size between " + min_map_size + " and 50: ");

            while (!scanner.hasNextInt()) scanner.next();
            try{
                mainGame.setupMap(scanner.nextInt());
                break; // if InvalidMapSizeException is not thrown, break
            }
            catch(InvalidMapSizeException ignored){ }
        }

        // repeatedly ask for string input for the path to write the HTML files to, until valid input is provided
        while(true){
            System.out.print("Kindly enter a directory path in which to write the HTML files: ");

            try{
                mainGame.setHTMLDirectory(scanner.next());
                break; // if IOException is not thrown, break
            }
            catch(IOException ignored){ }
        }

        mainGame.setPlayerPositions(); // initialize the player positions

        // this is the main game sequence
        while(!win){ // loop until a player lands on the Treasure tile
            // generate current maps
            try{
                mainGame.writeHTMLFiles();
            }
            // if persistence to disk fails, this is generally a fatal error beyond the scope of the program
            catch(IOException ioe){
                ioe.printStackTrace();

                System.out.println("Fatal error occurred during file persistence. Sorry! Exiting...");
                System.exit(1);
            }

            System.out.println("------------------------------------------------------------------------\n");
            for(Player player : mainGame.players){ // for each player
                System.out.println("Player #" + player.get_pID() + ", it's your turn!\n");

                Position new_position;

                // repeatedly ask for input until the move specified is valid
                while(true){
                    System.out.print("Do you wish to move U(p), D(own), L(eft), or R(ight)? : ");

                    try{
                        // if MoveException is thrown, then the character input is invalid
                        new_position = player.move(scanner.next().charAt(0));

                        // if character input is valid and move within map boundary
                        if(mainGame.map.isValidPosition(new_position)){
                            player.setPosition(new_position); // set the position and break
                            break;
                        }
                        else{ // else if character input is valid but the move is outside the map boundary, loop again
                            System.err.println("Invalid input provided. The move is outside of the map boundary.");
                        }
                    }
                    catch(MoveException ignored){ }
                }

                PlayerStatus status = mainGame.map.getTileType(new_position).statusAfterMove; // get status of player

                if(status.equals(PlayerStatus.Death)){ // if dead, reset player
                    System.out.println("Better be careful, or you'll drown!");
                    player.reset();
                }
                else if(status.equals(PlayerStatus.Win)){ // else if won, break outside while loop by setting win = true
                    System.out.println("Ding ding ding! We have a winner!");
                    win = true;
                }

                System.out.println("------------------------------------------------------------------------\n");
            }
        }

        System.out.println("Thank you for playing...bye bye!\nExiting...");
        System.exit(0);
    }
}