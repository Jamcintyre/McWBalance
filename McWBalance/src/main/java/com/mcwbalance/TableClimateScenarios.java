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
    private static final String[] columnNames = {"Scenario", "Description","Length","Avg Ann Precip","Min Ann Precip","Max Ann Precip", "Yr 1 Precip"};
    public static final int NUMBER_OF_COLUMNS = 7;
    
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
            
            climateScenarios[i].description = DataClimate.NULL_DESCRIP;
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
                return climateScenarios[row].precip.length;
            }
            case 3 -> {
                return aaprecip[row];
            }
            case 4 -> {
                return minprecip[row];
            }
            case 5 -> {
                return maxprecip[row];
            }
            case 6 -> {
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
    
    
    /**
     * Method to remove a climate set, if all data is removed a Null value will be placed into row 0
     * @param rowIndex 
     */
    public void removeRow(int rowIndex){
        if(rowIndex < 0 || rowIndex >= climateScenarios.length){
            //Do Nothing
        }
        else if(climateScenarios.length == 1){
            if(climateScenarios[0].description.equals(DataClimate.NULL_DESCRIP) ){
                //Do Nothing
            }
            else{
                climateScenarios[0] = new DataClimate(1);
                aaprecip[0] = 0;
                minprecip[0] = 0;
                maxprecip[0] = 0;
                yr1precip[0] = 0;
                fireTableRowsUpdated(0, 0);
            }
        }
        else{
            int size = climateScenarios.length -1;
            double[] aaprecipBuff = new double[size];
            double[] minprecipBuff = new double[size];
            double[] maxprecipBuff = new double[size];
            double[] yr1precipBuff = new double[size];
            DataClimate[] climateScenariosBuff = new DataClimate[size];
            
            for (int i = 0; i < rowIndex; i++){
                aaprecipBuff[i] = aaprecip[i];
                minprecipBuff[i] = minprecip[i];
                maxprecipBuff[i] = maxprecip[i];
                yr1precipBuff[i] = yr1precip[i];
                climateScenariosBuff[i] = climateScenarios[i];
            }
            for (int i = rowIndex; i < size; i++){
                aaprecipBuff[i] = aaprecip[i+1];
                minprecipBuff[i] = minprecip[i+1];
                maxprecipBuff[i] = maxprecip[i+1];
                yr1precipBuff[i] = yr1precip[i+1];
                climateScenariosBuff[i] = climateScenarios[i+1];
            }
            aaprecip = aaprecipBuff;
            minprecip = minprecipBuff;
            maxprecip = maxprecipBuff;
            yr1precip = yr1precipBuff;
            climateScenarios = climateScenariosBuff;
            fireTableDataChanged();
        }
    }
    /**
     * Method to take a string value of a cclm file and add it to the list of climate scenarios
     * @param readString 
     */
    public void addClimateScenario(String readString){
        
        int size = climateScenarios.length;
        if(climateScenarios[0].description != DataClimate.NULL_DESCRIP){
            size++;
            double[] aaprecipBuff = new double[size];
            double[] minprecipBuff = new double[size];
            double[] maxprecipBuff = new double[size];
            double[] yr1precipBuff = new double[size];
            DataClimate[] climateScenariosBuff = new DataClimate[size];
            
            for (int i = 0; i < size -1; i++){
                aaprecipBuff[i] = aaprecip[i];
                minprecipBuff[i] = minprecip[i];
                maxprecipBuff[i] = maxprecip[i];
                yr1precipBuff[i] = yr1precip[i];
                climateScenariosBuff[i] = climateScenarios[i];
            }
            aaprecip = aaprecipBuff;
            minprecip = minprecipBuff;
            maxprecip = maxprecipBuff;
            yr1precip = yr1precipBuff;
            climateScenarios = climateScenariosBuff;
        }
        climateScenarios[size-1] = new DataClimate(readString);
        aaprecip[size-1] = climateScenarios[size-1].getAnnualAvgPrecip();
        minprecip[size-1] = climateScenarios[size-1].getMinimumAnnualPrecip();
        maxprecip[size-1] = climateScenarios[size-1].getMaximumAnnualPrecip();
        yr1precip[size-1] = climateScenarios[size-1].getyr1Precip();
        fireTableDataChanged();
        
    }

    
}
