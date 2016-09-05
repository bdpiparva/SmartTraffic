package com.thoughtworks.smarttraffic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salonivithalani on 9/3/16.
 */
public class Lane {

    private int greenTime;
    private List<UltrasonicSensor> sensors;
    private float thresholdFactor;
    private int roadDistanceVariation;

    public Lane(int greenTime) {
        sensors = new ArrayList<>();
        this.greenTime = greenTime;
        thresholdFactor = 0.5f;
        roadDistanceVariation = 10;
    }

    public float getThresholdFactor() {
        return thresholdFactor;
    }

    public void setThresholdFactor(float thresholdFactor) {
        this.thresholdFactor = thresholdFactor;
    }

    public int getGreenTime() {
        return greenTime;
    }

    public void setGreenTime(int greenTime) {
        this.greenTime = greenTime;
    }

    public void addSensor(UltrasonicSensor sensor) {
        sensors.add(sensor);
    }

    public void start() {
        startSensor();
    }

    public boolean isTrafficAtPeak() {

        int sensorAtThreshold = 0;

        for (UltrasonicSensor sensor : sensors) {
            if (getTrafficFactor(sensor) <= thresholdFactor) {
                sensorAtThreshold++;
            }
        }

        return sensorAtThreshold / sensors.size() >= 0.5;
    }

    private float getTrafficFactor(UltrasonicSensor sensor) {

        List<Float> readings = sensor.getReadings();

        if (readings.isEmpty())
            return 0;

        int roadDistanceFrequency = calculateRoadDistanceFrequency(readings, getApproxRoadDistance(sensor));
        return (roadDistanceFrequency * 1.0f/ readings.size());
    }

    private float getApproxRoadDistance(UltrasonicSensor sensor) {
        return sensor.getRoadDistance() - roadDistanceVariation;
    }

    private int calculateRoadDistanceFrequency(List<Float> readings, float approxRoadDistance) {

        int roadDistanceFrequency = 0;

        for (Float reading : readings) {
            if (reading >= approxRoadDistance) {
                roadDistanceFrequency++;
            }
        }

        return roadDistanceFrequency;
    }

    private void startSensor() {
        sensors.forEach(sensor -> new Thread(sensor).start());
    }
}
