package com.thoughtworks.smarttraffic;


import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Created by Bhupendrakumar Piprava on 9/2/16.
 */
public class Main {

    public static void main(String args[]) throws InterruptedException {

        Pin echoPin = RaspiPin.GPIO_00;
        Pin trigPin = RaspiPin.GPIO_07;
        UltrasonicSensor monitor = new UltrasonicSensor(echoPin, trigPin);
        new Thread(monitor).start();

        while (true) {
            System.out.println("Data:" + monitor.getReadings());
            Thread.sleep(5000);
        }

    }
}
