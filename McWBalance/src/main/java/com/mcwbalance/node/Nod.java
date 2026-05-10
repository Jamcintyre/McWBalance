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
package com.mcwbalance.node;

import com.mcwbalance.McWBalance;
import com.mcwbalance.dacapacity.DAC;
import com.mcwbalance.landcover.DataCatchment;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.generics.IndexList;
import com.mcwbalance.project.Project;
import com.mcwbalance.result.ResultFlow;
import com.mcwbalance.result.ResultLevel;
import com.mcwbalance.settings.Limit;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Contains all information required to represent 1 Node such as a Basin,
 * Tailings Facility, Pit, Process Plant.
 *
 * Class was originally called ELM, changed to Node, but had a case issue node
 * vs Node this class should not be called Node as that class is already used by
 * the XML Parser and could cause confusion
 *
 * @see NODEList
 * @author amcintyre
 */
public class Nod {

    /**
     * X coordinate of node
     */
    public int x;
    /**
     * Y coordinate of node
     */
    public int y;
    /**
     * hitbox of node
     */
    public Rectangle hitBox;
    /**
     * used for tracking if selected
     */
    public boolean isSelected;
    /**
     * name of node
     */
    public String objname;
    /**
     * Type of node
     */
    public String objSubType;
    /**
     * display image of node
     */
    public BufferedImage objSprite;
    /**
     * horizontal scale of node
     */
    public double scaleX;
    /**
     * vertical display scale of node
     */
    public double scaleY;

    /**
     * Flag for use during solve, i.e. if has catchment then will have runoff
     * result
     */
    public Boolean hasCatchment;
    /**
     * number of non null catchments
     */
    public int nCatchments;
    /**
     * array holding catchment values
     */
    public DataCatchment[] Catchment = new DataCatchment[Limit.MAX_LAND_COVERS + 1]; // needs a totalizer value.

    /**
     * used for determining if the node will have an input of tailings or waste
     * solids
     */
    public Boolean hasSolids;

    /**
     * used for plot position of void loss result in FlowChartCad
     */
    public int oSetXVoids;
    /**
     * used for plot position of void loss result in FlowChartCad
     */
    public int oSetYVoids;

    /**
     * Flags if node is allowed to be unbalanced (i.e. store water)
     */
    public Boolean hasStorage;

    /**
     * flags if change in storage result should be displayed in FlowChartCad
     */
    public Boolean showStorage;

    /**
     * used for plot position of change in storage result in FlowChartCad
     */
    public int oSetXStorage;
    /**
     * used for plot position of change in storage result in FlowChartCad
     */
    public int oSetYStorage;

    /**
     * used to indicate storage is exposed (as opposed to a closed top tank)
     */
    public Boolean hasStorageEvapandPrecip;
    /**
     * Depth Area Capacity Curve for attached storage note will only be
     * constructed if boolean hasStorage is selected;
     */
    public DAC dAC;

    /**
     * List of linked transfers that flow into Node
     */
    public IndexList inflows;
    /**
     * List of linked transfers that flow out of node
     */
    public IndexList outflows;

    /**
     * Table of target operating levels
     */
    public TableNodeLevel targetOperatingVol;

    /**
     * Table of minimum operating depths (i.e. 1 m water cover above tails)
     */
    public TableNodeLevel minDepth;
    /**
     * Table of maximum operating level before emergency discharge required
     */
    public TableNodeLevel maxOpLevel;
    /**
     * Table of overflow levels that will result in overflow discharge
     */
    public TableNodeLevel overflowLevel;
    /**
     * Table of crest levels for plotting
     */
    public TableNodeLevel crestLevel;

    /**
     * Table of solids deposition rates
     */
    public TableTailingsDepositionRates depositionRates;

    /**
     * List of inflows that are constant value
     */
    public IndexList inflowFixedTRN;
    /**
     * List of outflows that are constant rate
     */
    public IndexList outflowFixedTRN;
    /**
     * list of inflows that occur if below target operating level
     */
    public IndexList inflowOnDemandTRN;
    /**
     * list of outflows that occur if above target operating level
     */
    public IndexList outflowOnDemandTRN;
    /**
     * list of tailings inflow transfers
     */
    public IndexList tailsTRNOptions;
    /**
     * index of inflow transfer linked to tailings solids deposition
     */
    public int tailsTRN;
    /**
     * list of options for overflow
     */
    public IndexList overflowOptions;
    /**
     * index of transfer used to handle spillway overflows
     */
    public int overflowTRN;

