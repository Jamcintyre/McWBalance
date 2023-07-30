
package com.mcwbalance.landcover;
/**
 * 
 * @author Alex
 */
public class DataCatchment {
    int nPoints;
    int[] time;
    int[] area;
    String LandCover;
    
    DataCatchment(int numberofPoints){ 
        nPoints = numberofPoints;
        time = new int[nPoints];
        area = new int[nPoints];
        LandCover = "none";        
    }
    
    public int getArea(int timestep){
        if(timestep < time[0]){
            return 0;
        }
        if (timestep > time[nPoints -1]){ // doesn't need else as statement above is a return
            return 0;
        }
        for(int i = 0; i < nPoints; i++){
            if (timestep >= time[i]){
                return area[i];
            }
        }
        return -1; // not found
    }
    
    
}
