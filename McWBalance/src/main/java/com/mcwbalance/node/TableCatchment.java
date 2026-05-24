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

import com.mcwbalance.McWBalance;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.table.AbstractTableModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Table for managing staged catchment area input, will allow for separation 
 * of land covers
 * @author Alex McIntyre
 */
public class TableCatchment extends AbstractTableModel {

    // Columns, Timestep, Total area, LC1, Lc2, LC3, etc...
    ArrayList <String> columnNames; 
    String columnNameBasis;
    
    /**
     * DataRow is intended to allow variable table with of ints, with a fixed 
     * descriptor basis column
     */
    private class DataRow {
        ArrayList<Integer> values;
        String basis;
        DataRow(){
            this.values = new ArrayList<Integer>();
            this.basis = "";
        };
    };
    
    ArrayList<DataRow> data;

    /**
     * Generates a blank Catchment Area Table with 1 empty entry
     * todo, make second Row of table the Units
     */
    TableCatchment() {
        columnNames = new ArrayList<>();
        
        columnNames.add(McWBalance.langRB.getString("MODEL_STEP"));
        columnNames.add(McWBalance.langRB.getString("TOTAL_AREA"));
        columnNameBasis = McWBalance.langRB.getString("BASIS");
        
        data = new ArrayList<>();
        data.add(new DataRow());
        data.get(0).values.add(0);
        data.get(0).values.add(0);
    }
    /**
     * used for loading in an existing project or importing
     * @param catchXML element representing a catchment table
     */
    TableCatchment(Element catchXML){
        this();
        NodeList lcs = catchXML.getElementsByTagName("landcover");
        for (int c = 0; c < lcs.getLength(); c++){
            Element lc = (Element) lcs.item(c);
            this.addLandCover(lc.getAttribute("name"));
        }
        NodeList rows = catchXML.getElementsByTagName("row");
        for (int r = 0; r < rows.getLength(); r++){
            data.add(new DataRow());
            Element row = (Element) rows.item(r);
            data.get(r).values.add(Integer.valueOf(wdef(row.getAttribute("timestep"), "0")));
            data.get(r).values.add(Integer.valueOf(wdef(row.getAttribute("total"), "0")));
            for (int c = 2; c < columnNames.size(); c++){
                data.get(r).values.add(Integer.valueOf(wdef(row.getAttribute(columnNames.get(c)), "0")));
            }
            data.get(r).basis = wdef(row.getAttribute("basis"),"");
        }
    }
    
    /**
     * Adds a new column with array of 0's 
     * @param landcover to add, will not allow duplicates
     */
    public final void addLandCover(String landcover) {
        if (!columnNames.contains(landcover)) {
            columnNames.add(landcover);
            for (int i = 0; i < data.size(); i++) {
                data.get(i).values.add(0);
            }
        }
        this.fireTableStructureChanged(); //  structure change when # cols updates
    }
    /**
     * Used for adding a new blank row to the end of the table
     */
    public void addRow(){
        data.add(new DataRow());
        for (int c = 0; c < columnNames.size(); c++){
            data.getLast().values.add(0);
        }
        this.fireTableDataChanged();
    }

    /**
     * used to calculate totals
     * Fires Table Data Changed 
     */
    void calcTotal() {
        for (int i = 0; i < data.size(); i++) {
            int total = 0;
            for (int c = 2; c < data.get(i).values.size(); c++) {
                total += data.get(i).values.get(c);
            }
            data.get(i).values.set(1, total);
        }
        this.fireTableDataChanged();
    }
    
    /**
     * Deletes column without checking, except for column 0 and 1
     */
    private void deleteColumn(int col){
        if (col < 2 || col > columnNames.size()){
            return;
        }
        columnNames.remove(col);
        for (int row = 0; row < data.size(); ++row){
            data.get(row).values.remove(col);
        }
    }
    
    /**
     * removes all land covers with 0 area
     */
    public void deleteUnusedLandCovers(){
        for (int col = columnNames.size()-1; col > 1; --col){       
            if(this.getAreaMax(columnNames.get(col)) == 0){
                deleteColumn(col);
            }
        }
        this.fireTableStructureChanged();
    }

    /**
     * For removing a specific row from the table
     *
     * @param row to delete, note will not delete row 0
     */
    public void deleteRow(int row) {
        if (row < 1 || row > data.size()){
            return;            
        }
        data.remove(row);
        this.fireTableRowsDeleted(row, row);
    }

    /**
     * Returns the area given the a time step functions like VLookup, in that if
     * 5 is search for then values of 1 through 5 are accepted, but not 6
     *
     * @param timestep of model, variable units, i.e. can mean hours or days
     * @return 0 if time step is less then 0, otherwise returns the area
     */
    public int getTotalArea(int timestep) {
        if (timestep < 0) {
            return 0;
        }
        int res = data.get(0).values.get(1); // sets to the first result 
        for (int i = 0; i < data.size(); i++){
            if(data.get(i).values.get(0) > timestep){
                return res; 
            }
            res = data.get(i).values.get(1);
        }
        return res;
    }
    
