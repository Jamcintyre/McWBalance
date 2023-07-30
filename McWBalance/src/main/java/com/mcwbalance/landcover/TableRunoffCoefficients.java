/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.landcover;

import com.mcwbalance.McWBalance;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.settings.Limit;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Alex
 */
public class TableRunoffCoefficients extends AbstractTableModel{
    String[] columnNames = McWBalance.langRB.getString("RUNOFF_COEFFICIENTS_TABLE_HEADINGS").split(",");
    private static final int NUMBER_OF_COLUMNS = 13;
    private static final int MAX_LAND_COVERS = Limit.MAX_LAND_COVERS;
    private Object[][] data;
    private int length = 1; 
    
    private static final double COEFF_MIN = 0;
    private static final double COEFF_MAX = 1;
    
    public TableRunoffCoefficients(){
        data = new Object[MAX_LAND_COVERS][NUMBER_OF_COLUMNS];
        data[0][0] = "Undisturbed";
        for (int i = 1; i < 13; i++){
            data[0][i] = (double)1;
        }
    }
    
    /**
     * used for implementing sorting and null value cleanup
     */
    public void cleanUp(){
    }
    
    /**
     * Copies data to clipboard in string format
     */
    public void copyToClipBoard(){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection content = new StringSelection(toString()); 
        clipboard.setContents(content, content);
        
        
    }
    /**
     * Used to select runoff coeffiecient from data table
     * @param landCover Sting name of landcover must match a cover in this table
     * @param month Jan = 1, Feb = 2, etc...
     * @return value generally from 0.00 to 1.00 representing the runoff coeffient
     */
    public double getCoefficient(String landCover, int month){
        for (int i = 0; i < length; i++){
            if (landCover.equals(data[i][0])){
                return (double)data[i][month];
            }
        }
        return 0;
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
        return true;
        
    }
    @Override
    public void setValueAt(Object value, int row, int col){
        if(value == null && row == 0){ // need to prevent user from nulling up the first row to avoid losing the class
            switch(col){
                case 0 -> data[0][0] = "NONE";
                default -> data[0][col] = (double)0;
            }
        }
        switch(col){
            case 0 -> data[row][0] = (String)value;
            default ->{
                if ((double)value < COEFF_MIN){
                    data[row][col] = COEFF_MIN;
                }
                else if((double)value > COEFF_MAX){
                    data[row][col] = COEFF_MAX;                
                }
                else{
                    data[row][col] = (double)value;                
                }
            }
        }
        fireTableCellUpdated(row,col);
    }
    
    
    @Override
    public String toString(){
        String nextLine = System.getProperty("line.separator");
        StringBuilder saveString = new StringBuilder(); 
        
        cleanUp();

        saveString.append(columnNames[0]);
        for (int i = 1; i < columnNames.length; i++){
            saveString.append("\t");
            saveString.append(columnNames[i]);
            
        }
        saveString.append(nextLine);
        
        for (int i = 0; i < length; i ++){
            saveString.append(data[i][0]);
            for (int j = 1; j < NUMBER_OF_COLUMNS; j++){
                saveString.append("\t");
                saveString.append(data[i][j]);
            }
            saveString.append(nextLine);
        }
        saveString.append(ProjSetting.LIST_TERMINATOR);
        return saveString.toString();
    }
    
    public int getLength(){
        return length; 
    }
    
    public String getLandRunoffName(int index){
        return " " + (String)data[index][0] + " Runoff";
    }
    
    
    public void setFromString(String input){
        
        System.err.println("Method setFromString Not Implemented Yet");
         // Not Implemented yet 
    }
    
    
    public void addRow(){
        
        System.err.println("Method addRow Not Implemented Yet");
        
    }
    
    public void deleteRow(int row){
        
        System.err.println("Method deleteRow Not Implemented Yet");
    }
    public void moveUp(int row){
        
        System.err.println("Method MoveDown Not Implemented Yet");
    }
    public void moveDown(int row){
        
        System.err.println("Method MoveUp Not Implemented Yet");
    }
}
