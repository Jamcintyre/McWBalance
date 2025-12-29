/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.solve;

import com.mcwbalance.McWBalance;
import com.mcwbalance.flowchart.FlowChartCAD;
import com.mcwbalance.transfer.TRNList;
import com.mcwbalance.node.NodList;
import com.mcwbalance.settings.Limit;
import javax.swing.table.AbstractTableModel;

/**
 * Supersedes Balance Run Settings
 * @author Alex
 */
public class SolveOrder extends AbstractTableModel{
    
    public int[] tRNIndex; //index for whichever TRN or ELM is being solved
    public String[] tRNName;
    public String[] tRNType;
    public String[] columnNames;
    FlowChartCAD flowchart;
    
    
    public SolveOrder(FlowChartCAD flowchart) {
        this.flowchart = flowchart;
        columnNames = McWBalance.langRB.getString("SOLVE_ORDER_TABLE_HEADINGS").split(",");
        
        tRNIndex = new int[1];
        tRNIndex[0] = -1;
        
        tRNType = new String[1];
        tRNType[0] = "Not Initialized";
    }
    
    
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0 -> {
                return row + 1;
            }
            case 1 -> {
                return tRNIndex[row];
            }
            case 2 -> {
                if (tRNIndex[row] >=0){

                    return flowchart.getTRNList().tRNs[tRNIndex[row]].objname;
                }
                else {
                    return "None";
                }
            }
            case 3 -> {
                return tRNType[row];
            }
        }
        return 0;
    }
    @Override
    public int getColumnCount(){
        return 4;
    }
    @Override 
    public String getColumnName(int col){
        return columnNames[col];
    }
    @Override
    public int getRowCount(){
        return tRNIndex.length;
    }
    @Override 
    public Class getColumnClass(int col){        
        switch (col) {
            case 0 -> {
                return Integer.class;
            }
            case 1 -> {
                return Integer.class;
            }
            default -> {
                return String.class;
            }
        }
    }
    @Override 
    public boolean isCellEditable(int rowIndex, int columnIndex){
        return false; // sets all data to editable
    }

    
    public void moveUp(int row){
        if (row != 0){
            int btRNIndex = tRNIndex[row - 1]; 
            String btRNType = tRNType[row - 1];
            tRNIndex[row - 1] = tRNIndex[row];
            tRNType[row - 1] = tRNType[row];
            tRNIndex[row] = btRNIndex;
            tRNType[row] = btRNType;
        }
        fireTableRowsUpdated(row-1, row);
    }
    
    public void moveDown(int row){
        if (row != tRNIndex.length - 1 && row >= 0){
            int btRNIndex = tRNIndex[row + 1]; 
            String btRNType = tRNType[row + 1];
            tRNIndex[row + 1] = tRNIndex[row];
            tRNType[row + 1] = tRNType[row];
            tRNIndex[row] = btRNIndex;
            tRNType[row] = btRNType;
        }
        fireTableRowsUpdated(row, +1);
    }
    
    /**
     * Pulled from Balance Run Settings, takes an initial pass at determining solve order
     * @param tRNList
     * @param eLMList 
     */
    public void setAutoOrder(TRNList tRNList, NodList eLMList){
        int initSolveIndex[] = new int[Limit.MAX_TRNS];
        String initSolveType[] = new String[Limit.MAX_TRNS]; 
        int c = 0;
        int tr, el;
        
        boolean tRNCounted[] = new boolean[tRNList.count];
        for (int i = 0; i < tRNCounted.length; i++){
            tRNCounted[i] = false;
        }
        boolean eLMCounted[] = new boolean[eLMList.count];
        for (int i = 0; i < eLMCounted.length; i++){
            eLMCounted[i] = false;
        }
        // first pass sets any fixed flow rates
        for (int i = 0; i < eLMList.count; i ++){
            if(eLMList.nodes[i].tailsTRN != -1){ // sets any tailings deposition to first solve order as it will be fixed
                tr = eLMList.nodes[i].tailsTRN;
                initSolveIndex[c] = tr;
                initSolveType[c] = "SOLIDS";
                tRNCounted[tr] = true;
                c ++;
            }
            for(int j = 0; j < eLMList.nodes[i].inflowFixedTRN.count; j++){ //loops through the fixed inflows
                // need to check if it has no output or is not a demand then can solve
                tr = eLMList.nodes[i].inflowFixedTRN.getObjIndex(j);
                if (!tRNCounted[tr]) {
                    el = tRNList.tRNs[tr].outObjNumber;
                    if (el == -1 || eLMList.nodes[el].outflowFixedTRN.getListIndex(tr) != -1) {
                        initSolveIndex[c] = tr;
                        initSolveType[c] = "FIXED";
                        tRNCounted[tr] = true;
                        c++;
                    }
                }
            }
            for(int j = 0; j < eLMList.nodes[i].outflowFixedTRN.count; j++){ //loops through the fixed outflows
                // need to check if it has no output or is not a demand then can solve
                tr = eLMList.nodes[i].outflowFixedTRN.getObjIndex(j);
                if (!tRNCounted[tr]) {
                    el = tRNList.tRNs[tr].outObjNumber;
                    if (el == -1 || eLMList.nodes[el].inflowFixedTRN.getListIndex(tr) != -1) {
                        initSolveIndex[c] = tr;
                        initSolveType[c] = "FIXED";
                        tRNCounted[tr] = true;
                        c++;
                    }
                }
            }
        }
        // Second Pass for the on demand rates
        for (int i = 0; i < eLMList.count; i++) {
            for (int j = 0; j < eLMList.nodes[i].inflowOnDemandTRN.count; j++) { //loops through the fixed inflows
                // need to check if it has no output or is not a demand then can solve
                tr = eLMList.nodes[i].inflowOnDemandTRN.getObjIndex(j);
                if (!tRNCounted[tr]) {
                    initSolveIndex[c] = tr;
                    initSolveType[c] = "ON_DEMAND";
                    tRNCounted[tr] = true;
                    c++;
                }
            }
            for (int j = 0; j < eLMList.nodes[i].outflowOnDemandTRN.count; j++) { //loops through the fixed outflows
                // need to check if it has no output or is not a demand then can solve
                tr = eLMList.nodes[i].outflowOnDemandTRN.getObjIndex(j);
                if (!tRNCounted[tr]) {
                    initSolveIndex[c] = tr;
                    initSolveType[c] = "ON_DEMAND";
                    tRNCounted[tr] = true;
                    c++;

                }
            }
            tr = eLMList.nodes[i].overflowTRN;
            if(tr != -1 && !tRNCounted[tr]){
                initSolveIndex[c] = tr;
                initSolveType[c] = "ON_DEMAND";
                tRNCounted[tr] = true;
                c++;
            }
            
        }
        
        tRNIndex = new int[c];
        tRNType = new String[c];
        for (int i = 0; i < c; i++){
            tRNIndex[i] = initSolveIndex[i];
            tRNType[i] = initSolveType[i];
        }
        fireTableDataChanged();
    }


}
