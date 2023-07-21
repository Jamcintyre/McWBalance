/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

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
    
    
    DataClimateSettingWindow(JFrame owner){
        
        super(java.util.ResourceBundle.getBundle("com/mcwbalance/Language").getString("CLIMATE SETTINGS"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(owner);
        
        JTable table = new JTable(ProjSetting.climateScenariosTable);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tablePanel = new JPanel();
        tablePanel.add(scrollPane);
        this.add(tablePanel); 
        
        JButton addClimate = new JButton(java.util.ResourceBundle.getBundle("com/mcwbalance/Language").getString("ADD CLIMATE SCENARIO"));
        addClimate.addActionListener(l ->{
            DataClimateImportWindow importWindow = new DataClimateImportWindow(this);
        
        });
        this.add(addClimate);
        this.pack();
        this.setVisible(true);
        
        
    }
    
}