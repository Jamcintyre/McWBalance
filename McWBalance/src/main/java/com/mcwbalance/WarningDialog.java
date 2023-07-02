/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Generic warning dialog for display of errors and exceptions
 * @author Alex
 */
public class WarningDialog extends JDialog{
    WarningDialog(JFrame owner, String warningMessage){
        super(owner,"McWBalance - Warning",true);
        JLabel message = new JLabel(warningMessage);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(l-> {
            this.dispose();
        });

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.add(message);
        panel2.add(okButton); 
        this.add(panel1,BorderLayout.CENTER);
        this.add(panel2,BorderLayout.PAGE_END);
        this.pack();
        this.setLocationRelativeTo(owner);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}
