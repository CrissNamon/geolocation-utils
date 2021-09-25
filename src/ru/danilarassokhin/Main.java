package ru.danilarassokhin;

import ru.danilarassokhin.geolocation.data.ListPath;
import ru.danilarassokhin.geolocation.data.Path;
import ru.danilarassokhin.geolocation.exception.GeolocationException;
import ru.danilarassokhin.geolocation.data.Location;
import ru.danilarassokhin.geolocation.converter.MetersToMilesDistanceConverter;
import ru.danilarassokhin.geolocation.GeolocationUtils;


public class Main {

    public static void main(String[] args) throws GeolocationException {
        Location loc1 = new Location(61.24233, 55.395747);
        Location loc2 = new Location(64.523494, 68.755122);
        System.out.println("LOCATION 1: " + loc1.toString());
        System.out.println("LOCATION 2: " + loc2.toString());
        System.out.println();

        String loc1Qth = loc1.toQthLocator(10);
        System.out.println("LOCATION 1 QTH LOCATOR: " + loc1Qth);
        String loc1QthPrecision = loc1.toQthLocator(4);
        System.out.println("LOCATION 1 QTH LOCATOR 4 SYMBOLS: " + loc1QthPrecision);
        System.out.println("LOCATION 1 QTH LOCATOR VALID: " + GeolocationUtils.isValidQthLocator(loc1Qth));
        Location qthToLocation = GeolocationUtils.qthLocatorToLatLon(loc1Qth);
        System.out.println("LOCATION -> QTH != QTH -> Location: " + loc1.toString() + " != " + qthToLocation.toString());
        System.out.println();

        System.out.println("LATITUDE, LONGITUDE TO QTH LOCATOR: " + GeolocationUtils.latLonToQthLocator(40, 0, 10));
        System.out.println();

        String qthLocator = "KN";
        System.out.println(qthLocator + " locator to location: " + GeolocationUtils.qthLocatorToLatLon(qthLocator));
        System.out.println();

        System.out.println("DISTANCE IN METERS: " + GeolocationUtils.distanceBetween(loc1, loc2));
        System.out.println("DISTANCE IN MILES: " + GeolocationUtils.distanceBetween(loc1, loc2, new MetersToMilesDistanceConverter()));
        System.out.println("DISTANCE IN KILOMETERS: " + GeolocationUtils.distanceBetween(loc1, loc2, (d) -> d / 1000));
        System.out.println();

        Path listPath = new ListPath();
        listPath.add(loc1);
        listPath.add(loc2);
        System.out.println("PATH DISTANCE: " + listPath.getDistance());
        System.out.println("GET PATH DISTANCE FROM CACHE: " + listPath.getDistance());
        listPath.add(new Location(1, 0));
        System.out.println("LOCATION ADDED. CALCULATE NEW PATH DISTANCE: " + listPath.getDistance());
    }
}
