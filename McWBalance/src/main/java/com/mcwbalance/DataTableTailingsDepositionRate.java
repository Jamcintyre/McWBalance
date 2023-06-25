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
public class DataTableTailingsDepositionRate extends AbstractTableModel{
        
    private static final String[] columnNames = {"Model Day", "Rate (tonnes/day)","Solids Content (by mass)","Specific Gravity","Final Settled Density (tonne/cu.m)","Water with Solids (cu.m./day)", "Voidloss (cu.m./day)","Volume of Settled Solids (cu.m./day)"};
    private final Object[][] data = new Object[ObjELM.MAX_DEPO_RATES][8];
    
    public static final int MAX_LENGTH = 10;
    public static final int MAX_RATES = 20;    
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
        else if (columnIndex > 4){
            return false; // will not be able to directly edit fields that must be calculated
        }
        return true; // sets all data to editable
    }
    @Override 
    public void setValueAt(Object value, int row, int col){
        if(value == null && row == 0){ // need to prevent user from nulling up the first row to avoid losing the class
            switch(col){
                case 1:
                    data[0][0] = (int)0;
                case 2:
                    data[0][1] = (double)0;
                case 3:
                    data[0][2] = (double)0;
                case 4:
                    data[0][3] = (double)0;
                case 5:
                    data[0][4] = (double)0;
            }
        }else if(col == 0){ // prevents negitive value in the day column
            if ((int)value < 0){
                data[row][col] = (int)0;
            }else{
                data[row][col] = (int)value;
            }
        }else if(col == 1){ // prevents negitive rate
            if ((double)value < 0){
                data[row][col] = (double)0;
            }else{
                data[row][col] = (double)value;
            }
        }else if(col == 2){ // cannot be less then 
            if ((double)value < 0){
                data[row][col] = (double)0;
            }else if((double)value >1){
                data[row][col] = (double)1; // cannot be more then 100% solids
            }else{
                data[row][col] = (double)value;
            }
        }else if(col == 3){ // prevents negitive SG, and prevents impossible SG
            if ((double)value < 0){
                data[row][col] = (double)0;
            }else if((double)value >22){
                data[row][col] = (double)22; // densist material on earth is 22g/cm3
            }else{
                data[row][col] = (double)value;
            }
        }else{
            data[row][col] = value;
        }
        calculateRow(row); // implements custom row calculator
        fireTableCellUpdated(row,col);
    }
    public void setAllData(DataTailingsDepositionRateList ratelist){
        for (int i = 0; i < ratelist.length; i ++){
            
            
            
        }
        for (int i = ratelist.length -1; i < MAX_RATES; i ++){
            
            
            
        }
    }
    
    
    
    /**
     * should not be required since setAllData from an initialized list will be called
     * @deprecated 
     */
    public void setBlankFirstRow (){
    
        data[0][0] = (int)0;//"Model Day", 
        data[0][1] = (double)0; //"Rate (Tonnes per Day)",
        data[0][2] = (double)0; //"Solids Content (by mass)",
        data[0][3] = (double)0; //"Specific Gravity",
        data[0][4] = (double)0; //"Final Settled Density (dry tonne/cu.m)",
        data[0][5] = (double)0; //"Water with Solids (cu.m./day)", 
        data[0][6] = (double)0; //"Voidloss (cu.m./day)"
        data[0][7] = (double)0; //"Volume of Settled Solids (cu.m./day)"
    }
    public void calculateRow(int row){
        if(data[row][1] != null && data[row][2] != null && data[row][3] !=null && data[row][4] !=null){ // check first to ensure no nulls
            if((double)data[row][1] > 0 && (double)data[row][2] > 0 && (double)data[row][3] > 0 && (double)data[row][4] > 0){ // checks to ensure no zeros or negatives
                // if the info is good then
                
                double rate = (double)data[row][1];
                double sCont = (double)data[row][2];
                double sG = (double)data[row][3];
                double sDens = (double)data[row][4];
                
                double wWSolids = (rate / sCont)  * (1-sCont); //"Water with Solids (cu.m./day)", 
                double vSSolidsandVoids = rate/sDens;  //"Volume of Settled Solids (cu.m./day)"
                double vSSolidsperM = sDens/sG;
                //double voidLoss = vSSolids*(1-rate/sG); //"Voidloss (cu.m./day)"
                double voidLoss = (1-vSSolidsperM)*rate/sDens;
                
                
                
                CalcBasics.roundDbl(wWSolids, ProjSetting.precisionDay); // rounds off value based on project setting
                data[row][5] = wWSolids;
                
                CalcBasics.roundDbl(voidLoss, ProjSetting.precisionDay); // rounds off value based on project setting
                data[row][6] = voidLoss;
                
                CalcBasics.roundDbl(vSSolidsandVoids, ProjSetting.precisionDay); // rounds off value based on project setting
                data[row][7] = vSSolidsandVoids;
                fireTableDataChanged(); 
            }else {
                data[row][5] = (double)0;
                data[row][6] = (double)0;
                data[row][7] = (double)0;
            }
        }
        else {
            data[row][5] = (double)0;
            data[row][6] = (double)0;
            data[row][7] = (double)0;
        }

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

    
    
    
}
