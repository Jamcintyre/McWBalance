/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

/**
 *
 * @author amcintyre
 */
public class DataTailingsDepositionRateList {
    public int length; 
    public DataTailingsDepositionRate rates[];    
    DataTailingsDepositionRateList(){
        length = 1;
        rates[0] = new DataTailingsDepositionRate();
        rates[0].setDay(0);
    }

    public void sortAssending() {
        DataTailingsDepositionRate swap;
        for (int i = 0; i < length; i++) { // may need to move the i++ and j++, want to repeat the step if needed;
            for (int j = i + 1; j < length; j++) {
                if (rates[j].getDay() < rates[i].getDay()) { // if it matches leave it alone, the doublicate cleaner will deal with it; 
                    swap = rates[j];
                    for (int k = j - 1; k > i - 1; k--) {
                        rates[k + 1] = rates[k];
                    }
                    rates[i] = swap;
                    i = -1; // assumes i++ will bring this back to 0 and loop will restart;
                    break;
                }
            }
        }
    }
    
    public void removeDoupsAndNulls(){
        double cDay;
        for (int i = 0; i < length; i ++){
            cDay = rates[i].getDay();
            for (int j = i + 1; j < length; j ++){
                if (rates[j].getDay() == cDay || rates[j].isNull()){
                    for (int k = j + 1; k < length; k ++){
                        rates[k-1] = rates[k];
                    }
                    length--;
                }
            }
        }
        if (length != rates.length){
            DataTailingsDepositionRate newRates[] = new DataTailingsDepositionRate[length];
             
            for (int i = 0; i < length; i ++){
                newRates[i] = rates[i];
            }
            rates[0].setDay(0); // ensures first day is 0, since will need to lookup rate for day 0
            rates = newRates;
        }    
    }
    /**
     * This method for seeking the correct 
     * @param inDay
     * @return Location of applicable rate in the rates[] array, returns -1 if method fails 
     */
    public int getRateIndex (int inDay){
        for (int i = 0; i < length; i ++){
            if (rates[i].getDay() == inDay){
                return i;
            }
            if (rates[i].getDay() > inDay && i > 0){
                return i-1;
            }
        }
        
        return -1; // 
    }
    
    
    
    
    
}
