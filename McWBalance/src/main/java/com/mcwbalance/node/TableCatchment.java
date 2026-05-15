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
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Alex McIntyre
 */
public class TableCatchment extends AbstractTableModel {

    // Columns, Timestep, Total area, LC1, Lc2, LC3, etc...
    
    
    ArrayList <String> columnNames; 
    
    ArrayList<ArrayList<Integer>> data = new ArrayList<>();
    int nPoints;
    int[] time;
    int[] area;
    String LandCover;

    /**
     * Generates a blank Catchment Area Table with 1 empty entry
     */
    TableCatchment() {
        columnNames = new ArrayList<>();
        
        columnNames.add(McWBalance.langRB.getString("MODEL_STEP"));
        columnNames.add(McWBalance.langRB.getString("TOTAL_AREA"));
        
        data = new ArrayList<>();
        data.add(new ArrayList<Integer>()); 
        
        time = new int[nPoints];
        area = new int[nPoints];
        LandCover = "none";
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
        int res = data.get(0).get(2); // sets to the first result 
        for (int i = 0; i < data.size(); i++){
            if(data.get(i).get(1) > timestep){
                return res; 
            }
            res = data.get(i).get(2);
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
        int res = data.get(0).get(col); // sets to the first result 
        for (int i = 0; i < data.size(); i++){
            if(data.get(i).get(1) > timestep){
                return res; 
            }
            res = data.get(i).get(col);
        }
        return res;
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
     * Bases the width of the table on the number of columns named
     * @return 
     */
    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * Pulls direct from Data Array List
     * @param row time step of data
     * @param col column of data
     * @return value in data.get(row).get(col);
     */
    @Override
    public Object getValueAt(int row, int col) {
        return data.get(row).get(col);
    }

}
