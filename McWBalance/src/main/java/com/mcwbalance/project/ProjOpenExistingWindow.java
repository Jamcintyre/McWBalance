/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.project;

import com.mcwbalance.MainWindow;
import com.mcwbalance.McWBalance;
import com.mcwbalance.flowchart.FlowChartCAD;
import com.mcwbalance.transfer.TRNList;
import com.mcwbalance.node.NodeList;
import com.mcwbalance.settings.Limit;
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
    
    
    public ProjOpenExistingWindow(JFrame owner, FlowChartCAD flowchart, ProjSetting projSetting){
            JDialog subframe = new JDialog(owner, "Choose Existing Project", true); // was orginally a frame but changed to dialog

            subframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            subframe.setLocationRelativeTo(null);
                        
            JFileChooser filechooser = new JFileChooser(projSetting.getPathFolder());
            filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);      
            filechooser.setFileFilter(McWBalance.DEFAULT_FILEEXTENSION_FILTER);
            
            filechooser.addActionListener(l ->{
                switch (l.getActionCommand()) {
                    case JFileChooser.APPROVE_SELECTION -> {
                        
                        projSetting.setSavePath(filechooser.getCurrentDirectory());
                        projSetting.setFileName(filechooser.getSelectedFile());

                        try {
                            ZipFile ifile = new ZipFile(projSetting.getSaveFile());
                            Enumeration<? extends ZipEntry> ifEntries = ifile.entries();
                            ZipEntry entry;
                            String entryName[]; // = new String [2];
                            InputStream istream;
                            String inbuffer;
                            int objNumber;
                            flowchart.setNodeList(new NodeList(projSetting)); // wipes existing list data
                            flowchart.setTRNList(new TRNList()); // wipes existing list data
                            System.out.println(projSetting.getSaveFile().getName() + " has been opened");

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
                                        if(objNumber < Limit.MAX_TRNS){
                                            istream = ifile.getInputStream(entry);
                                            inbuffer = new String(istream.readAllBytes(), "UTF-8");
                                            flowchart.getTRNList().tRNs[objNumber].setFromString(inbuffer);
                                            if (flowchart.getTRNList().count < objNumber +1){
                                                flowchart.getTRNList().count = objNumber +1;
                                            }
                                        }
                                        System.out.println("found TRN"); // DEBUG PRINTING
                                    }
                                    case "elm" -> {
                                        objNumber = Integer.parseInt(entryName[0]); //
                                        if(objNumber < Limit.MAX_NODES){
                                            istream = ifile.getInputStream(entry);
                                            inbuffer = new String(istream.readAllBytes(), "UTF-8");
                                            flowchart.getNodeList().nodes[objNumber].setFromString(inbuffer);
                                            if (flowchart.getNodeList().count < objNumber +1){
                                                flowchart.getNodeList().count = objNumber +1;
                                            }
                                        }
                                    }
                                }
                            } 
                            // should populate bufferd list first then re-initialize if load successful
                            
                            

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
