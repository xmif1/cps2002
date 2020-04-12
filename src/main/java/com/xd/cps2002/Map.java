package com.xd.cps2002;

public interface Map {
    // Method which returns a string representing the type of the map (used for the map factory)
    String getMapType();

    // Setter used to set the dimensions of the map
    // Parameters:
    // - n : dimension of the map (in map tiles)
    // Returns: true if the map size is valid and false otherwise.
    boolean setMapSize(int n);

    // Method used to randomly generate the map
    void generate();

    // Function used to check if the given coordinate is a valid position in the map
    // Parameters:
    // - x : x-coordinate in the map
    // - y : y-coordinate in the map
    // Returns: true if the given (x,y) coordinate is a valid tile position in the map and false otherwise.
    boolean isValidPosition(int x, int y);

    // Function used to check the type of the tile at the given coordinate in the map
    // Parameters:
    // - x : x-coordinate in the map
    // - y : y-coordinate in the map
    // Returns: the type of the tile at position (x,y)
    TileType getTileType(int x, int y);
}
