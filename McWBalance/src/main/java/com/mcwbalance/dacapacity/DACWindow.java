/*
Copyright (c) 2026, Alex McIntyre
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. All advertising materials mentioning features or use of this software
   must display the following acknowledgement:
   This product includes software developed by Alex McIntyre.
4. Neither the name of the organization nor the
   names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.mcwbalance.dacapacity;

import com.mcwbalance.McWBalance;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Alex McIntyre
 */
public class DACWindow extends JFrame{
    private final int TABLE_FIRST_COL_WIDTH = 100;
    private final int TABLE_SECOND_COL_WIDTH = 100;
    private final int TABLE_THIRD_COL_WIDTH = 100;
    private final int TABLE_ROW_HEIGHT = 20;
    private final Dimension TABLE_PREF_DIMENSION = new Dimension(300,TABLE_ROW_HEIGHT*20);
    
    /**
     * Generates a window for interacting with the DAC data for a node
     * @param owner jframe window that will own this window
     * @param dAC depth area capacity curve to edit / manage
     */
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
