package com.thoughtworks.smarttraffic.ui;

import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.LCDBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.wiringpi.Lcd;

/**
 * Created by Bhupendrakumar Piprava on 9/8/16.
 */
public class GPIOLCDDisplay extends LCDBase implements LCD {

    protected int rows;
    protected int columns;
    private int lcdHandle;

    public GPIOLCDDisplay(int rows, int columns, Pin rsPin, Pin strobePin, Pin... dataPins) {
        this.rows = rows;
        this.columns = columns;
        int bits[] = {0, 0, 0, 0, 0, 0, 0, 0};

//        com.pi4j.wiringpi.Gpio.wiringPiSetup();

        // seed bit pin address array
        for (int index = 0; index < 8; index++) {
            if (index < dataPins.length)
                bits[index] = dataPins[index].getAddress();
        }

        // initialize LCD
        lcdHandle = Lcd.lcdInit(rows,
                columns,
                dataPins.length,
                rsPin.getAddress(),
                strobePin.getAddress(),
                bits[0], bits[1], bits[2], bits[3], bits[4], bits[5], bits[6], bits[7]);

        // verify LCD initialization
        if (lcdHandle == -1)
            throw new RuntimeException("Invalid LCD handle returned from wiringPi: " + lcdHandle);
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public void clear() {
        Lcd.lcdClear(lcdHandle);
    }

    @Override
    public void setCursorHome() {
        Lcd.lcdHome(lcdHandle);
    }

    @Override
    public void setCursorPosition(int row, int column) {
        validateCoordinates(row, column);
        Lcd.lcdPosition(lcdHandle, column, row);
    }

    @Override
    public void write(byte data) {
        Lcd.lcdPutchar(lcdHandle, data);
    }

    @Override
    public void write(String data) {
        Lcd.lcdPuts(lcdHandle, data);
    }
}
