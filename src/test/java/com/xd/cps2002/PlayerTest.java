package com.xd.cps2002;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/*
Tester class for the Player class. Since the class mostly handles user input, it predominantly requires testing by
supplying I/O to the console streams. Hence during setup and teardown, these necessary arrangements are made.
 */
public class PlayerTest{
    // streams to get output
    private final ByteArrayOutputStream BAOutS = new ByteArrayOutputStream();

    // maintaining of original console output stream, to restore after each unit test
    private final PrintStream systemOut = System.out;

    // Player object to initialise on setup and de-reference on teardown
    private Player player;
    private String input = null;

    @Before
    public void setupPlayerTest(){
        // setting streams to emulate user input, and fetch the console output
        System.setOut(new PrintStream(BAOutS));

        player = new Player(); // initialising new player
    }

    // testing that player_id is correctly auto-incremented whenever a new Player instance is created
    @Test
    public void autoincID_Test(){
        Player player2 = new Player();
        // consistency check: id of previously initialised Player object does not change
        assertEquals(Player.get_global_player_count() - 2, player.get_pID());
        assertEquals(Player.get_global_player_count() - 1, player2.get_pID());

        Player player3 = new Player();
        // consistency check: id of previously initialised Player object does not change
        assertEquals(Player.get_global_player_count() - 3, player.get_pID());
        assertEquals(Player.get_global_player_count() - 2, player2.get_pID());
        assertEquals(Player.get_global_player_count() - 1, player3.get_pID());
    }

    // testing if user is correctly told that invalid input was entered when input is null, and that false is returned
    @Test
    public void move_NullInputTest(){
        input = null; // no input

        boolean ret = player.move(input);
        assertEquals("Invalid input entered.\n", BAOutS.toString());
        assertFalse(ret);
    }

    // testing if user is correctly told that invalid input was entered, and that false is returned
    @Test
    public void move_InvalidInputTest(){
        input = "up 2"; // first character is valid, but string length > 1 - should reject

        boolean ret = player.move(input);
        assertEquals("Invalid input entered.\n", BAOutS.toString());
        assertFalse(ret);
    }

    @After
    public void teardownPlayerTest(){
        // restore console output stream
        System.setOut(systemOut);

        player = null; // dereference
        input = null; // dereference
    }
}
