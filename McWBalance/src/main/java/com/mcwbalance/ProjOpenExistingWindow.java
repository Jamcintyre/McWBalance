/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

import static com.mcwbalance.MainWindow.mainframe;
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
    
    
    public void ProjNewWindowFunc(){
            JDialog subframe = new JDialog(MainWindow.mainframe, "Choose Existing Project", true); // was orginally a frame but changed to dialog

            subframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            subframe.setLocationRelativeTo(null);
                        
            JFileChooser filechooser = new JFileChooser(ProjSetting.pathfolder);
            filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);      
            filechooser.setFileFilter(McWBalance.DEFAULT_FILEEXTENSION_FILTER);
            
            filechooser.addActionListener(l ->{
                switch (l.getActionCommand()) {
                    case JFileChooser.APPROVE_SELECTION -> {
                        
                        ProjSetting.pathfolder = filechooser.getCurrentDirectory();
                        ProjSetting.fileName = filechooser.getSelectedFile();
                        
                        /*

                        try {
                            ZipFile ifile = new ZipFile(ProjSetting.pathfolder + ProjSetting.saveFileName); // will need to replace with selected file

                            ZipEntry zEntVersion = ifile.getEntry("Version.txt");
                            InputStream istream = ifile.getInputStream(zEntVersion);
                            String inbuffer = new String(istream.readAllBytes(), "UTF-8");

                            ZipEntry zEntTRNList = ifile.getEntry("TransferList.csv");
                            ZipEntry zEntELMList = ifile.getEntry("ElementList.csv");

                            System.out.println(zEntVersion.toString());
                            System.out.print(inbuffer);
                        } catch (FileNotFoundException fe) {
                            JDialog warningDialog = new JDialog(mainframe, "Error File Not Found", Dialog.ModalityType.DOCUMENT_MODAL);
                            // to add OK button and more descriptive text
                            warningDialog.setVisible(true);
                        } catch (IOException fe) {
                            JDialog warningDialog = new JDialog(mainframe, "Error IO Exception", Dialog.ModalityType.DOCUMENT_MODAL);
                            // to add OK button and more descriptive text
                            warningDialog.setVisible(true);
                        }
                        */

                        /*
                        Need to change Path and filename here
                        Test to see if file can be opened
                        Then trigger the load command
                         */
                    }
                    case JFileChooser.CANCEL_SELECTION -> subframe.dispose();
                    
                }
            
            
            
            
            });

            subframe.add(filechooser);
            subframe.setSize(new Dimension(600,400));
            
            subframe.setVisible(true);
            
    }
}
