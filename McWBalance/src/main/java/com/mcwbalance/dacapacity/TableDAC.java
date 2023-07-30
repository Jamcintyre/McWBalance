/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.dacapacity;

import com.mcwbalance.McWBalance;
import com.mcwbalance.settings.Limit;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.table.AbstractTableModel;

/**
 * Data structure used for storage of a Depth Area Capacity relationship
 * @author Alex
 */
public class TableDAC extends AbstractTableModel{
    
    String[] columnNames = McWBalance.langRB.getString("DAC_TABLE_HEADINGS").split(",");
    double[] elev;
    int[] area;
    int[] vol;
    
    
    /**
     * Constructs a DAC table with 1 row of 0's to start
     */
    public TableDAC() {
        elev = new double[1];
        elev[0] = 0;
        area = new int[1];
        area[0] = 0;
        vol = new int[1];
        vol[0] = 0; 
    }
    
    
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
    @Override 
    public int getColumnCount(){
        return 3;
    }
    @Override 
    public int getRowCount(){
        return elev.length;

    }
    @Override 
    public String getColumnName(int col){
        return columnNames[col];
    }
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
    @Override 
    public boolean isCellEditable(int rowIndex, int columnIndex){
        if (rowIndex ==0 && columnIndex > 0){
            return false;  
            // first Area and First Volume must be 0, this will enforce it.
        }
        return true; // sets all data to editable or should, does not seem to work
        
    }
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
                
                
                
                if (elev.length < clippedValueLines.length + selectedrow[0]){
                    resize(clippedValueLines.length + selectedrow[0]+1);
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
    
    private void resize(int rows){
        if(rows == elev.length + 1){
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
            
        }
    }
    
    
    public void removeRow(int row){
        if(row < 0 || row >= elev.length){
            //Do Nothing
        }
        // TO BE COMPLETED
        
    }
    
    
    public void addRow(){
        resize(elev.length+2);
    }
    
    
    
    
}
