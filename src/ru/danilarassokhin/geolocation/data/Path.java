package ru.danilarassokhin.geolocation.data;

import ru.danilarassokhin.geolocation.converter.DistanceConverter;
import ru.danilarassokhin.geolocation.exception.GeolocationException;

/**
 * Represents geolocation path
 * @author Danila Rassokhin
 */
public interface Path {

    /**
     * Adds location to the end of current path
     * @param location Location to add
     */
    void add(Location location);

    /**
     * Removes location in path by index
     * @param index Location index in path to remove
     */
    void remove(int index) throws GeolocationException;

    /**
     * Inserts location to current path
     * <br>If index greater than current path size behaves as {@link ru.danilarassokhin.geolocation.data.Path#add(Location)}
     * @param index Index to insert
     * @param location Location to insert
     */
    void insert(int index, Location location) throws GeolocationException;

    /**
     * Calculates current path distance in meters
     * @return Path distance
     */
    double getDistance();

    /**
     * Calculates current path distance and converts it with {@code distanceConverter}
     * @param distanceConverter Converter for distance
     * @return Path distance
     */
    double getDistance(DistanceConverter distanceConverter);

}
