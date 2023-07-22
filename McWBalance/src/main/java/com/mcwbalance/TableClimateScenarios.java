/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Alex
 */
public class TableClimateScenarios extends AbstractTableModel{
    private static final String[] columnNames = {"Scenario", "Description","Avg Ann Precip","Min Ann Precip","Max Ann Precip", "Yr 1 Precip"};
    public static final int NUMBER_OF_COLUMNS = 6;
    
    String[] description;
    double[] aaprecip;
    double[] minprecip;
    double[] maxprecip;
    double[] yr1precip;
    DataClimate[] climateScenarios; 
    
    TableClimateScenarios(int size){
        aaprecip = new double[size];
        minprecip = new double[size];
        maxprecip = new double[size];
        yr1precip = new double[size];
        climateScenarios = new DataClimate[size];
        for (int i = 0; i < size; i++) {
            climateScenarios[i] = new DataClimate(1);
            
            climateScenarios[i].description = "new";
            aaprecip[i] = 0;
            minprecip[i] = 0;
            maxprecip[i] = 0;
            yr1precip[i] = 0;
        }
        
        
    }
    TableClimateScenarios(DataClimate[] climates){
        climateScenarios = climates; 
        int size = climates.length;
        aaprecip = new double[size];
        minprecip = new double[size];
        maxprecip = new double[size];
        yr1precip = new double[size];

    }
        
        
        
    
    @Override 
    public Object getValueAt(int row, int col){
        switch(col){
            case 0 -> {
                return row;
            }
            case 1 -> {
                return climateScenarios[row].description;
            }
            case 2 -> {
                return aaprecip[row];
            }
            case 3 -> {
                return minprecip[row];
            }
            case 4 -> {
                return maxprecip[row];
            }
            case 5 -> {
                return yr1precip[row];
            }
        }
        return null;
    }
    @Override 
    public int getColumnCount(){
        return columnNames.length;
    }
    @Override 
    public int getRowCount(){
        return climateScenarios.length;
    }
    @Override 
    public String getColumnName(int col){
        return columnNames[col];
    }
    @Override 
    public Class getColumnClass(int c){        
        return getValueAt(0,c).getClass(); 
    }
    
    @Override 
    public boolean isCellEditable(int rowIndex, int columnIndex){
        return false;
        
    }
    
    public void removeRow(int rowIndex){
        
    }

    
}
