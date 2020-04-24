package com.xd.cps2002;

import com.xd.cps2002.game.MainGame;

import java.util.ArrayList;

/**
 * Launches a maze-game session, defines the runtime routine.
 *
 * @author Xandru Mifsud
 */
public class Launcher{
    static MainGame mainGame; // hold singleton instance

    public static void main(String[] args){
        mainGame = MainGame.getMainGame(); // get instance
        mainGame.initializeGame(); // setup game
        ArrayList<Integer> winners = mainGame.startGame(); // start game

        // print all the winners, and then exit
        System.out.println("Congratulations to the following winners!");
        for(int p_id : winners){
            System.out.println("Player #" + p_id);
        }

        System.out.println("Thank you for playing...bye bye!\nExiting...");
        System.exit(0);
    }
}