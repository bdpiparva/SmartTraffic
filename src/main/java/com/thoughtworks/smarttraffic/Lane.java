package com.thoughtworks.smarttraffic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salonivithalani on 9/3/16.
 */
public class Lane {

    private int greenTime;
    private final int defaultGreenTime;
    private List<UltrasonicSensor> sensors;
    private float thresholdFactor;
    private int roadDistanceVariation;
    private LEDTimer ledTimer;
    private int nextGreenTime;


    public Lane(int defaultGreenTime) {
        sensors = new ArrayList<>();
        this.greenTime = defaultGreenTime;
        this.defaultGreenTime = defaultGreenTime;
        thresholdFactor = 0.5f;
        roadDistanceVariation = 10;
    }

    public int getGreenTime() {
        return greenTime;
    }

    public void setGreenTime(int greenTime) {
        this.greenTime = greenTime;
    }

    public float getThresholdFactor() {
        return thresholdFactor;
    }

    public void setThresholdFactor(float thresholdFactor) {
        this.thresholdFactor = thresholdFactor;
    }

    public void addSensor(UltrasonicSensor sensor) {
        sensors.add(sensor);
    }

    public int getNextGreenTime() {
        return nextGreenTime;
    }

    public void setNextGreenTime(int nextGreenTime) {
        this.nextGreenTime = nextGreenTime;
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

    public void startSensor() {
        sensors.forEach(sensor -> new Thread(sensor).start());
    }

    public int getDefaultGreenTime() {
        return defaultGreenTime;
    }

    public void display(String content) {
    }

    public void setNextGreenTimeAsDefault() {
        this.nextGreenTime = this.defaultGreenTime;
    }

    public void updateGreenTime() {
        this.greenTime = this.nextGreenTime;
    }
}