    /**
     * Allows imageLib to determine which sprite sheet to use
     */
    public static String[] objSubTypesAllowed = { // Note list also exitss in IconLibrary
        "DAM_PADDOCK",
        "DAM_VALLEY_LEFT",
        "DAM_VALLEY_RIGHT",
        "DEFAULT",
        "LAKE",
        "OPENPIT",
        "PLANT",
        "STOCKPILE",
        "UNDERGROUND",
        "WELL"
    };

    //ProjSetting projsetting;
    /**
     * Used for selecting symbol on flowchart, will not be used in the
     * calculations directly
     */
    public static String[] StatesAllowed = {
        "INACTIVE",
        "ACTIVE",
        "UNDERCONSTRUCTION",
        "CONSTRUCTEDEMPTY",
        "HALFWATER",
        "FULLWATER",
        "TAILINGS",
        "TAILINGSWATERCOVER",
        "TAILINGSDRY",
        "CLOSEDDRYCOVER",
        "CLOSEDWETCOVER"
    };

    /**
     * used for tracking object state
     */
    public int stateTime[] = new int[Limit.MAX_STATES];
    /**
     * Used for tracking object state
     */
    public String state[] = new String[Limit.MAX_STATES];
    int stateCount;

    //Results
    
    ResultNode res[];

    /**
     * constructs a blank node
     *
     * @param aP Active project needed for getting sprites
     */
    Nod(Project aP) {
        this(0, 0, 0, aP);
    }

    /**
     * Constructs node from xml, used for loading in
     *
     * @param aP active project needed for pulling object sprite and land cover
     * names for
     * @param nodeXML source XML
     */
    Nod(Project aP, Element nodeXML) {
        //Generates a null node to fill in if XML is missing information
        this(aP);

        //Added wdef method to fill in default values when xml file is incompleted
        this.objname = nodeXML.getAttribute("Name");
        objSubType = nodeXML.getAttribute("SubType");
        x = Integer.parseInt(wdef(nodeXML.getAttribute("x"), "0"));
        y = Integer.parseInt(wdef(nodeXML.getAttribute("y"), "0"));

        scaleX = Double.parseDouble(wdef(nodeXML.getAttribute("scaleX"), "1"));
        scaleY = Double.parseDouble(wdef(nodeXML.getAttribute("scaleY"), "1"));
        objSprite = aP.getProjectSetting().getImageLib().getImage(objSubType, "ACTIVE", scaleX, scaleY);
        updateHitbox();

        //Get from Catchments element
        //hasCatchment = Boolean.valueOf(nodeXML.getAttribute("hasCatchment"));
        //nCatchments = Integer.parseInt(nodeXML.getAttribute("nCatchments"));
        hasStorageEvapandPrecip = Boolean.valueOf(wdef(nodeXML.getAttribute("hasStorageEvapandPrecip"), "0"));
        hasSolids = Boolean.valueOf(wdef(nodeXML.getAttribute("hasSolids"), "false"));
        oSetXVoids = Integer.parseInt(wdef(nodeXML.getAttribute("oSetXVoids"), "0"));
        oSetYVoids = Integer.parseInt(wdef(nodeXML.getAttribute("oSetYVoids"), "0"));
        hasStorage = Boolean.valueOf(wdef(nodeXML.getAttribute("hasStorage"), "false"));
        showStorage = Boolean.valueOf(wdef(nodeXML.getAttribute("showStorage"), "false"));
        oSetXStorage = Integer.parseInt(wdef(nodeXML.getAttribute("oSetXStorage"), "0"));
        oSetYStorage = Integer.parseInt(wdef(nodeXML.getAttribute("oSetYStorage"), "0"));

        //TODO NOT COMPLETED
    }

