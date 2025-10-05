package com.mcwbalance.project;

import com.mcwbalance.solve.SolveOrder;
import com.mcwbalance.flowchart.ImageLib;
import com.mcwbalance.landcover.TableRunoffCoefficients;
import com.mcwbalance.climate.TableClimateScenarios;
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
    
    /**
     * Initial software version stamping, to be replaced eventually
     */
    public static String verInfo ="0.2025.10.04";
    
    /**
     * Short name for water balance for title of figure
     */
    String balancename;
    
    /**
     * Name of Client as would like to appear on figures
     */
    String clientname;
    
    /**
     * Total model time in days from start to finish for each run. Each day will generate 1 result. 
     */
    int duration; // 2 years  note this will be 3650 lines for a typical 10 year Mine Life.  

    /**
     * Name of save file including extension
     */
    String fileName;

    /**
     * Name of project as would like to appear on figures
     */
    String projectname;
    
    /**
     * Project number as would like to appear on figures
     */
    String projectnumber;

    /**
     * Runoff Coefficients for use in calculation
     */
    public static TableRunoffCoefficients runoffCoefficients;
    
    /**
     * Available sets of climate data precip evap etc. to use in calcs
     */
    public static TableClimateScenarios climateScenarios;
    
    /**
     * Container for various icons to use on the flowsheet
     */
    ImageLib imageLib; 
    
    /**
     * Used to flag if a save should be suggested on close of the program
     */
    public static boolean hasChangedSinceSave = true; // Debug, will update to false
    
    /**
     * Full path of where the save file for the current model is to be saved
     */
    String savepathfolder;
        
    /**
     * Number of decimals to be used in calculations
     */
    int precisionDay; // number of decimal places to use in actual calcuations;
    
    /**
     * Number of decimals for use in calculations
     */
    int precisionHr; 
    
    /**
     * Format to use when displaying annual result values
     */
    DecimalFormat fmtAnn;
    
    /**
     * Format to use when displaying daily result values
     */
    DecimalFormat fmtDay;
    
    /**
     * Format to use when displaying hourly result values
     */
    DecimalFormat fmtHr;
    
    /**
     * Format to use when displaying date and time
     */
    DateTimeFormatter fmtdt;
    
    /**
     * initiates project settings to default values
     */
    public ProjSetting(){
        
        balancename = "SITE WATER BALANCE";
        
        fmtAnn = new DecimalFormat("#,##0");
        
        fmtDay = new DecimalFormat("#,##0.0");
        
        fmtHr = new DecimalFormat("#,##0.00");
        
        fmtdt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        
        fileName = "TestFile.mcbl";
        
        clientname = "CLIENT NAME";
        
        duration = 25; 
    
        precisionDay = 1; // number of decimal places to use in actual calcuations;
        
        precisionHr = 2; 
        
        projectname = "PROJECT NAME";
        
        projectnumber = "NB101-###_#";
        
        savepathfolder = "C:\\Temp\\McBalance\\";
        
        runoffCoefficients = new TableRunoffCoefficients();
        
        climateScenarios = new TableClimateScenarios(1);
        
        imageLib = new ImageLib(); 
        
        hasChangedSinceSave = true; // Debug, will update to false
        
    }
    
    /**
     * @deprecated method is no longer static. 
     */
    public void resetDefaults() {
        clientname = "CLIENT NAME";
        projectname = "PROJECT NAME";
        projectnumber = "NB101-###_#";
        balancename = "SITE WATER BALANCE";
        runoffCoefficients = new TableRunoffCoefficients();
        climateScenarios = new TableClimateScenarios(1);
    }
    
    public StringBuilder getSaveString(){
        StringBuilder savestring = new StringBuilder();
        String nextLine = System.getProperty("line.separator");
        savestring.append("Client");
        savestring.append("\t");
        savestring.append(clientname);
        savestring.append(nextLine);
        
        savestring.append("Project");
        savestring.append("\t");
        savestring.append(projectname);
        savestring.append(nextLine);
        
        savestring.append("Project No");
        savestring.append("\t");
        savestring.append(projectnumber);
        savestring.append(nextLine);
        
        savestring.append("Balance Name");
        savestring.append("\t");
        savestring.append(balancename);
        savestring.append(nextLine);
        
        savestring.append("Runoff Coefficients");
        savestring.append(nextLine);
        savestring.append(runoffCoefficients.toString());
        savestring.append(nextLine);
        
        return savestring;
    }

    
    public String getBalanceName(){
        return balancename;
    }
    
    public String getClientName(){
        return clientname;
    }
    
    public int getDuration(){
        return duration; 
    }
    
    public DecimalFormat getFmtAnn(){
        return fmtAnn;
    }
    
    public DecimalFormat getFmtDay(){
        return fmtDay;
        
    }
    public DecimalFormat getFmtHr(){
        return fmtHr;
    }
    
    
    public ImageLib getImageLib(){
        return imageLib;
    }
    
    public String getProjectName(){
        return projectname;
    }
    
    public String getProjectNumber(){
        return projectnumber;
    }
    
    public int getPrecisionDay(){
        return precisionDay;
    }

    public File getPathFolder(){
        return new File(savepathfolder);
    }

    public File getSaveFile(){
        return new File(savepathfolder+fileName);
    }
    
    /**
     * Limits name to 100 Char, removes leading and trailing spaces
     * @param balancename 
     */
    public void setBalanceName(String balancename){
        String stripped = balancename.strip();
        
        if (stripped.length() < 100){
            this.balancename = stripped;
        }else{
            this.balancename = stripped.substring(0, 100);
        }
    }
    
    /**
     * Limits client name to 50 Char, removed leading and trailing spaces
     * @param clientname name of client for use in figures
     */
    public void setClientName(String clientname){
        String stripped = clientname.strip();   
        if (stripped.length() < 50){
            this.clientname = stripped;
        } else{
            this.clientname = stripped.subSequence(0, 50).toString();
        }
        
    }
    
    /**
     * Sets duration of the analysis run
     * @param duration 
     */
    public void setDuration(int duration){
        this.duration = duration;
    }
    
    /**
     * Limits name to 50 Char, removed leading and trailing spaces
     * @param projectname name of project for use in figures
    */
    public void setProjectName(String projectname){
        String stripped = projectname.strip();
        if (stripped.length() < 50){
            this.projectname = stripped;
        }else{
            this.projectname = stripped.subSequence(0, 50).toString();
        }
    }
    
    /**
     * Limits project number to 50 char, removes leading and trailing spaces
     * @param projectnumber 
     */
    public void setProjectNumber(String projectnumber){
        String stripped = projectnumber.strip();
        
        if(stripped.length() < 50){
            this.projectnumber = stripped;
        }else{
            this.projectnumber = stripped.subSequence(0, 50).toString();
        }
    }
    
    /**
     * Placeholder to be implemented 
     * @param setString 
     */
    public void setFromString(String setString){
        
        
    }
    
    /**
     * for setting the save path, no limits or controls implemented
     * @param savepathfolder as a string c:\file\
     */
    public void setSavePath(String savepathfolder){
        this.savepathfolder = savepathfolder;
    }
    
    /**
     * for setting the save path, no limits or controls implemented
     * @param savepathfolder as a File c:\file\
     */
    public void setSavePath(File savepathfolder){
        this.savepathfolder = savepathfolder.toString();
    }
    
    /**
     * Sets filename from a File
     * @param savepathfolder 
     */
    public void setFileName(File savepathfolder){
        this.fileName = savepathfolder.getName();
    }
    
    
    
    
    
}
