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
     * Finds the maximum value in an array of doubles
     * @param array
     * @return 
     */
    public static double findMaxDouble(double[] array){
        double max = array[0];
        for (int i = 1; i < array.length; i++){
            if(max < array[i]){
                max = array[i];
            }
        }
        return max;
    }
     /**
     * Finds the maximum value in an array of integers
     * @param array
     * @return 
     */
    public static int findMaxInteger(int[] array){
        int max = array[0];
        for (int i = 1; i < array.length; i++){
            if(max < array[i]){
                max = array[i];
            }
        }
        return max;
    }
    
    /**
     * Finds the minimum value in an array of doubles
     * @param array
     * @return 
     */
    public static double findMinDouble(double[] array){
        double min = array[0];
        for (int i = 1; i < array.length; i++){
            if(min > array[i]){
                min = array[i];
            }
        }
        return min;
    }

    /**
     * Finds the minimum value in an array of integers
     * @param array
     * @return 
     */
    public static int findMinInteger(int[] array){
        int min = array[0];
        for (int i = 1; i < array.length; i++){
            if(min > array[i]){
                min = array[i];
            }
        }
        return min;
    }
    
    /**
     * Finds a matching string index value in a given array of strings
     * @param searchvalue Value to search for
     * @param array list of Strings to search in
     * @return index value of match, returns -1 if no match found
     */
    public static int findArrayMatchIndex(String searchvalue, String[] array){
        for (int i = 0; i < array.length; i++){
            if (array[i].equals(searchvalue)){
                return i;
            }
        }
        return -1;
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
     * Method for identifying which year to assign a numerical day value. Method assumes Day 1 is Jan 1. Method also assumes all years are 365 days, Feb 29 does not exist
     * this may be adjusted later
     * @param day any positive day integer greater then 0; 
     * @return a year number starting at 1.
     */
    public static int getYear(int day){
        return (int)(day/365);
    }
    
    /**
     * Provides the number of days in a month, month 1 is January, ignores leap years
     * @param month
     * @return number of days in a month, will always return 28days for feb, if month is not valid 30 is returned
     */
    public static int getDaysInMonth(int month){
        month = month - (int)(month/12);
        return switch (month){
            case 1 -> 31;
            case 2 -> 28;
            case 3 -> 31;
            case 4 -> 30;
            case 5 -> 31;
            case 6 -> 30;
            case 7 -> 31;
            case 8 -> 31;
            case 9 -> 30;
            case 10 -> 31;
            case 11 -> 30;
            case 12 -> 31;
            default -> 30;  
        };
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
