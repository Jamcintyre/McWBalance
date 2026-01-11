/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.climate;

import com.mcwbalance.settings.Limit;

/**
 *
 * @author amcintyre
 */
public class DataClimate {  // Climate must begin on Jan 1. No Leap years. 

    public String description;
    public double[] precip;
    public double[] rain;
    public double[] temp;
    public double[] evap;
    public double[] snowpack;
    public double[] melt;
    public double[] iceThickness;
    public int size;
    private boolean calcsRun;
    private String ls = System.getProperty("line.separator");
    private String dl = ","; //switch to comma Seperated Values, Tab delimited is unreliable;
    public static final String NULL_DESCRIP = "NULL";
    public static final String VALID_DESCRIP = "VALID";
    public static final int DATA_COLUMNS = 10;
    
    double aaprecip;
    double minaprecip;
    double maxaprecip;
    double yr1precip;

    DataClimate(int setsize) {
        calcsRun = false;
        description = NULL_DESCRIP;
        setSize(setsize);
    }
    
    /**
     * Builds and initial null climate set
     */
    DataClimate(){
        description = NULL_DESCRIP;
        size = 1;
        precip = new double[size];
        rain = new double[size];
        temp = new double[size];
        evap = new double[size];
        snowpack = new double[size];
        melt = new double[size];
        iceThickness = new double[size];
        calcsRun = false;
        calcStats();
        calcsRun = true;
    }
    
    
    /**
     * builds a climate dataset from a string
     * ToDo add string validator 
     * @param fileString 
     */
    DataClimate(String fileString) {
        calcsRun = false;
        String[] allLines, sLine; 
        
        allLines = fileString.split(ls);
        sLine = allLines[0].split(dl);
        description = sLine[1];
        
        if (allLines.length > Limit.MAX_DURATION + 3){
            setSize(Limit.MAX_DURATION + 1);
        }
        else if (allLines.length <= 2){
            setSize(1);
            precip[0] = 0;
            rain[0] = 0;
            temp[0] = 0;
            evap[0] = 0;
            snowpack[0] = 0;
            melt[0] = 0;
            iceThickness[0] = 0;
            return;
        }
        else {
            setSize(allLines.length - 2);
        }
        for (int i = 2; i < size + 2; i++) {
            sLine = allLines[i].split(dl);
            if (sLine.length < DATA_COLUMNS){
                System.err.print("input datafile does not contain enough columns");
                break;
            }
            precip[i-2] = Double.parseDouble(sLine[4]);
            rain[i-2] = Double.parseDouble(sLine[5]);
            temp[i-2] = Double.parseDouble(sLine[6]);
            evap[i-2] = Double.parseDouble(sLine[7]);
            snowpack[i-2] = Double.parseDouble(sLine[8]);
            iceThickness[i-2] = Double.parseDouble(sLine[9]);
        }
        calcSnowMelt();
        calcStats();
        calcsRun = true;

    }
    
    /**
     * Produces a .cclm formatted string
     * @return 
     */
    public String getString(){
        StringBuilder s = new StringBuilder();
        s.append("Climate Scenario").append(dl);
        s.append(description).append(ls);    
        s.append("ModelDay").append(dl);
        s.append("Year").append(dl);
        s.append("Month").append(dl);
        s.append("JulienDay").append(dl);
        s.append("Precip(mm)").append(dl);
        s.append("Rain(mm)").append(dl);
        s.append("AvgDailyTemp(C)").append(dl);
        s.append("Evap (mm)").append(dl);
        s.append("Swwe (mm)").append(dl);
        s.append("Ice Thickness");

        for (int i = 0; i < size; i++){
            s.append(ls);
            s.append(i).append(dl);
            s.append(getyear(i)).append(dl);
            s.append(getmonth(i)).append(dl);
            s.append(getjulianday(i)).append(dl);
            s.append(String.valueOf(precip[i])).append(dl);
            s.append(String.valueOf(rain[i])).append(dl);
            s.append(String.valueOf(temp[i])).append(dl);
            s.append(String.valueOf(snowpack[i])).append(dl);
            s.append(String.valueOf(iceThickness[i]));
        }
        return s.toString();
    }
    
