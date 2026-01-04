package com.mcwbalance.node;

import com.mcwbalance.dacapacity.DAC;
import com.mcwbalance.landcover.DataCatchment;
import com.mcwbalance.generics.DataTimeDoubleSeries;
import com.mcwbalance.generics.DataTimeIntSeries;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.generics.IndexList;
import com.mcwbalance.result.ResultFlow;
import com.mcwbalance.result.ResultStorageVolume;
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
 * Class was originally called ELM,  changed to Node, but had a case issue node vs Node
 * this class should not be called Node as that class is already used by the XML
 * Parser and could cause confusion
 *
 * @see NODEList
 * @author amcintyre
 */
public class Nod {

   
    public int x;
    public int y;
    public Rectangle hitBox;
    public boolean isSelected;
    public String objname;
    public String objSubType;
    public BufferedImage objSprite;
    public double scaleX;
    public double scaleY;

    public Boolean hasCatchment;
    public int nCatchments;
    public DataCatchment[] Catchment = new DataCatchment[Limit.MAX_LAND_COVERS + 1]; // needs a totalizer value.
    /**
     * @deprecated 
     */
    public int indexRunoffTracker;
    public Boolean hasSolids;
    /**
     * @deprecated 
     */
    public int indexSolidsTracker;
    public int oSetXVoids;
    public int oSetYVoids;
    public Boolean hasStorage;
    public Boolean showStorage;
    /**
     * @deprecated 
     */
    public int indexStorageTracker; // used to also index Direct Precips and Evaps; may be depreciated? 
    public int oSetXStorage;
    public int oSetYStorage;
    
    public Boolean hasStorageEvapandPrecip; // used to indicate storage is exposed (as opposed to a closed top tank)
    public DAC dAC; // note will only be constructed if boolean hasStorage is selected; 

    public IndexList inflows;
    public IndexList outflows;

    public DataTimeIntSeries targetOperatingVol;
    public DataTimeDoubleSeries minDepth;
    public DataTimeDoubleSeries maxOpLevel;
    public DataTimeDoubleSeries overflowLevel;
    public DataTimeDoubleSeries crestLevel;

    public TableTailingsDepositionRates depositionRates;

    public IndexList inflowFixedTRN;
    public IndexList outflowFixedTRN;
    public IndexList inflowOnDemandTRN;
    public IndexList outflowOnDemandTRN;
    public IndexList tailsTRNOptions;
    public int tailsTRN; //index of inflow transfer linked to tailings solids deposition
    public IndexList overflowOptions;
    public int overflowTRN; // index of transfer used to handle spillway overflows

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
    
    ProjSetting projsetting;
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

    public int stateTime[] = new int[Limit.MAX_STATES];
    public String state[] = new String[Limit.MAX_STATES];
    int stateCount;
    
    public ResultLevel resultWaterLevel;
    public ResultLevel resultSolidsLevel;
    
    public ResultStorageVolume resultTotalVolume;
    public ResultStorageVolume resultPondVolume;
    
    public ResultFlow resultSolidsInflow;
    
    public ResultFlow resultRunoff[];
    public ResultFlow resultDirectPrecip;
    public ResultFlow resultEvaporation;
    public ResultFlow resultSeepage;
    
    
    /**
     * constructs a blank node
    */
    Nod(ProjSetting projSetting) {
        this(0, 0, 0, projSetting);
    }
    
    /**
     * Constructs node from xml, used for loading in
     * @param projSetting
     * @param nodeXML 
     */
    Nod(ProjSetting projSetting, Element nodeXML){
        //Generates a null node to fill in if XML is missing information
        this(projSetting);
        
        //TODO NOT COMPLETED
        
        
    }
    

