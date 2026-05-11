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
package com.mcwbalance.transfer;

import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.settings.Limit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * For managing a list of transfers
 *
 * @author Alex McIntyre
 */
public class TRNList {

    /**
     * counter to keep track fo list size
     */
    public int count;
    /**
     * oversized array to manage all possible transfers TODO - Rework to Array
     * list
     */
    public TRN[] tRNs = new TRN[Limit.MAX_TRNS];

    /**
     * Used to initialize an empty list of transfers
     */
    public TRNList() { // Constructor sets initial values
        count = 0; // sets number of transfers to 0 to start
        for (int i = 0; i < Limit.MAX_TRNS; i++) {
            tRNs[i] = new TRN(); // Constructs the element so there is a place in memory for it, only needed for Object arrays
            tRNs[i].x = 0;
            tRNs[i].y = 0;

            tRNs[i].objname = "Transfer " + i;
            tRNs[i].inObjNumber = -1; // -1 is used as Null, first object number is 0;
            tRNs[i].outObjNumber = -1; // -1 is used as Null, first object number is 0; 
        }
    }

    /**
     * Intended to be used on a freshly constructed null TRNList
     *
     * @param transfers root element from tranfers.xml
     */
    public void addXMLElements(Element transfers) {
        NodeList cnl = transfers.getElementsByTagName("Transfer");
        for (int i = 0; i < cnl.getLength(); i++) {
            if (cnl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                tRNs[count] = new TRN((Element) cnl.item(i));
                count++;
            }
        }
    }

    /**
     * Used for adding a new blank transfer into the transfer list limits object
     * addition to max number of objects -1 as last object needs to be null for
     * delete to work
     *
     * @param inX
     * @param inY
     */
    public void addTRN(int inX, int inY) {
        if (count - 1 == Limit.MAX_TRNS) { // limits object addtion to max number of objects -1 as last object needs to be null for delete to work
            System.err.println("Max Number of Tranfers Reached");
        }
        tRNs[count] = new TRN(inX, inY, count + 1);
        count++; // increments to next element, only allowed if not at maximum
    }

    /**
     * Provides an array of strings containing the name of each transfer object.
     * If not list is given then all names are returned
     *
     * @param includeNone include null transfers
     * @return
     */
    public String[] getNameList(boolean includeNone) { // used for picking objects 
        String[] nameList;

        if (includeNone) {
            nameList = new String[count + 1];
            nameList[0] = "None"; // null value required for edit menu
            for (int i = 0; i < count; i++) {
                nameList[i + 1] = tRNs[i].objname;
            }
        } else {
            nameList = new String[count];
            for (int i = 0; i < count; i++) {
                nameList[i] = tRNs[i].objname;
            }
        }
        return nameList;
    }

    /**
     * Provides an array of strings containing the name of each transfer object.
     * If not list is given then all names are returned
     *
     * @param indexList list of transfers to get
     * @return
     */
    public String[] getNameList(int indexList[]) {
        String[] nameList = new String[indexList.length];
        for (int i = 0; i < indexList.length; i++) {
            if (indexList[i] == -1) {
                nameList[i] = "None";
            } else {
                nameList[i] = tRNs[indexList[i]].objname;
            }
        }
        return nameList;
    }

    /**
     * Used for determining which Transfers inflow to an element
     *
     * @param eLMnumber Index value of the ELM that has inflows
     * @param includeNone include null transfers
     * @return a list of Inflows associated with the ELM, the first value is set
     * to None to avoid null
     */
    public String[] getInflowNameList(int eLMnumber, boolean includeNone) { // used for picking objects 
        int subcount = 0;
        for (int i = 0; i < count; i++) { // first itteration is to size array
            if (tRNs[i].outObjNumber == eLMnumber) {
                subcount++;
            }
        }
        String[] nameList;

        if (includeNone) {
            nameList = new String[subcount + 1];
            nameList[0] = "None"; // null value required for some comboBoxes
            int j = 1; // starts at 1 since 0 is set to None. 
            for (int i = 0; i < count; i++) {
                if (tRNs[i].outObjNumber == eLMnumber) {
                    nameList[j] = tRNs[i].objname;
                    j++; // secondary counter for use in the smaller list
                }
            }
        } else {
            nameList = new String[subcount];
            int j = 0; // starts at 0 since no None value is requested
            for (int i = 0; i < count; i++) {
                if (tRNs[i].outObjNumber == eLMnumber) {
                    nameList[j] = tRNs[i].objname;
                    j++; // secondary counter for use in the smaller list
                }
            }
        }
        return nameList;
    }

