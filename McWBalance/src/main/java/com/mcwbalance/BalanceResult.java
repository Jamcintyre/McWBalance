package com.mcwbalance;

public class BalanceResult {

public double[] daily; // this will be a memory hog, may switch to storing as int or shifted int if memory becomes a limitation
public int[] monthly; // Any value more then daily is stored as int to save memory
public int[] annual;
public String name;
public String units;
private boolean sumsCalculated = false;



/**
 * @deprecated 
 * @param totalDays
 * @param inname 
 */
    BalanceResult(int totalDays, String inname){
        daily = new double[totalDays +1]; //Timestep 0 will be used to hold initialiation values so + 1 is needed to get to end;
        for (int i = 0; i < daily.length; i++){
            daily[i] = 0;
        }
        int years = totalDays/365 + 1;
        int months = years * 12;
        monthly = new int[months + 1];
        annual = new int[years +1];
        name = inname;
        units = "cu.m.";
    }
    private void calculateSums(){
        int d;
        int m;
        int y;
        for (d = 1; d < daily.length; d ++){ // day 0 is left out of sums
            m = CalcBasics.getMonth(d);
            y = CalcBasics.getYear(d);
            
            monthly[m] = (int)(monthly[m] + daily[d]);
            annual[y] = (int)(annual[y] + daily[d]);
        }
        sumsCalculated = true;
    }
    
    
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
        if (!sumsCalculated){
            calculateSums();
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
        if (!sumsCalculated){
            calculateSums();
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
