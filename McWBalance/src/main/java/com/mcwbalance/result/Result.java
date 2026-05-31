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
package com.mcwbalance.result;

import com.mcwbalance.measure.Time;
import com.mcwbalance.measure.Unit;
import com.mcwbalance.settings.Limit;

/**
 * Class used to store a time series result in multiple time step options, i.e.
 * hourly, daily, weekly, monthly, annual.
 *
 * Class is intended to allow fast data retrieval and plotting, this may not be
 * memory efficient as it generates summary arrays.
 * 
 * TODO - Add testing
 * 
 * TEST STATUS - Not tested
 *
 * @author Alex
 */
public class Result {

    private final float[] hourly;
    private final float[] daily; // this will be a memory hog, may switch to storing as int or shifted int if memory becomes a limitation
    private final float[] weekly;
    private final float[] monthly; // Any value more then daily is stored as int to save memory
    private final float[] annual;
    private String name;
    private Unit unit;
    private Time.TimeUnit timeUnit;
    private boolean isflow;
    private int hours;
    private int days;
    private int weeks;
    private int months;
    private int years;

    private boolean calculated;

    /**
     * Initializes a result array Note that it is assumed that element 0 of the
     * result is for initial conditions and in most cases the initial condition
     * will be 0 unless initial conditions i.e. pond volume or level are set
     * explicitly
     *
     * @param name name of result
     * @param timesteps number of time steps, limited to 0 - Limit.MAX_DURATION; out of 
     * bounds will be adjusted without warning
     * @param timeUnit time step unit such as hour, day, week, month, year
     * @param unit result unit of measure
     * @param isFlow Sets if values should be calculated as flows, or as levels
     * / stored volumes
     */
    public Result(String name, int timesteps, Time.TimeUnit timeUnit, Unit unit, boolean isFlow) {

        this.name = name;
        this.timeUnit = timeUnit;
        this.unit = unit;
        this.isflow = isFlow;
        
        if(timesteps > Limit.MAX_DURATION){
            timesteps = Limit.MAX_DURATION;
        } else if (timesteps < 0) {
            timesteps = 0;
        }
        

        switch (this.timeUnit) {
            case Time.TimeUnit.Hour -> {
                hours = timesteps;
                days = hours / 24 + 1;
                weeks = days / 7 + 1;
                years = days / 365 + 1;
                months = years*12; // Cannot test Enum in method so switched to this             
            }
            case Time.TimeUnit.Day -> {
                hours = 0;
                days = timesteps;
                weeks = days / 7 + 1;
                years = timesteps / 365 + 1;
                months = years*12; // Cannot test Enum in method so switched to this
                
            }
            case Time.TimeUnit.Week -> {
                hours = 0;
                days = 0;
                weeks = timesteps;
                years = timesteps * 7 / 365;
                months = years*12; // Cannot test Enum in method so switched to this
            }
            case Time.TimeUnit.Month -> {
                hours = 0;
                days = 0;
                weeks = 0;
                months = timesteps;
                years = months / 12 + 1;
            }
            case Time.TimeUnit.Year -> {
                hours = 0;
                days = 0;
                weeks = 0;
                months = 0;
                years = timesteps;
            }

            default -> {
                hours = 0;
                days = 0;
                weeks = 0;
                months = 0;
                years = 0;
            }
        }
        hourly = new float[hours + 1];
        daily = new float[days + 1]; //Timestep 0 will be used to hold initialiation values so + 1 is needed to get to end;
        weekly = new float[weeks + 1];
        monthly = new float[months + 1];
        annual = new float[years + 1];
        hourly[0] = 0;
        weekly[0] = 0;
        daily[0] = 0;
        monthly[0] = 0;
        annual[0] = 0;

        calculated = false;
    }