    /**
     * Used for determining which Transfers inflow to an element
     *
     * @param eLMnumber Index value of the ELM that has inflows
     * @param includeNone Include null transfers
     * @return a list of Inflows associated with the ELM, the first value is set
     * to -1 to avoid null
     */
    public int[] getInflowNameListIndex(int eLMnumber, boolean includeNone) { // used for picking objects 

        int subcount = 0;
        for (int i = 0; i < count; i++) { // first itteration is to size array
            if (tRNs[i].outObjNumber == eLMnumber) {
                subcount++;
            }
        }
        int[] nameListIndex;
        if (includeNone) {
            nameListIndex = new int[subcount + 1];
            nameListIndex[0] = -1; // null value required for edit menu
            int j = 1;
            for (int i = 0; i < count; i++) {
                if (tRNs[i].outObjNumber == eLMnumber) {
                    nameListIndex[j] = i;
                    j++; // secondary counter for use in the smaller list
                }
            }
        } else {
            nameListIndex = new int[subcount];
            int j = 0;
            for (int i = 0; i < count; i++) {
                if (tRNs[i].outObjNumber == eLMnumber) {
                    nameListIndex[j] = i;
                    j++; // secondary counter for use in the smaller list
                }
            }
        }
        return nameListIndex;
    }

    /**
     * used for picking objects
     *
     * @param eLMnumber
     * @param includeNone
     * @return
     */
    public String[] getOutflowNameList(int eLMnumber, boolean includeNone) { // 

        int subcount = 0;
        for (int i = 0; i < count; i++) { // first itteration is to size array
            if (tRNs[i].inObjNumber == eLMnumber) {
                subcount++;
            }
        }
        String[] nameList;
        if (includeNone) {
            nameList = new String[subcount + 1];
            nameList[0] = "None"; // null value required for edit menu
            int j = 1;
            for (int i = 0; i < count; i++) {
                if (tRNs[i].inObjNumber == eLMnumber) {
                    nameList[j] = tRNs[i].objname;
                    j++; // secondary counter for use in the smaller list
                }
            }
        } else {
            nameList = new String[subcount];
            int j = 0;
            for (int i = 0; i < count; i++) {
                if (tRNs[i].inObjNumber == eLMnumber) {
                    nameList[j] = tRNs[i].objname;
                    j++; // secondary counter for use in the smaller list
                }
            }
        }
        return nameList;
    }

    /**
     * used for picking objects
     *
     * @param eLMnumber
     * @param includeNone
     * @return
     */
    public int[] getOutflowNameListIndex(int eLMnumber, boolean includeNone) { // 

        int subcount = 0;
        for (int i = 0; i < count; i++) { // first itteration is to size array
            if (tRNs[i].inObjNumber == eLMnumber) {
                subcount++;
            }
        }
        int[] nameListIndex;
        if (includeNone) {
            nameListIndex = new int[subcount + 1];
            nameListIndex[0] = -1; // null value required for edit menu
            int j = 1;
            for (int i = 0; i < count; i++) {
                if (tRNs[i].inObjNumber == eLMnumber) {
                    nameListIndex[j] = i;
                    j++; // secondary counter for use in the smaller list
                }
            }
        } else {
            nameListIndex = new int[subcount];
            int j = 0;
            for (int i = 0; i < count; i++) {
                if (tRNs[i].inObjNumber == eLMnumber) {
                    nameListIndex[j] = i;
                    j++; // secondary counter for use in the smaller list
                }
            }
        }
        return nameListIndex;
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
        //Element mainroot = XMLDoc.getDocumentElement();

        Element root = xMLDoc.createElement("Transfers");
        xMLDoc.appendChild(root);
        for (int i = 0; i < count; i++) {
            root.appendChild(tRNs[i].getXMLElement(xMLDoc, i));
        }
        return xMLDoc;
    }

    /**
     * used for resetting the transfer list
     *
     * @param projSetting
     */
    public void initResults(ProjSetting projSetting) {
        for (int i = 0; i < count; i++) {
            tRNs[i].initResults(projSetting);
        }
    }

    /**
     * links the inflow of a transfer to an element
     *
     * @param inELM
     * @param inTRN
     */
    public void setInflow(int inELM, int inTRN) {
        tRNs[inTRN].inObjNumber = inELM;
    }

    /**
     * sets the number of a transfer tRNs[inNumber] = inObjTRN;
     *
     * @param inNumber
     * @param inObjTRN
     */
    public void setObjTRN(int inNumber, TRN inObjTRN) {
        tRNs[inNumber] = inObjTRN;
        System.out.println("ObjTRNList.setObjTRN has been called on TRN " + inNumber); // DEBUG

    }

    /**
     * tRNs[inTRN].outObjNumber = outELM;
     *
     * @param outELM
     * @param inTRN
     */
    public void setOutflow(int outELM, int inTRN) {
        tRNs[inTRN].outObjNumber = outELM;
    }

    /**
     * removes all links from a each transfer to and from a specified ELM
     *
     * @param rELM
     */
    public void removeELM(int rELM) {
        for (int i = 0; i < count; i++) {
            tRNs[i].removeELM(rELM);
        }
    }

    /**
     * Removes the requested transfer from the TRN list and shifts all following
     * elements up 1 index. Method does not modify Linking in the ELM list. i.e.
     * if TRN 4 is deleted the any Transfers to or from an element are not
     * automatically updated, the method removeTRN must be called from both the
     * ELM list and TRN list
     *
     * @param inNumber Index value of the element to be removed from the list
     */
    public void removeTRN(int inNumber) {// removes object from list and shifts proceeding objects up
        for (int i = inNumber; i < count; i++) {
            tRNs[i] = tRNs[i + 1];
        }
        count--;
    }

}
