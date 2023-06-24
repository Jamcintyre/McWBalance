package com.mcwbalance;

public class BalanceResult {

public double[] daily; // this will be a memory hog, may switch to storing as int or shifted int if memory becomes a limitation
public int[] monthly; // Any value more then daily is stored as int to save memory
public int[] annual;
public String name;

    BalanceResult(int totalDays, String inname){
        daily = new double[totalDays +1]; //Timestep 0 will be used to hold initialiation values so + 1 is needed to get to end;
        int years = totalDays/365 + 1;
        int months = years * 12;
        monthly = new int[months + 1];
        annual = new int[years +1];
        name = inname;
    }
    
}
