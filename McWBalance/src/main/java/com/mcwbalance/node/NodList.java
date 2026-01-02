
package com.mcwbalance.node;

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
 * Contains a complete array of all Nodes present in the model as well as placeholder values. 
 * @see Nod
 * @author amcintyre
 */
public class NodList {

    public int count; // counter to keep track fo list size
    public Nod[] nodes= new Nod[Limit.MAX_NODES]; 
    
    ProjSetting projSetting;
    /**
     * Constructor initializes a blank array of Nodes of generally 0 values to avoid issues with null values
     */
    public NodList(ProjSetting projSetting){
        this.projSetting = projSetting;
        count = 0; // sets number of elements to 0 to start
        for (int i = 0; i < Limit.MAX_NODES; i++){
            nodes[i] = new Nod(projSetting); // Constructs the node so there is a place in memory for it, only needed for Object arrays
            nodes[i].x = 0;
            nodes[i].y = 0;
            nodes[i].objname = "Node " + i;
            nodes[i].objSubType = "NODE"; // defaults to ugly square that needs to be defined
        }
    }
    
    public NodList(ProjSetting projSetting, Element nodeXML){
       this(projSetting);     
       NodeList cnl = nodeXML.getElementsByTagName("Node");
       for (int i = 0; i < cnl.getLength(); i++){
           if (cnl.item(i).getNodeType() == Node.ELEMENT_NODE){
               nodes[count] = new Nod(projSetting, (Element) cnl.item(i));
               count ++;
           }
       }
    }
    
     /**
      * Adds a new Node to the end of the Node List
      * @param inX Midpoint of the Node draw location used in FlowChartCAD class
      * @param inY Midpoint of the Node draw location used in FlowChartCAD class
      * @return 0 is returned of successful, -1 if unsuccessful
      */
    public int addNode(int inX, int inY){
        if (count - 1 == Limit.MAX_NODES){ // limits object addtion to max number of objects -1 as last object needs to be null for delete to work
            System.out.println("Max Number of Objects");
        return -1; // returns -1 as error code, object could not be added
        }
        nodes[count] = new Nod(inX, inY, count + 1,projSetting);
        count ++; // increments to next element, only allowed if not at maximum
        return 0;
    }
    /**
     * Used for picking objects in Transfer 
     * @return A list of all Nodes currently active in the Node list. "None" is included as a first entry for cases where the user would 
     * prefer to select no Node.
     */
    public String[] getNameList(){  
        String[] nameList = new String [count+1];
        nameList[0] = "None"; // null value required for edit menu
        for (int i = 0; i < count; i++){
            nameList[i+1] = nodes[i].objname;
        }
        return nameList;
    }
    /**
     * Removes the requested Node from the Node list and shifts all following elements up 1 index. Method does not modify
     * Linking in the TRN list.  i.e. if Node 4 is deleted the any Transfers to Node 4, 5, 6 etc.. are not automatically updated
     * @param inNumber Index value of the Node to be removed from the list
     */
    public void removeNode(int inNumber){// removes object from list and shifts proceeding objects up
        for (int i = inNumber; i < count; i ++){
            nodes[i] = nodes[i+1];
        }
        count --;
    }
    /**
    * removes all links from a each Node to and from a specified TRN
    * @param rTRN 
    */
   public void removeTRN (int rTRN){
       for (int i = 0; i < count; i ++){
           nodes[i].remove(rTRN);
       }
   }
  
    /**
     * Used to set the values of an Node entry, typically with data passed back from ObjNodeWindow. Method does not modify the number
     * of Nodes active on the list. If 5 elements are present and element 6 is modified, the data will be modified however the functions
     * calling on NodeList will generally ignore Nodes 6, 7, 8 ... unless the count is updated
     * @param inNumber Index number of element to be 
     * @param inNode Populated Node data to be used to overwrite the selected index
     */
    public void set(int inNumber, Nod inNode){
        nodes[inNumber] = inNode;
    }
    
