/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

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
    
    DataClimateSettingWindow(JFrame owner){
        
        super(McWBalance.langRB.getString("CLIMATE_SCENARIOS"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(owner);
        
        JTable table = new JTable(ProjSetting.climateScenarios);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(TABLE_FIRST_COL_WIDTH);
        table.getColumnModel().getColumn(1).setPreferredWidth(TABLE_SECOND_COL_WIDTH);
        for (int i = 2; i < TableClimateScenarios.NUMBER_OF_COLUMNS; i ++){
            table.getColumnModel().getColumn(i).setPreferredWidth(TABLE_OTHER_COL_WIDTH);
        }
        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setPreferredScrollableViewportSize(TABLE_PREF_DIMENSION);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel panel = new JPanel();
        panel.add(scrollPane);
        
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
        panel.add(addClimate);
        panel.add(removeClimate);
        
        this.add(panel); 
        
        this.pack();
        this.setVisible(true);
        
        
    }
    
}