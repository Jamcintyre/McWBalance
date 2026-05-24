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
package com.mcwbalance.landcover;

import com.mcwbalance.McWBalance;
import com.mcwbalance.project.Project;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * For user input into runoff coefficients TODO - add basis
 *
 * @author Alex McIntyre
 */
public class RunoffCoefficientWindow extends JFrame {

    private final int TABLE_FIRST_COL_WIDTH = 150;
    private final int TABLE_OTHER_COL_WIDTH = 50;
    private final int TABLE_ROW_HEIGHT = 20;
    private final Dimension TABLE_PREF_DIMENSION = new Dimension(
            TABLE_FIRST_COL_WIDTH + TABLE_OTHER_COL_WIDTH * 12,
            TABLE_ROW_HEIGHT * 10);

    /**
     * Constructs user input window
     *
     * @param owner Parent J frame to call window from
     * @param aP Active project that contains the runoff coefficient data to
     * edit
     */
    public RunoffCoefficientWindow(JFrame owner, Project aP) {
        super(McWBalance.langRB.getString("RUNOFF_COEFFICIENTS"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTable table = new JTable(aP.runoffCoeffs);
        table.getColumnModel().getColumn(0).setPreferredWidth(TABLE_FIRST_COL_WIDTH);
        for (int i = 1; i < aP.runoffCoeffs.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(TABLE_OTHER_COL_WIDTH);
        }
        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setPreferredScrollableViewportSize(TABLE_PREF_DIMENSION);

        JButton buttonaddRow = new JButton(McWBalance.langRB.getString("ADD_ROW"));
        buttonaddRow.addActionListener(l -> {
            aP.runoffCoeffs.addRow();
        });

        JButton buttonDeleteRow = new JButton(McWBalance.langRB.getString("DELETE_ROW"));
        buttonDeleteRow.addActionListener(l -> {
            aP.runoffCoeffs.deleteRow(table.getSelectedRow());
        });

        JButton buttonmoveUp = new JButton(McWBalance.langRB.getString("MOVE_UP"));
        buttonmoveUp.addActionListener(l -> {
            aP.runoffCoeffs.moveUp(table.getSelectedRow());
        });

        JButton buttonmoveDown = new JButton(McWBalance.langRB.getString("MOVE_DOWN"));
        buttonmoveDown.addActionListener(l -> {
            aP.runoffCoeffs.moveDown(table.getSelectedRow());
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
        this.setAlwaysOnTop(true);
        this.setVisible(true);
    }

}
