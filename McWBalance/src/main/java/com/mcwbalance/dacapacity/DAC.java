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

package com.mcwbalance.dacapacity;

import com.mcwbalance.McWBalance;
import com.mcwbalance.measure.Area;
import com.mcwbalance.measure.Depth;
import com.mcwbalance.measure.Volume;
import com.mcwbalance.settings.Limit;
import com.mcwbalance.settings.Preferences;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.table.AbstractTableModel;

/**
 * Table model used for input and management of Depth Area Capacity Curves
 * 
 * TODO - implement variable units; 
 * @author Alex McIntyre
 */
public class DAC extends AbstractTableModel{
    String[] columnNames = McWBalance.langRB.getString("DAC_TABLE_HEADINGS").split(",");
    double[] elev;
    int[] area;
    int[] vol;
    
    private final Area.AreaUnit areaunit;
    private final Depth.DepthUnit elunit;
    private final Volume.VolumeUnit volunit;
    
    DACPlotGraphics plotGraphic;
    
    /**
     * Constructs a DAC table with 1 row of 0's to start
     */
    public DAC() {
        
        elunit = Depth.DepthUnit.m;
        areaunit = Area.AreaUnit.m2;
        volunit = Volume.VolumeUnit.m3;
        
        elev = new double[1];
        elev[0] = 0;
        area = new int[1];
        area[0] = 0;
        vol = new int[1];
        vol[0] = 0; 
        plotGraphic = new DACPlotGraphics(this);
    }
    
    /**
     * Adds a blank row to the DAC tables and fires resize
     */
    public void addRow(){
        resize(elev.length+1);
    }
    
    /**
     * used for determining what the current units of measure are for the area
     * in the dac; 
     * @return The current area unit of the dac;
     */
    public Area.AreaUnit getAreaUnit(){
        return areaunit;
    }
    
    /**
     * used for determining what the current units of measure are for the elevation
     * in the dac; 
     * @return The current elevation unit of the dac;
     */
    public Depth.DepthUnit getElevationUnit(){
        return elunit;
    }
    
    /**
     * used for determining what the current units of measure are for the volume
     * in the dac; 
     * @return The current volume unit of the dac;
     */
    public Volume.VolumeUnit getVolumeUnit(){
        return volunit;
    }
    
    /**
     * Returns the volume corresponding to a given elevation using linear 
     * interpolation 
     * @param targetElv
     * @return 
     */
    public int getVolfromEl(double targetElv){
        double slopeofVol;
        if(targetElv <= elev[0]){
            return 0; // if below the DAC it must return a 0;
        }
        if(targetElv >= elev[elev.length-1]){
            return vol[elev.length-1]; // if DAC is maxed out return max volume
        }
        for (int i = 1; i < elev.length; i++){ // starts search at 1, since index 0 is 0
            if (elev[i] >= targetElv){ // first elevation past the mark
                if (elev[i] == targetElv){
                    return vol[i]; // in case of Exact Match can skip the interpolation
                }
               slopeofVol = (vol[i] - vol[i-1])/(elev[i] - elev[i-1]);
               return (int)(vol[i-1] + (targetElv - elev[i-1])*slopeofVol); // returns interpolated Match
            }   
        }
        return -1; //Should never return -1 unless something is messed. 
    }
    
    /**
     * Returns the area corresponding to a given elevation using linear 
     * interpolation 
     * @param targetElv
     * @return area, -1 is returned if there is an error
     */
    public int getAreafromEl(double targetElv){
        double slopeofarea;
        if(targetElv <= elev[0]){
            return 0; // if below the DAC it must return a 0;
        }
        if(targetElv >= elev[elev.length-1]){
            return area[elev.length-1]; // if DAC is maxed out return max volume
        }
        for (int i = 1; i < elev.length; i++){ // starts search at 1, since index 0 is 0
            if (elev[i] >= targetElv){ // first elevation past the mark
                if (elev[i] == targetElv){
                    return area[i]; // in case of Exact Match can skip the interpolation
                }
               slopeofarea = (area[i] - area[i-1])/(elev[i] - elev[i-1]);
               return (int)(area[i-1] + (targetElv - elev[i-1])*slopeofarea); // returns interpolated Match
            }   
        }
        return -1; //Should never return -1 unless something is messed. 
    }
    
