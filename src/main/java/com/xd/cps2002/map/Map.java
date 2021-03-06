package com.xd.cps2002.map;

import com.xd.cps2002.game.Game;
import com.xd.cps2002.player.Player;
import com.xd.cps2002.player.Position;

/**
 * The {@code Map} abstract class provides a common interface for all the different types of map types. This acts as the
 * abstract "product" class used to implement the factory design pattern for Maps.
 *
 * This class provides access to generic behaviour expected to be implemented by each map type. Hence, it allows client
 * code to avoid type casting after initializing a new map instance using the {@link MapCreator} factory.
 *
 * The {@link Map} class was implemented as an abstract class rather than an interface since the only functionality that
 * would be carried out differently by each subclass is the map generation and the way that that map is verified to be
 * playable. Hence, all other functionality was implemented in this class so that it would be common to all subclasses.
 *
 * @author Domenico Agius
 */
public abstract class Map {
    /**
     * The {@code size} member stores the dimension of the square map (size x size tiles).
     */
    protected int size;
    /**
     * The {@code tiles} member stores the actual types for each tile on the map that the player can walk on.
     */
    protected TileType[][] tiles;

    /**
     * The {@code treasurePos} member stores position of the treasure tile in the map.
     */
    protected Position treasurePos;

    /**
     * The {@code winnableTiles} member stores a 2D array of tiles from which a player can reach the treasure tile and
     * win the game. It is assumed that this array does not mark the treasure tile itself as winnable however, since a
     * player should not start directly on a treasure tile.
     *
     * The reason why the class does not implement a function to directly return start positions for the players is so
     * that the {@link Map} class can operate completely independently of the {@link Player} and {@link Game} classes.
     */
    protected boolean[][] winnableTiles;

    /**
     * Constructor used to initialize an empty {@code Map} object.
     * @param n size of the {@code n} x {@code n} square map
     * @throws IllegalArgumentException if the method is given an invalid size parameter (outside of the range 5-50)
     *
     * @implNote This constructor has a {@code protected} access modifier to stop client code from instantiating the
     * class directly. However, unit tests and subclasses can still call the constructor normally.
     */
    protected Map(int n)
    {
        // If map size is invalid (not between 5-50) throw an exception
        if(n < 5 || n > 50) {
            throw new IllegalArgumentException("BasicMap was initialized with an invalid size argument.");
        }

        // Otherwise set the map size as normal
        this.size=n;

        // Initialize the "tiles" and "reachableTiles" arrays to null
        tiles = null;
        winnableTiles = null;

        // Initialize the "treasurePos" to null
        treasurePos = null;
    }

