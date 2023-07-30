/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.dacapacity;

import com.mcwbalance.McWBalance;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Alex
 */
public class DACWindow extends JFrame{
    private final int TABLE_FIRST_COL_WIDTH = 100;
    private final int TABLE_SECOND_COL_WIDTH = 100;
    private final int TABLE_THIRD_COL_WIDTH = 100;
    private final int TABLE_ROW_HEIGHT = 20;
    private final Dimension TABLE_PREF_DIMENSION = new Dimension(300,TABLE_ROW_HEIGHT*20);
    
    public DACWindow(JFrame owner, DAC dAC){
        super(McWBalance.langRB.getString("DEPTH_AREA_CAPACITY"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JTable table = new JTable(dAC);
        table.getColumnModel().getColumn(0).setPreferredWidth(TABLE_FIRST_COL_WIDTH);
        table.getColumnModel().getColumn(1).setPreferredWidth(TABLE_SECOND_COL_WIDTH);
        table.getColumnModel().getColumn(2).setPreferredWidth(TABLE_THIRD_COL_WIDTH);
        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setPreferredScrollableViewportSize(TABLE_PREF_DIMENSION);
        
        JButton addRow = new JButton(McWBalance.langRB.getString("ADD_ROW"));
        addRow.addActionListener(l ->{
            dAC.addRow();
        });
        
        JButton deleteRow = new JButton(McWBalance.langRB.getString("DELETE_ROW"));
        deleteRow.addActionListener(l ->{
            dAC.deleteRow(table.getSelectedRow());
        });
        
        JButton viewData = new JButton(McWBalance.langRB.getString("VIEW_DATA"));
        viewData.addActionListener(l ->{
            // to be implemented
        });
        
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel cpanel = new JPanel();
        cpanel.add(scrollPane);
        JPanel bpanel = new JPanel();
        bpanel.add(addRow);
        bpanel.add(deleteRow);
        bpanel.add(viewData);

        this.add(cpanel, BorderLayout.CENTER); 
        this.add(bpanel, BorderLayout.SOUTH); 
        this.pack();
        this.setLocationRelativeTo(owner);
        this.setVisible(true);

    }
    
    
    
    
}
