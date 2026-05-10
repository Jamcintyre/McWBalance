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
import com.mcwbalance.settings.Limit;
import com.mcwbalance.settings.Preferences;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.table.AbstractTableModel;

/**
 * For managing user entry of runoff coefficients
 * @author Alex McIntyre
 */
public class TableRunoffCoefficients extends AbstractTableModel {

    String[] columnNames = McWBalance.langRB.getString("RUNOFF_COEFFICIENTS_TABLE_HEADINGS").split(",");
    private static final int NUMBER_OF_COLUMNS = 13;
    private static final int MAX_LAND_COVERS = Limit.MAX_LAND_COVERS;
    private final Object[][] data;
    private final int length = 1;

    private static final double COEFF_MIN = 0;
    private static final double COEFF_MAX = 1;

    /**
     * Generates a table of runoff coefficients with space for the maximum
     * number of land covers
     */
    public TableRunoffCoefficients() {
        data = new Object[MAX_LAND_COVERS][NUMBER_OF_COLUMNS];
        data[0][0] = "Undisturbed";
        for (int i = 1; i < 13; i++) {
            data[0][i] = (double) 1;
        }
    }

    /**
     * used for implementing sorting and null value cleanup
     */
    public void cleanUp() {
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
    public double getCoefficient(String landCover, int month) {
        for (int i = 0; i < length; i++) {
            if (landCover.equals(data[i][0])) {
                return (double) data[i][month];
            }
        }
        return 0;
    }

    /**
     * for accessing data
     *
     * @param row to pull data from
     * @param col to pull data from
     * @return data[row][col]
     */
    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
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
        return data.length;
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
                    data[0][0] = "NONE";
                default ->
                    data[0][col] = (double) 0;
            }
        }
        switch (col) {
            case 0 ->
                data[row][0] = (String) value;
            default -> {
                if ((double) value < COEFF_MIN) {
                    data[row][col] = COEFF_MIN;
                } else if ((double) value > COEFF_MAX) {
                    data[row][col] = COEFF_MAX;
                } else {
                    data[row][col] = (double) value;
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

        cleanUp();

        saveString.append(columnNames[0]);
        for (int i = 1; i < columnNames.length; i++) {
            saveString.append("\t");
            saveString.append(columnNames[i]);

        }
        saveString.append(nextLine);

        for (int i = 0; i < length; i++) {
            saveString.append(data[i][0]);
            for (int j = 1; j < NUMBER_OF_COLUMNS; j++) {
                saveString.append("\t");
                saveString.append(data[i][j]);
            }
            saveString.append(nextLine);
        }
        saveString.append(Preferences.LIST_TERMINATOR);
        return saveString.toString();
    }

    /**
     * gets length of valid non null data
     *
     * @return lengh of data excluding nulls
     */
    public int getLength() {
        return length;
    }

    /**
     * for plotting purposes, adds runoff to end of name
     *
     * @param index
     * @return
     */
    public String getLandRunoffName(int index) {
        return " " + (String) data[index][0] + " Runoff";
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
     * not implemented
     */
    public void addRow() {

        System.err.println("Method addRow Not Implemented Yet");

    }

    /**
     * not implemented, intended for clearing a row
     *
     * @param row
     */
    public void deleteRow(int row) {

        System.err.println("Method deleteRow Not Implemented Yet");
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
