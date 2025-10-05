/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.project;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

/**
 *
 * @author amcintyre
 */
public class ProjNewWindow extends JDialog{
    
    
    public void ProjNewWindowFunc(JFrame owner){
            
            SpringLayout layout = new SpringLayout();
            
            JDialog subframe = new JDialog(owner, "New Project Path", true); // was orginally a frame but changed to dialog
            subframe.setLayout(layout);
            
            

    }
}
