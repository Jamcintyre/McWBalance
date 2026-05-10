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
package com.mcwbalance.measure;


import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author alex
 */
public class TimeTest {
    
    public TimeTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }

    /**
     * Test of getJulianDayFromDay method, of class Time.
     */
    @org.junit.jupiter.api.Test
    public void testGetJulianDayFromDay() {
        System.out.println("getJulianDayFromDay");
        
        int day[] = {0, 1, 45, 366, 367, };
        int expResult[] = {0, 1, 45, 1, 2};
        
        for (int i = 0; i < day.length; i++){
            int res = Time.getJulianDayFromDay(day[i]);
            System.out.println("day: " + day[i] + " JDay: "+ res + " Expected: " + expResult[i]);
            assertEquals(expResult[i], res);
        }
    }

    /**
     * Test of getYearFromDay method, of class Time.
     */
    @org.junit.jupiter.api.Test
    public void testGetYearFromDay() {
        System.out.println("getYearFromDay");
        int day[] = {0, 1, 45, 366, 367};
        int expResult[] = {0, 1, 1, 2, 2};
        for (int i = 0; i < day.length; i++){
            int res = Time.getYearFromDay(day[i]);
            System.out.println("day: " + day[i] + " JDay: "+res + " Expected: " + expResult[i]);
            assertEquals(expResult[i], res);
        }
    }
    /**
     * Test of Month.toJulianDay method, of class Time.
     */
    @org.junit.jupiter.api.Test
    public void testMonthtoJulianDay(){
        System.out.println("Month.toJulianDay");
        int month[] = {1, 2, 3, 4, 5, 16};
        int expResult[] = {1, 32, 60, 91, 121, 91};
        for (int i = 0; i < month.length; i++){
            int res = Time.Month.toJulianDay(month[i]);
            System.out.println("month: " + month[i] + " JDay: "+res + " Expected: " + expResult[i]);
            assertEquals(expResult[i], res);
        }
    }
    
}