    /**
     * Adds a result value to a corresponding result series based on current
     * units Note that this method does not implement error handling, i.e. out
     * of bounds requests wont be handled.
     *
     * @param result value to add
     * @param timestep to add result to, note that time step can be in hours
     * days, months, years etc.. should check units if needed before adding
     * Method adds +1 to timestep for the 0 (initial) conditions;
     */
    public void add(float result, int timestep) {
        switch (this.timeUnit) {
            case Time.TimeUnit.Hour ->
                hourly[timestep] = result;
            case Time.TimeUnit.Week ->
                weekly[timestep] = result;
            case Time.TimeUnit.Day ->
                daily[timestep] = result;
            case Time.TimeUnit.Month ->
                monthly[timestep] = result;
            case Time.TimeUnit.Year ->
                annual[timestep] = result;
        }
        if(calculated){
            calculated = false;
        }
    }

    /**
     * used to calculate summary results of different time steps from result
     * needs to check if flow
     * Might be able to increase performance by getting weekly from daily etc.. 
     * TODO - Not completed
     */
    public void calculate() {
        if (isflow) { // if flow then sum the value to get year and day
            switch (timeUnit) {
                case Time.TimeUnit.Hour -> {
                    int day = 0;
                    int week = 1;
                    int month = 0;
                    Time.Month curMonth = Time.Month.Jan;
                    int year = 0;
                    for (int hour = 1; hour < hourly.length; hour++) {              
                        if ((float)hour / 24 > day) {
                            day++;
                        }
                        if ((float)hour / 168 > week) {
                            week++;
                        }
                        if (Time.Month.valueOfDay(day) != curMonth) {
                            month++;
                            curMonth = Time.Month.valueOfDay(day);
                        }
                        if ((float)hour/8760> year) {
                            year++;
                        }
                        daily[day] += hourly[hour];
                        weekly[week] += hourly[hour];
                        monthly[month] += hourly[hour];
                        annual[year] += hourly[hour];
                    }
                }
                case Time.TimeUnit.Day -> {
                    int week = 1;
                    int month = 0;
                    Time.Month curMonth = Time.Month.Jan;
                    int year = 0;
                    for (int day = 1; day < daily.length; day++) {
                        if ((float)day / 7 > week) {
                            week++;
                        }
                        if (Time.Month.valueOfDay(day) != curMonth) {
                            month++;
                            curMonth = Time.Month.valueOfDay(day);
                        }
                        if ((float)day/365 > year) {
                            year++;
                        }
                        weekly[week] += daily[day];
                        monthly[month] += daily[day];
                        annual[year] += daily[day];
                    }
                }
                case Time.TimeUnit.Week -> {
                    int month = 0;
                    Time.Month curMonth = Time.Month.Jan;
                    int year = 0;
                    for (int week = 1; week < weekly.length; week++) {
                        if (Time.Month.valueOfDay(week*7) != curMonth) {
                            month++;
                            curMonth = Time.Month.valueOfDay(week*7);
                        }
                        if ((float)week*7/365 > year) {
                            year++;
                        }
                        monthly[month] += weekly[week];
                        annual[year] += weekly[week];
                    }
                }
                
                case Time.TimeUnit.Month -> {
                    int year = 0;
                    for (int month = 1; month < monthly.length; month++) {

                        if ((float)month/12 > year) {
                            year++;
                        }
                        annual[year] += monthly[month];
                    }
                }
            }
        } else { // if level get the first possible value for timesept
            switch (timeUnit) {

                case Time.TimeUnit.Hour -> {
                    int day = 0;
                    int week = 1;
                    int month = 0;
                    Time.Month curMonth = Time.Month.Jan;
                    int year = 0;
                    monthly[1] = hourly[1];
                    for (int hour = 1; hour < hourly.length; hour++) {              
                        if ((float)hour / 24 > day) {
                            day++;
                            daily[day] = hourly[hour];
                        }
                        if ((float)hour / 168 > week) {
                            week++;
                            weekly[week] = hourly[hour];
                        }
                        if (Time.Month.valueOfDay(day) != curMonth) {
                            month++;
                            curMonth = Time.Month.valueOfDay(day);
                            monthly[month] = hourly[hour];
                        }
                        if ((float)hour/8760> year) {
                            year++;
                            annual[year] = hourly[hour];
                        }
                    }
                }
                case Time.TimeUnit.Day -> {
                    int week = 1;
                    int month = 0;
                    monthly[1] = daily[1];
                    Time.Month curMonth = Time.Month.Jan;
                    int year = 0;
                    for (int day = 1; day < daily.length; day++) {
                        if ((float)day / 7 > week) {
                            week++;
                            weekly[week] = daily[day];
                        }
                        if (Time.Month.valueOfDay(day) != curMonth) {
                            month++;
                            curMonth = Time.Month.valueOfDay(day);
                            monthly[month] = daily[day];         
                        }
                        if ((float)day/365 > year) {
                            year++;
                            annual[year] = daily[day];
                        }
                    }
                }
                case Time.TimeUnit.Week -> {
                    int month = 0;
                    monthly[1] = weekly[1];
                    Time.Month curMonth = Time.Month.Jan;
                    int year = 0;
                    for (int week = 1; week < weekly.length; week++) {
                        if (Time.Month.valueOfDay(week*7) != curMonth) {
                            month++;
                            curMonth = Time.Month.valueOfDay(week*7);
                            monthly[month] = weekly[week];
                        }
                        if ((float)week*7/365 > year) {
                            year++;
                            annual[year] = weekly[week];
                        }
                    }
                }
                case Time.TimeUnit.Month -> {
                    int year = 0;
                    for (int month = 1; month < monthly.length; month++) {
                        if ((float)month/12 > year) {
                            year++;
                            annual[year] = monthly[month];
                        }
                    }
                }
            }
        }

        calculated = true;
    }

