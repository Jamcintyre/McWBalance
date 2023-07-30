/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.climate;

import com.mcwbalance.McWBalance;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.climate.DataClimate;
import com.mcwbalance.result.ResultViewer;
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
 * @author Alex
 */
public class DataClimateSettingWindow extends JFrame{
    private final int TABLE_FIRST_COL_WIDTH = 50;
    private final int TABLE_SECOND_COL_WIDTH = 150;
    private final int TABLE_OTHER_COL_WIDTH = 80;
    private final int TABLE_ROW_HEIGHT = 20;
    private final Dimension TABLE_PREF_DIMENSION = new Dimension(TABLE_FIRST_COL_WIDTH+TABLE_SECOND_COL_WIDTH+(TableClimateScenarios.NUMBER_OF_COLUMNS-1)*TABLE_OTHER_COL_WIDTH,TABLE_ROW_HEIGHT*5);
    
    public DataClimateSettingWindow(JFrame owner){
        
        super(McWBalance.langRB.getString("CLIMATE_SCENARIOS"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
        JTable table = new JTable(ProjSetting.climateScenarios);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(TABLE_FIRST_COL_WIDTH);
        table.getColumnModel().getColumn(1).setPreferredWidth(TABLE_SECOND_COL_WIDTH);
        for (int i = 2; i < TableClimateScenarios.NUMBER_OF_COLUMNS; i ++){
            table.getColumnModel().getColumn(i).setPreferredWidth(TABLE_OTHER_COL_WIDTH);
        }
        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setPreferredScrollableViewportSize(TABLE_PREF_DIMENSION);
        
        
        JButton addClimate = new JButton(McWBalance.langRB.getString("ADD_CLIMATE_SCENARIO"));
        addClimate.addActionListener(l ->{
            DataClimateImportWindow importWindow = new DataClimateImportWindow(this);   
            if(importWindow.getStatus() == DataClimateImportWindow.FILE_OBTAINED){
                ProjSetting.climateScenarios.addClimateScenario(importWindow.getString());
            }
        });
        JButton removeClimate = new JButton(McWBalance.langRB.getString("DELETE_CLIMATE_SCENARIO"));
        removeClimate.addActionListener(l ->{
            ProjSetting.climateScenarios.removeRow(table.getSelectedRow());
        });
        JButton viewData = new JButton(McWBalance.langRB.getString("VIEW_DATA"));
        viewData.addActionListener(l ->{
            int scenario = table.getSelectedRow();
            if (scenario >= 0
                    && scenario < ProjSetting.climateScenarios.climateScenarios.length
                    && !ProjSetting.climateScenarios.climateScenarios[scenario].equals(DataClimate.NULL_DESCRIP)) {

                double[][] results = new double[4][];
                String[] resultnames = new String[4];
                Color[] rescolors = new Color[4];
                
                resultnames[0] = McWBalance.langRB.getString("DAILY_PRECIPITATION_UNITS");
                resultnames[1] = McWBalance.langRB.getString("DAILY_RAINFALL_UNITS");
                resultnames[2] = McWBalance.langRB.getString("DAILY_SNOWMELT_UNITS");
                resultnames[3] = McWBalance.langRB.getString("DAILY_EVAPORATION_UNITS");
                //resultnames[4] = McWBalance.langRB.getString("CUMULATED_SNOWPACK_UNITS");
                
                results[0] = ProjSetting.climateScenarios.climateScenarios[scenario].precip;
                results[1] = ProjSetting.climateScenarios.climateScenarios[scenario].rain;
                results[2] = ProjSetting.climateScenarios.climateScenarios[scenario].melt;
                results[3] = ProjSetting.climateScenarios.climateScenarios[scenario].evap;
                //results[4] = ProjSetting.climateScenarios.climateScenarios[scenario].snowpack;
                
                String[] rgb = McWBalance.style.getProperty("PREF_COLOR_PRECIP","255,255,255").split(",");
                rescolors[0] = new Color(Integer.valueOf(rgb[0]),Integer.valueOf(rgb[1]),Integer.valueOf(rgb[2]));
                rgb = McWBalance.style.getProperty("PREF_COLOR_RAIN","255,255,255").split(",");
                rescolors[1] = new Color(Integer.valueOf(rgb[0]),Integer.valueOf(rgb[1]),Integer.valueOf(rgb[2]));
                rgb = McWBalance.style.getProperty("PREF_COLOR_MELT","255,255,255").split(",");
                rescolors[2] = new Color(Integer.valueOf(rgb[0]),Integer.valueOf(rgb[1]),Integer.valueOf(rgb[2]));
                rgb = McWBalance.style.getProperty("PREF_COLOR_EVAP","255,255,255").split(",");
                rescolors[3] = new Color(Integer.valueOf(rgb[0]),Integer.valueOf(rgb[1]),Integer.valueOf(rgb[2]));
                //rgb = McWBalance.titleBlock.getProperty("PREF_COLOR_SNOWPACK","255,255,255").split(",");
                //rescolors[4] = new Color(Integer.valueOf(rgb[0]),Integer.valueOf(rgb[1]),Integer.valueOf(rgb[2]));
                
                ResultViewer rv = new ResultViewer(ProjSetting.climateScenarios.climateScenarios[scenario].description,
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