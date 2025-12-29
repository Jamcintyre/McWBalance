// This class is for the popup window to edit an Element's properties

package com.mcwbalance.node;

import com.mcwbalance.generics.DataComboBoxModel;
import com.mcwbalance.generics.DataNameListModel;
import com.mcwbalance.MainWindow;
import com.mcwbalance.generics.ObjStateTableModel;
import com.mcwbalance.transfer.TRNList;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.dacapacity.DACWindow;
import com.mcwbalance.settings.Limit;
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
public class NodWindow extends JFrame { // implements ActionListener not needed if lamba is used
    //int objELMNumber = -1;
    /**
     * Container for storing and modifying element used for return at end
     * Setting to static does not fix the problem with action listener making a seperate Copy...
     */
    Nod buffNode; 
    /**
     * Container for allowing mainwindow access to returned value since return doesnt work in Jdialog
     */
    //static Node returnedObjELM = new Node();
    
    
    /**
     * This function calls on the JDialog window needed for user input of all information needed to populate an ELM
     * @param inNode ELM object that will be edited by the user
     * @param nodeNumber Index number of the ELM object so that object can be re-inserted into ELMList
     * @param ctRNList Current TRN list must be provided to allow user to select from available TRNs for solve order planning
     * @return 
     */
    public Nod ObjELMWindowFunct(Nod inNode, int nodeNumber, TRNList ctRNList, ProjSetting projSetting){ // requires object number to edit
        
        buffNode = new Nod(projSetting);
        
        ProjSetting.hasChangedSinceSave = true; // assumes if this window was opened then a change occured
        
        //objELMNumber = inNumber; // sets value for save and loads, needs to be called external to this function in action listener
        MainWindow.editorIsActive = true; 
        buffNode = inNode; // sets buffered object to in object
        //returnedObjELM = inNode; // sets default return to whatever was provided
      
        int hPadLabels = 10; // padding relative to left side of window
        int hPadBoxes = 100; // padding relative to left side of window
        int hPadSpacing =10; // sets default spacing between objects 
        int vPadFirstRow = 10; // sets padding for initial row relative top of window
        int vPadSpacing = 10; // sets spacing between rows
        int vPadNextGroup = 20; // sets spacing between grooups of info
        
        JFrame subframe = new JFrame("Node Properties"); // Changed back to frame, as was having problems with not being able to call a second frame
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
        buffNode.inflows.overwriteList(ctRNList.getInflowNameListIndex(nodeNumber,false), ctRNList.getInflowNameList(nodeNumber,false));
        buffNode.inflows.trimFromList(buffNode.inflowFixedTRN.getShortIndexList());
        buffNode.inflows.trimFromList(buffNode.inflowOnDemandTRN.getShortIndexList());
        buffNode.inflowFixedTRN.setNames(ctRNList.getNameList(buffNode.inflowFixedTRN.getShortIndexList()));
        buffNode.inflowOnDemandTRN.setNames(ctRNList.getNameList(buffNode.inflowOnDemandTRN.getShortIndexList()));
        
        buffNode.outflows.overwriteList(ctRNList.getOutflowNameListIndex(nodeNumber,false), ctRNList.getOutflowNameList(nodeNumber,false));
        buffNode.outflows.trimFromList(buffNode.outflowFixedTRN.getShortIndexList());
        buffNode.outflows.trimFromList(buffNode.outflowOnDemandTRN.getShortIndexList());
        buffNode.outflows.trimFromList(buffNode.overflowTRN);
        
        // overflow Options built independently from outflows since i believe the actionlistener somehow skips back to be beginning of the class, but debug breaks don't catch it...
        buffNode.overflowOptions.overwriteList(ctRNList.getOutflowNameListIndex(nodeNumber,false), ctRNList.getOutflowNameList(nodeNumber,false));
        buffNode.overflowOptions.trimFromList(buffNode.outflowFixedTRN.getShortIndexList());
        buffNode.overflowOptions.trimFromList(buffNode.outflowOnDemandTRN.getShortIndexList());     
       
        buffNode.outflowFixedTRN.setNames(ctRNList.getNameList(buffNode.outflowFixedTRN.getShortIndexList()));
        buffNode.outflowOnDemandTRN.setNames(ctRNList.getNameList(buffNode.outflowOnDemandTRN.getShortIndexList()));
   
        DataNameListModel tab2LModelFixedInputs = new DataNameListModel();
        tab2LModelFixedInputs.setAllData(buffNode.inflowFixedTRN.getShortNameList());
        JList tab2listFixedInputs = new JList(tab2LModelFixedInputs);
        
        DataNameListModel tab2LModelInputs = new DataNameListModel();
        tab2LModelInputs.setAllData(buffNode.inflows.getShortNameList());
        JList tab2listInputs = new JList(tab2LModelInputs);
        
        DataNameListModel tab2LModelDemandInputs = new DataNameListModel();
        tab2LModelDemandInputs.setAllData(buffNode.inflowOnDemandTRN.getShortNameList());
        JList tab2listDemandInputs = new JList(tab2LModelDemandInputs);
        
        DataNameListModel tab2LModelFixedOutputs = new DataNameListModel();
        tab2LModelFixedOutputs.setAllData(buffNode.outflowFixedTRN.getShortNameList());
        JList tab2listFixedOutputs = new JList(tab2LModelFixedOutputs);
        
        DataNameListModel tab2LModelOutputs = new DataNameListModel();
        tab2LModelOutputs.setAllData(buffNode.outflows.getShortNameList());
        JList tab2listOutputs = new JList(tab2LModelOutputs);
        
        DataNameListModel tab2LModelDemandOutputs = new DataNameListModel();
        tab2LModelDemandOutputs.setAllData(buffNode.outflowOnDemandTRN.getShortNameList());
        JList tab2listDemandOutputs = new JList(tab2LModelDemandOutputs);
        
        //experementing, may not be able ot use the same type of data model for comboBoxes... 
        DataComboBoxModel tab2ComboModelOverflow = new DataComboBoxModel();
        tab2ComboModelOverflow.setAllData(buffNode.overflowOptions.getShortNameListWithNone());
        
        
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
            int tRNIndex = buffNode.inflows.getObjIndex(tab2listInputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffNode.inflows.trimFromList(tRNIndex);
            tab2LModelInputs.setAllData(buffNode.inflows.getShortNameList());
            buffNode.inflowFixedTRN.appendToList(tRNIndex, tRNName);
            tab2LModelFixedInputs.setAllData(buffNode.inflowFixedTRN.getShortNameList());
        });
        JButton tab2buttonRemFixedInput = new JButton(">");
        tab2buttonRemFixedInput.addActionListener(e -> {
            int tRNIndex = buffNode.inflowFixedTRN.getObjIndex(tab2listFixedInputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffNode.inflows.appendToList(tRNIndex, tRNName);
            tab2LModelInputs.setAllData(buffNode.inflows.getShortNameList());
            buffNode.inflowFixedTRN.trimFromList(tRNIndex);
            tab2LModelFixedInputs.setAllData(buffNode.inflowFixedTRN.getShortNameList());
        });
        JButton tab2buttonAddDemandInput = new JButton(">");
        tab2buttonAddDemandInput.addActionListener(e -> {
            int tRNIndex = buffNode.inflows.getObjIndex(tab2listInputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffNode.inflows.trimFromList(tRNIndex);
            tab2LModelInputs.setAllData(buffNode.inflows.getShortNameList());
            buffNode.inflowOnDemandTRN.appendToList(tRNIndex, tRNName);
            tab2LModelDemandInputs.setAllData(buffNode.inflowOnDemandTRN.getShortNameList());

        });
        JButton tab2buttonRemDemandInput = new JButton("<");
        tab2buttonRemDemandInput.addActionListener(e -> {
            int tRNIndex = buffNode.inflowOnDemandTRN.getObjIndex(tab2listDemandInputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffNode.inflows.appendToList(tRNIndex, tRNName);
            tab2LModelInputs.setAllData(buffNode.inflows.getShortNameList());
            buffNode.inflowOnDemandTRN.trimFromList(tRNIndex);
            tab2LModelDemandInputs.setAllData(buffNode.inflowOnDemandTRN.getShortNameList());
        });
        JButton tab2buttonUpDemandInput = new JButton("UP");
        tab2buttonUpDemandInput.addActionListener(e -> {
            buffNode.inflowOnDemandTRN.shiftUp(tab2listDemandInputs.getSelectedIndex());
            tab2LModelDemandInputs.setAllData(buffNode.inflowOnDemandTRN.getShortNameList());
        });
        JButton tab2buttonDownDemandInput = new JButton("DOWN");
        tab2buttonDownDemandInput.addActionListener(e -> {
            buffNode.inflowOnDemandTRN.shiftDown(tab2listDemandInputs.getSelectedIndex());
            tab2LModelDemandInputs.setAllData(buffNode.inflowOnDemandTRN.getShortNameList());
        });
        JButton tab2buttonAddFixedOutput = new JButton("<");
        tab2buttonAddFixedOutput.addActionListener(e -> {
            int tRNIndex = buffNode.outflows.getObjIndex(tab2listOutputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffNode.outflows.trimFromList(tRNIndex);
            tab2LModelOutputs.setAllData(buffNode.outflows.getShortNameList());
            buffNode.outflowFixedTRN.appendToList(tRNIndex, tRNName);
            tab2LModelFixedOutputs.setAllData(buffNode.outflowFixedTRN.getShortNameList());
        });
        JButton tab2buttonRemFixedOutput = new JButton(">");
        tab2buttonRemFixedOutput.addActionListener(e -> {
            int tRNIndex = buffNode.outflowFixedTRN.getObjIndex(tab2listFixedOutputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffNode.outflows.appendToList(tRNIndex, tRNName);
            tab2LModelOutputs.setAllData(buffNode.outflows.getShortNameList());
            buffNode.outflowFixedTRN.trimFromList(tRNIndex);
            tab2LModelFixedOutputs.setAllData(buffNode.outflowFixedTRN.getShortNameList());
        });
        JButton tab2buttonAddDemandOutput = new JButton(">");
        tab2buttonAddDemandOutput.addActionListener(e -> {
            int tRNIndex = buffNode.outflows.getObjIndex(tab2listOutputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffNode.outflows.trimFromList(tRNIndex);
            tab2LModelOutputs.setAllData(buffNode.outflows.getShortNameList());
            buffNode.outflowOnDemandTRN.appendToList(tRNIndex, tRNName);
            tab2LModelDemandOutputs.setAllData(buffNode.outflowOnDemandTRN.getShortNameList());
        });
        JButton tab2buttonRemDemandOutput = new JButton("<");
        tab2buttonRemDemandOutput.addActionListener(e -> {
            int tRNIndex = buffNode.outflowOnDemandTRN.getObjIndex(tab2listDemandOutputs.getSelectedIndex());
            if (tRNIndex < 0) {
                return;
            }
            String tRNName = ctRNList.tRNs[tRNIndex].objname;
            buffNode.outflows.appendToList(tRNIndex, tRNName);
            tab2LModelOutputs.setAllData(buffNode.outflows.getShortNameList());
            buffNode.outflowOnDemandTRN.trimFromList(tRNIndex);
            tab2LModelDemandOutputs.setAllData(buffNode.outflowOnDemandTRN.getShortNameList());
        });    
        JButton tab2buttonUpDemandOutput = new JButton("UP");
        tab2buttonUpDemandOutput.addActionListener(e -> {
            buffNode.outflowOnDemandTRN.shiftUp(tab2listDemandOutputs.getSelectedIndex());
            tab2LModelDemandOutputs.setAllData(buffNode.outflowOnDemandTRN.getShortNameList());
        });
        JButton tab2buttonDownDemandOutput = new JButton("DOWN");
        tab2buttonDownDemandOutput.addActionListener(e -> {
            buffNode.outflowOnDemandTRN.shiftDown(tab2listDemandOutputs.getSelectedIndex());
            tab2LModelDemandOutputs.setAllData(buffNode.outflowOnDemandTRN.getShortNameList());
        });
        
        JComboBox tab2comboOverflow = new JComboBox(tab2ComboModelOverflow);
        tab2comboOverflow.setSelectedIndex(buffNode.overflowOptions.getListIndex(buffNode.overflowTRN)+1);
        tab2comboOverflow.addActionListener(e-> {
            if( buffNode.overflowTRN != buffNode.overflowOptions.getObjIndex(tab2comboOverflow.getSelectedIndex()-1)){ // confirms selection actually changed
                if(buffNode.overflowTRN > -1){
                    buffNode.outflows.appendToList(buffNode.overflowTRN, ctRNList.tRNs[buffNode.overflowTRN].objname);
                }
                System.out.println("Object Index " + buffNode.overflowOptions.getObjIndex(tab2comboOverflow.getSelectedIndex()) + " minus 1 Selected");
                buffNode.overflowTRN = buffNode.overflowOptions.getObjIndex(tab2comboOverflow.getSelectedIndex()-1);
                if(buffNode.overflowTRN > -1){ 
                    buffNode.outflows.trimFromList(buffNode.overflowTRN);
                }
                tab2LModelOutputs.setAllData(buffNode.outflows.getShortNameList());
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
        
        // TAB 3 for handelling Depth Area Capacity
        JPanel tab3 = new JPanel();

        

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
        TableNodeVolume tab4TableModelTargetVol = new TableNodeVolume();
        tab4TableModelTargetVol.setAllData(buffNode.targetOperatingVol.getDays(),buffNode.targetOperatingVol.getValues());
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
        TableNodeLevel tab4TableModelMinDepth = new TableNodeLevel();
        tab4TableModelMinDepth.setAllData(buffNode.minDepth.getDays(),buffNode.minDepth.getValues());
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
        TableNodeLevel tab4TableModelMaxOpLevel = new TableNodeLevel();
        tab4TableModelMaxOpLevel.setAllData(buffNode.maxOpLevel.getDays(),buffNode.maxOpLevel.getValues());
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
        TableNodeLevel tab4TableModelOverflowLevel = new TableNodeLevel();
        tab4TableModelOverflowLevel.setAllData(buffNode.overflowLevel.getDays(),buffNode.overflowLevel.getValues());
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
        TableNodeLevel tab4TableModelCrestLevel = new TableNodeLevel();
        tab4TableModelCrestLevel.setAllData(buffNode.crestLevel.getDays(),buffNode.crestLevel.getValues());
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

        JTable tab5TableTails = new JTable(buffNode.depositionRates);
        
        JPopupMenu popupMenuTailsTable = new JPopupMenu();
        JMenuItem popupMenuItemTailsSelectAll = new JMenuItem("Select All");
        popupMenuItemTailsSelectAll.addActionListener(e->{
                tab5TableTails.setColumnSelectionInterval(0, 8);
                tab5TableTails.setRowSelectionInterval(0, tab5TableTails.getRowCount()-1);
        });
        JMenuItem popupMenuItemTailsDelete = new JMenuItem("Delete Selection");
        popupMenuItemTailsDelete.addActionListener(e->{
                buffNode.depositionRates.removeData(tab5TableTails.getSelectedRows(),tab5TableTails.getSelectedColumns());
        });
        JMenuItem popupMenuItemTailsCopy = new JMenuItem("Copy All");
        popupMenuItemTailsCopy.addActionListener(e->{
                buffNode.depositionRates.copyToClipBoard();
        });

        JMenuItem popupMenuItemTailsPaste = new JMenuItem("Paste All");
        popupMenuItemTailsPaste.addActionListener(e->{
                buffNode.depositionRates.pasteFromClipboard(tab5TableTails.getSelectedRows(),tab5TableTails.getSelectedColumns());
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
        for(int i = 0; i < Limit.MAX_STATES; i++){
            tab6TableModelState.setValueAt((int)buffNode.stateTime[i], i, 0);
            tab6TableModelState.setValueAt((String)buffNode.state[i], i, 1);
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
        
        JComboBox cBoxTRNState = new JComboBox(Nod.StatesAllowed);
        TableColumn stateColumn = tab6TableState.getColumnModel().getColumn(1);
        stateColumn.setCellEditor(new DefaultCellEditor(cBoxTRNState));
        
        JScrollPane tab6ScrollState = new JScrollPane(tab6TableState);
        tab6.add(tab6ScrollState);
        
        // End of Tab 6

        // TAB 1 USED FOR BASIC ELEMENT INFORMATION 
        JPanel tab1 = new JPanel();
        SpringLayout layoutTab1 = new SpringLayout(); // sets up a layout manager for the first tab
        tab1.setLayout(layoutTab1);

        JLabel ltfobjName = new JLabel("Name");
        JTextField tfobjName = new JTextField(buffNode.objname);
        tfobjName.setColumns(20); // Sets Width if Name Field
        
        JLabel lcbobjType = new JLabel ("Type");
        JComboBox cbobjType = new JComboBox(Nod.objSubTypesAllowed); // Pulls options list from ObjELM static
        cbobjType.setSelectedItem(buffNode.objSubType);
        
        SpinnerModel tab1scaleXSpinnerModel = new SpinnerNumberModel(buffNode.scaleX,.05,5,.05);
        JSpinner tab1scaleXSpinner = new JSpinner(tab1scaleXSpinnerModel);
        tab1scaleXSpinner.setMaximumSize(new Dimension(50, 30));
        tab1scaleXSpinner.addChangeListener(e->{
            buffNode.scaleX = (double)tab1scaleXSpinner.getValue();
        });
        JLabel tab1scaleXSpinnerLabel = new JLabel("Horizontal Scale");
        SpinnerModel tab1scaleYSpinnerModel = new SpinnerNumberModel(buffNode.scaleY,.05,5,.05);
        JSpinner tab1scaleYSpinner = new JSpinner(tab1scaleYSpinnerModel);
        tab1scaleYSpinner.setMaximumSize(new Dimension(50, 30));
        tab1scaleYSpinner.addChangeListener(e->{
            buffNode.scaleY = (double)tab1scaleYSpinner.getValue();
        });
        JLabel tab1scaleYSpinnerLabel = new JLabel("Vertical Scale");
        
        JCheckBox tab1CheckBoxhasCatchment = new JCheckBox("Include Catchment Area");
        tab1CheckBoxhasCatchment.setSelected(buffNode.hasCatchment);
        tab1CheckBoxhasCatchment.addActionListener(e-> {
            buffNode.hasCatchment = tab1CheckBoxhasCatchment.isSelected();
        });
        
        JCheckBox tab1CheckBoxhasSolids = new JCheckBox("Include Solids Deposition");
        tab1CheckBoxhasSolids.setSelected(buffNode.hasSolids);
        tab1CheckBoxhasSolids.addActionListener(e-> {
            buffNode.hasSolids = tab1CheckBoxhasSolids.isSelected();
        });
        JLabel tab1LabelVoidsXoSet = new JLabel("Flowbox Offset Horz:  ");
        SpinnerModel tab1SpinnerModelVoidsXoSet = new SpinnerNumberModel(buffNode.oSetXVoids, -100, 100, 5);
        JSpinner tab1SpinnerVoidsXoSet = new JSpinner(tab1SpinnerModelVoidsXoSet);
        tab1SpinnerVoidsXoSet.addChangeListener(e->{
            buffNode.oSetXVoids = (int)tab1SpinnerVoidsXoSet.getValue();
        });
        JLabel tab1LabelVoidsYoSet = new JLabel("Vert:");
        SpinnerModel tab1SpinnerModelVoidsYoSet = new SpinnerNumberModel(buffNode.oSetYVoids, -100, 100, 5);
        JSpinner tab1SpinnerVoidsYoSet = new JSpinner(tab1SpinnerModelVoidsYoSet);
        tab1SpinnerVoidsYoSet.addChangeListener(e->{
            buffNode.oSetYVoids = (int)tab1SpinnerVoidsXoSet.getValue();
        });
                
        JCheckBox tab1CheckBoxhasStorage = new JCheckBox("Include Depth Area Capacity");
        tab1CheckBoxhasStorage.setSelected(buffNode.hasStorage);
        tab1CheckBoxhasStorage.addActionListener(e-> {
            buffNode.hasStorage = tab1CheckBoxhasStorage.isSelected();
        });
        
        JButton tab1ButtonDACWindow = new JButton("Show DAC");
        tab1ButtonDACWindow.addActionListener(e -> {
            DACWindow dACWindow = new DACWindow(this, buffNode.dAC);
        });

        
        
        JCheckBox tab1CheckBoxshowStorage = new JCheckBox("Show Net Storage on Flowsheet");
        tab1CheckBoxshowStorage.setSelected(buffNode.showStorage);
        tab1CheckBoxshowStorage.addActionListener(e-> {
            buffNode.showStorage = tab1CheckBoxshowStorage.isSelected();
        });
        JLabel tab1LabelStorageXoSet = new JLabel("Flowbox Offset Horz:  ");
        SpinnerModel tab1SpinnerModelStorageXoSet = new SpinnerNumberModel(buffNode.oSetXStorage, -100, 100, 5);
        JSpinner tab1SpinnerStorageXoSet = new JSpinner(tab1SpinnerModelStorageXoSet);
        tab1SpinnerStorageXoSet.addChangeListener(e->{
            buffNode.oSetXStorage = (int)tab1SpinnerStorageXoSet.getValue();
        });
        JLabel tab1LabelStorageYoSet = new JLabel("Vert:");
        SpinnerModel tab1SpinnerModelStorageYoSet = new SpinnerNumberModel(buffNode.oSetYStorage, -100, 100, 5);
        JSpinner tab1SpinnerStorageYoSet = new JSpinner(tab1SpinnerModelStorageYoSet);
        tab1SpinnerStorageYoSet.addChangeListener(e->{
            buffNode.oSetYStorage = (int)tab1SpinnerStorageXoSet.getValue();
        });
        
        JCheckBox tab1CheckBoxhasStorageEvapandPrecip = new JCheckBox("Include Direct Evaporation and Precipitation");
        tab1CheckBoxhasStorageEvapandPrecip.setSelected(buffNode.hasStorageEvapandPrecip);
        tab1CheckBoxhasStorageEvapandPrecip.addActionListener(e-> {
            buffNode.hasStorageEvapandPrecip = tab1CheckBoxhasStorageEvapandPrecip.isSelected();
        });
        JButton bSave = new JButton("Save");
        bSave.addActionListener(e ->{ // uses a lambda instead of needing seperate listener override
            buffNode.objname = tfobjName.getText();
            buffNode.setSubType(String.valueOf(cbobjType.getSelectedItem()), "ACTIVE");


            buffNode.targetOperatingVol.setAllData(tab4TableModelTargetVol.getDayColumn(), tab4TableModelTargetVol.getVolColumn()); // problem seems to lie here
            
            buffNode.minDepth.setAllData(tab4TableModelMinDepth.getDayColumn(), tab4TableModelMinDepth.getLevelColumn()); 

            buffNode.minDepth.setAllData(tab4TableModelMaxOpLevel.getDayColumn(), tab4TableModelMaxOpLevel.getLevelColumn()); 
            
            buffNode.overflowLevel.setAllData(tab4TableModelOverflowLevel.getDayColumn(), tab4TableModelOverflowLevel.getLevelColumn());
            buffNode.crestLevel.setAllData(tab4TableModelCrestLevel.getDayColumn(), tab4TableModelCrestLevel.getLevelColumn());
            
            

            for (int i = 0; i < tab6TableModelState.getRowCount(); i++){
                if (i >= buffNode.stateTime.length){
                    break;
                }
                buffNode.stateTime[i] = (int)tab6TableModelState.getValueAt(i, 0);
                buffNode.state[i] = (String)tab6TableModelState.getValueAt(i, 1);
            }
            

            
            //returnedObjELM = buffNode; // sets returned value to 
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
        
        return buffNode;
        
        
    }
    

    
    
}
