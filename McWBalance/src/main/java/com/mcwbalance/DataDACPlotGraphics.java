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
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.text.AttributedString;
import javax.swing.JComponent;

/**
 *
 * @author amcintyre
 */
public class DataDACPlotGraphics extends JComponent{
    private static final int PAGE_WIDTH = 1360;
    private static final int PAGE_HEIGHT = 800;
    private static final int MARGIN_TOP = 70;
    private static final int MARGIN_BOTTOM = 70;
    private static final int MARGIN_LEFT = 90;
    private static final int MARGIN_RIGHT = 50;
    
    private static final int CHART_WIDTH = PAGE_WIDTH - MARGIN_LEFT - MARGIN_RIGHT;
    private static final int CHART_HEIGHT = PAGE_HEIGHT - MARGIN_TOP - MARGIN_BOTTOM;
    
    private static final int MAX_NO_OF_ELEV_INCS = 7;
    private static final int MAX_NO_OF_HORIZONTAL_INCS = 10;
    
    private static final BasicStroke THIN_LINE = new BasicStroke(1);
    private static final BasicStroke THICK_LINE = new BasicStroke(3);
    
    private static final int TICK_LENGTH = 4;
    private static final int OFFSET_LABEL = 5;
    private static final int INSET_TITLE = 8; 
    
    private static final Color COLOR_DEFAULT = Color.BLACK;
    private static final Color COLOR_GRIDLINE = new Color(198,198,198);
    private static final Color COLOR_AREA = new Color(119,147,60);
    private static final Color COLOR_VOLUME = new Color(85,142,212);
    
    private static final Font FONT_AXIS_VALUE = new Font("Arial", Font.PLAIN, 18);
    private static final Font FONT_AXIS_TITLE = new Font("Arial", Font.BOLD, 20);
    
    private static double rangeElev;
    private static int rangeVol;
    private static int rangeArea;
 
    
    private static double minElev;
    private static int minVol = 0; // min area is always going to default to 0
    private static int minArea = 0; // min area is always going to default to 0

    
    private static double incElev;
    private static int incVol; 
    private static int incArea;
    
    private static int incElevNoOfMajors; 
    private static int incVolNoOfMajors;
    
    private static double scaleElev; 
    private static double scaleVol; 
    private static double scaleArea; 
    
    
    private static int scaledElev[];
    private static int scaledVol[];
    private static int scaledArea[];
    
    private static final int INC_OPTIONS_VERT[] = {1,2,5,10,20,25,50,100};
    
    private static final int INC_OPTIONS_HORZ[] = {
        1,2,5,
        10,20,25,30,40,50,
        100,150,200,250,300,400,500,
        1000,1500,2000,2500,3000,4000,5000,
        10000,15000,20000,25000,30000,40000,50000,
        100000,150000,200000,250000,300000,400000,500000,
        1000000,1500000,2000000,2500000,3000000,4000000,500000,
        10000000,15000000,20000000,25000000,30000000,40000000,50000000,
        100000000,150000000,200000000,250000000,300000000,400000000,500000000
    };

