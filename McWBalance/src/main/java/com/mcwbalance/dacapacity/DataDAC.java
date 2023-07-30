/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.dacapacity;

import java.io.File;
import java.io.IOException;
/**
 * Plan to migrate this to TableDAC Model... 
 * @deprecated 
 * @author Alex
 */
public class DataDAC {
    int nPoints;
    public double[] elev;
    public int[] area;
    public int[] vol;
    static int MAX_LENGTH = 1000; // maximum length of DAC that will be accepted,  intention is about 1000 or 1500 to allow for 150 m tall TSF 0.1 m increments. 
    static double ELEV_NULL = 9999;
    static int AREA_NULL = -1;
    static int VOL_NULL = -1; 
    
    public DataDAC(int nofPoints){
        nPoints = nofPoints;
        elev = new double[nPoints];
        area = new int[nPoints];
        area[0] = 0; // note program will ignore the first DAC area and storage given as they must be 0;
        vol = new int[nPoints];
        vol[0] = 0; // note program will ignore the first DAC area and storage given as they must be 0;
    }
    
    public void setData (double inElev[], int inArea[], int inVol[]){
        int length;
        double newElev[];
        int newArea[];
        int newVol[];
        if (inElev.length == 0 || inArea.length == 0 || inVol.length == 0){
            length = 1;
        }
        if (inElev.length <= inArea.length && inElev.length <= inVol.length){
            length = inElev.length;
        }else if (inArea.length <= inElev.length && inArea.length <= inVol.length){
            length = inArea.length;
        }else{
            length = inVol.length;
        }
        if (length > MAX_LENGTH){
            length = MAX_LENGTH;
        }  
        newElev = new double[length];
        newArea = new int[length];
        newVol = new int[length];
        for (int i = 0; i < length; i ++){
            newElev[i] = inElev[i];
            newArea[i] = inArea[i];
            newVol[i] = inVol[i];
        }
        elev = newElev;
        area = newArea;
        vol = newVol;
        
        
        sortAssending();
        removeDoupAndNulls();
    }
    public void removeDoupAndNulls(){
        int length = elev.length;
        double cElev;
        for (int i = 0; i < length; i ++){
            cElev = elev[i];
            for (int j = i + 1; j < length; j ++){
                if (elev[j] == cElev || elev[j] == ELEV_NULL || area[j] == AREA_NULL || vol[j] == VOL_NULL){
                    for (int k = j + 1; k < length; k ++){
                        elev[k-1] = elev[k];
                        area[k-1] = area[k];
                        vol[k-1] = vol[k];
                    }
                    length--;
                }
            }
        }
        if (length != elev.length){
            double newElev[] = new double[length];
            int newArea[] = new int[length];
            int newVol[] = new int[length];
            newElev[0] = elev[0];
            newArea[0] = 0;
            newVol[0] = 0;
            for (int i = 1; i < length; i ++){
                newElev[i] = elev[i];
                newArea[i] = area[i];
                newVol[i] = vol[i];
            }
            elev = newElev;
            area = newArea;
            vol = newVol;
        }
    }
    
    
    public void sortAssending(){
        double swapElev;
        int swapArea;
        int swapVol;
        for (int i = 0; i < elev.length; i ++){ // may need to move the i++ and j++, want to repeat the step if needed;
            for (int j = i+1; j < elev.length; j ++){
                if(elev[j] < elev[i]){ // if it matches leave it alone, the doublicate cleaner will deal with it; 
                    swapElev = elev[j];
                    swapArea = area[j];
                    swapVol = vol[j];
                    for (int k = j-1; k > i-1; k --){
                        elev[k+1] = elev[k];
                        area[k+1] = area[k];
                        vol[k+1] = vol[k];
                    }
                    elev[i] = swapElev;
                    area[i] = swapArea;
                    vol[i] = swapVol;
                    i = -1; // assumes i++ will bring this back to 0 and loop will restart;
                    break; 
                }
            }
        }
    }
    
    
    /**
     * Will replace with method to just build the string, the actual saving and naming of the file should happen in the ELMlist so that
     * DAC can be numbered with the element number. 
     * @param inPath 
     */
    public void WriteToFile(String inPath){ // needs more work,  this only creates a file,  must also learn to overwrite / delete existing files 
        try {
            File wfile = new File("ELM" + ".txt");
            if (wfile.createNewFile()){
                System.out.println("File Created " + wfile.getPath() + wfile.getName());
            }
            else{
                System.out.println("File Already Exists");
            }
        } catch (IOException e){
            System.out.println("An Error occured while writing DAC file");
            e.printStackTrace();
        }
        
        
        /// NOT COMPLETED! NEEDS TO ACTUALLY PUT THE CONTENT INTO THE FILE. 

    }
    
