/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.dacapacity;

import javax.swing.JFrame;

/**
 *
 * @author amcintyre
 */
public class DACPlotWindow extends JFrame{
    
    private int windowSizeX = 1360;
    private int windowSizeY = 800+37;

    public void plotWindow(DAC dAC){
         
        JFrame frame = new JFrame("DAC PLOT");
    
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        DACPlotGraphics plot = new DACPlotGraphics(dAC);
        frame.setSize(windowSizeX, windowSizeY);
        frame.add(plot);
        frame.setVisible(true);
        
        
    }
    
    
}
