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
package com.mcwbalance.landcover;

import com.mcwbalance.McWBalance;
import com.mcwbalance.node.NodList;
import com.mcwbalance.settings.Preferences;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * For managing user entry of runoff coefficients
 * @author Alex McIntyre
 */
public class TableRunoffCoefficients extends AbstractTableModel {

    String[] columnNames;
    
    /**
     * DataRow is intended to allow variable table with of ints, with a fixed
     * descriptor basis column
     */
    private class DataRow {

        float rc[];
        String name;
        String basis;
        DataRow(String name){
            rc = new float[12];
            this.name = name;
            this.basis = "";
        };
    };
    
    ArrayList<DataRow> data;
    
    /**
     * Requires a node list to check against to prevent deletion of in use land 
     * cover names
     */
    private NodList activeNodeList;
    
    
    private final int length = 1;

    private static final float COEFF_MIN = 0;
    private static final float COEFF_MAX = 1;

    /**
     * Generates a table of runoff coefficients with space for the maximum
     * number of land covers
     * @param nodeList required for checking usage of a land cover
     */
    public TableRunoffCoefficients(NodList nodeList) {
        
        this.activeNodeList = nodeList;
        
        columnNames = new String[14];
        columnNames[0] = McWBalance.langRB.getString("LAND_COVER");
        columnNames[1] = McWBalance.langRB.getString("JAN");
        columnNames[2] = McWBalance.langRB.getString("FEB");
        columnNames[3] = McWBalance.langRB.getString("MAR");
        columnNames[4] = McWBalance.langRB.getString("APR");
        columnNames[5] = McWBalance.langRB.getString("MAY");
        columnNames[6] = McWBalance.langRB.getString("JUN");
        columnNames[7] = McWBalance.langRB.getString("JUL");
        columnNames[8] = McWBalance.langRB.getString("AUG");
        columnNames[9] = McWBalance.langRB.getString("SEP");
        columnNames[10] = McWBalance.langRB.getString("OCT");
        columnNames[11] = McWBalance.langRB.getString("NOV");
        columnNames[12] = McWBalance.langRB.getString("DEC");
        columnNames[13] = McWBalance.langRB.getString("BASIS");
        
        data = new ArrayList<>();
        data.add(new DataRow("Undisturbed"));
        
    }

