/*
Copyright (c) 2026, Alex McIntyre
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. All advertising materials mentioning features or use of this software
   must display the following acknowledgement:
   This product includes software developed by Alex McIntyre.
4. Neither the name of the organization nor the
   names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.mcwbalance.climate;

import com.mcwbalance.settings.Limit;
import com.mcwbalance.measure.Depth;
import com.mcwbalance.measure.Time;

/**
 *
 * @author amcintyre
 */
public class DataClimate {  // Climate must begin on Jan 1. No Leap years. 
    
    /**
     * Short descriptive name of climate set, i.e. average conditions, wet conditions
     */
    private String description;
    
    /**
     * Total precipitation rain and snow over the time step
     * Unit and time step based on set time unit
     */
    public double[] precip;
    
    /**
     * Total precipitation rain only over the time step
     * Unit and time step based on set time unit
     */
    public double[] rain;
    
    /**
     * average temperature over the time step
     * Unit and time step based on set time unit
     */
    public double[] temp;
    
    /**
     * Total potential pan evaporation over the time step
     * Unit and time step based on set time unit
     */
    public double[] evap; 
    
    /**
     * Total accumulated snow pack over the time step
     * calculated from precipitation minus rain and melt plus previous snow pack
     * Unit and time step based on set time unit
     */
    public double[] snowpack;
    
    /**
     * Total potential melt  over the time step
     * calculated from precipitation rain temperature, previous month 
     * Unit and time step based on set time unit
     */
    public double[] melt;
    
    /**
     * Calculated from cumulative freezing index
     */
    public double[] iceThickness;
    
    
    private int size;
    private boolean calcsRun;
    private String ls = System.getProperty("line.separator");
    private String dl = ","; //switch to comma Seperated Values, Tab delimited is unreliable;
    
    
    /**
     * Used for identifying Null data
     */
    public static final String NULL_DESCRIP = "NULL";
    
    /**
     * Used for messaging that data is valid, intended for use with the validate 
     * method
     */
    public static final String VALID_DESCRIP = "VALID";
    
    /**
     * Fixed number of data columns allowed
     */
    public static final int DATA_COLUMNS = 10;
    
    /**
     * To prevent import of overly large data sets, which would occur if 
     * someone decides to try 10 years of hourly measurements
     * this can be relaxed after testing memory requirements as it might not be 
     * a limitation
     * 
     * TODO - confirm performance / memory limitations for large data sets
     * set to 100 years at default. 
     */
    public static final int MAX_SIZE = 36500;
    
    double aaprecip;
    double minaprecip;
    double maxaprecip;
    double yr1precip;
    
    
    private Time.TimeUnit timeunit;
    private Depth.DepthUnit depthunit;

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
        
        timeunit = Time.TimeUnit.Day;
        depthunit = Depth.DepthUnit.mm;
        
    }
    
    
    /**
     * builds a climate data set from a string
     * TODD add string validation 
     * TODO - allow variable depth and time units
     * @param fileString 
     */
    DataClimate(String fileString) {
        
        timeunit = Time.TimeUnit.Day;
        depthunit = Depth.DepthUnit.mm;
        
        
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
        for (int i = 2; i < (size + 2); i++) {
            sLine = allLines[i].split(dl);
            if (sLine.length < DATA_COLUMNS){
                System.err.print("input datafile does not contain enough columns, must contain " + DATA_COLUMNS);
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
     * Produces a .cclm formatted string which is a comma separated value ASCII file
     * Row titles are fixed in english
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
            int year;
            int month;
            int day;
            switch (timeunit){
                case Time.TimeUnit.Hour ->{
                    year = Time.getYearFromDay(i/24);
                    month = Time.Month.valueOfDay(i/24).getNumber();
                    day = Time.getJulianDayFromDay(i/24);
                }
                case Time.TimeUnit.Day ->{
                    year = Time.getYearFromDay(i);
                    month = Time.Month.valueOfDay(i).getNumber();
                    day = Time.getJulianDayFromDay(i);
                }
                case Time.TimeUnit.Month ->{
                    year = Time.getYearFromDay(i);
                    month = Time.Month.valueOf(i).getNumber();
                    day = Time.getJulianDayFromDay(i);
                }
                case Time.TimeUnit.Year ->{
                    year = i;
                    month = 1;
                    day = 1;
                }
                default ->{
                    year = Time.getYearFromDay(i);
                    month = Time.Month.valueOfDay(i).getNumber();
                    day = Time.getJulianDayFromDay(i);
                }
            } 
                        
            s.append(ls);
            s.append(i).append(dl);
            s.append(year).append(dl);
            s.append(month).append(dl);
            s.append(day).append(dl);
            s.append(String.valueOf(precip[i])).append(dl);
            s.append(String.valueOf(rain[i])).append(dl);
            s.append(String.valueOf(temp[i])).append(dl);
            s.append(String.valueOf(snowpack[i])).append(dl);
            s.append(String.valueOf(iceThickness[i]));
        }
        return s.toString();
    }
    
    private void setSize(int setsize){
        if (setsize < 1){
            size = 1;
        } else if (setsize > MAX_SIZE){
            size = MAX_SIZE;
        } else{
            size = setsize;
        }
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
    
    /**
     * Short descriptive name of this data set, not intended for lookup
     * @return short name
     */
    public String getDescription() {   
        return description;
    }
    
    /**
     * Runs internal calculations if not run already then provides average of the 
     * summed annual precipitation over the length of the data set
     * @return value in base units of data set, i.e. mm or inches 
     */
    public double getAnnualAvgPrecip() {
        if(!calcsRun){
            calcSnowMelt();
            calcStats();
            calcsRun = true;
        }
        return aaprecip;
    }

    /**
     * Runs internal calculations if not run already then provides minimum of the 
     * summed annual precipitation over the length of the data set
     * @return value in base units of data set, i.e. mm or inches 
     */
    public double getMinimumAnnualPrecip() {
        if(!calcsRun){
            calcSnowMelt();
            calcStats();
            calcsRun = true;
        }
        return minaprecip;
    }

    /**
     * Runs internal calculations if not run already then provides minimum of the 
     * summed annual precipitation over the length of the data set
     * @return value in base units of data set, i.e. mm or inches 
     */
    public double getMaximumAnnualPrecip() {
        if(!calcsRun){
            calcSnowMelt();
            calcStats();
            calcsRun = true;
        }
        return maxaprecip;
    }

    /**
     * Runs internal calculations if not run already then provides the first year of the 
     * summed annual precipitation over the length of the data set
     * this is used for visual identification of a data set
     * @return value in base units of data set, i.e. mm or inches 
     */
    public double getyr1Precip() {
        if(!calcsRun){
            calcSnowMelt();
            calcStats();
            calcsRun = true;
        }
        return yr1precip;
    }
    
    /**
     * For setting the description of the climate data set, normally called to 
     * set it to a null descriptor
     * @param description 
     */
    public void setDescription(String description){
        this.description = description;
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
