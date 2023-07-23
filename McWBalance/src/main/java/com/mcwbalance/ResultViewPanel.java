/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;

/**
 * Used for plotting time series data, multiple data sets on 2 axis only no secondary
 * axis yet
 * @author Alex
 */
public class ResultViewPanel extends JComponent{
    
    double[][] results;
    Color[] rescolors;
    int minX;
    int maxX;
    int minY;
    int maxY;
    String hozTitle;
    String verTitle;
    
    private int pageHeight;
    private int pageWidth;
    private int marginTop;
    private int marginBottom;
    private int marginRight;
    private int marginLeft;
    
    private int plotHeight;
    private int plotWidth;
    
    private int plotTickLength;
    private int plotOffsetLabel;
    private int plotInsetTitle;
    
    private int maximumVertIncs;
    private int maximumHorzIncs;
    
    private BasicStroke thinLine;
    private BasicStroke thickLine;
    
    private Color colorBorder;
    private Color colorGridLine;
    private String[] rgb;
    
    private static final Font FONT_AXIS_VALUE = new Font("Arial", Font.PLAIN, 18);
    private static final Font FONT_AXIS_TITLE = new Font("Arial", Font.BOLD, 20);
    
    private int rangeHorz;
    private int rangeVert;
    private int incHorz;
    private int incVert;
    private int incHorzNoOfMajors; 
    private int incVertNoOfMajors;
    private double scaleHorz; 
    private double scaleVert;
    
    private int[][] scaledresults;
    private int[] scaledresultsHorz;
    
    private final int INC_OPTIONS_VERT[] = {1,2,5,10,20,25,50,100};
    
