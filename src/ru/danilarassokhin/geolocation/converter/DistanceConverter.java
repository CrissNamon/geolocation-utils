package ru.danilarassokhin.geolocation.converter;

/**
 * Represents converter for distance
 * @author Danila Rassokhin
 */
public interface DistanceConverter {
    /**
     * Converts distance as specified
     * @param x Distance to convert
     * @return Converted distance
     */
    double convert(double x);
}
