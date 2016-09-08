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
    public void shouldAbleToCalculateGreenTimeFactor() throws InterruptedException {
        System.out.print((int)Math.ceil(10 / 15f));

    }

}