    Nod(int inX, int inY, int number, ProjSetting projSetting) {
        this.projsetting = projSetting;
        x = inX;
        y = inY;
        hitBox = new Rectangle(0, 0, 20, 20);
        isSelected = false;
        objname = "NEW NODE " + number;
        objSubType = "DEFAULT";
        scaleX = 1.0;
        scaleY = 1.0;
        objSubType = "DEFAULT";
        objSprite = projsetting.getImageLib().getImage(objSubType, "ACTIVE", scaleX, scaleY);
        hitBox.x = x - objSprite.getWidth() / 2;
        hitBox.y = y - objSprite.getHeight() / 2;
        hitBox.width = objSprite.getWidth();
        hitBox.height = objSprite.getHeight();
        hasCatchment = false;
        nCatchments = 0;
        indexRunoffTracker = -1; // used when building the run settings to track output
        hasStorageEvapandPrecip = false;
        hasSolids = false;
        oSetXVoids = 0;
        oSetYVoids = 0; 
        hasStorage = false;
        showStorage = false;
        oSetXStorage = 0;
        oSetYStorage = 0;
        dAC = new DAC();

        targetOperatingVol = new DataTimeIntSeries(1);
        minDepth = new DataTimeDoubleSeries(1);
        maxOpLevel = new DataTimeDoubleSeries(1);
        overflowLevel = new DataTimeDoubleSeries(1);
        crestLevel = new DataTimeDoubleSeries(1);
        
        depositionRates = new TableTailingsDepositionRates(projsetting);

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
    * method used to determine the state of the object for any given day
    * @param day 
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
     * Used to build an XML element representing all of the information stored
     * within this class, note that it is not intended to store hit box or other
     * info that can be re-calculated
     * @param xMLDoc xML document that this element will be appended to
     * @param index element index number
     * @return 
     */
    public Element getXMLElement(Document xMLDoc, int index){
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
        nXML.setAttribute("overflowTRN", String.valueOf(overflowTRN));
        nXML.setAttribute("showStorage", String.valueOf(showStorage));
        nXML.setAttribute("tailsTRN", String.valueOf(tailsTRN));

        if(hasCatchment){
            Element catchmentsXML = xMLDoc.createElement("Catchments");
            for (int i = 0; i < nCatchments; i++){
                Element catchXML = xMLDoc.createElement("Catchment");
                catchXML.setAttribute("LandCover", Catchment[i].getLandCover());
                for (int j = 0; j < Catchment[i].getLength(); j++){
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
        if(hasStorage){
            Element capXML = xMLDoc.createElement("Capacity");
            for (int i = 0; i < dAC.getRowCount(); i++){
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
        
        
    /**    
    public ResultLevel resultWaterLevel;
    public ResultLevel resultSolidsLevel;
    
    public ResultStorageVolume resultTotalVolume;
    public ResultStorageVolume resultPondVolume;
    
    public ResultFlow resultSolidsInflow;
    
    public ResultFlow resultRunoff[];
    public ResultFlow resultDirectPrecip;
    public ResultFlow resultEvaporation;
    public ResultFlow resultSeepage;
    */
        
        
        
        
        
        return nXML;
    }
    
    /**
     * method to initialize result variables to current project duration and names
     */
    public void initResults() {
        resultWaterLevel = new ResultLevel(projsetting.getDuration(), objname + " Water Level");
        resultSolidsLevel = new ResultLevel(projsetting.getDuration(), objname + " Solids Level");
        resultSolidsInflow = new ResultFlow(projsetting.getDuration(), objname + " Solids Inflow");
        
        resultTotalVolume = new ResultStorageVolume(projsetting.getDuration(), objname + " Total Stored Volume");
        
        resultPondVolume = new ResultStorageVolume(projsetting.getDuration(), objname + " Pond Volume");
        
        resultRunoff = new ResultFlow[ProjSetting.runoffCoefficients.getLength()];
        for (int i = 0; i < resultRunoff.length; i++) {
            resultRunoff[i] = new ResultFlow(projsetting.getDuration(), objname + ProjSetting.runoffCoefficients.getLandRunoffName(i));
        }
        resultDirectPrecip = new ResultFlow(projsetting.getDuration(), objname + " Direct Precip");
        
        resultEvaporation = new ResultFlow(projsetting.getDuration(), objname + " Evaporation");
        resultSeepage = new ResultFlow(projsetting.getDuration(), objname + " Seepage");
    }
    
    /**
     * @deprecated should use XML element instead
     * @param inData 
     */
    public void setFromString(String inData){
        String nextLine = System.getProperty("line.separator");
        String lines[] = inData.split(nextLine);
        /*
        if(lines.length < MIN_FILE_LENGTH){ to be set after getSaveString completed
            return;
        }
        */
        int lc = 0;
        objname = lines[lc].split("\t")[1];
        lc++;
        objSubType = lines[lc].split("\t")[1];
        lc++;
        x = Integer.parseInt(lines[lc].split("\t")[1]);
        y = Integer.parseInt(lines[lc].split("\t")[2]);
        lc++;
        
        //NOT COMPLETE
        
        
        
        setSubType(objSubType, "ACTIVE");
    }
    
    
    /**
     * Used to set sprite and dimensions of object for flowChartCad whenever
     * object Subtype is changed
     * @param inSubType
     * @param inState state from StatesAllowed, if no match is found then
     * imageLib returns a default sprite
     */
    public void setSubType(String inSubType, String inState) {
        objSubType = inSubType;
        objSprite = projsetting.getImageLib().getImage(objSubType, inState, scaleX, scaleY);
        hitBox.x = x - objSprite.getWidth() / 2;
        hitBox.y = y - objSprite.getHeight() / 2;
        hitBox.width = objSprite.getWidth();
        hitBox.height = objSprite.getHeight();
        ProjSetting.hasChangedSinceSave = true;
    }

    /**
     * Sets sprite image for the element from the imageLib class
     *
     * @param inState state from StatesAllowed, if no match is found then
     * imageLib returns a default sprite
     */
    public void setSpriteState(String inState) {
        objSprite = projsetting.getImageLib().getImage(objSubType, inState, scaleX, scaleY);
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
}