    /**
     * For pulling an array of Annual data will trigger a calculation run if
     * needed
     *
     * @return array containing years + 1, year 0 being initial conditions
     */
    public float[] getAnnual() {
        if (calculated) {
            return this.annual;
        }
        calculate();
        return annual;
    }

    /**
     * For pulling an array of daily data will trigger a calculation run if
     * needed
     *
     * @param timestep to retrieve, year 0 for initial conditions
     * @return value out of array representing day
     */
    public float getAnnual(int timestep) {
             if (timestep == 0) {
            return getAnnual()[timestep];
        }
        return switch (this.timeUnit) {
            case Time.TimeUnit.Hour ->
                getAnnual()[timestep * (int) (Time.TimeUnit.getConversion(Time.TimeUnit.Hour, Time.TimeUnit.Year))];
            case Time.TimeUnit.Day ->
                getAnnual()[timestep * (int) (Time.TimeUnit.getConversion(Time.TimeUnit.Day, Time.TimeUnit.Year))];
            case Time.TimeUnit.Week ->
                getAnnual()[timestep * (int) (Time.TimeUnit.getConversion(Time.TimeUnit.Week, Time.TimeUnit.Year))];
            case Time.TimeUnit.Month ->
                getAnnual()[timestep * (int) (Time.TimeUnit.getConversion(Time.TimeUnit.Month, Time.TimeUnit.Year))];
            case Time.TimeUnit.Year ->
                getAnnual()[timestep];
            default ->
                0;
        };
    }

    /**
     * For pulling an array of daily data will trigger a calculation run if
     * needed
     *
     * @return array containing days + 1, day 0 being initial conditions
     */
    public float[] getDaily() {
        if (calculated) {
            return daily;
        }
        calculate();
        return daily;
    }

