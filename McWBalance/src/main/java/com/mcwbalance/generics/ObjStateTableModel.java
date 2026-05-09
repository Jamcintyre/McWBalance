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
package com.mcwbalance.generics;

import com.mcwbalance.settings.Limit;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.table.AbstractTableModel;

/**
 * 2D table model, first column is int for days, second column is String.
 * Assumes ACTIVE as the default state
 *
 * @author Alex McIntyre
 */
public class ObjStateTableModel extends AbstractTableModel {

    /**
     * Column names for this model class are fixed as "Model Day" and "State"
     * modification of these names must be carried out within this class.
     */
    private final String[] columnNames = {"Model Day", "Object State"};

    /**
     * Data structure for this table model is of fixed size
     */
    private final Object[][] data = new Object[Limit.MAX_STATES][2];

    /**
     * Method for retrieving specific value
     *
     * @param row to retrieve value from
     * @param col to retrieve value from
     * @return value of data[row][col]
     */
    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /**
     * Method for retrieving number of columns
     *
     * @return number of columns
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Method for retreiving number of rows of data
     *
     * @return number of rows
     */
    @Override
    public int getRowCount() {
        return data.length;
    }

    /**
     * Method for retrieving column name
     *
     * @param col column to get name from
     * @return column name
     */
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * Method for getting column class
     *
     * @param col column to look up data type from
     * @return class of data in column col
     */
    @Override
    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    /**
     * Used to determine if data can be edited or not only row 0, col 0 is
     * fixed, i.e. must be 0 as we need a 0 time step all other data can be user
     * edited
     *
     * @param row to check
     * @param col to check
     * @return true if user is allowed to modify, false if not
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        if (row == 0 && col == 0) {
            return false;
            // Must have a value for Day 0.
        }
        return true; // sets all data to editable
    }

    /**
     * Used for setting a specific value and calling table update
     *
     * @param value source value
     * @param row of destination
     * @param col of destination
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    /**
     * Initializes and or resets the first row of data to 0 values.
     */
    public void setBlankFirstRow() {
        data[0][0] = (int) 0;
        data[0][1] = (String) "ACTIVE";
    }

    /**
     * Allows data currently in the clipboard to be pasted into the active table
     * view. The data is assumed to be cut or copied from MS Excel and is
     * expected to be Tab delimited.
     *
     * @param selectedrow Used to select the starting point for data pasting.
     * Note that Data pasted will not be limited to 1 row if multiple rows of
     * data are pasted
     * @param selectedcol Used to select the starting point for data pasting.
     * Note that Data pasted will not be limited to 1 column if multiple columns
     * of data are pasted
     */
    public void pasteFromClipboard(int[] selectedrow, int[] selectedcol) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable content = clipboard.getContents(this);
        if (content != null) {
            try {
                String clippedvalue = content.getTransferData(DataFlavor.stringFlavor).toString(); // string flavor seams easiest..  
                String clippedValueLines[] = clippedvalue.split("\\R"); // the \\R is cross platform for \n new line charactor
                String clippedValueSingleLine[]; // this is the final array of values, used for each line within a loop 

                for (int i = 0; i < clippedValueLines.length; i++) {
                    clippedValueSingleLine = clippedValueLines[i].split("\\t");
                    if (clippedValueSingleLine.length == 2) { // if three columns are present then will assume all 2 columns are to be overwritten;
                        setValueAt(Integer.valueOf(clippedValueSingleLine[0]), i + selectedrow[0], 0); // day column
                        setValueAt(clippedValueSingleLine[1], i + selectedrow[0], 1); // State column // will need to add an error check to default to Active if state not possible
                    }
                    if (clippedValueSingleLine.length == 1) { // if only 1 column present then will now need to find wich column
                        if (selectedcol[0] == 0) { // if its 0 it must be an int used for date
                            setValueAt(Integer.valueOf(clippedValueSingleLine[0]), i + selectedrow[0], 0);
                        } else if (selectedcol[0] == 1) { // if its 1 it must be a String used for rate
                            setValueAt(clippedValueSingleLine[0], i + selectedrow[0], 1);
                        }
                    }
                }
                fireTableDataChanged();
            } catch (UnsupportedFlavorException e) {
                System.err.println("Unsupported Data Type " + e.getLocalizedMessage());
            } catch (IOException e) {
                System.err.println("Data Consumed Exception " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Method responsible for data deletion from this class of table model. Data
     * deletion is carried out with basic rules to prevent null values resulting
     * in null class in row 0.
     *
     * @param selectedrow Indicates the rows that data will be removed / deleted
     * from
     * @param selectedcol Indicates the columns that data will be removed /
     * deleted from
     */
    public void removeData(int[] selectedrow, int[] selectedcol) { //allow deletion of data but first row must remain
        for (int i = 0; i < selectedrow.length; i++) {
            if (selectedrow[i] != 0) { // if isn't row 0 then run the normal sequence
                for (int j = 0; j < selectedcol.length; j++) {
                    data[selectedrow[i]][selectedcol[j]] = null;
                }
            } else if (selectedcol[0] == 0) { // confirms that elevation column is selected

                data[0][0] = 0; // only deletes the first elevation value,  Area and Vol maintained as 0. 
                if (selectedcol.length > 1) { // maximum columns for this data type is 2. 
                    data[0][1] = 0.0;
                }
            } else if (selectedcol[0] == 1) {
                data[0][1] = 0.0;
            }
            fireTableDataChanged();
        }
    }

}
