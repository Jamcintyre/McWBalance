/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.generics;

/**
 * Used to store and modify a list of Object Indicies in fixed length arrays with counters
 * @author amcintyre
 */

public class IndexList {
    int[] indexList;
    String[] nameList;
    public int count;
    
    /**
     * constructor for IndexList
     * @param maxLength sets the maximum length of the list, note that this values is not intended to change so should be set to a fixed maximum defined in the ProjSettings
     */
    public IndexList(int maxLength){
        if (maxLength < 1){
            maxLength = 1;
        }
        indexList = new int[maxLength];
        int count = 0;
        nameList = new String[maxLength];
    }
    
    /**
     * Appends a single index value to the end of the list assuming there is space. Checks to ensure no duplication.
     * @param addIndex int index value corresponding to current list of Objects / transfers 
     * @param addName String corresponding to the index value
     */
    public void appendToList(int addIndex, String addName){
        //This is where we add to the list;
        if (addIndex < 0){
            System.out.println("Value of " + addIndex + " is less then 0 and will not be added");
        }
        
        for (int i = 0; i < count; i++){
            if (indexList[i] == addIndex){
                System.err.println("Value " + addIndex + "already exists on list at Index " + i);
                return;
            }
        }
        if (count < indexList.length){
            indexList[count] = addIndex;
            nameList[count] = addName;
            count++;
        }
        else {
            System.out.println("Maximum List size of " + indexList.length + " TRNs Exceeded");
        }
    }
    /**
     * debugging tool used to print contents of list
     */
    
    public void debugPrintIndexList(){
        System.out.println("Current length of list is " + count + " and contains the following");
        for (int i = 0; i < count; i ++){
            System.out.println("Index " + indexList[i] + " Corresponding to object name " + nameList[i]);
        }
    }
            
    
    
    
    /**
     * Provides a shortened version of the IndexList with the null values removed;
     * @return 
     */
    public int[] getShortIndexList(){ 
        int [] shortIndexList;
        if (count < 1){
            shortIndexList = new int[1];
            shortIndexList[0] = -1;
            return shortIndexList; 
        }
        else if(count > indexList.length){ // count shouldn't ever be longer then the list. however if it does happen this will ignore it;
            count = indexList.length;
            System.err.println("List Count Value" + count + " exceeded actual list size of " + indexList.length +" excess count ingnored");
        } 
        shortIndexList = new int[count];
            for (int i = 0; i < count; i++){
                shortIndexList[i] = indexList[i];
            }
       return shortIndexList; 
    }
    /**
     * pulls directly from indexList as a strait getter, unless value is -1 in which case it returns -1 
     * @param localIndex
     * @return The object index value corresponding to the local list index, -1 returned for null value. 
     */
    
    public int getObjIndex(int localIndex){
        if(localIndex == -1){
            return -1;
        }
        else{
            return indexList[localIndex];
        }
    }
    /**
     * Searches the list of index values and returns the corresponding list index. Note that "None" is not contained in the list index and as
     * such the returned list index value may be offset by 1 if chosing from a list that contains "None"
     * @param objIndex
     * @return An index int value pointing to the first ocurrance of the requested object index. if object not found then -1 is returned
     */
    public int getListIndex(int objIndex){
        if (objIndex == -1){
            return -1;
        }
        for (int i = 0; i < indexList.length; i ++){
            if (indexList[i] == objIndex){
                return i;
            }
        }
        return -1; 
    }
    
    
       /**
     * Provides a shortened version of the IndexList with the null values removed;
     * @return 
     */
    public String[] getShortNameList(){ 
        String [] shortNameList;
        if (count < 1){
            shortNameList = new String[1];
            shortNameList[0] = "None";
            return shortNameList; 
        }
        else if(count > indexList.length){ // count shouldn't ever be longer then the list. however if it does happen this will ignore it;
            count = indexList.length;
            System.err.println("List Count Value" + count + " exceeded actual list size of " + indexList.length +" excess count ingnored");
        } 
        shortNameList = new String[count];
            for (int i = 0; i < count; i++){
                shortNameList[i] = nameList[i];
            }
       return shortNameList; 
    }
    public String[] getShortNameListWithNone(){ 
        String [] shortNameList;
        if (count < 1){
            shortNameList = new String[1];
            shortNameList[0] = "None";
            return shortNameList; 
        }
        else if(count > indexList.length){ // count shouldn't ever be longer then the list. however if it does happen this will ignore it;
            count = indexList.length;
            System.err.println("List Count Value" + count + " exceeded actual list size of " + indexList.length +" excess count ingnored");
        } 
        shortNameList = new String[count +1];
        shortNameList[0] = "None";
            for (int i = 1; i < count+1; i++){
                shortNameList[i] = nameList[i-1];
            }
       return shortNameList; 
    }
    

