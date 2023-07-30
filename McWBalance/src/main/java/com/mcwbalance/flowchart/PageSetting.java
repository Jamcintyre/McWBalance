/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.flowchart;

import java.awt.Color;

/**
 *
 * @author Alex
 */
public class PageSetting {
    static final int DEFAULT = 1;
    static final int KPL11X17 = 2;
    static final int KPL85X11 = 3;
    
    int pageDimensionWidth;
    int pageDimensionHeight;
    
    int mainBorderXLeftMargin;
    int mainBorderYTopMargin;
    int mainBorderXRightMargin;
    int mainBorderYBottomMargin;
    int mainBorderWidth;
    int mainBorderHeight;
    
    int titleBorderWidth;
    int titleBorderHeight;
    int titleBorderXLeft;
    int titleBorderYTop;
    
    int clientNameUnderlineY;
    int projectNameUnderlineY;
    int figureNameUnderlineY;
    int logoRrightBoundX;
    int pANumberRightBoundX;
    int figureNumberRightBoundX;
    
    int thickLineWidth;
    int thinLineWidth;
    Color titleBlockColor;
    
    PageSetting(){
        this(DEFAULT);
    };
    PageSetting(int pageSelection){
        switch(pageSelection){
            case DEFAULT -> setDEFAULT();
            case KPL11X17 -> setKPL11X17();
            case KPL85X11 -> setKPL85X11();
        }
        
        
    }
    
    
    private void setDEFAULT(){
        setKPL11X17();
    }
    private void setKPL11X17(){

    pageDimensionWidth = 2230; //pixels based on 11x17.
    pageDimensionHeight = 1510; //pixels based on 11x17.
    
    mainBorderXLeftMargin = 102;
    mainBorderYTopMargin = 42;
    mainBorderXRightMargin = 40;
    mainBorderYBottomMargin = 40;
    mainBorderWidth = pageDimensionWidth - mainBorderXLeftMargin - mainBorderXRightMargin;
    mainBorderHeight = pageDimensionHeight - mainBorderYTopMargin - mainBorderYBottomMargin;
    
    titleBorderWidth = 433;
    titleBorderHeight = 189;
    titleBorderXLeft = pageDimensionWidth - mainBorderXRightMargin - titleBorderWidth;
    titleBorderYTop = pageDimensionHeight - mainBorderYBottomMargin - titleBorderHeight; 
    
    clientNameUnderlineY = titleBorderYTop + 32; // measured from titlebound_Y_TOP;
    projectNameUnderlineY = titleBorderYTop + 64;
    figureNameUnderlineY = titleBorderYTop + 128;
    logoRrightBoundX = titleBorderXLeft + 232;
    pANumberRightBoundX = titleBorderXLeft + 252;
    figureNumberRightBoundX = titleBorderXLeft + 406;
    
    thickLineWidth = 3;
    thinLineWidth = 1; 
    titleBlockColor = Color.BLACK;
        
    }
    private void setKPL85X11(){

    pageDimensionWidth = 2230; //pixels based on 11x17.
    pageDimensionHeight = 1510; //pixels based on 11x17.
    
    mainBorderXLeftMargin = 102;
    mainBorderYTopMargin = 42;
    mainBorderXRightMargin = 40;
    mainBorderYBottomMargin = 40;
    mainBorderWidth = pageDimensionWidth - mainBorderXLeftMargin - mainBorderXRightMargin;
    mainBorderHeight = pageDimensionHeight - mainBorderYTopMargin - mainBorderYBottomMargin;
    
    titleBorderWidth = 433;
    titleBorderHeight = 189;
    titleBorderXLeft = pageDimensionWidth - mainBorderXRightMargin - titleBorderWidth;
    titleBorderYTop = pageDimensionHeight - mainBorderYBottomMargin - titleBorderHeight; 
    
    clientNameUnderlineY = titleBorderYTop + 32; // measured from titlebound_Y_TOP;
    projectNameUnderlineY = titleBorderYTop + 64;
    figureNameUnderlineY = titleBorderYTop + 128;
    logoRrightBoundX = titleBorderXLeft + 232;
    pANumberRightBoundX = titleBorderXLeft + 252;
    figureNumberRightBoundX = titleBorderXLeft + 406;
    
    thickLineWidth = 3;
    thinLineWidth = 1; 
    titleBlockColor = Color.BLACK;
        
    }
    
    
    
}
