package com.thoughtworks.smarttraffic.ui;

/**
 * Created by Bhupendrakumar Piprava on 9/8/16.
 */
public class UIRunner {

    public static void main(String[] args) {
        LCD16X2 lcd16X2 = new LCD16X2();
        lcd16X2.write("This is string 1");
        lcd16X2.write("This is string 2");
        lcd16X2.write("This is string 3");
        lcd16X2.write("This is string 4");
    }
}
