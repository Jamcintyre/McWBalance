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
package com.mcwbalance.node;

import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.settings.Limit;
import com.mcwbalance.settings.Preferences;
import com.mcwbalance.util.CalcBasics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.table.AbstractTableModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Table class used for storing and calculating deposition rates
 *
 * @author Alex
 */
public class TableTailingsDepositionRates extends AbstractTableModel {

    private static final String[] columnNames = {"Model Day", "Rate (tonnes/day)", "Solids Content (by mass)", "Specific Gravity", "Final Settled Density (tonne/cu.m)", "Water with Solids (cu.m./day)", "Voidloss (cu.m./day)", "Volume of Settled Solids (cu.m./day)"};
    private static final int NUMBER_OF_COLUMNS = 8;
    private final Object[][] data = new Object[Limit.MAX_DEPO_RATES][NUMBER_OF_COLUMNS];
    private static final int DAY_MAX = Limit.MAX_DURATION;
    private static final int DAY_MIN = 0;
    private static final int DAY_NULL = DAY_MAX + 1;
    private static final double RATE_MIN = 0;
    private static final double SOLIDS_CONT_MIN = 0;
    private static final double SOLIDS_CONT_MAX = 1;
    private static final double SG_MIN = 0;
    private static final double SG_MAX = 22;
    private static final double SETTLED_MIN = 0;
    private static final double SETTTLED_MAX = 22;

    /**
     * Maximum Length
     */
    public static final int MAX_LENGTH = 10;
    /**
     * Maximum number of rates
     */
    public static final int MAX_RATES = 20;

    private int length;

    ProjSetting projSetting;

    /**
     * generates a table used for storing deposition rate data data[0][0] =
     * (int)0;//"Model Day", data[0][1] = (double)0; //"Rate (Tonnes per Day)",
     * data[0][2] = (double)0; //"Solids Content (by mass)", data[0][3] =
     * (double)0; //"Specific Gravity", data[0][4] = (double)0; //"Final Settled
     * Density (dry tonne/cu.m)", data[0][5] = (double)0; //"Water with Solids
     * (cu.m./day)", data[0][6] = (double)0; //"Voidloss (cu.m./day)" data[0][7]
     * = (double)0; //"Volume of Settled Solids (cu.m./day)"
     *
     * @param projSetting
     */
    public TableTailingsDepositionRates(ProjSetting projSetting) {

        this.projSetting = projSetting;

        data[0][0] = (int) 0;//"Model Day", 
        data[0][1] = (double) 0; //"Rate (Tonnes per Day)",
        data[0][2] = (double) 0; //"Solids Content (by mass)",
        data[0][3] = (double) 0; //"Specific Gravity",
        data[0][4] = (double) 0; //"Final Settled Density (dry tonne/cu.m)",
        data[0][5] = (double) 0; //"Water with Solids (cu.m./day)", 
        data[0][6] = (double) 0; //"Voidloss (cu.m./day)"
        data[0][7] = (double) 0; //"Volume of Settled Solids (cu.m./day)"
        length = 1;
    }

    /**
     * Used for getting a save file formatted XML element to append into a
     * larger doc Only appends if length > 0
     *
     * @see getXMLElement
     * @param element Element to append too
     * @param xMLDoc
     * @param tagname
     */
    public void appendXMLElement(Element element, Document xMLDoc, String tagname) {
        if (length > 0) {
            element.appendChild(getXMLElement(xMLDoc, tagname));
        }
    }

    /**
     * For getting value from set location
     *
     * @param row to get value from
     * @param col to get value from
     * @return value at row and col
     */
    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /**
     * For display number of column headings
     *
     * @return number of column headings
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * for getting number of rows
     *
     * @return data.length
     */
    @Override
    public int getRowCount() {
        return data.length;
    }

