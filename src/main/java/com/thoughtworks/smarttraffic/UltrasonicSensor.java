package com.thoughtworks.smarttraffic;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;

import java.util.ArrayList;
import java.util.List;

public class UltrasonicSensor implements Runnable {

    private final static float SOUND_SPEED = 340.29f;  // speed of sound in m/s
    private final static int TRIG_DURATION_IN_MICROS = 10; // trigger duration of 10 micro s
    private final static int WAIT_DURATION_IN_MILLIS = 60; // wait 60 milli s
    private final static int TIMEOUT = Integer.MAX_VALUE;
    private static GpioController gpio;
    private GpioPinDigitalInput echoPin;
    private GpioPinDigitalOutput trigPin;

    private final List<Float> readings = new ArrayList<>();
    private boolean pause = true;
    //Unit is cm
    private float roadDistance;

    public UltrasonicSensor(Pin echoPin, Pin trigPin) {
        gpio = GpioFactory.getInstance();
        this.echoPin = gpio.provisionDigitalInputPin(echoPin);
        this.trigPin = gpio.provisionDigitalOutputPin(trigPin);
        this.trigPin.low();
        this.roadDistance = 300f;
    }

    public float measureDistance() throws TimeoutException {
        this.triggerSensor();
        this.waitForSignal();
        long duration = this.measureSignal();
        return duration * SOUND_SPEED / (2 * 10000);
    }

    private void triggerSensor() {
        try {
            this.trigPin.high();
            Thread.sleep(0, TRIG_DURATION_IN_MICROS * 1000);
            this.trigPin.low();
        } catch (InterruptedException ex) {
            System.err.println("Interrupt during trigger");
        }
    }

    private void waitForSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        while (this.echoPin.isLow() && countdown > 0) {
            countdown--;
        }

        if (countdown <= 0) {
            throw new TimeoutException("Timeout waiting for signal start");
        }
    }

    private long measureSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        long start = System.nanoTime();
        while (this.echoPin.isHigh() && countdown > 0) {
            countdown--;
        }
        long end = System.nanoTime();

        if (countdown <= 0) {
            throw new TimeoutException("Timeout waiting for signal end");
        }

        return (long) Math.ceil((end - start) / 1000.0);  // Return micro seconds
    }

    @Override
    public void run() {
        while (true) {
            if (pause) {
                try {
                    float distance = measureDistance();
                    readings.add(distance);
                } catch (TimeoutException e) {
                    System.err.println(e);
                }

                try {
                    Thread.sleep(WAIT_DURATION_IN_MILLIS);
                } catch (InterruptedException ex) {
                    System.err.println("Interrupt during trigger");
                }
            }
        }

    }

    public List<Float> getReadings() {
        List<Float> data = new ArrayList<>(readings.subList(0, readings.size()));
        readings.clear();
        return data;
    }

    public float getRoadDistance() {
        return roadDistance;
    }

    public void setRoadDistance(float roadDistance) {
        this.roadDistance = roadDistance;
    }

    private static class TimeoutException extends Exception {

        private final String reason;

        public TimeoutException(String reason) {
            this.reason = reason;
        }

        @Override
        public String toString() {
            return this.reason;
        }
    }

}