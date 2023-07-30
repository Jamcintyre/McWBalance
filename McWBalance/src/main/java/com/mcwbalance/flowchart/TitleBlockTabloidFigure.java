/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.flowchart;

import java.awt.Color;

/**
 * This class contains the constants related to a Standard KP 11x17 sized Title Block
 * @author amcintyre
 */
public class TitleBlockTabloidFigure {
    static final int PAGE_DIMENSION_WIDTH = 2230; //pixels based on 11x17.
    static final int PAGE_DIMENSION_HEIGHT = 1510; //pixels based on 11x17.
    
    static final int MAINBOUNDARY_X_LEFT_MARGIN = 102;
    static final int MAINBOUNDARY_Y_TOP_MARGIN = 42;
    static final int MAINBOUNDARY_X_RIGHT_MARGIN = 40;
    static final int MAINBOUNDARY_Y_BOTTOM_MARGIN = 40;
    static final int MAINBOUNDARY_WIDTH = PAGE_DIMENSION_WIDTH - MAINBOUNDARY_X_LEFT_MARGIN - MAINBOUNDARY_X_RIGHT_MARGIN;
    static final int MAINBOUNDARY_HEIGHT = PAGE_DIMENSION_HEIGHT - MAINBOUNDARY_Y_TOP_MARGIN - MAINBOUNDARY_Y_BOTTOM_MARGIN;
    
    static final int TITLEBOUND_WIDTH = 433;
    static final int TITLEBOUND_HEIGHT = 189;
    static final int TITLEBOUND_X_LEFT = PAGE_DIMENSION_WIDTH - MAINBOUNDARY_X_RIGHT_MARGIN - TITLEBOUND_WIDTH;
    static final int TITLEBOUND_Y_TOP = PAGE_DIMENSION_HEIGHT - MAINBOUNDARY_Y_BOTTOM_MARGIN - TITLEBOUND_HEIGHT; 
    
    static final int CLIENTNAME_UNDERLINE_Y = TITLEBOUND_Y_TOP + 32; // measured from titlebound_Y_TOP;
    static final int PROJECTNAME_UNDERLINE_Y = TITLEBOUND_Y_TOP + 64;
    static final int FIGURENAME_UNDERLINE_Y = TITLEBOUND_Y_TOP + 128;
    static final int LOGO_RIGHT_BOUND_X = TITLEBOUND_X_LEFT + 232;
    static final int PANUMBER_RIGHT_BOUND_X = TITLEBOUND_X_LEFT + 252;
    static final int FIGURENUMBER_RIGHT_BOUND_X = TITLEBOUND_X_LEFT + 406;
    
    
    static final int THICK_LINE_WIDTH = 3;
    static final int THIN_LINE_WIDTH = 1; 
    static final Color TITLE_BLOCK_COLOR = Color.BLACK;
    

    
    
}