    /**
     * Copies data to clipboard in string format
     */
    public void copyToClipBoard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection content = new StringSelection(toString());
        clipboard.setContents(content, content);

    }

    /**
     * Used to select runoff coefficient from data table
     *
     * @param landCover Sting name of land cover must match a cover in this
     * table
     * @param month Jan = 1, Feb = 2, etc...
     * @return value generally from 0.00 to 1.00 representing the runoff
     * coefficient
     */
    public float getCoefficient(String landCover, int month) {
        if(month < 1 || month > 12){
            return 0;
        }
        for (int i = 0; i < length; i++) {
            if (landCover.equals(data.get(i).name)) {
                return  data.get(i).rc[month+1];
            }
        }
        return 0;
    }

    /**
     * for accessing data
     *
     * @param row to pull data from
     * @param col to pull data from
     * @return name if col = 0, basis if col = 13, rc if col 1 to 12
     */
    @Override
    public Object getValueAt(int row, int col) {
        if(col == 0){
            return data.get(row).name;
        }
        if(col == 13){
            return data.get(row).basis;
        }
        return data.get(row).rc[col-1];
    }

    /**
     * for getting number of columns
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * for getting number of data rows available includes null data
     *
     * @return includes null data rows
     */
    @Override
    public int getRowCount() {
        return data.size();
    }

    /**
     * Name of a column
     *
     * @param col to get name of
     * @return string value of column name
     */
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * For getting class value of column
     *
     * @param col
     * @return
     */
    @Override
    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    /**
     * All data can be edited
     *
     * @param row to check
     * @param col to check
     * @return true in all cases
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    /**
     * For setting data in a specific cell, enforces limits on value and auto
     * corrects
     *
     * @param value between 0 and 1
     * @param row if 0 rules will apply to prevent nulling
     * @param col column to edit
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        if (value == null && row == 0) { // need to prevent user from nulling up the first row to avoid losing the class
            switch (col) {
                case 0 ->
                    data.get(row).name = "NONE";
                case 13 ->
                    data.get(row).basis = "";
                default ->
                    data.get(row).rc[col-1] = (float) 0;
            }
        }
        switch (col) {
            case 0 ->
                data.get(row).name = (String) value;
            case 13 ->
                data.get(row).basis = (String) value;
            default -> {
                if ((float) value < COEFF_MIN) {
                    data.get(row).rc[col-1] = COEFF_MIN;
                } else if ((float) value > COEFF_MAX) {
                    data.get(row).rc[col-1] = COEFF_MAX;
                } else {
                    data.get(row).rc[col-1] = (float) value;
                }
            }
        }
        fireTableCellUpdated(row, col);
    }

    /**
     * for generating a tab delimited string useful for cut and paste to excel
     *
     * @return
     */
    @Override
    public String toString() {
        String nextLine = System.getProperty("line.separator");
        StringBuilder saveString = new StringBuilder();

        saveString.append(columnNames[0]);
        for (int i = 1; i < columnNames.length; i++) {
            saveString.append("\t");
            saveString.append(columnNames[i]);

        }
        saveString.append(nextLine);
        for (int i = 0; i < length; i++) {
            saveString.append(data.get(i).name);
            for (int j = 1; j < data.get(i).rc.length; j++) {
                saveString.append("\t");
                saveString.append(data.get(i).rc[j]);
            }
            saveString.append("\t");
            saveString.append(data.get(i).basis);
            saveString.append(nextLine);
        }
        saveString.append(Preferences.LIST_TERMINATOR);
        return saveString.toString();
    }

    /**
     * for plotting purposes, adds runoff to end of name
     *
     * @param row of land cover table
     * @return name of land cover at row
     */
    public String getLandRunoffName(int row) {
        return " " + (String) data.get(row).name + " Runoff";
    }

    /**
     * Not implemented intended for pasting of data
     *
     * @param input
     */
    public void setFromString(String input) {

        System.err.println("Method setFromString Not Implemented Yet");
        // Not Implemented yet 
    }

    /**
     * adds in a new blank land cover row with a unique name
     */
    public void addRow() {
        String name = McWBalance.langRB.getString("NEW_LAND_COVER");
        
        if (containsLandCover(name) != -1) {
            int c = 1;
            String newName = name + "(" + c + ")";
            while (containsLandCover(newName) != -1 && c < 1000) {
                newName = name + "(" + c + ")";
                c++;
            }
            name = newName;
        }
        data.add(new DataRow(name));
        this.fireTableDataChanged();
    }

    /**
     * Used for checking douplicate land cover names
     * @param name
     * @return -1 if no match, or row # of match if found
     */
    private int containsLandCover(String name){
        for (int r = 0; r < data.size(); r++){
            if (data.get(r).name.equals(name)){
                return r;
            }
        }
        return -1;
    }
    /**
     * Will delete all but the last row, one land cover must remain
     * also runs a check on land cover usage and prevents deletion of in use 
     * land cover
     *
     * @param row
     */
    public void deleteRow(int row) {
        if (data.size() < 2 || row < 0 || row >= data.size()){
            return;
        }
        
        
        
        if(activeNodeList.getLandCoverUsed(data.get(row).name) != null){
            return;
        }
        
        data.remove(row);
        this.fireTableRowsDeleted(row, row);
    }

    /**
     * not implemented intended for re-ordering
     *
     * @param row
     */
    public void moveUp(int row) {

        System.err.println("Method MoveDown Not Implemented Yet");
    }

    /**
     * not implemented intended for re-ordering should stop if already the last
     * valid row
     *
     * @param row
     */
    public void moveDown(int row) {

        System.err.println("Method MoveUp Not Implemented Yet");
    }
}
