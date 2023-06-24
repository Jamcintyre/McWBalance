/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author amcintyre
 */
public class ProjOpenExistingWindow extends JDialog{
    
    
    public void ProjNewWindowFunc(){
            JDialog subframe = new JDialog(MainWindow.mainframe, "Choose Existing Project", true); // was orginally a frame but changed to dialog

            subframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            subframe.setLocationRelativeTo(null);
                        
            JFileChooser filechooser = new JFileChooser();
            
            filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            subframe.add(filechooser);
            subframe.setSize(new Dimension(600,400));
            
            subframe.setVisible(true);
            
            
            
    }
}
