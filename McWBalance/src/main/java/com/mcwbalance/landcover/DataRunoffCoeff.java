/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.landcover;

import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.settings.Limit;

/**
 * @author amcintyre
 * @deprecated Replaced with TableRunoffCoefficients model
 */
public class DataRunoffCoeff {
    int nLandCovers;
    static final int MAX_COVERS = Limit.MAX_LAND_COVERS;
    DataLandCover landCovers[];
    
    DataRunoffCoeff(int nCovers){
        if(nCovers > MAX_COVERS){
            nCovers = MAX_COVERS;
        }else if(nCovers < 1){
            nCovers = 1; 
        }
        
        landCovers = new DataLandCover[nCovers];
        for (int i = 0; i < nCovers; i ++){
            landCovers[i] = new DataLandCover(String.valueOf("NEW_COVER_" + i)); 
        }
        nLandCovers = nCovers; 
    }
    public void addLandCover(String inName, double inCoeffArray[]){
        if (getIndex(inName) == -1){
            inName = (inName + "_Copy");
        }
        DataLandCover newlandCovers[] = new DataLandCover[nLandCovers + 1];
        for (int i = 0; i < nLandCovers; i ++){
            newlandCovers[i] = landCovers[i];
        }
        newlandCovers[nLandCovers] = new DataLandCover(inName, inCoeffArray);
        landCovers = newlandCovers; 
    }
    public void removeLandCover(int index){
        if (index >=0 && index < nLandCovers -1){
            DataLandCover newlandCovers[] = new DataLandCover[nLandCovers - 1];
        
            for (int i = 0; i < index; i ++){
                newlandCovers[i] = landCovers[i];
            }
            for (int i = index; i < nLandCovers; i ++){
                newlandCovers[i] = landCovers[i+1];
            }
            landCovers = newlandCovers; 
        }
    }
    
    
    /**
     * 
     * @param inName
     * @return returns -1 if name is not found, otherwise returns the first instance of the name matching
     */
    public int getIndex(String inName){
        for (int i = 0; i < landCovers.length; i++){
            if (inName == landCovers[i].getCoverName()){
                return i;
            }
        }
        return -1;
    }
    
}
