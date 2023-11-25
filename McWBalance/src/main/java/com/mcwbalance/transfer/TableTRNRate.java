/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.transfer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.table.AbstractTableModel;

/**
 * Contains the data input format used for JTables where user is required to define a time dependent flow rate, 
 * Column 0 i.e. data[][0] represents the date in days. Column 1 represents the daily flow rate in cu.m. per day. 
 * @author amcintyre
 */
public class TableTRNRate extends AbstractTableModel{
    /**
     * Column names for this model class are fixed as "Model Day" and "Rate (cu.m. per day)" modification of these 
     * names must be carried out within this class.
     */
    private final String[] columnNames = {"Model Day", "Rate (cu.m. per day)"};
    
    
    /**
     * Data structure for this table model is of fixed size
     */
    private final Object[][] data = new Object[TRN.MAX_PUMP_RATES][2];
        
    @Override 
    public Object getValueAt(int row, int col){
        return data[row][col];
    }
    @Override 
    public int getColumnCount(){
        return columnNames.length;
    }
    @Override 
    public int getRowCount(){
        return data.length;
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
        if (rowIndex ==0 && columnIndex == 0){
            return false;  
            // Must have a value for Day 0.
        }
        return true; // sets all data to editable
    }
    @Override 
    public void setValueAt(Object value, int row, int col){
        data[row][col] = value;
        fireTableCellUpdated(row,col);
    }
    /**
     * Initializes and or resets the first row of data to 0 values.
     */
    public void setBlankFirstRow(){
        data[0][0] = (int)0;
        data[0][1] = (double)0;
    }
    
    
    /**
     * Allows data currently in the clipboard to be pasted into the active table view. The data is assumed to be cut or copied from
     * MS Excel and is expected to be Tab delimited. 
     * @param selectedrow Used to select the starting point for data pasting. Note that Data pasted will not be limited to 1 row if
     * multiple rows of data are pasted
     * @param selectedcol Used to select the starting point for data pasting. Note that Data pasted will not be limited to 1 column if
     * multiple columns of data are pasted
     */
    public void pasteFromClipboard(int[] selectedrow, int[] selectedcol){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable content = clipboard.getContents(this);
        if (content != null){
            try{
                String clippedvalue = content.getTransferData(DataFlavor.stringFlavor).toString(); // string flavor seams easiest..  
                String clippedValueLines[] = clippedvalue.split("\\R"); // the \\R is cross platform for \n new line charactor
                String clippedValueSingleLine[]; // this is the final array of values, used for each line within a loop 
                
                for (int i = 0; i < clippedValueLines.length; i++){
                    clippedValueSingleLine = clippedValueLines[i].split("\\t");
                    if (clippedValueSingleLine.length == 2){ // if three columns are present then will assume all 2 columns are to be overwritten;
                        setValueAt(Integer.valueOf(clippedValueSingleLine[0]),i+selectedrow[0], 0); // day column
                        setValueAt(Double.valueOf(clippedValueSingleLine[1]),i+selectedrow[0], 1); // elevation column
                    }
                    if (clippedValueSingleLine.length == 1){ // if only 1 column present then will now need to find wich column
                        if (selectedcol[0] == 0){ // if its 0 it must be an int used for date
                            setValueAt(Integer.valueOf(clippedValueSingleLine[0]),i+selectedrow[0], 0);
                        }
                        else if (selectedcol[0] == 1){ // if its 1 it must be a double used for rate
                            setValueAt(Double.valueOf(clippedValueSingleLine[0]),i+selectedrow[0], 1);
                        }
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
     * Method responsible for data deletion from this class of table model. Data deletion is carried out with basic rules to prevent 
     * null values resulting in null class in row 0. 
     * @param selectedrow Indicates the rows that data will be removed / deleted from
     * @param selectedcol Indicates the columns that data will be removed / deleted from
     */
    public void removeData(int[] selectedrow, int[] selectedcol){ //allow deletion of data but first row must remain
        for (int i = 0; i < selectedrow.length; i++){
            if (selectedrow[i] != 0){ // if isn't row 0 then run the normal sequence
                for(int j = 0; j < selectedcol.length; j++){
                    data[selectedrow[i]][selectedcol[j]] = null;
                }
            }
            else if(selectedcol[0] == 0){ // confirms that elevation column is selected

                data[0][0] = 0; // only deletes the first elevation value,  Area and Vol maintained as 0. 
                if(selectedcol.length > 1){ // maximum columns for this data type is 2. 
                    data[0][1] = 0.0;
                }
            }
            else if (selectedcol[0] == 1){
                data[0][1] = 0.0;
            }
            fireTableDataChanged();     
        }
    }

}
