/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.generics;

import com.mcwbalance.ProjSetting;
import com.mcwbalance.settings.Limit;

/**
 *
 * @author amcintyre
 */
public class DataTimeDoubleSeries {
      public int length;
    private int[] day;
    private double[] value;
    public static int MAX_LENGTH = Limit.MAX_LEVELS; 
    public static final int DAY_NULL = -1;
    public static final double VAL_NULL = -9999;
    
    /**
     * constructs a blank null value array, mostly as a placeholder for table generation, note that calling remove DoupAndDUlls will erase the list.
     * @param nPoints 
     */
    public DataTimeDoubleSeries(int nPoints){
        if (nPoints > MAX_LENGTH){
            nPoints = MAX_LENGTH;
        }else if (nPoints < 1){
            nPoints = 1;
        }
        length = nPoints;
        day = new int[length];
        value = new double[length];
        day[0] = 0; // first day mmust be 0; 
        value[0] = 0; // note program will ignore the first DAC area and storage given as they must be 0;
        for (int i = 1; i < nPoints; i ++){
            day[i] = DAY_NULL;
            value[i] = VAL_NULL;
        }
    }
    /**
     * constructs the full set of both arrays, as well calls removeDoupAndNulls and sortAssending Methods
     * @param inDay
     * @param inValue 
     */
    public DataTimeDoubleSeries(int inDay[], double inValue[]){
        if(inDay.length < 1 || inValue.length <1){
            length = 1;
            day = new int[length];
            value = new double[length];
            day[0] = 0; 
            value[0] = 0;
        }else if (inDay.length == inValue.length){
            if (inDay.length <= MAX_LENGTH){
                day = inDay;
                value = inValue;
            }
            else {
                length = MAX_LENGTH;
                day = new int[length];
                value = new double[length];
                for (int i = 0; i < length; i ++){
                    day[i] = inDay[i];
                    value[i] = inValue[i];
                }
            }
            
        }else if (inDay.length < inValue.length){
            length = inDay.length;
            day = new int[length];
            value = new double[length];
            for (int i = 0; i < length; i ++){
                day[i] = inDay[i];
                value[i] = inValue[i];
            }
        }else {
            length = inValue.length;
            day = new int[length];
            value = new double[length];
            for (int i = 0; i < length; i ++){
                day[i] = inDay[i];
                value[i] = inValue[i];
            }
        }
        sortAssending();
        removeDoupAndNulls();

    }
    /**
     * Replaces array data with new inputs then calls removeDoupAndNulls and sortAssending Methods
     * @param inDay
     * @param inValue 
     */
    public int[] getDays(){
        return day;
    }
    public double[] getValues(){
        return value;
    }
    public double getValue(int inDay){
        for (int i = length - 1; i > -1; i --){
           if(inDay >= day[i]){
               return value[i];
           } 
        }
        System.err.println("DataTimeIntSeries - WARNING VALUE NOT FOUND FOR SEARCHED DAY " + inDay +" VALUE OF " + value[0] + "RETURNED INSTEAD");
        return value[0];
    }
    public StringBuilder getTabbedString(String header){
        StringBuilder tabbedString = new StringBuilder();
        tabbedString.append(header);
        tabbedString.append(System.getProperty("line.separator"));
        for (int i = 0; i < length; i ++){
            tabbedString.append(day[i]);
            tabbedString.append("\\t");
            tabbedString.append(value[i]);
            tabbedString.append(System.getProperty("line.separator"));
        }
        tabbedString.append(ProjSetting.LIST_TERMINATOR);
        tabbedString.append(System.getProperty("line.separator"));
        return tabbedString; 
    }
    public void setFromStringLine(String line, int index){
        if (index < MAX_LENGTH && index >= 0 && line != ProjSetting.LIST_TERMINATOR){
            if (index +1 < length){
                int newdayArray[] = new int[index +1];
                double newvalueArray[] = new double[index +1];
                for (int i = 0; i < length; i ++){
                    newdayArray[i] = day[i];
                    newvalueArray[i] = value[i];
                }
                int newday = Integer.valueOf(line.split("\\t")[0]);
                double newvalue = Double.valueOf(line.split("\\t")[1]);
                newdayArray[index +1] = newday;
                newvalueArray[index +1] = newvalue;
                day = newdayArray;
                value = newvalueArray; 
            }else{ // needed for the first row.
                int newday = Integer.valueOf(line.split("\\t")[0]);
                double newvalue = Double.valueOf(line.split("\\t")[1]);
                day[index +1] = newday;
                value[index +1] = newvalue;
            }  
        }
    }
    public void setAllData(int inDay[], double inValue[]){
        int newDay[];
        double newValue[];
        if(inDay.length < 1 || inValue.length <1){
            length = 1;
            newDay = new int[length];
            newValue = new double[length];
            newDay[0] = 0; 
            newValue[0] = 0;
        }else if (inDay.length == inValue.length){
            if (inDay.length <= MAX_LENGTH){
                length = inDay.length;
                newDay = inDay;
                newValue = inValue;
            }
            else {
                length = MAX_LENGTH;
                newDay = new int[length];
                newValue = new double[length];
                for (int i = 0; i < length; i ++){
                    newDay[i] = inDay[i];
                    newValue[i] = inValue[i];
                }
            }
            
        }else if (inDay.length < inValue.length){
            length = inDay.length;
            newDay = new int[length];
            newValue = new double[length];
            for (int i = 0; i < length; i ++){
                newDay[i] = inDay[i];
                newValue[i] = inValue[i];
            }
        }else {
            length = inValue.length;
            newDay = new int[length];
            newValue = new double[length];
            for (int i = 0; i < length; i ++){
                newDay[i] = inDay[i];
                newValue[i] = inValue[i];
            }
        }
        
        day = newDay;
        value = newValue; 
        sortAssending();
        removeDoupAndNulls();
    }
    /**
     * Removes any douplicated day values since 1 day should not have 2 seperate operating targets
     * Also removes null values from list (i.e. -1) to minimize clutter
     */
    public void removeDoupAndNulls(){ // this is having the problem
        double cDay;
        for (int i = 0; i < length; i ++){
            cDay = day[i];
            for (int j = i + 1; j < length; j ++){
                if (day[j] == cDay || day[j] == DAY_NULL || value[j] == VAL_NULL){
                    for (int k = j + 1; k < length; k ++){
                        day[k-1] = day[k];
                        value[k-1] = value[k];
                    }
                    length--;
                }
            }
        }
        if (length != day.length){
            int newDay[] = new int[length];
            double newValue[] = new double[length];
             newDay[0] = 0; // forces first day value to be 0;
             newValue[0] = value[0];
            
            for (int i = 1; i < length; i ++){
                newDay[i] = day[i];
                newValue[i] = value[i];
            }
            
            
            day = newDay;
            value = newValue;
        }
    }
    /**
     * Sorts both day and Value arrays in assending order of day; 
     */
    public void sortAssending(){
        int swapDay;
        double swapValue;
        for (int i = 0; i < length; i ++){ // may need to move the i++ and j++, want to repeat the step if needed;
            for (int j = i+1; j < length; j ++){
                if(day[j] < day[i]){ // if it matches leave it alone, the doublicate cleaner will deal with it; 
                    swapDay = day[j];
                    swapValue = value[j];
                    for (int k = j-1; k > i-1; k --){
                        day[k+1] = day[k];
                        value[k+1] = value[k];
                    }
                    day[i] = swapDay;
                    value[i] = swapValue;
                    i = -1; // assumes i++ will bring this back to 0 and loop will restart;
                    break; 
                }
            }
        }
    }
    
}
