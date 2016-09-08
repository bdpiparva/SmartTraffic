package com.thoughtworks.smarttraffic.ui;

import com.pi4j.io.gpio.RaspiPin;

/**
 * Created by Bhupendrakumar Piprava on 9/8/16.
 */
public class LCD16X2 {

    final GPIOLCDDisplay lcd;

    public LCD16X2() {

        lcd = new GPIOLCDDisplay(2,
                16,
                RaspiPin.GPIO_26,
                RaspiPin.GPIO_21,
                RaspiPin.GPIO_22,
                RaspiPin.GPIO_23,
                RaspiPin.GPIO_24,
                RaspiPin.GPIO_25);

    }

    public void write(String string) {
        lcd.clear();
        lcd.write(string);
    }

}