    /**
     * Returns the area corresponding to a given volume using linear 
     * interpolation 
     * @param targetVol
     * @return area, -1 is returned if there is an error
     */
    public int getAreafromVol(int targetVol){
        double slopeofarea;
        if(targetVol <= vol[0]){
            return 0; // if below the DAC it must return a 0;
        }
        if(targetVol >= vol[elev.length-1]){
            return area[elev.length-1]; // if DAC is maxed out return max volume
        }
        for (int i = 1; i < elev.length; i++){ // starts search at 1, since index 0 is 0
            if (vol[i] >= targetVol){ // first elevation past the mark
                if (vol[i] == targetVol){
                    return area[i]; // in case of Exact Match can skip the interpolation
                }
               slopeofarea = (area[i] - area[i-1])/(vol[i] - vol[i-1]);
               return (int)(area[i-1] + (targetVol - vol[i-1])*slopeofarea); // returns interpolated Match
            }   
        }
        return -1; //Should never return -1 unless something is messed. 
    }
    /**
     * Returns the elevation corresponding to a given volume using linear 
     * interpolation 
     * @param targetVol
     * @return elevation, -9999 is returned if there is an error
     */
    public double getElfromVol(int targetVol){
        double slopeofelev;
        if(targetVol <= vol[0]){
            return 0; // if below the DAC it must return a 0;
        }
        if(targetVol >= vol[elev.length-1]){
            return elev[elev.length-1]; // if DAC is maxed out return max volume
        }
        for (int i = 1; i < elev.length; i++){ // starts search at 1, since index 0 is 0
            if (vol[i] >= targetVol){ // first elevation past the mark
                if (vol[i] == targetVol){
                    return elev[i]; // in case of Exact Match can skip the interpolation
                }
               slopeofelev = (elev[i] - elev[i-1])/(vol[i] - vol[i-1]);
               return (elev[i-1] + (targetVol - vol[i-1])*slopeofelev); // returns interpolated Match
            }   
        }
        return -9999; //Should never return -1 unless something is messed.   
    }
    
    
    
