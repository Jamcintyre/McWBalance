/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.settings;

import java.awt.Color;

/**
 *
 * @author Alex
 */
public class Preferences {
    
    public static final int LIGHT_MODE = 0; 
    public static final int DARK_MODE = 1;
    public static final int NINETIES_MODE = 2;
    public static int currentMode = LIGHT_MODE;
    public static Color DEFAULT_DRAW_COLOR = Color.BLACK;
    public static Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    
    public static double zoomScale = .5;
    
    public static void setDefaultColors(int viewmode){
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
    
    
    /**
     * NOT COMPLETED
     * @return 
     */
    public static String toSaveString(){
        StringBuilder outstring = new StringBuilder();
        
        
        
        
        return outstring.toString();
    }
    
}
