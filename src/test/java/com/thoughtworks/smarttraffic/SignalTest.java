package com.thoughtworks.smarttraffic;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.asm.Label;

import static org.junit.Assert.*;

/**
 * Created by Bhupendrakumar Piprava on 9/5/16.
 */
public class SignalTest {

    @Test
    public void shouldAbleToStatSignalProcessing() throws InterruptedException {

        Lane lane = Mockito.mock(Lane.class);
        Mockito.stubVoid(lane).toReturn().on().start();

        Signal golfCourse = new Signal("Golf Course", 10);
        golfCourse.addLane(lane);

        golfCourse.start();
    }

}