    /**
     * For pulling an array of daily data will trigger a calculation run if
     * needed
     *
     * @param timestep to retrieve, step 0 for initial conditions
     * @return daily data based on daily data if available or factored down
     * from next smallest time step
     */
    public float getDaily(int timestep) {
        if (timestep == 0) {
            return getDaily()[timestep];
        }
        return switch (this.timeUnit) {
            case Time.TimeUnit.Hour ->
                getDaily()[(int)(timestep * Time.TimeUnit.getConversion(Time.TimeUnit.Hour, Time.TimeUnit.Day))];
            case Time.TimeUnit.Day ->
                getDaily()[timestep];
            case Time.TimeUnit.Week ->
                getWeekly()[timestep] * (float)Time.TimeUnit.getConversion(Time.TimeUnit.Week, Time.TimeUnit.Day);
            case Time.TimeUnit.Month ->
                getMonthly()[timestep] * (float)Time.TimeUnit.getConversion(Time.TimeUnit.Month, Time.TimeUnit.Day);
            case Time.TimeUnit.Year ->
                getAnnual()[timestep] * (float)Time.TimeUnit.getConversion(Time.TimeUnit.Year, Time.TimeUnit.Day);
            default ->
                0;
        };
    }

    /**
     * For pulling an array of hourly data will trigger a calculation run if
     * needed
     *
     * @return array containing hours + 1, day 0 being initial conditions
     */
    public float[] getHourly() {
        if (calculated) {
            return hourly;
        }
        calculate();
        return hourly;
    }

    /**
     * For pulling an array of hourly data will trigger a calculation run if
     * needed
     *
     * @param timestep to retrieve, step 0 for initial conditions
     * @return hourly data based on hourly data if available or factored down
     * from next smallest timestep will return 0 if timeunit isnt recognized
     *
     */
    public float getHourly(int timestep) {
        if (timestep == 0) {
            return getHourly()[timestep];
        }
        return switch (this.timeUnit) {
            case Time.TimeUnit.Hour ->
                getHourly()[timestep];
            case Time.TimeUnit.Day ->
                getDaily()[timestep] * (float)Time.TimeUnit.getConversion(Time.TimeUnit.Day, Time.TimeUnit.Hour);
            case Time.TimeUnit.Week ->
                getWeekly()[timestep] * (float)Time.TimeUnit.getConversion(Time.TimeUnit.Week, Time.TimeUnit.Hour);
            case Time.TimeUnit.Month ->
                getMonthly()[timestep] * (float)Time.TimeUnit.getConversion(Time.TimeUnit.Month, Time.TimeUnit.Hour);
            case Time.TimeUnit.Year ->
                getAnnual()[timestep] * (float)Time.TimeUnit.getConversion(Time.TimeUnit.Year, Time.TimeUnit.Hour);
            default ->
                0;
        };
    }

    /**
     * For pulling an array of daily data will trigger a calculation run if
     * needed
     *
     * @return array containing months + 1, month 0 being initial conditions
     */
    public float[] getMonthly() {
        if (calculated) {
            return monthly;
        }
        calculate();
        return monthly;
    }

    /**
     * For pulling an array of daily data will trigger a calculation run if
     * needed
     *
     * @param timestep to retrieve, step 0 for initial conditions
     * @return monthly data based on monthly data if available or factored down
     * from next smallest time step unless time step is annual in which case a
     * conversion will be used
     */
    public float getMonthly(int timestep) {
        if (timestep == 0) {
            return getMonthly()[timestep];
        }
        return switch (this.timeUnit) {
            case Time.TimeUnit.Hour ->
                getMonthly()[timestep * (int) (Time.TimeUnit.getConversion(Time.TimeUnit.Hour, Time.TimeUnit.Month))];
            case Time.TimeUnit.Day ->
                getMonthly()[timestep * (int) (Time.TimeUnit.getConversion(Time.TimeUnit.Day, Time.TimeUnit.Month))];
            case Time.TimeUnit.Week ->
                getMonthly()[timestep * (int) (Time.TimeUnit.getConversion(Time.TimeUnit.Year, Time.TimeUnit.Month))];
            case Time.TimeUnit.Month ->
                getMonthly()[timestep];
            case Time.TimeUnit.Year ->
                getAnnual()[timestep] * (float)Time.TimeUnit.getConversion(Time.TimeUnit.Year, Time.TimeUnit.Month);
            default ->
                0;
        };
    }

