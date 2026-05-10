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
package com.mcwbalance.project;

import com.mcwbalance.flowchart.ImageLib;
import com.mcwbalance.measure.Time;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This Class is for storage and retrieval of all key project software related
 * settings. Class contains static variables to allow all sub classes access.
 *
 * @author amcintyre
 * @version Pre-Alpha 0.2023.06.24
 */
public class ProjSetting {

    /**
     * Initial software version stamping, to be replaced eventually
     */
    public static String verInfo = "0.2026.01.01";

    /**
     * Short name for water balance for title of figure
     */
    String balancename;

    /**
     * Name of Client as would like to appear on figures
     */
    String clientname;

    /**
     * Total model time in days from start to finish for each run. Each day will
     * generate 1 result.
     */
    int duration; // 2 years  note this will be 3650 lines for a typical 10 year Mine Life.  

    /**
     * Name of save file including extension
     */
    String fileName;

    static final String DEFAULTS_PATH = java.io.File.separator + "ProjectDefaults.properties";

    /**
     * Name of project as would like to appear on figures
     */
    String projectname;

    /**
     * Project number as would like to appear on figures
     */
    String projectnumber;

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
     * Day for start of model in julien days (i.e. 1 to 365)
     */
    int startday;

    /**
     * Path of the default Title Block
     */
    String titleBlockPath;

