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
package com.mcwbalance.result;

import com.mcwbalance.util.CalcBasics;
import com.mcwbalance.McWBalance;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Alex McIntyre
 */
public class ResultViewerWindow extends JFrame {

    private int pageWidth;
    private int pageHeight;

    private int minY;
    private int maxY;
    /**
     * beginning of data to view
     */
    public int startDate;
    /**
     * End of data to view
     */
    public int endDate;
    SpinnerModel startDateSpinnerModel;
    SpinnerModel endDateSpinnerModel;
    SpinnerModel timeStepSpinnerModel;

    /**
     * Time step in increments of overall project time step
     */
    public int timeStep;
    /**
     * for tracking options for time step config
     */
    public static String[] timeStepOptions;

    /**
     * Prototype result viewer
     *
     * @param title
     * @param results
     * @param resultnames
     * @param rescolors
     * @param verTitle
     */
    public ResultViewerWindow(String title, float[][] results, String[] resultnames, Color[] rescolors, String verTitle) {
        super(title);
        pageWidth = Integer.parseInt(McWBalance.style.getProperty("EMBEDDED_PAGE_WIDTH", "50"));
        pageHeight = Integer.parseInt(McWBalance.style.getProperty("EMBEDDED_PAGE_HEIGHT", "50"));

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(pageWidth, pageHeight + 37);

        int r;
        minY = (int) results[0][0];
        maxY = (int) results[0][0];

        for (int i = 0; i < results.length; i++) {
            r = (int) CalcBasics.findMinFloat(results[i]);
            if (r < minY) {
                minY = r;
            }
        }
        for (int i = 0; i < results.length; i++) {
            r = (int) CalcBasics.findMaxFloat(results[i]);
            if (r > maxY) {
                maxY = r;
            }
        }

        startDate = 0;
        endDate = results[0].length;

        timeStep = ResultViewPlot.DAILY;
        timeStepOptions = McWBalance.langRB.getString("TIME_STEP_OPTIONS").split(",");

        ResultViewPlot resultViewPanel = new ResultViewPlot(results, rescolors, resultnames, 0, results[0].length, minY, maxY, verTitle);
        JScrollPane scrollpane = new JScrollPane(resultViewPanel);
        resultViewPanel.repaint();

        // View Toolbar
        // Zoom Selector
        JToolBar toolbarView = new JToolBar();
        JPanel toolbarViewpanel = new JPanel();

        startDateSpinnerModel = new SpinnerNumberModel(startDate, 0, endDate - 1, 1);

        JSpinner startDateSpinner = new JSpinner(startDateSpinnerModel);
        startDateSpinner.setMaximumSize(new Dimension(50, 30));
        startDateSpinner.addChangeListener(e -> {
            startDate = (int) startDateSpinner.getValue();
            resultViewPanel.setStartDate(startDate);
        });
        JLabel startDateSpinnerLabel = new JLabel(McWBalance.langRB.getString("START_DATE"));
        toolbarViewpanel.add(startDateSpinnerLabel);
        toolbarViewpanel.add(startDateSpinner);

        endDateSpinnerModel = new SpinnerNumberModel(endDate, startDate + 1, results[0].length, 1);
        JSpinner endDateSpinner = new JSpinner(endDateSpinnerModel);
        endDateSpinner.setMaximumSize(new Dimension(50, 30));
        endDateSpinner.addChangeListener(e -> {
            endDate = (int) endDateSpinner.getValue();
            resultViewPanel.setEndDate(endDate);
        });
        JLabel endDateSpinnerLabel = new JLabel(McWBalance.langRB.getString("END_DATE"));
        toolbarViewpanel.add(endDateSpinnerLabel);
        toolbarViewpanel.add(endDateSpinner);

        timeStepSpinnerModel = new SpinnerListModel(timeStepOptions);
        timeStepSpinnerModel.setValue(timeStepOptions[0]);
        JSpinner timeStepSpinner = new JSpinner(timeStepSpinnerModel);
        timeStepSpinner.setPreferredSize(new Dimension(100, 20));
        timeStepSpinner.addChangeListener(e -> {
            timeStep = CalcBasics.findArrayMatchIndex(String.valueOf(timeStepSpinner.getValue()), timeStepOptions);
            resultViewPanel.setTimeStep(timeStep);
        });
        JLabel timeStepSpinnerLabel = new JLabel(McWBalance.langRB.getString("TIME_STEP"));
        toolbarViewpanel.add(timeStepSpinnerLabel);
        toolbarViewpanel.add(timeStepSpinner);

        this.setLayout(new BorderLayout());
        this.add(toolbarViewpanel, BorderLayout.SOUTH);
        this.add(scrollpane, BorderLayout.CENTER);
        this.setVisible(true);
    }

}
