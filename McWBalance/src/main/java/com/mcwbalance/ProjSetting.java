package com.mcwbalance;

import java.io.File;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

/**
* This Class is for storage and retrieval of all key project software related settings. 
* Class contains static variables to allow all sub classes access.
* @author amcintyre
* @version Pre-Alpha 0.2023.06.24
*/
public class ProjSetting {
    static String verInfo ="0.2023.06.24";
    /**
     * Note this is a placeholder, should set this on project start to whatever the previous save was
     * will add an info file that will save beside the Program for this kind of thing
     * 
     */
    static File pathFolder = new File("C:\\Temp\\McBalance\\"); 
    static File pathFile = new File("C:\\Temp\\McBalance\\TestFile.mcbl");
    static String clientName = "CLIENT NAME";
    static String projectName = "PROJECT NAME";
    static String projectNumber = "NB101-###_#";
    static String balanceName = "SITE WATER BALANCE";
    
    static TableRunoffCoefficients runoffCoefficients = new TableRunoffCoefficients();
    
    static ImageLib imageLib = new ImageLib(); 
    
    static boolean hasChangedSinceSave = true; // Debug, will update to false
    
    /**
     * Total model time in days from start to finish for each run. Each day will generate 1 result. 
    */
    static int duration = 25; // 2 years  note this will be 3650 lines for a typical 10 year Mine Life.  

    static BalanceRunSetting[] balanceRunSettings; // list of runs
    


    // Project Limitations
    /**
     * Limit of how many ELMs (Basins, Process Plants, Ponds, Open Pits, Etc.) can be present in the model. Limit is needed
     * as ELMList is not of fixed length
     * @see ObjELMList
     */
    static final int MAX_DURATION = 365*10;
    static final int MAX_ELMS = 20;
    static final int MAX_TRNS = 40;
    static final int MAX_LEVELS = 20;
    static final int MAX_LAND_COVERS = 15; 
    static final int MAX_STATES = 10;
    static final String LIST_TERMINATOR = "---END---";
    
    // Misc Settings for Formatting
    static int precisionDay = 1; // number of decimal places to use in actual calcuations;
    static int precisionHr = 2; 
    static DecimalFormat fmtAnn = new DecimalFormat("#,##0");
    static DecimalFormat fmtDay = new DecimalFormat("#,##0.0");
    static DecimalFormat fmtHr = new DecimalFormat("#,##0.00");
    
    static DateTimeFormatter fmtdt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    

    /**
     * This method replaces the constructor as this class is fully static
     */
    static void resetDefaults(){
    clientName = "CLIENT NAME";
    projectName = "PROJECT NAME";
    projectNumber = "NB101-###_#";
    balanceName = "SITE WATER BALANCE";
    runoffCoefficients = new TableRunoffCoefficients();
        
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
