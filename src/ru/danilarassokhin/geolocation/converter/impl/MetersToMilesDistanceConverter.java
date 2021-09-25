package ru.danilarassokhin.geolocation.converter.impl;

import ru.danilarassokhin.geolocation.converter.DistanceConverter;

/**
 * Represents meters to miles distance converter {@link DistanceConverter}
 * @author Danila Rassokhin
 */
public class MetersToMilesDistanceConverter implements DistanceConverter{
    @Override
    public double convert(double x) {
        return x / 1609;
    }
}
