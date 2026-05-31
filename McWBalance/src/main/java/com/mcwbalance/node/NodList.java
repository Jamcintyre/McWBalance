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

import com.mcwbalance.project.Project;
import com.mcwbalance.settings.Limit;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Contains a complete array of all Nodes present in the model as well as
 * placeholder values.
 *
 * @see Nod
 * @author amcintyre
 */
public class NodList {

    /**
     * counter to keep track fo list size
     */
    public int count;
    /**
     * Array of nodes, may replace with ArrayList
     */
    public Nod[] nodes = new Nod[Limit.MAX_NODES];

    /**
     * Constructor initializes a blank array of Nodes of generally 0 values to
     * avoid issues with null values
     *
     * @param aP active project
     */
    public NodList(Project aP) {
        count = 0; // sets number of elements to 0 to start
        for (int i = 0; i < Limit.MAX_NODES; i++) {
            nodes[i] = new Nod(aP); // Constructs the node so there is a place in memory for it, only needed for Object arrays
            nodes[i].x = 0;
            nodes[i].y = 0;
            nodes[i].objname = "Node " + i;
            nodes[i].objSubType = "NODE"; // defaults to ugly square that needs to be defined
        }
    }

    /**
     * Intended to be used on a freshly constructed null TRNList Note it assumes
     * project settings has already been set
     *
     * @param nodeXML input XML element containing node data for all nodes
     * @param aP Active project
     */
    public void addXMLElements(Element nodeXML, Project aP) {

        NodeList cnl = nodeXML.getElementsByTagName("Node");
        for (int i = 0; i < cnl.getLength(); i++) {
            if (cnl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                nodes[count] = new Nod(aP, (Element) cnl.item(i));
                count++;
            }
        }
    }

    /**
     * Adds a new Node to the end of the Node List
     *
     * @param inX Midpoint of the Node draw location used in FlowChartCAD class
     * @param inY Midpoint of the Node draw location used in FlowChartCAD class
     * @param aP Active Project
     * @return 0 is returned of successful, -1 if unsuccessful
     */
    public int addNode(int inX, int inY, Project aP) {
        if (count - 1 == Limit.MAX_NODES) { // limits object addtion to max number of objects -1 as last object needs to be null for delete to work
            System.out.println("Max Number of Objects");
            return -1; // returns -1 as error code, object could not be added
        }
        nodes[count] = new Nod(inX, inY, count + 1, aP);
        count++; // increments to next element, only allowed if not at maximum
        return 0;
    }
    
    /**
     * Returns node at index location 
     * @param index of node to get
     * @return node at index location
     */
    public Nod get(int index){
        return this.nodes[index];
    }

    /**
     * Used for picking objects in Transfer
     *
     * @return A list of all Nodes currently active in the Node list. "None" is
     * included as a first entry for cases where the user would prefer to select
     * no Node.
     */
    public String[] getNameList() {
        String[] nameList = new String[count + 1];
        nameList[0] = "None"; // null value required for edit menu
        for (int i = 0; i < count; i++) {
            nameList[i + 1] = nodes[i].objname;
        }
        return nameList;
    }
    
    /**
     * used to see if a land cover name has an area assigned to it
     * intended to prevent accidental deletion of landcover
     * @param name of landcover
     * @return 2 column array [i][0] = Index of node that uses land cover
     * [i][1] = use of area, 1 = basin, 2 = upstream, 3 = both
     */
    public int[][] getLandCoverUsed(String name){
        record val(int index, int useage){}
        ArrayList<val> usage = new ArrayList<>();
        for(int n = 0; n < this.count; n++){
            int use = nodes[n].getLandCoverUsed(name);
            if (use > 0){
                usage.add(new val(n, use));
            }
        }
        if(usage.isEmpty()){
            return null;
        }
        int usagearr[][] = new int[usage.size()][2];
        for(int u = 0; u < usage.size(); u++){
            usagearr[u][0] = usage.get(u).index;
            usagearr[u][1] = usage.get(u).useage;
        }
        return usagearr;
        
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
        Document xMLDoc = builder.newDocument();

        Element root = xMLDoc.createElement("Nodes");
        xMLDoc.appendChild(root);
        for (int i = 0; i < count; i++) {
            root.appendChild(nodes[i].getXMLElement(xMLDoc, i));
        }
        return xMLDoc;
    }

    /**
     * Removes the requested Node from the Node list and shifts all following
     * elements up 1 index. Method does not modify Linking in the TRN list. i.e.
     * if Node 4 is deleted the any Transfers to Node 4, 5, 6 etc.. are not
     * automatically updated
     *
     * @param inNumber Index value of the Node to be removed from the list
     */
    public void removeNode(int inNumber) {// removes object from list and shifts proceeding objects up
        for (int i = inNumber; i < count; i++) {
            nodes[i] = nodes[i + 1];
        }
        count--;
    }

    /**
     * removes all links from a each Node to and from a specified TRN
     *
     * @param rTRN
     */
    public void removeTRN(int rTRN) {
        for (int i = 0; i < count; i++) {
            nodes[i].remove(rTRN);
        }
    }
    
    /**
     * Used for replacing a landCover Name, note this assumes that name is correct
     * and will not confirm if option exists 
     * @param oldName name to replace
     * @param newName new name
     */
    public void renameLandCover(String oldName, String newName){
        for (int n = 0; n < this.size(); ++n){
            this.nodes[n].renameLandCover(oldName, newName);
        }
    }

    /**
     * Used to set the values of an Node entry, typically with data passed back
     * from ObjNodeWindow. Method does not modify the number of Nodes active on
     * the list. If 5 elements are present and element 6 is modified, the data
     * will be modified however the functions calling on NodeList will generally
     * ignore Nodes 6, 7, 8 ... unless the count is updated
     *
     * @param inNumber Index number of element to be
     * @param inNode Populated Node data to be used to overwrite the selected
     * index
     */
    public void set(int inNumber, Nod inNode) {
        nodes[inNumber] = inNode;
    }
    
    /**
     * Used for solving environmental inputs that are not depended on other inputs
     * @param step time step
     * @param cs Climate Scenario Index 
     * @param aP active Project
     */
    public void solveEnvironmentals(int step, int cs, Project aP){
        for (int n = 0; n < this.size(); n++){
            this.get(n).solveEnvironmentals(step, cs, aP);
        }
    }
    
    /**
     * For getting size of the contained data, intention is to replace count 
     * @return 
     */
    public int size(){
        return count; 
    }
    



    /**
     * Method not yet complete. This method will take a string representation of
     * the Node list. This method will not preserve Node Index numbers, and will
     * take the index based on order provided
     *
     * @param loadFile String representing all element data to be written to the
     * NodeList
     * @param overWrite if true data NodeList will be overwritten with loaded
     * data. if false Nodes will be appended without links transfers
     */
    public void setFromString(String loadFile, Boolean overWrite) {

    }

}
