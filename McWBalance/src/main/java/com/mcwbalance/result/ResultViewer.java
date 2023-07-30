/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
 * @author Alex
 */
public class ResultViewer extends JFrame{
    private int pageWidth;
    private int pageHeight;

    
    private int minY;
    private int maxY;
    
    public int startDate;
    public int endDate;
    SpinnerModel startDateSpinnerModel;
    SpinnerModel endDateSpinnerModel;
    SpinnerModel timeStepSpinnerModel;

    public int timeStep; 
    public static String[] timeStepOptions; 
    
    public ResultViewer(String title, double[][] results, String[] resultnames, Color[] rescolors, String verTitle){
        super(title);
        pageWidth = Integer.valueOf(McWBalance.style.getProperty("EMBEDDED_PAGE_WIDTH", "50"));
        pageHeight = Integer.valueOf(McWBalance.style.getProperty("EMBEDDED_PAGE_HEIGHT", "50"));
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(pageWidth, pageHeight + 37);
        
        int r;
        minY = (int)results[0][0];
        maxY = (int)results[0][0];
  
        for(int i = 0; i < results.length; i++){
            r = (int)CalcBasics.findMinDouble(results[i]);
            if (r < minY){
                minY = r;
            }
        }
        for(int i = 0; i < results.length; i++){
            r = (int)CalcBasics.findMaxDouble(results[i]);
            if (r > maxY){
                maxY = r;
            }      
        }
        
        startDate = 0;
        endDate = results[0].length;
  
        timeStep = ResultViewPanel.DAILY;
        timeStepOptions = McWBalance.langRB.getString("TIME_STEP_OPTIONS").split(",");

        ResultViewPanel resultViewPanel = new ResultViewPanel(results, rescolors,resultnames,0,results[0].length,minY,maxY, verTitle);
        JScrollPane scrollpane = new JScrollPane(resultViewPanel);
        resultViewPanel.repaint();
        
        // View Toolbar
        // Zoom Selector
        JToolBar toolbarView = new JToolBar();
        JPanel toolbarViewpanel = new JPanel();
        
        startDateSpinnerModel = new SpinnerNumberModel(startDate,0,endDate-1,1);
        
        JSpinner startDateSpinner = new JSpinner(startDateSpinnerModel);
        startDateSpinner.setMaximumSize(new Dimension(50, 30));
        startDateSpinner.addChangeListener(e->{
            startDate = (int)startDateSpinner.getValue();
            resultViewPanel.setStartDate(startDate);
        });
        JLabel startDateSpinnerLabel = new JLabel(McWBalance.langRB.getString("START_DATE"));
        toolbarViewpanel.add(startDateSpinnerLabel);
        toolbarViewpanel.add(startDateSpinner);
        
        endDateSpinnerModel = new SpinnerNumberModel(endDate,startDate+1,results[0].length,1);
        JSpinner endDateSpinner = new JSpinner(endDateSpinnerModel);
        endDateSpinner.setMaximumSize(new Dimension(50, 30));
        endDateSpinner.addChangeListener(e->{
            endDate = (int)endDateSpinner.getValue();
            resultViewPanel.setEndDate(endDate);
        });
        JLabel endDateSpinnerLabel = new JLabel(McWBalance.langRB.getString("END_DATE"));
        toolbarViewpanel.add(endDateSpinnerLabel);
        toolbarViewpanel.add(endDateSpinner);
        
        timeStepSpinnerModel = new SpinnerListModel(timeStepOptions);
        timeStepSpinnerModel.setValue(timeStepOptions[0]);
        JSpinner timeStepSpinner = new JSpinner(timeStepSpinnerModel);
        timeStepSpinner.setPreferredSize(new Dimension(100,20));
        timeStepSpinner.addChangeListener(e->{
            timeStep = CalcBasics.findArrayMatchIndex(String.valueOf(timeStepSpinner.getValue()), timeStepOptions);
            resultViewPanel.setTimeStep(timeStep);
        });
        JLabel timeStepSpinnerLabel = new JLabel(McWBalance.langRB.getString("TIME_STEP"));
        toolbarViewpanel.add(timeStepSpinnerLabel);
        toolbarViewpanel.add(timeStepSpinner);
        
        this.setLayout(new BorderLayout());
        this.add(toolbarViewpanel,BorderLayout.SOUTH);
        this.add(scrollpane,BorderLayout.CENTER);
        this.setVisible(true);
    }
    
    
}