    private void setSize(int setsize){
        size = setsize;
        precip = new double[size];
        rain = new double[size];
        temp = new double[size];
        evap = new double[size];
        snowpack = new double[size];
        melt = new double[size];
        iceThickness = new double[size];
        
    }

    private void calcSnowMelt() {
        if (!description.contentEquals(NULL_DESCRIP)) {
            for (int i = 1; i < size; i++) {
                melt[i] = snowpack[i - 1] + precip[i] - rain[i] - snowpack[i];
                if (melt[i] < 0) {
                    melt[i] = 0;
                }
            }
        }
    }
    /**
     * to be called to calculate averages and maximums
     */
    private void calcStats() {
        double sum = 0;
        double min;
        double max;
        int d = 1;
        if (description.contentEquals(NULL_DESCRIP)) {
            aaprecip = 0;
            minaprecip = 0;
            maxaprecip = 0;
            yr1precip = 0;

        } else {

            for (int i = 1; i < precip.length; i++) {
                sum = sum + precip[i];
            }
            aaprecip = sum / (precip.length / 365);
            sum = 0;
            for (int i = 1; i < 366 && i < precip.length; i++) {
                sum = sum + precip[i];
            }
            yr1precip = sum;
            min = sum;
            max = sum;
            sum = 0;
            for (int i = 366; i < precip.length; i++) {
                sum = sum + precip[i];
                d++;

                if (d == 365) {
                    if (sum < min) {
                        min = sum;
                    }
                    if (sum > max) {
                        max = sum;
                    }
                    sum = 0;
                    d = 1;
                }
            }
            minaprecip = min;
            maxaprecip = max;
        }
        
        
    }

    public String getDescription() {
        if(!calcsRun){
            calcSnowMelt();
            calcStats();
            calcsRun = true;
        }
        
        return description;
    }

    public double getAnnualAvgPrecip() {
        if(!calcsRun){
            calcSnowMelt();
            calcStats();
            calcsRun = true;
        }
        return aaprecip;
    }

    public double getMinimumAnnualPrecip() {
        if(!calcsRun){
            calcSnowMelt();
            calcStats();
            calcsRun = true;
        }
        return minaprecip;
    }

    public double getMaximumAnnualPrecip() {
        if(!calcsRun){
            calcSnowMelt();
            calcStats();
            calcsRun = true;
        }
        return maxaprecip;
    }

    public double getyr1Precip() {
        if(!calcsRun){
            calcSnowMelt();
            calcStats();
            calcsRun = true;
        }
        return yr1precip;
    }
    
    int getyear(int day){
        return day/365;
    }
    
    /**
     * ignores leap years
     * @param day
     * @return 
     */
    int getmonth(int day){
        int jday = getjulianday(day);
        
        if(jday > 334){
            return 12;
        }
        if(jday > 304){
            return 11;
        }
        if(jday > 273){
            return 10;
        }
        if(jday > 243){
            return 9;
        }
        if(jday > 212){
            return 8;
        }
        if(jday > 181){
            return 7;
        }
        if(jday > 151){
            return 6;
        }
        if(jday > 120){
            return 5;
        }
        if(jday > 90){
            return 4;
        }
        if(jday > 59){
            return 3;
        }
        if(jday > 31){
            return 2;
        }
        return 1;   
    }
    
    /**
     * ignores leap years
     * @param day
     * @return 
     */
    int getjulianday(int day){
        return day - (getyear(day) - 1)/365;
    }
    
    
    /**
     * TODO - placeholder not completed yet
     * @param input
     * @return 
     */
    public String validateString(String input){
        String msg = VALID_DESCRIP;
        
        String st = input; 
        
        
        
        
        return msg;
    }

}
