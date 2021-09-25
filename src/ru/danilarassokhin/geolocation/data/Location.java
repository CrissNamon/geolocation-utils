package ru.danilarassokhin.geolocation.data;

import ru.danilarassokhin.geolocation.GeolocationUtils;

/**
 * Represents geographic location by latitude and longitude
 * @author Danila Rassokhin
 */
public class Location {

    private final double LATITUDE;
    private final double LONGITUDE;

    /**
     * Main constructor. Location MUST contain latitude and longitude
     */
    public Location(double latitude, double longitude) {
        this.LATITUDE = latitude;
        this.LONGITUDE = longitude;
    }

    /**
     * Converts current location to QTH locator
     * @param precision Locator precision. See {@link GeolocationUtils#latLonToQthLocator(double, double, int)}
     * @return QTH locator of current location
     */
    public final String toQthLocator(int precision) {
        return GeolocationUtils.latLonToQthLocator(
                this.LATITUDE,
                this.LONGITUDE,
                precision
        );
    }

    public double getLatitude() {
        return LATITUDE;
    }

    public double getLongitude() {
        return LONGITUDE;
    }

    @Override
    public String toString() {
        return LATITUDE + ", " + LONGITUDE;
    }

    @Override
    public int hashCode() {
        return toQthLocator(10).hashCode();
    }

    /**
     * Checks two objects for equality
     * <br>
     * 1) {@code obj} must not be null
     * <br>
     * 2) Objects classes must be the same
     * <br>
     * 3) Objects 10 symbols locators must be the same
     * @param obj Object to check equality
     * @return true if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Location testLocation = (Location) obj;
        return toQthLocator(10)
                .equals(
                        testLocation.toQthLocator(10)
                );
    }
}
