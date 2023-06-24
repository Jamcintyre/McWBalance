package com.mcwbalance;


import javax.swing.AbstractListModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author amcintyre
 */
public class DataNameListModel extends AbstractListModel{
    String strings[];
    
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
    
}
