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
package com.mcwbalance.climate;

import com.mcwbalance.McWBalance;
import com.mcwbalance.project.Project;
import com.mcwbalance.settings.Limit;
import com.mcwbalance.util.WarningDialog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This JDialog is used to load a .cclm Unicode text formatted climate data set.
 * cclm is a proprietary format developed for McWBalance
 * @author Alex McIntyre
 */
public class DataClimateImportWindow extends JDialog{
    
    /**
     * Status flag for successful operation
     */
    public final static int FILE_OBTAINED = 1;
    /**
     * Status flag for unsuccessful operation, i.e. import file could not be read
     */
    public final static int FILE_NOT_READ = 0;
    
    private int status = FILE_NOT_READ;
    private final FileNameExtensionFilter CLIMATE_FILE_FILTER = new FileNameExtensionFilter(McWBalance.langRB.getString("CLIMATE_DATA_FILE"),"cclm");
    private BufferedReader reader;
    private StringBuilder stringBuilder = new StringBuilder();
    private String ls = System.getProperty("line.separator");
    
    DataClimateImportWindow(JFrame owner, Project aP) {
        super(owner, McWBalance.langRB.getString("SELECT_CLIMATE_DATASET"), true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(owner);
        JFileChooser fileChooser = new JFileChooser(aP.getProjectSetting().getPathFolder());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(CLIMATE_FILE_FILTER);
        fileChooser.addActionListener(l -> {
            switch (l.getActionCommand()) {
                case JFileChooser.APPROVE_SELECTION -> {
                    try {
                        reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                        String line;
                        for (int i = 0; i < Limit.MAX_DURATION + 3; i++) { // climate data file has 2 headers and a 0 row
                            if ((line = reader.readLine()) == null) {
                                break;
                            }
                            stringBuilder.append(line);
                            stringBuilder.append(ls);
                        }
                        status = FILE_OBTAINED;            
                        this.dispose();
                    } catch (IOException e) {
                        WarningDialog warn = new WarningDialog(owner, e.getMessage());
                    }
                }
                case JFileChooser.CANCEL_SELECTION -> this.dispose();
            }
        });
        this.add(fileChooser);
        this.pack();
        this.setVisible(true);
    }
    /**
     * Flags if a file was successfully loaded or not 
     * @return Returns int flag of 0 FILE_NOT_READ or 1 FILE_OBTAINED
     */
    public int getStatus(){
        return status;
    }
    /**
     * Used for returning loaded string
     * @return A String representation of the loaded file
     */
    public String getString(){        
        return stringBuilder.toString();
    }
}
