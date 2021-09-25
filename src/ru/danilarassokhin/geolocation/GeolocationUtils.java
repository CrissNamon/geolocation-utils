package ru.danilarassokhin.geolocation;

import ru.danilarassokhin.geolocation.converter.DistanceConverter;
import ru.danilarassokhin.geolocation.data.Location;
import ru.danilarassokhin.geolocation.exception.GeolocationException;

import static java.lang.Math.floor;

/**
 * Contains utils for geolocation data
 * @author Danila Rassokhin
 */
public class GeolocationUtils {

    /**
     * Earth radius
     */
    static final double EARTH_RADIUS = 6371E3;

    /**
     * EPSILON for QTH locator calculation
     */
    static final double EPS = 1E-7;

    /**
     * Converts location latitude and longitude to QTH locator
     * <br>
     * Explanation: https://en.wikipedia.org/wiki/Maidenhead_Locator_System
     * @param latitude location latitude
     * @param longitude location longitude
     * @param precision symbols count in locator, must be even and in [2-10] range
     * @return QTH locator of {@code precision} symbols
     */
    public static String latLonToQthLocator(double latitude, double longitude, int precision) {
        StringBuilder result = new StringBuilder();
        double t = latitude + 90;
        double o = longitude + 180;

        t = t / 10 + EPS;
        o = o / 20 + EPS;
        result.append(
                charFromDouble(o + 65)
        ).append(
                charFromDouble(65 + t)
        );

        if(precision >= 4) {
            t = 10 * (t - floor(t));
            o = 10 * (o - floor(o));
            result.append(
                    charFromDouble(48 + o)
            ).append(
                    charFromDouble(48 + t)
            );
        }

        if(precision >= 6) {
            t = 24 * (t - floor(t));
            o = 24 * (o - floor(o));
            result.append(
                    charFromDouble(65 + o)
            ).append(
                    charFromDouble(65 + t)
            );
        }

        if(precision >= 8) {
            t = 10 * (t - floor(t));
            o = 10 * (o - floor(o));
            result.append(
                    charFromDouble(48 + o)
            ).append(
                    charFromDouble(48 + t)
            );
        }

        if(precision >= 10) {
            t = 24 * (t - floor(t));
            o = 24 * (o - floor(o));
            result.append(
                    charFromDouble(65 + o)
            ).append(
                    charFromDouble(65 + t)
            );
        }

        return result.toString();
    }

    /**
     * Converts location latitude and longitude to QTH locator
     * <br>
     * Explanation: https://en.wikipedia.org/wiki/Maidenhead_Locator_System
     * @param latitude location latitude
     * @param longitude location longitude
     * @return QTH locator of 10 symbols
     */
    public static String latLonToExactQthLocator(double latitude, double longitude) {
        return latLonToQthLocator(latitude, longitude, 10);
    }

    /**
     * Converts QTH locator back to latitude, longitude location.
     * <br>Be careful: Location -> QTH != QTH -> Location
     * @param qthLocator QTH locator to convert
     * @return {@link Location} with latitude and longitude
     * @throws GeolocationException if QTH locator is not valid. See {@link GeolocationUtils#isValidQthLocator(String)}
     */
    public static Location qthLocatorToLatLon(String qthLocator) throws GeolocationException {
        if (!isValidQthLocator(qthLocator)) {
            throw new GeolocationException("Wrong QTH locator!");
        }

        String t = qthLocator.toUpperCase();
        double[] e = new double[10];

        if (qthLocator.length() == 2) {
            t += "55LL55LL";
        }
        if (qthLocator.length() == 4) {
            t += "LL55LL";
        }
        if (qthLocator.length() == 6) {
            t += "55LL";
        }
        if (qthLocator.length() == 8) {
            t += "LL";
        }

        for (int o = 0; o < qthLocator.length(); ++o) {
            e[o] = t.charAt(o) - 65;
        }

        e[2] += 17;
        e[3] += 17;
        e[6] += 17;
        e[7] += 17;

        double n = 20 * e[0] + 2 * e[2] + e[4] / 12 + e[6] / 120 + e[8] / 2880 - 180;
        double a = 10 * e[1] + e[3] + e[5] / 24 + e[7] / 240 + e[9] / 5760 - 90;
        return new Location(a, n);
    }

    /**
     * Checks QTH locator for validity
     * <br>
     * 1) Locator must not be null
     * <br>
     * 2) Locator length must be in range [2-10] and must be even
     * <br>
     * 3) Locator contains alternating groups of 2 alphabetic and 2 symbols. Such as "ZZ00YY..."
     * @param qthLocator QTH locator to check
     * @return true if locator is valid
     */
    public static boolean isValidQthLocator(String qthLocator) {
        if(qthLocator == null) {
            return false;
        }
        boolean lengthCondition = qthLocator.length() > 1 && qthLocator.length() < 11 && qthLocator.length() % 2 == 0;
        if (!lengthCondition) {
            return false;
        }
        boolean containsCondition = true;
        for(int i = 0, r = 1; i < qthLocator.length() && containsCondition; i += 2, ++r) {
            if(r % 2 != 0) {
                containsCondition = isUpperCaseLetter(
                        qthLocator.charAt(i)
                ) && isUpperCaseLetter(
                        qthLocator.charAt(i + 1)
                );
            }else{
                containsCondition = isDigit(
                        qthLocator.charAt(i)
                ) && isDigit(
                        qthLocator.charAt(i + 1)
                );
            }
        }
        return containsCondition;
    }

    /**
     * Calculates distance between two locations
     * @param location1 Start location
     * @param location2 End location
     * @return Distance in meters
     */
    public static double distanceBetween(Location location1, Location location2) {
        double radians = Math.PI / 180;
        double phi1 = location1.getLatitude() * radians;
        double phi2 = location2.getLatitude() * radians;
        double halfDeltaPhi = (location2.getLatitude() - location1.getLatitude()) * radians / 2;
        double halfDeltaLambda = (location2.getLongitude() - location2.getLongitude()) * radians / 2;

        double a = Math.sin(halfDeltaPhi) * Math.sin(halfDeltaPhi)
                + Math.cos(phi1) * Math.cos(phi2)
                * Math.sin(halfDeltaLambda) * Math.sin(halfDeltaLambda);
        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );
        return EARTH_RADIUS * c;
    }

    /**
     * Calculates distance between two locations and converts it to another measure. See {@link DistanceConverter}
     * @param location1 Start location
     * @param location2 End location
     * @return Distance converted from meters by {@code distanceConverter}
     */
    public static double distanceBetween(Location location1, Location location2, DistanceConverter distanceConverter) {
        return distanceConverter.convert(
                distanceBetween(location1, location2)
        );
    }

    private static char charFromDouble(double d) {
        return (char)(d);
    }

    private static boolean isUpperCaseLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }

    private static boolean isLowerCaseLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    private static boolean isLetter(char c) {
        return isUpperCaseLetter(c) || isLowerCaseLetter(c);
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

}
