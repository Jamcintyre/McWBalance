/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

/**
 *
 * @author amcintyre
 */
public class DataClimate {  // Climate must begin on Jan 1. No Leap years. 
    int[] rain;
    int[] snow;
    int[] precip;
    int[] evap;
    int[] temp;
    int[] melt;
    int size;
    
    
    int[] snowpack; // mm equivelant
    
    
    
    DataClimate(int setsize){
        size = setsize;
        rain = new int[size];
        snow = new int[size];
        precip = new int[size];
        evap = new int[size];
        temp = new int[size];
        melt = new int[size];
        snowpack = new int[size];
    }
    
    public void calcSnowMeltDDM(){
        
        
        
        
    }
}
