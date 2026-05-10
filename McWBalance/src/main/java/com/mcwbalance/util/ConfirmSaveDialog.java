/*
Copyright (c) 2026, Alex McIntyre
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. All advertising materials mentioning features or use of this software
   must display the following acknowledgement:
   This product includes software developed by Alex McIntyre.
4. Neither the name of the organization nor the
   names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.mcwbalance.util;

import com.mcwbalance.project.ProjSetting;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Dialog window to confirm if project should be saved before proceeding
 * @author Alex McIntyre
 */
public class ConfirmSaveDialog extends JDialog{
    /**
     * Return message indicating Save requested 
     */
    public static final int SELECTION_SAVE = 1;
    /**
     * Return message indicating don't save 
    */
    public static final int SELECTION_NOSAVE = 2;
    /**
     * Return message requesting to cancel the action i.e. don't close program
     */
    public static final int SELECTION_CANCEL = 3;
    private int selection = SELECTION_CANCEL;
    
    /**
     * Generates a do you want to save dialog before closing
     * @param owner
     * @param projSetting 
     */
    public ConfirmSaveDialog(JFrame owner,ProjSetting projSetting){
        super(owner,"McWBalance",true);
        
        JLabel message = new JLabel(projSetting.getSaveFile().getName() + " has been changed since last save, would you like to Save");
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
