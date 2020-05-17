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
 * {@link Map} subclasses do not need to be singletons, they also become far more easy to test as a result. That being
 * said, to allow only the {@link MapCreator} class to initialize concrete implementations of the {@link Map} class, the
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
     * same package to easily reset the instance between tests. However, external methods still are not given direct
     * access to this member.
     */
     static Map instance;

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

            // Keep regenerating the tiles of the map until it can be played by the user
            do {
                instance.generate();
            } while(!instance.isPlayable());
        }

        // Return the singleton instance
        return instance;
    }
}