    public void setPlaceholder(int totArea, int totVol, double sElv, double eElv){
        elev[0] = sElv;
        elev[nPoints-1] = eElv;
        area[nPoints-1] = totArea;
        vol[nPoints-1] = totVol;
        
        double factor;
        for (int i = 1; i < nPoints - 1; i++){ // may need to circle back i assume this will not overwrite final point;
            factor = i / nPoints; 
            area[i] = (int)(totArea*factor); // casting to int will delete anything past the decimal point
            vol[i] = (int)(totArea*factor);
        }
    }
    public int getVolfromEl(double targetElv){
        double slopeofVol;
        if(targetElv <= elev[0]){
            return 0; // if below the DAC it must return a 0;
        }
        if(targetElv >= elev[nPoints-1]){
            return vol[nPoints-1]; // if DAC is maxed out return max volume
        }
        for (int i = 1; i < nPoints; i++){ // starts search at 1, since index 0 is 0
            if (elev[i] >= targetElv){ // first elevation past the mark
                if (elev[i] == targetElv){
                    return vol[i]; // in case of Exact Match can skip the interpolation
                }
               slopeofVol = (vol[i] - vol[i-1])/(elev[i] - elev[i-1]);
               return (int)(vol[i-1] + (targetElv - elev[i-1])*slopeofVol); // returns interpolated Match
            }   
        }
        return -1; //Should never return -1 unless something is messed. 
    }
    public int getAreafromEl(double targetElv){
        double slopeofarea;
        if(targetElv <= elev[0]){
            return 0; // if below the DAC it must return a 0;
        }
        if(targetElv >= elev[nPoints-1]){
            return area[nPoints-1]; // if DAC is maxed out return max volume
        }
        for (int i = 1; i < nPoints; i++){ // starts search at 1, since index 0 is 0
            if (elev[i] >= targetElv){ // first elevation past the mark
                if (elev[i] == targetElv){
                    return area[i]; // in case of Exact Match can skip the interpolation
                }
               slopeofarea = (area[i] - area[i-1])/(elev[i] - elev[i-1]);
               return (int)(area[i-1] + (targetElv - elev[i-1])*slopeofarea); // returns interpolated Match
            }   
        }
        return -1; //Should never return -1 unless something is messed. 
    }
    public int getAreafromVol(int targetVol){
        double slopeofarea;
        if(targetVol <= vol[0]){
            return 0; // if below the DAC it must return a 0;
        }
        if(targetVol >= vol[nPoints-1]){
            return area[nPoints-1]; // if DAC is maxed out return max volume
        }
        for (int i = 1; i < nPoints; i++){ // starts search at 1, since index 0 is 0
            if (vol[i] >= targetVol){ // first elevation past the mark
                if (vol[i] == targetVol){
                    return area[i]; // in case of Exact Match can skip the interpolation
                }
               slopeofarea = (area[i] - area[i-1])/(vol[i] - vol[i-1]);
               return (int)(area[i-1] + (targetVol - vol[i-1])*slopeofarea); // returns interpolated Match
            }   
        }
        return -1; //Should never return -1 unless something is messed. 
    }
    public double getElfromVol(int targetVol){
        double slopeofelev;
        if(targetVol <= vol[0]){
            return 0; // if below the DAC it must return a 0;
        }
        if(targetVol >= vol[nPoints-1]){
            return elev[nPoints-1]; // if DAC is maxed out return max volume
        }
        for (int i = 1; i < nPoints; i++){ // starts search at 1, since index 0 is 0
            if (vol[i] >= targetVol){ // first elevation past the mark
                if (vol[i] == targetVol){
                    return elev[i]; // in case of Exact Match can skip the interpolation
                }
               slopeofelev = (elev[i] - elev[i-1])/(vol[i] - vol[i-1]);
               return (elev[i-1] + (targetVol - vol[i-1])*slopeofelev); // returns interpolated Match
            }   
        }
        return -1; //Should never return -1 unless something is messed.   
    }
    
}
