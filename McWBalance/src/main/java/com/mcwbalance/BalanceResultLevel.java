/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

/**
 *
 * @author Alex
 */
public class BalanceResultLevel{
    
    public double[] daily; // this will be a memory hog, may switch to storing as int or shifted int if memory becomes a limitation
    public double[] monthly; // Any value more then daily is stored as int to save memory
    public double[] annual;
    public String name;
    public String units;
    private boolean avgsCalculated = false;
    
    BalanceResultLevel(int totalDays, String inname){
       daily = new double[totalDays +1]; //Timestep 0 will be used to hold initialiation values so + 1 is needed to get to end;
        for (int i = 0; i < daily.length; i++){
            daily[i] = 0;
        }
        int years = totalDays/365 + 1;
        int months = years * 12;
        monthly = new double[months + 1];
        annual = new double[years +1];
        name = inname;
        units = "m";
    }
    
    /**
     * NOTE WILLL NEED TO GET THIS TO figure out partial years. 
     */
    private void calculateAvgs(){
        int d;
        int m;
        int y;
        for (d = 1; d < daily.length; d ++){ // day 0 is left out of sums
            m = CalcBasics.getMonth(d);
            y = CalcBasics.getYear(d);
            
            monthly[m] = (int)(monthly[m] + daily[d]);
            annual[y] = (int)(annual[y] + daily[d]);
        }
        
        for (m = 0; m < monthly.length; m ++){
            monthly[m] = monthly[m] / CalcBasics.getDaysInMonth(m);
        }
        for (y = 0; m < annual.length; y ++){
            annual[y] = annual[y] / 365;
        }
        
        avgsCalculated = true;
    }
    
    
    public String toTabbedStringDaily(){
        StringBuilder resultString = new StringBuilder();
        resultString.append(name);
        resultString.append("\t");
        resultString.append("(");
        resultString.append(units);
        resultString.append(")");
        resultString.append("\t");
        for (int i = 0; i < daily.length; i++){
            resultString.append("\t");
            resultString.append(daily[i]);
        }
        return resultString.toString();
    }
    
    
    public String toTabbedStringMonthly(){
        if (!avgsCalculated){
            calculateAvgs();
        }
        StringBuilder resultString = new StringBuilder();
        resultString.append(name);
        resultString.append("\t");
        resultString.append("(");
        resultString.append(units);
        resultString.append(")");
        resultString.append("\t");
        for (int i = 0; i < monthly.length; i++){
            resultString.append("\t");
            resultString.append(monthly[i]);
        }
        return resultString.toString();
    }
    
    
     public String toTabbedStringAnnual(){
        if (!avgsCalculated){
            calculateAvgs();
        }
        StringBuilder resultString = new StringBuilder();
        resultString.append(name);
        resultString.append("\t");
        resultString.append("(");
        resultString.append(units);
        resultString.append(")");
        resultString.append("\t");
        for (int i = 0; i < annual.length; i++){
            resultString.append("\t");
            resultString.append(annual[i]);
        }
        return resultString.toString();
    }
    
}
