/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

import com.mcwbalance.flowchart.FlowChartCAD;
import com.mcwbalance.transfer.ObjTRNList;
import com.mcwbalance.element.ObjELMList;
import static com.mcwbalance.MainWindow.mainframe;
import java.awt.Dialog;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
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
    private static int MAX_ITTERATIONS = 100; 
    
    
    public void ProjNewWindowFunc(){
            JDialog subframe = new JDialog(MainWindow.mainframe, "Choose Existing Project", true); // was orginally a frame but changed to dialog

            subframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            subframe.setLocationRelativeTo(null);
                        
            JFileChooser filechooser = new JFileChooser(ProjSetting.pathFolder);
            filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);      
            filechooser.setFileFilter(McWBalance.DEFAULT_FILEEXTENSION_FILTER);
            
            filechooser.addActionListener(l ->{
                switch (l.getActionCommand()) {
                    case JFileChooser.APPROVE_SELECTION -> {
                        
                        ProjSetting.pathFolder = filechooser.getCurrentDirectory();
                        ProjSetting.pathFile = filechooser.getSelectedFile();

                        try {
                            ZipFile ifile = new ZipFile(ProjSetting.pathFile);
                            Enumeration<? extends ZipEntry> ifEntries = ifile.entries();
                            ZipEntry entry;
                            String entryName[]; // = new String [2];
                            InputStream istream;
                            String inbuffer;
                            int objNumber;
                            FlowChartCAD.eLMList = new ObjELMList(); // wipes existing list data
                            FlowChartCAD.tRNList = new ObjTRNList(); // wipes existing list data
                            System.out.println(ProjSetting.pathFile.getName() + " has been opened");

                            for (int i = 0; ifEntries.hasMoreElements() && i < 100; i++) {
                                entry = ifEntries.nextElement();
                                entryName = entry.getName().split("\\.",2); // splits into Name and extension
                                
                                System.out.println("file " + entryName[0] + " found with extension " + entryName[1]); // DEBUG
                                switch (entryName[1]) {
                                    case "ver" -> {
                                        istream = ifile.getInputStream(entry);
                                        inbuffer = new String(istream.readAllBytes(), "UTF-8");
                                        System.out.print(inbuffer); // debug only should remove and replace with a check version method
                                    }
                                    case "trn" -> {
                                        System.out.println(entryName[0]);
                                        objNumber = Integer.parseInt(entryName[0]); //
                                        if(objNumber < ProjSetting.MAX_TRNS){
                                            istream = ifile.getInputStream(entry);
                                            inbuffer = new String(istream.readAllBytes(), "UTF-8");
                                            FlowChartCAD.tRNList.tRNs[objNumber].setFromString(inbuffer);
                                            if (FlowChartCAD.tRNList.count < objNumber +1){
                                                FlowChartCAD.tRNList.count = objNumber +1;
                                            }
                                        }
                                        System.out.println("found TRN"); // DEBUG PRINTING
                                    }
                                    case "elm" -> {
                                        objNumber = Integer.parseInt(entryName[0]); //
                                        if(objNumber < ProjSetting.MAX_ELMS){
                                            istream = ifile.getInputStream(entry);
                                            inbuffer = new String(istream.readAllBytes(), "UTF-8");
                                            FlowChartCAD.eLMList.eLMs[objNumber].setFromString(inbuffer);
                                            if (FlowChartCAD.eLMList.count < objNumber +1){
                                                FlowChartCAD.eLMList.count = objNumber +1;
                                            }
                                        }
                                    }
                                }
                            } 
                            // should populate bufferd list first then re-initialize if load successful
                            
                            

                        } catch (FileNotFoundException fe) {
                            JDialog warningDialog = new JDialog(mainframe, "Error File Not Found", Dialog.ModalityType.DOCUMENT_MODAL);
                            // to add OK button and more descriptive text
                            warningDialog.setVisible(true);
                        } catch (IOException fe) {
                            JDialog warningDialog = new JDialog(mainframe, "Error IO Exception", Dialog.ModalityType.DOCUMENT_MODAL);
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
