package com.xd.cps2002.map;

/** The {@code MapCreator} class is used to create different types of {@link Map} objects. This class is the
 * "creator" class used to implement the static factory design pattern.
 *
 * Using the static factory design pattern, the creation process of different types of maps is abstracted away from
 * other classes. Hence, this allows the code to support new map types without necessarily needing to change the code
 * where the {@link Map} sub-class is instantiated.
 *
 * Moreover, since the factory method used to create {@link Map} instances is {@code static}, the function can also
 * simultaneously restrict the number of map instances that can be created to a single instance. In effect, the class
 * also acts similar to a singleton, and creates and stores a {@link Map} object which is referred to throughout the
 * runtime of the program.
 *
 * The advantage of including the logic to limit the creation of new instances in the {@link MapCreator} rather than
 * {@link Map} is two-fold. First of all, the functionality to implement this restriction is only implemented once
 * rather than having to implement it for each and every concrete instance of the {@link Map} class. Secondly, since the
 * {@link Map} subclasses do not need to be singletons, they also become far more easy to test as a result.
 *
 * To only allow the {@link MapCreator} class to initialize concrete implementations of the {@link Map} class, the
 * subclasses' constructors need to have {@code protected} rather {@code private} access modifiers. However, this access
 * modifier is still flexible enough to allows unit tests in the same package as the classes to reinitialize {@link Map}
 * instances between unit tests.
 *
 * @author Domenico Agius
 */
public class MapCreator {

    /**
     * The {@code instance} member stores the Singleton instance of the map accessed by classes outside of the
     * {@link com.xd.cps2002.map} package.
     *
     * @implNote  Its access modifier is set as {@code default} instead of {@code private} to allow unit tests in the
     * same package to easily reset the instance between tests. However, classes outside the package still are not given
     * direct access to this member.
     */
     static Map instance;

    /** Factory method used to create different types of {@link Map} objects. Depending on the string passed to the
     * {@code mapType} parameter, the method can return the following map types:
     *
     * <ul>
     *     <li>"safe" - creates a map where 0-10% of the tiles are water tiles, and the player can start playing from
     *     at least 75% of the tiles </li>
     *     <li>"hazardous" - creates a map where 25-35% of the tiles are water tiles, and the player can start playing
     *     from at least 60% of the tiles </li>
     * </ul>
     *
     * @param mapType A string representing the type of map to be created.
     * @param size size of the map to be created.
     * @return A Map object with the type represented by {@code mapType}
     *
     * @apiNote This method creates a new {@link Map} instance only the first time it is called. This means that the
     * method is guaranteed to return a map of the requested type only the first time it is called.
     * */
    public static Map createMap(String mapType, int size) {
        // Change mapType to lowercase to avoid having case sensitivity
        mapType = mapType.toLowerCase();

        // If the singleton instance has not been created yet, create one according to the map type specified in the
        // parameter "mapType"
        if(instance == null) {
            switch (mapType) {
                case "safe": {
                    // Create a BasicMap with 0-10% water tiles where at least 75% of the tiles are playable
                    BasicMap basicMap = new BasicMap(size);
                    basicMap.setWaterTilePercentage(0, 10);
                    basicMap.setMinPlayableTilesPercentage(75);

                    // Store the basic map in the singleton instance
                    instance = basicMap;
                } break;
                case "hazardous": {
                    // Create a BasicMap with 25-35% water tiles where at least 60% of the tiles are playable
                    BasicMap basicMap = new BasicMap(size);
                    basicMap.setWaterTilePercentage(25, 35);
                    basicMap.setMinPlayableTilesPercentage(60);

                    // Store the basic map in the singleton instance
                    instance = basicMap;
                } break;

                // If the map type is invalid, throw an exception
                default:
                    throw new IllegalArgumentException("Invalid map type.");
            }

            // Keep regenerating the tiles of the map until it can be played by the players
            do {
                instance.generate();
            } while(!instance.isPlayable());
        }

        // Return the singleton instance
        return instance;
    }

    /** Factory method used to create different types of {@link Map} objects with a preset set of tiles (for testing).
     *  Depending on the string passed to the {@code mapType} parameter, the method can return the following map types:
     *
     * <ul>
     *     <li>"basic" - creates a {@link BasicMap} object.
     * </ul>
     *
     * @param mapType A string representing the type of map to be created.
     * @param tiles a 2D array of {@link TileType} elements which represents the placement of the tiles in the map
     * @return A Map object with the type represented by {@code mapType}
     *
     * @apiNote This method creates a new {@link Map} instance only the first time it is called. This means that the
     * method is guaranteed to return a map of the requested type only the first time it is called.
     *
     * @implNote Since this method does not randomly generate the map tiles, it could not be made to return "safe" and
     * "hazardous" map types. Hence, it only returns the "basic" map type.
     * */
    public static Map createMap(String mapType, TileType[][] tiles) {
        // Change mapType to lowercase to avoid having case sensitivity
        mapType = mapType.toLowerCase();

        // If the singleton instance has not been created yet, create one according to the map type specified in the
        // parameter "mapType"
        if(instance == null) {
            switch (mapType) {
                case "basic":
                    instance = new BasicMap(tiles); break;
                default:
                    throw new IllegalArgumentException("Invalid map type.");
            }
        }

        // Return the singleton instance
        return instance;
    }
}
