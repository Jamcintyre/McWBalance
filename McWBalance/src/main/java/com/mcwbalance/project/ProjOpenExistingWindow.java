/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.project;

import com.mcwbalance.McWBalance;
import java.awt.Dialog;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;


/**
 *
 * @author amcintyre
 */
public class ProjOpenExistingWindow extends JDialog{
    
    /**
     * Prj should be a newly constructed project
     * @param owner
     * @param prj 
     */
    public ProjOpenExistingWindow(JFrame owner, Project prj){
            JDialog subframe = new JDialog(owner, "Choose Existing Project", true); // was orginally a frame but changed to dialog

            subframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            subframe.setLocationRelativeTo(null);
                        
            JFileChooser filechooser = new JFileChooser(prj.setting.getPathFolder());
            filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);      
            filechooser.setFileFilter(McWBalance.DEFAULT_FILEEXTENSION_FILTER);
            
            filechooser.addActionListener(l ->{
                switch (l.getActionCommand()) {
                    case JFileChooser.APPROVE_SELECTION -> {
                        String path = filechooser.getSelectedFile().getAbsolutePath();
                        String dname = filechooser.getSelectedFile().getParent();
                        String fname = filechooser.getSelectedFile().getName();
                        
                        try {
                            //Get contents of Zip file
                            ZipFile loadfile = new ZipFile(path);
                            ZipEntry zeVer = loadfile.getEntry("Version.ver");
                            if(zeVer != null){
                                System.out.println("Load File contains a Version.Ver that is not null");
                                prj.loadXML(loadfile);
                            } else{
                                System.err.println("Load File contains a Version.Ver that is null");
                            }
                                    

                        } catch (FileNotFoundException fe) {
                            JDialog warningDialog = new JDialog(owner, "Error File Not Found", Dialog.ModalityType.DOCUMENT_MODAL);
                            // to add OK button and more descriptive text
                            warningDialog.setVisible(true);
                        } catch (IOException fe) {
                            JDialog warningDialog = new JDialog(owner, "Error IO Exception", Dialog.ModalityType.DOCUMENT_MODAL);
                            // to add OK button and more descriptive text
                            warningDialog.setVisible(true);
                        }

                        subframe.dispose();
                        
                    }
                    case JFileChooser.CANCEL_SELECTION -> subframe.dispose();
                    
                }
            

            });

            subframe.add(filechooser);
            subframe.setSize(new Dimension(600,400));
            
            subframe.setVisible(true);
            
    }
    
}
