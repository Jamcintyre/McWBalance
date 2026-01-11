/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.climate;

import com.mcwbalance.McWBalance;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.project.Project;
import com.mcwbalance.settings.Limit;
import com.mcwbalance.util.WarningDialog;
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
    public final static int FILE_OBTAINED = 1;
    public final static int FILE_NOT_READ = 0;
    
    private int status = FILE_NOT_READ;
    private final FileNameExtensionFilter CLIMATE_FILE_FILTER = new FileNameExtensionFilter(McWBalance.langRB.getString("CLIMATE_DATA_FILE"),"cclm");
    private BufferedReader reader;
    private StringBuilder stringBuilder = new StringBuilder();
    private String ls = System.getProperty("line.separator");
    
    DataClimateImportWindow(JFrame owner, Project aP) {
        super(owner, McWBalance.langRB.getString("SELECT_CLIMATE_DATASET"), true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(owner);
        JFileChooser fileChooser = new JFileChooser(aP.getProjectSetting().getPathFolder());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(CLIMATE_FILE_FILTER);
        fileChooser.addActionListener(l -> {
            switch (l.getActionCommand()) {
                case JFileChooser.APPROVE_SELECTION -> {
                    try {
                        reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                        String line;
                        for (int i = 0; i < Limit.MAX_DURATION + 3; i++) { // climate data file has 2 headers and a 0 row
                            if ((line = reader.readLine()) == null) {
                                break;
                            }
                            stringBuilder.append(line);
                            stringBuilder.append(ls);
                        }
                        status = FILE_OBTAINED;            
                        this.dispose();
                    } catch (IOException e) {
                        WarningDialog warn = new WarningDialog(owner, e.getMessage());
                    }
                }
                case JFileChooser.CANCEL_SELECTION -> this.dispose();
            }
        });
        this.add(fileChooser);
        this.pack();
        this.setVisible(true);
    }
    /**
     * Flags if a file was successfully loaded or not 
     * @return Returns int flag of 0 FILE_NOT_READ or 1 FILE_OBTAINED
     */
    public int getStatus(){
        return status;
    }
    /**
     * Used for returning loaded string
     * @return A String representation of the loaded file
     */
    public String getString(){        
        return stringBuilder.toString();
    }
}
