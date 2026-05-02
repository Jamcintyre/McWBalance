/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.node;

import com.mcwbalance.generics.DataTimeDoubleSeries;
import com.mcwbalance.settings.Limit;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.table.AbstractTableModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Table for storing day vs elevation data
 * @author amcintyre
 */
public class TableNodeLevel extends AbstractTableModel {
    
    private final String[] columnNames = new String[3];
    private final Object[][] data;
    private int datalength;
    

    private final static int DAY_NULL = DataTimeDoubleSeries.DAY_NULL;
    private final static double VAL_NULL = DataTimeDoubleSeries.VAL_NULL; 
    
    /**
     * Blank table constructor
     */
    
    /*
    TableNodeLevel(){
        
        data = new Object[Limit.MAX_LEVELS][3];
        
        data[0][0] = 0;
        data[0][1] = 0;
        data[0][2] = 0;
        for (int i = 1; i < Limit.MAX_LEVELS; i ++){
            data[i][0] = DAY_NULL;
            data[i][1] = VAL_NULL;
            data[i][2] = "Placeholder";
        }
    }
    */
    /**
     * used for generating a table with a fest size
     * @param length length of starting array
     * @param col0Name name of first column
     * @param col1Name name of Second column
     * @param col2Name name of third column
     * 
     */
    TableNodeLevel(int length, String col0Name, String col1Name, String col2Name){
        
        columnNames[0] = col0Name;
        columnNames[1] = col1Name;
        columnNames[2] = col2Name;
        
        data = new Object[Limit.MAX_LEVELS][3];

        
        data[0][0] = 0;
        data[0][1] = 0;
        data[0][2] = "Placeholder";
        
        for (int i = 1; i < Limit.MAX_LEVELS; i ++){
            data[i][0] = DAY_NULL;
            data[i][1] = VAL_NULL;
            data[i][2] = "Placeholder";
        }
        this.fireTableDataChanged();
        
    }
    
    
     /**
     * Used for getting a save file formatted XML element to append into a larger doc
     * Only appends if length > 0
     * @see getXMLElement
     * @param element Element to append too
     * @param xMLDoc 
     * @param tagname 
     */
    public void appendXMLElement(Element element, Document xMLDoc, String tagname){
        if(datalength >0){
            element.appendChild(getXMLElement(xMLDoc, tagname));
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
    
    /**
     * Used for getting a save file formatted XML element to append into a
     * larger doc
     *
     * @param xMLDoc Document required to generate element,
     * @param tagname Name of element
     * @return
     */
    public Element getXMLElement(Document xMLDoc, String tagname) {
        Element ele = xMLDoc.createElement(tagname);
        for (int i = 0; i < datalength; i++) {
            Element cele = xMLDoc.createElement("row");
            cele.setAttribute("Index", String.valueOf(data[i][0]));
            cele.setAttribute("Name", String.valueOf(data[i][1]));
            cele.setAttribute("Comment", String.valueOf(data[i][2]));
            ele.appendChild(cele);
        }
        return ele;
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
                if(selectedcol.length > 2){ // maximum columns for this data type is 3. 
                    data[0][1] = 0.0;
                }
            }
            else if (selectedcol[0] == 1){
                data[0][1] = 0.0;
                data[0][1] = "empty";
            }
            fireTableDataChanged();     
        }
    }
    /**
     * used for setting all data in table 
     * @param day
     * @param level
     * @param basis string containing comments on the basis of the data
     */
    public void setAllData(int day[], double level[], String basis[]){
        if (day.length <= level.length){
            datalength = day.length;
        }else{
            datalength = level.length;
        }
        for (int i = 0; i < datalength; i ++){
            data[i][0] = day[i];
            data[i][1] = level[i];
            data[i][2] = basis[i];
        }
        fireTableDataChanged();    
    }

}
