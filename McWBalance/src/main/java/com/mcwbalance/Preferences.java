/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

import java.awt.Color;

/**
 *
 * @author Alex
 */
public class Preferences {
    
    static final int LIGHT_MODE = 0; 
    static final int DARK_MODE = 1;
    static final int NINETIES_MODE = 2;
    static int currentMode = LIGHT_MODE;
    static Color DEFAULT_DRAW_COLOR = Color.BLACK;
    static Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    
    static double zoomScale = 0.75;
    
    static void setDefaultColors(int viewmode){
        currentMode = viewmode;
        switch (viewmode){
            case LIGHT_MODE ->{
                DEFAULT_DRAW_COLOR = Color.BLACK;
                DEFAULT_BACKGROUND_COLOR = Color.WHITE;
                currentMode = LIGHT_MODE;
            }
            case DARK_MODE ->{
                DEFAULT_DRAW_COLOR = Color.LIGHT_GRAY;
                DEFAULT_BACKGROUND_COLOR = Color.DARK_GRAY;
            }
            case NINETIES_MODE ->{
                DEFAULT_DRAW_COLOR = new Color(0,136,255);
                DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 170);
            }       
        }
    }
    
    
    
    static String toSaveString(){
        StringBuilder outstring = new StringBuilder();
        
        
        
        
        return outstring.toString();
    }
    
}