    /**
     * Returns the area given the a time step functions like VLookup, in that if
     * 5 is search for then values of 1 through 5 are accepted, but not 6
     * 
     * @param timestep of model, variable units, i.e. can mean hours or days
     * @param landcover land cover to search for 
     * @return 0 if time step is less then 0, or land cover not found
     */
    public int getArea(int timestep, String landcover){
        if (timestep < 0 || !columnNames.contains(landcover)) {
            return 0;
        }
        int col = columnNames.indexOf(landcover);
        int res = data.get(0).values.get(col); // sets to the first result 
        for (int i = 0; i < data.size(); i++){
            if(data.get(i).values.get(0) > timestep){
                return res; 
            }
            res = data.get(i).values.get(col);
        }
        return res;
    }
    /**
     * returns the maximum area assigned to a given landcover
     * Intended to be used for usage check, i.e. if 0 then not used, 
     * @param landcover name of landcover; excluding first 2 columns
     * @return 0 if area is not used, or greatest amount of area used
     */
    public int getAreaMax(String landcover){
        
        if(!columnNames.contains(landcover)){
            return 0;
        }
        int area = 0;
        int col = columnNames.indexOf(landcover);
        if (col < 2){
            return 0;
        }
        
        for (int row = 0; row < data.size(); row++) {
            if (data.get(row).values.get(col) > area){
                area = data.get(row).values.get(col);
            }
        }
        return area;
    }

    /**
     * Bases the number of rows based on the number of array lists in data
     * @return 
     */
    @Override
    public int getRowCount() {
        return data.size();
    }

    /**
     * Bases the width of the table on the number of columns named plust the
     * basis column (+1)
     * @return 
     */
    @Override
    public int getColumnCount() {
        return columnNames.size()+1;
    }
    
    /**
     * for getting the header name of a column
     *
     * @param col to get header for
     * @return column name
     */
    @Override
    public String getColumnName(int col) {
        if(col < columnNames.size()){
            return columnNames.get(col);
        }
        return columnNameBasis;
    }

    /**
     * Pulls direct from Data Array List
     * @param row time step of data
     * @param col column of data
     * @return value in data.get(row).get(col);
     */
    @Override
    public Object getValueAt(int row, int col) {
        if(col < columnNames.size()){
            return data.get(row).values.get(col);
        }
        return data.get(row).basis;
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
        for (int c = 2; c < columnNames.size(); c++){
            Element lc = xMLDoc.createElement("landcover");
            lc.setAttribute("name", columnNames.get(c));
            ele.appendChild(lc);
        }
        
        
        for (int i = 0; i < data.size(); i++) {
            Element row = xMLDoc.createElement("row");
            row.setAttribute("timestep", String.valueOf(data.get(i).values.get(0)));
            row.setAttribute("total", String.valueOf(data.get(i).values.get(1)));
            for (int c = 0; c < columnNames.size(); c++){
                Element landcover = xMLDoc.createElement("landcover");
                landcover.setAttribute(columnNames.get(c), String.valueOf(data.get(i).values.get(c)));
                row.appendChild(landcover);
            }
            row.setAttribute("basis", data.get(i).basis);
            
            ele.appendChild(row);
        }
        return ele;
    }
    
    /**
     * For determining if a cell can be user edited row 0 col 0 is not user
     * editable as it must contain 0, all other cells are editable
     *
     * @param row to check
     * @param col to check
     * @return true unless row 0 col 0 or col = 1 (totals)
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return !(row == 0 && col == 0 || col == 1);
    }
    
    /**
     * Intended to be called as window is closed or on user demand 
     * Method calls duplicate and null removal,  sorts, calcs totals, and 
     * fires structure change, note that calcTotal will fire the data change. 
     */
    public void clean(){
        removeDoupsAndNulls();
        sortAssending();
        calcTotal();
    }

    /**
     * Removes any nulls, zeros and duplicate area indicies TODO - Not
     * Completed
     */
    private void removeDoupsAndNulls() {
        for (int r = 1; r < data.size(); r++) {
            if (data.get(r).values.get(0) == null || data.get(r).values.get(0) == 0) {
                data.remove(r);
                r = 0; // resets to first row (note r++ assumed to occur and r will = 1 next loop
            } else {
                for (int r2 = r + 1; r2 < data.size(); r2++) {
                    if (Objects.equals(data.get(r).values.get(0), data.get(r2).values.get(0))) {
                        data.remove(r2);
                        r2 = r + 1; // resets since removal of row can shift index up
                    }
                }
            }
        }
    }
    
 
  
    /**
     * for setting the value of a specific cell
     *
     * @param value source value, limited to 0 to MaxInt
     * @param row to set
     * @param col to set
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        if(col >= columnNames.size()){
            data.get(row).basis = String.valueOf(value);
        }else {
            int input = Integer.valueOf(value.toString());
            if (input < 0) {
                input = 0;
            } else if (input > Integer.MAX_VALUE) {
                input = Integer.MAX_VALUE;
            }
            data.get(row).values.set(col, input);
            fireTableCellUpdated(row, col);
            if (col > 1) {
                this.calcTotal();
            }
        }
        fireTableCellUpdated(row, 1);
    }
  
    /**
     * Only separated out because unsure of how comparator works, and will want
     * to test, i.e
     * 
     */
    private void sortAssending() {
        data.sort((a, b) -> a.values.get(0) - b.values.get(0));
    }

    /**
     * Simple utiity for ensuring default values are read in;
     *
     * @param string string to read
     * @param def string to use if string is ""
     * @return
     */
    private String wdef(String string, String def) {
        if (!"".equals(string)) {
            return string;
        }
        System.err.println("Possible Error - TableCatchment.Java - wdef() - Default " + def + " value was applied due to blank string");
        return def;
    }
    
    
}
