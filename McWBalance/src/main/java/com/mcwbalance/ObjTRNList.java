
package com.mcwbalance;

import java.awt.Rectangle;


public class ObjTRNList {
    int count; // counter to keep track fo list size
    ObjTRN[] tRNs= new ObjTRN[ProjSetting.MAX_TRNS]; 
    
   public ObjTRNList(){ // Constructor sets initial values
        count = 0; // sets number of transfers to 0 to start
        for (int i = 0; i < ProjSetting.MAX_TRNS; i++){
            tRNs[i] = new ObjTRN(); // Constructs the element so there is a place in memory for it, only needed for Object arrays
            tRNs[i].x = 0;
            tRNs[i].y = 0;
            
            tRNs[i].objname = "Transfer " + i;
            tRNs[i].inObjNumber = -1; // -1 is used as Null, first object number is 0;
            tRNs[i].outObjNumber = -1; // -1 is used as Null, first object number is 0; 
        }
    } 
    public void addTRN(int inX, int inY){
        if (count - 1 == ProjSetting.MAX_TRNS){ // limits object addtion to max number of objects -1 as last object needs to be null for delete to work
            System.err.println("Max Number of Tranfers Reached");
        }
        tRNs[count] = new ObjTRN(inX, inY, count +1); 
        count ++; // increments to next element, only allowed if not at maximum
    } 
    /**
     * Provides an array of strings containing the name of each transfer object.  If not list is given then all names are returned
     * @return An array of string containing each transfer object led with a "none" null value at beginning of list;
     */
    public String[] getNameList(boolean includeNone){ // used for picking objects 
       String[] nameList;
        
        if(includeNone){
            nameList = new String [count+1];
            nameList[0] = "None"; // null value required for edit menu
            for (int i = 0; i < count; i++){
                nameList[i+1] = tRNs[i].objname;
            }
        }else{
            nameList = new String [count];
            for (int i = 0; i < count; i++){
                nameList[i] = tRNs[i].objname;
            }
        }
        return nameList;
    }
    public String[] getNameList(int indexList[]){
        String[] nameList = new String [indexList.length];
        for (int i = 0; i < indexList.length; i++){
            if (indexList[i] == -1){
                nameList[i] = "None";
            }else{
                nameList[i] = tRNs[indexList[i]].objname;
            }
        }
        return nameList;
    }
    /**
     * Used for determining which Transfers inflow to an element
     * @param eLMnumber Index value of the ELM that has inflows
     * @return a list of Inflows associated with the ELM, the first value is set to None to avoid null
     */
    public String[] getInflowNameList(int eLMnumber, boolean includeNone){ // used for picking objects 
        int subcount = 0; 
        for (int i = 0; i < count; i++){ // first itteration is to size array
            if (tRNs[i].outObjNumber == eLMnumber){
                subcount ++;
            }
        }
        String[] nameList;
        
        if(includeNone){
            nameList = new String [subcount+1];
            nameList[0] = "None"; // null value required for some comboBoxes
            int j = 1; // starts at 1 since 0 is set to None. 
            for (int i = 0; i < count; i++){
                if (tRNs[i].outObjNumber == eLMnumber){
                    nameList[j] = tRNs[i].objname;
                    j++; // secondary counter for use in the smaller list
                }
            }
        }else{
            nameList = new String [subcount];
            int j = 0; // starts at 0 since no None value is requested
            for (int i = 0; i < count; i++){
                if (tRNs[i].outObjNumber == eLMnumber){
                    nameList[j] = tRNs[i].objname;
                    j++; // secondary counter for use in the smaller list
                }
            }
        }
        return nameList;
    }

