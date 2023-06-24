/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

/**
 *
 * @author amcintyre
 */
public class CalcBasics {

    public static void debugPrintIntArray(int[] array){
        for (int i = 0; i < array.length; i ++){
           System.out.println("Array value at index " + i + " is " + array[i]);
        }

    }
    public static void debugPrintDoubleArray(double[] array){
        for (int i = 0; i < array.length; i ++){
           System.out.println("Array value at index " + i + " is " + array[i]);
        }

    }

    
    /**
     * Method for identifying which month given a numerical day value. Method assumes Day 1 is Jan 1. Method also assumes all years are 365 days, Feb 29 does not exist
     * @param day any positive day integer greater then 0; 
     * @return a value between 1 through 12 representing month
     */
    public static int getMonth(int day){
        int month;
        int dayofyear = day - 365*(int)(day/365); // this should take say 1.1 change it to 1 and them multiply back in.
            if(dayofyear <= 31){
                month = 1;
            }
            else if(dayofyear <= 59){
                month = 2;
            }
            else if(dayofyear <= 90){
                month = 3;
            }
            else if(dayofyear <= 120){
                month = 4;
            }
            else if(dayofyear <= 151){
                month = 5;
            }
            else if(dayofyear <= 181){
                month = 6;
            }
            else if(dayofyear <= 212){
                month = 7;
            }
            else if(dayofyear <= 243){
                month = 8;
            }
            else if(dayofyear <= 273){
                month = 9;
            }
            else if(dayofyear <= 304){
                month = 10;
            }
            else if(dayofyear <= 334){
                month = 11;
            }
            else{
                month = 12;
            }
            return month;
    }
    /**
     * Method for rounding to the preferred number of decimals. Method intended to function equal to Excel's round function
     * @param inValue Value to be rounded
     * @param decimals number of decimal places. Note does not handle negitive values yet.
     * @return 
     */
    public static double roundDbl (double inValue, int decimals){
        double roundedValue;
        int roundfactor = 1; // starts at 1 for 0 decimals
        int scaledValue; 
        for (int i = 0; i < decimals; i++){
            roundfactor = roundfactor * 10;
        }
        scaledValue = (int)(inValue*roundfactor); // shifts decimal place over and trims any trailing decimals
        if (inValue*roundfactor*10 - scaledValue*10 >= 5){ // checks to see if the decimal is 5
            scaledValue++; // adds 1 if needed 
        }
        roundedValue = scaledValue/roundfactor;
        return roundedValue;
    }
}
