package com.example.micrometerboot.sample;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleClassForTest {

    public int add(String a, String b) {
        int firstNum = 0;
        int secondNum = 0;
        try {
            firstNum = Integer.parseInt(a);
            secondNum = Integer.parseInt(b);
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
        return firstNum + secondNum;
    }

}