    /**
     * @deprecated Superceeded by ObjELM individual save strings
     * This Method defines and populates the format of the Portion of the Save file containing the ELM list.
     * @return A full String containing all key information from the ELMList formatted 
     * as TAB delimited and ready to be output directly to a .txt file or copied into the clipboard. 
     * @see getCopyString
     */
    public StringBuilder getSaveString(){
        int eLMIndicies[] = new int[count];
        for (int i = 0; i < count; i++){
            eLMIndicies[i] = i;
        }
        return getCopyString(eLMIndicies);
       }
    
    /**
     * @deprecated Superceeded by ObjELM individual save strings
     * This Method defines and populates the format save or copy data pulled from the ELM list. Method allow individual
     * selection of ELMs to be copied to allow future implementation of ctrl+C and ctrl+V between this program and Excel or even
     * 2 instances of this program. 
     * @param eLMIndices list of ELM indices to copy into a string. to copy complete list use getSaveString
     * @return A String containing all key information from the ELMs selected from the ELMList formatted 
     * as TAB delimited and ready to be output directly to a .txt file or copied into the clipboard. 
     */
    public StringBuilder getCopyString(int[] eLMIndices){
        StringBuilder copyString = new StringBuilder();
        copyString.append("List of Nodes");
        copyString.append(System.getProperty("line.separator")); // used instead of /n for cross platform compatibility
        copyString.append("No of Nodes" + "\t");
        copyString.append(count);
        copyString.append(System.getProperty("line.separator"));
        
        for(int i = 0; i < count; i++){
            copyString.append(nodes[i].objname);
            copyString.append(System.getProperty("line.separator"));
            copyString.append(nodes[i].objSubType);
            copyString.append(System.getProperty("line.separator"));
            copyString.append((String)("coord xy," + nodes[i].x + "\t" + nodes[i].y));
            copyString.append(System.getProperty("line.separator"));    
            
        }
        return copyString;
    }
    
        /**
     * Not finished, will have to add info to the doc
     * @return
     * @throws ParserConfigurationException 
     */
    public Document getXMLDoc() throws ParserConfigurationException{
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document XMLDoc = builder.newDocument();
        //Element mainroot = XMLDoc.getDocumentElement();
        
        Element root = XMLDoc.createElement("Nodes");
        XMLDoc.appendChild(root);
        for (int i = 0; i < count; i++){
            // need to add leading zeros,  allow 999 elements
            String tagName = "Node" + String.valueOf(i+1);
            Element node = XMLDoc.createElement(tagName);
            node.setAttribute("ObjName", nodes[i].objname);
            node.setAttribute("SubType", nodes[i].objSubType);
            node.setAttribute("x", String.valueOf(nodes[i].x));
            node.setAttribute("y", String.valueOf(nodes[i].y));
            
            
            /**
            Element inflow = XMLDoc.createElement("INFLOW");
            inflow.setAttribute("inObjNumber", String.valueOf(nodes[i].inObjNumber));
            inflow.setAttribute("inSideFrom", tRNs[i].inSideFrom);
            inflow.setAttribute("inSideFromOset", String.valueOf(tRNs[i].inSideFromOset));
            inflow.setAttribute("inSideTo", tRNs[i].inSideTo);
            tran.appendChild(inflow);
            **/
            
            root.appendChild(node);
        }    
        return XMLDoc;
    }
      
    /**
     * calls on each Node to construct its result arrays.
     */
    public void initResults(){
        for (int i = 0; i < count; i++){
            nodes[i].initResults();
        }
    }
    
    
    /**
     * Method not yet complete. This method will take a string representation of the Node list. 
     * This method will not preserve Node Index numbers, and will take the index based on order provided
     * @param loadFile String representing all element data to be written to the NodeList
     * @param overWrite if true data NodeList will be overwritten with loaded data. if false Nodes will be appended without links
     * transfers
     * @see getSaveString
     */
    public void setFromString (String loadFile, Boolean overWrite){
        
    }
    
    
    
    
}
