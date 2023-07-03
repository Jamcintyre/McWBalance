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
 * @author Alex
 */
public class TableTailingsDepositionRates extends AbstractTableModel{
    private static final String[] columnNames = {"Model Day", "Rate (tonnes/day)","Solids Content (by mass)","Specific Gravity","Final Settled Density (tonne/cu.m)","Water with Solids (cu.m./day)", "Voidloss (cu.m./day)","Volume of Settled Solids (cu.m./day)"};
    private static final int NUMBER_OF_COLUMNS = 8;
    private Object[][] data = new Object[ObjELM.MAX_DEPO_RATES][NUMBER_OF_COLUMNS];
    private static final int DAY_MAX = ProjSetting.MAX_DURATION;
    private static final int DAY_MIN = 0;
    private static final int DAY_NULL = DAY_MAX + 1;
    private static final double RATE_MIN = 0;
    private static final double SOLIDS_CONT_MIN = 0;
    private static final double SOLIDS_CONT_MAX = 1;
    private static final double SG_MIN = 0;
    private static final double SG_MAX = 22;
    private static final double SETTLED_MIN = 0;
    private static final double SETTTLED_MAX = 22;
    public static final int MAX_LENGTH = 10;
    public static final int MAX_RATES = 20; 
    
    private int length; 
    TableTailingsDepositionRates(){
        data[0][0] = (int)0;//"Model Day", 
        data[0][1] = (double)0; //"Rate (Tonnes per Day)",
        data[0][2] = (double)0; //"Solids Content (by mass)",
        data[0][3] = (double)0; //"Specific Gravity",
        data[0][4] = (double)0; //"Final Settled Density (dry tonne/cu.m)",
        data[0][5] = (double)0; //"Water with Solids (cu.m./day)", 
        data[0][6] = (double)0; //"Voidloss (cu.m./day)"
        data[0][7] = (double)0; //"Volume of Settled Solids (cu.m./day)"
        length = 1;
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
            if ((int)value < DAY_MIN){
                data[row][col] = (int)0;
            }else{
                data[row][col] = (int)value;
            }
        }else if(col == 1){ // prevents negitive rate
            if ((double)value < RATE_MIN){
                data[row][col] = (double)0;
            }else{
                data[row][col] = (double)value;
            }
        }else if(col == 2){ // cannot be less then 
            if ((double)value < SOLIDS_CONT_MIN){
                data[row][col] = (double)SOLIDS_CONT_MIN;
            }else if((double)value >SOLIDS_CONT_MAX){
                data[row][col] = (double)SOLIDS_CONT_MAX; // cannot be more then 100% solids
            }else{
                data[row][col] = (double)value;
            }
        }else if(col == 3){ // prevents negitive SG, and prevents impossible SG
            if ((double)value < SG_MIN){
                data[row][col] = (double)SG_MIN;
            }else if((double)value >SG_MAX){
                data[row][col] = (double)SG_MAX; // densist material on earth is 22g/cm3
            }else{
                data[row][col] = (double)value;
            }
        }else{
            data[row][col] = value;
        }
        calculateRow(row); // implements custom row calculator
        fireTableCellUpdated(row,col);
    }
    
    /**
     * Will take in Model Day, rate, solids, specific Gravity, Final Settled Density 
     * @param lineIn Tab delimited data 
     * @param row row number
     */
    public void setRowFromString(String lineIn, int row){
        String columns[] = lineIn.split("\t");
        if (columns.length > 5){
            setValueAt(Integer.parseInt(columns[0]),row,0);
            setValueAt(Double.parseDouble(columns[1]),row,1);
            setValueAt(Double.parseDouble(columns[2]),row,2);
            setValueAt(Double.parseDouble(columns[3]),row,3);
            setValueAt(Double.parseDouble(columns[4]),row,4);
            calculateRow(row);
        }

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
    
    /**
     * not fully implemented yet, should utilize the set row from string method
     * @see setRowFromString
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
    
    
   

    /**
     * Rate tonnes/day data[i][1] Min = 0
     */
    public double getRate(int day){
        for (int i = 0; i < getRowCount(); i ++){
            if ((int)data[i][0] == day){
                return (double)data[i][1];
            }
            if ((int)data[i][0] > day && i > 0){
                return (double)data[i-1][1];
            }
        }
        return (double)data[0][1];
    }
    

    /**
     * Solids Content (by mass) data[i][2] Max = 1.00, Min = 0
     */
    public double getSolidsCont(int day){
        for (int i = 0; i < getRowCount(); i ++){
            if ((int)data[i][0] == day){
                return (double)data[i][2];
            }
            if ((int)data[i][0] > day && i > 0){
                return (double)data[i-1][2];
            }
        }
        return (double)data[0][2];
    }
    
    /**
     * Specific gravity data[i][3], between 0 and 22 weight of osmium data[i][3]
     */
    public double getSG(int day){
        for (int i = 0; i < getRowCount(); i ++){
            if ((int)data[i][0] == day){
                return (double)data[i][3];
            }
            if ((int)data[i][0] > day && i > 0){
                return (double)data[i-1][3];
            }
        }
        return (double)data[0][3];
    }
    
    /**
     * final dry settled density in tonnes / m3 data[i][4] Min = 0, Max = 22
     * tonnes/m3 (solids mass of osmium
     */
    public double getSettledDensity(int day){
        for (int i = 0; i < getRowCount(); i ++){
            if ((int)data[i][0] == day){
                return (double)data[i][4];
            }
            if ((int)data[i][0] > day && i > 0){
                return (double)data[i-1][4];
            }
        }
        return (double)data[0][4];
    }
    

    /**
     * Volume of water with the solids out of pipe (cu.m. per day) data [i][5]
     */
    public double getWaterWithSolids(int day){
        for (int i = 0; i < getRowCount(); i ++){
            if ((int)data[i][0] == day){
                return (double)data[i][5];
            }
            if ((int)data[i][0] > day && i > 0){
                return (double)data[i-1][5];
            }
        }
        return (double)data[0][5];
    }

    /**
     * Volume of voids per day (cu.m. per day) data [i][6];
     */
    public double getVolVoids(int day){
        for (int i = 0; i < getRowCount(); i ++){
            if ((int)data[i][0] == day){
                return (double)data[i][6];
            }
            if ((int)data[i][0] > day && i > 0){
                return (double)data[i-1][6];
            }
        }
        return (double)data[0][6];
    }

    /**
     * Volume of Solids and Voids;
     */
    public double getVolSolids(int day){
        for (int i = 0; i < getRowCount(); i ++){
            if ((int)data[i][0] == day){
                return (double)data[i][7];
            }
            if ((int)data[i][0] > day && i > 0){
                return (double)data[i-1][7];
            }
        }
        return (double)data[0][7];
    }
    
    
     public void sortAssending() {
        Object swap[] = new Object[NUMBER_OF_COLUMNS];
        for (int i = 0; i < length; i++) { // may need to move the i++ and j++, want to repeat the step if needed;
            for (int j = i + 1; j < length; j++) {
                if ((int)data[j][0] < (int)data[i][0]) { // if it matches leave it alone, the doublicate cleaner will deal with it; 
                    swap = data[j];
                    for (int k = j - 1; k > i - 1; k--) {
                        data[k + 1] = data[k];
                    }
                    data[i] = swap;
                    i = -1; // assumes i++ will bring this back to 0 and loop will restart;
                    break;
                }
            }
        }
        fireTableDataChanged();
    }
    /*
    public void removeDoupsAndNulls(){
        double cDay;
        for (int i = 0; i < length; i ++){
            cDay = rates[i].getDay();
            for (int j = i + 1; j < length; j ++){
                if (rates[j].getDay() == cDay || rates[j].isNull()){
                    for (int k = j + 1; k < length; k ++){
                        rates[k-1] = rates[k];
                    }
                    length--;
                }
            }
        }
        if (length != rates.length){
            DataTailingsDepositionRate newRates[] = new DataTailingsDepositionRate[length];
             
            for (int i = 0; i < length; i ++){
                newRates[i] = rates[i];
            }
            rates[0].setDay(0); // ensures first day is 0, since will need to lookup rate for day 0
            rates = newRates;
        }    
    }
    */

    
    
}
