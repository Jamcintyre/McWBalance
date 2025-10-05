package com.mcwbalance;


import com.mcwbalance.project.ProjSettingWindow;
import com.mcwbalance.project.ProjOpenExistingWindow;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.settings.Preferences;
import com.mcwbalance.solve.SolveOrderWindow;
import com.mcwbalance.flowchart.FlowChartCAD;
import com.mcwbalance.util.ConfirmSaveDialog;
import com.mcwbalance.transfer.TRNWindow;
import com.mcwbalance.transfer.TRNList;
import com.mcwbalance.node.NodeWindow;
import com.mcwbalance.node.NodeList;
import com.mcwbalance.util.WarningDialog;
import com.mcwbalance.landcover.RunoffCoefficientWindow;
import com.mcwbalance.climate.DataClimateSettingWindow;
import com.mcwbalance.settings.Limit;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class MainWindow extends JFrame implements MouseListener, ActionListener, MouseMotionListener, MouseWheelListener{
    
    
    /**
     * Cad drawing area
     */
    FlowChartCAD flowchart;
    
    /**
     * Open Icon
     */
    ImageIcon iconOpen;
    
    /**
     * Save Icon
     */
    ImageIcon iconSave;
    
    /**
     * SaveAs Icon
     */
    ImageIcon iconSaveAs;
    
    /**
     * New Project Icon
     */
    ImageIcon iconNewProject;
    
    /**
     * Settings Icon
     */
    ImageIcon iconSettings;
    
    /**
     * Delete Icon
     */
    ImageIcon iconDelete;
    
    /**
     * New Element Icon
     */
    ImageIcon iconNewELM;
    
    /**
     * New Transfer Icon
     */
    ImageIcon iconNewTRN;
    
    /**
     * container for project settings
     */
    ProjSetting projSetting; 
    
    /**
     * Spinner for selecting zoom level on CAD interface
     */
    SpinnerModel zoomSpinnerModel;
    
    /**
     * sets a switch to allow adding an ELM on next mouse click
     */
    String requestedAction = "None";
    
    /**
     * used to allow object to become mobile on mouse click, -1 for not on the move
     */
    int nodeOnTheMove = -1;
    /**
     * used to allow object to become mobile on mouse click, -1 for not on the move
     */
    int tRNOnTheMove = -1;
    
    boolean isViewPanning = false;
    int viewX;
    int viewY;
    int startMouseX;
    int startMouseY;
    static final double PAN_SPEED = 0.85; // if set to 1 its gittery
    static final double ZOOM_MIN = 0.05;
    static final double ZOOM_MAX = 2;
    static final double ZOOM_STEP = 0.05;
    
    
    JPanel flowChartPanel = new JPanel(new BorderLayout()); // declair comment flow chart panel
    JScrollPane flowChartScPane = new JScrollPane(flowChartPanel); // moved from beginning to here. 
    
    // public static JFrame mainframe = new JFrame("McWBalance Pre-Release Non-Functional"); // JFrame declared here and as static so that ObjELMWindow can refer to it
    public static boolean editorIsActive = false; // boolean used to track if ObjELM or ObjTRN windows are arleady active.  
    
    
    
    /**
     * Constructs a new main window
     */
    public MainWindow() {
        
        projSetting = new ProjSetting();
        
        flowchart = new FlowChartCAD(projSetting);
        
        // Placeholders, pull from resource list
        iconOpen = new ImageIcon("bin/icons/open.png");
        iconSave = new ImageIcon("bin/icons/save.png");
        iconSaveAs = new ImageIcon("bin/icons/saveAs.png");
        iconNewProject = new ImageIcon("bin/icons/newProject.png");
        iconSettings = new ImageIcon("bin/icons/settings.png");
        iconDelete = new ImageIcon("bin/icons/delete.png");
        iconNewELM = new ImageIcon("bin/icons/newELM.png");
        iconNewTRN = new ImageIcon("bin/icons/newTRN.png");
        
        zoomSpinnerModel = new SpinnerNumberModel(FlowChartCAD.zoomscale,ZOOM_MIN,ZOOM_MAX,ZOOM_STEP);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1150, 850);

        // Overall Menu Bar
        JMenuBar menubar = new JMenuBar();
        // Start of File Menu
        JMenu menufile = new JMenu(McWBalance.langRB.getString("FILE"));
        menufile.setMnemonic(KeyEvent.VK_F);
        
        JMenuItem menufilenew = new JMenuItem(McWBalance.langRB.getString("NEW_PROJECT"), KeyEvent.VK_N); 
        menufilenew.setActionCommand("new"); // Event trigger to make new project
        menufilenew.addActionListener(this);
        menufilenew.setIcon(iconNewProject);
        menufile.add(menufilenew);
           
        JMenuItem menufileopen = new JMenuItem(McWBalance.langRB.getString("OPEN_EXISTING_PROJECT"), KeyEvent.VK_O);
        menufileopen.setActionCommand("Open"); // Event trigger to change project path
        menufileopen.addActionListener(this);
        menufileopen.setIcon(iconOpen);
        menufile.add(menufileopen);
        
        JMenuItem menufileSave = new JMenuItem(McWBalance.langRB.getString("SAVE_PROJECT"), KeyEvent.VK_O);
        menufileSave.setActionCommand("Save"); // Event trigger to change project path
        menufileSave.addActionListener(this);
        menufileSave.setIcon(iconSave);
        menufile.add(menufileSave);
        
        JMenuItem menufileSaveAs = new JMenuItem(McWBalance.langRB.getString("SAVE_PROJECT_AS"), KeyEvent.VK_O);
        menufileSaveAs.setActionCommand("SaveAs"); // Event trigger to change project path
        menufileSaveAs.addActionListener(this);
        menufileSaveAs.setIcon(iconSaveAs);
        menufile.add(menufileSaveAs);
        
        JMenuItem menufileProjSettings = new JMenuItem(McWBalance.langRB.getString("PROJECT_SETTINGS"), KeyEvent.VK_S);
        menufileProjSettings.setActionCommand("PSettings"); // Event trigger to change project path
        menufileProjSettings.addActionListener(this);
        menufileProjSettings.setIcon(iconSettings);
        menufile.add(menufileProjSettings);
        
        menubar.add(menufile);
        //End of File Menu

        //Start of Edit Menu
        JMenu menuedit = new JMenu(McWBalance.langRB.getString("EDIT"));
        menuedit.setMnemonic(KeyEvent.VK_E);
        
        JMenuItem menueditdelete = new JMenuItem(McWBalance.langRB.getString("DELETE_OBJECT"), KeyEvent.VK_D);
        menueditdelete.setActionCommand("DeleteObj");
        menueditdelete.addActionListener(this);
        menueditdelete.setIcon(iconDelete);
        menuedit.add(menueditdelete);
 
        JMenuItem menueditaddELM = new JMenuItem(McWBalance.langRB.getString("ADD_NODE"), KeyEvent.VK_L);
        menueditaddELM.setActionCommand("AddObjELM");
        menueditaddELM.addActionListener(this);
        menueditaddELM.setIcon(iconNewELM);
        menuedit.add(menueditaddELM);
        
        JMenuItem menueditaddTRN = new JMenuItem(McWBalance.langRB.getString("ADD_TRANSFER"), KeyEvent.VK_T);
        menueditaddTRN.setActionCommand("AddObjTRN");
        menueditaddTRN.addActionListener(this);
        menueditaddTRN.setIcon(iconNewTRN);
        menuedit.add(menueditaddTRN);
        
        menubar.add(menuedit);
        //End of Edit Menu
        
         // Start of Solver Menu
        JMenu menusolve = new JMenu(McWBalance.langRB.getString("CALCULATION_SETTINGS"));
        menufile.setMnemonic(KeyEvent.VK_S);
        
        JMenuItem menuclimate = new JMenuItem(McWBalance.langRB.getString("CLIMATE"), KeyEvent.VK_C); 
        menuclimate.setActionCommand("ClimateSetting"); // Event trigger to make new project
        menuclimate.addActionListener(this);
        menusolve.add(menuclimate);
        
        JMenuItem menuRunoffCoefficients = new JMenuItem(McWBalance.langRB.getString("RUNOFF_COEFFICIENTS"), KeyEvent.VK_O); 
        menuRunoffCoefficients.setActionCommand("RunoffCoefficientsWindow"); // Event trigger to make new project
        menuRunoffCoefficients.addActionListener(this);
        menusolve.add(menuRunoffCoefficients);
        
        JMenuItem menuSolveOrder = new JMenuItem(McWBalance.langRB.getString("SOLVE_ORDER"), KeyEvent.VK_O); 
        menuSolveOrder.setActionCommand("SolveOrderWindow"); // Event trigger to make new project
        menuSolveOrder.addActionListener(this);
        menusolve.add(menuSolveOrder); 
        
        JMenuItem menusolvesall = new JMenuItem(McWBalance.langRB.getString("SOLVE"), KeyEvent.VK_S); 
        menusolvesall.setActionCommand("Solve"); // Event trigger to make new project
        menusolvesall.addActionListener(this);
        menusolve.add(menusolvesall);
        
        menubar.add(menusolve);
        //End of Solver Menu
        
        this.setJMenuBar(menubar);
        // End of Overall MenuBar
        
        flowChartPanel.setBackground(Preferences.DEFAULT_BACKGROUND_COLOR);
        flowChartPanel.addMouseListener(this);
        flowChartPanel.addMouseMotionListener(this); // added to allow for moving of Object
        // keyboard binding - Doesn't work yet, suspect because focus isn;t pulled away from jSPinner
 
        flowChartPanel.setPreferredSize(new Dimension((int)(FlowChartCAD.CAD_AREA_WIDTH*FlowChartCAD.zoomscale),(int)(FlowChartCAD.CAD_AREA_HEIGHT*FlowChartCAD.zoomscale)));
        flowChartPanel.add(flowchart); // does not seem to work
      
        flowChartScPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        flowChartScPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        flowChartScPane.addMouseWheelListener(this);
        flowChartScPane.setWheelScrollingEnabled(false);
        flowChartPanel.setFocusable(true);
        //listens for delete key (note that Ctrl is considered a modifier so cant seem to listen for it alone
        flowChartPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(127, 0), "pressDEL");
        flowChartPanel.getActionMap().put("pressDEL", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                flowchart.deleteSelection();
                flowchart.repaint();
            }
        });
        flowChartPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(67, 128), "pressCtrlC");
        flowChartPanel.getActionMap().put("pressCtrlC", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                flowchart.copySelectiontoClipboard();
            }
        });
        flowChartPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(86, 128), "pressCtrlV");
        flowChartPanel.getActionMap().put("pressCtrlV", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                flowchart.pasteFromClipBoard();
                flowchart.repaint();
            }
        });
        
        // View Toolbar
        // Zoom Selector
        JToolBar toolbarView = new JToolBar();
        JPanel toolbarViewpanel = new JPanel();
        JSpinner zoomSpinner = new JSpinner(zoomSpinnerModel);
        zoomSpinner.setMaximumSize(new Dimension(50, 30));
        zoomSpinner.addChangeListener(e->{
            FlowChartCAD.zoomscale = (double)zoomSpinner.getValue();
            flowChartPanel.setPreferredSize(new Dimension((int)(FlowChartCAD.CAD_AREA_WIDTH*FlowChartCAD.zoomscale),(int)(FlowChartCAD.CAD_AREA_HEIGHT*FlowChartCAD.zoomscale)));
            flowChartPanel.repaint();
        });
        JLabel zoomSpinnerLabel = new JLabel(McWBalance.langRB.getString("ZOOM"));
        toolbarViewpanel.add(zoomSpinnerLabel);
        toolbarViewpanel.add(zoomSpinner);
        
        //Date Selector
        SpinnerModel dateSpinnerModel = new SpinnerNumberModel(-1,-1,Limit.MAX_DURATION,1);
        JSpinner dateSpinner = new JSpinner(dateSpinnerModel);
        dateSpinner.setMaximumSize(new Dimension(50, 30));
        dateSpinner.addChangeListener(e->{
            FlowChartCAD.drawdate = (int)dateSpinner.getValue();
            flowChartPanel.repaint();
        });
        JLabel dateSpinnerLabel = new JLabel(McWBalance.langRB.getString("MODEL_DAY"));
        toolbarViewpanel.add(dateSpinnerLabel);
        toolbarViewpanel.add(dateSpinner);
       
        toolbarView.add(toolbarViewpanel);
        toolbarView.setFloatable(true);
        
        this.setLayout(new BorderLayout());
        this.add(toolbarView, BorderLayout.NORTH);
        this.add(flowChartScPane, BorderLayout.CENTER); // adds the FlowChart graphics area to the Frame
        this.validate(); // done because set visible is before the menu additions
        this.setIconImage(McWBalance.mainIcon30);
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        //mainframe.repaint(); not needed unless a refesh is required
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "New" -> {
                if (ProjSetting.hasChangedSinceSave) {
                    ConfirmSaveDialog dialog = new ConfirmSaveDialog(this, projSetting);
                    dialog.addWindowListener(new WindowAdapter() {
                        @Override
                        @SuppressWarnings("empty-statement")
                        public void windowClosed(WindowEvent e) {
                            switch (dialog.getSelection()) {
                                case ConfirmSaveDialog.SELECTION_SAVE ->
                                    saveProject();
                                case ConfirmSaveDialog.SELECTION_NOSAVE ->
                                    resetProject();
                            };
                        }
                    });
                } else {
                    resetProject();
                }
            }
            case "Open" -> {

                ProjOpenExistingWindow projOpenExistingWindow = new ProjOpenExistingWindow(this, flowchart, projSetting);
                flowchart.repaint();
            }
            case "Save" -> {
                saveProject();
            }
            case "PSettings" -> {
                ProjSettingWindow projSettingWindow = new ProjSettingWindow(this,projSetting);
            }
            case "AddObjELM" -> {
                requestedAction = "AddObjELM";
            }
            case "AddObjTRN" -> {
                requestedAction = "AddObjTRN";
            }
            case "DeleteObj" -> {
                requestedAction = "DeleteObj";
            }
            case "ClimateSetting" -> {
                JFrame climateSettingWindow = new DataClimateSettingWindow(this,projSetting);
            }
            case "SolveOrderWindow" -> {
                JFrame solveOrderWindow = new SolveOrderWindow(this, flowchart, projSetting);
            }
            case "RunoffCoefficientsWindow" ->{
                JFrame runoffCoefficientWindow = new RunoffCoefficientWindow(this);
            }
            
            
            case "solve" ->{
                //TO DO

            }
            
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        int mx = (int) (me.getX() / FlowChartCAD.zoomscale);
        int my = (int) (me.getY() / FlowChartCAD.zoomscale);
        int mELMHit;
        int mTRNHit;
        flowChartPanel.requestFocusInWindow(); // need to set focus away from spinner so that keyboard commands can work in window
        if (me.getButton() == MouseEvent.BUTTON1) { // Left Click Action Menu
            switch (requestedAction) {
                case "AddObjELM" -> {
                    requestedAction = "None"; //resets menu selection
                    flowchart.addObjELM(mx, my);
                }
                case "AddObjTRN" -> {
                    requestedAction = "None"; //resets menu selection
                    flowchart.addObjTRN(mx, my);
                }
                case "DeleteObj" -> {
                    requestedAction = "None"; // assumes object hit. on miss will need to re-try
                    // Note TRNS should be first, since they will draw on top. 
                    mTRNHit = flowchart.checkTRNHit(mx, my);
                    flowchart.removeTRN(mTRNHit);
                    if (mTRNHit == -1) {
                        mELMHit = flowchart.checkELMHit(mx, my);
                        flowchart.removeELM(mELMHit);
                    }
                }
                default -> {
                    mTRNHit = flowchart.checkTRNHit(mx, my);
                    mELMHit = flowchart.checkELMHit(mx, my);
                    if (mTRNHit != -1) {
                        if (flowchart.checkSelectionTRN(mTRNHit)) {
                            if (!editorIsActive) { 
                                
                                TRNWindow objTRNWindow = new TRNWindow(this, flowchart.getObjTRN(mTRNHit), flowchart.getNodeList().getNameList());
                                objTRNWindow.addWindowListener(new WindowAdapter(){
                                    @Override
                                    public void windowClosed(WindowEvent e){
                                        //NOT CLEAR HOW DATA IS TRANSFERED FROM buffOBJ into the active list. 
                                        //will want to add a step here to confirm changes and set has changed flag                                     
                                    }     
                                });
                            }
                            break;
                        } else {
                            flowchart.clearSelection();
                            flowchart.addSelectionTRN(mTRNHit);
                        }
                    } else if (mELMHit != -1) {
                        if (flowchart.checkSelectionELM(mELMHit)) {
                            if (!editorIsActive) {
                                NodeWindow objELMWindow = new NodeWindow();
                                objELMWindow.ObjELMWindowFunct(flowchart.getObjELM(mELMHit), mELMHit, flowchart.getTRNList(), projSetting);
                            }
                            break;
                        } else {
                            flowchart.clearSelection(); // should check if keypressed CTRL, requires implmenting a seperate Key listener and flag.. 
                            flowchart.addSelectionELM(mELMHit);
                        }
                    } else {
                        flowchart.clearSelection();
                    }
                }




            }
            flowChartPanel.repaint();
        }
  
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int mx = (int)(me.getX() / FlowChartCAD.zoomscale); // re-uses same variable names as above, but varables are not the same..
        int my = (int)(me.getY() / FlowChartCAD.zoomscale);
        
        switch (me.getButton()) {
            case MouseEvent.BUTTON1 -> {
                switch (requestedAction) {
                    case "None" -> { // if no other action is pending then Move is possible
                        tRNOnTheMove = flowchart.checkTRNHit(mx, my); // Sets the moving object to whatever is hit
                        if (tRNOnTheMove == -1) {
                            nodeOnTheMove = flowchart.checkELMHit(mx, my); // Transfers can move if Larger object isn't moving
                        }
                    }

                }
            }
            case MouseEvent.BUTTON2 ->{
                isViewPanning = true; 
                viewX = flowChartScPane.getHorizontalScrollBar().getValue();
                viewY = flowChartScPane.getVerticalScrollBar().getValue();
                startMouseX = me.getX();
                startMouseY = me.getY();
            }
        }
          //blank for now  
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        switch (me.getButton()) {
            case MouseEvent.BUTTON1 -> {
                nodeOnTheMove = -1;
                tRNOnTheMove = -1; // Halts movement of any object
            }
            case MouseEvent.BUTTON2 ->{
                isViewPanning = false;
            }
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
        if (nodeOnTheMove > -1){
            flowchart.moveObjELM(mx, my, nodeOnTheMove);
            flowChartPanel.repaint(); 
        }
        else if (tRNOnTheMove > -1){
            flowchart.moveObjTRN(mx, my, tRNOnTheMove);
            flowChartPanel.repaint();
        }
        else if (isViewPanning){
            int mouseMoveX = mme.getX() - startMouseX;
            int mouseMoveY = mme.getY() - startMouseY;
            int panX = viewX - (int)(mouseMoveX*PAN_SPEED);
            if (panX < 0){
                panX = 0;
            }
            else if (panX > flowChartScPane.getHorizontalScrollBar().getMaximum()){
                panX = flowChartScPane.getHorizontalScrollBar().getMaximum();
            }
            flowChartScPane.getHorizontalScrollBar().setValue(panX);
            int panY = viewY - (int)(mouseMoveY*PAN_SPEED);
            if (panY < 0){
                panY = 0;
            }
            else if (panY > flowChartScPane.getVerticalScrollBar().getMaximum()){
                panY = flowChartScPane.getVerticalScrollBar().getMaximum();
            }
            flowChartScPane.getVerticalScrollBar().setValue(panY);
            
        }
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe){
        if(mwe.isControlDown()){
            if(mwe.getWheelRotation() < 0){
                FlowChartCAD.zoomscale = FlowChartCAD.zoomscale + ZOOM_STEP;
                if (FlowChartCAD.zoomscale > ZOOM_MAX){
                    FlowChartCAD.zoomscale = ZOOM_MAX;
                }
            }
            else if(mwe.getWheelRotation() > 0){
                FlowChartCAD.zoomscale = FlowChartCAD.zoomscale - ZOOM_STEP;
                if (FlowChartCAD.zoomscale < ZOOM_MIN){
                    FlowChartCAD.zoomscale = ZOOM_MIN;
                }
            }
            zoomSpinnerModel.setValue(FlowChartCAD.zoomscale);
            
            flowchart.repaint();
        }
    }
    
    public void resetProject(){
        
        
        projSetting = new ProjSetting();
        flowchart.setTRNList(new TRNList());
        flowchart.setNodeList(new NodeList(projSetting));
        flowchart.repaint();
    }
        
    public void saveProject() {
        StringBuilder sverInfoFile = new StringBuilder();
        sverInfoFile.append(ProjSetting.verInfo);
        try (FileOutputStream sfileos = new FileOutputStream(projSetting.getSaveFile());
            ZipOutputStream sfilezos = new ZipOutputStream(sfileos)){

            // initializes ZipEntry info for files to include
            ZipEntry zEntVersion = new ZipEntry("Version.ver");

            ZipEntry zEntTRN[] = new ZipEntry[flowchart.getTRNList().count];
            for (int i = 0; i < flowchart.getTRNList().count; i++) {
                zEntTRN[i] = new ZipEntry(i + ".trn");
            }
            ZipEntry zEntELM[] = new ZipEntry[flowchart.getNodeList().count];
            for (int i = 0; i < flowchart.getNodeList().count; i++) {
                zEntELM[i] = new ZipEntry(i + ".elm");
                
            }

            // Write Version Information
            sfilezos.putNextEntry(zEntVersion);
            byte[] bytedata = sverInfoFile.toString().getBytes(); // converts string data to byte data
            sfilezos.write(bytedata, 0, bytedata.length);
            sfilezos.closeEntry();

            // Write Transfer Information
            for (int i = 0; i < zEntTRN.length; i++) {
                sfilezos.putNextEntry(zEntTRN[i]);
                bytedata = flowchart.getTRNList().tRNs[i].getSaveString().toString().getBytes(); // converts string data to byte data // byte data variable re-used
                sfilezos.write(bytedata, 0, bytedata.length);
                sfilezos.closeEntry();
            }

            // Write ElementInformation
            for (int i = 0; i < zEntELM.length; i++) {
                sfilezos.putNextEntry(zEntELM[i]);
                bytedata = flowchart.getNodeList().nodes[i].getSaveString().toString().getBytes(); // converts string data to byte data // byte data variable re-used
                sfilezos.write(bytedata, 0, bytedata.length);
                sfilezos.closeEntry();
            }

            //writeToZipFile()
            sfilezos.close();
            sfileos.close();
            ProjSetting.hasChangedSinceSave = false; // Debug - does not confirm successful save
            new WarningDialog(this, McWBalance.langRB.getString("SAVE_SUCCESSFUL"));
        } catch (FileNotFoundException fe) {
            new WarningDialog(this, fe.getMessage());
        } catch (IOException fe) {
            new WarningDialog(this, fe.getMessage());
        }

    }

            
            
            
            
}
