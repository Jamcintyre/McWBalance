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

import com.mcwbalance.McWBalance;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.JComponent;

/**
 * Used for plotting time Result class data similar to scatter plot
 * 
 * @author alex
 */
public class ResultPlot extends JComponent{
    
    private final Color colorBorder;
    private final Color colorGridLine;

    private final Font fontAxisTitle;
    private final Font fontAxisValue;

    private int incHorz;
    private int incVert;
    private int incHorzNoOfMajors;
    private int incVertNoOfMajors;

    private static final int INC_OPTIONS_VERT[] = {1, 2, 5, 10, 20, 25, 50, 100};

    private static final int INC_OPTIONS_HORZ[] = {
        1, 2, 7, 14, 21, 30, 60, 90, 182, 365, 730, 1460, 2920
    };
    
    
    private class IntPolyLine{
        private ArrayList<Integer> x;
        private ArrayList<Integer> y;
        IntPolyLine(){
            x = new ArrayList();
            y = new ArrayList();
        }
        
        void addPoint(int x, int y){
            this.x.add(x);
            this.y.add(y);
        }
        int[] getX(){
            int[] arr = new int[size()];
            for (int i = 0; i < size(); ++i){
                arr[i] = x.get(i);
            }
            return arr;
        };
        int[] getY(){
            int[] arr = new int[size()];
            for (int i = 0; i < size(); ++i) {
                arr[i] = y.get(i);
            }
            return arr;
        };
        int size(){
            return x.size();
        }
    }
    
    private final int marginTop;
    private final int marginBottom;
    private final int marginRight;
    private final int marginLeft;
    
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    
    /**
     * Area where lines plot on, after margins are applied
     */
    private Rectangle plotArea;
    
    private int plotOffsetLabel;
    private int plotOffsetTitle;
    private int plotTickLength;

    private final int maximumVertIncs;
    private final int maximumHorzIncs;
    
    private int rangeHorz;
    private int rangeVert;
    
    private ResultCollection results;
    
    private ArrayList<IntPolyLine> scaledresults;
    
    private float scaleX;
    private float scaleY;
    
    private final BasicStroke strokeThin;
    private final BasicStroke strokeResult;
    private final BasicStroke strokeThick;
   
    private String titleX;
    private String titleY;
    
    
    /**
     * Generates a plot of a given collection of results
     * @param results collection of results to plot
     */
    public ResultPlot(ResultCollection results){
        
        String[] rgbBorder = McWBalance.style.getProperty("PREF_COLOR_BORDER", "255,255,255").split(",");
        colorBorder = new Color(Integer.parseInt(rgbBorder[0]), Integer.parseInt(rgbBorder[1]), Integer.parseInt(rgbBorder[2]));
        String[] rgbGridline = McWBalance.style.getProperty("PREF_COLOR_GRIDLINE", "255,255,255").split(",");
        colorGridLine = new Color(Integer.parseInt(rgbGridline[0]), Integer.parseInt(rgbGridline[1]), Integer.parseInt(rgbGridline[2]));
        
        fontAxisTitle = new Font("Arial", Font.BOLD, 20);
        fontAxisValue = McWBalance.fontRegular;

        this.results = results; 
        scaledresults = new ArrayList();
        
        marginTop = Integer.parseInt(McWBalance.style.getProperty("EMBEDDED_MARGIN_TOP", "50"));
        marginBottom = Integer.parseInt(McWBalance.style.getProperty("EMBEDDED_MARGIN_BOTTOM", "50"));
        marginLeft = Integer.parseInt(McWBalance.style.getProperty("EMBEDDED_MARGIN_LEFT", "50"));
        marginRight = Integer.parseInt(McWBalance.style.getProperty("EMBEDDED_MARGIN_RIGHT", "50"));

        plotArea = new Rectangle();
        plotOffsetTitle = Integer.parseInt(McWBalance.style.getProperty("PLOT_OFFSET_TITLE", "5"));
        
        maxX = 50;// DEBUG
        maxY = 50;//DEBUG
        maximumVertIncs = Integer.parseInt(McWBalance.style.getProperty("PLOT_MAX_VERT_INCREMENTS", "5"));
        maximumHorzIncs = Integer.parseInt(McWBalance.style.getProperty("PLOT_MAX_HORZ_INCREMENTS", "10"));
        minX = 0;
        minY = 0;

        strokeThick = new BasicStroke(Integer.parseInt(McWBalance.style.getProperty("LINE_THICK", "3")));
        strokeResult = new BasicStroke(Integer.parseInt(McWBalance.style.getProperty("PLOT_LINE_SERIES", "2")));
        strokeThin = new BasicStroke(Integer.parseInt(McWBalance.style.getProperty("LINE_THIN", "1")));

        System.out.println("ResultPlot.Java - Constructor getWidth " + this.getWidth() + "getHeight " + this.getHeight());
        
        update();

    }
    
