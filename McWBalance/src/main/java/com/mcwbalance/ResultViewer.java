/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 *
 * @author Alex
 */
public class ResultViewer extends JFrame{
    private int pageWidth;
    private int pageHeight;
    private int marginTop;
    private int marginBottom;
    private int marginRight;
    private int marginLeft;
    
    private int minY;
    private int maxY;
    
    
    ResultViewer(String title, double[][] results, String[] resultnames, Color[] rescolors, String horTitle, String verTitle){
        super(title);
        pageWidth = Integer.valueOf(McWBalance.titleBlock.getProperty("EMBEDDED_PAGE_WIDTH", "50"));
        pageHeight = Integer.valueOf(McWBalance.titleBlock.getProperty("EMBEDDED_PAGE_HEIGHT", "50"));
        
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
        
        ResultViewPanel resultViewPanel = new ResultViewPanel(results, rescolors,resultnames,0,results[0].length,minY,maxY, horTitle, verTitle);
        JScrollPane scrollpane = new JScrollPane(resultViewPanel);
        resultViewPanel.repaint();
        this.add(scrollpane);
        this.setVisible(true);
    }
    
    
}