    /**
     * For getting column name does not do input error handling
     *
     * @param col col to get name of
     * @return columnName
     */
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * for getting class of data in a column does not do input error handling
     *
     * @param col to get class from
     * @return class of data
     */
    @Override
    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    /**
     * initial rate must start at day 0 so first row col is not editable Values
     * after col 4 are calculated and not user editable
     *
     * @param row to check
     * @param col to check
     * @return true unless col > 4 or row and col == 0;
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        if (row == 0 && col == 0) {
            return false;
            // Must have a value for Day 0.
        } else if (col > 4) {
            return false; // will not be able to directly edit fields that must be calculated
        }
        return true; // sets all data to editable
    }

    /**
     * for setting value of a row and col
     *
     * @param value source value
     * @param row to set
     * @param col to set
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        if (value == null && row == 0) { // need to prevent user from nulling up the first row to avoid losing the class
            switch (col) {
                case 1:
                    data[0][0] = (int) 0;
                case 2:
                    data[0][1] = (double) 0;
                case 3:
                    data[0][2] = (double) 0;
                case 4:
                    data[0][3] = (double) 0;
                case 5:
                    data[0][4] = (double) 0;
            }
        } else if (col == 0) { // prevents negitive value in the day column
            if ((int) value < DAY_MIN) {
                data[row][col] = (int) 0;
            } else {
                data[row][col] = (int) value;
            }
        } else if (col == 1) { // prevents negitive rate
            if ((double) value < RATE_MIN) {
                data[row][col] = (double) 0;
            } else {
                data[row][col] = (double) value;
            }
        } else if (col == 2) { // cannot be less then 
            if ((double) value < SOLIDS_CONT_MIN) {
                data[row][col] = (double) SOLIDS_CONT_MIN;
            } else if ((double) value > SOLIDS_CONT_MAX) {
                data[row][col] = (double) SOLIDS_CONT_MAX; // cannot be more then 100% solids
            } else {
                data[row][col] = (double) value;
            }
        } else if (col == 3) { // prevents negitive SG, and prevents impossible SG
            if ((double) value < SG_MIN) {
                data[row][col] = (double) SG_MIN;
            } else if ((double) value > SG_MAX) {
                data[row][col] = (double) SG_MAX; // densist material on earth is 22g/cm3
            } else {
                data[row][col] = (double) value;
            }
        } else {
            data[row][col] = value;
        }
        calculateRow(row); // implements custom row calculator
        fireTableCellUpdated(row, col);
    }

    /**
     * Will take in Model Day, rate, solids, specific Gravity, Final Settled
     * Density
     *
     * @param lineIn Tab delimited data
     * @param row row number
     */
    public void setRowFromString(String lineIn, int row) {
        String columns[] = lineIn.split("\t");
        if (columns.length > 5) {
            setValueAt(Integer.valueOf(columns[0]), row, 0);
            setValueAt(Double.valueOf(columns[1]), row, 1);
            setValueAt(Double.valueOf(columns[2]), row, 2);
            setValueAt(Double.valueOf(columns[3]), row, 3);
            setValueAt(Double.valueOf(columns[4]), row, 4);
            calculateRow(row);
        }

    }

    /**
     * Used to calculate out the row once all data exists
     *
     * @param row row to calculate, only runs if sufficient data available
     */
    public void calculateRow(int row) {
        if (data[row][1] != null && data[row][2] != null && data[row][3] != null && data[row][4] != null) { // check first to ensure no nulls
            if ((double) data[row][1] > 0 && (double) data[row][2] > 0 && (double) data[row][3] > 0 && (double) data[row][4] > 0) { // checks to ensure no zeros or negatives
                // if the info is good then

                double rate = (double) data[row][1];
                double sCont = (double) data[row][2];
                double sG = (double) data[row][3];
                double sDens = (double) data[row][4];

                double wWSolids = (rate / sCont) * (1 - sCont); //"Water with Solids (cu.m./day)", 
                double vSSolidsandVoids = rate / sDens;  //"Volume of Settled Solids (cu.m./day)"
                double vSSolidsperM = sDens / sG;
                //double voidLoss = vSSolids*(1-rate/sG); //"Voidloss (cu.m./day)"
                double voidLoss = (1 - vSSolidsperM) * rate / sDens;

                CalcBasics.roundDbl(wWSolids, projSetting.getPrecisionDay()); // rounds off value based on project setting
                data[row][5] = wWSolids;

                CalcBasics.roundDbl(voidLoss, projSetting.getPrecisionDay()); // rounds off value based on project setting
                data[row][6] = voidLoss;

                CalcBasics.roundDbl(vSSolidsandVoids, projSetting.getPrecisionDay()); // rounds off value based on project setting
                data[row][7] = vSSolidsandVoids;
                fireTableDataChanged();
            } else {
                data[row][5] = (double) 0;
                data[row][6] = (double) 0;
                data[row][7] = (double) 0;
            }
        } else {
            data[row][5] = (double) 0;
            data[row][6] = (double) 0;
            data[row][7] = (double) 0;
        }
        sortAssending();

    }

