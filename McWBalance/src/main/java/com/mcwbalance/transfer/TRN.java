
package com.mcwbalance.transfer;


import com.mcwbalance.flowchart.FlowChartCAD;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.result.ResultFlow;
import com.mcwbalance.settings.Limit;
import com.mcwbalance.settings.Preferences;
import com.mcwbalance.util.Direction;
import com.mcwbalance.util.Direction.Side;
import java.awt.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TRN {// class to catalog properties of a Pipe or other water transfer Mechanism
    
    Direction sides;
    
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
    public Side inSideFrom; // used to set what side the inflow is drawn from off Element
    public int inSideFromOset; // stores offset for plotting so lines don;t cross 
    public Side inSideTo; // used to set what side the inflow is drawn to
    
    /**
     * 5 normal, 9 highest, 1 lowest
     */
    int inflowPriority;
    
    public Side outSideFrom; // used to set what side the outflow is drawn from on on Transfer
    public Side outSideTo;
    public int outSideToOset; // stores offset for plotting so lines don;t cross 
    /**
     * 5 normal, 9 highest, 1 lowest
    */
    int outflowPriority;
    
    
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

    
    /**
     * Used to select calculation method for the transfer
     * @deprecated 
     */
    public static String[] objSubTypesAllowed = { // Note list also exitss in IconLibrary
        "FIXED RATE PUMPING", // transfer rate based on pump rates // these will be first to solve
        "ON DEMAND SUPPLY", // transfer rate based on pump rate and only on if reciever is in defict during previous day;
        "ON DEMAND DISCHARGE", // transfer rate based on pump rate and only on if supplier is in surplus previous day; 
        "OVERFLOW"
    };
    
    
    /**
     * Used to select calculation method for the transfer
     */
    public static enum Type{
        /**
         * Transfer rate based on pump rates // these will be first to solve
         */
        FIXEDRATE,
        /**
         * transfer rate based on pump rate and only on if receiver is requested during previous day;
         */
        SUPPLY, 
        /**
         * transfer rate based on pump rate and only on if supplier is in surplus previous day;
         */
        DEMAND,
        /**
         * Transfer rate not limited by pump rate, will overflow based on basin dac;
         */
        OVERFLOW;
    }
    
    /**
     * Used primarily for plotting purposes in the flowsheet
     */
    public static String[] tRNStatesAllowed ={
        "ACTIVE",
        "INACTIVE",
        "DASHED",
        "",
    };
    
    public int stateTime[] = new int[Limit.MAX_STATES];
    public String state[] = new String[Limit.MAX_STATES];
    
    public ResultFlow result; 
    
    /**
     * Used for generating blank transfers to initialize an array 
     */
    TRN(){
        this(0,0,0);
        
    }
    
    /**
     * Used for adding a new transfer
     * @param inX Location in X
     * @param inY Location in y note y axis is down
     * @param number Sequential number used for generating the name only
     */
    TRN(int inX, int inY, int number) {
        
        sides = new Direction();
        
        x = inX;
        y = inY;
        hitBox = new Rectangle(x,y,FlowChartCAD.TRN_BOX_WIDTH,FlowChartCAD.TRN_BOX_HEIGHT);
        hitBox.setLocation(inX - hitBox.getSize().width/2, inY - hitBox.getSize().height/2); // centers the hitbox
        isSelected = false;
        objname = "NEW TRANSFER " + number;
        subType = "Pump";
        
        inObjNumber = -1;
        inSideFrom = Side.TOP;
        inSideFromOset = 0;
        inSideTo = Side.RIGHT;
        inflowPriority = 5;
        
        outObjNumber = -1;
        outSideFrom = Side.LEFT;
        outSideTo = Side.TOP;
        outSideToOset = 0;
        outflowPriority = 5;
        
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
     * Constructs a transfer from an XML element, to be used when loading a save
     * file
     * @param xML Element of an XML representing a transfer
     */
    TRN(Element xML){
        this(); // generates a blank tranfer in event that not all data is found in XML

        // TO FILL IN

        //Resets the hitbox after xml is read in
        hitBox = new Rectangle(x,y,FlowChartCAD.TRN_BOX_WIDTH,FlowChartCAD.TRN_BOX_HEIGHT);
        hitBox.setLocation(x - hitBox.getSize().width/2, y - hitBox.getSize().height/2); // centers the hitbox
        
    }
    

    /**
     * Calculates hourly and daily flow rates, and assigns the provided values to plotVolperDay, PlotVolperHr, and PlotVolPerAnnum. This is intended for use in the
     * flowchart plotting part of the code. the indention is that these values will be picked from pre-solved results. 
     * @param volPerDay 
     */
    public void setPlotValues(double volPerDay){
        plotVolperDay = volPerDay;
        plotVolperHr = volPerDay/24;
        plotVolperAnnum = (int)volPerDay*365; 
    }
    
    /**
     * Priority of inflow 
     * @return 1 = lowest, 5 moderate, 9 highest
     */
    public int getInflowPriority(){
        return inflowPriority;
    }
    
    /**
     * Used for determining pump capacity based on its construction staging
     * @param day 
     * @return 
     */
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
    
    /**
     * Priority of outflow 
     * @return 1 = lowest, 5 moderate, 9 highest
     */
    public int getOutflowPriority(){
        return outflowPriority;
    }
    
    /**
     * Was intended for the solve step but should not be used
     * @deprecated 
     * @param day
     * @param caller
     * @param inflowSurplus
     * @param outflowSurplus
     * @return 
     */
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
     * Builds a tab delimited string of all key data from the TRN,  includes in
     * and outflow links
     * @return 
     * @deprecated use getXMLElement instead
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
        saveString.append(Preferences.LIST_TERMINATOR);
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
        saveString.append(Preferences.LIST_TERMINATOR);
        saveString.append(nextLine);
        return saveString;
    }
    
    
    public String[] getSidesAllowed(){
        return sides.getSidesAllowed();
    }

    /**
     * Used to build an XML element representing all of the information stored
     * within this class, note that it is not intended to store hit box or other
     * info that can be re-calculated
     * @param xMLDoc
     * @param index
     * @return 
     */
    public Element getXMLElement(Document xMLDoc, int index){
                    
            Element tran = xMLDoc.createElement("Tranfer");
            tran.setAttribute("Index", String.valueOf(index));
            tran.setAttribute("ObjName", objname);
            tran.setAttribute("SubType", subType);
            tran.setAttribute("x", String.valueOf(x));
            tran.setAttribute("y", String.valueOf(y));
            
            Element inflow = xMLDoc.createElement("Inflow");
            inflow.setAttribute("inObjNumber", String.valueOf(inObjNumber));
            inflow.setAttribute("inSideFrom", inSideFrom.toString());
            inflow.setAttribute("inSideFromOset", String.valueOf(inSideFromOset));
            inflow.setAttribute("inSideTo", inSideTo.toString());
            tran.appendChild(inflow);
            
            Element outflow = xMLDoc.createElement("Outflow");
            outflow.setAttribute("outObjNumber", String.valueOf(outObjNumber));
            outflow.setAttribute("outSideFrom", outSideFrom.toString());
            outflow.setAttribute("outSideTo", outSideTo.toString());
            outflow.setAttribute("outSideToOset", String.valueOf(outSideToOset));
            tran.appendChild(outflow);
            
            return tran;
    }

    /**
     * method to call constructor on result variables
     * @param projSetting
     */
    public void initResults(ProjSetting projSetting) {
        result = new ResultFlow(projSetting.getDuration(), objname);
    }
    
    
    /**
     * Populates the element from a tab delimited list of data 
     *
     * @param inData Receives a string that is formatted identically to the getString method
     * @deprecated Nolonger does anything replace with constructor from XML
     */
    public void setFromString(String inData){
 
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