    /**
     * Constructor used to initialize a {@code Map} object with a pre-generated set of tiles.
     * @param tiles a 2D array of {@link TileType} elements which represents the placement of the tiles in the map
     * @throws IllegalArgumentException if {@code tiles} is null, empty or if the lists in the 2D array do not have the
     * same lengths.
     *
     * @implNote This constructor has a {@code protected} access modifier to stop client code from instantiating the
     * class directly. However, unit tests and subclasses can still call the constructor normally.
     */
    protected Map(TileType[][] tiles) {
        // If the given 2D tile array is actually null, throw an exception
        if(tiles == null) {
            throw new IllegalArgumentException("The lists in the 2D array of tiles cannot be null.");
        }

        // If the given 2D tile array is empty, throw an exception
        if(tiles.length == 0) {
            throw new IllegalArgumentException("The lists in the 2D array of tiles cannot be empty.");
        }

        // If the lists in the 2D tile array do not have equal lengths (i.e. it does not have dimensions n x n) throw an
        // exception
        if(tiles.length != tiles[0].length) {
            throw new IllegalArgumentException("The lists in the 2D array of tiles must have equal lengths " +
                    "(they must form a square).");
        }

        // Set the size of the map to the size of the array
        size = tiles.length;

        // Count the number of treasure tiles in the map
        int treasureCount = 0;

        treasureSearch:
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(tiles[i][j] == null) {
                    // If the tile is null (not set) throw an error
                    throw new IllegalArgumentException("The 2D array of tiles cannot have tiles which are null.");
                }
                else if(tiles[i][j] == TileType.Treasure) {
                    // Increase "treasureCount" if a treasure tile is found
                    treasureCount++;

                    // If more than 1 treasure tile has been found already stop searching - ERROR
                    if(treasureCount > 1) break treasureSearch;
                    // If this is the 1st treasure tile that has been found, store its position
                    else treasurePos = new Position(i, j);
                }
            }
        }

        // If there is more or less than one treasure tile throw an exception
        if(treasureCount != 1) {
            throw new IllegalArgumentException("The 2D array of tiles must include 1 treasure tile.");
        }

        // Store the given 2D tile array in the "tiles" member
        this.tiles = tiles;

        // Initialize "winnableTiles" to null
        winnableTiles = null;
    }

    /**
     * Getter method used to get the {@code size} attribute of the {@code Map} object.
     * @return the size of the map
     */
    public int getSize() {
        return size;
    }

    /**
     * Method used to randomly generate the map. This method should be implemented by each subclass of the Map class to
     * allow different methods of map generation.
     *
     * @apiNote Note that this function should also set the {@link Map#treasurePos} member so that it can be used later
     * in the function {@link Map#isPlayable()}.
     */
    public abstract void generate();

    /**
     * Method used to check if the given map can be played by a player. This method should also be implemented by the
     * sub-class given that map generation is also handled there.
     * @return true if the player can reach the treasure starting from any point in the map.
     * @apiNote This function assumes that the {@link Map#treasurePos} member has been set previously using either the
     * {@link Map#Map(TileType[][])} constructor or the {@link Map#generate()} function.
     * @implNote It is assumed that this function will also set the member variable {@link Map#winnableTiles} while
     * checking that the map is playable. Moreover this was left to be implemented by the subclasses since it assumed
     * that given that they may generate maps differently, they may also need to traverse the maps differently as well.
     */
    public abstract boolean isPlayable();

    /**
     *  Used to check if starting from a particular position the player can reach the treasure tile by just using
     *  up/down/left/right movements.
     * @param pos The {@code Position} from which the treasure tile needs to be reached.
     * @return returns true if the treasure tile can be reached from the given map position and false otherwise.
     * @apiNote The function expects that the position actually exists in the map. It also expects that the function
     * {@link Map#isPlayable()} has been run before, so that the member {@link Map#winnableTiles} gets set beforehand.
     * @implNote Note that if the tile at {@code pos} is actually the treasure tile, the function returns false, since
     * the player should not be able to start from this tile.
     * @throws NullPointerException if given a null {@link Position} argument.
     * @throws IllegalArgumentException if given a {@link Position} which does not exist in the map.
     */
    public boolean isPositionWinnable(Position pos) {
        // Throw an exception if pos argument is null
        if(pos == null) {
            throw new NullPointerException("Given tile position cannot be null.");
        }

        // Throw an exception if pos is not a valid position in the map
        if(!isValidPosition(pos)) {
            throw new IllegalArgumentException("Given tile position is not valid.");
        }

        // If the "isPlayable" function has not been run yet and "winnableTiles" is still empty throw an exception
        if(winnableTiles == null) {
            throw new NullPointerException("The isPlayable function must be run before isPositionWinnable.");
        }

        // Check if the given position is winnable using the "winnableTiles" array
        return winnableTiles[pos.x][pos.y];
    }

    /**
     * Function used to check if the given coordinate is a valid position which exists in the map.
     * @param x x-coordinate in the map
     * @param y y-coordinate in the map
     * @return true if the given (x,y) coordinate is a valid tile position in the map and false otherwise.
     */
    public boolean isValidPosition(int x, int y) {
        // Check if the x and y coordinates are within the ranges of the "tiles" array (from 0 to size)
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    /**
     * Function used to check if the given tile {@link Position} exists in the map. This function was implemented as a
     * convenience function to allow use of the {@link Position} class.
     * @param pos position in the map to be validated
     * @return true if {@code pos} is a valid tile position in the map and false otherwise.
     * @implNote Note that this function returns false if the position is null. It does not throw an exception since
     * it would not make sense given that the function is used for validation of positions.
     */
    public boolean isValidPosition(Position pos) {
        // Return false if the position is null
        if(pos == null) {
            return false;
        }

        // Pass the x and y coordinates to the other version of the function
        return isValidPosition(pos.x, pos.y);
    }

    /**
     * Function used to check the type of the tile at the given coordinate in the map.
     * @param x x-coordinate in the map
     * @param y y-coordinate in the map
     * @apiNote The function indexes tiles in the map starting from (0,0), which is the tile in the upper left corner
     * of the map.
     * @return the type of the tile at position (x,y)
     * @throws NullPointerException if the Map's tiles have not been set or generated yet.
     * @throws IllegalArgumentException if given a {@link Position} which does not exist in the map.
     */
    public TileType getTileType(int x, int y) {
        // If the map tiles have not been generated yet, throw an exception
        if(tiles == null) {
            throw new NullPointerException("Map tiles have not been generated yet.");
        }

        // If the given position is invalid throw an exception
        if(!isValidPosition(x,y)) {
            throw new IllegalArgumentException("Given tile position is not valid.");
        }

        // Get the tile at the given position from the "tiles" 2D array
        return tiles[x][y];
    }

    /**
     * Function used to check the type of the tile at the given {@link Position} in the map. This function was
     * implemented as a convenience function to allow use of the {@link Position} class.
     * @param pos position of the tile of interest in the map.
     * @apiNote The function indexes tiles in the map starting from (0,0), which is the tile in the upper left corner
     * of the map.
     * @return the type of the tile at position {@code pos}
     * @throws NullPointerException if the argument {@code pos} is set to {@code null} or if the Map's tiles have not
     * been set or generated yet.
     * @throws IllegalArgumentException if given a {@link Position} which does not exist in the map.
     */
    public TileType getTileType(Position pos) {
        // Throw an exception if pos argument is null
        if(pos == null) {
            throw new NullPointerException("Given tile position cannot be null.");
        }

        // Pass the x and y coordinates to the other version of the function
        return getTileType(pos.x, pos.y);
    }
}
