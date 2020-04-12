package com.xd.cps2002;

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
        player.setPosition(new Position(0,0));// setting to origin
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

    /* ---- This section is intended to test the failing scenarios of Player.move() ---- */

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

    // testing exception is thrown when Player Position position is null
    @Test(expected = Exception.class)
    public void move_NullPositionTest() throws Exception{
        player.setPosition(null);
        input = 'u'; // providing valid input, beyond scope of this test
        player.move(input);
    }

    /* ---- This section is intended to test the passing scenarios of Player.move() ----

    * Note that the Player is agnostic of the game map - hence negative coordinates are allowed. The client may then
    * handle these accordingly, eg. by wrap around (similar to Snake or Pac-Man)
    *
    * While being agnostic from the Map, it is assumed throughout this project that the (i,j) index of a map is the
    * (i,j) cartesian coordinate.*/

    // testing if the 'u' character results in a decrement in the y-axis while the x-axis remains fixed
    @Test
    public void move_UpPosCalcTest() throws Exception{
        input = 'u';

        Position p = player.move(input);
        assertEquals(0, p.x); // x remains at the origin
        assertEquals(-1, p.y); // y decreases in accordance to the cartesian coordinates
    }

    // testing if the 'd' character results in an increment in the y-axis while the x-axis remains fixed
    @Test
    public void move_DownPosCalcTest() throws Exception{
        input = 'd';

        Position p = player.move(input);
        assertEquals(0, p.x); // x remains at the origin
        assertEquals(1, p.y); // y increases in accordance to the cartesian coordinates
    }

    // testing if the 'l' character results in a decrease in the x-axis while the y-axis remains fixed
    @Test
    public void move_LeftPosCalcTest() throws Exception{
        input = 'l';

        Position p = player.move(input);
        assertEquals(-1, p.x); // x decreases in accordance to the cartesian coordinates
        assertEquals(0, p.y); // y remains at the origin
    }

    // testing if the 'r' character results in an increment in the x-axis while the y-axis remains fixed
    @Test
    public void move_RightPosCalcTest() throws Exception{
        input = 'r';

        Position p = player.move(input);
        assertEquals(1, p.x); // x increases in accordance to the cartesian coordinates
        assertEquals(0, p.y); // y remains at the origin
    }

    @After
    public void teardownPlayerTest(){
        player = null; // dereference
        input = '\0';
    }
}
