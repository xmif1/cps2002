package com.xd.cps2002;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/*
Tester class for the Player class.
 */
public class PlayerTest{
    // Player object to initialise on setup and de-reference on teardown
    private Player player;
    private char input = '\0';

    @Before
    public void setupPlayerTest(){
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

    // testing exception is thrown when a char outside of the set {'u', 'd', 'r', 'l'} is passed
    @Test(expected = Exception.class)
    public void move_InvalidInputTest() throws Exception{
        input = 'i'; // character is invalid, since 'i' is not in {'u', 'd', 'r', 'l'}
        player.move(input);
    }

    // testing exception is thrown when the null char '\0' is passed
    @Test(expected = Exception.class)
    public void move_NullCharInputTest() throws Exception{
        input = '\0'; // character is invalid, since it is not in {'u', 'd', 'r', 'l'}
        player.move(input);
    }

    @After
    public void teardownPlayerTest(){
        player = null; // dereference
        input = '\0';
    }
}
