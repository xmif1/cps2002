package com.xd.cps2002.map;

/** The {@code MapCreator} class is used to create different types of {@link Map} objects. This class is the
 * "creator" class used to implement the factory design pattern.
 *
 * @author Domenico Agius
 */
public class MapCreator {

    /**
     * The {@code instance} member stores the Singleton instance of the map accessed by classes outside of the
     * {@link com.xd.cps2002.map} package.
     *
     * @implNote  This is declared as {@code protected} instead of {@code private} to allow unit tests in the same
     * package to easily reset the instance between tests. However, external methods still are not given direct access
     * to this member.
     */
    protected static Map instance;

    /** Factory method used to create different types of {@link Map} objects.
     * @param mapType A string representing the type of map to be created.
     * @param size size of the map to be created
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
                case "basic":
                    instance = new BasicMap(size); break;
                default:
                    throw new IllegalArgumentException("Invalid map type.");
            }
        }

        // Return the singleton instance
        return instance;
    }
}
