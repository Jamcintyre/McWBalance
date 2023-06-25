package com.mcwbalance;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
/**
 * Contains all information required to represent 1 ELM such as a Basin, Tailings Facility, Pit, Process Plant.
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
    public Boolean hasStorage;
    public int indexStorageTracker; // used to also index Direct Precips and Evaps; 
    public Boolean hasStorageEvapandPrecip; // used to indicate storage is exposed (as opposed to a closed top tank)
    public DataDAC dAC; // note will only be constructed if boolean hasStorage is selected; 
    
    public IndexList inflows;
    public IndexList outflows;

    public DataTimeIntSeries targetOperatingVol;
    public DataTimeDoubleSeries minDepth;
    public DataTimeDoubleSeries maxOpLevel;
    public DataTimeDoubleSeries overflowLevel;
    public DataTimeDoubleSeries crestLevel;
    
    public DataTailingsDepositionRateList depositionRates;
    
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
     * Used for selecting symbol on flowchart, will not be used in the calculations directly
     */
      public static String[] eLMStatesAllowed ={
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
      
    ObjELM(){// constructor
        x = 0;
        y = 0;
        hitBox = new Rectangle(0,0,20,20);
        isSelected = false;
        objname = "None";
        objSubType = "DEFAULT";
        scaleX = 1.0;
        scaleY = 1.0;
        objSubType = "DEFAULT";
        objSprite = ProjSetting.imageLib.getImage(objSubType, "ACTIVE", scaleX, scaleY);
        hitBox.x = x - objSprite.getWidth()/2;
        hitBox.y = y - objSprite.getHeight()/2;
        hitBox.width = objSprite.getWidth();
        hitBox.height = objSprite.getHeight();
        hasCatchment = false;
        nCatchments = 0;
        indexRunoffTracker = -1; // used when building the run settings to track output
        hasStorageEvapandPrecip = false;
        hasSolids = false;
        hasStorage = false;
        dAC = new DataDAC(1);
        
        targetOperatingVol = new DataTimeIntSeries(1);
        minDepth = new DataTimeDoubleSeries(1);
        maxOpLevel = new DataTimeDoubleSeries(1);
        overflowLevel = new DataTimeDoubleSeries(1);
        crestLevel = new DataTimeDoubleSeries(1);

        
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
        for (int i = 1; i < ProjSetting.MAX_STATES; i++){
            stateTime[i] = ProjSetting.MAX_DURATION;
        }
        state[0] = "ACTIVE";
    }
 
    public String getState(int day){
        if (day == -1){
            return state[0];
        }
        for (int i = 1; i < state.length; i ++){
            if (day < stateTime[i]){
                if(state[i-1] != null){
                    return state[i-1];
                }
            }
        }
        return state[0];
    }
    
    /**
     * Used to set sprite and dimensions of object for flowChartCad whenever 
     * object Subtype is changed
     */
    public void setSubType(String inSubType, String inState){
        objSubType = inSubType;
        objSprite = ProjSetting.imageLib.getImage(objSubType, inState, scaleX, scaleY);
        hitBox.x = x - objSprite.getWidth()/2;
        hitBox.y = y - objSprite.getHeight()/2;
        hitBox.width = objSprite.getWidth();
        hitBox.height = objSprite.getHeight();
    }
    /**
     * Sets sprite image for the element from the imageLib class
     * @param inState state from eLMStatesAllowed, if no match is found then imageLib returns a default sprite
     */
    public void setSpriteState(String inState){
        objSprite = ProjSetting.imageLib.getImage(objSubType, inState, scaleX, scaleY);
    }


}