    /**
     * not fully implemented yet, should utilize the set row from string method
     *
     * @see setRowFromString
     * @param selectedrow
     * @param selectedcol
     */
    public void pasteFromClipboard(int[] selectedrow, int[] selectedcol) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable content = clipboard.getContents(this);
        if (content != null) {
            try {
                String clippedValue = content.getTransferData(DataFlavor.stringFlavor).toString(); // string flavor seams easiest..  
                String clippedValueLines[] = clippedValue.split(System.getProperty("line.separator")); // the \\R is cross platform for \n new line charactor
                String clippedValueSingleLine[]; // this is the final array of values, used for each line within a loop 

                int startCol;
                int endCol;

                clippedValueSingleLine = clippedValueLines[0].split("\t");
                if (columnNames[0].equals(clippedValueSingleLine[0])) {
                    setFromString(clippedValue);
                    return;
                }
                if (clippedValueSingleLine.length < 5) {
                    startCol = selectedcol[0];
                    endCol = selectedcol[selectedcol.length - 1];
                    if (endCol > 4) {
                        endCol = 4;
                    }
                } else {
                    startCol = 0;
                    endCol = 4;
                }
                for (int j = 0; j < startCol - endCol; j++) {
                    if (j + startCol == 0) {
                        setValueAt(Integer.valueOf(clippedValueSingleLine[0]), 0, 0);
                    } else {
                        setValueAt(Double.valueOf(clippedValueSingleLine[j]), 0, j + startCol);
                    }

                }

                for (int i = 1; i < clippedValueLines.length; i++) {
                    clippedValueSingleLine = clippedValueLines[i].split("\t");
                    for (int j = 0; j < startCol - endCol; j++) {
                        if (j + startCol == 0) {
                            setValueAt(Integer.valueOf(clippedValueSingleLine[0]), i, 0);
                        } else {
                            setValueAt(Double.valueOf(clippedValueSingleLine[j]), i, j + startCol);

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
     * Allows deletion of data but first row must remain
     *
     * @param selectedrow rows to delete
     * @param selectedcol cols to delete
     */
    public void removeData(int[] selectedrow, int[] selectedcol) {
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

    /**
     * Rate tonnes/day data[i][1] Min = 0
     *
     * @param day time step to lookup
     * @return data[i][1] Min = 0
     */
    public double getRate(int day) {
        for (int i = 0; i < getRowCount(); i++) {
            if ((int) data[i][0] == day) {
                return (double) data[i][1];
            }
            if ((int) data[i][0] > day && i > 0) {
                return (double) data[i - 1][1];
            }
        }
        return (double) data[0][1];
    }

    /**
     * Solids Content (by mass) data[i][2] Max = 1.00, Min = 0
     *
     * @param day time step to lookup
     * @return data[i][2] Max = 1.00, Min = 0
     */
    public double getSolidsCont(int day) {
        for (int i = 0; i < getRowCount(); i++) {
            if ((int) data[i][0] == day) {
                return (double) data[i][2];
            }
            if ((int) data[i][0] > day && i > 0) {
                return (double) data[i - 1][2];
            }
        }
        return (double) data[0][2];
    }

    /**
     * Specific gravity data[i][3], between 0 and 22 weight of osmium data[i][3]
     *
     * @param day time step to lookup
     * @return data[i][3],
     */
    public double getSG(int day) {
        for (int i = 0; i < getRowCount(); i++) {
            if ((int) data[i][0] == day) {
                return (double) data[i][3];
            }
            if ((int) data[i][0] > day && i > 0) {
                return (double) data[i - 1][3];
            }
        }
        return (double) data[0][3];
    }

    /**
     * final dry settled density in tonnes / m3 data[i][4] Min = 0, Max = 22
     * tonnes/m3 (solids mass of osmium
     *
     * @param day time step to lookup
     * @return data [i][4]
     */
    public double getSettledDensity(int day) {
        for (int i = 0; i < getRowCount(); i++) {
            if ((int) data[i][0] == day) {
                return (double) data[i][4];
            }
            if ((int) data[i][0] > day && i > 0) {
                return (double) data[i - 1][4];
            }
        }
        return (double) data[0][4];
    }

    /**
     * Volume of water with the solids out of pipe (cu.m. per day) data [i][5]
     *
     * @param day time step to lookup
     * @return data [i][5]
     */
    public double getWaterWithSolids(int day) {
        for (int i = 0; i < getRowCount(); i++) {
            if ((int) data[i][0] == day) {
                return (double) data[i][5];
            }
            if ((int) data[i][0] > day && i > 0) {
                return (double) data[i - 1][5];
            }
        }
        return (double) data[0][5];
    }

    /**
     * Volume of voids per day (cu.m. per day) data [i][6];
     *
     * @param day time step to lookup
     * @return data [i][6]
     */
    public double getVolVoids(int day) {
        for (int i = 0; i < getRowCount(); i++) {
            if ((int) data[i][0] == day) {
                return (double) data[i][6];
            }
            if ((int) data[i][0] > day && i > 0) {
                return (double) data[i - 1][6];
            }
        }
        return (double) data[0][6];
    }

    /**
     * Volume of Solids and Voids;
     *
     * @param day time step to lookup
     * @return data [i][6]
     */
    public double getVolSolids(int day) {
        for (int i = 0; i < getRowCount(); i++) {
            if ((int) data[i][0] == day) {
                return (double) data[i][7];
            }
            if ((int) data[i][0] > day && i > 0) {
                return (double) data[i - 1][7];
            }
        }
        return (double) data[0][7];
    }

    /**
     * sorts list by day, note that methods require days to be in order short
     * clear nulls before sortAssending
     */
    private void sortAssending() {
        Object swap[] = new Object[NUMBER_OF_COLUMNS];
        for (int i = 0; i < getRowCount(); i++) { // may need to move the i++ and j++, want to repeat the step if needed;
            for (int j = i + 1; j < getRowCount(); j++) {
                if (data[j][0] == null || data[i][0] == null) {
                    break;
                } else if ((int) data[j][0] < (int) data[i][0]) { // if it matches leave it alone, the doublicate cleaner will deal with it; 
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

    /**
     * Nulls incomplete rows and shifts all completed rows to top
     */
    private void clearNulls() {
        //first loop ensures if any blanks are present the entire row is nulled out
        for (int i = 1; i < getRowCount(); i++) {
            if (data[i][0] == null || (int) data[i][0] < DAY_MIN || data[i][1] == null || data[i][2] == null || data[i][3] == null || data[i][4] == null) {
                data[i] = new Object[NUMBER_OF_COLUMNS];
            }
        }
        for (int i = getRowCount() - 1; i > 0; i--) {
            if (data[i][0] != null && data[i - 1][0] == null) {
                for (int j = i; j < getRowCount(); j++) {
                    data[j - 1] = data[j];
                }
            }
        }
    }

    /**
     * Removes douplicants from list and shifts up, note data should be sorted
     * assending and cleared of partial rows prior to using this method
     *
     * @see clearNulls
     * @see sortAssending
     */
    private void clearDouplicants() {
        for (int i = 1; i < getRowCount(); i++) {
            if (data[i][0] == null) {
                return;
            } else if ((int) data[i][0] == (int) data[i - 1][0]) {
                for (int j = i; j < getRowCount(); j++) {
                    data[j - 1] = data[j];
                }
                data[getRowCount() - 1] = new Object[NUMBER_OF_COLUMNS];
            }
        }
    }

    private void updateLength() {
        length = 1;
        for (int i = 0; i < getRowCount(); i++) {
            if (data[i][0] == null) {
                length = i;
                return;
            }
        }
        length = getRowCount();
    }

    /**
     * Method to call cleanup methods in correct order Clears all null and
     * partial lines sorts assending removes douplicated days updates the length
     * value
     */
    public void cleanUp() {
        clearNulls();
        sortAssending();
        clearDouplicants();
        updateLength();
        fireTableDataChanged();
    }

    /**
     * Converts data into a tab delimited string list suitable for a save file
     *
     * @return tab delimited data array with headers
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
     * Used for getting a save file formatted XML element to append into a
     * larger doc
     *
     * @param xMLDoc Document required to generate element,
     * @param tagname Name of element
     * @return
     */
    public Element getXMLElement(Document xMLDoc, String tagname) {
        Element ele = xMLDoc.createElement(tagname);
        for (int i = 0; i < length; i++) {
            Element cele = xMLDoc.createElement("row");
            cele.setAttribute("Day", String.valueOf(data[i][0]));
            cele.setAttribute("Rate", String.valueOf(data[i][1]));
            cele.setAttribute("SolContent", String.valueOf(data[i][2]));
            cele.setAttribute("SG", String.valueOf(data[i][3]));
            cele.setAttribute("FSetDen", String.valueOf(data[i][4]));
            cele.setAttribute("WatwSol", String.valueOf(data[i][5]));
            cele.setAttribute("VoidLoss", String.valueOf(data[i][6]));
            cele.setAttribute("VolSetSol", String.valueOf(data[i][7]));
            ele.appendChild(cele);
        }
        return ele;
    }

    /**
     * Not Implemented intended for pasting from clipboard
     *
     * @param input
     */
    public void setFromString(String input) {
        // Not Implemented yet 
    }

    /**
     * Copies data to clipboard in string format
     */
    public void copyToClipBoard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection content = new StringSelection(toString());
        clipboard.setContents(content, content);

    }

}
