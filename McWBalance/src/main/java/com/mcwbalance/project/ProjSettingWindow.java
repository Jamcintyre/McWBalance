/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.project;

import com.mcwbalance.MainWindow;
import com.mcwbalance.settings.Limit;
import com.mcwbalance.util.WarningDialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author amcintyre
 */
public class ProjSettingWindow extends JDialog{
    private final int TEXTBOX_WIDTH = 25;
    private final int RC_TABLE_FIRST_COL_WIDTH = 100;
    private final int RC_TABLE_OTHER_COL_WIDTH = 30;
    private final int TABLE_ROW_HEIGHT = 20;
    private final Dimension RC_TABLE_PREF_DIMENSION = new Dimension(RC_TABLE_FIRST_COL_WIDTH+12*RC_TABLE_OTHER_COL_WIDTH,TABLE_ROW_HEIGHT*10);
    private int closeAction = 1;
    

   public ProjSettingWindow(){
        
        super(MainWindow.mainframe, "Project Settings", true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(MainWindow.mainframe);
        JTabbedPane tabPane = new JTabbedPane();
        
        //Beginning of Tab 1 General
        JPanel tab1 = new JPanel();
        GridBagLayout layoutTab1 = new GridBagLayout(); // sets up a layout manager for the tab
        tab1.setLayout(layoutTab1);
        
        JLabel tab1PathFolderLabel = new JLabel("Save Directory: ");
        GridBagConstraints tab1PathFolderLabelConstr = new GridBagConstraints();
        tab1PathFolderLabelConstr.gridx = 0;
        tab1PathFolderLabelConstr.gridy = 0;
        tab1PathFolderLabelConstr.anchor = GridBagConstraints.EAST;
        tab1.add(tab1PathFolderLabel,tab1PathFolderLabelConstr);
        JTextField tab1PathFolderField = new JTextField(ProjSetting.pathFolder.getPath());
        tab1PathFolderField.setColumns(TEXTBOX_WIDTH);
        GridBagConstraints tab1PathFolderFieldConstr = new GridBagConstraints();
        tab1PathFolderFieldConstr.gridx = 1;
        tab1PathFolderFieldConstr.gridy = 0;
        tab1PathFolderFieldConstr.anchor = GridBagConstraints.WEST;
        tab1.add(tab1PathFolderField,tab1PathFolderFieldConstr);
        tab1PathFolderField.addActionListener(e-> {
            if(new File(tab1PathFolderField.getText()).isDirectory()){
                ProjSetting.pathFolder = new File(tab1PathFolderField.getText());
            }else{
                new WarningDialog(MainWindow.mainframe,"Directory Does Not Exist or is not Accessible");
                tab1PathFolderField.setText(ProjSetting.pathFolder.getPath());
            }
        });
        
        JLabel tab1FileNameLabel = new JLabel("Filename: ");
        GridBagConstraints tab1FileNameLabelConstr = new GridBagConstraints();
        tab1FileNameLabelConstr.gridx = 0;
        tab1FileNameLabelConstr.gridy = 1;
        tab1FileNameLabelConstr.anchor = GridBagConstraints.EAST;
        tab1.add(tab1FileNameLabel,tab1FileNameLabelConstr);
        JTextField tab1FileNameField = new JTextField(ProjSetting.pathFile.getName());
        tab1FileNameField.setColumns(TEXTBOX_WIDTH);
        GridBagConstraints tab1FileNameFieldConstr = new GridBagConstraints();
        tab1FileNameFieldConstr.gridx = 1;
        tab1FileNameFieldConstr.gridy = 1;
        tab1FileNameFieldConstr.anchor = GridBagConstraints.WEST;
        tab1.add(tab1FileNameField,tab1FileNameFieldConstr);
        tab1FileNameField.addActionListener(e-> {
            System.err.println("Action Listener to be implemented"); // Placholder, will want to check if new path is valid / accessable before committing change;
        });
        
        JLabel tab1ClientNameLabel = new JLabel("Client Name: ");
        GridBagConstraints tab1ClientNameLabelConstr = new GridBagConstraints();
        tab1ClientNameLabelConstr.gridx = 0;
        tab1ClientNameLabelConstr.gridy = 2;
        tab1ClientNameLabelConstr.anchor = GridBagConstraints.EAST;
        tab1.add(tab1ClientNameLabel,tab1ClientNameLabelConstr);
        JTextField tab1ClientNameField = new JTextField(ProjSetting.clientName);
        tab1ClientNameField.setColumns(TEXTBOX_WIDTH);
        GridBagConstraints tab1ClientNameFieldConstr = new GridBagConstraints();
        tab1ClientNameFieldConstr.gridx = 1;
        tab1ClientNameFieldConstr.gridy = 2;
        tab1ClientNameFieldConstr.anchor = GridBagConstraints.WEST;
        tab1.add(tab1ClientNameField,tab1ClientNameFieldConstr);
        tab1ClientNameField.addActionListener(e-> {
            ProjSetting.clientName = tab1ClientNameField.getText();
            // TODO - add a length control, as well as a null control; 
        });
        JLabel tab1ProjectNameLabel = new JLabel("Project Name: ");
        GridBagConstraints tab1ProjectNameLabelConstr = new GridBagConstraints();
        tab1ProjectNameLabelConstr.gridx = 0;
        tab1ProjectNameLabelConstr.gridy = 3;
        tab1ProjectNameLabelConstr.anchor = GridBagConstraints.EAST;
        tab1.add(tab1ProjectNameLabel,tab1ProjectNameLabelConstr);
        JTextField tab1ProjectNameField = new JTextField(ProjSetting.projectName);
        tab1ProjectNameField.setColumns(TEXTBOX_WIDTH);
        GridBagConstraints tab1ProjectNameFieldConstr = new GridBagConstraints();
        tab1ProjectNameFieldConstr.gridx = 1;
        tab1ProjectNameFieldConstr.gridy = 3;
        tab1ProjectNameFieldConstr.anchor = GridBagConstraints.WEST;
        tab1.add(tab1ProjectNameField,tab1ProjectNameFieldConstr);
        tab1ProjectNameField.addActionListener(e-> {
            ProjSetting.projectName = tab1ProjectNameField.getText();
            // TODO - add a length control, as well as a null control; 
        });
        JLabel tab1ProjectNumberLabel = new JLabel("Project Number: ");
        GridBagConstraints tab1ProjectNumberLabelConstr = new GridBagConstraints();
        tab1ProjectNumberLabelConstr.gridx = 0;
        tab1ProjectNumberLabelConstr.gridy = 4;
        tab1ProjectNumberLabelConstr.anchor = GridBagConstraints.EAST;
        tab1.add(tab1ProjectNumberLabel,tab1ProjectNumberLabelConstr);
        JTextField tab1ProjectNumberField = new JTextField(ProjSetting.projectNumber);
        tab1ProjectNumberField.setColumns(TEXTBOX_WIDTH);
        GridBagConstraints tab1ProjectNumberFieldConstr = new GridBagConstraints();
        tab1ProjectNumberFieldConstr.gridx = 1;
        tab1ProjectNumberFieldConstr.gridy = 4;
        tab1ProjectNumberFieldConstr.anchor = GridBagConstraints.WEST;
        tab1.add(tab1ProjectNumberField,tab1ProjectNumberFieldConstr);
        tab1ProjectNumberField.addActionListener(e-> {
            ProjSetting.projectNumber = tab1ProjectNumberField.getText();
            // TODO - add a length control, as well as a null control; 
        });
        JLabel tab1BalanceNameLabel = new JLabel("Balance Name: ");
        GridBagConstraints tab1BalanceNameLabelConstr = new GridBagConstraints();
        tab1BalanceNameLabelConstr.gridx = 0;
        tab1BalanceNameLabelConstr.gridy = 5;
        tab1BalanceNameLabelConstr.anchor = GridBagConstraints.EAST; 
        tab1.add(tab1BalanceNameLabel,tab1BalanceNameLabelConstr);
        JTextField tab1BalanceNameField = new JTextField(ProjSetting.balanceName);
        tab1BalanceNameField.setColumns(TEXTBOX_WIDTH);
        GridBagConstraints tab1BalanceNameFieldConstr = new GridBagConstraints();
        tab1BalanceNameFieldConstr.gridx = 1;
        tab1BalanceNameFieldConstr.gridy = 5;
        tab1BalanceNameFieldConstr.anchor = GridBagConstraints.WEST;
        tab1.add(tab1BalanceNameField,tab1BalanceNameFieldConstr);
        tab1BalanceNameField.addActionListener(e-> {
            ProjSetting.balanceName = tab1BalanceNameField.getText();
            // TODO - add a length control, as well as a null control; 
        });
        
        JLabel tab1DurationLabel = new JLabel("Balance Duration (days): ");
        GridBagConstraints tab1DurationLabelConstr = new GridBagConstraints();
        tab1DurationLabelConstr.gridx = 0;
        tab1DurationLabelConstr.gridy = 6;
        tab1DurationLabelConstr.anchor = GridBagConstraints.EAST;
        tab1.add(tab1DurationLabel,tab1DurationLabelConstr);
        SpinnerModel tab1DurationSpinnerModel = new SpinnerNumberModel(ProjSetting.duration,1,Limit.MAX_DURATION,1);
        JSpinner tab1DurationSpinner = new JSpinner(tab1DurationSpinnerModel);
        GridBagConstraints tab1DurationSpinnerConstr = new GridBagConstraints();
        tab1DurationSpinnerConstr.gridx = 1;
        tab1DurationSpinnerConstr.gridy = 6;
        tab1DurationSpinnerConstr.anchor = GridBagConstraints.WEST; 
        tab1.add(tab1DurationSpinner,tab1DurationSpinnerConstr);
        tab1DurationSpinner.addChangeListener(e-> {
            ProjSetting.duration = (int)tab1DurationSpinnerModel.getValue();
        });

        tabPane.addTab("General",tab1);
        
        // TAB 2
        JPanel tab2 = new JPanel();
        
        JTable tab2Table = new JTable(ProjSetting.runoffCoefficients);
        tab2Table.getColumnModel().getColumn(0).setPreferredWidth(RC_TABLE_FIRST_COL_WIDTH);
        for (int i = 1; i < 13; i ++){
            tab2Table.getColumnModel().getColumn(i).setPreferredWidth(RC_TABLE_OTHER_COL_WIDTH);
        }
        tab2Table.setRowHeight(TABLE_ROW_HEIGHT);
        tab2Table.setPreferredScrollableViewportSize(RC_TABLE_PREF_DIMENSION);
        
        JScrollPane tab2ScrollPane = new JScrollPane(tab2Table); 
        
        
        
        tab2.add(tab2ScrollPane);
        tabPane.addTab("Runoff Coefficients", tab2);
        
        
        this.add(tabPane);
        this.pack();
        this.setVisible(true);
    }
}
