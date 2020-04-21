package com.xd.cps2002;

import java.util.Scanner;

public class Launcher{
    static MainGame mainGame;

    public static void main(String[] args){
        mainGame = MainGame.getMainGame();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome! Ready for a treasure hunt? The rules of the game are simple:\n\n" +

                           "Each player must use the U(p), D(down), L(eft), and R(ight) keys to move\n" +
                           "along the map. Each player gets one (valid) move per round. The first to\n" +
                           "find the treasure, wins! Beware however - land on a water tile, and you\n" +
                           "have to start all over again! Are you up to the challenge?\n" +
                           "-------------------------------------------------------------------------");

        while(true){
            System.out.print("Kindly enter the number of players between 2 and 8: ");

            while (!scanner.hasNextInt()) scanner.next();
            try{
                mainGame.setupPlayers(scanner.nextInt());
                break;
            }
            catch(InvalidNumberOfPlayersException ignored){ }
        }

        int min_map_size = (MainGame.players.length < 5) ? 5 : 8;
        while(true){
            System.out.print("Kindly enter a map size between " + min_map_size + " and 50: ");

            while (!scanner.hasNextInt()) scanner.next();
            try{
                mainGame.setupMap(scanner.nextInt());
                break;
            }
            catch(InvalidMapSizeException ignored){ }
        }

        mainGame.setPlayerPositions();
    }
}
