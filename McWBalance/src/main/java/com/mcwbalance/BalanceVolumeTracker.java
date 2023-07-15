/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

/**
 * @deprecated 
 * @author amcintyre
 */
public class BalanceVolumeTracker {
    public String name;
    int[] volume;
    
    BalanceVolumeTracker(int totalDays, String inname){
        name = inname;
        volume = new int[totalDays];
    }
}
