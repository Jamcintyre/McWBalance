/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.element;

import com.mcwbalance.generics.DataTimeDoubleSeries;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.settings.Limit;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author amcintyre
 */
public class TableELMLevel extends AbstractTableModel {
    
    private final String[] columnNames = {"Model Day", "Level (m)"};
    private final Object[][] data = new Object[Limit.MAX_LEVELS][2];
    private int datalength;
    

     private final static int DAY_NULL = DataTimeDoubleSeries.DAY_NULL;
    private final static double VAL_NULL = DataTimeDoubleSeries.VAL_NULL; 
    
    TableELMLevel(){
        data[0][0] = 0;
        data[0][1] = 0;
        for (int i = 1; i < Limit.MAX_LEVELS; i ++){
            data[i][0] = DAY_NULL;
            data[i][1] = VAL_NULL;
        }
    }
    
        
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
        if (rowIndex == 0 && columnIndex == 0){
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
    public int[] getDayColumn(){
        datalength = 0; 
        for (int i = 0; i < Limit.MAX_LEVELS; i ++){
            if((int)data[i][0] == DAY_NULL || data[i][0] == null){
            break; 
            }
            datalength++;
        }
        if(datalength < 1){
            datalength = 1;
        }
        int outColumn[] = new int[datalength];
        for (int i = 0; i < datalength; i ++){
            outColumn[i] = (int)data[i][0];
        }
         return outColumn;
    }
    public double[] getLevelColumn(){
        double outColumn[] = new double[datalength];
        for (int i = 0; i < datalength; i ++){
            outColumn[i] = (double)data[i][1];
        }
         return outColumn;
    }
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
                        if (selectedcol[0] == 0){ // if its 0 it must be date
                            setValueAt(Integer.valueOf(clippedValueSingleLine[0]),i+selectedrow[0], 0);
                        }
                        else if (selectedcol[0] == 1){ // if its 1 it must be level
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
     public void setAllData(int inDay[], double inLevel[]){
        if (inDay.length <= inLevel.length){
            datalength = inDay.length;
        }else{
            datalength = inLevel.length;
        }
        for (int i = 0; i < datalength; i ++){
            data[i][0] = inDay[i];
            data[i][1] = inLevel[i];
        }
        fireTableDataChanged();    
    }

}
