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

/**
 * Contains all information required to represent 1 Node such as a Basin,
 * Tailings Facility, Pit, Process Plant.
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
    
    public ResultLevel resultWaterLevel;
    public ResultLevel resultSolidsLevel;
    
    public ResultStorageVolume resultTotalVolume;
    public ResultStorageVolume resultPondVolume;
    
    public ResultFlow resultSolidsInflow;
    
    public ResultFlow resultRunoff[];
    public ResultFlow resultDirectPrecip;
    public ResultFlow resultEvaporation;
    public ResultFlow resultSeepage;
    
    
    

    Nod(ProjSetting projSetting) {
        this(0, 0, 0, projSetting);
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
    
    public StringBuilder getSaveString(){
        String nextLine = System.getProperty("line.separator");// used instead of /n for cross platform compatibility
        StringBuilder saveString = new StringBuilder();
        saveString.append("Node Name" + "\t");
        saveString.append(objname);
        saveString.append(nextLine); 
        saveString.append("SubType" + "\t");
        saveString.append(objSubType);
        saveString.append(nextLine);
        saveString.append("XYCoords" + "\t");
        saveString.append(x);
        saveString.append("\t");
        saveString.append(y);
        saveString.append(nextLine);
        
        //TO BE COMPLETED
        
        
        
        
        return saveString;
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