    private void drawAxisTitles(Graphics2D g2){
        
        AffineTransform orgAT = g2.getTransform();
        g2.setFont(fontAxisTitle);
        
        int textWidth = (int) g2.getFontMetrics().getStringBounds(titleX, g2).getWidth();
        g2.drawString(titleX, plotArea.x + plotArea.width / 2 - textWidth / 2, plotArea.y + plotArea.height + plotOffsetTitle);
        
        textWidth = (int) g2.getFontMetrics().getStringBounds(titleY, g2).getWidth();
        int vlx = marginLeft - plotOffsetTitle;
        int vly = marginTop + plotArea.height / 2 + textWidth / 2;
        g2.rotate(-1.570795,vlx,vly);    // 1/2 pi = 90 deg
        g2.drawString(titleY, vlx, vly);
        g2.setTransform(orgAT); // Resets rotation 
    }
    
    private void drawMajorGridLines(Graphics g2){
        //g2.setStroke(strokeThin);
        g2.setColor(colorBorder);
        g2.setFont(fontAxisValue);
        int major;
        double majorLabelValue;
        String majorLabel;
        int textWidth;
        int textHeight;

        String labelFormatVert = "%.1f";
        if (incVert > 9) {
            labelFormatVert = "%.0f";
        }
        
        System.out.println("incVertNoOfMajors: "+ incVertNoOfMajors + " incVert: " + incVert); //DEBUG
        for (int i = 0; i < incVertNoOfMajors + 1; i++) {
            major = plotArea.height + marginTop - (int) (i * incVert / scaleY);
            System.out.println("Major Y = "+ major); //DEBUG
            majorLabelValue = minY + i * incVert;
            g2.drawLine(plotArea.x - plotTickLength, major, plotArea.x + plotArea.width, major);
            majorLabel = String.format(labelFormatVert, majorLabelValue);
            textWidth = (int) g2.getFontMetrics().getStringBounds(majorLabel, g2).getWidth();
            textHeight = (int) g2.getFontMetrics().getStringBounds(majorLabel, g2).getHeight();
            g2.drawString(majorLabel, marginLeft - plotTickLength - plotOffsetLabel - textWidth, major + textHeight / 2);
        }
        
        String labelFormatHorz = "%.0f";
        for (int i = 0; i < incHorzNoOfMajors + 1; i++) {
            major = marginLeft + (int) (i * incHorz / scaleX);
            majorLabelValue = (i * incHorz + minX);
            g2.drawLine(major, plotArea.y + plotArea.height + plotTickLength, major, plotArea.y);
            majorLabel = String.format(labelFormatHorz, majorLabelValue);
            textWidth = (int) g2.getFontMetrics().getStringBounds(majorLabel, g2).getWidth();
            textHeight = (int) g2.getFontMetrics().getStringBounds(majorLabel, g2).getHeight();
            g2.drawString(majorLabel, major - textWidth / 2, plotArea.y + plotArea.height + plotTickLength + plotOffsetLabel + textHeight);
        }
    }
    
    
    /**
     * This is where the plot is rendered
     * @param g instance to render plot to
     */
    @Override
    public void paintComponent(Graphics g){
        revalidate();
        update();
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // This step fixes the font rendering so it doesn't look pixelated
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        //AffineTransform at = new AffineTransform();
        //at.setToScale(1, 1);
        //g2.setTransform(at); // in event windows scale is not 100%, then at transform is needed first

        g2.setColor(colorBorder);
        //g2.setStroke(thickLine);
        g2.drawRect(plotArea.x, plotArea.y, plotArea.width, plotArea.height);
        
        drawMajorGridLines(g2);
        drawAxisTitles(g2);
    }
    
    
    /**
     * Used to update all data, intended for use after changing units or scale
     */
    public final void update(){
        updateTitleX();
        updateTitleY();
        calcMaxY();
        updatePlotArea();
        updateScales();
    }
    
    /**
     * X axis title is always Time (Units) this method is intended to be 
     * called on construction and when units are switched
     */
    private final void updateTitleX(){
        titleX = McWBalance.langRB.getString("TIME") + " " + results.getTimeUnit().getBracketedDesciptor();
    }
    
