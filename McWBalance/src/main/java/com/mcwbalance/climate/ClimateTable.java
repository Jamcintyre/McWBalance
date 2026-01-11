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

package com.mcwbalance.climate;

import com.mcwbalance.climate.DataClimate;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Alex
 */
public class ClimateTable extends AbstractTableModel{
    private static final String[] columnNames = {"Scenario", "Description","Length","Avg Ann Precip","Min Ann Precip","Max Ann Precip", "Yr 1 Precip"};
    public static final int NUMBER_OF_COLUMNS = 7;
    
    double[] aaprecip;
    double[] minprecip;
    double[] maxprecip;
    double[] yr1precip;
    DataClimate[] climates; 
    
    
    /**
     * Generates a null list of climates
     * @param size 
     */
    public ClimateTable(int size){
        aaprecip = new double[size];
        minprecip = new double[size];
        maxprecip = new double[size];
        yr1precip = new double[size];
        climates = new DataClimate[size];
        for (int i = 0; i < size; i++) {
            climates[i] = new DataClimate(1);        
            climates[i].description = DataClimate.NULL_DESCRIP;
            aaprecip[i] = 0;
            minprecip[i] = 0;
            maxprecip[i] = 0;
            yr1precip[i] = 0;
        }
    }
    
    /**
     * not useful
     * @deprecated 
     * @param climates 
     */
    public ClimateTable(DataClimate[] climates){
        this.climates = climates; 
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
                return climates[row].description;
            }
            case 2 -> {
                return climates[row].precip.length;
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
        return climates.length;
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
     *
     * @return provides access to climate list
     */
    public DataClimate[] getClimates(){
        return climates;
    }
    
    /**
     * Method to remove a climate set, if all data is removed a Null value will be placed into row 0
     * @param rowIndex 
     */
    public void removeRow(int rowIndex){
        if(rowIndex < 0 || rowIndex >= climates.length){
            //Do Nothing
        }
        else if(climates.length == 1){
            if(climates[0].description.equals(DataClimate.NULL_DESCRIP) ){
                //Do Nothing
            }
            else{
                climates[0] = new DataClimate(1);
                aaprecip[0] = 0;
                minprecip[0] = 0;
                maxprecip[0] = 0;
                yr1precip[0] = 0;
                fireTableRowsUpdated(0, 0);
            }
        }
        else{
            int size = climates.length -1;
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
                climateScenariosBuff[i] = climates[i];
            }
            for (int i = rowIndex; i < size; i++){
                aaprecipBuff[i] = aaprecip[i+1];
                minprecipBuff[i] = minprecip[i+1];
                maxprecipBuff[i] = maxprecip[i+1];
                yr1precipBuff[i] = yr1precip[i+1];
                climateScenariosBuff[i] = climates[i+1];
            }
            aaprecip = aaprecipBuff;
            minprecip = minprecipBuff;
            maxprecip = maxprecipBuff;
            yr1precip = yr1precipBuff;
            climates = climateScenariosBuff;
            fireTableDataChanged();
        }
    }
    /**
     * Method to take a string value of a cclm file and add it to the list of climate scenarios
     * @param readString 
     */
    public void addClimateScenario(String readString){
        
        int size = climates.length;
        if(climates[0].description != DataClimate.NULL_DESCRIP){
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
                climateScenariosBuff[i] = climates[i];
            }
            aaprecip = aaprecipBuff;
            minprecip = minprecipBuff;
            maxprecip = maxprecipBuff;
            yr1precip = yr1precipBuff;
            climates = climateScenariosBuff;
        }
        climates[size-1] = new DataClimate(readString);
        aaprecip[size-1] = climates[size-1].getAnnualAvgPrecip();
        minprecip[size-1] = climates[size-1].getMinimumAnnualPrecip();
        maxprecip[size-1] = climates[size-1].getMaximumAnnualPrecip();
        yr1precip[size-1] = climates[size-1].getyr1Precip();
        fireTableDataChanged();
        
    }

    
}
