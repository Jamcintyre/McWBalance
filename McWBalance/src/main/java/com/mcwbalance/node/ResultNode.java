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
import com.mcwbalance.project.Project;
import com.mcwbalance.result.ResultFlow;
import com.mcwbalance.result.ResultLevel;
import com.mcwbalance.result.ResultStorageVolume;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * TODO - NOT COMPLETED
 * Holds and handles all the results from a single climate scenario run for a node
 * @author alex
 */
public class ResultNode {
    
    ResultLevel lvlPond;
    ResultLevel lvlSolids;
    
    ResultStorageVolume volPond;
    ResultStorageVolume volSolids;
    
    ResultFlow inSolids;
    
    ResultFlow inDirectPrecip;
    ResultFlow inRunoffandMeltbyLC[];
    ResultFlow inRunoffandMelt;
    
    
    
    ResultFlow outEvaporation;
    ResultFlow outSeepage;
    ResultFlow outVoidloss;
    

    private final int nodeIndex;
    private final String name;
    private final String scenario;
        
    /**
     * Holds and handles all the results from a single climate scenario run for a node
     * @param nodeIndex index of node within NodList
     * @param nodeName String name of node for reporting
     * @param scenario String name of scenario for reporting

     */
    public ResultNode(int nodeIndex, String nodeName, String scenario) {
        this.nodeIndex = nodeIndex;
        this.name = nodeName;
        this.scenario = scenario;
    }
    
    /**
     * Initializes results based on the provided project
     * TODO - should only initialize result arrays that need to exist to save on memory
     * @param aP 
     */
    final void initResults(Project aP){

        inDirectPrecip = new ResultFlow(aP.getProjectSetting().getDuration(), name + " " + McWBalance.langRB.getString("DIRECT_PRECIP"));
        inRunoffandMelt = new ResultFlow(aP.getProjectSetting().getDuration(), name + " " + McWBalance.langRB.getString("RUNOFF_AND_MELT"));
        inRunoffandMeltbyLC = new ResultFlow[aP.runoffCoeffs.getLength()];
        for (int i = 0; i < inRunoffandMeltbyLC.length; i++) {
            inRunoffandMeltbyLC[i] = new ResultFlow(aP.getProjectSetting().getDuration(),
                    name + " " + McWBalance.langRB.getString("RUNOFF_AND_MELT")
                    + " " + aP.climateTable.getValueAt(i, 0).toString());
        }
        
        lvlSolids = new ResultLevel(aP.getProjectSetting().getDuration(), name + " " + McWBalance.langRB.getString("SOLIDS_EL"));
        lvlPond = new ResultLevel(aP.getProjectSetting().getDuration(), name + " " + McWBalance.langRB.getString("POND_EL"));
        
        volPond = new ResultStorageVolume(aP.getProjectSetting().getDuration(), name + " " + McWBalance.langRB.getString("POND_VOLUME"));
        volSolids = new ResultStorageVolume(aP.getProjectSetting().getDuration(), name + " " + McWBalance.langRB.getString("SOLIDS_VOLUME"));
        
        outEvaporation = new ResultFlow(aP.getProjectSetting().getDuration(), name + " " + McWBalance.langRB.getString("EVAPORATION"));
        outSeepage = new ResultFlow(aP.getProjectSetting().getDuration(), name + " " + McWBalance.langRB.getString("SEEPAGE"));
        outVoidloss = new ResultFlow(aP.getProjectSetting().getDuration(), name + " " + McWBalance.langRB.getString("VOID_LOSS"));
        
        
    }
    
    /**
     * TODO Used for generating a CSV representation of the Result
     *
     * @return
     */
    public String getCSVString() {
        return "ResultNode.java getCSVString() method not implemented";
    }

    /**
     * TODO Used for generating a XML representation of the Result for Saving
     * only Not to be used for Export to XML Spreadsheet
     *
     * @param doc XML document that Element will be appended to
     * @return XML representation of the Result for Saving only, not for XML
     * Spreadsheets
     */
    public Element getXMLElement(Document doc) {
        Element ele = doc.createElement("NodeResult");
        ele.setAttribute("Status", "ResultNode.java getXMLElement() method not implemented");
        return ele;
    }

    /**
     * TODO Used for generating a XML representation of the Result for Exporting
     * to Spreadsheet
     *
     * @param doc XML document that Element will be appended to
     * @return XML representation of the Result for Saving only, not for XML
     * Spreadsheets
     */
    public Element getXMLElementSS(Document doc) {
        Element ele = doc.createElement("NodeResult");
        ele.setAttribute("Status", "ResultNode.java getXMLElementSS() method not implemented");
        return ele;
    }
    /**
     * TODO - Not Completed
     * Loads result in from a save file XML element, this will not work with a Spreadsheet XML
     * @see getXMLElement
     * @param ele XML representation of the Result generated by getXMLElement
     * 
     */
    public void loadXMLElement(Element ele) {
        System.err.println("ResultNode.java loadXMLElement() method not implemented");
    }
    

    
    /**
     * Used for getting NODList index of node
     * @return NODList index of node
     */
    public int getnodeIndex(){
        return nodeIndex;
    }
    
    /**
     * Used for getting name of scenario
     * @return Name of Climate Scenario
     */
    public String getscenario(){
        return scenario;
    }
}