    /**
     * Y axis title is always Units (Units) this method is intended to be 
     * called on construction and when units are switched
     */
    private final void updateTitleY(){
        titleY = results.getResultDescriptor();
    }
    /**
     * for updating position of axis, titles, and plot area
     * note plot area limited to min of 5 pixels 
     */
    private final void updatePlotArea(){
        
        plotArea.x = getX() + marginLeft;
        plotArea.y = getY() + marginTop;
        
        int tryheight = this.getHeight() - marginTop - marginBottom;
        if(tryheight < 5){
            tryheight = 5;
        }
        plotArea.height = tryheight;
        
        int trywidth = this.getWidth() - marginRight - marginLeft;
        if(trywidth < 5){
            trywidth = 5;
        }
        plotArea.width = trywidth;
        
    }
    
    public final void updateScales(){
        int rangeX = maxX - minX;
        int rangeY = maxY - minY;
        
        scaleX = rangeX / plotArea.width;
        scaleY = rangeY / plotArea.height;
        if(results.getRowCount() == 0){
            return;
        }
        for (int r = 0; r < results.getRowCount(); ++r){
            int time = (int)results.getValueAt(r, 0);
            
            if(time > maxX){
                break;
            }else if (time >= minX ){
                int x = (int)((float)(time - minX)*scaleX);
                
                for(int c = 1; c < results.getColumnCount(); ++c){
                    int y = (int)((Float.parseFloat(results.getValueAt(r, c).toString()) - minY)*scaleY);
                    
                }
            }
        }
        updateHorizontalGrid();
        updateVerticalGrid();
        
    }
    
    /**
     * Note this method does not set vertical scale this sets the increment of
     * the Horizontal axis to something rounded to the nearest major tick
     */
    private void updateHorizontalGrid() {
 
        rangeHorz = maxX - minX;

        for (int i = 0; i < INC_OPTIONS_HORZ.length; i++) {
            incHorzNoOfMajors = (int) (rangeHorz / INC_OPTIONS_HORZ[i]);
            incHorz = INC_OPTIONS_HORZ[i];
            if (incHorzNoOfMajors < maximumHorzIncs) {
                break;
            }
        }

        for (int i = 0; i < 100; i++) {
            if (i * incHorz + minX > maxX) {
                incHorzNoOfMajors = i;
                break;
            }
        }
        rangeHorz = incHorzNoOfMajors * incHorz;
        scaleX = (float) rangeHorz / plotArea.width;
        
        //TODO Code for updateing the actual result has horizontal scale has been removed
        // Plan to use a path or similar scalable polyline instead of previous 
        // pixel by pixel scale

    }

    /**
     * resets vertical scale
     */
    private void updateVerticalGrid() {
        rangeVert = maxY - minY;
        // this sets the increments of the Vertical axis to something rounded to the nearest major tick
        for (int i = 0; i < INC_OPTIONS_VERT.length; i++) {
            incVertNoOfMajors = (int) (rangeVert / INC_OPTIONS_VERT[i]);
            incVert = INC_OPTIONS_VERT[i];
            if (incVertNoOfMajors < maximumVertIncs) {
                break;
            }
        }
        if (minY == 0) {
        } else if (minY < 0) {
            for (int i = 0; i < 10000; i++) {
                if (minY > -i * incVert) {
                    minY = -i * incVert;
                    break;
                }
            }
        } else if (minY > 0) {
            for (int i = 0; i < 10000; i++) {
                if (minY < (i + 1) * incVert) {
                    minY = i * incVert;
                    break;
                }
            }
        }
        for (int i = 0; i < 100; i++) {
            if (i * incVert + minY > maxY) {
                incVertNoOfMajors = i;
                break;
            }
        }
        rangeVert = incVertNoOfMajors * incVert;
        scaleY = (float) rangeVert / plotArea.height;

    }
    
    /**
     * Method is used to set the Y scale, based on the biggest value in the 
     * result collection
     * Intended to only be run when changes are made to the result collection
     * or requested by user for scale reset
     */
    private void calcMaxY(){
        if (results.getColumnCount() < 2 || results.getRowCount() < 1){
            maxY = 100;
            return;
        }
        
        int mY = 0;
        //First column in results is just the date
        for (int c = 1; c < results.getColumnCount(); ++c){
            for (int r = 0; r < results.getRowCount(); ++r){
                if (mY < (int)results.getValueAt(r, c)){
                    mY = (int)results.getValueAt(r, c);
                }
            }
        }
        maxY = mY;
    }
    
}
