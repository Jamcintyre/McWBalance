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
import com.mcwbalance.measure.Volume;
import com.mcwbalance.settings.Limit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author alex
 */
public class ResultTest {
    
    public ResultTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of constructor to ensure arrays are generated large enough to store
     * number of timesteps;
     * @param timesteps number of timesteps
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 365, 366, 1000, 5000, 50000, 500000, Integer.MAX_VALUE}) 
    public void testResult_Const_Hour(int timesteps){
        System.out.println("testResult_Const_TimeUnit.Hour");
        Result instance = new Result("Test", timesteps, Time.TimeUnit.Hour, Volume.VolumeUnit.m3, true);
        
        int hoursL = instance.getHourly().length;
        int daysL = instance.getDaily().length;
        int weeksL = instance.getWeekly().length;
        int monthsL = instance.getMonthly().length;
        int annualL = instance.getAnnual().length;
        
        System.out.println("timesteps: " + timesteps
                        + " hours: " + hoursL
                        + " days: " + daysL
                        + " weeks: " + weeksL
                        + " months: " + monthsL
                        + " years: " + annualL
                );
        
         if(timesteps > Limit.MAX_DURATION){
            timesteps = Limit.MAX_DURATION;
        }
        assertEquals(timesteps + 1, hoursL);
        assertEquals(timesteps/24 + 2, daysL);
        assertTrue(weeksL >= (int)((timesteps/24)/7 + 1));
        assertTrue(monthsL >= (int)((timesteps/24/365)*12 + 1));
        assertTrue(annualL >= (int)(timesteps/24/365 + 1));  
    }
    
    /**
     * Test of constructor to ensure arrays are generated large enough to store
     * number of time steps;
     *
     * @param timesteps number of time steps
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 365, 366, 1000, 5000, 50000, 500000, Integer.MAX_VALUE}) 
    public void testResult_Const_Day(int timesteps){
        System.out.println("testResult_Const_TimeUnit.Day");
        Result instance = new Result("Test", timesteps, Time.TimeUnit.Day, Volume.VolumeUnit.m3, true);
        
        int hoursL = instance.getHourly().length;
        int daysL = instance.getDaily().length;
        int weeksL = instance.getWeekly().length;
        int monthsL = instance.getMonthly().length;
        int annualL = instance.getAnnual().length;
        
        System.out.println("timesteps: " + timesteps
                        + " hours: " + hoursL
                        + " days: " + daysL
                        + " weeks: " + weeksL
                        + " months: " + monthsL
                        + " years: " + annualL
                );
        
         if(timesteps > Limit.MAX_DURATION){
            timesteps = Limit.MAX_DURATION;
        }
        assertEquals(1, hoursL);
        assertEquals(timesteps + 1, daysL);
        assertTrue(weeksL >= (int)((timesteps)/7 + 1));
        assertTrue(monthsL >= (int)((timesteps/365)*12 + 1));
        assertTrue(annualL >= (int)(timesteps/365 + 1));  
    }
    
    /**
     * Test of constructor to ensure arrays are generated large enough to store
     * number of time steps;
     *
     * @param timesteps number of time steps
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 365, 366, 1000, 5000, 50000, 500000, Integer.MAX_VALUE}) 
    public void testResult_Const_Week(int timesteps){
        System.out.println("testResult_Const_TimeUnit.Week");
        Result instance = new Result("Test", timesteps, Time.TimeUnit.Week, Volume.VolumeUnit.m3, true);
        
        int hoursL = instance.getHourly().length;
        int daysL = instance.getDaily().length;
        int weeksL = instance.getWeekly().length;
        int monthsL = instance.getMonthly().length;
        int annualL = instance.getAnnual().length;
        
        System.out.println("timesteps: " + timesteps
                        + " hours: " + hoursL
                        + " days: " + daysL
                        + " weeks: " + weeksL
                        + " months: " + monthsL
                        + " years: " + annualL
                );
        
         if(timesteps > Limit.MAX_DURATION){
            timesteps = Limit.MAX_DURATION;
        }
        assertEquals(1, hoursL);
        assertEquals(1, daysL);
        assertEquals(timesteps + 1, weeksL);
        assertTrue(monthsL >= (int)((timesteps*7/365)*12 + 1));
        assertTrue(annualL >= (int)(timesteps*7/365 + 1));  
    }
    
        /**
     * Test of constructor to ensure arrays are generated large enough to store
     * number of time steps;
     *
     * @param timesteps number of time steps
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 12, 13, 11, 5000, 50000, 500000, Integer.MAX_VALUE}) 
    public void testResult_Const_Month(int timesteps){
        System.out.println("testResult_Const_TimeUnit.Month");
        Result instance = new Result("Test", timesteps, Time.TimeUnit.Month, Volume.VolumeUnit.m3, true);
        
        int hoursL = instance.getHourly().length;
        int daysL = instance.getDaily().length;
        int weeksL = instance.getWeekly().length;
        int monthsL = instance.getMonthly().length;
        int annualL = instance.getAnnual().length;
        
        System.out.println("timesteps: " + timesteps
                        + " hours: " + hoursL
                        + " days: " + daysL
                        + " weeks: " + weeksL
                        + " months: " + monthsL
                        + " years: " + annualL
                );
        
         if(timesteps > Limit.MAX_DURATION){
            timesteps = Limit.MAX_DURATION;
        }
        assertEquals(1, hoursL);
        assertEquals(1, daysL);
        assertEquals(1, weeksL);
        assertEquals(timesteps + 1, monthsL);
        assertTrue(annualL >= (int)(timesteps/12 + 1));  
    }
    
        /**
     * Test of constructor to ensure arrays are generated large enough to store
     * number of time steps;
     *
     * @param timesteps number of time steps
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 365, 366, 1000, 5000, 50000, 500000, Integer.MAX_VALUE}) 
    public void testResult_Const_Annual(int timesteps){
        System.out.println("testResult_Const_TimeUnit.Year");
        Result instance = new Result("Test", timesteps, Time.TimeUnit.Year, Volume.VolumeUnit.m3, true);
        
        int hoursL = instance.getHourly().length;
        int daysL = instance.getDaily().length;
        int weeksL = instance.getWeekly().length;
        int monthsL = instance.getMonthly().length;
        int annualL = instance.getAnnual().length;
        
        System.out.println("timesteps: " + timesteps
                        + " hours: " + hoursL
                        + " days: " + daysL
                        + " weeks: " + weeksL
                        + " months: " + monthsL
                        + " years: " + annualL
                );
        
         if(timesteps > Limit.MAX_DURATION){
            timesteps = Limit.MAX_DURATION;
        }
        assertEquals(1, hoursL);
        assertEquals(1, daysL);
        assertEquals(1, weeksL);
        assertEquals(1, monthsL);
        assertEquals(timesteps + 1, annualL);  
    }
    
    /**
     * Test of add method, of class Result.
     * adds a set of daily data, will try all arrays
     */
    @ParameterizedTest
    @ValueSource(ints = {1227}) 
    public void testAdd_Hourly(int timesteps) {
        System.out.println("testAdd_Hourly");
        Result instance = new Result("Test", timesteps, Time.TimeUnit.Hour, Volume.VolumeUnit.m3, true);
        for (int i = 0; i < timesteps+1; i++) {
            instance.add(i * 10, i);
        }
        int ts[] = {0, 1, 45, 366, 1227};
        float expResult[] = {0, 10, 450, 3660, 12270};
        float[] hourly = instance.getHourly();

        for (int t = 0; t < ts.length; t++) {
            float res = hourly[ts[t]];
            float exp = expResult[t];
            System.out.println("timestep: " + ts[t] + " Value " + res + " Expected: " + exp);
            assertEquals(res, exp);
        }
    }
    
    /**
     * Test of add method, of class Result.
     * adds a set of daily data, will try all arrays
     */
    @Test
    public void testAdd_Daily() {
        System.out.println("testAdd_Daily");
        Result instance = new Result("Test", 1227, Time.TimeUnit.Day, Volume.VolumeUnit.m3, true);
        for (int i = 0; i < 1227+1; i++) {
            instance.add(i * 10, i);
        }
        int ts[] = {0, 1, 45, 366, 1227};
        float expResult[] = {0, 10, 450, 3660, 12270};
        float[] daily = instance.getDaily();

        for (int t = 0; t < ts.length; t++) {
            float res = daily[ts[t]];
            float exp = expResult[t];
            System.out.println("timestep: " + ts[t] + " Value " + res + " Expected: " + exp);
            assertEquals(res, exp);
        }
    }

    /**
     * Test of calculate method, of class Result.
     * 24*365*5 = 43800
     */
    @Test
    public void testCalculate_Hourly() {
        System.out.println("calculate_hourly");
        int steps = 43800;
        Result instance = new Result("Test", steps, Time.TimeUnit.Hour, Volume.VolumeUnit.m3, true);
        for (int i = 1; i < steps+1; i++) {
            instance.add(i, i);
        }
        instance.calculate();
        int hours[] ={0, 1, 24, 25, 4752};
        for (int h = 0; h < hours.length; h++){
            System.out.println("hour h " + hours[h] + " = "+ instance.getHourly(hours[h]));
        }
        int ts[] ={0, 1, 5, 24, 25, 100, 500};
        for (int t = 0; t < ts.length; t++){
            float day = ts[t] * (float)Time.TimeUnit.getConversion(Time.TimeUnit.Hour, Time.TimeUnit.Day);
            
            System.out.println("timestep h " + ts[t] + " = d: " 
            + day
            + " daily = "+ instance.getDaily(ts[t]));
            
        }
 
    }

    /**
     * Test of getAnnual method, of class Result.
     */
    @Test
    @Disabled
    public void testGetAnnual_int() {
        System.out.println("getAnnual");
        int timestep = 0;
        Result instance = null;
        double expResult = 0.0;
        double result = instance.getAnnual(timestep);
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDaily method, of class Result.
     */
    @Test
    @Disabled
    public void testGetDaily_int() {
        System.out.println("getDaily");
        int timestep = 0;
        Result instance = null;
        double expResult = 0.0;
        double result = instance.getDaily(timestep);
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHourly method, of class Result.
     */
    @Test
    @Disabled
    public void testGetHourly_int() {
        System.out.println("getHourly");
        int timestep = 0;
        Result instance = null;
        double expResult = 0.0;
        double result = instance.getHourly(timestep);
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMonthly method, of class Result.
     */
    @Test
    @Disabled
    public void testGetMonthly_int() {
        System.out.println("getMonthly");
        int timestep = 0;
        Result instance = null;
        double expResult = 0.0;
        double result = instance.getMonthly(timestep);
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWeekly method, of class Result.
     */
    @Test
    @Disabled
    public void testGetWeekly_int() {
        System.out.println("getWeekly");
        int timestep = 0;
        Result instance = null;
        double expResult = 0.0;
        double result = instance.getWeekly(timestep);
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