    /**
     * Used for determining which Transfers inflow to an element
     * @param eLMnumber Index value of the ELM that has inflows
     * @return a list of Inflows associated with the ELM, the first value is set to -1 to avoid null
     */
    public int[] getInflowNameListIndex(int eLMnumber, boolean includeNone){ // used for picking objects 
        
        int subcount = 0; 
        for (int i = 0; i < count; i++){ // first itteration is to size array
            if (tRNs[i].outObjNumber == eLMnumber){
                subcount ++;
            }
        }
        int[] nameListIndex;
        if(includeNone){
            nameListIndex = new int[subcount+1];
            nameListIndex[0] = -1; // null value required for edit menu
            int j = 1; 
            for (int i = 0; i < count; i++){
                if (tRNs[i].outObjNumber == eLMnumber){
                    nameListIndex[j] = i;
                    j++; // secondary counter for use in the smaller list
                }
            }
        }else{
            nameListIndex = new int[subcount];
            int j = 0; 
            for (int i = 0; i < count; i++){
                if (tRNs[i].outObjNumber == eLMnumber){
                    nameListIndex[j] = i;
                    j++; // secondary counter for use in the smaller list
                }
            }
        }
        return nameListIndex;
    }
    
    public String[] getOutflowNameList(int eLMnumber, boolean includeNone){ // used for picking objects 
        
        int subcount = 0; 
        for (int i = 0; i < count; i++){ // first itteration is to size array
            if (tRNs[i].inObjNumber == eLMnumber){
                subcount ++;
            }
        }
        String[] nameList;
        if (includeNone){
            nameList = new String [subcount+1];
            nameList[0] = "None"; // null value required for edit menu
            int j = 1; 
            for (int i = 0; i < count; i++){
                if (tRNs[i].inObjNumber == eLMnumber){
                    nameList[j] = tRNs[i].objname;
                    j++; // secondary counter for use in the smaller list
                }
            }
        }else {
            nameList = new String [subcount];
            int j = 0; 
            for (int i = 0; i < count; i++){
                if (tRNs[i].inObjNumber == eLMnumber){
                    nameList[j] = tRNs[i].objname;
                    j++; // secondary counter for use in the smaller list
                }
            }         
        }
        
        
        
        return nameList;
    }
    
    public int[] getOutflowNameListIndex(int eLMnumber, boolean includeNone){ // used for picking objects 
        
        int subcount = 0; 
        for (int i = 0; i < count; i++){ // first itteration is to size array
            if (tRNs[i].inObjNumber == eLMnumber){
                subcount ++;
            }
        }
        int[] nameListIndex;
        if (includeNone){
            nameListIndex = new int[subcount+1];
            nameListIndex[0] = -1; // null value required for edit menu
            int j = 1; 
            for (int i = 0; i < count; i++){
                if (tRNs[i].inObjNumber == eLMnumber){
                    nameListIndex[j] = i;
                    j++; // secondary counter for use in the smaller list
                }
            }
        }else{
            nameListIndex = new int[subcount];
            int j = 0; 
            for (int i = 0; i < count; i++){
                if (tRNs[i].inObjNumber == eLMnumber){
                    nameListIndex[j] = i;
                    j++; // secondary counter for use in the smaller list
                }
            }
        }
        return nameListIndex;
    }
    
   public void setInflow(int inELM, int inTRN){
        tRNs[inTRN].inObjNumber = inELM;
    }
   
   public void setObjTRN(int inNumber, ObjTRN inObjTRN){
       tRNs[inNumber] = inObjTRN; 
       System.out.println("ObjTRNList.setObjTRN has been called on TRN " + inNumber); // DEBUG
       
   }

   public void setOutflow(int outELM, int inTRN){
        tRNs[inTRN].outObjNumber = outELM;
    }
   /**
    * removes all links from a each transfer to and from a specified ELM
    * @param rELM 
    */
   public void removeELM (int rELM){
       for (int i = 0; i < count; i ++){
           tRNs[i].removeELM(rELM);
       }
   }
   
   /**
     * Removes the requested transfer from the TRN list and shifts all following elements up 1 index. Method does not modify
     * Linking in the ELM list.  i.e. if TRN 4 is deleted the any Transfers to or from an element are not automatically updated, the method removeTRN must be called from
     * both the ELM list and TRN list
     * @param inNumber Index value of the element to be removed from the list
     */
    public void removeTRN(int inNumber){// removes object from list and shifts proceeding objects up
        for (int i = inNumber; i < count; i ++){
            tRNs[i] = tRNs[i+1];
        }
        count --;
    }
   
           
           
           
}