    /**
     * Populates the entire list, typically used for initialization
     * @param inList Array if index values for each object
     * @param inNameList Corresponding list of Names for the index;
     */
    public void overwriteList(int[] inList, String[] inNameList){
        if(inList.length != inNameList.length){
            System.err.println("inList length " + inList.length +" does not match inNameList Length " + inNameList.length + " overwite List aborted");
            return;
        }
        if(inList.length > indexList.length){
            count = indexList.length;
        } else{
            count = inList.length;
        }
        for (int i = 0; i < count; i ++){
            indexList[i] = inList[i];
            nameList[i] = inNameList[i];
        }
    }
    public void setNames(String[] inNameList){
        if(count != inNameList.length){
            
            if(inNameList[0] == "None"){
                return;
            }
            System.err.println("List length " + count +" does not match inNameList Length " + inNameList.length + " updating of names List aborted");
            return;
        }
        for( int i = 0; i < count; i ++){
            nameList[i] = inNameList[i];
        }
    }
    public void shiftUp(int index){
        if (index < 1){
            return;
        }
        int bindex = indexList[index - 1];
        String bname = nameList[index - 1];
        indexList[index - 1] = indexList[index];
        nameList[index - 1] = nameList[index];
        indexList[index] = bindex;
        nameList[index] = bname;
    }
    public void shiftDown(int index){
        if (index >= count -1){
            return;
        }
        int bindex = indexList[index + 1];
        String bname = nameList[index + 1];
        indexList[index + 1] = indexList[index];
        nameList[index + 1] = nameList[index];
        indexList[index] = bindex;
        nameList[index] = bname;
    }
    

     /**
     * function used to remove a single value from a selected index list
     * @param remvalue 
     */
    public void trimFromList(int remvalue){
        if (count < 1){
            count = 0; // ensures no negitive value
            indexList[0] = -1;
            return;
        }
        for (int i = 0; i < count; i++){
            if (indexList[i] == remvalue){
                if (i == count -1){
                        indexList[i] = -1;
                        count --;
                }else{
                    for (int j = i; j < count; j++){
                        indexList[j] = indexList[j+1];
                        nameList[j] = nameList[j+1];
                    }
                    count --;
                }
            }
        }
        if (count < 1){
            indexList[0] = -1;
        }
    }
    public void trimFromList(int[] remvalues){
        if (count < 1){
            count = 0; // ensures no negitive value
            indexList[0] = -1; // sets inital value to the null value
            return;
        }
        for (int c = 0; c < remvalues.length; c++){
            for (int i = 0; i < count; i++){
                if (indexList[i] == remvalues[c]){
                    if (i == count -1){
                        indexList[i] = -1;
                        count --;
                    }else{
                        for (int j = i; j < count -1; j++){
                            indexList[j] = indexList[j+1]; 
                            nameList[j] = nameList[j+1];
                        }
                        count --;
                    }
                }
            }
        }
        if (count < 1){
            indexList[0] = -1;
        }
    }
    

}