    /**
     * Generates a blank note at a specified coordinate
     *
     * @param inX location where to place node
     * @param inY location where to place node
     * @param number index number of node
     * @param aP Active project
     */
    Nod(int inX, int inY, int number, Project aP) {
        x = inX;
        y = inY;
        hitBox = new Rectangle(0, 0, 20, 20);
        isSelected = false;
        objname = "NEW NODE " + number;
        objSubType = "DEFAULT";
        scaleX = 1.0;
        scaleY = 1.0;
        objSprite = aP.getProjectSetting().getImageLib().getImage(objSubType, "ACTIVE", scaleX, scaleY);
        updateHitbox();

        hasCatchment = false;
        nCatchments = 0;

        hasStorageEvapandPrecip = false;
        hasSolids = false;
        oSetXVoids = 0;
        oSetYVoids = 0;
        hasStorage = false;
        showStorage = false;
        oSetXStorage = 0;
        oSetYStorage = 0;
        dAC = new DAC();

        targetOperatingVol = new TableNodeLevel(1,
                McWBalance.langRB.getString("MODEL_DAY"),
                McWBalance.langRB.getString("VOLUME") + " (cu.m.)",
                McWBalance.langRB.getString("BASIS"));
        minDepth = new TableNodeLevel(1,
                McWBalance.langRB.getString("MODEL_DAY"),
                McWBalance.langRB.getString("LEVEL") + " (m)",
                McWBalance.langRB.getString("BASIS"));
        maxOpLevel = new TableNodeLevel(1,
                McWBalance.langRB.getString("MODEL_DAY"),
                McWBalance.langRB.getString("LEVEL") + " (m)",
                McWBalance.langRB.getString("BASIS"));
        overflowLevel = new TableNodeLevel(1,
                McWBalance.langRB.getString("MODEL_DAY"),
                McWBalance.langRB.getString("LEVEL") + " (m)",
                McWBalance.langRB.getString("BASIS"));
        crestLevel = new TableNodeLevel(1,
                McWBalance.langRB.getString("MODEL_DAY"),
                McWBalance.langRB.getString("LEVEL") + " (m)",
                McWBalance.langRB.getString("BASIS"));

        depositionRates = new TableTailingsDepositionRates(aP.getProjectSetting());

        inflows = new IndexList(Limit.MAX_TRNS);
        outflows = new IndexList(Limit.MAX_TRNS);
        tailsTRNOptions = new IndexList(Limit.MAX_TRNS);
        tailsTRN = -1;

        inflowFixedTRN = new IndexList(Limit.MAX_TRNS);
        outflowFixedTRN = new IndexList(Limit.MAX_TRNS);
        inflowOnDemandTRN = new IndexList(Limit.MAX_TRNS);
        outflowOnDemandTRN = new IndexList(Limit.MAX_TRNS);

        overflowOptions = new IndexList(Limit.MAX_TRNS);
        overflowTRN = -1;

        stateTime[0] = -1;
        for (int i = 1; i < Limit.MAX_STATES; i++) {
            stateTime[i] = Limit.MAX_DURATION;
        }
        state[0] = "ACTIVE";
        stateCount = 0;

    }

    /**
     * wipes and replaces all results with new instances, useful for duration
     * and landcover change Should be used prior to each solve
     *
     * @param aP active project
     
     */
    public final void initResults(Project aP) {
        res = new ResultNode[aP.climateTable.getRowCount()];
        for (int cs = 1; cs < res.length; cs++){
            res[cs] = new ResultNode(cs, this.objname, aP.climateTable.getValueAt(cs, 0).toString());
            res[cs].initResults(aP);
        }
    }

    /**
     * method used to determine the state of the object for any given day
     *
     * @param day timestep to get state
     * @return
     */
    public String getState(int day) {
        if (day == -1) {
            return state[0];
        }
        for (int i = 1; i < state.length; i++) {
            if (day < stateTime[i]) {
                if (state[i - 1] != null) {
                    return state[i - 1];
                }
            }
        }
        return state[0];
    }

    /**
     * TODO - Not completed
     *
     * @param day
     */
    private int getPondArea(int day) {
        if (day < 0) {
            return 0;
        } else {
            return 100; //DEBUG placeholder for testing
        }
    }

    /**
     * TODO - Not completed
     *
     * @param day
     */
    private int getBasinArea(int day) {
        if (day < 0) {
            return 0;
        } else {
            return 100; //DEBUG placeholder for testing
        }
    }