    private final int INC_OPTIONS_HORZ[] = {
        1,2,7,14,21,30,60,90,182,365,730,1460,2920
    };
    
    
    ResultViewPanel(double[][] results, Color[] rescolors, int minX, int maxX, int minY, int maxY, String hozTitle, String verTitle){
        // all of the work is done in the paint component method
        this.results = results;
        this.rescolors = rescolors;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.hozTitle = hozTitle;
        this.verTitle = verTitle;
        scaledresults = new int[results.length][results[0].length];
        scaledresultsHorz = new int[results[0].length];
        
        rgb = McWBalance.titleBlock.getProperty("PREF_COLOR_BORDER","255,255,255").split(",");
        colorBorder = new Color(Integer.valueOf(rgb[0]),Integer.valueOf(rgb[1]),Integer.valueOf(rgb[2]));
        rgb = McWBalance.titleBlock.getProperty("PREF_COLOR_GRIDLINE","255,255,255").split(",");
        colorGridLine = new Color(Integer.valueOf(rgb[0]),Integer.valueOf(rgb[1]),Integer.valueOf(rgb[2]));
        
        pageHeight = Integer.valueOf(McWBalance.titleBlock.getProperty("EMBEDDED_PAGE_HEIGHT", "800"));
        pageWidth = Integer.valueOf(McWBalance.titleBlock.getProperty("EMBEDDED_PAGE_WIDTH", "1400"));
        marginTop = Integer.valueOf(McWBalance.titleBlock.getProperty("EMBEDDED_MARGIN_TOP", "50"));
        marginBottom = Integer.valueOf(McWBalance.titleBlock.getProperty("EMBEDDED_MARGIN_BOTTOM", "50"));
        marginLeft = Integer.valueOf(McWBalance.titleBlock.getProperty("EMBEDDED_MARGIN_LEFT", "50")); 
        marginRight = Integer.valueOf(McWBalance.titleBlock.getProperty("EMBEDDED_MARGIN_RIGHT", "50"));
        
        plotHeight = pageHeight - marginTop - marginBottom;
        plotWidth = pageWidth - marginLeft - marginRight;
        
        plotTickLength = Integer.valueOf(McWBalance.titleBlock.getProperty("PLOT_TICK_LENGTH", "4"));
        plotOffsetLabel = Integer.valueOf(McWBalance.titleBlock.getProperty("PLOT_LABEL_OFFSET", "5"));
        plotInsetTitle = Integer.valueOf(McWBalance.titleBlock.getProperty("PLOT_INSET_TITLE", "5"));
        
        maximumVertIncs = Integer.valueOf(McWBalance.titleBlock.getProperty("PLOT_MAX_VERT_INCREMENTS", "5")); 
        maximumHorzIncs = Integer.valueOf(McWBalance.titleBlock.getProperty("PLOT_MAX_HORZ_INCREMENTS", "10"));
        
        
        thickLine = new BasicStroke(Integer.valueOf(McWBalance.titleBlock.getProperty("LINE_THICK", "3")));
        thinLine = new BasicStroke(Integer.valueOf(McWBalance.titleBlock.getProperty("LINE_THIN", "1")));
        
        calcScales();
    }
    
    
    public void calcScales(){
        rangeHorz = maxX - minX;
        rangeVert = maxY - minY;
        // this sets the increments of the Vertical axis to something rounded to the nearest major tick
        for (int i = 0; i < INC_OPTIONS_VERT.length; i++){
            incVertNoOfMajors = (int)(rangeVert / INC_OPTIONS_VERT[i]);
            incVert = INC_OPTIONS_VERT[i];
            if (incVertNoOfMajors < maximumVertIncs){
                break;
            }
        }
        if (minY == 0){ 
        }else if(minY < 0){
            for (int i = 0; i < 10000; i ++){
                if (minY > -i*incVert){
                    minY = -i*incVert;
                    break;
                }
            }
        }else if (minY > 0){
            for (int i = 0; i < 10000; i ++){
                if (minY < (i+1)*incVert){
                    minY = i*incVert;
                    break;
                }
            }
        }
        for(int i = 0; i < 100; i ++){
            if(i*incVert + minY > maxY){
                incVertNoOfMajors = i;
                break;
            }
        }
        rangeVert = incVertNoOfMajors * incVert;
        scaleVert = (double)rangeVert/plotHeight; 

    // this sets the increment of the Horizontal axis to something rounded to the nearest major tick
        for (int i = 0; i < INC_OPTIONS_HORZ.length; i++){
            incHorzNoOfMajors = (int)(rangeHorz / INC_OPTIONS_HORZ[i]);
            incHorz = INC_OPTIONS_HORZ[i];
            if (incHorzNoOfMajors < maximumHorzIncs){
                break;
            }
        }
        if (minX == 0){
        }else if(minX < 0){
            for (int i = 0; i < 10000; i ++){
                if (minX > -i*incHorz){
                    minX = -i*incHorz;
                    break;
                }
            }
        }else if (minX > 0){
            for (int i = 0; i < 10000; i ++){
                if (minX < (i+1)*incHorz){
                    minX = i*incHorz;
                    break;
                }
            }
        }
        
        for(int i = 0; i < 100; i ++){
            if(i*incHorz > maxX){
                incHorzNoOfMajors = i;
                break;
            }
        }
        rangeHorz = incHorzNoOfMajors * incHorz;
        scaleHorz = (double)rangeHorz/ plotWidth; 
        
        
        
        for (int day = 0; day < results[0].length; day++) {
            for (int res = 0; res < results.length; res++) {
                scaledresults[res][day] = pageHeight - marginBottom - (int)((results[res][day]-minY)/scaleVert);
            }
            scaledresultsHorz[day] = marginLeft + (int)(day/scaleHorz);
        }
        
        
        
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform at = new AffineTransform();
        g2.setTransform(at); // in event windows scale is not 100%, then at transform is needed first
        g2.setColor(Color.WHITE); // used to set background color, would have preferred use setbackground to preserve transparancy but doesnt seem to work
        g2.fillRect(0, 0, pageWidth, pageHeight);
        
        // minor gridlines are drawn first, behind the data lines 
        int minor;
        g2.setStroke(thinLine);
        g2.setColor(colorGridLine);
        for (int i = 0; i < incVertNoOfMajors * 5; i ++){
            minor = marginTop+plotHeight - (int)(i*incVert/(scaleVert*5));

            g2.drawLine(marginLeft, minor, marginLeft+plotWidth, minor);
        }
        for (int i = 0; i < incHorzNoOfMajors * 5; i ++){
            minor = marginLeft+plotWidth - (int)(i*incHorz/(scaleHorz*5)); 

            g2.drawLine(minor, pageHeight - marginBottom, minor, marginTop);
        }
        // data lines are drawn next, so tht border and major gridlines overlap
        g2.setStroke(thinLine);
        for (int res = 0; res < results.length; res++) {
                g2.setColor(rescolors[res]);
                g2.drawPolyline(scaledresultsHorz, scaledresults[res], scaledresultsHorz.length);
            }
        
        // draws border
        g2.setColor(colorBorder);
        g2.setStroke(thickLine);
        g2.drawRect(marginLeft, marginTop, plotWidth, plotHeight);
        
        // sets up to draw major gridlines and axis lables
        g2.setStroke(thinLine);
        g2.setColor(colorBorder);
        g2.setFont(FONT_AXIS_VALUE);
        int major;
        double majorLabelValue;
        String majorLabel;
        int textWidth;
        int textHeight;
        
        String labelFormatVert = "%.1f";
        if (incVert > 9){
            labelFormatVert = "%.0f";
        }
        for (int i = 0; i < incVertNoOfMajors+1; i++){
            major = marginTop+plotHeight - (int)(i*incVert/scaleVert); 
            majorLabelValue = minY + i*incVert; 
            g2.drawLine(marginLeft - plotTickLength, major, marginLeft+plotWidth, major);
            majorLabel = String.format(labelFormatVert,majorLabelValue);
            textWidth = (int)g2.getFontMetrics().getStringBounds(majorLabel, g2).getWidth();
            textHeight = (int)g2.getFontMetrics().getStringBounds(majorLabel, g2).getHeight();
            g2.drawString(majorLabel, marginLeft - plotTickLength - plotOffsetLabel - textWidth, major + textHeight/2);
        }
        String labelFormatHorz = "%.0f"; 
        for (int i = 0; i < incHorzNoOfMajors+1; i++){
            major = marginLeft + (int)(i*incHorz/scaleHorz); 
            majorLabelValue = (i*incHorz); 
            g2.drawLine(major, pageHeight - marginBottom + plotTickLength, major, marginTop);
            majorLabel = String.format(labelFormatHorz,majorLabelValue);
            textWidth = (int)g2.getFontMetrics().getStringBounds(majorLabel, g2).getWidth();
            textHeight = (int)g2.getFontMetrics().getStringBounds(majorLabel, g2).getHeight();
            g2.drawString(majorLabel, major - textWidth/2, pageHeight - marginBottom + plotTickLength + plotOffsetLabel + textHeight);
        }
        
        
        
        g2.setFont(FONT_AXIS_TITLE);
        textWidth = (int)g2.getFontMetrics().getStringBounds(hozTitle, g2).getWidth();
        g2.drawString(hozTitle,marginLeft + plotWidth/2 - textWidth/2, pageHeight - plotInsetTitle);
        
        textWidth = (int)g2.getFontMetrics().getStringBounds(verTitle, g2).getWidth();
        textHeight = (int)g2.getFontMetrics().getStringBounds(verTitle, g2).getHeight();
        
        at.setToRotation(0,-1);
        g2.setTransform(at);
        g2.drawString(verTitle,-marginTop - plotHeight/2-textWidth/2,plotInsetTitle + textHeight);
        at.setToRotation(1,0);
        g2.setTransform(at);
        
        
        
    }
    
    
}
