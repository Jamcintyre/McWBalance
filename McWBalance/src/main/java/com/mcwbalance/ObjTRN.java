
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
    
    //Misc Limits
    private static final int MAX_READLOOP_ITERATIONS = 100; // sets a limit in event inData has too many lines
    private static final int MIN_FILE_LENGTH = 13; // this is the minimum number of lines for a save string to be completed
    
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
        hitBox = new Rectangle(x,y,FlowChartCAD.TRN_BOX_WIDTH,FlowChartCAD.TRN_BOX_HEIGHT);
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
        pumpRateDay[0] = 0;
        pumpRateCount = 1;
        
        stateTime[0] = 0;
        state[0] = "ACTIVE";
        
        
        plotVolperDay = 0.0;
        plotVolperHr = 0.00;
        plotVolperAnnum = 0; 

    }
        ObjTRN(int inX, int inY, int number){
        x = inX;
        y = inY;
        hitBox = new Rectangle(x,y,FlowChartCAD.TRN_BOX_WIDTH,FlowChartCAD.TRN_BOX_HEIGHT);
        hitBox.setLocation(inX - hitBox.getSize().width/2, inY - hitBox.getSize().height/2); // centers the hitbox
        isSelected = false;
        objname = "NEW TRANSFER " + number;
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
        pumpRateDay[0] = 0;
        pumpRateCount = 1;
        
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
    
    public double getMaxPumpRate(int day){
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
    
    public double getPreferredRate(int day, String caller, double inflowSurplus, double outflowSurplus){
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
        double out = 0;
        switch (subType) {
            case "FIXED RATE PUMPING" -> // transfer rate based on pump rates // these will be first to solve
                out = pumpCapacity;
            case "ON DEMAND SUPPLY" -> {
                if (outflowSurplus >= 0) {
                    out = 0;
                } else if (-outflowSurplus >= pumpCapacity) {
                    out = pumpCapacity;
                } else {
                    out = -outflowSurplus;
                }
            }
            case "ON DEMAND DISCHARGE" -> {
                if (inflowSurplus <= 0) {
                    out = 0;
                } else if (inflowSurplus >= pumpCapacity) {
                    out = pumpCapacity;
                } else {
                    out = inflowSurplus;
                }
            }
            case "OVERFLOW" ->
                out = pumpCapacity;
        }

        return out;
    }
    
    /**
     * Builds a tab delimited string of all key data from the TRN,  inlcudes in
     * and ouflow links
     * @return 
     */
    public StringBuilder getSaveString(){
        String nextLine = System.getProperty("line.separator");// used instead of /n for cross platform compatibility
        StringBuilder saveString = new StringBuilder();
        saveString.append("Transfer Name" + "\t");
        saveString.append(objname);
        saveString.append(nextLine); 
        saveString.append("SubType" + "\t");
        saveString.append(subType);
        saveString.append(nextLine);
        saveString.append("XYCoords" + "\t");
        saveString.append(x);
        saveString.append("\t");
        saveString.append(y);
        saveString.append(nextLine);
        saveString.append("Inflow From" + "\t");
        saveString.append(inObjNumber);
        saveString.append(nextLine);
        saveString.append("Inflow Line" + "\t");
        saveString.append(inSideFrom);
        saveString.append("\t");
        saveString.append(inSideFromOset);
        saveString.append("\t");
        saveString.append(inSideTo);
        saveString.append("\t");
        saveString.append(nextLine);
        saveString.append("Outflow To" + "\t");
        saveString.append(outObjNumber);
        saveString.append(nextLine);
        saveString.append("Outflow Line" + "\t");
        saveString.append(outSideFrom);
        saveString.append("\t");
        saveString.append(outSideTo);
        saveString.append("\t");
        saveString.append(outSideToOset);
        saveString.append("\t");
        saveString.append(nextLine);
        saveString.append("Pump Rate");
        saveString.append(nextLine);
        saveString.append("Time(day)" +"\t"+"Rate(vol/day)");
        saveString.append(nextLine);
        for (int i = 0; i < pumpRateCount; i++){
            saveString.append(pumpTime[i]);
            saveString.append("\t");
            saveString.append(pumpRateDay[i]);
            saveString.append(nextLine);
        }
        saveString.append(ProjSetting.LIST_TERMINATOR);
        saveString.append(nextLine);
        saveString.append("Object State");
        saveString.append(nextLine);
        saveString.append("Time(day)" +"\t"+"State");
        saveString.append(nextLine);
        for (int i = 0; i < stateTime.length; i++){
            saveString.append(stateTime[i]);
            saveString.append("\t");
            saveString.append(state[i]);
            saveString.append(nextLine);
        }
        saveString.append(ProjSetting.LIST_TERMINATOR);
        saveString.append(nextLine);
        return saveString;
    }
    
    /**
     * Populates the element from a tab delmited list of data 
     *
     * @param inData Recieves a string that is formatted identically to the getString method
     */
    public void setFromString(String inData){

        String nextLine = System.getProperty("line.separator");
        String lines[] = inData.split(nextLine);
        if(lines.length < MIN_FILE_LENGTH){
            return;
        }
        
        int lc = 0;
        objname = lines[lc].split("\t")[1];
        lc++;
        subType = lines[lc].split("\t")[1];
        lc++;
        x = Integer.parseInt(lines[lc].split("\t")[1]);
        y = Integer.parseInt(lines[lc].split("\t")[2]);
        lc++;
        inObjNumber = Integer.parseInt(lines[lc].split("\t")[1]);
        lc++;
        inSideFrom = lines[lc].split("\t")[1];
        inSideFromOset = Integer.parseInt(lines[lc].split("\t")[2]);
        inSideTo = lines[lc].split("\t")[3];
        lc++;
        outObjNumber = Integer.parseInt(lines[lc].split("\t")[1]);
        lc++;
        outSideFrom = lines[lc].split("\t")[1];
        outSideTo = lines[lc].split("\t")[2];
        outSideToOset = Integer.parseInt(lines[lc].split("\t")[3]);
        lc++; 
        lc++; // 2 line header
        lc++;
        for (int i = 0; i < MAX_READLOOP_ITERATIONS; i++, lc++){
            if (lines.length <= lc){
                return;
            }
            if (lines[lc].equals(ProjSetting.LIST_TERMINATOR)){
                break; 
            }else if (i < MAX_PUMP_RATES){
                pumpTime[i] = Integer.parseInt(lines[lc].split("\t")[0]);
                pumpRateDay[i] = Double.parseDouble(lines[lc].split("\t")[1]);
                pumpRateCount = i; 
            }
        }
        lc++;
        lc++;
        lc++;
        for (int i = 0; i < MAX_READLOOP_ITERATIONS; i++, lc++){
            if (lines.length <= lc){
                break;
            }
            if (lines[lc].equals(ProjSetting.LIST_TERMINATOR)){
                break; 
            }else if (i < ProjSetting.MAX_STATES){
                stateTime[i] = Integer.parseInt(lines[lc].split("\t")[0]);
                state[i] = lines[lc].split("\t")[1];
            }
        }
        setHitBox();
        
    }
    /**
     * updates object hitbox, called during a setFromString or other
     * 
     */
    public void setHitBox(){
        hitBox = new Rectangle(x,y,FlowChartCAD.TRN_BOX_WIDTH,FlowChartCAD.TRN_BOX_HEIGHT);
        hitBox.setLocation(x - hitBox.getSize().width/2, y - hitBox.getSize().height/2);

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
        ProjSetting.hasChangedSinceSave = true;
    }
}