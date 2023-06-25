package com.mcwbalance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

public class MainWindow extends JFrame implements MouseListener, ActionListener, MouseMotionListener {
    

    
    FlowChartCAD flowChart = new FlowChartCAD(); // This is the Cad area handeled in a seperate class
    String requestedAction = "None"; // sets a switch to allow adding an ELM on next mouse click
    int eLMOnTheMove = -1; // used to allow object to become mobile on mouse click
    int tRNOnTheMove = -1; // used to allow object to become mobile on mouse click
    
    JPanel flowChartPanel = new JPanel(new BorderLayout()); // declair comment flow chart panel
    JScrollPane flowChartScPane = new JScrollPane(flowChartPanel); // moved from beginning to here. 
    // Turns out you need to construct the Panel into the Pane
    static JFrame mainframe = new JFrame("McBalance Pre Pre Alpha"); // JFrame declared here and as static so that ObjELMWindow can refer to it
    static boolean editorIsActive = false; // boolean used to track if ObjELM or ObjTRN windows are arleady active.  
    
    public void MainWindowFunct() {
    
        // sets font for menubar
        Font fontDef = new Font("Arial", Font.PLAIN, 12);
        UIManager.put("Menu.font", fontDef); // Sets default menu font
        UIManager.put("MenuItem.font", fontDef);
        UIManager.put("Label.font", fontDef);
        UIManager.put("ComboBox.font", fontDef);
        UIManager.put("Button.font", fontDef);
        
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setSize(1150, 850);

        // Overall Menu Bar
        JMenuBar menubar = new JMenuBar();
        // Start of File Menu
        JMenu menufile = new JMenu("File");
        menufile.setMnemonic(KeyEvent.VK_F);
        
        JMenuItem menufilenew = new JMenuItem("New Project", KeyEvent.VK_N); 
        menufilenew.setActionCommand("New"); // Event trigger to make new project
        menufilenew.addActionListener(this);
        menufile.add(menufilenew);
           
        JMenuItem menufileopen = new JMenuItem("Open Existing Project", KeyEvent.VK_O);
        menufileopen.setActionCommand("Open"); // Event trigger to change project path
        menufileopen.addActionListener(this);
        menufile.add(menufileopen);
        
        JMenuItem menufilesave = new JMenuItem("Save Project", KeyEvent.VK_O);
        menufilesave.setActionCommand("Save"); // Event trigger to change project path
        menufilesave.addActionListener(this);
        menufile.add(menufilesave);
        
        JMenuItem menufileProjSettings = new JMenuItem("Project Settings", KeyEvent.VK_S);
        menufileProjSettings.setActionCommand("PSettings"); // Event trigger to change project path
        menufileProjSettings.addActionListener(this);
        menufile.add(menufileProjSettings);
        
        menubar.add(menufile);
        //End of File Menu

        //Start of Edit Menu
        JMenu menuedit = new JMenu("Edit");
        menuedit.setMnemonic(KeyEvent.VK_E);
        
        JMenuItem menueditedit = new JMenuItem("Edit Object", KeyEvent.VK_E);
        menueditedit.setActionCommand("EditObj");
        menueditedit.addActionListener(this);
        menuedit.add(menueditedit);
        
        JMenuItem menueditdelete = new JMenuItem("Delete Object", KeyEvent.VK_D);
        menueditdelete.setActionCommand("DeleteObj");
        menueditdelete.addActionListener(this);
        menuedit.add(menueditdelete);
 
        JMenuItem menueditaddELM = new JMenuItem("Add Element", KeyEvent.VK_L);
        menueditaddELM.setActionCommand("AddObjELM");
        menueditaddELM.addActionListener(this);
        menuedit.add(menueditaddELM);
        
        JMenuItem menueditaddTRN = new JMenuItem("Add Transfer", KeyEvent.VK_T);
        menueditaddTRN.setActionCommand("AddObjTRN");
        menueditaddTRN.addActionListener(this);
        menuedit.add(menueditaddTRN);
        
        menubar.add(menuedit);
        //End of Edit Menu
        
         // Start of Solver Menu
        JMenu menusolve = new JMenu("Solve");
        menufile.setMnemonic(KeyEvent.VK_S);
        
        JMenuItem menusolvesall = new JMenuItem("Solve All", KeyEvent.VK_A); 
        menusolvesall.setActionCommand("SolveAll"); // Event trigger to make new project
        menusolvesall.addActionListener(this);
        menusolve.add(menusolvesall);
        
        menubar.add(menusolve);
        //End of File Menu
        SpinnerModel zoomSpinnerModel = new SpinnerNumberModel(FlowChartCAD.zoomscale,.05,2,.05);
        JSpinner zoomSpinner = new JSpinner(zoomSpinnerModel);
        zoomSpinner.setMaximumSize(new Dimension(50, 30));
        zoomSpinner.addChangeListener(e->{
            FlowChartCAD.zoomscale = (double)zoomSpinner.getValue();
            flowChartPanel.setPreferredSize(new Dimension((int)(FlowChartCAD.CAD_AREA_WIDTH*FlowChartCAD.zoomscale),(int)(FlowChartCAD.CAD_AREA_HEIGHT*FlowChartCAD.zoomscale)));
            flowChartPanel.repaint();
        });
        menubar.add(zoomSpinner);
        JLabel zoomSpinnerLabel = new JLabel("Zoom");
        menubar.add(zoomSpinnerLabel);
        
        SpinnerModel dateSpinnerModel = new SpinnerNumberModel(-1,-1,ProjSetting.MAX_DURATION,1);
        JSpinner dateSpinner = new JSpinner(dateSpinnerModel);
        dateSpinner.setMaximumSize(new Dimension(50, 30));
        dateSpinner.addChangeListener(e->{
            FlowChartCAD.drawdate = (int)dateSpinner.getValue();
            flowChartPanel.repaint();
        });
        menubar.add(dateSpinner);
        JLabel dateSpinnerLabel = new JLabel("DisplayDate");
        menubar.add(dateSpinnerLabel);
        
        
        
        
        mainframe.setJMenuBar(menubar);
        // End of Overall MenuBar
        
        flowChartPanel.setBackground(Color.white);
        flowChartPanel.addMouseListener(this);
        flowChartPanel.addMouseMotionListener(this); // added to allow for moving of Object
        flowChartPanel.setPreferredSize(new Dimension((int)(FlowChartCAD.CAD_AREA_WIDTH*FlowChartCAD.zoomscale),(int)(FlowChartCAD.CAD_AREA_HEIGHT*FlowChartCAD.zoomscale)));
        flowChartPanel.add(flowChart); // does not seem to work
      
        flowChartScPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        flowChartScPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        mainframe.add(flowChartScPane); // adds the FlowChart graphics area to the Frame
        mainframe.validate(); // done because set visible is before the menu additions
        mainframe.setIconImage(McWBalance.mainIcon30);
        mainframe.setVisible(true);
        //mainframe.repaint(); not needed unless a refesh is required
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        switch (e.getActionCommand()){
            case "Open":
                ProjOpenExistingWindow projOpenExistingWindow = new ProjOpenExistingWindow();
                projOpenExistingWindow.ProjNewWindowFunc();
                try{
                    ZipFile ifile = new ZipFile(ProjSetting.pathfolder + ProjSetting.saveFileName); // will need to replace with selected file
                
                    ZipEntry zEntVersion = ifile.getEntry("Version.txt");
                    InputStream istream = ifile.getInputStream(zEntVersion);                    
                    String inbuffer = new String(istream.readAllBytes(), "UTF-8");

                    ZipEntry zEntTRNList = ifile.getEntry("TransferList.csv");
                    ZipEntry zEntELMList = ifile.getEntry("ElementList.csv");
                    
                    
                    System.out.println(zEntVersion.toString());
                    System.out.print(inbuffer);
                
                }
                catch (FileNotFoundException fe){
                    fe.printStackTrace();
                }
                catch (IOException fe){
                    fe.printStackTrace();
                }
                
                
                
                break;
            case "Save":
                // builds the version info. 
                StringBuilder sverInfoFile = new StringBuilder();
                sverInfoFile.append(ProjSetting.verInfo);
                
                StringBuilder tRNListFile = flowChart.tRNList.getSaveString();
                StringBuilder eLMListFile = flowChart.elmList.getSaveString();
                
                try{
                FileOutputStream sfileos = new FileOutputStream(ProjSetting.pathfolder + ProjSetting.saveFileName);
                ZipOutputStream sfilezos = new ZipOutputStream(sfileos);
                
                // initializes ZipEntry info for files to include
                ZipEntry zEntVersion = new ZipEntry("Version.txt");
                ZipEntry zEntTRNList = new ZipEntry("TransferList.csv");
                ZipEntry zEntELMList = new ZipEntry("ElementList.csv");
                
                // Write Version Information
                sfilezos.putNextEntry(zEntVersion);
                byte[] bytedata = sverInfoFile.toString().getBytes(); // converts string data to byte data
                sfilezos.write(bytedata, 0, bytedata.length);
                sfilezos.closeEntry();
                
                // Write TransferList Information
                sfilezos.putNextEntry(zEntTRNList);
                bytedata = tRNListFile.toString().getBytes(); // converts string data to byte data // byte data variable re-used
                sfilezos.write(bytedata, 0, bytedata.length);
                sfilezos.closeEntry();
                
                // Write ElementList Information
                sfilezos.putNextEntry(zEntELMList);
                bytedata = eLMListFile.toString().getBytes(); // converts string data to byte data // byte data variable re-used
                sfilezos.write(bytedata, 0, bytedata.length);
                sfilezos.closeEntry();
                
                //writeToZipFile()
                sfilezos.close();
                sfileos.close();
 
                }
                catch (FileNotFoundException fe){
                    fe.printStackTrace();
                }
                catch (IOException fe){
                    fe.printStackTrace();
                }
                
                break;
                
            case "PSettings":
                ProjSettingWindow projSettingWindow = new ProjSettingWindow();
                projSettingWindow.projSettingWindowFunc();
                break;
            case "AddObjELM":
                requestedAction = "AddObjELM";
                break;
            case "AddObjTRN":
                requestedAction = "AddObjTRN";
                break;
            case "DeleteObj":
                requestedAction = "DeleteObj";
                break;
        }        
    }
    
