/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.dacapacity;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 * @deprecated 
 * @author amcintyre
 */
public class DataDACWindow extends JDialog{

    public DataDACWindow(JFrame owner){
        super(owner, "Depth Area Capacity", true);
        
        
        TableColumn colElev = new TableColumn();
        colElev.setHeaderValue("Elevation (m)");
        TableColumn colArea = new TableColumn();
        colArea.setHeaderValue("Area (sq.m)");
        TableColumn colCapacity = new TableColumn();
        colCapacity.setHeaderValue("Cumulative Volume (cu.m)");
        
        JTable entryTable = new JTable();
        entryTable.addColumn(colElev);
        entryTable.addColumn(colArea);
        entryTable.addColumn(colCapacity);
        
        this.add(entryTable);
        this.setVisible(true);

    }
    
    
}
