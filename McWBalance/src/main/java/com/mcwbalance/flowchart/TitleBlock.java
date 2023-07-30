/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.flowchart;


import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Alex
 */
public class TitleBlock {

    int pageWidth;
    int pageHeight;
    int mainBoundaryLeftMargin;
    int mainBoundaryRightMargin;
    int mainBoundaryTopMargin;
    int mainBoundaryBottomMargin;
    int mainBoundaryWidth;
    int mainBoundaryHeight;

    int titleBoundaryWidth;
    int titleBoundaryHeight;
    int titleBoundaryLeft;
    int titleBoundaryTop;

    int clientNameHeight;
    int clientNameUnderline;
    
    int projectNameHeight;
    int projectNameUnderline;
    
    int figureNameHeight;
    int figureNameUnderline;
    
    int logoWidth;
    int logoBoundaryRight;
    int projectNumberBoundaryRight;
    int projectNumberWidth;
    int figureNumberBoundaryRight;
    int figureNumberWidth;
    int lineThickWidth;
    int lineThinWidth;

    Properties prop = new Properties();
    
    TitleBlock(){
       try {
                prop.load(new java.io.FileInputStream("src/main/resources/TitleBlock.properties"));
                setFromProperties();
            } catch (IOException e2) {
                System.err.println("src/main/resources/TitleBlock.properties not located");
                setDefaults();
            }
    }
    
    TitleBlock(String selection){
        try {
            prop.load(new java.io.FileInputStream("src/main/resources/TitleBlock_" + selection + ".properties"));
            setFromProperties();
        } catch (IOException e) {
            System.err.println("src/main/resources/TitleBlock_" + selection + ".properties Not Located");
            setDefaults();

            try {
                prop.load(new java.io.FileInputStream("src/main/resources/TitleBlock.properties"));
                setFromProperties();
            } catch (IOException e2) {
                System.err.println("src/main/resources/TitleBlock.properties not located");
                setDefaults();
            }
        }
    }
    
    private void setDefaults(){
        pageWidth = 5100;
        pageHeight = 3300;
        mainBoundaryLeftMargin = 102;
        mainBoundaryRightMargin = 40;
        mainBoundaryTopMargin = 42;
        mainBoundaryBottomMargin = 40;
        titleBoundaryWidth = 433;
        titleBoundaryHeight = 189;
        clientNameHeight = 32;
        projectNameHeight = 64;
        figureNameHeight = 128;
        logoBoundaryRight = 232;
        projectNumberWidth = 252;
        figureNumberWidth = 406;
        lineThickWidth = 9;
        lineThinWidth = 3;
        calcOtherProperties();
    }
    
    private void setFromProperties(){
        pageWidth = Integer.parseInt(prop.getProperty("PAGE_WIDTH", "5100"));
        pageHeight = Integer.parseInt(prop.getProperty("PAGE_HEIGHT", "3300"));
        mainBoundaryLeftMargin = Integer.parseInt(prop.getProperty("MAIN_BOUNDARY_LEFT_MARGIN", "102"));
        mainBoundaryRightMargin = Integer.parseInt(prop.getProperty("MAIN_BOUNDARY_RIGHT_MARGIN", "40"));
        mainBoundaryTopMargin = Integer.parseInt(prop.getProperty("MAIN_BOUNDARY_TOP_MARGIN", "42"));
        mainBoundaryBottomMargin = Integer.parseInt(prop.getProperty("MAIN_BOUNDARY_BOTTOM_MARGIN", "40"));
        titleBoundaryWidth = Integer.parseInt(prop.getProperty("TITLE_BOUNDARY_WIDTH", "433"));
        titleBoundaryHeight = Integer.parseInt(prop.getProperty("TITLE_BOUNDARY_HEIGHT", "189"));
        clientNameHeight = Integer.parseInt(prop.getProperty("CLIENT_NAME_HEIGHT", "32"));
        projectNameHeight = Integer.parseInt(prop.getProperty("PROJECT_NAME_HEIGHT", "64"));
        figureNameHeight = Integer.parseInt(prop.getProperty("FIGURE_NAME_HEIGHT", "128"));
        logoWidth = Integer.parseInt(prop.getProperty("LOGO_WIDTH", "232"));
        projectNumberWidth = Integer.parseInt(prop.getProperty("PROJECT_NUMBER_WIDTH", "30"));
        figureNumberWidth = Integer.parseInt(prop.getProperty("FIGURE_NUMBER_WIDTH", "174"));
        lineThickWidth = Integer.parseInt(prop.getProperty("LINE_WITDH_THICK", "9"));
        lineThinWidth = Integer.parseInt(prop.getProperty("LINE_WITDH_THIN", "3"));
        calcOtherProperties();
    }
    
    private void calcOtherProperties(){
        mainBoundaryWidth = pageWidth - mainBoundaryLeftMargin - mainBoundaryRightMargin;
        mainBoundaryHeight = pageHeight - mainBoundaryTopMargin - mainBoundaryBottomMargin;
        titleBoundaryLeft = pageWidth - mainBoundaryRightMargin - titleBoundaryWidth;
        titleBoundaryTop = pageHeight - mainBoundaryBottomMargin - titleBoundaryHeight;
        clientNameUnderline = titleBoundaryTop - clientNameHeight;
        projectNameUnderline = clientNameUnderline - projectNameHeight;
        figureNameUnderline = clientNameUnderline - figureNameHeight;
        logoBoundaryRight = titleBoundaryLeft + logoWidth; 
        projectNumberBoundaryRight = logoBoundaryRight + projectNumberWidth;
        figureNumberBoundaryRight = logoBoundaryRight + figureNumberWidth;
    }
    
    
}
