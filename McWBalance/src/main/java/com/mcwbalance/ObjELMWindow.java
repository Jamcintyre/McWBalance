// This class is for the popup window to edit an Element's properties

package com.mcwbalance;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
 * @author amcintyre
 */
public class ObjELMWindow extends JFrame { // implements ActionListener not needed if lamba is used
    //int objELMNumber = -1;
    /**
     * Container for storing and modifying element used for return at end
     * Setting to static does not fix the problem with action listener making a seperate Copy...
     */
    ObjELM buffObjELM = new ObjELM(); 
    /**
     * Container for allowing mainwindow access to returned value since return doesnt work in Jdialog
     */
    static ObjELM returnedObjELM = new ObjELM();
    
    
    /**
     * This function calls on the JDialog window needed for user input of all information needed to populate an ELM
     * @param inObjELM ELM object that will be edited by the user
     * @param eLMNumber Index number of the ELM object so that object can be re-inserted into ELMList
     * @param ctRNList Current TRN list must be provided to allow user to select from available TRNs for solve order planning
     * @return 
     */
    public ObjELM ObjELMWindowFunct(ObjELM inObjELM, int eLMNumber, ObjTRNList ctRNList){ // requires object number to edit
        //objELMNumber = inNumber; // sets value for save and loads, needs to be called external to this function in action listener
        MainWindow.editorIsActive = true; 
        buffObjELM = inObjELM; // sets buffered object to in object
        returnedObjELM = inObjELM; // sets default return to whatever was provided
      
        int hPadLabels = 10; // padding relative to left side of window
        int hPadBoxes = 100; // padding relative to left side of window
        int hPadSpacing =10; // sets default spacing between objects 
        int vPadFirstRow = 10; // sets padding for initial row relative top of window
        int vPadSpacing = 10; // sets spacing between rows
        int vPadNextGroup = 20; // sets spacing between grooups of info
        
        JFrame subframe = new JFrame("Element Properties"); // Changed back to frame, as was having problems with not being able to call a second frame
        subframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        subframe.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we){
                MainWindow.editorIsActive = false; // free up the editor flag so that another window can be called
                subframe.dispose(); // this is the actual closing of the window
            }    
        });
        
        
        subframe.setSize(350, 200);
        subframe.setLocationRelativeTo(null);
        
        JTabbedPane tabPane = new JTabbedPane();

        // TAB 2 for handelling Analysis Solve Order
        JPanel tab2 = new JPanel();
        SpringLayout layoutTab2 = new SpringLayout(); // sets up a layout manager for the tab
        tab2.setLayout(layoutTab2);
        
        JLabel tab2lableFixedInputs = new JLabel("Fixed Inputs");
        JLabel tab2lableInputs = new JLabel("Inputs");
        JLabel tab2lableDemandInputs = new JLabel("On Demand Inputs");
        JLabel tab2lableFixedOutputs = new JLabel("Fixed Outputs");
        JLabel tab2lableOutputs = new JLabel("Outputs");
        JLabel tab2lableDemandOutputs = new JLabel("On Demand Outputs");
        JLabel tab2lableOverflow = new JLabel("Overflow");
        
        // Sets initial Data; intent s to taeke the full list and trim down every time. 
        buffObjELM.inflows.overwriteList(ctRNList.getInflowNameListIndex(eLMNumber,false), ctRNList.getInflowNameList(eLMNumber,false));
        buffObjELM.inflows.trimFromList(buffObjELM.inflowFixedTRN.getShortIndexList());
        buffObjELM.inflows.trimFromList(buffObjELM.inflowOnDemandTRN.getShortIndexList());
        buffObjELM.inflowFixedTRN.setNames(ctRNList.getNameList(buffObjELM.inflowFixedTRN.getShortIndexList()));
        buffObjELM.inflowOnDemandTRN.setNames(ctRNList.getNameList(buffObjELM.inflowOnDemandTRN.getShortIndexList()));
        
        buffObjELM.outflows.overwriteList(ctRNList.getOutflowNameListIndex(eLMNumber,false), ctRNList.getOutflowNameList(eLMNumber,false));
        buffObjELM.outflows.trimFromList(buffObjELM.outflowFixedTRN.getShortIndexList());
        buffObjELM.outflows.trimFromList(buffObjELM.outflowOnDemandTRN.getShortIndexList());
        buffObjELM.outflows.trimFromList(buffObjELM.overflowTRN);
        
        // overflow Options built independently from outflows since i believe the actionlistener somehow skips back to be beginning of the class, but debug breaks don't catch it...
        buffObjELM.overflowOptions.overwriteList(ctRNList.getOutflowNameListIndex(eLMNumber,false), ctRNList.getOutflowNameList(eLMNumber,false));
        buffObjELM.overflowOptions.trimFromList(buffObjELM.outflowFixedTRN.getShortIndexList());
        buffObjELM.overflowOptions.trimFromList(buffObjELM.outflowOnDemandTRN.getShortIndexList());     
       
        buffObjELM.outflowFixedTRN.setNames(ctRNList.getNameList(buffObjELM.outflowFixedTRN.getShortIndexList()));
        buffObjELM.outflowOnDemandTRN.setNames(ctRNList.getNameList(buffObjELM.outflowOnDemandTRN.getShortIndexList()));
   
        DataNameListModel tab2LModelFixedInputs = new DataNameListModel();
        tab2LModelFixedInputs.setAllData(buffObjELM.inflowFixedTRN.getShortNameList());
        JList tab2listFixedInputs = new JList(tab2LModelFixedInputs);
        
        DataNameListModel tab2LModelInputs = new DataNameListModel();
        tab2LModelInputs.setAllData(buffObjELM.inflows.getShortNameList());
        JList tab2listInputs = new JList(tab2LModelInputs);
        
        DataNameListModel tab2LModelDemandInputs = new DataNameListModel();
        tab2LModelDemandInputs.setAllData(buffObjELM.inflowOnDemandTRN.getShortNameList());
        JList tab2listDemandInputs = new JList(tab2LModelDemandInputs);
        
        DataNameListModel tab2LModelFixedOutputs = new DataNameListModel();
        tab2LModelFixedOutputs.setAllData(buffObjELM.outflowFixedTRN.getShortNameList());
        JList tab2listFixedOutputs = new JList(tab2LModelFixedOutputs);
        
        DataNameListModel tab2LModelOutputs = new DataNameListModel();
        tab2LModelOutputs.setAllData(buffObjELM.outflows.getShortNameList());
        JList tab2listOutputs = new JList(tab2LModelOutputs);
        
        DataNameListModel tab2LModelDemandOutputs = new DataNameListModel();
        tab2LModelDemandOutputs.setAllData(buffObjELM.outflowOnDemandTRN.getShortNameList());
        JList tab2listDemandOutputs = new JList(tab2LModelDemandOutputs);
        
        //experementing, may not be able ot use the same type of data model for comboBoxes... 
        DataComboBoxModel tab2ComboModelOverflow = new DataComboBoxModel();
        tab2ComboModelOverflow.setAllData(buffObjELM.overflowOptions.getShortNameListWithNone());
        
        
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
            int tRNIndex = buffObjELM.inflows.getObjIndex(tab2listInputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffObjELM.inflows.trimFromList(tRNIndex);
            tab2LModelInputs.setAllData(buffObjELM.inflows.getShortNameList());
            buffObjELM.inflowFixedTRN.appendToList(tRNIndex, tRNName);
            tab2LModelFixedInputs.setAllData(buffObjELM.inflowFixedTRN.getShortNameList());
        });
        JButton tab2buttonRemFixedInput = new JButton(">");
        tab2buttonRemFixedInput.addActionListener(e -> {
            int tRNIndex = buffObjELM.inflowFixedTRN.getObjIndex(tab2listFixedInputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffObjELM.inflows.appendToList(tRNIndex, tRNName);
            tab2LModelInputs.setAllData(buffObjELM.inflows.getShortNameList());
            buffObjELM.inflowFixedTRN.trimFromList(tRNIndex);
            tab2LModelFixedInputs.setAllData(buffObjELM.inflowFixedTRN.getShortNameList());
        });
        JButton tab2buttonAddDemandInput = new JButton(">");
        tab2buttonAddDemandInput.addActionListener(e -> {
            int tRNIndex = buffObjELM.inflows.getObjIndex(tab2listInputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffObjELM.inflows.trimFromList(tRNIndex);
            tab2LModelInputs.setAllData(buffObjELM.inflows.getShortNameList());
            buffObjELM.inflowOnDemandTRN.appendToList(tRNIndex, tRNName);
            tab2LModelDemandInputs.setAllData(buffObjELM.inflowOnDemandTRN.getShortNameList());

        });
        JButton tab2buttonRemDemandInput = new JButton("<");
        tab2buttonRemDemandInput.addActionListener(e -> {
            int tRNIndex = buffObjELM.inflowOnDemandTRN.getObjIndex(tab2listDemandInputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffObjELM.inflows.appendToList(tRNIndex, tRNName);
            tab2LModelInputs.setAllData(buffObjELM.inflows.getShortNameList());
            buffObjELM.inflowOnDemandTRN.trimFromList(tRNIndex);
            tab2LModelDemandInputs.setAllData(buffObjELM.inflowOnDemandTRN.getShortNameList());

        });
        JButton tab2buttonUpDemandInput = new JButton("UP");
        tab2buttonUpDemandInput.addActionListener(e -> {
            buffObjELM.inflowOnDemandTRN.shiftUp(tab2listDemandInputs.getSelectedIndex());
            tab2LModelDemandInputs.setAllData(buffObjELM.inflowOnDemandTRN.getShortNameList());

        });
        JButton tab2buttonDownDemandInput = new JButton("DOWN");
        tab2buttonDownDemandInput.addActionListener(e -> {
            buffObjELM.inflowOnDemandTRN.shiftDown(tab2listDemandInputs.getSelectedIndex());
            tab2LModelDemandInputs.setAllData(buffObjELM.inflowOnDemandTRN.getShortNameList());

        });
        JButton tab2buttonAddFixedOutput = new JButton("<");
        tab2buttonAddFixedOutput.addActionListener(e -> {
            int tRNIndex = buffObjELM.outflows.getObjIndex(tab2listOutputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffObjELM.outflows.trimFromList(tRNIndex);
            tab2LModelOutputs.setAllData(buffObjELM.outflows.getShortNameList());
            buffObjELM.outflowFixedTRN.appendToList(tRNIndex, tRNName);
            tab2LModelFixedOutputs.setAllData(buffObjELM.outflowFixedTRN.getShortNameList());

        });
        JButton tab2buttonRemFixedOutput = new JButton(">");
        tab2buttonRemFixedOutput.addActionListener(e -> {
            int tRNIndex = buffObjELM.outflowFixedTRN.getObjIndex(tab2listFixedOutputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffObjELM.outflows.appendToList(tRNIndex, tRNName);
            tab2LModelOutputs.setAllData(buffObjELM.outflows.getShortNameList());
            buffObjELM.outflowFixedTRN.trimFromList(tRNIndex);
            tab2LModelFixedOutputs.setAllData(buffObjELM.outflowFixedTRN.getShortNameList());
        });
        JButton tab2buttonAddDemandOutput = new JButton(">");
        tab2buttonAddDemandOutput.addActionListener(e -> {
            int tRNIndex = buffObjELM.outflows.getObjIndex(tab2listOutputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffObjELM.outflows.trimFromList(tRNIndex);
            tab2LModelOutputs.setAllData(buffObjELM.outflows.getShortNameList());
            buffObjELM.outflowOnDemandTRN.appendToList(tRNIndex, tRNName);
            tab2LModelDemandOutputs.setAllData(buffObjELM.outflowOnDemandTRN.getShortNameList());
        });
        JButton tab2buttonRemDemandOutput = new JButton("<");
        tab2buttonRemDemandOutput.addActionListener(e -> {
            int tRNIndex = buffObjELM.outflowOnDemandTRN.getObjIndex(tab2listDemandOutputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffObjELM.outflows.appendToList(tRNIndex, tRNName);
            tab2LModelOutputs.setAllData(buffObjELM.outflows.getShortNameList());
            buffObjELM.outflowOnDemandTRN.trimFromList(tRNIndex);
            tab2LModelDemandOutputs.setAllData(buffObjELM.outflowOnDemandTRN.getShortNameList());
        });    
        JButton tab2buttonUpDemandOutput = new JButton("UP");
        tab2buttonUpDemandOutput.addActionListener(e -> {
            buffObjELM.outflowOnDemandTRN.shiftUp(tab2listDemandOutputs.getSelectedIndex());
            tab2LModelDemandOutputs.setAllData(buffObjELM.outflowOnDemandTRN.getShortNameList());
        });
        JButton tab2buttonDownDemandOutput = new JButton("DOWN");
        tab2buttonDownDemandOutput.addActionListener(e -> {
            buffObjELM.outflowOnDemandTRN.shiftDown(tab2listDemandOutputs.getSelectedIndex());
            tab2LModelDemandOutputs.setAllData(buffObjELM.outflowOnDemandTRN.getShortNameList());
        });
        
        JComboBox tab2comboOverflow = new JComboBox(tab2ComboModelOverflow);
        tab2comboOverflow.setSelectedIndex(buffObjELM.overflowOptions.getListIndex(buffObjELM.overflowTRN)+1);
        tab2comboOverflow.addActionListener(e-> {
            if( buffObjELM.overflowTRN != buffObjELM.overflowOptions.getObjIndex(tab2comboOverflow.getSelectedIndex()-1)){ // confirms selection actually changed
                if(buffObjELM.overflowTRN > -1){
                    buffObjELM.outflows.appendToList(buffObjELM.overflowTRN, ctRNList.tRNs[buffObjELM.overflowTRN].objname);
                }
                System.out.println("Object Index " + buffObjELM.overflowOptions.getObjIndex(tab2comboOverflow.getSelectedIndex()) + " minus 1 Selected");
                buffObjELM.overflowTRN = buffObjELM.overflowOptions.getObjIndex(tab2comboOverflow.getSelectedIndex()-1);
                if(buffObjELM.overflowTRN > -1){ 
                    buffObjELM.outflows.trimFromList(buffObjELM.overflowTRN);
                }
                tab2LModelOutputs.setAllData(buffObjELM.outflows.getShortNameList());
            }
            buffObjELM.overflowOptions.debugPrintIndexList();
            
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
        
        // TAB 3 for handelling Depth Area Capacity
        JPanel tab3 = new JPanel();
 
        DataDACTableModel tab3TableModelDAC = new DataDACTableModel();
        tab3TableModelDAC.setAllData(buffObjELM.dAC.elev, buffObjELM.dAC.area, buffObjELM.dAC.vol);
        JTable tab3TableDAC = new JTable(tab3TableModelDAC);
        
        JPopupMenu popupMenuDACTable = new JPopupMenu();
        JMenuItem popupMenuItemDACSelectAll = new JMenuItem("Select All");
        popupMenuItemDACSelectAll.addActionListener(e->{
                tab3TableDAC.setColumnSelectionInterval(0, 2);
                tab3TableDAC.setRowSelectionInterval(0, tab3TableDAC.getRowCount()-1);
        });
        JMenuItem popupMenuItemDACDelete = new JMenuItem("Delete Selection");
        popupMenuItemDACDelete.addActionListener(e->{
                tab3TableModelDAC.removeData(tab3TableDAC.getSelectedRows(),tab3TableDAC.getSelectedColumns());
        });
        JMenuItem popupMenuItemDACCopy = new JMenuItem("Copy (Not yet working use ctrl+C");
        popupMenuItemDACCopy.addActionListener(e->{
                System.out.println("popup menu Copy button hit");
        });

        JMenuItem popupMenuItemDACPaste = new JMenuItem("Paste (All 3 Columns El. A V)");
        popupMenuItemDACPaste.addActionListener(e->{
                tab3TableModelDAC.pasteFromClipboard(tab3TableDAC.getSelectedRows(),tab3TableDAC.getSelectedColumns());
        });
        
        popupMenuDACTable.add(popupMenuItemDACSelectAll);
        popupMenuDACTable.add(popupMenuItemDACDelete);
        popupMenuDACTable.add(popupMenuItemDACCopy);
        popupMenuDACTable.add(popupMenuItemDACPaste);
        tab3TableDAC.setComponentPopupMenu(popupMenuDACTable);
        tab3TableDAC.setCellSelectionEnabled(true);
        //tab3TableDAC.
        JScrollPane tab3ScrollDAC = new JScrollPane(tab3TableDAC);
        
        
        JButton tab3buttonPlotDAC = new JButton("Show DAC");
        tab3buttonPlotDAC.addActionListener(e -> {
            // save the current DAC to the buffer. this will result in an update to the ELM
            buffObjELM.dAC.setData(tab3TableModelDAC.getElevColumn(), tab3TableModelDAC.getAreaColumn(), tab3TableModelDAC.getVolColumn());
            DataDACPlotWIndow DACplotwindow = new DataDACPlotWIndow();
            DACplotwindow.plotWindow(buffObjELM.dAC);

        });
        
        
        tab3.add(tab3ScrollDAC);
        tab3.add(tab3buttonPlotDAC);

       // TAB 4 for handelling Operating Levels
        JPanel tab4 = new JPanel();
        GridBagLayout layoutTab4 = new GridBagLayout(); // sets up a layout manager for the tab
        
        Insets tab4insets = new Insets(0,10,0,10); // Padding around Tables
        int tab4PrefColCol0Width = 100; // width of Day column
        int tab4PrefColCol1Width = 150; // width of Day column
        Dimension tab4TableDims = new Dimension(250,400); // Prefered dimension of tables
        // layout constraints 
        GridBagConstraints tab4constrTargetVolLabel = new GridBagConstraints();
        tab4constrTargetVolLabel.gridx = 0;
        tab4constrTargetVolLabel.gridy = 0;  
        GridBagConstraints tab4constrTargetVol = new GridBagConstraints();
        tab4constrTargetVol.gridx = 0;
        tab4constrTargetVol.gridy = 1;
        tab4constrTargetVol.insets = tab4insets;
        GridBagConstraints tab4constrMinDepthLabel = new GridBagConstraints();
        tab4constrMinDepthLabel.gridx = 1;
        tab4constrMinDepthLabel.gridy = 0;
        GridBagConstraints tab4constrMinDepth = new GridBagConstraints();
        tab4constrMinDepth.gridx = 1;
        tab4constrMinDepth.gridy = 1;
        tab4constrMinDepth.insets = tab4insets;
        GridBagConstraints tab4constrMaxOpLevelLabel = new GridBagConstraints();
        tab4constrMaxOpLevelLabel.gridx = 2;
        tab4constrMaxOpLevelLabel.gridy = 0;
        GridBagConstraints tab4constrMaxOpLevel = new GridBagConstraints();
        tab4constrMaxOpLevel.gridx = 2;
        tab4constrMaxOpLevel.gridy = 1;
        tab4constrMaxOpLevel.insets = tab4insets;
        GridBagConstraints tab4constrOverflowLevelLabel = new GridBagConstraints();
        tab4constrOverflowLevelLabel.gridx = 3;
        tab4constrOverflowLevelLabel.gridy = 0;
        GridBagConstraints tab4constrOverflowLevel = new GridBagConstraints();
        tab4constrOverflowLevel.gridx = 3;
        tab4constrOverflowLevel.gridy = 1;
        tab4constrOverflowLevel.insets = tab4insets;
        GridBagConstraints tab4constrCrestLevelLabel = new GridBagConstraints();
        tab4constrCrestLevelLabel.gridx = 4;
        tab4constrCrestLevelLabel.gridy = 0;
        GridBagConstraints tab4constrCrestLevel = new GridBagConstraints();
        tab4constrCrestLevel.gridx = 4;
        tab4constrCrestLevel.gridy = 1;
        tab4constrCrestLevel.insets = tab4insets;
        tab4.setLayout(layoutTab4);
        // TABLE CONSTRUCTION 
        //tab4LabelTargetVol
        JLabel tab4LabelTargetVol = new JLabel("Target Operating Volume");
        ObjELMVolumeTableModel tab4TableModelTargetVol = new ObjELMVolumeTableModel();
        tab4TableModelTargetVol.setAllData(buffObjELM.targetOperatingVol.getDays(),buffObjELM.targetOperatingVol.getValues());
        JTable tab4TableTargetVol = new JTable(tab4TableModelTargetVol);
        // TODO ADD POPUP MEMU  -------------------------------------------------------------------------------------!!!
        tab4TableTargetVol.getColumnModel().getColumn(0).setPreferredWidth(tab4PrefColCol0Width);
        tab4TableTargetVol.getColumnModel().getColumn(1).setPreferredWidth(tab4PrefColCol1Width);
        JScrollPane tab4ScrollPaneTargetVol = new JScrollPane(tab4TableTargetVol);
        tab4ScrollPaneTargetVol.setPreferredSize(tab4TableDims);
        tab4.add (tab4LabelTargetVol, tab4constrTargetVolLabel);
        tab4.add(tab4ScrollPaneTargetVol, tab4constrTargetVol);
        //tab4TableMinDepth
        JLabel tab4LabelMinDepth = new JLabel("Minimum Water Depth");
        ObjELMLevelTableModel tab4TableModelMinDepth = new ObjELMLevelTableModel();
        tab4TableModelMinDepth.setAllData(buffObjELM.minDepth.getDays(),buffObjELM.minDepth.getValues());
        JTable tab4TableMinDepth = new JTable(tab4TableModelMinDepth);
        // TODO ADD POPUP MEMU  -------------------------------------------------------------------------------------!!!
        tab4TableMinDepth.getColumnModel().getColumn(0).setPreferredWidth(tab4PrefColCol0Width);
        tab4TableMinDepth.getColumnModel().getColumn(1).setPreferredWidth(tab4PrefColCol1Width);
        JScrollPane tab4ScrollPaneMinDepth = new JScrollPane(tab4TableMinDepth);
        tab4ScrollPaneMinDepth.setPreferredSize(tab4TableDims);
        tab4.add(tab4LabelMinDepth,tab4constrMinDepthLabel);
        tab4.add(tab4ScrollPaneMinDepth,tab4constrMinDepth);
        //tab4LabelMaxOpLevel
        JLabel tab4LabelMaxOpLevel = new JLabel("Maximum Operating Level");
        ObjELMLevelTableModel tab4TableModelMaxOpLevel = new ObjELMLevelTableModel();
        tab4TableModelMaxOpLevel.setAllData(buffObjELM.maxOpLevel.getDays(),buffObjELM.maxOpLevel.getValues());
        JTable tab4TableMaxOpLevel = new JTable(tab4TableModelMaxOpLevel);
        // TODO ADD POPUP MEMU  -------------------------------------------------------------------------------------!!!
        tab4TableMaxOpLevel.getColumnModel().getColumn(0).setPreferredWidth(tab4PrefColCol0Width);
        tab4TableMaxOpLevel.getColumnModel().getColumn(1).setPreferredWidth(tab4PrefColCol1Width);
        JScrollPane tab4ScrollPaneMaxOpLevel = new JScrollPane(tab4TableMaxOpLevel);
        tab4ScrollPaneMaxOpLevel.setPreferredSize(tab4TableDims);
        tab4.add(tab4LabelMaxOpLevel,tab4constrMaxOpLevelLabel);
        tab4.add(tab4ScrollPaneMaxOpLevel,tab4constrMaxOpLevel);
        //tab4TableModelOverflowLevel
        JLabel tab4LabelOverflowLevel = new JLabel("Overflow Level");
        ObjELMLevelTableModel tab4TableModelOverflowLevel = new ObjELMLevelTableModel();
        tab4TableModelOverflowLevel.setAllData(buffObjELM.overflowLevel.getDays(),buffObjELM.overflowLevel.getValues());
        JTable tab4TableOverflowLevel = new JTable(tab4TableModelOverflowLevel);
        // TODO ADD POPUP MEMU  -------------------------------------------------------------------------------------!!!
        tab4TableOverflowLevel.getColumnModel().getColumn(0).setPreferredWidth(tab4PrefColCol0Width);
        tab4TableOverflowLevel.getColumnModel().getColumn(1).setPreferredWidth(tab4PrefColCol1Width);
        JScrollPane tab4ScrollPaneOverflowLevel = new JScrollPane(tab4TableOverflowLevel);
        tab4ScrollPaneOverflowLevel.setPreferredSize(tab4TableDims);
        tab4.add(tab4LabelOverflowLevel,tab4constrOverflowLevelLabel);
        tab4.add(tab4ScrollPaneOverflowLevel,tab4constrOverflowLevel);
        //tab4TableModelCrestLevel
        JLabel tab4LabelCrestLevel = new JLabel("Crest Level");
        ObjELMLevelTableModel tab4TableModelCrestLevel = new ObjELMLevelTableModel();
        tab4TableModelCrestLevel.setAllData(buffObjELM.crestLevel.getDays(),buffObjELM.crestLevel.getValues());
        JTable tab4TableCrestLevel = new JTable(tab4TableModelCrestLevel);
        // TODO ADD POPUP MEMU  -------------------------------------------------------------------------------------!!!
        tab4TableCrestLevel.getColumnModel().getColumn(0).setPreferredWidth(tab4PrefColCol0Width);
        tab4TableCrestLevel.getColumnModel().getColumn(1).setPreferredWidth(tab4PrefColCol1Width);
        JScrollPane tab4ScrollPaneCrestLevel = new JScrollPane(tab4TableCrestLevel);
        tab4ScrollPaneCrestLevel.setPreferredSize(tab4TableDims);
        tab4.add(tab4LabelCrestLevel,tab4constrCrestLevelLabel);
        tab4.add(tab4ScrollPaneCrestLevel,tab4constrCrestLevel);

        // End of Tab 4
        
        // TAB 5 for handelling Tailings Deposition
        JPanel tab5 = new JPanel();
         
        DataTableTailingsDepositionRate tab5TableModelTails = new DataTableTailingsDepositionRate();
        tab5TableModelTails.setBlankFirstRow(); // sets up a blank first row to ensure classes are set properly
        

        //tab5TableModelTails.setAllData(buffObjELM.depositionRates); 
        
        
        
        JTable tab5TableTails = new JTable(tab5TableModelTails);
        
        JPopupMenu popupMenuTailsTable = new JPopupMenu();
        JMenuItem popupMenuItemTailsSelectAll = new JMenuItem("Select All");
        popupMenuItemTailsSelectAll.addActionListener(e->{
                tab5TableTails.setColumnSelectionInterval(0, 2);
                tab5TableTails.setRowSelectionInterval(0, tab5TableTails.getRowCount()-1);
        });
        JMenuItem popupMenuItemTailsDelete = new JMenuItem("Delete Selection");
        popupMenuItemTailsDelete.addActionListener(e->{
                tab5TableModelTails.removeData(tab5TableTails.getSelectedRows(),tab5TableTails.getSelectedColumns());
        });
        JMenuItem popupMenuItemTailsCopy = new JMenuItem("Copy (Not yet working use ctrl+C");
        popupMenuItemTailsCopy.addActionListener(e->{
                System.out.println("popup menu Copy button hit");
        });

        JMenuItem popupMenuItemTailsPaste = new JMenuItem("Paste (All 3 Columns El. A V)");
        popupMenuItemTailsPaste.addActionListener(e->{
                tab5TableModelTails.pasteFromClipboard(tab5TableTails.getSelectedRows(),tab5TableTails.getSelectedColumns());
        });
        
        popupMenuTailsTable.add(popupMenuItemTailsSelectAll);
        popupMenuTailsTable.add(popupMenuItemTailsDelete);
        popupMenuTailsTable.add(popupMenuItemTailsCopy);
        popupMenuTailsTable.add(popupMenuItemTailsPaste);
        tab5TableTails.setComponentPopupMenu(popupMenuTailsTable);
        tab5TableTails.setCellSelectionEnabled(true);
        

        JScrollPane tab5ScrollTails = new JScrollPane(tab5TableTails);
        tab5ScrollTails.setPreferredSize(new Dimension(800,190));
        tab5.add(tab5ScrollTails);

        // End of Tab 5
        
        // Tab 6
        JPanel tab6 = new JPanel();
        ObjStateTableModel tab6TableModelState = new ObjStateTableModel();
        //tab6TableModelState.setBlankFirstRow(); // sets up a blank first row to ensure classes are set properly
        JTable tab6TableState = new JTable(tab6TableModelState);      
        for(int i = 0; i < ProjSetting.MAX_STATES; i++){
            tab6TableModelState.setValueAt((int)buffObjELM.stateTime[i], i, 0);
            tab6TableModelState.setValueAt((String)buffObjELM.state[i], i, 1);
        }
        
        
        JPopupMenu popupMenuStateTable = new JPopupMenu();
        JMenuItem popupMenuItemStateSelectAll = new JMenuItem("Select All");
        popupMenuItemStateSelectAll.addActionListener(e->{
                tab6TableState.setColumnSelectionInterval(0, 1);
                tab6TableState.setRowSelectionInterval(0, tab6TableState.getRowCount()-1);
        });
        JMenuItem popupMenuItemStateDelete = new JMenuItem("Delete Selection");
        popupMenuItemStateDelete.addActionListener(e->{
                tab6TableModelState.removeData(tab6TableState.getSelectedRows(),tab6TableState.getSelectedColumns());
        });
        JMenuItem popupMenuItemStateCopy = new JMenuItem("Copy (Not yet working use ctrl+C");
        popupMenuItemStateCopy.addActionListener(e ->{
                System.out.println("popup menu Copy button hit");
        });
        JMenuItem popupMenuItemStatePaste = new JMenuItem("Paste");
        popupMenuItemStatePaste.addActionListener(e -> {
            tab6TableModelState.pasteFromClipboard(tab6TableState.getSelectedRows(), tab6TableState.getSelectedColumns());
        });
        
        popupMenuStateTable.add(popupMenuItemStateSelectAll);
        popupMenuStateTable.add(popupMenuItemStateDelete);
        popupMenuStateTable.add(popupMenuItemStateCopy);
        popupMenuStateTable.add(popupMenuItemStatePaste);
        tab6TableState.setComponentPopupMenu(popupMenuStateTable);
        tab6TableState.setCellSelectionEnabled(true);
        
        JComboBox cBoxTRNState = new JComboBox(ObjELM.eLMStatesAllowed);
        TableColumn stateColumn = tab6TableState.getColumnModel().getColumn(1);
        stateColumn.setCellEditor(new DefaultCellEditor(cBoxTRNState));
        
        JScrollPane tab6ScrollState = new JScrollPane(tab6TableState);
        tab6.add(tab6ScrollState);
        
        // End of Tab 6

        // TAB 1 USED FOR BASIC ELEMENT INFORMATION 
        JPanel tab1 = new JPanel();
        SpringLayout layoutTab1 = new SpringLayout(); // sets up a layout manager for the first tab
        tab1.setLayout(layoutTab1);

        JLabel ltfobjName = new JLabel("Element Name");
        JTextField tfobjName = new JTextField(buffObjELM.objname);
        tfobjName.setColumns(20); // Sets Width if Name Field
        
        JLabel lcbobjType = new JLabel ("Element Type");
        JComboBox cbobjType = new JComboBox(ObjELM.objSubTypesAllowed); // Pulls options list from ObjELM static
        cbobjType.setSelectedItem(buffObjELM.objSubType);
        
        SpinnerModel tab1scaleXSpinnerModel = new SpinnerNumberModel(buffObjELM.scaleX,.05,5,.05);
        JSpinner tab1scaleXSpinner = new JSpinner(tab1scaleXSpinnerModel);
        tab1scaleXSpinner.setMaximumSize(new Dimension(50, 30));
        tab1scaleXSpinner.addChangeListener(e->{
            buffObjELM.scaleX = (double)tab1scaleXSpinner.getValue();
        });
        JLabel tab1scaleXSpinnerLabel = new JLabel("Horizontal Scale");
        SpinnerModel tab1scaleYSpinnerModel = new SpinnerNumberModel(buffObjELM.scaleY,.05,5,.05);
        JSpinner tab1scaleYSpinner = new JSpinner(tab1scaleYSpinnerModel);
        tab1scaleYSpinner.setMaximumSize(new Dimension(50, 30));
        tab1scaleYSpinner.addChangeListener(e->{
            buffObjELM.scaleY = (double)tab1scaleYSpinner.getValue();
        });
        JLabel tab1scaleYSpinnerLabel = new JLabel("Vertical Scale");
        
        JCheckBox tab1CheckBoxhasCatchment = new JCheckBox("Include Catchment Area");
        tab1CheckBoxhasCatchment.setSelected(buffObjELM.hasCatchment);
        tab1CheckBoxhasCatchment.addActionListener(e-> {
            buffObjELM.hasCatchment = tab1CheckBoxhasCatchment.isSelected();
        });
        
        JCheckBox tab1CheckBoxhasSolids = new JCheckBox("Include Solids Deposition");
        tab1CheckBoxhasSolids.setSelected(buffObjELM.hasSolids);
        tab1CheckBoxhasSolids.addActionListener(e-> {
            buffObjELM.hasSolids = tab1CheckBoxhasSolids.isSelected();
        });
        JLabel tab1LabelVoidsXoSet = new JLabel("Flowbox Offset Horz:  ");
        SpinnerModel tab1SpinnerModelVoidsXoSet = new SpinnerNumberModel(buffObjELM.oSetXVoids, -100, 100, 5);
        JSpinner tab1SpinnerVoidsXoSet = new JSpinner(tab1SpinnerModelVoidsXoSet);
        tab1SpinnerVoidsXoSet.addChangeListener(e->{
            buffObjELM.oSetXVoids = (int)tab1SpinnerVoidsXoSet.getValue();
        });
        JLabel tab1LabelVoidsYoSet = new JLabel("Vert:");
        SpinnerModel tab1SpinnerModelVoidsYoSet = new SpinnerNumberModel(buffObjELM.oSetYVoids, -100, 100, 5);
        JSpinner tab1SpinnerVoidsYoSet = new JSpinner(tab1SpinnerModelVoidsYoSet);
        tab1SpinnerVoidsYoSet.addChangeListener(e->{
            buffObjELM.oSetYVoids = (int)tab1SpinnerVoidsXoSet.getValue();
        });
                
        JCheckBox tab1CheckBoxhasStorage = new JCheckBox("Include Depth Area Capacity");
        tab1CheckBoxhasStorage.setSelected(buffObjELM.hasStorage);
        tab1CheckBoxhasStorage.addActionListener(e-> {
            buffObjELM.hasStorage = tab1CheckBoxhasStorage.isSelected();
        });
        
        JCheckBox tab1CheckBoxshowStorage = new JCheckBox("Show Net Storage on Flowsheet");
        tab1CheckBoxshowStorage.setSelected(buffObjELM.showStorage);
        tab1CheckBoxshowStorage.addActionListener(e-> {
            buffObjELM.showStorage = tab1CheckBoxshowStorage.isSelected();
        });
        JLabel tab1LabelStorageXoSet = new JLabel("Flowbox Offset Horz:  ");
        SpinnerModel tab1SpinnerModelStorageXoSet = new SpinnerNumberModel(buffObjELM.oSetXStorage, -100, 100, 5);
        JSpinner tab1SpinnerStorageXoSet = new JSpinner(tab1SpinnerModelStorageXoSet);
        tab1SpinnerStorageXoSet.addChangeListener(e->{
            buffObjELM.oSetXStorage = (int)tab1SpinnerStorageXoSet.getValue();
        });
        JLabel tab1LabelStorageYoSet = new JLabel("Vert:");
        SpinnerModel tab1SpinnerModelStorageYoSet = new SpinnerNumberModel(buffObjELM.oSetYStorage, -100, 100, 5);
        JSpinner tab1SpinnerStorageYoSet = new JSpinner(tab1SpinnerModelStorageYoSet);
        tab1SpinnerStorageYoSet.addChangeListener(e->{
            buffObjELM.oSetYStorage = (int)tab1SpinnerStorageXoSet.getValue();
        });
        
        
        
        JCheckBox tab1CheckBoxhasStorageEvapandPrecip = new JCheckBox("Include Direct Evaporation and Precipitation");
        tab1CheckBoxhasStorageEvapandPrecip.setSelected(buffObjELM.hasStorageEvapandPrecip);
        tab1CheckBoxhasStorageEvapandPrecip.addActionListener(e-> {
            buffObjELM.hasStorageEvapandPrecip = tab1CheckBoxhasStorageEvapandPrecip.isSelected();
        });
        JButton bSave = new JButton("Save");
        bSave.addActionListener(e ->{ // uses a lambda instead of needing seperate listener override
            buffObjELM.objname = tfobjName.getText();
            buffObjELM.setSubType(String.valueOf(cbobjType.getSelectedItem()), "ACTIVE");
            buffObjELM.dAC.setData(tab3TableModelDAC.getElevColumn(), tab3TableModelDAC.getAreaColumn(), tab3TableModelDAC.getVolColumn());
            //tab4TableModelTargetVol
            buffObjELM.targetOperatingVol.setAllData(tab4TableModelTargetVol.getDayColumn(), tab4TableModelTargetVol.getVolColumn()); // problem seems to lie here
            
            
            //tab4TableModelMinDepth
            buffObjELM.minDepth.setAllData(tab4TableModelMinDepth.getDayColumn(), tab4TableModelMinDepth.getLevelColumn()); 
            //tab4TableModelMaxOpLevel
            buffObjELM.minDepth.setAllData(tab4TableModelMaxOpLevel.getDayColumn(), tab4TableModelMaxOpLevel.getLevelColumn()); 
            
            buffObjELM.overflowLevel.setAllData(tab4TableModelOverflowLevel.getDayColumn(), tab4TableModelOverflowLevel.getLevelColumn());
            buffObjELM.crestLevel.setAllData(tab4TableModelCrestLevel.getDayColumn(), tab4TableModelCrestLevel.getLevelColumn());
            
            //tab5TableModelTails
            
            for (int i = 0; i < tab6TableModelState.getRowCount(); i++){
                if (i >= buffObjELM.stateTime.length){
                    break;
                }
                buffObjELM.stateTime[i] = (int)tab6TableModelState.getValueAt(i, 0);
                buffObjELM.state[i] = (String)tab6TableModelState.getValueAt(i, 1);
            }
            
            //tab6TableModelState
            
            returnedObjELM = buffObjELM; // sets returned value to 
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
        
        tabPane.addTab("General",tab1);
        tabPane.addTab("FlowSettings",tab2);
        tabPane.addTab("Capacity",tab3);
        tabPane.addTab("Op. Levels", tab4);
        tabPane.addTab("Tailings Solids Deposition",tab5);
        tabPane.addTab("Object State",tab6);

        subframe.add(tabPane);
        
        subframe.pack();
        subframe.setBackground(Color.GRAY);
        subframe.setVisible(true);
        
        return buffObjELM;
        
    }
    

    
    
}
