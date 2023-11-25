
package com.mcwbalance.element;

import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.settings.Limit;

/**
 * Contains a complete array of all ELMs present in the model as well as placeholder values. 
 * @see ELM
 * @author amcintyre
 */
public class ELMList {

    public int count; // counter to keep track fo list size
    public ELM[] eLMs= new ELM[Limit.MAX_ELMS]; 
    /**
     * Constructor initializes a blank array of ELMs of generally 0 values to avoid issues with null values
     */
     public ELMList(){
        count = 0; // sets number of elements to 0 to start
        for (int i = 0; i < Limit.MAX_ELMS; i++){
            eLMs[i] = new ELM(); // Constructs the element so there is a place in memory for it, only needed for Object arrays
            eLMs[i].x = 0;
            eLMs[i].y = 0;
            eLMs[i].objname = "Element " + i;
            eLMs[i].objSubType = "ELM"; // defaults to ugly square that needs to be defined
        }

    }
     /**
      * Adds a new ELM to the end of the ELM List
      * @param inX Midpoint of the ELM draw location used in FlowChartCAD class
      * @param inY Midpoint of the ELM draw location used in FlowChartCAD class
      * @return 0 is returned of successful, -1 if unsuccessful
      */
    public int addELM(int inX, int inY){
        if (count - 1 == Limit.MAX_ELMS){ // limits object addtion to max number of objects -1 as last object needs to be null for delete to work
            System.out.println("Max Number of Objects");
        return -1; // returns -1 as error code, object could not be added
        }
        eLMs[count] = new ELM(inX, inY, count + 1);
        count ++; // increments to next element, only allowed if not at maximum
        return 0;
    }
    /**
     * Used for picking objects in Transfer 
     * @return A list of all ELMs currently active in the ELM list. "None" is included as a first entry for cases where the user would 
     * prefer to select no ELM.
     */
    public String[] getNameList(){  
        String[] nameList = new String [count+1];
        nameList[0] = "None"; // null value required for edit menu
        for (int i = 0; i < count; i++){
            nameList[i+1] = eLMs[i].objname;
        }
        return nameList;
    }
    /**
     * Removes the requested element from the ELM list and shifts all following elements up 1 index. Method does not modify
     * Linking in the TRN list.  i.e. if ELM 4 is deleted the any Transfers to ELM 4, 5, 6 etc.. are not automatically updated
     * @param inNumber Index value of the element to be removed from the list
     */
    public void removeELM(int inNumber){// removes object from list and shifts proceeding objects up
        for (int i = inNumber; i < count; i ++){
            eLMs[i] = eLMs[i+1];
        }
        count --;
    }
    /**
    * removes all links from a each Element to and from a specified TRN
    * @param rTRN 
    */
   public void removeTRN (int rTRN){
       for (int i = 0; i < count; i ++){
           eLMs[i].remove(rTRN);
       }
   }
  
    /**
     * Used to set the values of an ELM entry, typically with data passed back from ObjELMWindow. Method does not modify the number
     * of ELMs active on the list. If 5 elements are present and element 6 is modified, the data will be modified however the functions
     * calling on ELMList will generally ignore ELMs 6, 7, 8 ... unless the count is updated
     * @param inNumber Index number of element to be 
     * @param inObjELM Populated ELM data to be used to overwrite the selected index
     */
    public void setObjELM(int inNumber, ELM inObjELM){
        eLMs[inNumber] = inObjELM;
    }
    
    /**
     * @deprecated Superceeded by ObjELM individual save strings
     * This Method defines and populates the format of the Portion of the Save file containing the ELM list.
     * @return A full String containing all key information from the ELMList formatted 
     * as TAB delimted and ready to be output directly to a .txt file or copied into the clipboard. 
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
     * selection of ELMs to be copied to allow future implentation of ctrl+C and ctrl+V between this program and Excel or even
     * 2 instances of this program. 
     * @param eLMIndices list of ELM indices to copy into a string. to copy complete list use getSaveString
     * @return A String containing all key information from the ELMs selected from the ELMList formatted 
     * as TAB delimted and ready to be output directly to a .txt file or copied into the clipboard. 
     */
    public StringBuilder getCopyString(int[] eLMIndices){
        StringBuilder copyString = new StringBuilder();
        copyString.append("List of Elements");
        copyString.append(System.getProperty("line.separator")); // used instead of /n for cross platform compatibility
        copyString.append("No of Elements" + "\t");
        copyString.append(count);
        copyString.append(System.getProperty("line.separator"));
        
        for(int i = 0; i < count; i++){
            copyString.append(eLMs[i].objname);
            copyString.append(System.getProperty("line.separator"));
            copyString.append(eLMs[i].objSubType);
            copyString.append(System.getProperty("line.separator"));
            copyString.append((String)("coord xy," + eLMs[i].x + "\t" + eLMs[i].y));
            copyString.append(System.getProperty("line.separator"));    
            
        }
        return copyString;
    }
    
    /**
     * calls on each ELM to construct its result arrays.
     */
    public void initializeResults(){
        for (int i = 0; i < count; i++){
            eLMs[i].initializeResults();
        }
    }
    
    
    /**
     * Method not yet complete. This method will take a string representation of the ELM list. 
     * This method will not preserve ELM Index numbers, and will take the index based on order provided
     * @param loadFile String representing all element data to be written to the ELMlist
     * @param overWrite if true data ELMList will be overwitten with loaded data. if false ELMs will be appended without links
     * transfers
     * @see getSaveString
     */
    public void setFromString (String loadFile, Boolean overWrite){
        
    }
    
    
    
    
}