    /**
     * Used to build an XML element representing all of the information stored
     * within this class, note that it is not intended to store hit box or other
     * info that can be re-calculated
     *
     * @param xMLDoc xML document that this element will be appended to
     * @param index element index number
     * @return
     */
    public Element getXMLElement(Document xMLDoc, int index) {
        Element nXML = xMLDoc.createElement("Node");

        nXML.setAttribute("Index", String.valueOf(index));
        nXML.setAttribute("Name", objname);
        nXML.setAttribute("SubType", objSubType);

        nXML.setAttribute("x", String.valueOf(x));
        nXML.setAttribute("y", String.valueOf(y));

        nXML.setAttribute("scaleX", String.valueOf(scaleX));
        nXML.setAttribute("scaleY", String.valueOf(scaleY));

        nXML.setAttribute("hasSolids", String.valueOf(hasSolids));
        nXML.setAttribute("hasStorage", String.valueOf(hasStorage));

        nXML.setAttribute("oSetXVoids", String.valueOf(oSetXVoids));
        nXML.setAttribute("oSetYVoids", String.valueOf(oSetYVoids));
        nXML.setAttribute("oSetXStorage", String.valueOf(oSetXStorage));
        nXML.setAttribute("oSetYStorage", String.valueOf(oSetYStorage));

        nXML.setAttribute("overflowTRN", String.valueOf(overflowTRN));
        nXML.setAttribute("showStorage", String.valueOf(showStorage));
        nXML.setAttribute("tailsTRN", String.valueOf(tailsTRN));

        if (hasCatchment) {
            Element catchmentsXML = xMLDoc.createElement("Catchments");
            for (int i = 0; i < nCatchments; i++) {
                Element catchXML = xMLDoc.createElement("Catchment");
                catchXML.setAttribute("LandCover", Catchment[i].getLandCover());
                for (int j = 0; j < Catchment[i].getLength(); j++) {
                    Element areaXML = xMLDoc.createElement("Area");
                    areaXML.setAttribute("Time", String.valueOf(Catchment[i].getTimeAtIndex(j)));
                    areaXML.setAttribute("Area", String.valueOf(Catchment[i].getAreaAtIndex(j)));
                    catchXML.appendChild(areaXML);
                }
                catchmentsXML.appendChild(catchXML);
            }
            nXML.appendChild(catchmentsXML);
        }

        // DAC is only saved if the basin is flagged as having storage
        if (hasStorage) {
            Element capXML = xMLDoc.createElement("Capacity");
            for (int i = 0; i < dAC.getRowCount(); i++) {
                Element areaXML = xMLDoc.createElement("Area");
                areaXML.setAttribute("Elevation", String.valueOf(dAC.getValueAt(i, 0)));
                areaXML.setAttribute("Area", String.valueOf(dAC.getValueAt(i, 1)));
                capXML.appendChild(areaXML);
            }
            nXML.appendChild(capXML);
        }

        inflows.appendXMLElement(nXML, xMLDoc, "Inflows");
        outflows.appendXMLElement(nXML, xMLDoc, "Outflows");
        targetOperatingVol.appendXMLElement(nXML, xMLDoc, "targetOperatingVol");
        minDepth.appendXMLElement(nXML, xMLDoc, "minDepth");
        maxOpLevel.appendXMLElement(nXML, xMLDoc, "maxOpLevel");
        overflowLevel.appendXMLElement(nXML, xMLDoc, "overflowLevel");
        crestLevel.appendXMLElement(nXML, xMLDoc, "crestLevel");
        depositionRates.appendXMLElement(nXML, xMLDoc, "depositionRates");
        inflowFixedTRN.appendXMLElement(nXML, xMLDoc, "inflowFixedTRN");
        outflowFixedTRN.appendXMLElement(nXML, xMLDoc, "outflowFixedTRN");
        inflowOnDemandTRN.appendXMLElement(nXML, xMLDoc, "inflowOnDemandTRN");
        outflowOnDemandTRN.appendXMLElement(nXML, xMLDoc, "outflowOnDemandTRN");
        tailsTRNOptions.appendXMLElement(nXML, xMLDoc, "tailsTRNOptions");
        tailsTRNOptions.appendXMLElement(nXML, xMLDoc, "tailsTRNOptions");

        Element statesXML = xMLDoc.createElement("States");
        for (int i = 0; i < stateCount; i++) {
            Element stateXML = xMLDoc.createElement("State");
            stateXML.setAttribute("Time", String.valueOf(stateTime[i]));
            stateXML.setAttribute("State", String.valueOf(state[i]));
            statesXML.appendChild(stateXML);
        }
        nXML.appendChild(statesXML);
        return nXML;
    }

    /**
     * method used to purge references of a specific transfer from any settings
     * within the Node
     *
     * @param rTRN
     */
    public void remove(int rTRN) {
        inflows.trimFromList(rTRN);
        outflows.trimFromList(rTRN);
        tailsTRNOptions.trimFromList(rTRN);
        if (tailsTRN == rTRN) {
            tailsTRN = -1;
        }
        inflowFixedTRN.trimFromList(rTRN);
        outflowFixedTRN.trimFromList(rTRN);
        inflowOnDemandTRN.trimFromList(rTRN);
        outflowOnDemandTRN.trimFromList(rTRN);
        overflowOptions.trimFromList(rTRN);
        if (overflowTRN == rTRN) {
            overflowTRN = -1;
        }
        ProjSetting.hasChangedSinceSave = true;
    }

