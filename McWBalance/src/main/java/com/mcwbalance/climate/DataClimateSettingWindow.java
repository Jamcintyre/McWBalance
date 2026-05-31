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
package com.mcwbalance.climate;

import com.mcwbalance.McWBalance;
import com.mcwbalance.project.Project;
import com.mcwbalance.result.ResultViewerWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Need to debug why table does not appear
 * @author Alex McIntyre
 */
public class DataClimateSettingWindow extends JFrame{
    private final int TABLE_FIRST_COL_WIDTH = 50;
    private final int TABLE_SECOND_COL_WIDTH = 150;
    private final int TABLE_OTHER_COL_WIDTH = 80;
    private final int TABLE_ROW_HEIGHT = 20;
    private final Dimension TABLE_PREF_DIMENSION = new Dimension(TABLE_FIRST_COL_WIDTH+TABLE_SECOND_COL_WIDTH+(ClimateTable.columnNames.length-1)*TABLE_OTHER_COL_WIDTH,TABLE_ROW_HEIGHT*5);
    
    
    /**
     * Calls in a window for managing climate data sets
     * @param owner
     * @param aP 
     */
    public DataClimateSettingWindow(JFrame owner, Project aP){
        
        super(McWBalance.langRB.getString("CLIMATE_SCENARIOS"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JTable table = new JTable(aP.climateTable);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(TABLE_FIRST_COL_WIDTH);
        table.getColumnModel().getColumn(1).setPreferredWidth(TABLE_SECOND_COL_WIDTH);
        for (int i = 2; i < ClimateTable.columnNames.length; i ++){
            table.getColumnModel().getColumn(i).setPreferredWidth(TABLE_OTHER_COL_WIDTH);
        }
        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setPreferredScrollableViewportSize(TABLE_PREF_DIMENSION);
        
        
        JButton addClimate = new JButton(McWBalance.langRB.getString("ADD_CLIMATE_SCENARIO"));
        addClimate.addActionListener(l ->{
            DataClimateImportWindow importWindow = new DataClimateImportWindow(this, aP);   
            if(importWindow.getStatus() == DataClimateImportWindow.FILE_OBTAINED){
                aP.climateTable.addClimateScenario(importWindow.getString());
            }
        });
        JButton removeClimate = new JButton(McWBalance.langRB.getString("DELETE_CLIMATE_SCENARIO"));
        removeClimate.addActionListener(l ->{
            aP.climateTable.removeRow(table.getSelectedRow());
        });
        JButton viewData = new JButton(McWBalance.langRB.getString("VIEW_DATA"));
        viewData.addActionListener(l ->{
            int scenario = table.getSelectedRow();
            if (scenario >= 0
                    && scenario < aP.climateTable.climates.length
                    && !aP.climateTable.climates[scenario].equals(DataClimate.NULL_DESCRIP.toString())) {

                float[][] results = new float[4][];
                String[] resultnames = new String[4];
                Color[] rescolors = new Color[4];
                
                resultnames[0] = McWBalance.langRB.getString("DAILY_PRECIPITATION_UNITS");
                resultnames[1] = McWBalance.langRB.getString("DAILY_RAINFALL_UNITS");
                resultnames[2] = McWBalance.langRB.getString("DAILY_SNOWMELT_UNITS");
                resultnames[3] = McWBalance.langRB.getString("DAILY_EVAPORATION_UNITS");
                //resultnames[4] = McWBalance.langRB.getString("CUMULATED_SNOWPACK_UNITS");
                
                results[0] = aP.climateTable.climates[scenario].precip;
                results[1] = aP.climateTable.climates[scenario].rain;
                results[2] = aP.climateTable.climates[scenario].melt;
                results[3] = aP.climateTable.climates[scenario].evap;
                //results[4] = ProjSetting.climateScenarios.climateScenarios[scenario].snowpack;
                
                String[] rgb = McWBalance.style.getProperty("PREF_COLOR_PRECIP","255,255,255").split(",");
                rescolors[0] = new Color(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2]));
                rgb = McWBalance.style.getProperty("PREF_COLOR_RAIN","255,255,255").split(",");
                rescolors[1] = new Color(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2]));
                rgb = McWBalance.style.getProperty("PREF_COLOR_MELT","255,255,255").split(",");
                rescolors[2] = new Color(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2]));
                rgb = McWBalance.style.getProperty("PREF_COLOR_EVAP","255,255,255").split(",");
                rescolors[3] = new Color(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2]));
                //rgb = McWBalance.titleBlock.getProperty("PREF_COLOR_SNOWPACK","255,255,255").split(",");
                //rescolors[4] = new Color(Integer.valueOf(rgb[0]),Integer.valueOf(rgb[1]),Integer.valueOf(rgb[2]));
                
                ResultViewerWindow rv = new ResultViewerWindow(aP.climateTable.climates[scenario].getDescription(),
                         results,
                         resultnames,
                         rescolors,
                         McWBalance.langRB.getString("WATER_DEPTH_UNITS")
                );
                

            }

            
        });
        
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel cpanel = new JPanel();
        cpanel.add(scrollPane);
        JPanel bpanel = new JPanel();
        bpanel.add(addClimate);
        bpanel.add(removeClimate);
        bpanel.add(viewData);

        this.add(cpanel, BorderLayout.CENTER); 
        this.add(bpanel, BorderLayout.SOUTH); 
        this.pack();
        
        this.setLocationRelativeTo(owner);
        this.setVisible(true);
        
        
    }
    
}