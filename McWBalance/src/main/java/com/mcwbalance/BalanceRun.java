
package com.mcwbalance;

import java.time.LocalDateTime;

public class BalanceRun {

int duration;
int totalsolvetime; // used to track duration of calculation steps
DataClimate climate;
DataRunoffCoeff rc; // used to store project monthly runoff coefficients.
int nLandCovers;




String[] runSolveOrdType; // can be TRN or ELM
int[] runSolveOrdIndex; //index for whichever TRN or ELM is being solved
int nOBJtoSolve; // must match up with solve order list; 

String result; 

    BalanceRun(BalanceRunSetting s){
        result = "Not Solved";
        duration = s.duration;
        climate = s.climate;
        rc = s.rc; // used to store project monthly runoff coefficients.
        runSolveOrdType = s.runSolveOrdType;
        runSolveOrdIndex = s.runSolveOrdIndex;
    }
    
    
    public String solve(ObjTRNList tRN, ObjELMList eLM){
        result = "Solve Started"; // Defaults success to Solved, errors in solve loop will return if needed;
        int obj; 
        int i;
        int j;
        int day;
        int dayofyear;
        int sodpondArea;
        int sodTotalVol; // tracker for start of Day Volume used for Pond area calcs
        int month;
        double rainandmelt;
        double totaler;
        int runArea;
        int pondAreaSubtraction = 0;
        
        String resultName; 
        
        for (i = 0; i < tRN.count; i++){
            resultName = tRN.tRNs[i].objname; 
            tRN.tRNs[i].result = new BalanceResult(duration, resultName);
        }
        
        
        
        for (day = 1; day < duration; day++){ // must start at 1 since solver will need previous days numbers to work;
            // set month;...
            month = CalcBasics.getMonth(day);
            
            
            for (i = 0; i < nOBJtoSolve; i++){
                obj = runSolveOrdIndex[i];
                switch(runSolveOrdType[i]){
                    case"TRN": // Transfers solved by themselves assume max pumping rate. otherwise must be resolved by element; 
                        tRN.tRNs[i].result.daily[day] = tRN.tRNs[obj].getMaxPumpRate(day);
                        
                        // if Contains Solids need to add a tonnage of solids
                        
                        
                        break;
                    case "ELM":

                        // resets Pond Areas
                        sodpondArea = 0; 
                        if(eLM.eLMs[obj].hasStorage){
                            sodTotalVol = totalVol[eLM.eLMs[obj].indexStorageTracker].volume[day-1]; 
                            sodpondArea = eLM.eLMs[obj].dAC.getAreafromVol(duration);
                        }
                       
                        // Direct Precip and Evaporation
                        if(eLM.eLMs[obj].hasStorageEvapandPrecip){
                            directprecip[eLM.eLMs[obj].indexStorageTracker].daily[day] = climate.precip[day]*sodpondArea/1000;
                            evaporation[eLM.eLMs[obj].indexStorageTracker].daily[day] = climate.evap[day]*sodpondArea/1000;
                        }
                        // Upstream Runoff
                        if(eLM.eLMs[obj].hasCatchment){
                            totaler = 0;
                            rainandmelt = climate.rain[day] +climate.melt[day];
                            pondAreaSubtraction = sodpondArea;
                            for (j = 0; j < nLandCovers; j++){
                                if (pondAreaSubtraction >= eLM.eLMs[obj].Catchment[j].getArea(day)){ // runoff area needs to be trimmed down to not double count the pond
                                    runArea = 0;     
                                }else if (pondAreaSubtraction > 0){
                                    runArea = eLM.eLMs[obj].Catchment[j].getArea(day);
                                    pondAreaSubtraction = pondAreaSubtraction - runArea;
                                }else{
                                    runArea = eLM.eLMs[obj].Catchment[j].getArea(day);
                                }
                                runoff[eLM.eLMs[obj].indexRunoffTracker][j].daily[day] = runArea * rainandmelt * rc.landCovers[j].getMonthlyCoeff(month) / 1000;
                                totaler = totaler + runoff[eLM.eLMs[obj].indexRunoffTracker][j].daily[day];
                            }
                            runoff[eLM.eLMs[obj].indexRunoffTracker][nLandCovers + 1].daily[day] = totaler; 
                            
                        }
                        
                        // Seepage  // can be handeled as a manditory transfer if varies by time. 
                        
                        // Solids Storage
                        
                                                
                        // Void Losses
                        
                        // Manditory Inflow Transfers

                        // Manditory Outflow Transfers
                        
                        // Optional Inflow Transders
                        
                        // Optional Ouflow Transfers
                        
                        
                        break;
                            
                }
                
                
                
            }
        
        
        
        }
        
        
        
        
        
        
        
        // Returns 
        LocalDateTime now = LocalDateTime.now();
        result = "Solved " + ProjSetting.fmtdt.format(now);
        return result; 
    }
    
    
    /**
     * currently written as a test only will need 2 versions, version 1 will be all the results, version 2 will be a functional water balance as a 
     * fall back in event the user needs to customize beyond what this software will be able to manage. 
     * will need to figure out how to compile and include appachi POI, 
     */
    public void writeXLSX(){
       // XSSFWorkbook workbook = new XSSFWorkbook();
        
        
    }
    
    public String getResultTabbedString(){
        StringBuilder resultString = new StringBuilder();
        
        return resultString.toString();
    }
    
   
    
}
