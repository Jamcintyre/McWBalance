/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.dacapacity;

import com.mcwbalance.McWBalance;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
        
        JPopupMenu popupMenuDACTable = new JPopupMenu();
        JMenuItem popupMenuItemDACSelectAll = new JMenuItem("Select All");
        popupMenuItemDACSelectAll.addActionListener(e->{
                table.setColumnSelectionInterval(0, 2);
                table.setRowSelectionInterval(0, table.getRowCount()-1);
        });
        JMenuItem popupMenuItemDACDelete = new JMenuItem("Delete Rows (not Implemented");
        popupMenuItemDACDelete.addActionListener(e->{
                //buffObjELM.dAC.removeRows(tab3TableDAC.getSelectedRows());
        });
        JMenuItem popupMenuItemDACCopy = new JMenuItem("Copy (Not yet working use ctrl+C");
        popupMenuItemDACCopy.addActionListener(e->{
                System.out.println("popup menu Copy button hit");
        });

        JMenuItem popupMenuItemDACPaste = new JMenuItem("Paste");
        popupMenuItemDACPaste.addActionListener(e->{
                dAC.pasteFromClipboard(table.getSelectedRows(),table.getSelectedColumns());
        });
        
        popupMenuDACTable.add(popupMenuItemDACSelectAll);
        popupMenuDACTable.add(popupMenuItemDACDelete);
        popupMenuDACTable.add(popupMenuItemDACCopy);
        popupMenuDACTable.add(popupMenuItemDACPaste);
        table.setComponentPopupMenu(popupMenuDACTable);
        table.setCellSelectionEnabled(true);        
     
        dAC.setPlotGraphic();
        JPanel ppanel = new JPanel();
        ppanel.add(dAC.plotGraphic);
        
        JButton addRow = new JButton(McWBalance.langRB.getString("ADD_ROW"));
        addRow.addActionListener(l ->{
            dAC.addRow();
        });
        
        JButton deleteRow = new JButton(McWBalance.langRB.getString("DELETE_ROW"));
        deleteRow.addActionListener(l ->{
            dAC.deleteRow(table.getSelectedRow());
            
        });
        
        JButton updatePlot = new JButton(McWBalance.langRB.getString("UPDATE_PLOT"));
        updatePlot.addActionListener(l ->{
            dAC.setPlotGraphic();
        });
        
        
        
        
        
        
        
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel cpanel = new JPanel();
        cpanel.add(scrollPane);
        JPanel bpanel = new JPanel();
        bpanel.add(addRow);
        bpanel.add(deleteRow);
        bpanel.add(updatePlot);
        
        
        
        this.add(ppanel, BorderLayout.CENTER);
        this.add(cpanel, BorderLayout.WEST); 
        this.add(bpanel, BorderLayout.SOUTH); 
        this.pack();
        this.setLocationRelativeTo(owner);

        dAC.plotGraphic.setTranslation(ppanel.getX(), ppanel.getY()); // sets location relative to panel
   
        this.setVisible(true);

    }
    
    
    
    
}
