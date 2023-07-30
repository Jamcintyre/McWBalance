/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.climate;

import com.mcwbalance.ProjSetting;

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
    private String delim = "\t"; //tab delimited files;
    public static final String NULL_DESCRIP = "NONE";
    
    double aaprecip;
    double minaprecip;
    double maxaprecip;
    double yr1precip;
    
    

    DataClimate(int setsize) {
        calcsRun = false;
        description = NULL_DESCRIP;
        setSize(setsize);
    }

    DataClimate(String fileString) {
        calcsRun = false;
        String[] allLines, sLine; 
        
        allLines = fileString.split(ls);
        sLine = allLines[0].split(delim);
        description = sLine[1];
        
        if (allLines.length > ProjSetting.MAX_DURATION + 3){
            setSize(ProjSetting.MAX_DURATION + 1);
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
            sLine = allLines[i].split(delim);
            if (sLine.length < 9){
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

    public void calcSnowMelt() {
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
    public void calcStats() {
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

}
