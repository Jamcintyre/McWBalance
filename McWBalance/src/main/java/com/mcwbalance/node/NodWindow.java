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

package com.mcwbalance.node;

import com.mcwbalance.generics.DataComboBoxModel;
import com.mcwbalance.generics.DataNameListModel;
import com.mcwbalance.MainWindow;
import com.mcwbalance.McWBalance;
import com.mcwbalance.generics.ObjStateTableModel;
import com.mcwbalance.transfer.TRNList;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.dacapacity.DACWindow;
import com.mcwbalance.project.Project;
import com.mcwbalance.settings.Limit;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.table.TableColumn;

/**
 * This window class allows user input into an individual ELM object. The class is intended to allow manual input of 
 * all information needed to populate an ELM with exception of location.
 * 
 * TODO - remove the buffernode and allow popups to directly update data. end goal is to allow 
 * on the fly adjusments to the balance for trial and error of pumping rates etc.. no need to
 * save and fire a change
 * @author amcintyre
 */
public class NodWindow extends JFrame {

    private Project aP; 
    private Nod node;
    private int nodeIndex;
    private JTabbedPane tabPane;
     
    //Formatting 
    /**
     * padding relative to left side of window
     */
    int hPadLabels;
    /**
     * padding relative to left side of window
     */
    int hPadBoxes;
    /**
     * sets default spacing between objects
     */
    int hPadSpacing;
    /**
     * sets padding for initial row relative top of window
     */
    int vPadFirstRow;
    /**
     * sets spacing between rows
     */
    int vPadSpacing;
    /**
     * sets spacing between groups of info
     */
    int vPadNextGroup;

    
    /**
     * This function calls on the JDialog window needed for user input of all information needed to populate an ELM
     * @param owner window called from to allow always on top
     * @param nodeIndex Index number of the ELM object so that object can be re-inserted into ELMList
     * @param aP used for managing general project details such as save location
     * 
     */
    public NodWindow(JFrame owner, int nodeIndex, Project aP){ // requires object number to edit
        super();
        this.aP = aP;
        this.node = aP.nODEList.get(nodeIndex);
        this.nodeIndex = nodeIndex;
        updateTitle();
        
        ProjSetting.hasChangedSinceSave = true; // assumes if this window was opened then a change occured
        
        MainWindow.editorIsActive = true; 
      
        hPadLabels = 10;
        hPadBoxes = 100;
        hPadSpacing =10;
        vPadFirstRow = 10;
        vPadSpacing = 10;
        vPadNextGroup = 20;
        
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we){
                MainWindow.editorIsActive = false; // free up the editor flag so that another window can be called
            }    
        });
        
        this.setSize(350, 200);
        this.setLocationRelativeTo(owner);
                
        // Tab 6 // All of this should go away and Node just carry an instance of the table model
        JPanel tab6 = new JPanel();
        ObjStateTableModel tmState = new ObjStateTableModel();
        //tab6TableModelState.setBlankFirstRow(); // sets up a blank first row to ensure classes are set properly
        JTable tState = new JTable(tmState);
        for (int i = 0; i < Limit.MAX_STATES; i++) {
            tState.setValueAt((int) node.stateTime[i], i, 0);
            tmState.setValueAt((String) node.state[i], i, 1);
        }
        for (int i = 0; i < tmState.getRowCount(); i++) {
            if (i >= node.stateTime.length) {
                break;
            }
            node.stateTime[i] = (int) tmState.getValueAt(i, 0);
            node.state[i] = (String) tmState.getValueAt(i, 1);
        }

        tabPane = new JTabbedPane();
        tabPane.addTab("General",makeTabGeneral());
        tabPane.addTab("State", makeTabTableState(tState));
        tabPane.addTab("FlowSettings",makeTabFlowSettings());
        tabPane.addTab("T.O.V.", makeTabLevelTable("Target Operating Volume", node.targetOperatingVol));
        tabPane.addTab("M.W.D.", makeTabLevelTable("Minimum Water Depth", node.minDepth));
        tabPane.addTab("M.O.L.", makeTabLevelTable("Maximum Operating Level", node.maxOpLevel));
        tabPane.addTab("Spill", makeTabLevelTable("Overflow Level", node.overflowLevel));
        tabPane.addTab("Crest", makeTabLevelTable("Crest Level", node.crestLevel));
        tabPane.addTab("Basin Catch", makeTabTableCatchment(new JLabel("Basin Catchment Area"), node.catchmentBasin));
        tabPane.addTab("Upstream Catch", makeTabTableCatchment(new JLabel("Upstream Catchment Area"), node.catchmentUpstream));
        tabPane.addTab("Tailings Solids Deposition", makeTabTailingsDeposition(node.depositionRates));
        tabPane.addTab("Object State", tab6);

        this.add(tabPane);

        this.pack();
        this.setBackground(Color.GRAY);
        this.setAlwaysOnTop(true);
        this.setVisible(true);

    }
    
    
    private JPanel makeTabLevelTable(String label, TableNodeLevel tableModel){
        
        JTable table = new JTable(tableModel);
        
        GridBagLayout layout = new GridBagLayout(); // sets up a layout manager for the tab
        JPanel tab = new JPanel(layout);
        Insets insets = new Insets(0,10,0,10); // Padding around Tables
        int prefCol0Width = 100; // width of Day column
        int prefCol1Width = 150; // width of Day column
        int prefCol2Width = 400; // width of Day column
        Dimension tableDims = new Dimension(650,400); // Prefered dimension of tables
        // layout constraints 
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = 0;  
        GridBagConstraints gbcTable = new GridBagConstraints();
        gbcTable.gridx = 0;
        gbcTable.gridy = 1;
        gbcTable.insets = insets;
       
        // TODO ADD POPUP MEMU  -------------------------------------------------------------------------------------!!!
        table.getColumnModel().getColumn(0).setPreferredWidth(prefCol0Width);
        table.getColumnModel().getColumn(1).setPreferredWidth(prefCol1Width);
        table.getColumnModel().getColumn(2).setPreferredWidth(prefCol2Width);
        
        //table.getColumnModel().getColumn(2).setPreferredWidth(prefCol2Width);
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(tableDims);
        
        
        tab.add (new JLabel(label), gbcLabel);
        tab.add(sp, gbcTable);
        
        
        return tab;
    }
    
    /**
     * TAB 1 USED FOR BASIC ELEMENT INFORMATION 
     * @return 
     */
    private JPanel makeTabGeneral(){
        
        JPanel tab1 = new JPanel();
        SpringLayout layoutTab1 = new SpringLayout(); // sets up a layout manager for the first tab
        tab1.setLayout(layoutTab1);

        JLabel ltfobjName = new JLabel("Name");
        JTextField tfobjName = new JTextField(node.objname);
        tfobjName.setColumns(20); // Sets Width if Name Field
        
        JLabel lcbobjType = new JLabel ("Type");
        JComboBox cbobjType = new JComboBox(Nod.objSubTypesAllowed); // Pulls options list from ObjELM static
        cbobjType.setSelectedItem(node.objSubType);
        
        SpinnerModel tab1scaleXSpinnerModel = new SpinnerNumberModel(node.scaleX,.05,5,.05);
        JSpinner tab1scaleXSpinner = new JSpinner(tab1scaleXSpinnerModel);
        tab1scaleXSpinner.setMaximumSize(new Dimension(50, 30));
        tab1scaleXSpinner.addChangeListener(e->{
            node.scaleX = (double)tab1scaleXSpinner.getValue();
        });
        JLabel tab1scaleXSpinnerLabel = new JLabel("Horizontal Scale");
        SpinnerModel tab1scaleYSpinnerModel = new SpinnerNumberModel(node.scaleY,.05,5,.05);
        JSpinner tab1scaleYSpinner = new JSpinner(tab1scaleYSpinnerModel);
        tab1scaleYSpinner.setMaximumSize(new Dimension(50, 30));
        tab1scaleYSpinner.addChangeListener(e->{
            node.scaleY = (double)tab1scaleYSpinner.getValue();
        });
        JLabel tab1scaleYSpinnerLabel = new JLabel("Vertical Scale");
        
        JCheckBox tab1CheckBoxhasCatchment = new JCheckBox("Include Catchment Area");
        tab1CheckBoxhasCatchment.setSelected(node.hasCatchment);
        tab1CheckBoxhasCatchment.addActionListener(e-> {
            node.hasCatchment = tab1CheckBoxhasCatchment.isSelected();
        });
        
        JCheckBox tab1CheckBoxhasSolids = new JCheckBox("Include Solids Deposition");
        tab1CheckBoxhasSolids.setSelected(node.hasSolids);
        tab1CheckBoxhasSolids.addActionListener(e-> {
            node.hasSolids = tab1CheckBoxhasSolids.isSelected();
        });
        JLabel tab1LabelVoidsXoSet = new JLabel("Flowbox Offset Horz:  ");
        SpinnerModel tab1SpinnerModelVoidsXoSet = new SpinnerNumberModel(node.oSetXVoids, -100, 100, 5);
        JSpinner tab1SpinnerVoidsXoSet = new JSpinner(tab1SpinnerModelVoidsXoSet);
        tab1SpinnerVoidsXoSet.addChangeListener(e->{
            node.oSetXVoids = (int)tab1SpinnerVoidsXoSet.getValue();
        });
        JLabel tab1LabelVoidsYoSet = new JLabel("Vert:");
        SpinnerModel tab1SpinnerModelVoidsYoSet = new SpinnerNumberModel(node.oSetYVoids, -100, 100, 5);
        JSpinner tab1SpinnerVoidsYoSet = new JSpinner(tab1SpinnerModelVoidsYoSet);
        tab1SpinnerVoidsYoSet.addChangeListener(e->{
            node.oSetYVoids = (int)tab1SpinnerVoidsXoSet.getValue();
        });
                
        JCheckBox tab1CheckBoxhasStorage = new JCheckBox("Include Depth Area Capacity");
        tab1CheckBoxhasStorage.setSelected(node.hasStorage);
        tab1CheckBoxhasStorage.addActionListener(e-> {
            node.hasStorage = tab1CheckBoxhasStorage.isSelected();
        });
        
        JButton tab1ButtonDACWindow = new JButton("Show DAC");
        tab1ButtonDACWindow.addActionListener(e -> {
            DACWindow dACWindow = new DACWindow(this, node.dAC);
        });

        
        
        JCheckBox tab1CheckBoxshowStorage = new JCheckBox("Show Net Storage on Flowsheet");
        tab1CheckBoxshowStorage.setSelected(node.showStorage);
        tab1CheckBoxshowStorage.addActionListener(e-> {
            node.showStorage = tab1CheckBoxshowStorage.isSelected();
        });
        JLabel tab1LabelStorageXoSet = new JLabel("Flowbox Offset Horz:  ");
        SpinnerModel tab1SpinnerModelStorageXoSet = new SpinnerNumberModel(node.oSetXStorage, -100, 100, 5);
        JSpinner tab1SpinnerStorageXoSet = new JSpinner(tab1SpinnerModelStorageXoSet);
        tab1SpinnerStorageXoSet.addChangeListener(e->{
            node.oSetXStorage = (int)tab1SpinnerStorageXoSet.getValue();
        });
        JLabel tab1LabelStorageYoSet = new JLabel("Vert:");
        SpinnerModel tab1SpinnerModelStorageYoSet = new SpinnerNumberModel(node.oSetYStorage, -100, 100, 5);
        JSpinner tab1SpinnerStorageYoSet = new JSpinner(tab1SpinnerModelStorageYoSet);
        tab1SpinnerStorageYoSet.addChangeListener(e->{
            node.oSetYStorage = (int)tab1SpinnerStorageXoSet.getValue();
        });
        
        JCheckBox tab1CheckBoxhasStorageEvapandPrecip = new JCheckBox("Include Direct Evaporation and Precipitation");
        tab1CheckBoxhasStorageEvapandPrecip.setSelected(node.hasStorageEvapandPrecip);
        tab1CheckBoxhasStorageEvapandPrecip.addActionListener(e-> {
            node.hasStorageEvapandPrecip = tab1CheckBoxhasStorageEvapandPrecip.isSelected();
        });
        JButton bSave = new JButton("Save");
        bSave.addActionListener(e ->{ // uses a lambda instead of needing seperate listener override
            node.objname = tfobjName.getText();
            node.setSubType(String.valueOf(cbobjType.getSelectedItem()), "ACTIVE", aP);
            
            this.updateTitle();
            this.fireChartUpdate();

/*
            node.targetOperatingVol.setAllData(tmTargetVol.getDayColumn(), tmTargetVol.getVolColumn()); // problem seems to lie here
            
            node.minDepth.setAllData(tmMinDepth.getDayColumn(), tmMinDepth.getLevelColumn()); 

            node.minDepth.setAllData(tmMaxOpLevel.getDayColumn(), tmMaxOpLevel.getLevelColumn()); 
            
            node.overflowLevel.setAllData(tmOverflowLevel.getDayColumn(), tmOverflowLevel.getLevelColumn());
            node.crestLevel.setAllData(tmCrestLevel.getDayColumn(), tmCrestLevel.getLevelColumn());
  */          
            



            
            //returnedObjELM = node; // sets returned value to 
        });
        
        tfobjName.getText(); // not sure what this is doing DELETE? 
        
        tab1.add(ltfobjName);
        tab1.add(tfobjName);
        tab1.add(lcbobjType);
        tab1.add(cbobjType);
        
        tab1.add(tab1scaleXSpinner);
        tab1.add(tab1scaleXSpinnerLabel);
        tab1.add(tab1scaleYSpinner);
        tab1.add(tab1scaleYSpinnerLabel);
        
        
        tab1.add(tab1CheckBoxhasCatchment);
        tab1.add(tab1CheckBoxhasSolids);

        tab1.add(tab1LabelVoidsXoSet);
        tab1.add(tab1SpinnerVoidsXoSet);
        tab1.add(tab1LabelVoidsYoSet);
        tab1.add(tab1SpinnerVoidsYoSet);
        
        tab1.add(tab1CheckBoxhasStorage);
        tab1.add(tab1CheckBoxshowStorage);
        
        tab1.add(tab1ButtonDACWindow);
        
        tab1.add(tab1LabelStorageXoSet);
        tab1.add(tab1SpinnerStorageXoSet);
        tab1.add(tab1LabelStorageYoSet);
        tab1.add(tab1SpinnerStorageYoSet);
          
        
        tab1.add(tab1CheckBoxhasStorageEvapandPrecip);
        tab1.add(bSave);
        

        layoutTab1.putConstraint(SpringLayout.WEST, ltfobjName, hPadLabels, SpringLayout.WEST, this);
        layoutTab1.putConstraint(SpringLayout.NORTH, ltfobjName, vPadFirstRow, SpringLayout.NORTH, this);
        
        layoutTab1.putConstraint(SpringLayout.WEST, tfobjName, hPadBoxes, SpringLayout.WEST, this); // aligns horizontal relative to side of window
        layoutTab1.putConstraint(SpringLayout.NORTH, tfobjName, 0, SpringLayout.NORTH, ltfobjName); // aligns vertical with lable
        
        layoutTab1.putConstraint(SpringLayout.WEST, lcbobjType, hPadLabels, SpringLayout.WEST, this);
        layoutTab1.putConstraint(SpringLayout.NORTH, lcbobjType, vPadSpacing, SpringLayout.SOUTH, ltfobjName);
        
        layoutTab1.putConstraint(SpringLayout.WEST, cbobjType, hPadBoxes, SpringLayout.WEST, this);
        layoutTab1.putConstraint(SpringLayout.NORTH, cbobjType, 0, SpringLayout.NORTH, lcbobjType);
        
        layoutTab1.putConstraint(SpringLayout.WEST, tab1scaleXSpinnerLabel, hPadLabels, SpringLayout.WEST, this);
        layoutTab1.putConstraint(SpringLayout.NORTH, tab1scaleXSpinnerLabel, vPadSpacing, SpringLayout.SOUTH, lcbobjType);
        layoutTab1.putConstraint(SpringLayout.WEST, tab1scaleXSpinner, hPadLabels, SpringLayout.EAST, tab1scaleXSpinnerLabel);
        layoutTab1.putConstraint(SpringLayout.NORTH, tab1scaleXSpinner, 0, SpringLayout.NORTH, tab1scaleXSpinnerLabel);
        
        layoutTab1.putConstraint(SpringLayout.WEST, tab1scaleYSpinnerLabel, hPadLabels, SpringLayout.WEST, this);
        layoutTab1.putConstraint(SpringLayout.NORTH, tab1scaleYSpinnerLabel, vPadSpacing, SpringLayout.SOUTH, tab1scaleXSpinnerLabel);
        layoutTab1.putConstraint(SpringLayout.WEST, tab1scaleYSpinner, hPadLabels, SpringLayout.EAST, tab1scaleYSpinnerLabel);
        layoutTab1.putConstraint(SpringLayout.NORTH, tab1scaleYSpinner, 0, SpringLayout.NORTH, tab1scaleYSpinnerLabel);
        
        layoutTab1.putConstraint(SpringLayout.WEST, tab1CheckBoxhasCatchment, hPadLabels, SpringLayout.WEST, this);
        layoutTab1.putConstraint(SpringLayout.NORTH, tab1CheckBoxhasCatchment, vPadSpacing, SpringLayout.SOUTH, tab1scaleYSpinnerLabel); 
        
        layoutTab1.putConstraint(SpringLayout.WEST, tab1CheckBoxhasSolids, hPadLabels, SpringLayout.WEST, this);
        layoutTab1.putConstraint(SpringLayout.NORTH, tab1CheckBoxhasSolids, vPadSpacing, SpringLayout.SOUTH, tab1CheckBoxhasCatchment); 
        
        layoutTab1.putConstraint(SpringLayout.WEST, tab1LabelVoidsXoSet, hPadLabels,SpringLayout.EAST, tab1CheckBoxhasSolids);
        layoutTab1.putConstraint(SpringLayout.VERTICAL_CENTER, tab1LabelVoidsXoSet, 0, SpringLayout.VERTICAL_CENTER, tab1CheckBoxhasSolids);
        layoutTab1.putConstraint(SpringLayout.WEST, tab1SpinnerVoidsXoSet, hPadLabels,SpringLayout.EAST, tab1LabelVoidsXoSet);
        layoutTab1.putConstraint(SpringLayout.VERTICAL_CENTER, tab1SpinnerVoidsXoSet, 0,SpringLayout.VERTICAL_CENTER, tab1LabelVoidsXoSet);

        layoutTab1.putConstraint(SpringLayout.WEST, tab1LabelVoidsYoSet, hPadLabels,SpringLayout.EAST, tab1SpinnerVoidsXoSet);
        layoutTab1.putConstraint(SpringLayout.VERTICAL_CENTER, tab1LabelVoidsYoSet, 0,SpringLayout.VERTICAL_CENTER, tab1SpinnerVoidsXoSet);
        layoutTab1.putConstraint(SpringLayout.WEST, tab1SpinnerVoidsYoSet, hPadLabels,SpringLayout.EAST, tab1LabelVoidsYoSet);
        layoutTab1.putConstraint(SpringLayout.VERTICAL_CENTER, tab1SpinnerVoidsYoSet, 0,SpringLayout.VERTICAL_CENTER, tab1LabelVoidsYoSet);
                
        layoutTab1.putConstraint(SpringLayout.WEST, tab1CheckBoxhasStorage, hPadLabels, SpringLayout.WEST, this);
        layoutTab1.putConstraint(SpringLayout.NORTH, tab1CheckBoxhasStorage, vPadSpacing, SpringLayout.SOUTH, tab1CheckBoxhasSolids); 
        
        layoutTab1.putConstraint(SpringLayout.WEST, tab1ButtonDACWindow, hPadLabels, SpringLayout.EAST, tab1CheckBoxhasStorage);
        layoutTab1.putConstraint(SpringLayout.VERTICAL_CENTER, tab1ButtonDACWindow, 0, SpringLayout.VERTICAL_CENTER, tab1CheckBoxhasStorage); 
        
        layoutTab1.putConstraint(SpringLayout.WEST, tab1CheckBoxshowStorage, hPadLabels, SpringLayout.WEST, this);
        layoutTab1.putConstraint(SpringLayout.NORTH, tab1CheckBoxshowStorage, vPadSpacing, SpringLayout.SOUTH, tab1CheckBoxhasStorage); 
        
        layoutTab1.putConstraint(SpringLayout.WEST, tab1LabelStorageXoSet, hPadLabels,SpringLayout.EAST, tab1CheckBoxshowStorage);
        layoutTab1.putConstraint(SpringLayout.VERTICAL_CENTER, tab1LabelStorageXoSet, 0, SpringLayout.VERTICAL_CENTER, tab1CheckBoxshowStorage);
        layoutTab1.putConstraint(SpringLayout.WEST, tab1SpinnerStorageXoSet, hPadLabels,SpringLayout.EAST, tab1LabelStorageXoSet);
        layoutTab1.putConstraint(SpringLayout.VERTICAL_CENTER, tab1SpinnerStorageXoSet, 0,SpringLayout.VERTICAL_CENTER, tab1LabelStorageXoSet);

        layoutTab1.putConstraint(SpringLayout.WEST, tab1LabelStorageYoSet, hPadLabels,SpringLayout.EAST, tab1SpinnerStorageXoSet);
        layoutTab1.putConstraint(SpringLayout.VERTICAL_CENTER, tab1LabelStorageYoSet, 0,SpringLayout.VERTICAL_CENTER, tab1SpinnerStorageXoSet);
        layoutTab1.putConstraint(SpringLayout.WEST, tab1SpinnerStorageYoSet, hPadLabels,SpringLayout.EAST, tab1LabelStorageYoSet);
        layoutTab1.putConstraint(SpringLayout.VERTICAL_CENTER, tab1SpinnerStorageYoSet, 0,SpringLayout.VERTICAL_CENTER, tab1LabelStorageYoSet);
        
        layoutTab1.putConstraint(SpringLayout.WEST, tab1CheckBoxhasStorageEvapandPrecip, hPadLabels, SpringLayout.WEST, this);
        layoutTab1.putConstraint(SpringLayout.NORTH, tab1CheckBoxhasStorageEvapandPrecip, vPadSpacing, SpringLayout.SOUTH, tab1CheckBoxshowStorage); 
        
        layoutTab1.putConstraint(SpringLayout.WEST, bSave, hPadLabels, SpringLayout.WEST, this);
        layoutTab1.putConstraint(SpringLayout.NORTH, bSave, vPadSpacing, SpringLayout.SOUTH, tab1CheckBoxhasStorageEvapandPrecip);

        return tab1;
    }
    
    private JPanel makeTabFlowSettings(){
       // TAB 2 for handelling Analysis Solve Order
       JPanel tab2;
       SpringLayout layoutTab2;

        JLabel tab2lableFixedInputs;
        JLabel tab2lableInputs;
        JLabel tab2lableDemandInputs;
        JLabel tab2lableFixedOutputs;
        JLabel tab2lableOutputs;
        JLabel tab2lableDemandOutputs;
        JLabel tab2lableOverflow;
    
        DataNameListModel tab2LModelFixedInputs;
        JList tab2listFixedInputs;
        DataNameListModel tab2LModelInputs;
        JList tab2listInputs;
        DataNameListModel tab2LModelDemandInputs;
        JList tab2listDemandInputs;
        DataNameListModel tab2LModelFixedOutputs;
        JList tab2listFixedOutputs;
        DataNameListModel tab2LModelOutputs;
        JList tab2listOutputs;
        DataNameListModel tab2LModelDemandOutputs;
        JList tab2listDemandOutputs;
    //experementing, may not be able ot use the same type of data model for comboBoxes... 
        DataComboBoxModel tab2ComboModelOverflow;
       
       
       
        tab2 = new JPanel();
        layoutTab2 = new SpringLayout(); // sets up a layout manager for the tab
        tab2.setLayout(layoutTab2);
        
        tab2lableFixedInputs = new JLabel("Fixed Inputs");
        tab2lableInputs = new JLabel("Inputs");
        tab2lableDemandInputs = new JLabel("On Demand Inputs");
        tab2lableFixedOutputs = new JLabel("Fixed Outputs");
        tab2lableOutputs = new JLabel("Outputs");
        tab2lableDemandOutputs = new JLabel("On Demand Outputs");
        tab2lableOverflow = new JLabel("Overflow");
        
        // Sets initial Data; intent s to taeke the full list and trim down every time. 
        
        
        TRNList ctRNList = aP.tRNList;
        node.inflows.overwriteList(ctRNList.getInflowNameListIndex(nodeIndex,false), ctRNList.getInflowNameList(nodeIndex,false));
        node.inflows.trimFromList(node.inflowFixedTRN.getShortIndexList());
        node.inflows.trimFromList(node.inflowOnDemandTRN.getShortIndexList());
        node.inflowFixedTRN.setNames(ctRNList.getNameList(node.inflowFixedTRN.getShortIndexList()));
        node.inflowOnDemandTRN.setNames(ctRNList.getNameList(node.inflowOnDemandTRN.getShortIndexList()));
        
        node.outflows.overwriteList(ctRNList.getOutflowNameListIndex(nodeIndex,false), ctRNList.getOutflowNameList(nodeIndex,false));
        node.outflows.trimFromList(node.outflowFixedTRN.getShortIndexList());
        node.outflows.trimFromList(node.outflowOnDemandTRN.getShortIndexList());
        node.outflows.trimFromList(node.overflowTRN);
        
        // overflow Options built independently from outflows since i believe the actionlistener somehow skips back to be beginning of the class, but debug breaks don't catch it...
        node.overflowOptions.overwriteList(ctRNList.getOutflowNameListIndex(nodeIndex,false), ctRNList.getOutflowNameList(nodeIndex,false));
        node.overflowOptions.trimFromList(node.outflowFixedTRN.getShortIndexList());
        node.overflowOptions.trimFromList(node.outflowOnDemandTRN.getShortIndexList());     
       
        node.outflowFixedTRN.setNames(ctRNList.getNameList(node.outflowFixedTRN.getShortIndexList()));
        node.outflowOnDemandTRN.setNames(ctRNList.getNameList(node.outflowOnDemandTRN.getShortIndexList()));
   
        tab2LModelFixedInputs = new DataNameListModel();
        tab2LModelFixedInputs.setAllData(node.inflowFixedTRN.getShortNameList());
        tab2listFixedInputs = new JList(tab2LModelFixedInputs);
        
        tab2LModelInputs = new DataNameListModel();
        tab2LModelInputs.setAllData(node.inflows.getShortNameList());
        tab2listInputs = new JList(tab2LModelInputs);
        
        tab2LModelDemandInputs = new DataNameListModel();
        tab2LModelDemandInputs.setAllData(node.inflowOnDemandTRN.getShortNameList());
        tab2listDemandInputs = new JList(tab2LModelDemandInputs);
        
        tab2LModelFixedOutputs = new DataNameListModel();
        tab2LModelFixedOutputs.setAllData(node.outflowFixedTRN.getShortNameList());
        tab2listFixedOutputs = new JList(tab2LModelFixedOutputs);
        
        tab2LModelOutputs = new DataNameListModel();
        tab2LModelOutputs.setAllData(node.outflows.getShortNameList());
        tab2listOutputs = new JList(tab2LModelOutputs);
        
        tab2LModelDemandOutputs = new DataNameListModel();
        tab2LModelDemandOutputs.setAllData(node.outflowOnDemandTRN.getShortNameList());
        tab2listDemandOutputs = new JList(tab2LModelDemandOutputs);
        
        //experementing, may not be able ot use the same type of data model for comboBoxes... 
        tab2ComboModelOverflow = new DataComboBoxModel();
        tab2ComboModelOverflow.setAllData(node.overflowOptions.getShortNameListWithNone());
        
        
        Dimension tab2PrefListDim = new Dimension(150,150); // sets the size of the lists
        
        JScrollPane tab2SPFixedInputs = new JScrollPane(tab2listFixedInputs);
        tab2SPFixedInputs.setPreferredSize(tab2PrefListDim);
        JScrollPane tab2SPInputs = new JScrollPane(tab2listInputs);
        tab2SPInputs.setPreferredSize(tab2PrefListDim);
        JScrollPane tab2SPDemandInputs = new JScrollPane(tab2listDemandInputs);
        tab2SPDemandInputs.setPreferredSize(tab2PrefListDim);
        JScrollPane tab2SPFixedOutputs = new JScrollPane(tab2listFixedOutputs);
        tab2SPFixedOutputs.setPreferredSize(tab2PrefListDim);
        JScrollPane tab2SPOutputs = new JScrollPane(tab2listOutputs);
        tab2SPOutputs.setPreferredSize(tab2PrefListDim);
        JScrollPane tab2SPDemandOutputs = new JScrollPane(tab2listDemandOutputs);
        tab2SPDemandOutputs.setPreferredSize(tab2PrefListDim);
        
        JButton tab2buttonAddFixedInput = new JButton("<");
        tab2buttonAddFixedInput.addActionListener(e -> {
            int tRNIndex = node.inflows.getObjIndex(tab2listInputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            node.inflows.trimFromList(tRNIndex);
            tab2LModelInputs.setAllData(node.inflows.getShortNameList());
            node.inflowFixedTRN.appendToList(tRNIndex, tRNName);
            tab2LModelFixedInputs.setAllData(node.inflowFixedTRN.getShortNameList());
        });
        JButton tab2buttonRemFixedInput = new JButton(">");
        tab2buttonRemFixedInput.addActionListener(e -> {
            int tRNIndex = node.inflowFixedTRN.getObjIndex(tab2listFixedInputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            node.inflows.appendToList(tRNIndex, tRNName);
            tab2LModelInputs.setAllData(node.inflows.getShortNameList());
            node.inflowFixedTRN.trimFromList(tRNIndex);
            tab2LModelFixedInputs.setAllData(node.inflowFixedTRN.getShortNameList());
        });
        JButton tab2buttonAddDemandInput = new JButton(">");
        tab2buttonAddDemandInput.addActionListener(e -> {
            int tRNIndex = node.inflows.getObjIndex(tab2listInputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            node.inflows.trimFromList(tRNIndex);
            tab2LModelInputs.setAllData(node.inflows.getShortNameList());
            node.inflowOnDemandTRN.appendToList(tRNIndex, tRNName);
            tab2LModelDemandInputs.setAllData(node.inflowOnDemandTRN.getShortNameList());

        });
        JButton tab2buttonRemDemandInput = new JButton("<");
        tab2buttonRemDemandInput.addActionListener(e -> {
            int tRNIndex = node.inflowOnDemandTRN.getObjIndex(tab2listDemandInputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            node.inflows.appendToList(tRNIndex, tRNName);
            tab2LModelInputs.setAllData(node.inflows.getShortNameList());
            node.inflowOnDemandTRN.trimFromList(tRNIndex);
            tab2LModelDemandInputs.setAllData(node.inflowOnDemandTRN.getShortNameList());
        });
        JButton tab2buttonUpDemandInput = new JButton("UP");
        tab2buttonUpDemandInput.addActionListener(e -> {
            node.inflowOnDemandTRN.shiftUp(tab2listDemandInputs.getSelectedIndex());
            tab2LModelDemandInputs.setAllData(node.inflowOnDemandTRN.getShortNameList());
        });
        JButton tab2buttonDownDemandInput = new JButton("DOWN");
        tab2buttonDownDemandInput.addActionListener(e -> {
            node.inflowOnDemandTRN.shiftDown(tab2listDemandInputs.getSelectedIndex());
            tab2LModelDemandInputs.setAllData(node.inflowOnDemandTRN.getShortNameList());
        });
        JButton tab2buttonAddFixedOutput = new JButton("<");
        tab2buttonAddFixedOutput.addActionListener(e -> {
            int tRNIndex = node.outflows.getObjIndex(tab2listOutputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            node.outflows.trimFromList(tRNIndex);
            tab2LModelOutputs.setAllData(node.outflows.getShortNameList());
            node.outflowFixedTRN.appendToList(tRNIndex, tRNName);
            tab2LModelFixedOutputs.setAllData(node.outflowFixedTRN.getShortNameList());
        });
        JButton tab2buttonRemFixedOutput = new JButton(">");
        tab2buttonRemFixedOutput.addActionListener(e -> {
            int tRNIndex = node.outflowFixedTRN.getObjIndex(tab2listFixedOutputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            node.outflows.appendToList(tRNIndex, tRNName);
            tab2LModelOutputs.setAllData(node.outflows.getShortNameList());
            node.outflowFixedTRN.trimFromList(tRNIndex);
            tab2LModelFixedOutputs.setAllData(node.outflowFixedTRN.getShortNameList());
        });
        JButton tab2buttonAddDemandOutput = new JButton(">");
        tab2buttonAddDemandOutput.addActionListener(e -> {
            int tRNIndex = node.outflows.getObjIndex(tab2listOutputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            node.outflows.trimFromList(tRNIndex);
            tab2LModelOutputs.setAllData(node.outflows.getShortNameList());
            node.outflowOnDemandTRN.appendToList(tRNIndex, tRNName);
            tab2LModelDemandOutputs.setAllData(node.outflowOnDemandTRN.getShortNameList());
        });
        JButton tab2buttonRemDemandOutput = new JButton("<");
        tab2buttonRemDemandOutput.addActionListener(e -> {
            int tRNIndex = node.outflowOnDemandTRN.getObjIndex(tab2listDemandOutputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            node.outflows.appendToList(tRNIndex, tRNName);
            tab2LModelOutputs.setAllData(node.outflows.getShortNameList());
            node.outflowOnDemandTRN.trimFromList(tRNIndex);
            tab2LModelDemandOutputs.setAllData(node.outflowOnDemandTRN.getShortNameList());
        });    
        JButton tab2buttonUpDemandOutput = new JButton("UP");
        tab2buttonUpDemandOutput.addActionListener(e -> {
            node.outflowOnDemandTRN.shiftUp(tab2listDemandOutputs.getSelectedIndex());
            tab2LModelDemandOutputs.setAllData(node.outflowOnDemandTRN.getShortNameList());
        });
        JButton tab2buttonDownDemandOutput = new JButton("DOWN");
        tab2buttonDownDemandOutput.addActionListener(e -> {
            node.outflowOnDemandTRN.shiftDown(tab2listDemandOutputs.getSelectedIndex());
            tab2LModelDemandOutputs.setAllData(node.outflowOnDemandTRN.getShortNameList());
        });
        
        JComboBox tab2comboOverflow = new JComboBox(tab2ComboModelOverflow);
        tab2comboOverflow.setSelectedIndex(node.overflowOptions.getListIndex(node.overflowTRN)+1);
        tab2comboOverflow.addActionListener(e-> {
            if( node.overflowTRN != node.overflowOptions.getObjIndex(tab2comboOverflow.getSelectedIndex()-1)){ // confirms selection actually changed
                if(node.overflowTRN > -1){
                    node.outflows.appendToList(node.overflowTRN, ctRNList.tRNs[node.overflowTRN].objname);
                }
                System.out.println("Object Index " + node.overflowOptions.getObjIndex(tab2comboOverflow.getSelectedIndex()) + " minus 1 Selected");
                node.overflowTRN = node.overflowOptions.getObjIndex(tab2comboOverflow.getSelectedIndex()-1);
                if(node.overflowTRN > -1){ 
                    node.outflows.trimFromList(node.overflowTRN);
                }
                tab2LModelOutputs.setAllData(node.outflows.getShortNameList());
            }
        });

        tab2.add(tab2lableFixedInputs);
        tab2.add(tab2lableInputs);
        tab2.add(tab2lableDemandInputs);
        tab2.add(tab2lableFixedOutputs);
        tab2.add(tab2lableOutputs);
        tab2.add(tab2lableDemandOutputs);
        tab2.add(tab2lableOverflow );
        
        tab2.add(tab2SPFixedInputs);
        tab2.add(tab2SPInputs);
        tab2.add(tab2SPDemandInputs);
        tab2.add(tab2SPFixedOutputs);
        tab2.add(tab2SPOutputs);
        tab2.add(tab2SPDemandOutputs);
        
        tab2.add(tab2buttonAddFixedInput);
        tab2.add(tab2buttonRemFixedInput);
        tab2.add(tab2buttonAddDemandInput);
        tab2.add(tab2buttonRemDemandInput);  
        tab2.add(tab2buttonUpDemandInput);
        tab2.add(tab2buttonDownDemandInput);
        
        tab2.add(tab2buttonAddFixedOutput);
        tab2.add(tab2buttonRemFixedOutput);
        tab2.add(tab2buttonAddDemandOutput);
        tab2.add(tab2buttonRemDemandOutput); 
        tab2.add(tab2buttonUpDemandOutput);
        tab2.add(tab2buttonDownDemandOutput);
        
        tab2.add(tab2comboOverflow );
        
        
        // layout of Input selection boxes
        layoutTab2.putConstraint(SpringLayout.WEST, tab2lableFixedInputs, hPadLabels, SpringLayout.WEST, this);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2lableFixedInputs, vPadFirstRow, SpringLayout.NORTH, this);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2SPFixedInputs, hPadLabels, SpringLayout.WEST, this);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2SPFixedInputs, vPadSpacing, SpringLayout.SOUTH, tab2lableFixedInputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2buttonAddFixedInput, hPadLabels, SpringLayout.EAST, tab2SPFixedInputs);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2buttonAddFixedInput, 0, SpringLayout.NORTH, tab2SPFixedInputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2buttonRemFixedInput, hPadLabels, SpringLayout.EAST, tab2SPFixedInputs);
        layoutTab2.putConstraint(SpringLayout.SOUTH, tab2buttonRemFixedInput, 0, SpringLayout.SOUTH, tab2SPFixedInputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2SPInputs, hPadLabels, SpringLayout.EAST, tab2buttonAddFixedInput);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2SPInputs, 0, SpringLayout.NORTH, tab2SPFixedInputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2lableInputs, 0, SpringLayout.WEST, tab2SPInputs);
        layoutTab2.putConstraint(SpringLayout.SOUTH, tab2lableInputs, -vPadSpacing, SpringLayout.NORTH, tab2SPInputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2buttonAddDemandInput, hPadLabels, SpringLayout.EAST, tab2SPInputs);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2buttonAddDemandInput, 0, SpringLayout.NORTH, tab2buttonAddFixedInput);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2buttonRemDemandInput, hPadLabels, SpringLayout.EAST, tab2SPInputs);
        layoutTab2.putConstraint(SpringLayout.SOUTH, tab2buttonRemDemandInput, 0, SpringLayout.SOUTH, tab2buttonRemFixedInput);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2SPDemandInputs, hPadLabels, SpringLayout.EAST, tab2buttonAddDemandInput);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2SPDemandInputs, 0, SpringLayout.NORTH, tab2SPFixedInputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2lableDemandInputs, 0, SpringLayout.WEST, tab2SPDemandInputs);
        layoutTab2.putConstraint(SpringLayout.SOUTH, tab2lableDemandInputs, -vPadSpacing, SpringLayout.NORTH, tab2SPDemandInputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2buttonUpDemandInput, hPadLabels, SpringLayout.EAST, tab2SPDemandInputs);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2buttonUpDemandInput, 0, SpringLayout.NORTH, tab2buttonAddFixedInput);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2buttonDownDemandInput, hPadLabels, SpringLayout.EAST, tab2SPDemandInputs);
        layoutTab2.putConstraint(SpringLayout.SOUTH, tab2buttonDownDemandInput, 0, SpringLayout.SOUTH, tab2buttonRemFixedInput);
        
        // layout of Output selection boxes
        layoutTab2.putConstraint(SpringLayout.WEST, tab2lableFixedOutputs, hPadLabels, SpringLayout.WEST, this);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2lableFixedOutputs, vPadNextGroup, SpringLayout.SOUTH, tab2SPFixedInputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2SPFixedOutputs, hPadLabels, SpringLayout.WEST, this);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2SPFixedOutputs, vPadSpacing, SpringLayout.SOUTH, tab2lableFixedOutputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2buttonAddFixedOutput, hPadLabels, SpringLayout.EAST, tab2SPFixedOutputs);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2buttonAddFixedOutput, 0, SpringLayout.NORTH, tab2SPFixedOutputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2buttonRemFixedOutput, hPadLabels, SpringLayout.EAST, tab2SPFixedOutputs);
        layoutTab2.putConstraint(SpringLayout.SOUTH, tab2buttonRemFixedOutput, 0, SpringLayout.SOUTH, tab2SPFixedOutputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2SPOutputs, hPadLabels, SpringLayout.EAST, tab2buttonAddFixedOutput);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2SPOutputs, 0, SpringLayout.NORTH, tab2SPFixedOutputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2lableOutputs, 0, SpringLayout.WEST, tab2SPOutputs);
        layoutTab2.putConstraint(SpringLayout.SOUTH, tab2lableOutputs, -vPadSpacing, SpringLayout.NORTH, tab2SPOutputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2buttonAddDemandOutput, hPadLabels, SpringLayout.EAST, tab2SPOutputs);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2buttonAddDemandOutput, 0, SpringLayout.NORTH, tab2buttonAddFixedOutput);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2buttonRemDemandOutput, hPadLabels, SpringLayout.EAST, tab2SPOutputs);
        layoutTab2.putConstraint(SpringLayout.SOUTH, tab2buttonRemDemandOutput, 0, SpringLayout.SOUTH, tab2buttonRemFixedOutput);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2SPDemandOutputs, hPadLabels, SpringLayout.EAST, tab2buttonAddDemandOutput);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2SPDemandOutputs, 0, SpringLayout.NORTH, tab2SPFixedOutputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2lableDemandOutputs, 0, SpringLayout.WEST, tab2SPDemandOutputs);
        layoutTab2.putConstraint(SpringLayout.SOUTH, tab2lableDemandOutputs, -vPadSpacing, SpringLayout.NORTH, tab2SPDemandOutputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2buttonUpDemandOutput, hPadLabels, SpringLayout.EAST, tab2SPDemandOutputs);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2buttonUpDemandOutput, 0, SpringLayout.NORTH, tab2buttonAddFixedOutput);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2buttonDownDemandOutput, hPadLabels, SpringLayout.EAST, tab2SPDemandOutputs);
        layoutTab2.putConstraint(SpringLayout.SOUTH, tab2buttonDownDemandOutput, 0, SpringLayout.SOUTH, tab2buttonRemFixedOutput);
        
        // layout of Overflow
        layoutTab2.putConstraint(SpringLayout.WEST, tab2lableOverflow, hPadLabels, SpringLayout.WEST, this);
        layoutTab2.putConstraint(SpringLayout.NORTH, tab2lableOverflow, vPadNextGroup, SpringLayout.SOUTH, tab2SPFixedOutputs);
        
        layoutTab2.putConstraint(SpringLayout.WEST, tab2comboOverflow, hPadSpacing, SpringLayout.EAST, tab2lableOverflow);
        layoutTab2.putConstraint(SpringLayout.VERTICAL_CENTER, tab2comboOverflow, 0, SpringLayout.VERTICAL_CENTER, tab2lableOverflow);
        
        return tab2;
    }
    
    private JPanel makeTabTableCatchment(JLabel label, TableCatchment tableModel) {
        
        JTable table = new JTable(tableModel);
        
        JPopupMenu popupMenu = new JPopupMenu();
        
        ArrayList<JMenuItem> popupMenuItems = new ArrayList<>();
        popupMenuItems.add(new JMenuItem(McWBalance.langRB.getString("ADD_ROW")));
        popupMenuItems.get(0).addActionListener(e -> {
            tableModel.addRow();
        });
        popupMenuItems.add(new JMenuItem(McWBalance.langRB.getString("DELETE_ROW")));
        popupMenuItems.get(1).addActionListener(e -> {
            tableModel.deleteRow(table.getSelectedRow());
        });
        popupMenuItems.add(new JMenuItem(McWBalance.langRB.getString("DELETE_UNUSED_LAND_COVER")));
        popupMenuItems.get(2).addActionListener(e -> {
            tableModel.deleteUnusedLandCovers();
        });
        
        String addL = McWBalance.langRB.getString("ADD_LAND_COVER") + ": ";
        for (int lc = 0; lc < aP.runoffCoeffs.getRowCount(); ++lc){
            String name = String.valueOf(aP.runoffCoeffs.getValueAt(lc, 0));
            JMenuItem menuItem = new JMenuItem(addL+name);
            menuItem.addActionListener(e ->{
                tableModel.addLandCover(name);
            });
            popupMenuItems.add(menuItem);
        }
        
        for (int mi = 0; mi < popupMenuItems.size(); ++mi){
            popupMenu.add(popupMenuItems.get(mi));
        }

        table.setComponentPopupMenu(popupMenu);


        JButton buttonAdd = new JButton(McWBalance.langRB.getString("ADD_ROW"));
        buttonAdd.addActionListener(e -> {
            tableModel.addRow();
        }); 
        
        JButton buttonDelete = new JButton(McWBalance.langRB.getString("DELETE_ROW"));
        buttonDelete.addActionListener(e -> {
            tableModel.deleteRow(table.getSelectedRow());
        }); 
        
        //Layout 
        GridBagLayout layout = new GridBagLayout(); // sets up a layout manager for the tab
        JPanel tab = new JPanel(layout);
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 1;
        gbcLabel.gridy = 0;
        GridBagConstraints gbcTable = new GridBagConstraints();
        gbcTable.gridx = 1;
        gbcTable.gridy = 1;
        gbcTable.insets = new Insets(0, 10, 0, 10); // Padding around Tables;
        GridBagConstraints gbcButtonAdd = new GridBagConstraints();
        gbcButtonAdd.gridx = 0;
        gbcButtonAdd.gridy = 1;
        GridBagConstraints gbcButtonDelete = new GridBagConstraints();
        gbcButtonDelete.gridx = 0;
        gbcButtonDelete.gridy = 2;

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(650, 200));

        tab.add(label, gbcLabel);
        tab.add(sp, gbcTable);
        tab.add(buttonAdd, gbcButtonAdd);
        tab.add(buttonDelete, gbcButtonDelete);

        return tab;
    }
    
    
    private JPanel makeTabTailingsDeposition(TableTailingsDepositionRates tableModel){
        GridBagLayout layout = new GridBagLayout(); // sets up a layout manager for the tab
        JPanel tab = new JPanel(layout);
        
        JTable table = new JTable(tableModel);
        
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem popupMenuItemTailsSelectAll = new JMenuItem("Select All");
        popupMenuItemTailsSelectAll.addActionListener(e->{
                table.setColumnSelectionInterval(0, 8);
                table.setRowSelectionInterval(0, table.getRowCount()-1);
        });
        JMenuItem popupMenuItemTailsDelete = new JMenuItem("Delete Selection");
        popupMenuItemTailsDelete.addActionListener(e->{
                tableModel.removeData(table.getSelectedRows(),table.getSelectedColumns());
        });
        JMenuItem popupMenuItemTailsCopy = new JMenuItem("Copy All");
        popupMenuItemTailsCopy.addActionListener(e->{
                tableModel.copyToClipBoard();
        });

        JMenuItem popupMenuItemTailsPaste = new JMenuItem("Paste All");
        popupMenuItemTailsPaste.addActionListener(e->{
                tableModel.pasteFromClipboard(table.getSelectedRows(),table.getSelectedColumns());
        });
        
        popupMenu.add(popupMenuItemTailsSelectAll);
        popupMenu.add(popupMenuItemTailsDelete);
        popupMenu.add(popupMenuItemTailsCopy);
        popupMenu.add(popupMenuItemTailsPaste);
        table.setComponentPopupMenu(popupMenu);
        table.setCellSelectionEnabled(true);
        

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800,190));
        tab.add(scrollPane);
        return tab;
    }
    
    /**
     * Used for handellign the Object State Table (i.e. under construction
     * half filled, filled) 
     * @param label
     * @param table Model must be castable to ObjStateTableModel
     * @return 
     */
    private JPanel makeTabTableState(JTable table){
        JPanel tab = new JPanel();

        JPopupMenu popupMenuStateTable = new JPopupMenu();
        JMenuItem popupMenuItemStateSelectAll = new JMenuItem("Select All");
        popupMenuItemStateSelectAll.addActionListener(e->{
                table.setColumnSelectionInterval(0, 1);
                table.setRowSelectionInterval(0, table.getRowCount()-1);
        });
        JMenuItem popupMenuItemStateDelete = new JMenuItem("Delete Selection");
        popupMenuItemStateDelete.addActionListener(e->{
                ((ObjStateTableModel)table.getModel()).removeData(table.getSelectedRows(),table.getSelectedColumns());
        });
        JMenuItem popupMenuItemStateCopy = new JMenuItem("Copy (Not yet working use ctrl+C");
        popupMenuItemStateCopy.addActionListener(e ->{
                System.out.println("popup menu Copy button hit");
        });
        JMenuItem popupMenuItemStatePaste = new JMenuItem("Paste");
        popupMenuItemStatePaste.addActionListener(e -> {
            ((ObjStateTableModel)table.getModel()).pasteFromClipboard(table.getSelectedRows(), table.getSelectedColumns());
        });
        
        popupMenuStateTable.add(popupMenuItemStateSelectAll);
        popupMenuStateTable.add(popupMenuItemStateDelete);
        popupMenuStateTable.add(popupMenuItemStateCopy);
        popupMenuStateTable.add(popupMenuItemStatePaste);
        table.setComponentPopupMenu(popupMenuStateTable);
        table.setCellSelectionEnabled(true);
        
        JComboBox cBoxTRNState = new JComboBox(Nod.StatesAllowed);
        TableColumn stateColumn = table.getColumnModel().getColumn(1);
        stateColumn.setCellEditor(new DefaultCellEditor(cBoxTRNState));
        
        JScrollPane tab6ScrollState = new JScrollPane(table);
        tab.add(tab6ScrollState);
        return tab;
        
    }
    /**
     * Used for consistency in title update
     */
    private void updateTitle(){
        this.setTitle(aP.nODEList.get(nodeIndex).objname +" " + McWBalance.langRB.getString("PROPERTIES"));
    }
    /**
     * Used to request the aP to update the flowchart
     */
    private void fireChartUpdate(){
        aP.getFlowChart().repaint();
    }

}
