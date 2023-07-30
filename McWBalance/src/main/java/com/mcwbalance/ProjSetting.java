package com.mcwbalance;

import com.mcwbalance.flowchart.ImageLib;
import com.mcwbalance.landcover.TableRunoffCoefficients;
import com.mcwbalance.climate.TableClimateScenarios;
import java.io.File;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
* This Class is for storage and retrieval of all key project software related settings. 
* Class contains static variables to allow all sub classes access.
* @author amcintyre
* @version Pre-Alpha 0.2023.06.24
*/
public class ProjSetting {
    public static String verInfo ="0.2023.07.29";
    /**
     * Note this is a placeholder, should set this on project start to whatever the previous save was
     * will add an info file that will save beside the Program for this kind of thing
     * 
     */
    public static File pathFolder = new File("C:\\Temp\\McBalance\\"); 
    public static File pathFile = new File("C:\\Temp\\McBalance\\TestFile.mcbl");
    public static String clientName = "CLIENT NAME";
    public static String projectName = "PROJECT NAME";
    public static String projectNumber = "NB101-###_#";
    public static String balanceName = "SITE WATER BALANCE";
    
    public static TableRunoffCoefficients runoffCoefficients = new TableRunoffCoefficients();
    
    public static TableClimateScenarios climateScenarios = new TableClimateScenarios(1);
    public static ImageLib imageLib = new ImageLib(); 
    
    
    public static boolean hasChangedSinceSave = true; // Debug, will update to false
    
    /**
     * Total model time in days from start to finish for each run. Each day will generate 1 result. 
    */
    public static int duration = 25; // 2 years  note this will be 3650 lines for a typical 10 year Mine Life.  

    public static SolveOrder solveOrder = new SolveOrder();
    


    // Project Limitations
    /**
     * Limit of how many ELMs (Basins, Process Plants, Ponds, Open Pits, Etc.) can be present in the model. Limit is needed
     * as ELMList is not of fixed length
     * @see ObjELMList
     */
    public static final int MAX_DURATION = 365*10;
    public static final int MAX_ELMS = 20;
    public static final int MAX_TRNS = 40;
    public static final int MAX_LEVELS = 20;
    public static final int MAX_LAND_COVERS = 15; 
    public static final int MAX_STATES = 10;
    public static final String LIST_TERMINATOR = "---END---";
    public static final int MAX_DEPO_RATES = 10;
    
    // Misc Settings for Formatting
    public static int precisionDay = 1; // number of decimal places to use in actual calcuations;
    public static int precisionHr = 2; 
    public static DecimalFormat fmtAnn = new DecimalFormat("#,##0");
    public static DecimalFormat fmtDay = new DecimalFormat("#,##0.0");
    public static DecimalFormat fmtHr = new DecimalFormat("#,##0.00");
    
    public static DateTimeFormatter fmtdt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    

    /**
     * This method replaces the constructor as this class is fully static
     */
    public static void resetDefaults() {
        clientName = "CLIENT NAME";
        projectName = "PROJECT NAME";
        projectNumber = "NB101-###_#";
        balanceName = "SITE WATER BALANCE";
        runoffCoefficients = new TableRunoffCoefficients();
        climateScenarios = new TableClimateScenarios(1);
        

    }
    
    public static StringBuilder getSaveString(){
        StringBuilder savestring = new StringBuilder();
        String nextLine = System.getProperty("line.separator");
        savestring.append("Client");
        savestring.append("\t");
        savestring.append(clientName);
        savestring.append(nextLine);
        
        savestring.append("Project");
        savestring.append("\t");
        savestring.append(projectName);
        savestring.append(nextLine);
        
        savestring.append("Project No");
        savestring.append("\t");
        savestring.append(projectNumber);
        savestring.append(nextLine);
        
        savestring.append("Balance Name");
        savestring.append("\t");
        savestring.append(balanceName);
        savestring.append(nextLine);
        
        savestring.append("Runoff Coefficients");
        savestring.append(nextLine);
        savestring.append(runoffCoefficients.toString());
        savestring.append(nextLine);
        
        return savestring;
    }
    
    public static void setFromString(String setString){
        
        
        
    }
}
