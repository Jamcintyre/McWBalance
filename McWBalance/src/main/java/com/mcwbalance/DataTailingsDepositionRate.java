/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

/**
 * TODO - bring in calculation method from DataTable Tailings Depositon rate 
 * add a list class for including the sort and remove doup methods 
 * @author amcintyre
 */
public class DataTailingsDepositionRate {

    /**
     * Model day data[i][0] Max = Maximum Project Duration Min = 1
     */
    private int day;
    private static final int DAY_MAX = ProjSetting.MAX_DURATION;
    private static final int DAY_MIN = 0;
    private static final int DAY_NULL = DAY_MAX + 1;

    /**
     * Rate tonnes/day data[i][1] Min = 0
     */
    private double rate;
    private static final double RATE_MIN = 0;

    /**
     * Solids Content (by mass) data[i][2] Max = 1.00, Min = 0
     */
    private double solidsCont;
    private static final double SOLIDS_CONT_MIN = 0;
    private static final double SOLIDS_CONT_MAX = 1;
    /**
     * Specific gravity data[i][3], between 0 and 22 weight of osmium data[i][3]
     */
    private double sG;
    private static final double SG_MIN = 0;
    private static final double SG_MAX = 22;
    /**
     * final dry settled density in tonnes / m3 data[i][4] Min = 0, Max = 22
     * tonnes/m3 (solids mass of osmium
     */
    private double settledDensity;
    private static final double SETTLED_MIN = 0;
    private static final double SETTTLED_MAX = 22;

    /**
     * Volume of water with the solids out of pipe (cu.m. per day) data [i][5]
     */
    private double waterWithSolids;

    /**
     * Volume of voids per day (cu.m. per day) data [i][6];
     */
    private double volVoids;

    /**
     * Volume of Solids and Voids;
     */
    private double volSolids;

    DataTailingsDepositionRate() {
        day = DAY_NULL;
        rate = RATE_MIN;
        solidsCont = SOLIDS_CONT_MIN;
        sG = SG_MIN;
        settledDensity = SETTLED_MIN;
        waterWithSolids = 0;
        volVoids = 0;
        volSolids = 0;
    }

    DataTailingsDepositionRate(Object[] indata) {
        if (indata.length != 7) {
            System.out.println("DataTailingsDepositionRate SetAll Error, data is not correct length");
        } else {
            day = (int) indata[0];
            rate = (double) indata[1];
            solidsCont = (double) indata[2];
            sG = (double) indata[3];
            settledDensity = (double) indata[4];
            waterWithSolids = (double) indata[5];
            volVoids = (double) indata[6];
            volSolids = (double) indata[7];
        }
    }
    public int getDay(){
        return day;
    }
    public boolean isNull(){
        if(day < 0 || day == DAY_NULL || rate < 0 || sG < 0 || settledDensity < 0){
            return true;
        }
     return false;   
    }
    
    public void setDay(int inday){
        if (inday <= DAY_MIN){
            day = DAY_MIN;
        }
        day = inday; 
    }
    public void setAll(Object[] indata) {
        if (indata.length != 7) {
            System.out.println("DataTailingsDepositionRate SetAll Error, data is not correct length");
        } else {
            day = (int) indata[0];
            rate = (double) indata[1];
            solidsCont = (double) indata[2];
            sG = (double) indata[3];
            settledDensity = (double) indata[4];
            waterWithSolids = (double) indata[5];
            volVoids = (double) indata[6];
            volSolids = (double) indata[7];
        }
    }
;

}
