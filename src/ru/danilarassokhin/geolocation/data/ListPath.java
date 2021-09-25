package ru.danilarassokhin.geolocation.data;

import ru.danilarassokhin.geolocation.GeolocationUtils;
import ru.danilarassokhin.geolocation.converter.DistanceConverter;
import ru.danilarassokhin.geolocation.exception.GeolocationException;

import java.util.ArrayList;
import java.util.List;

public class ListPath implements Path {

    private final List<Location> path;
    private double distance;
    private boolean isDirty;

    public ListPath() {
        this.path = new ArrayList<>();
        this.distance = Double.NaN;
        markDirty();
    }

    public ListPath(List<Location> locations) {
        this.path = new ArrayList<>();
        this.path.addAll(locations);
        this.distance = Double.NaN;
        markDirty();
    }

    @Override
    public void add(Location location) {
        path.add(location);
        markDirty();
    }

    @Override
    public void remove(int index) throws GeolocationException{
        if(index < 0) {
            throw new GeolocationException("Index must be greater than zero!");
        }
        path.remove(index);
        markDirty();
    }

    @Override
    public void insert(int index, Location location) throws GeolocationException {
        if(index < 0) {
            throw new GeolocationException("Index must be greater than zero!");
        }
        if(index > path.size()) {
            add(location);
        }else{
            path.set(index, location);
            markDirty();
        }
    }

    @Override
    public double getDistance() {
        if(isDirty) {
            distance = 0;
            for (int i = 0; i < path.size() - 1; ++i) {
                distance += GeolocationUtils.distanceBetween(
                        path.get(i),
                        path.get(i + 1)
                );
            }
            markClean();
        }
        return distance;
    }

    @Override
    public double getDistance(DistanceConverter distanceConverter) {
        return distanceConverter.convert(
                getDistance()
        );
    }

    private void markDirty() {
        this.isDirty = true;
    }

    private void markClean() {
        this.isDirty = false;
    }
}
