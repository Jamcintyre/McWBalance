
package com.mcwbalance.dacapacity;

import com.mcwbalance.McWBalance;
import com.mcwbalance.settings.Limit;
import com.mcwbalance.settings.Preferences;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.table.AbstractTableModel;

/**
 * 
 * @author Alex
 */
public class DAC extends AbstractTableModel{
    String[] columnNames = McWBalance.langRB.getString("DAC_TABLE_HEADINGS").split(",");
    double[] elev;
    int[] area;
    int[] vol;
    
    
    DACPlotGraphics plotGraphic;
    
    /**
     * Constructs a DAC table with 1 row of 0's to start
     */
    public DAC() {
        elev = new double[1];
        elev[0] = 0;
        area = new int[1];
        area[0] = 0;
        vol = new int[1];
        vol[0] = 0; 
        plotGraphic = new DACPlotGraphics(this);
    }
    
    public void addRow(){
        resize(elev.length+1);
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
    
    public void sortAssending(){
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
     * @return 
     */
    public void setPlotGraphic(){
        plotGraphic = new DACPlotGraphics(this);
      
    }
    
    
    
    
    
    
    
    


}
