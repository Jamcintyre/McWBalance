/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.solve;

import com.mcwbalance.McWBalance;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.flowchart.FlowChartCAD;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Window used to call in and edit the solve order
 * @author Alex
 */
public class SolveOrderWindow extends JFrame{
    private final int TABLE_FIRST_COL_WIDTH = 50;
    private final int TABLE_SECOND_COL_WIDTH = 50;
    private final int TABLE_THIRD_COL_WIDTH = 150;
    private final int TABLE_FOURTH_COL_WIDTH = 80;
    private final int TABLE_ROW_HEIGHT = 20;
    private final Dimension TABLE_PREF_DIMENSION = new Dimension(360,TABLE_ROW_HEIGHT*20);
    
    public SolveOrderWindow(JFrame owner, FlowChartCAD flowchart, ProjSetting projSetting){
        super(McWBalance.langRB.getString("SOLVE_ORDER"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
        JTable table = new JTable(flowchart.getSolveOrder());
        table.getColumnModel().getColumn(0).setPreferredWidth(TABLE_FIRST_COL_WIDTH);
        table.getColumnModel().getColumn(1).setPreferredWidth(TABLE_SECOND_COL_WIDTH);
        table.getColumnModel().getColumn(2).setPreferredWidth(TABLE_THIRD_COL_WIDTH);
        table.getColumnModel().getColumn(3).setPreferredWidth(TABLE_FOURTH_COL_WIDTH);
        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setPreferredScrollableViewportSize(TABLE_PREF_DIMENSION);
        
        JButton moveUp = new JButton(McWBalance.langRB.getString("MOVE_UP"));
        moveUp.addActionListener(l ->{
            flowchart.getSolveOrder().moveUp(table.getSelectedRow());
        });
        
        JButton moveDown = new JButton(McWBalance.langRB.getString("MOVE_DOWN"));
        moveDown.addActionListener(l ->{
            flowchart.getSolveOrder().moveUp(table.getSelectedRow());
        });
        
        JButton autoOrder = new JButton(McWBalance.langRB.getString("AUTO"));
        autoOrder.addActionListener(l ->{
            flowchart.getSolveOrder().setAutoOrder(flowchart.getTRNList(), flowchart.getNodeList());
        });
        
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel cpanel = new JPanel();
        cpanel.add(scrollPane);
        JPanel bpanel = new JPanel();
        bpanel.add(moveUp);
        bpanel.add(moveDown);
        bpanel.add(autoOrder);
        
        this.add(cpanel, BorderLayout.CENTER); 
        this.add(bpanel, BorderLayout.SOUTH); 
        this.pack();
        this.setLocationRelativeTo(owner);
        this.setVisible(true);
    }
}
