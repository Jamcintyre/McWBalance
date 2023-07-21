/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This JDialog is used to load a .cclm Unicode text formatted climate dataset.
 * cclm is a propriatary format developed for McWBalance
 * @author Alex
 */
public class DataClimateImportWindow extends JDialog{
    private final static FileNameExtensionFilter CLIMATE_FILE_FILTER = new FileNameExtensionFilter("McWBalance Climate Data File","cclm");
    private BufferedReader reader;
    private StringBuilder stringBuilder = new StringBuilder();
    private String ls = System.getProperty("line.separator");
    
    DataClimateImportWindow(JFrame owner) {
        super(owner, "Select Climate Dataset", true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(owner);
        JFileChooser fileChooser = new JFileChooser(ProjSetting.pathFolder);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(CLIMATE_FILE_FILTER);
        fileChooser.addActionListener(l -> {
            switch (l.getActionCommand()) {
                case JFileChooser.APPROVE_SELECTION -> {
                    try {
                        reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                        String line;
                        for (int i = 0; i < ProjSetting.MAX_DURATION + 3; i++) { // climate data file has 2 headers and a 0 row
                            if ((line = reader.readLine()) == null) {
                                break;
                            }
                            stringBuilder.append(line);
                            stringBuilder.append(ls);
                        }
                    } catch (IOException e) {
                        WarningDialog warn = new WarningDialog(owner, e.getMessage());
                    }
                }
            }
        });
        this.add(fileChooser);
        this.pack();
        this.setVisible(true);
    }
    public String getString(){
        return stringBuilder.toString();
    }
}