    /**
     * Tracks what timestep the model is set to
     */
    Time.TimeUnit timestep;

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
    public ProjSetting() {

        Properties prop = new Properties();
        URL propURL = getClass().getResource(DEFAULTS_PATH);
        if (propURL != null) {
            try {
                prop.load(propURL.openStream());
            } catch (IOException ex) {
                System.getLogger(ProjSetting.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        } else {
            System.err.println("Error: Properties file not found at path " + DEFAULTS_PATH);
        }

        balancename = prop.getProperty("BALANCENAME", "BALANCE");
        clientname = prop.getProperty("CLIENTNAME", "CLIENT");
        fmtAnn = new DecimalFormat(prop.getProperty("FMTANN", "#,##0"));
        fmtDay = new DecimalFormat(prop.getProperty("FMTDAY", "#,##0.0"));
        fmtHr = new DecimalFormat(prop.getProperty("FMTHR", "#,##0.00"));
        fmtdt = DateTimeFormatter.ofPattern(prop.getProperty("FMTDATE", "yyyy/MM/dd HH:mm:ss"));
        fileName = prop.getProperty("FILENAME", "File.mcbl");
        /**
         * Number of time steps to run analysis
         */
        duration = Integer.parseInt(prop.getProperty("DURATION", "25"));
        /**
         * number of decimal places to use in actual calculations
         */
        precisionDay = Integer.parseInt(prop.getProperty("PRECISIONDAY", "1"));
        precisionHr = Integer.parseInt(prop.getProperty("PRECISIONHR", "2"));

        projectname = prop.getProperty("PROJECTNAME", "New Project");
        projectnumber = prop.getProperty("PROJECTNUMBER", "P001");

        String userHome = System.getProperty("user.home");
        savepathfolder = userHome + java.io.File.separator + prop.getProperty("SAVEFOLDER", "McBalance");

        startday = 1;
        titleBlockPath = prop.getProperty("TITLEBLOCKPATH", "/TitleBlock_Default.svg");
        timestep = Time.TimeUnit.Day;

        imageLib = new ImageLib();

        hasChangedSinceSave = true; // Debug, will update to false

    }

    /**
     * balance name for use in title block
     *
     * @return string containing he balance name
     */
    public String getBalanceName() {
        return balancename;
    }

    /**
     * client name for use in title block
     *
     * @return string containing the client name
     */
    public String getClientName() {
        return clientname;
    }

    /**
     * Used to set length of result arrays and duration of analysis loop note
     * this is intended to update dynamically if switching time step duration
     *
     * @return number of time steps in the model
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Used for formatting Annual results
     *
     * @return annual result format
     */
    public DecimalFormat getFmtAnn() {
        return fmtAnn;
    }

    /**
     * Used for formatting day results
     *
     * @return day format
     */
    public DecimalFormat getFmtDay() {
        return fmtDay;
    }

    /**
     * Used for formatting hr results
     *
     * @return hr format
     */
    public DecimalFormat getFmtHr() {
        return fmtHr;
    }

    /**
     * Container for sprite images
     *
     * @return Sprite image container
     */
    public ImageLib getImageLib() {
        return imageLib;
    }

    /**
     * Project reference name for use in title block
     *
     * @return string containing the project name
     */
    public String getProjectName() {
        return projectname;
    }

    /**
     * Project reference number for use in title block
     *
     * @return string containing the project number
     */
    public String getProjectNumber() {
        return projectnumber;
    }

    /**
     * Used for rounding and display
     *
     * @return number of decimals to display for day results
     */
    public int getPrecisionDay() {
        return precisionDay;
    }

    /**
     * Model save folder
     *
     * @return Model save folder
     */
    public File getPathFolder() {
        return new File(savepathfolder);
    }

    /**
     * Points to where the model will be saved
     *
     * @return File to save the model to
     */
    public File getSaveFile() {
        return new File(savepathfolder + java.io.File.separator + fileName);
    }

    /**
     * Start day of model in Julian days
     *
     * @return value between 1 and 365
     */
    public int getStartDay() {
        return startday;
    }

    /**
     * Current calculation time step i.e. hour, day, month, year
     *
     * @return calculation time step
     */
    public Time.TimeUnit getTimeStep() {
        return timestep;
    }

    /**
     * Active title block save path
     *
     * @return path to active title block
     */
    public String getTitleBlockPath() {
        return titleBlockPath;
    }

    /**
     * Builds a proprietary XML data set for use in save files This method may
     * not work for generating xml spreadsheet files
     *
     * @return
     * @throws ParserConfigurationException
     */
    public Document getXMLDoc() throws ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        doc.appendChild(getXMLEle(doc));
        return doc;

    }

    /**
     * Provides a Settings Element for inclusion to an XML Doc
     *
     * @param doc
     * @return
     */
    public Element getXMLEle(Document doc) {
        Element ele = doc.createElement("Settings");
        ele.setAttribute("ver", verInfo);
        ele.setAttribute("balacename", balancename);
        ele.setAttribute("clientname", clientname);
        //Climate Scenarios
        ele.setAttribute("duration", String.valueOf(duration));
        ele.setAttribute("timestep", String.valueOf(timestep));
        ele.setAttribute("startday", String.valueOf(startday));

        ele.setAttribute("fmtAnn", fmtAnn.toPattern());
        ele.setAttribute("fmtDay", fmtDay.toPattern());
        ele.setAttribute("fmtHr", fmtHr.toPattern());
        //ele.setAttribute("fmtdt",fmtdt.toPattern());
        //imageLib
        ele.setAttribute("precisionDay", String.valueOf(precisionDay));
        ele.setAttribute("precisionHr", String.valueOf(precisionHr));

        ele.setAttribute("projectname", projectname);
        ele.setAttribute("projectnumber", projectnumber);
        //runoffCoefficients

        return ele;
    }

    /**
     * TODO - IN PROGRESS - add formats, climate Scenarios and runoff coeff Used
     * for loading an existing project
     *
     * @param settingsXML Element containing all settings, in same format as
     * generated by getXMLElement
     */
    public void loadXMLElement(Element settingsXML) {

        //TODO - need to ensure each setting has a null check
        setBalanceName(settingsXML.getAttribute("balancename"));
        setClientName(settingsXML.getAttribute("clientname"));
        setDuration(Integer.parseInt(settingsXML.getAttribute("duration")));
        setTimeStep(Time.TimeUnit.valueOfIngoreCase(settingsXML.getAttribute("timestep")));
        setStartDay(Integer.parseInt(settingsXML.getAttribute("startday")));
        //setfmtAnn
        //setfmtDay
        //setfmtHr
        //setfmtdt

        precisionDay = (Integer.parseInt(settingsXML.getAttribute("precisionDay")));
        precisionHr = (Integer.parseInt(settingsXML.getAttribute("precisionHr")));

        setProjectName(settingsXML.getAttribute("projectname"));
        setProjectNumber(settingsXML.getAttribute("projectnumber"));

        //climate Scenarios and runoff coeff
        //TODO - FILL IN
    }

    /**
     * Limits name to 100 Char, removes leading and trailing spaces
     *
     * @param balancename
     */
    public void setBalanceName(String balancename) {
        String stripped = balancename.strip();

        if (stripped.length() < 100) {
            this.balancename = stripped;
        } else {
            this.balancename = stripped.substring(0, 100);
        }
    }

    /**
     * Limits client name to 50 Char, removed leading and trailing spaces
     *
     * @param clientname name of client for use in figures
     */
    public void setClientName(String clientname) {
        String stripped = clientname.strip();
        if (stripped.length() < 50) {
            this.clientname = stripped;
        } else {
            this.clientname = stripped.subSequence(0, 50).toString();
        }

    }

    /**
     * Sets duration of the analysis run
     *
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Limits name to 50 Char, removed leading and trailing spaces
     *
     * @param projectname name of project for use in figures
     */
    public void setProjectName(String projectname) {
        String stripped = projectname.strip();
        if (stripped.length() < 50) {
            this.projectname = stripped;
        } else {
            this.projectname = stripped.subSequence(0, 50).toString();
        }
    }

    /**
     * Limits project number to 50 char, removes leading and trailing spaces
     *
     * @param projectnumber
     */
    public void setProjectNumber(String projectnumber) {
        String stripped = projectnumber.strip();

        if (stripped.length() < 50) {
            this.projectnumber = stripped;
        } else {
            this.projectnumber = stripped.subSequence(0, 50).toString();
        }
    }

    /**
     * for setting the save path, no limits or controls implemented
     *
     * @param savepathfolder as a string c:\file\
     */
    public void setSavePath(String savepathfolder) {
        this.savepathfolder = savepathfolder;
    }

    /**
     * for setting the save path, no limits or controls implemented
     *
     * @param savepathfolder as a File c:\file\
     */
    public void setSavePath(File savepathfolder) {
        this.savepathfolder = savepathfolder.toString();
    }

    /**
     * Used when loading in a project sets both the directory and file names
     *
     * @param absolutePath
     */
    public void setSavePathandName(Path absolutePath) {
        this.savepathfolder = absolutePath.getParent().toString();
        this.fileName = absolutePath.getFileName().toString();

    }

    /**
     * sets start day from Julian day
     *
     * @param day between 1 and 365, does not recognize leap years
     */
    public void setStartDay(int day) {
        if (day > 365) {
            startday = day - 365 * (int) (day / 365);
        } else if (day < 1) {
            startday = 1;

        } else {
            startday = day;
        }
    }

    /**
     * Sets filename from a File
     *
     * @param savepathfolder
     */
    public void setFileName(File savepathfolder) {
        this.fileName = savepathfolder.getName();
    }

    /**
     * Used for selecting days, months or years as the calculation time step
     *
     * @param timestep
     */
    public void setTimeStep(Time.TimeUnit timestep) {
        this.timestep = timestep;
    }

}
