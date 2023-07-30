/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.result;

/**
 *
 * @author Alex
 */
public abstract class Result {

    public double[] daily; // this will be a memory hog, may switch to storing as int or shifted int if memory becomes a limitation
    public double[] monthly; // Any value more then daily is stored as int to save memory
    public double[] annual;
    public String name;
    public String units;
    boolean calculated; 

    Result(int totalDays, String inname, String units){
        daily = new double[totalDays +1]; //Timestep 0 will be used to hold initialiation values so + 1 is needed to get to end;
        for (int i = 0; i < daily.length; i++){
            daily[i] = 0;
        }
        int years = totalDays/365 + 1;
        int months = years * 12;
        monthly = new double[months + 1];
        annual = new double[years +1];
        name = inname;
        this.units = units;
        calculated = false;
    }
    abstract void calculate(); 
   
    
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
}