    /**
     * Retrieves the current value of a specific come and row
     * @param row index of row to look
     * @param col 0 Elevation, 1 area, 2, volume
     * @return object located at the specified row and column
     */
    @Override     
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0 -> {
                return elev[row];
            }
            case 1 -> {
                return area[row];
            }
            case 2 -> {
                return vol[row];
            }
            default -> {
                return -1;
            }
        }
    }
    
    /**
     * Dac's only have 3 columns, depth, area, capacity;
     * @return 
     */
    @Override 
    public int getColumnCount(){
        return 3;
    }
    /**
     * Number of values in the dac
     * @return 
     */
    @Override 
    public int getRowCount(){
        return elev.length;

    }
    
    /**
     * Used for pulling column name
     * @param col column to get name from i.e. col 0 is elevation
     * @return Current name of the column in english
     */
    @Override 
    public String getColumnName(int col){
        return columnNames[col];
    }
    
    /**
     * For getting the class of data contained in the specified column
     * @param col column to check class of 
     * @return class of column col
     */
    @Override 
    public Class getColumnClass(int col) {
        switch (col) {
            case 0 -> {
                return Double.class;
            }
            default -> {
                return Integer.class;
            }
        }
    }
    
    /**
     * used for determining of a cell can be overwritten
     * First Area and First Volume must be 0, this will enforce it.
     * @param row row to check
     * @param col column to check 
     * @return false if cannot edit, true if edits are allowed
     */
    @Override 
    public boolean isCellEditable(int row, int col){
        if (row ==0 && col > 0){
            return false;  
            // first Area and First Volume must be 0, this will enforce it.
        }
        return true; // sets all data to editable or should, does not seem to work
        
    }
    
    /**
     * function used to facilitate pasting from clipboard
     * @param selectedrow
     * @param selectedcol 
     */
    public void pasteFromClipboard(int[] selectedrow, int[] selectedcol){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable content = clipboard.getContents(this);
        if (content != null){
            try{
                String clippedvalue = content.getTransferData(DataFlavor.stringFlavor).toString(); // string flavor seams easiest..  
                String clippedValueLines[] = clippedvalue.split("\\R"); // the \\R is cross platform for \n new line charactor
                String clippedValueSingleLine[]; // this is the final array of values, used for each line within a loop 
                
                
                //This part is a bit off, 
                if (elev.length < clippedValueLines.length + selectedrow[0]){
                    resize(clippedValueLines.length + selectedrow[0]);
                }
                
                
                for (int i = 0; i < clippedValueLines.length; i++){
                    clippedValueSingleLine = clippedValueLines[i].split("\\t");
                    if (clippedValueSingleLine.length == 3){ // if three columns are present then will assume all 3 columns are to be overwritten;
                        setValueAt(Double.valueOf(clippedValueSingleLine[0]),i+selectedrow[0], 0); // elevation column
                        setValueAt(Integer.valueOf(clippedValueSingleLine[1]),i+selectedrow[0], 1); // elevation column
                        setValueAt(Integer.valueOf(clippedValueSingleLine[2]),i+selectedrow[0], 2); // elevation column                        
                    }
                }
                fireTableDataChanged();                
            }
            catch (UnsupportedFlavorException e){
                System.err.println("Unsupported Data Type " + e.getLocalizedMessage());
            }
            catch (IOException e){
                System.err.println("Data Consumed Exception " + e.getLocalizedMessage());
            }
        }
    }
    

    /**
     * Removes the selected row unless the row selected is 0;
     * @param row 
     */
    public void deleteRow(int row){
        if(row < 0 || row >= elev.length){
            return;//Do Nothing
        }
        for (int i = row; i < elev.length; i++){
            elev[row] = elev[row+1];
            area[row] = area[row+1];
            vol[row] = vol[row+1];
        }
        this.resize(elev.length -1);
    }
    
    /**
     * used to clean up DAC table, we can only have 1 unique volume and area for 
     * a given elevation, this method will remove the second instance of any elevation
     * value
     */
    public void removeDoupAndNulls(){
        int length = elev.length;
        double cElev;
        for (int i = 0; i < length; i ++){
            cElev = elev[i];
            for (int j = i + 1; j < length; j ++){
                if (elev[j] == cElev || area[j] < 0 || vol[j] < 0){
                    for (int k = j + 1; k < length; k ++){
                        elev[k-1] = elev[k];
                        area[k-1] = area[k];
                        vol[k-1] = vol[k];
                    }
                    length--;
                }
            }
        }
        if (length != elev.length){
            double newElev[] = new double[length];
            int newArea[] = new int[length];
            int newVol[] = new int[length];
            newElev[0] = elev[0];
            newArea[0] = 0;
            newVol[0] = 0;
            for (int i = 1; i < length; i ++){
                newElev[i] = elev[i];
                newArea[i] = area[i];
                newVol[i] = vol[i];
            }
            elev = newElev;
            area = newArea;
            vol = newVol;
        }
        fireTableDataChanged();
    }
    
    /**
     * Used to re-initialize the array with an array of a new size
     * @param rows 
     */
    private void resize(int rows){
        if(rows == elev.length){
            return;
        }
        else if (rows > Limit.MAX_DAC_SIZE){
            rows = Limit.MAX_DAC_SIZE;
        }
        if (rows > 0){
            double[] nElev = new double[rows];
            int[] nArea = new int[rows];
            int[] nVol = new int[rows];
            for (int i = 0; i < elev.length && i < nElev.length; i++){
                nElev[i] = elev[i];
                nArea[i] = area[i];
                nVol[i] = vol[i];
            }
            elev = nElev;
            area = nArea;
            vol = nVol;
            fireTableDataChanged();
        }
    }
    
    /**
     * Sets a value without checking if editable or not
     * @param value value to set at location row and col
     * @param row row to set value at
     * @param col col to set value at
     */
    @Override 
    public void setValueAt(Object value, int row, int col){
        if (row > 0 && row < elev.length) {
            switch (col) {
                case 0 -> {
                    elev[row] = (double)value;
                }
                case 1 -> {
                    area[row] = (int)value;
                }
                case 2 -> {
                    vol[row] = (int)value;
                }
            }
        }
        fireTableCellUpdated(row,col);
    }
    
    /**
     * Depth Area Capacity curves must work in ascending order, this method 
     * resorts the array
     */
    public void sortAscending(){
        double swapElev;
        int swapArea;
        int swapVol;
        for (int i = 0; i < elev.length; i ++){ // may need to move the i++ and j++, want to repeat the step if needed;
            for (int j = i+1; j < elev.length; j ++){
                if(elev[j] < elev[i]){ // if it matches leave it alone, the doublicate cleaner will deal with it; 
                    swapElev = elev[j];
                    swapArea = area[j];
                    swapVol = vol[j];
                    for (int k = j-1; k > i-1; k --){
                        elev[k+1] = elev[k];
                        area[k+1] = area[k];
                        vol[k+1] = vol[k];
                    }
                    elev[i] = swapElev;
                    area[i] = swapArea;
                    vol[i] = swapVol;
                    i = -1; // assumes i++ will bring this back to 0 and loop will restart;
                    break; 
                }
            }
        }
    }
    
    /**
     * for generating a string value for cut and paste, using tab delimiting
     * @return 
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();        
        String nl = System.getProperty("line.separator");
        int i;
        for (i = 0; i < columnNames.length; i++){
            sb.append(columnNames[i]);
            sb.append("\t");
        }
        sb.append(nl);
        for (i = 0; i < elev.length; i++) {
            sb.append(elev[i]);
            sb.append("\t");
            sb.append(area[i]);
            sb.append("\t");
            sb.append(vol[i]);
            sb.append(nl);
        }
        sb.append(Preferences.LIST_TERMINATOR);
        return sb.toString();
    }
    /**
     * Provides a graphic plot of the DAC by calling DACPlotGraphics on this
     * 
     */
    public void setPlotGraphic(){
        plotGraphic = new DACPlotGraphics(this);
      
    }

}
