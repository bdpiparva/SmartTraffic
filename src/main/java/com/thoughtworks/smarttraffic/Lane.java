package com.thoughtworks.smarttraffic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by salonivithalani on 9/3/16.
 */
public class Lane {

    private int greenTime;
    private List<UltrasonicSensor> sensors;
    private float thresholdFactor;

    public Lane(int greenTime) {
        sensors = new ArrayList<>();
        this.greenTime = greenTime;
        thresholdFactor = 0.5f;
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

        for(UltrasonicSensor sensor : sensors) {
            if (getTrafficFactor(sensor) >= thresholdFactor) {
                sensorAtThreshold++;
            }
        }

        if(sensorAtThreshold / sensors.size() >= 0.5) {
            return true;
        }

        return false;
    }

    private float getTrafficFactor(UltrasonicSensor sensor) {

        List<Float> readings = sensor.getReadings();

        float approxRoadDistance = sensor.getRoadDistance() - 10;

        int vehicleCount = getVehicleCount(readings, approxRoadDistance);

        return (float) (vehicleCount / readings.size());
    }

    private int getVehicleCount(List<Float> readings, float approxRoadDistance) {

        int vehicleCount = 0;

        for (Float reading : readings) {
            if (reading < approxRoadDistance) {
                vehicleCount++;
            }
        }
        return vehicleCount;
    }

    private void startSensor() {
        sensors.forEach(sensor -> new Thread(sensor).start());
    }
}
