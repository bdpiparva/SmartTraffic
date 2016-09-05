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

        Lane A = new Lane(15);
        A.addSensor(monitor);

        Signal golfCourse = new Signal("Golf Course", 10);
        golfCourse.addLane(A);

        golfCourse.start();
    }
}
