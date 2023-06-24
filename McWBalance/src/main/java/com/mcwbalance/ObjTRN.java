
package com.mcwbalance;

import java.awt.Rectangle;

public class ObjTRN {// class to catalog properties of a Pipe or other water transfer Mechanism
    public String objname;
    public String subType;
    public int x; // Coordinates if Information Box
    public int y;
    public Rectangle hitBox;
    public boolean isSelected;
    

    //flow direction
    public int inObjNumber;
    public int outObjNumber;
    
    //Plotting parameters for Flow lines, only relevent to GUI...
    public String inSideFrom; // used to set what side the inflow is drawn from off Element
    public int inSideFromOset; // stores offset for plotting so lines don;t cross 
    public String inSideTo; // used to set what side the inflow is drawn to
    public String outSideFrom; // used to set what side the outflow is drawn from on on Transfer
    public String outSideTo;
    public int outSideToOset; // stores offset for plotting so lines don;t cross 
    
    //Plotting Storage Values;
    public double plotVolperDay; // seepage per day may be less then 1 m3 so base units must be double.
    public double plotVolperHr;
    public int plotVolperAnnum;
    
    //Pump Limits
    public static final int MAX_PUMP_RATES = 20; // allows 20 different pumping rates over project duration
    public int[] pumpTime = new int[MAX_PUMP_RATES]; // start time of pump install 
    public double[] pumpRateDay = new double[MAX_PUMP_RATES]; // rate must be in m3 per day
    public int pumpRateCount;
    
    // Note that Results will be stored to a seperate class, that will be Sized and Generated at Solve Time
    /**
     * Used to select what side the flow arrow gets drawn from
     */
    public static String[] objSidesAllowed = { 
        "RIGHT",
        "LEFT",
        "TOP",
        "BOTTOM"
    };
    /**
     * Used to select calculation menthod for the transfer
     */
    public static String[] objSubTypesAllowed = { // Note list also exitss in IconLibrary
        "FIXED RATE PUMPING", // transfer rate based on pump rates // these will be first to solve
        "ON DEMAND SUPPLY", // transfer rate based on pump rate and only on if reciever is in defict during previous day;
        "ON DEMAND DISCHARGE", // transfer rate based on pump rate and only on if supplier is in surplus previous day; 
        "OVERFLOW"
            
    };
    /**
     * Used primarily for plotting purposes in the flowsheet
     */
    public static String[] tRNStatesAllowed ={
        "ACTIVE",
        "INACTIVE",
        "DASHED",
        "",
    };
    
    public int stateTime[] = new int[ProjSetting.MAX_STATES];
    public String state[] = new String[ProjSetting.MAX_STATES];

    
    ObjTRN(){
        x = 0;
        y = 0;
        hitBox = new Rectangle(x,y,60,60);
        isSelected = false;
        objname = "None";
        subType = "Pump";
        inObjNumber = -1;
        inSideFrom = "TOP";
        inSideFromOset = 0;
        inSideTo = "RIGHT";
        outObjNumber = -1;
        outSideFrom = "LEFT";
        outSideTo = "TOP";
        outSideToOset = 0;
        
        pumpTime[0] = 0; 
        pumpRateDay[0] = 100;
        pumpRateCount = 0;
        
        stateTime[0] = 0;
        state[0] = "ACTIVE";
        
        
        plotVolperDay = 0.0;
        plotVolperHr = 0.00;
        plotVolperAnnum = 0; 

    }
    
    
    
    /**
     * Calculates hourly and daily flow rates, and assignes the provided values to plotVolperDay, PlotVolperHr, and PlotVolPerAnnum. This is indended for use in the
     * flowchart plotting part of the code. the indention is that these values will be picked from presolved results. 
     * @param volPerDay 
     */
    public void setPlotValues(double volPerDay){
        plotVolperDay = volPerDay;
        plotVolperHr = volPerDay/24;
        plotVolperAnnum = (int)volPerDay*365; 
    }
    
    public double GetMaxPumpRate(int day){
        if(day < pumpTime[0]){
            return 0;
        }
        for (int i = 0; i < MAX_PUMP_RATES; i++){
            if(day >= pumpTime[i]){
                return pumpRateDay[i];
            }
        }
        return -1;
    }
    
    public double GetPreferredRate(int day, String caller, double inflowSurplus, double outflowSurplus){
        double pumpCapacity = 0; 
        
        if(day < pumpTime[0]){
            return 0; // no need to proceed if transfer capacity is 0,  note even spillways will need a capacity assigned
        }
        for (int i = 0; i < MAX_PUMP_RATES; i++){
            if(day >= pumpTime[i]){
                if (pumpRateDay[i] == 0){
                    return 0;
                } 
                pumpCapacity = pumpRateDay[i];
            }
        }
        
        switch(subType){
            case "FIXED RATE PUMPING": // transfer rate based on pump rates // these will be first to solve
                return pumpCapacity;
            case "ON DEMAND SUPPLY":
                if(outflowSurplus >= 0){
                    return 0;
                }
                if (-outflowSurplus >= pumpCapacity){
                    return pumpCapacity;
                }
                return -outflowSurplus;                     
            case "ON DEMAND DISCHARGE":
                if(inflowSurplus <= 0){
                    return 0;
                }
                if (inflowSurplus >= pumpCapacity){
                    return pumpCapacity;
                }
                return inflowSurplus;                     
            case "OVERFLOW":
                return pumpCapacity;
        }
    return 0; 
    }
    /**
     * if ELM object is referenced this method replaces that reference with a "-1" null value. ELMs of higher index values then the rELM are 
     * downshifted 1 space
     * @param rELM Index number of ELM to be removed. 
     */
    public void removeELM(int rELM){
        if(inObjNumber == rELM){
            inObjNumber = -1;
        }else if (inObjNumber > rELM){
            inObjNumber--;
        }
        if(outObjNumber == rELM){
            outObjNumber = -1;
        }else if (outObjNumber > rELM){
            outObjNumber--;
        }
    }
}