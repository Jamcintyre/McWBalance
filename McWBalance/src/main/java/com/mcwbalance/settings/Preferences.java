/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.settings;

import java.awt.Color;

/**
 * Most preferences related to gui
 * @author Alex
 */
public class Preferences {
    /**
     * used for terminating .csv lists
     */
    public static final String LIST_TERMINATOR = "---END---";
    /**
     * selector for light mode gui
     */
    public static final int LIGHT_MODE = 0;
    /**
     * selector for dark mode gui
     */
    public static final int DARK_MODE = 1;
    /**
     * selector for blue and yellow mode gui
     */
    public static final int NINETIES_MODE = 2;
    /**
     * current mode used to control how Gui is drawn
     */
    public static int currentMode = LIGHT_MODE;
    
    /**
     * default line work color
     */
    public static Color DEFAULT_DRAW_COLOR = Color.BLACK;
    /**
     * default background color
     */
    public static Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    
    /**
     * used for zume settings
     */
    public static double zoomScale = .65;
    
    /**
     * used for switching between light and dark modes
     * @param viewmode 
     */
    public static void setDefaultColors(int viewmode) {
        currentMode = viewmode;
        switch (viewmode) {
            case LIGHT_MODE -> {
                DEFAULT_DRAW_COLOR = Color.BLACK;
                DEFAULT_BACKGROUND_COLOR = Color.WHITE;
                currentMode = LIGHT_MODE;
            }
            case DARK_MODE -> {
                DEFAULT_DRAW_COLOR = Color.LIGHT_GRAY;
                DEFAULT_BACKGROUND_COLOR = Color.DARK_GRAY;
            }
            case NINETIES_MODE -> {
                DEFAULT_DRAW_COLOR = new Color(0, 136, 255);
                DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 170);
            }
        }
    }

    /**
     * NOT COMPLETED
     *
     * @return
     */
    public static String toSaveString() {
        StringBuilder outstring = new StringBuilder();

        return outstring.toString();
    }

}
