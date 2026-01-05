/*
Copyright (c) 2026, Alex McIntyre
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. All advertising materials mentioning features or use of this software
   must display the following acknowledgement:
   This product includes software developed by Alex McIntyre.
4. Neither the name of the organization nor the
   names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.mcwbalance.transfer;


import com.mcwbalance.flowchart.FlowChartCAD;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.result.ResultFlow;
import com.mcwbalance.settings.Limit;
import com.mcwbalance.util.Direction;
import com.mcwbalance.util.Direction.Side;
import java.awt.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TRN {// class to catalog properties of a Pipe or other water transfer Mechanism
    
    Direction sides;
    
    public String objname;
    public String subType;
    public int x; // Coordinates if Information Box
    public int y;
    public Rectangle hitBox;
    
    public boolean isSelected;
    public boolean printable;
    
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
    public int stateCount;
    
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
        printable = true;
        
        inObjNumber = -1;
        inSideFrom = Side.TOP;
        inSideFromOset = 0;
        inSideTo = Side.LEFT;
        inflowPriority = 5;
        
        outObjNumber = -1;
        outSideFrom = Side.RIGHT;
        outSideTo = Side.TOP;
        outSideToOset = 0;
        outflowPriority = 5;
        
        pumpTime[0] = 0; 
        pumpRateDay[0] = 0;
        pumpRateCount = 1;
        
        stateTime[0] = 0;
        state[0] = "ACTIVE";
        stateCount = 1;
        
        plotVolperDay = 0.0;
        plotVolperHr = 0.00;
        plotVolperAnnum = 0; 

    }
    
    
    /**
     * Constructs a transfer from an XML element, to be used when loading a save
     * file
     * In the event that the provided XML file is incomplete or i made a typo 
     * a default value will be used and no errors will be thrown..
     * @param xML Element of an XML representing a "Transfer"
     */
    TRN(Element tran){
        this(); // generates a blank tranfer in event that not all data is found in XML
        
        objname = tran.getAttribute("ObjName");
        subType = tran.getAttribute("SubType");
        x = Integer.parseInt(wdef(tran.getAttribute("x"),"5"));
        y = Integer.parseInt(wdef(tran.getAttribute("y"),"5"));
        printable = Boolean.parseBoolean(wdef(tran.getAttribute("printable"),"true"));
        
        //Resets the hitbox after xml is read in
        hitBox = new Rectangle(x,y,FlowChartCAD.TRN_BOX_WIDTH,FlowChartCAD.TRN_BOX_HEIGHT);
        hitBox.setLocation(x - hitBox.getSize().width/2, y - hitBox.getSize().height/2); // centers the hitbox
        
        //Pulls only the first inflow node...
        Node inflowNode = tran.getElementsByTagName("Inflow").item(0);
        if (inflowNode.getNodeType() == Node.ELEMENT_NODE) {
            Element inflow  = (Element) inflowNode;
            inObjNumber = Integer.parseInt(wdef(inflow.getAttribute("inObjNumber"),"-1"));
            inSideFrom = Side.valueOf(wdef(inflow.getAttribute("inSideFrom"),Side.TOP.name()));
            inSideFromOset = Integer.parseInt(wdef(inflow.getAttribute("inSideFromOset"),"5"));
            inSideTo = Side.valueOf(wdef(inflow.getAttribute("inSideTo"),Side.TOP.name()));
            inflowPriority = Integer.parseInt(wdef(inflow.getAttribute("inflowPriority"),"5"));
        }
        
        //Pulls only the first inflow node...
        Node outflowNode = tran.getElementsByTagName("Outflow").item(0);
        if (outflowNode.getNodeType() == Node.ELEMENT_NODE) {
            Element outflow  = (Element) outflowNode;
            outObjNumber = Integer.parseInt(wdef(outflow.getAttribute("outObjNumber"),"-1"));
            outSideFrom = Side.valueOf(wdef(outflow.getAttribute("outSideFrom"),Side.TOP.name()));
            outSideTo = Side.valueOf(wdef(outflow.getAttribute("outSideTo"),Side.TOP.name()));
            outSideToOset = Integer.parseInt(wdef(outflow.getAttribute("outSideToOset"),"0"));
            outflowPriority = Integer.parseInt(wdef(outflow.getAttribute("outflowPriority"),"5"));
        }
        
        Node pumpratesNode = tran.getElementsByTagName("PumpRates").item(0);
        if (pumpratesNode.getNodeType() == Node.ELEMENT_NODE) {
            NodeList rates = pumpratesNode.getChildNodes();
            pumpRateCount = 0;
            for (int i = 0; i < rates.getLength(); i++) {
                if (rates.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element ele = (Element) rates.item(i);
                    addPumpRate(
                            Integer.parseInt(wdef(ele.getAttribute("Time"),"0")),
                            Double.parseDouble(wdef(ele.getAttribute("Rate"),"0"))
                    );
                }
            }
        }
        
        Node statesNode = tran.getElementsByTagName("States").item(0);
        if (statesNode.getNodeType() == Node.ELEMENT_NODE) {
                        NodeList states = statesNode.getChildNodes();
            pumpRateCount = 0;
            for (int i = 0; i < states.getLength(); i++) {
                if (states.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element ele = (Element) states.item(i);
                    addState(
                            Integer.parseInt(ele.getAttribute("Time")),
                            ele.getAttribute("State")
                    );
                }
            }

        }
        // TO FILL IN

        
        
    }
    
    /**
     * adds a pump rate to the list of pump rates
     * TODO - ADD SORTING
     * @param time In Days
     * @param rate in m3/day
     */
    private void addPumpRate(int time, double rate){
        if (pumpRateCount < MAX_PUMP_RATES) {
            pumpTime[pumpRateCount] = time;
            pumpRateDay[pumpRateCount] = rate;
            pumpRateCount++;
        }
    }
    
    /**
     * adds a state to the list of states
     * TODO - ADD SORTING
     * @param time
     * @param stateAdd 
     */
    private void addState(int time, String stateAdd) {
        if (stateCount < Limit.MAX_STATES) {
            stateTime[stateCount] = time;
            state[stateCount] = stateAdd;
            stateCount++;
        }
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

        Element tran = xMLDoc.createElement("Transfer");
        tran.setAttribute("Index", String.valueOf(index));
        tran.setAttribute("ObjName", objname);
        tran.setAttribute("SubType", subType);
        tran.setAttribute("x", String.valueOf(x));
        tran.setAttribute("y", String.valueOf(y));
        tran.setAttribute("printable", String.valueOf(printable));

        Element inflow = xMLDoc.createElement("Inflow");
        inflow.setAttribute("inObjNumber", String.valueOf(inObjNumber));
        inflow.setAttribute("inSideFrom", inSideFrom.toString());
        inflow.setAttribute("inSideFromOset", String.valueOf(inSideFromOset));
        inflow.setAttribute("inSideTo", inSideTo.toString());
        inflow.setAttribute("inflowPriority", String.valueOf(inflowPriority));
        tran.appendChild(inflow);

        Element outflow = xMLDoc.createElement("Outflow");
        outflow.setAttribute("outObjNumber", String.valueOf(outObjNumber));
        outflow.setAttribute("outSideFrom", outSideFrom.toString());
        outflow.setAttribute("outSideTo", outSideTo.toString());
        outflow.setAttribute("outSideToOset", String.valueOf(outSideToOset));
        inflow.setAttribute("outflowPriority", String.valueOf(outflowPriority));
        tran.appendChild(outflow);
        
        Element pumpratesXML = xMLDoc.createElement("PumpRates");
        for (int i = 0; i < pumpRateCount; i++) {
            Element rateXML = xMLDoc.createElement("Rate");
            rateXML.setAttribute("Time", String.valueOf(pumpTime[i]));
            rateXML.setAttribute("Rate", String.valueOf(pumpRateDay[i]));
            pumpratesXML.appendChild(rateXML);
        }
        tran.appendChild(pumpratesXML);
        
        Element statesXML = xMLDoc.createElement("States");
        for (int i = 0; i < stateCount; i++) {
            Element stateXML = xMLDoc.createElement("State");
            stateXML.setAttribute("Time", String.valueOf(stateTime[i]));
            stateXML.setAttribute("State", String.valueOf(state[i]));
            statesXML.appendChild(stateXML);
        }
        tran.appendChild(statesXML);
        
        //TODO ADD RESULTS

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
    
    /**
     * Simple Util for ensuring defualt values are read in;
     * A console err message will be generated to assist somewhat with debugging
     *
     * @param string string to read
     * @param def string to use if string is ""
     * @return
     */
    private String wdef(String string, String def){
        if (string != ""){
            return string;
        }
        System.err.println("Possible Error - TRN.Java - wdef() - Default " + def + " value was applied due to blank string");
        return def;
    }

   

}
