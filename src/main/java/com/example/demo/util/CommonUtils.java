package com.example.demo.util;

import java.util.Arrays;
import java.util.random.RandomGenerator;


public class CommonUtils {
    public static boolean checkValuePresent(String commaSeparatedValues, String value) {
        String[] array = commaSeparatedValues.split(",");
        return Arrays.asList(array).contains(value);
    }

    public static boolean checkValueAbsent(String commaSeparatedValues, String value) {
        return !checkValuePresent(commaSeparatedValues, value);
    }

    public static int generateOTP(){
        return RandomGenerator.getDefault().nextInt();
    }
}