    DataDACPlotGraphics(DataDAC dAC){
        minElev = dAC.elev[0];
        rangeElev =  dAC.elev[dAC.elev.length-1] - minElev;
        rangeArea =  dAC.area[dAC.vol.length-1] - minArea;
        rangeVol = dAC.vol[dAC.vol.length-1] - minVol;
        
        
        scaledElev = new int[dAC.elev.length];
        scaledVol = new int[dAC.elev.length];
        scaledArea = new int[dAC.elev.length];
        
        System.out.println("Elevation Range " + rangeElev);
        // this sets the increments of the elevation axis to something rounded to the nearest major tick
        for (int i = 0; i < INC_OPTIONS_VERT.length; i++){
            incElevNoOfMajors = (int)(rangeElev / INC_OPTIONS_VERT[i]);
            incElev = INC_OPTIONS_VERT[i];
            if (incElevNoOfMajors < MAX_NO_OF_ELEV_INCS){
                break;
            }
        }

        if (minElev == 0){
        }else if(minElev < 0){
            for (int i = 0; i < 10000; i ++){
                if (minElev > -i*incElev){
                    minElev = -i*incElev;
                    break;
                }
            }
        }else if (minElev > 0){
            for (int i = 0; i < 10000; i ++){
                if (minElev < (i+1)*incElev){
                    minElev = i*incElev;
                    break;
                }
            }
        }

        for(int i = 0; i < 100; i ++){
            if(i*incElev + minElev> dAC.elev[dAC.elev.length-1]){
                incElevNoOfMajors = i;
                break;
            }
        }
        rangeElev = incElevNoOfMajors * incElev;

        scaleElev = rangeElev/ CHART_HEIGHT; 
        // this sets the increment of the Volume axis to something rounded to the nearest major tick (note that min Volume is assumed to be 0;
        for (int i = 0; i < INC_OPTIONS_HORZ.length; i++){
            incVolNoOfMajors = (int)(rangeVol / INC_OPTIONS_HORZ[i]);
            incVol = INC_OPTIONS_HORZ[i];
            if (incVolNoOfMajors < MAX_NO_OF_HORIZONTAL_INCS){
                break;
            }
        }
        for(int i = 0; i < 100; i ++){
            if(i*incVol > dAC.vol[dAC.vol.length-1]){
                incVolNoOfMajors = i;
                break;
            }
        }
        rangeVol = incVolNoOfMajors * incVol;
        scaleVol = (double)rangeVol/ CHART_WIDTH; 
        
        // sets the increment for Area axis, majors must line up with Volume.
        for (int i = 0; i < INC_OPTIONS_HORZ.length; i++){
            if (incVolNoOfMajors*INC_OPTIONS_HORZ[i] > dAC.area[dAC.area.length-1]){
                incArea = INC_OPTIONS_HORZ[i];
                break;
            }
            
        }
        rangeArea = incVolNoOfMajors * incArea;
        scaleArea = (double)rangeArea/ CHART_WIDTH; 
        
        
        
        for(int i = 0; i < dAC.elev.length; i++){
            
            scaledElev[i] = PAGE_HEIGHT - MARGIN_BOTTOM - (int)((dAC.elev[i] - minElev)/scaleElev);
            scaledArea[i] = PAGE_WIDTH - MARGIN_RIGHT - (int)((dAC.area[i] - minArea)/scaleArea);
            scaledVol[i] = MARGIN_LEFT + (int)(dAC.vol[i]/scaleVol);
            
        }

    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform at = new AffineTransform();
        g2.setTransform(at); // in event windows scale is not 100%, then at transform is needed first
        g2.setColor(Color.WHITE); // used to set background color, would have preferred use setbackground to preserve transparancy but doesnt seem to work
        g2.fillRect(0, 0, PAGE_WIDTH, PAGE_HEIGHT);
        
        // minor gridlines are drawn first, behind the data lines
        int minor;
        g2.setStroke(THIN_LINE);
        g2.setColor(COLOR_GRIDLINE);
        for (int i = 0; i < incElevNoOfMajors * 5; i ++){
            minor = MARGIN_TOP+CHART_HEIGHT - (int)(i*incElev/(scaleElev*5));

            g2.drawLine(MARGIN_LEFT, minor, MARGIN_LEFT+CHART_WIDTH, minor);
        }
        for (int i = 0; i < incVolNoOfMajors * 5; i ++){
            minor = MARGIN_LEFT+CHART_WIDTH - (int)(i*incVol/(scaleVol*5)); 

            g2.drawLine(minor, PAGE_HEIGHT - MARGIN_BOTTOM, minor, MARGIN_TOP);
        }
        // data lines are drawn next, so tht border and major gridlines overlap
        g2.setStroke(THICK_LINE);
        g2.setColor(COLOR_VOLUME);
        g2.drawPolyline(scaledVol, scaledElev, scaledElev.length);
        g2.setColor(COLOR_AREA);
        g2.drawPolyline(scaledArea, scaledElev, scaledElev.length);
        
        // draws border
        g2.setColor(COLOR_DEFAULT);
        g2.setStroke(THICK_LINE);
        g2.drawRect(MARGIN_LEFT, MARGIN_TOP, CHART_WIDTH, CHART_HEIGHT);

        
        // sets up to draw major gridlines and axis lables
        g2.setStroke(THIN_LINE);
        g2.setColor(COLOR_DEFAULT);
        g2.setFont(FONT_AXIS_VALUE);
        int major;
        double majorLabelValue;
        String majorLabel;
        int textWidth;
        int textHeight;
        
        String labelElevAxis = "Elevation (m)";
        String labelFormatElev = "%.1f";
        if (incElev > 10){
            labelFormatElev = "%.0f";
        }
        
        
        int labelFactorArea = 1; 
        String labelAreaAxis;
        String labelFormatArea = "%.0f";
        if (incArea >=1000000){
            labelFactorArea = 1000000;
            labelAreaAxis ="Area (Million sq.m.)";
            labelFormatArea = "%.1f";
        }else if (incArea >=10000){
            labelFactorArea = 10000;
            labelAreaAxis = "Area (ha)";
            labelFormatArea = "%.1f";
        }else if (incArea >=1000){
            labelFactorArea = 1000;
            labelAreaAxis = "Area (Thousand sq.m.)";
            labelFormatArea = "%.1f";
        }else{
            labelAreaAxis = "Area (sq.m.)";
        }
 
        
        int labelFactorVol = 1;
        String labelVolAxis = "Volume (cu.m.)";
        String labelFormatVol = "%.0f";
        
        if (incVol >=1000000){
            labelFactorVol = 1000000;
            labelVolAxis = "Volume (Million cu.m.)";
            labelFormatVol = "%.1f";
        }else if (incVol >=1000){
            labelFactorVol = 1000;
            labelVolAxis = "Volume (Thousand cu.m.)";
            labelFormatVol = "%.1f";
        }
        

        g2.setColor(COLOR_DEFAULT);
        for (int i = 0; i < incElevNoOfMajors+1; i++){
            major = MARGIN_TOP+CHART_HEIGHT - (int)(i*incElev/scaleElev); 
            majorLabelValue = minElev + i*incElev; 
            g2.drawLine(MARGIN_LEFT - TICK_LENGTH, major, MARGIN_LEFT+CHART_WIDTH, major);
            majorLabel = String.format(labelFormatElev,majorLabelValue);
            textWidth = (int)g2.getFontMetrics().getStringBounds(majorLabel, g2).getWidth();
            textHeight = (int)g2.getFontMetrics().getStringBounds(majorLabel, g2).getHeight();
            g2.drawString(majorLabel, MARGIN_LEFT - TICK_LENGTH - OFFSET_LABEL - textWidth, major + textHeight/2);
        }
        for (int i = 0; i < incVolNoOfMajors+1; i++){
            major = MARGIN_LEFT + (int)(i*incVol/scaleVol); 
            //vol label
            majorLabelValue = (minVol + i*incVol)/labelFactorVol; 
            g2.drawLine(major, PAGE_HEIGHT - MARGIN_BOTTOM + TICK_LENGTH, major, MARGIN_TOP);
            majorLabel = String.format(labelFormatVol,majorLabelValue);
            textWidth = (int)g2.getFontMetrics().getStringBounds(majorLabel, g2).getWidth();
            textHeight = (int)g2.getFontMetrics().getStringBounds(majorLabel, g2).getHeight();
            g2.drawString(majorLabel, major - textWidth/2, PAGE_HEIGHT - MARGIN_BOTTOM + TICK_LENGTH + OFFSET_LABEL + textHeight);
            //area lable
            majorLabelValue = ((minArea+rangeArea) - i*incArea)/labelFactorArea; 
            majorLabel = String.format(labelFormatArea,majorLabelValue);
            textWidth = (int)g2.getFontMetrics().getStringBounds(majorLabel, g2).getWidth();
            g2.drawString(majorLabel, major - textWidth/2, MARGIN_TOP - TICK_LENGTH - OFFSET_LABEL);
        }
        
        g2.setFont(FONT_AXIS_TITLE);
        textWidth = (int)g2.getFontMetrics().getStringBounds(labelAreaAxis, g2).getWidth();
        textHeight = (int)g2.getFontMetrics().getStringBounds(labelAreaAxis, g2).getHeight();
        g2.drawString(labelAreaAxis,MARGIN_LEFT + CHART_WIDTH/2 - textWidth/2, INSET_TITLE+textHeight);
        textWidth = (int)g2.getFontMetrics().getStringBounds(labelVolAxis, g2).getWidth();
        g2.drawString(labelVolAxis,MARGIN_LEFT + CHART_WIDTH/2 - textWidth/2, PAGE_HEIGHT - INSET_TITLE);
        
        textWidth = (int)g2.getFontMetrics().getStringBounds(labelElevAxis, g2).getWidth();
        textHeight = (int)g2.getFontMetrics().getStringBounds(labelElevAxis, g2).getHeight();
        
        at.setToRotation(0,-1);
        g2.setTransform(at);
        g2.drawString(labelElevAxis,-MARGIN_TOP - CHART_HEIGHT/2-textWidth/2,INSET_TITLE + textHeight);
        at.setToRotation(1,0);
        g2.setTransform(at);

    }
}
