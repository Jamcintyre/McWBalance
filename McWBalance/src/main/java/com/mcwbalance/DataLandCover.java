/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

/**
 *
 * @author amcintyre
 */
public class DataLandCover {
    private String cover;
    private double runoffCoeff[];
    private static final double MIN_COEFF = 0;
    private static final double MAX_COEFF = 1;
    private static final double DEFAULT_COEFF = 1;
    private static final int ARRAY_LENGTH = 12;
    
    DataLandCover(){
        cover = "NEW_COVER";
        runoffCoeff = new double[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i ++){
            runoffCoeff[i] = DEFAULT_COEFF; 
        }
    }
    DataLandCover(String inName){
        cover = inName;
        runoffCoeff = new double[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i ++){
            runoffCoeff[i] = DEFAULT_COEFF; 
        }
    }
    DataLandCover(double coeff){
        cover = "NEW_COVER";
        runoffCoeff = new double[ARRAY_LENGTH];
        if (coeff < MIN_COEFF){
            coeff = MIN_COEFF;
        }else if (coeff > MAX_COEFF){
            coeff = MAX_COEFF;
        }
        for (int i = 0; i < ARRAY_LENGTH; i ++){
            runoffCoeff[i] = coeff; 
        }
    }
    DataLandCover(String inName, double coeff){
        cover = inName;
        runoffCoeff = new double[ARRAY_LENGTH];
        if (coeff < MIN_COEFF){
            coeff = MIN_COEFF;
        }else if (coeff > MAX_COEFF){
            coeff = MAX_COEFF;
        }
        for (int i = 0; i < ARRAY_LENGTH; i ++){
            runoffCoeff[i] = coeff; 
        }
    }
    DataLandCover(String inName, double coeff[]){
        cover = inName;
        runoffCoeff = new double[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i ++){
            if (i < coeff.length){
                if (coeff[i] < MIN_COEFF){
                    coeff[i] = MIN_COEFF;
                }else if (coeff[i] > MAX_COEFF){
                    coeff[i] = MAX_COEFF;
                }
                runoffCoeff[i] = coeff[i];
            }else{
                runoffCoeff[i] = DEFAULT_COEFF; 
            }    
        }
    }
    DataLandCover(double coeff[]){
        cover = "NEW_COVER";
        runoffCoeff = new double[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i ++){
            if (i < coeff.length){
                if (coeff[i] < MIN_COEFF){
                    coeff[i] = MIN_COEFF;
                }else if (coeff[i] > MAX_COEFF){
                    coeff[i] = MAX_COEFF;
                }
                runoffCoeff[i] = coeff[i];
            }else{
                runoffCoeff[i] = DEFAULT_COEFF; 
            }
            
        }
    }
    public String getCoverName(){
        return cover;
    }
    public double getMonthlyCoeff(int month){
        return runoffCoeff[month-1];
    }
    public double[] getCoeffArray(){
        return runoffCoeff;
    }
    public void setAllCoeff(double inCoeffArray[]){
        for (int i = 0; i < ARRAY_LENGTH; i ++){
            if (i < inCoeffArray.length){
                if (inCoeffArray[i] < MIN_COEFF){
                    inCoeffArray[i] = MIN_COEFF;
                }else if (inCoeffArray[i] > MAX_COEFF){
                    inCoeffArray[i] = MAX_COEFF;
                }
                runoffCoeff[i] = inCoeffArray[i];
            }else{
                runoffCoeff[i] = DEFAULT_COEFF; 
            }    
        }
    }   
    public void setAllCoeff(double inCoeff){
        if (inCoeff < MIN_COEFF){
            inCoeff = MIN_COEFF;
        }else if (inCoeff > MAX_COEFF){
            inCoeff = MAX_COEFF;
        }
        for (int i = 0; i < ARRAY_LENGTH; i ++){
                runoffCoeff[i] = inCoeff;  
        }
    } 
    public void setCoverName(String name){
        cover = name; 
    }
    public void setSingleCoeff(double inCoeff, int month){
        if (inCoeff < MIN_COEFF){
            inCoeff = MIN_COEFF;
        }else if (inCoeff > MAX_COEFF){
            inCoeff = MAX_COEFF;
        }
        runoffCoeff[month - 1] = inCoeff;  
    } 
}
