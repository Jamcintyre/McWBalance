/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.util;

import com.mcwbalance.ProjSetting;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Dialog window to confirm if project should be saved before proceeding
 * @author Alex
 */
public class ConfirmSaveDialog extends JDialog{
    public static final int SELECTION_SAVE = 1;
    public static final int SELECTION_NOSAVE = 2;
    public static final int SELECTION_CANCEL = 3;
    private int selection = SELECTION_CANCEL;
    
    public ConfirmSaveDialog(JFrame owner){
        super(owner,"McWBalance",true);
        
        JLabel message = new JLabel(ProjSetting.pathFile.getName() + " has been changed since last save, would you like to Save");
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(l-> {
            selection = SELECTION_SAVE;
            this.dispose();
        });
        JButton nosaveButton = new JButton("Don't Save");
        nosaveButton.addActionListener(l-> {
            selection = SELECTION_NOSAVE;
            this.dispose();
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(l-> {
            selection = SELECTION_CANCEL;
            this.dispose();
        });
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.add(message);
        panel2.add(saveButton);
        panel2.add(nosaveButton);
        panel2.add(cancelButton);
        
        this.add(panel1,BorderLayout.CENTER);
        this.add(panel2,BorderLayout.PAGE_END);
        this.pack();
        this.setLocationRelativeTo(owner);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    };
    
    
    /**
     * Returns which button was selected See
     * @see SELECTION_SAVE
     * @see SELECTION_NOSAVE
     * @see SELECTION_CANCEL
     * @return integer value of selection
     */
    public int getSelection(){
        return selection; 
    }
    
}
