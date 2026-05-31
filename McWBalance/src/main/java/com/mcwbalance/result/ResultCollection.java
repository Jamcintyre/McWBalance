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
package com.mcwbalance.result;

import com.mcwbalance.measure.Time;
import com.mcwbalance.measure.Unit;
import com.mcwbalance.measure.Volume;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Used for binning a collection of results and constructing viewers
 * @author alex
 */
public class ResultCollection extends AbstractTableModel{
    
    private ArrayList <Result> results;
    private ResultPlot viewPlot;
    private Time.TimeUnit timeUnit;
    private Unit resultUnit;
    private String timeColumnname;
    
    /**
     * In Progress,
     */
    public ResultCollection(){
        
        timeUnit = Time.TimeUnit.Day; // DEBUG PLACEHOLDER
        resultUnit = Volume.VolumeUnit.m3; // DEBUG PLACEHOLDER
        results = new ArrayList();
        
        viewPlot = new ResultPlot(this); 
        
        
        //Debug Result
        results.add(new Result("Debug Test",10,timeUnit,resultUnit,true));
        
        
    }
  
    /**
     * for adding to the collection
     *
     * @param result to add to collection
     */
    public void add(Result result) {
        results.add(result);
    }

    /**
     * for removing from collection
     *
     * @param name of result to remove, must match the .getName() value of the
     * result will remove all instances, no support for results of matching
     * names
     */
    public void remove(String name) {
        results.removeIf(r -> r.getName().equals(name));
    }
    
    /**
     * For use as JTable
     * 
     * @return Number records in result including time step 0 (initial conditions)
     */
    @Override
    public int getRowCount() {
        
        return 4; //DEBUG PLACEHOLDER
        //if (results == null || results.isEmpty()){
        //    return 0;
        //}
        //return results.getFirst().size(timeUnit);
        
    }

    /**
     * For use as JTable
     * @return Number of Columns including Time Step
     */
    @Override
    public int getColumnCount() {
        
        return 7; //DEBUG PLACEHOLDER
        //return results.size() + 1;
        
    }

    /**
     * For use in JTable
     * @param row time step at current time unit
     * @param col 0 = time step, 1 onward are for result values
     * @return 
     */
    @Override
    public Object getValueAt(int row, int col) {
        
        return 5;
        
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    /**
     * For getting access to plot view of the contained data
     * @return 
     */
    public ResultPlot getViewPlot(){
        return viewPlot;
    }
    
    /**
     * For returning current timeUnit
     * @return currently set time unit
     */
    Time.TimeUnit getTimeUnit(){
        return timeUnit;
    }
    
    /**
     * for getting the current units of measure for the result 
     * does not include time, and will need to be appended to time unit
     * @return current units of the result
     */
    Unit getResultUnit(){
        return resultUnit;
    }
    /**
     * For generating an Y axis lable
     * @return 
     */
    String getResultDescriptor(){
        return "Placeholder" + " (" + resultUnit.getDesciptor() +"/"+ timeUnit.getDesciptor() +")";
    }
    
    
}
