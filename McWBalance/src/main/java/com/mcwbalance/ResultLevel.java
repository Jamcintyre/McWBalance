/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

/**
 *
 * @author Alex
 */
public class ResultLevel extends Result{
    
     ResultLevel(int totalDays, String inname){
        super(totalDays, inname, "m");
        
    }
    
    @Override
    public void calculate(){
        int d;
        int m;
        int y;
        for (d = 1; d < daily.length; d ++){ // day 0 is left out of sums
            m = CalcBasics.getMonth(d);
            y = CalcBasics.getYear(d);
            
            monthly[m] = (int)daily[d]; // ensures the last day for each month gets written
            annual[y] = (int)daily[d]; // ensures the last day for each year gets written
        }
        calculated = true;
    }
    
    
    
    
}
