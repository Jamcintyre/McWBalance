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

/**
 * For operations involving time units , and conversions from
 * say hours to days, or day to Julian day
 * 
 * @author alex
 */
public class Time {
    /**
     * Units of time measure allowed in this model
     */
    public static enum TimeUnit implements Unit{
        /**
         * 1 hour
         */
        Hour("Hour",1),
        /**
         * 24 hour day
         */
        Day("Day",24),
        /**
         * 7 day week
         */
        Week("Week",168),
        /**
         * 1/12 of a 365 day year;
         */
        Month("Month",730),
        /**
         * non leap year 365 days
         */
        Year("Year",8760);
        
        private final String desc;
        private final int hours;
        
        private TimeUnit(String desc, int hours){
            this.desc = desc;
            this.hours = hours;
            
        }
 
        /**
         * For getting a conversion factor from one unit to another Double
         * precision in water balance is fine
         *
         * Method is overly simple, and is only here to minimize risk of flipped
         * operators and accidental conversion the wrong way.
         *
         * @param from base input units
         * @param to units to convert to
         * @return value to multiply the from value to get the to value
         */
        public static double getConversion(TimeUnit from, TimeUnit to) {
            return to.hours / from.hours;
        }
        
        /**
         * Used for getting the bracketed unit description, i.e. (days) or
         * (hr).
         *
         * @return bracketed descriptor
         */
        @Override
        public String getBracketedDesciptor() {
            return "("+desc+")";
        }

        /**
         * Used for getting the non bracketed unit description, i.e. days or
         * hrs
         *
         * @return
         */
        @Override
        public String getDesciptor() {
            return desc;
        }
        
        
        

        /**
         * Used for scaling time steps, assumes 1 year = 365 days and that 1
         * month = 1/12 of a year
         *
         * @return number of hours in the time unit
         */
        public int getHours() {
            return hours;
        }
                
        /**
         * same as value of just ignores the case, note values are not plural
         * i.e. Hour  not hours or hrs, Day or day not days
         * @param timeunit Hour, HOUR, hour or similar not plural string
         * @return 
         */
        public static TimeUnit valueOfIngoreCase(String timeunit) {
            for (TimeUnit tu : TimeUnit.values()) {
                if (tu.name().equalsIgnoreCase(timeunit)) {
                    return tu;
                }
            }
            return null;
        }
    }
    
    /**
     * Months to use in model, note no leap year allowance
     */
    public static enum Month{
        /**
         * January 31 days, Month 1
         */
        Jan (1,31),
        /**
         * February 28 days, no leap years, Month 2
         */
        Feb (2,28),
        /**
         * March 31 days, Month 3
         */
        Mar (3,31),
        /**
         * April 30 days, Month 4
         */
        Apr (4,30),
        /**
         * May 31 days, Month 5
         */
        May (5,31),
        /**
         * June 30 days, Month 6
         */
        Jun (6,30),
        /**
         * July 31 days, Month 7
         */
        Jul (7,31),
        /**
         * August 31 days, Month 8
         */
        Aug (8,31),
        /**
         * September 30 days, Month 9
         */        
        Sep (9,30),
        /**
         * October 31 days, Month 10
         */
        Oct (10,31),
        /**
         * November 30 days, Month 11
         */
        Nov (11,30),
        /**
         * December 31 days, Month 12
         */
        Dec (12,31);
        
        private final int days;
        private final int number;
        
        private Month (int number, int days){
            this.days = days;
            this.number = number;
        }
        
        /**
         * For converting month to Julian Day
         * @param month int value of month from 1 through 12; month 13 should act as 1, 14 as 2 etc.. 
         * @return the first Julian Day of a month ignoring leap years
         */
        public static int toJulianDay(int month){
            int day = 1;
            month -= 12*(int)(month/12);
            
            for (int m = 1; m < month; m++){
                day += valueOf(m).days;
            }
            return day;
        }
        
        /**
         * Takes in an english month name, looks at first 3 characters ignoring
         * case and returns matching enum value
         * @param month name of month i.e. JAN, jan, Jan, January all accepted
         * @return 
         */
        public static Month valueOfIgnoreCase(String month){
            for (Month m : Month.values()) {
                if (m.name().equalsIgnoreCase(month.substring(0, 2))) {
                    return m;
                }
            }
            return null;
        }
        
        /**
         * Picks matching month from integer value, allows rollover i.e.
         * month 1 = month 13 = Jan; 
         * @param month months assuming month 1 is January;
         * @return returns a month value or null if value below 1 is provided
         */
        public static Month valueOf(int month){
            if(month > 12){
                month = month - 12*(int)(month/12);
            }
            if(month > 0){
                for (Month m : Month.values()){
                    if(m.number == month){
                        return m;
                    }
                }
            }
            return null;
        }
        
        /**
         * assumes all years are 365 days, and that day count starts at 1 on January 1
         * @param day int value of 1 or above
         * @return month between Jan and Dec or null if month is not matched (i.e. 0 value or day 366
         */
        public static Month valueOfDay(int day){
            if(day > 365){
                day = day - 365*(int)(day/365);
            }
            
            for (Month m : Month.values()){
                if(m.days >= day){
                    return m;
                }
                day += m.days;
            }
            return null;
        }
        
        /**
         * converts month to int value
         * @return value from 1 to 12 representing the month 
         */
        public int getNumber(){
            return number;
        }
        
        /**
         * converts month to int value of days
         * @return number of days in month, ignores leap years
         */
        public int getDays(){
            return days;
        }
        
    }
    
    /**
     * Method for trimming years off of and input day to get julian day of 
     * final year
     * assumes 365 days per year, no leap years
     * @param day model day any number above 1, note that start day will need to be
     * added or subtracted outside this method if Day 1 is not jan 1. 
     * @return value between 1 and 365, unless input day is less then 1, in which
     * case 0 is returned
     */
    public static int getJulianDayFromDay(int day){
        if(day < 1){
            return 0;
        }
        return day - 365*(getYearFromDay(day) -1);
    }
    
    /**
     * Method for determining which year an input day is from
     * assumes 365 days per year, no leap years
     * @param day number above 1
     * @return year starting at 1 and counting up, returns 0 for invalid entry 
     * below 1;
     */
    public static int getYearFromDay(int day){
        if(day < 1){
            return 0;
        }
        return day/365 + 1; 
    }
}