    @Override
    public void mouseClicked(MouseEvent me){
        int mx = (int)(me.getX() / FlowChartCAD.zoomscale);
        int my = (int)(me.getY() / FlowChartCAD.zoomscale);
        int mELMHit;
        int mTRNHit;
       
        if(me.getButton() == MouseEvent.BUTTON1){ // Left Click Action Menu
            switch(requestedAction){
                case "AddObjELM":
                    requestedAction = "None"; //resets menu selection
                    flowChart.addObjELM(mx,my);
                    break;
                    
                case "AddObjTRN":
                    requestedAction = "None"; //resets menu selection
                    flowChart.addObjTRN(mx,my);
                    break;     

                case "DeleteObj":
                    requestedAction = "None"; // assumes object hit. on miss will need to re-try
                    // Note TRNS should be first, since they will draw on top. 
                    mTRNHit = flowChart.checkTRNHit(mx, my);
                    flowChart.removeTRN(mTRNHit); 
                    if (mTRNHit == -1){
                        mELMHit = flowChart.checkELMHit(mx, my);
                        flowChart.removeELM(mELMHit);
                    }
                    break;
                default:
                    mTRNHit = flowChart.checkTRNHit(mx, my);
                    mELMHit = flowChart.checkELMHit(mx, my);
                    if(mTRNHit != -1){
                        if(flowChart.checkSelectionTRN(mTRNHit)){
                            if (!editorIsActive){
                                ObjTRNWindow objTRNWindow = new ObjTRNWindow();
                                objTRNWindow.ObjTRNWindowFunct(flowChart.getObjTRN(mTRNHit), flowChart.elmList.getNameList());
                            }
                            break;
                        }else{
                            flowChart.clearSelection();
                            flowChart.addSelectionTRN(mTRNHit);
                        }
                        
                    }else if (mELMHit != -1){
                        if(flowChart.checkSelectionELM(mELMHit)){
                            if (!editorIsActive){
                                ObjELMWindow objELMWindow = new ObjELMWindow();
                                objELMWindow.ObjELMWindowFunct(flowChart.getObjELM(mELMHit), mELMHit, flowChart.tRNList);
                            }
                            break;
                        }else{
                            flowChart.clearSelection(); // should check if keypressed CTRL, requires implmenting a seperate Key listener and flag.. 
                            flowChart.addSelectionELM(mELMHit);
                        }
                    }else {
                        flowChart.clearSelection();
                    }
            }
            flowChartPanel.repaint();

        }
  
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int mx = (int)(me.getX() / FlowChartCAD.zoomscale); // re-uses same variable names as above, but varables are not the same..
        int my = (int)(me.getY() / FlowChartCAD.zoomscale);

        if(me.getButton() == MouseEvent.BUTTON1){
            switch (requestedAction){
                case "None": // if no other action is pending then Move is possible
                    tRNOnTheMove = flowChart.checkTRNHit(mx, my); // Sets the moving object to whatever is hit
                    if (tRNOnTheMove == -1){
                        eLMOnTheMove = flowChart.checkELMHit(mx, my); // Transfers can move if Larger object isn't moving
                    }
                    
                    break;
                
            }
        }
          //blank for now  
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if(me.getButton() == MouseEvent.BUTTON1){
            eLMOnTheMove = -1;
            tRNOnTheMove = -1; // Halts movement of any object
        }
    }
 
    @Override
    public void mouseEntered(MouseEvent e) { // not used
        
    }

    @Override
    public void mouseExited(MouseEvent e) { // not used for anything
        
    }
    
    @Override
    public void mouseMoved(MouseEvent mme){ // will be used for cursor location tracking

        
        
    }
    @Override
    public void mouseDragged(MouseEvent mme){
        int mx = (int)(mme.getX() / FlowChartCAD.zoomscale); // re-uses same variable names as above, but varables are not the same..
        int my = (int)(mme.getY()/ FlowChartCAD.zoomscale);
        if (eLMOnTheMove > -1){
            flowChart.moveObjELM(mx, my, eLMOnTheMove);
            flowChartPanel.repaint(); 
        }
        else if (tRNOnTheMove > -1){
            flowChart.moveObjTRN(mx, my, tRNOnTheMove);
            flowChartPanel.repaint();
        }
    }
            
            
            
            
}