    /**
     * For pulling an array of weekly data will trigger a calculation run if
     * needed
     *
     * @return array containing weeks + 1, week 0 being initial conditions Array
     * length will be 1 if time unit is larger then weekly
     */
    public float[] getWeekly() {
        if (calculated) {
            return weekly;
        }
        calculate();
        return weekly;
    }

    /**
     * For pulling an array of daily data will trigger a calculation run if
     * needed
     *
     * @param timestep to retrieve, step 0 for initial conditions
     * @return value out of array representing weekly result
     */
    public float getWeekly(int timestep) {
        if (timestep == 0) {
            return getWeekly()[timestep];
        }
        return switch (this.timeUnit) {
            case Time.TimeUnit.Hour ->
                getWeekly()[timestep * (int) (Time.TimeUnit.getConversion(Time.TimeUnit.Hour, Time.TimeUnit.Week))];
            case Time.TimeUnit.Day ->
                getWeekly()[timestep * (int) (Time.TimeUnit.getConversion(Time.TimeUnit.Day, Time.TimeUnit.Week))];
            case Time.TimeUnit.Week ->
                getWeekly()[timestep];
            case Time.TimeUnit.Month ->
                getMonthly()[timestep] * (float)Time.TimeUnit.getConversion(Time.TimeUnit.Month, Time.TimeUnit.Week);
            case Time.TimeUnit.Year ->
                getAnnual()[timestep] * (float)Time.TimeUnit.getConversion(Time.TimeUnit.Year, Time.TimeUnit.Week);
            default ->
                0;
        };
    }


    /**
     * Used to access name useful when plotting
     * @return name of result
     */
    public String getName(){
        return name;
    }

    /**
     * Gets the time step unit for the result
     *
     * @return
     */
    public Time.TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * gets the unit of measure for the result
     *
     * @return
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * used for setting name post construction
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns size of array based on timeUnit
     *
     * @param timeUnit to get size for, if time unit is less then base units 
     * then size will be 1
     */
    public int size(Time.TimeUnit timeUnit) {
        return switch (this.timeUnit) {
            case Time.TimeUnit.Hour ->
                hourly.length;
            case Time.TimeUnit.Day ->
                daily.length;
            case Time.TimeUnit.Week ->
                weekly.length;
            case Time.TimeUnit.Month ->
                monthly.length;
            case Time.TimeUnit.Year ->
                annual.length;
            default ->
                0;
        };
    }


    
    
   
    /**
     * Generates tab delimited string of daily results
     * @return 
     *
    public String toTabbedStringDaily(){
        StringBuilder resultString = new StringBuilder();
        resultString.append(name);
        resultString.append("\t");
        resultString.append("(");
        resultString.append(units);
        resultString.append("/day)");
        resultString.append("\t");
        for (int i = 0; i < daily.length; i++){
            resultString.append("\t");
            resultString.append(daily[i]);
        }
        return resultString.toString();
    }
    
    /**
     * Generates tab delimited string of monthly results
     * @return 
     *
    public String toTabbedStringMonthly(){
        if (!calculated){
            calculate();
        }
        StringBuilder resultString = new StringBuilder();
        resultString.append(name);
        resultString.append("\t");
        resultString.append("(");
        resultString.append(units);
        resultString.append("/month)");
        resultString.append("\t");
        for (int i = 0; i < monthly.length; i++){
            resultString.append("\t");
            resultString.append(monthly[i]);
        }
        return resultString.toString();
    }
    
    /**
     * Generates tab delimited string of Annual Results
     * @return 
     *
     public String toTabbedStringAnnual(){
        if (!calculated){
            calculate();
        }
        StringBuilder resultString = new StringBuilder();
        resultString.append(name);
        resultString.append("\t");
        resultString.append("(");
        resultString.append(units);
        resultString.append("/year)");
        resultString.append("\t");
        for (int i = 0; i < annual.length; i++){
            resultString.append("\t");
            resultString.append(annual[i]);
        }
        return resultString.toString();
    }
    */
}
