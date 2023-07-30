/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.landcover;

import com.mcwbalance.McWBalance;
import com.mcwbalance.ProjSetting;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Alex
 */
public class RunoffCoefficientWindow extends JFrame {

    private final int TABLE_FIRST_COL_WIDTH = 150;
    private final int TABLE_OTHER_COL_WIDTH = 50;
    private final int TABLE_ROW_HEIGHT = 20;
    private final Dimension TABLE_PREF_DIMENSION = new Dimension(
            TABLE_FIRST_COL_WIDTH + TABLE_OTHER_COL_WIDTH * 12,
            TABLE_ROW_HEIGHT * 10);
    
    public RunoffCoefficientWindow(JFrame owner){
        super(McWBalance.langRB.getString("RUNOFF_COEFFICIENTS"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
        JTable table = new JTable(ProjSetting.runoffCoefficients);
        table.getColumnModel().getColumn(0).setPreferredWidth(TABLE_FIRST_COL_WIDTH);
        for (int i = 1; i < ProjSetting.runoffCoefficients.getColumnCount(); i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(TABLE_OTHER_COL_WIDTH);
        }
        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setPreferredScrollableViewportSize(TABLE_PREF_DIMENSION);
        
        JButton buttonaddRow = new JButton(McWBalance.langRB.getString("ADD_ROW"));
        buttonaddRow.addActionListener(l ->{
            ProjSetting.runoffCoefficients.addRow();
        });
        
        JButton buttonDeleteRow = new JButton(McWBalance.langRB.getString("DELETE_ROW"));
        buttonDeleteRow.addActionListener(l ->{
            ProjSetting.runoffCoefficients.deleteRow(table.getSelectedRow());
        });
        
        JButton buttonmoveUp = new JButton(McWBalance.langRB.getString("MOVE_UP"));
        buttonmoveUp.addActionListener(l ->{
            ProjSetting.runoffCoefficients.moveUp(table.getSelectedRow());
        });
        
        JButton buttonmoveDown = new JButton(McWBalance.langRB.getString("MOVE_DOWN"));
        buttonmoveDown.addActionListener(l ->{
            ProjSetting.runoffCoefficients.moveDown(table.getSelectedRow());
        });
        
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel cpanel = new JPanel();
        cpanel.add(scrollPane);
        JPanel bpanel = new JPanel();
        bpanel.add(buttonaddRow);
        bpanel.add(buttonDeleteRow);
        bpanel.add(buttonmoveUp);
        bpanel.add(buttonmoveDown);
        
        this.add(cpanel, BorderLayout.CENTER); 
        this.add(bpanel, BorderLayout.SOUTH); 
        this.pack();
        this.setLocationRelativeTo(owner);
        this.setVisible(true);
    }

}
