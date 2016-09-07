package com.thoughtworks.smarttraffic;


import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Created by Bhupendrakumar Piprava on 9/2/16.
 */
public class Main {

    public static void main(String args[]) throws InterruptedException {

        Pin echoPin1 = RaspiPin.GPIO_00;
        Pin echoPin2 = RaspiPin.GPIO_01;
//        Pin echoPin3 = RaspiPin.GPIO_02;
//        Pin echoPin4 = RaspiPin.GPIO_03;

        Pin trigPin1 = RaspiPin.GPIO_07;
        Pin trigPin2 = RaspiPin.GPIO_08;
//        Pin trigPin3 = RaspiPin.GPIO_09;
//        Pin trigPin4 = RaspiPin.GPIO_10;

        UltrasonicSensor monitor1 = new UltrasonicSensor(echoPin1, trigPin1);
        UltrasonicSensor monitor2 = new UltrasonicSensor(echoPin2, trigPin2);
//        UltrasonicSensor monitor3 = new UltrasonicSensor(echoPin3, trigPin3);
//        UltrasonicSensor monitor4 = new UltrasonicSensor(echoPin4, trigPin4);

        Lane A = new Lane(10);
        A.addSensor(monitor1);

        Lane B = new Lane(10);
        B.addSensor(monitor2);

//        Lane C = new Lane(15);
//        C.addSensor(monitor3);
//
//        Lane D = new Lane(15);
//        D.addSensor(monitor4);

        Signal golfCourse = new Signal("Golf Course", 5);
        golfCourse.addLane(A);
        golfCourse.addLane(B);
       /* golfCourse.addLane(C);
        golfCourse.addLane(D);*/

        golfCourse.start();
    }
}
