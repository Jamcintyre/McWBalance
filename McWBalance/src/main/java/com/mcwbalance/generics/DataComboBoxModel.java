/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.generics;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author amcintyre
 */
public class DataComboBoxModel extends AbstractListModel implements ComboBoxModel{
    private Object selectedItem;  
    String strings[];
    
    /**
     * Custom method used to replace entire contents of comboBox list. this allows the actual list class to manage the changes
     * @param newList 
     */
    public void setAllData(String[] newList){
        strings = newList; 
        this.fireContentsChanged(this, 0, strings.length-1);
    }
    @Override
    public int getSize(){
        return strings.length;
    }
    @Override
    public String getElementAt(int index){
        return strings[index];
    }
    // the following additional overides are required for the comboBox part
    @Override
    public Object getSelectedItem(){
        return selectedItem;
    }
    @Override
    public void setSelectedItem(Object newValue){
        selectedItem = newValue; 
    }
    
}
