package com.mcwbalance;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Contains all information required to represent 1 ELM such as a Basin,
 * Tailings Facility, Pit, Process Plant.
 *
 * @see ObjELMList
 * @author amcintyre
 */
class ObjELM {

    static final int MAX_DEPO_RATES = 10;
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
    public DataCatchment[] Catchment = new DataCatchment[ProjSetting.MAX_LAND_COVERS + 1]; // needs a totalizer value.
    public int indexRunoffTracker;
    public Boolean hasSolids;
    public int indexSolidsTracker;
    public int oSetXVoids;
    public int oSetYVoids;
    public Boolean hasStorage;
    public Boolean showStorage;
    public int indexStorageTracker; // used to also index Direct Precips and Evaps; 
    public int oSetXStorage;
    public int oSetYStorage;
    
    public Boolean hasStorageEvapandPrecip; // used to indicate storage is exposed (as opposed to a closed top tank)
    public DataDAC dAC; // note will only be constructed if boolean hasStorage is selected; 

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
    /**
     * Used for selecting symbol on flowchart, will not be used in the
     * calculations directly
     */
    public static String[] eLMStatesAllowed = {
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

    public int stateTime[] = new int[ProjSetting.MAX_STATES];
    public String state[] = new String[ProjSetting.MAX_STATES];

    ObjELM() {
        this(0, 0, 0);
    }

    ObjELM(int inX, int inY, int number) {
        x = inX;
        y = inY;
        hitBox = new Rectangle(0, 0, 20, 20);
        isSelected = false;
        objname = "NEW ELEMENT " + number;
        objSubType = "DEFAULT";
        scaleX = 1.0;
        scaleY = 1.0;
        objSubType = "DEFAULT";
        objSprite = ProjSetting.imageLib.getImage(objSubType, "ACTIVE", scaleX, scaleY);
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
        dAC = new DataDAC(1);

        targetOperatingVol = new DataTimeIntSeries(1);
        minDepth = new DataTimeDoubleSeries(1);
        maxOpLevel = new DataTimeDoubleSeries(1);
        overflowLevel = new DataTimeDoubleSeries(1);
        crestLevel = new DataTimeDoubleSeries(1);
        
        depositionRates = new TableTailingsDepositionRates();

        inflows = new IndexList(ProjSetting.MAX_TRNS);
        outflows = new IndexList(ProjSetting.MAX_TRNS);
        tailsTRNOptions = new IndexList(ProjSetting.MAX_TRNS);
        tailsTRN = -1;

        inflowFixedTRN = new IndexList(ProjSetting.MAX_TRNS);
        outflowFixedTRN = new IndexList(ProjSetting.MAX_TRNS);
        inflowOnDemandTRN = new IndexList(ProjSetting.MAX_TRNS);
        outflowOnDemandTRN = new IndexList(ProjSetting.MAX_TRNS);

        overflowOptions = new IndexList(ProjSetting.MAX_TRNS);
        overflowTRN = -1;

        stateTime[0] = -1;
        for (int i = 1; i < ProjSetting.MAX_STATES; i++) {
            stateTime[i] = ProjSetting.MAX_DURATION;
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
        saveString.append("Element Name" + "\t");
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
     */
    public void setSubType(String inSubType, String inState) {
        objSubType = inSubType;
        objSprite = ProjSetting.imageLib.getImage(objSubType, inState, scaleX, scaleY);
        hitBox.x = x - objSprite.getWidth() / 2;
        hitBox.y = y - objSprite.getHeight() / 2;
        hitBox.width = objSprite.getWidth();
        hitBox.height = objSprite.getHeight();
        ProjSetting.hasChangedSinceSave = true;
    }

    /**
     * Sets sprite image for the element from the imageLib class
     *
     * @param inState state from eLMStatesAllowed, if no match is found then
     * imageLib returns a default sprite
     */
    public void setSpriteState(String inState) {
        objSprite = ProjSetting.imageLib.getImage(objSubType, inState, scaleX, scaleY);
    }

    /**
     * method used to purge references of a specific transfer from any settings
     * within the ELM
     *
     * @param rTRN
     */
    public void removeTRN(int rTRN) {
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
