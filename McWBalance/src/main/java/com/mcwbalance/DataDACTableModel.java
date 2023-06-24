/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

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
public class DataDACTableModel extends AbstractTableModel{
    
    private String[] columnNames = {"Elevation (m)", "Area (sq.m.)", "Total Volume (c.m.)"};
    private Object[][] data = new Object[DataDAC.MAX_LENGTH][3];
    
    private int datalength;

    
    DataDACTableModel(){
        datalength = DataDAC.MAX_LENGTH; 
        System.out.println("DataDACTableModel Initializer called");
        for( int i = 0; i < DataDAC.MAX_LENGTH; i++){
            data[i][0] = DataDAC.ELEV_NULL;
            data[i][1] = DataDAC.AREA_NULL;
            data[i][2] = DataDAC.AREA_NULL;
        }
        fireTableDataChanged(); 
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
        if (rowIndex ==0 && columnIndex > 0){
            return false;  
            // first Area and First Volume must be 0, this will enforce it.
        }
        
        return true; // sets all data to editable or should, does not seem to work
        
    }
    @Override 
    public void setValueAt(Object value, int row, int col){
        data[row][col] = value;
        fireTableCellUpdated(row,col);
        
        
    }
    /**
     * This method also sets the datalength and should be called imediately prior to the getAreaColumn and getVolColumn to avoid missmatch;
     * @return 
     */
    public double[] getElevColumn(){
        datalength = 0; 
        for (int i = 0; i < DataDAC.MAX_LENGTH; i ++){
            if((double)data[i][0] == DataDAC.ELEV_NULL || data[i][0] == null){
            break; 
            }
            datalength++;
        }
        if(datalength < 1){
            datalength = 1;
        }
        double outColumn[] = new double[datalength];
        for (int i = 0; i < datalength; i ++){
            outColumn[i] = (double)data[i][0];
        }
         return outColumn;
    }
    public int[] getAreaColumn(){
        int outColumn[] = new int[datalength];
        for (int i = 0; i < datalength; i ++){
            outColumn[i] = (int)data[i][1];
        }
         return outColumn;
    }
    public int[] getVolColumn(){
        int outColumn[] = new int[datalength];
        for (int i = 0; i < datalength; i ++){
            outColumn[i] = (int)data[i][2];
        }
         return outColumn;
    }
    public void setAllData(double[] inElev, int[] inArea, int inVol[]){
        if (inElev.length <= inArea.length && inElev.length <= inVol.length){
            datalength = inElev.length;
        }else if(inArea.length <= inElev.length && inArea.length <= inVol.length){
            datalength = inArea.length;
        }else{
            datalength = inVol.length;
        }
        for (int i = 0; i < datalength; i ++){
            data[i][0] = inElev[i];
            data[i][1] = inArea[i];
            data[i][2] = inVol[i];
        }
        fireTableDataChanged();    
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
    
    public void removeData(int[] selectedrow, int[] selectedcol){
        
        for (int i = 0; i < selectedrow.length; i++){
            if (selectedrow[i] != 0){ // if isn't row 0 then run the normal sequence
                for(int j = 0; j < selectedcol.length; j++){
                    data[selectedrow[i]][selectedcol[j]] = null;
                }
            }
            else if(selectedcol[0] == 0){ // confirms that elevation column is selected
                data[0][0] = 0.0; // only deletes the first elevation value,  Area and Vol maintained as 0. 
            }
            fireTableDataChanged();
                
        }
        
    }
    
}