    /**
     * Used to set sprite and dimensions of object for flowChartCad whenever
     * object Subtype is changed
     *
     * @param inSubType
     * @param inState state from StatesAllowed, if no match is found then
     * imageLib returns a default sprite
     * @param aP active project
     */
    public void setSubType(String inSubType, String inState, Project aP) {
        objSubType = inSubType;
        objSprite = aP.getProjectSetting().getImageLib().getImage(objSubType, inState, scaleX, scaleY);
        updateHitbox();
        ProjSetting.hasChangedSinceSave = true;
    }

    /**
     * Sets sprite image for the element from the imageLib class
     *
     * @param inState state from StatesAllowed, if no match is found then
     * imageLib returns a default sprite
     * @param aP Active project
     */
    public void setSpriteState(String inState, Project aP) {
        objSprite = aP.getProjectSetting().getImageLib().getImage(objSubType, inState, scaleX, scaleY);
    }

    /**
     * Resolves Precipitation, evaporation, and seepage
     *
     * @param step timestep to solve
     * @param aP Active project
     * @param cs picks which Scenario
     */
    public void solveEnvironmentals(int step, int cs, Project aP) {
        
        // need to calc upstream catch first, then evap,, 
        if (hasStorageEvapandPrecip && hasStorage) {
            int pondarea = getPondArea(step - 1);
            double precip = pondarea * aP.climateTable.getClimates()[cs].precip[step] / 1000;
            double evap = pondarea * aP.climateTable.getClimates()[cs].evap[step] / 1000;

            //TODO Limit evap 
            res[cs].inDirectPrecip.add(precip, step);
            res[cs].inDirectPrecip.add(evap, step);

        }

        if (hasCatchment) {

            double rainandmelt = (aP.climateTable.getClimates()[cs].rain[step]
                    + aP.climateTable.getClimates()[cs].melt[step]) / 1000;

            for (int i = 0; i < res[cs].inRunoffandMeltbyLC.length; i++) {
                int area = Catchment[i].getTimeAtIndex(step);

                if (area <= 0) {
                    res[cs].inRunoffandMeltbyLC[i].add(0, step);

                } else {
                    double rC = aP.runoffCoeffs.getCoefficient(Catchment[i].getLandCover(), aP.stepToMonth(step));
                }

                res[cs].inRunoffandMeltbyLC[i] = new ResultFlow(aP.getProjectSetting().getDuration(),
                        objname + " " + McWBalance.langRB.getString("RUNOFF_AND_MELT")
                        + " " + aP.climateTable.getValueAt(i, 0).toString());
            }

        }

        /*
        resultInDirectPrecip = new ResultFlow(aP.getProjectSetting().getDuration(), objname + " " + McWBalance.langRB.getString("DIRECT_PRECIP"));
        resultInRunoffandMelt = new ResultFlow(aP.getProjectSetting().getDuration(), objname + " " + McWBalance.langRB.getString("RUNOFF_AND_MELT"));
        resultInRunoffandMeltbyLC = new ResultFlow[aP.runoffCoeffs.getLength()];
        for (int i = 0; i < resultInRunoffandMeltbyLC.length; i++) {
            resultInRunoffandMeltbyLC[i] = new ResultFlow(aP.getProjectSetting().getDuration(),
                    objname + " " + McWBalance.langRB.getString("RUNOFF_AND_MELT")
                    + " " + aP.climateTable.getValueAt(i, 0).toString());
        }
        resultOutEvaporation = new ResultFlow(aP.getProjectSetting().getDuration(), objname + " " + McWBalance.langRB.getString("EVAPORATION"));
        resultOutSeepage = new ResultFlow(aP.getProjectSetting().getDuration(), objname + " " + McWBalance.langRB.getString("SEEPAGE"));
        resultOutVoidloss = new ResultFlow(aP.getProjectSetting().getDuration(), objname + " " + McWBalance.langRB.getString("VOID_LOSS"));
        resultLvlSolids = new ResultLevel(aP.getProjectSetting().getDuration(), objname + " " + McWBalance.langRB.getString("SOLIDS_EL"));
        resultLvlPond = new ResultLevel(aP.getProjectSetting().getDuration(), objname + " " + McWBalance.langRB.getString("POND_EL"));
         */
    }

    private void updateHitbox() {
        hitBox.x = x - objSprite.getWidth() / 2;
        hitBox.y = y - objSprite.getHeight() / 2;
        hitBox.width = objSprite.getWidth();
        hitBox.height = objSprite.getHeight();
    }

    /**
     * Simple Util for ensuring default values are read in;
     *
     * @param string string to read
     * @param def string to use if string is ""
     * @return
     */
    private String wdef(String string, String def) {
        if (!"".equals(string)) {
            return string;
        }
        System.err.println("Possible Error - Nod.Java - wdef() - Default " + def + " value was applied due to blank string");
        return def;
    }

}
