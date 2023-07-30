
package com.mcwbalance.transfer;

import com.mcwbalance.MainWindow;
import com.mcwbalance.generics.ObjStateTableModel;
import com.mcwbalance.ProjSetting;
import com.mcwbalance.settings.Limit;
import java.awt.Color;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
 * This window dialog class is for user editing of the active objTRNList 
 * 
 * @author Alex
 */
public class ObjTRNWindow extends JDialog {
    private ObjTRN buffObjTRN = new ObjTRN(); // container for storing and modifying object used for return at end
    private int closeAction = 1;
    
    public static final int CLOSE_ACTION_SAVE = 1;
    public static final int CLOSE_ACTION_DISCARD = 2;
        
    public ObjTRNWindow(ObjTRN inObjTRN, String[] eLMList){ // requires object number to edit
        super(MainWindow.mainframe, "Transfer Properties", true); // was orginally a frame but changed to dialog
        ProjSetting.hasChangedSinceSave = true; // assumes if this dialog is called then a change has been made
        buffObjTRN = inObjTRN; // sets buffered object to in object
        int fmtTFColumnsDEF = 30; // number of columns used for Name fields
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        
        JTabbedPane tabPane = new JTabbedPane();
        
        //TAB 2
        JPanel tab2 = new JPanel();
        ObjTRNRateTableModel tab2TableModelRate = new ObjTRNRateTableModel();
        tab2TableModelRate.setBlankFirstRow(); // sets up a blank first row to ensure classes are set properly
        
        for(int i = 0; i < ObjTRN.MAX_PUMP_RATES; i++){
            tab2TableModelRate.setValueAt((int)buffObjTRN.pumpTime[i], i, 0);
            tab2TableModelRate.setValueAt((double)buffObjTRN.pumpRateDay[i], i, 1);
        }

        JTable tab2TableRate = new JTable(tab2TableModelRate);
        
        JPopupMenu popupMenuRateTable = new JPopupMenu();
        JMenuItem popupMenuItemRateSelectAll = new JMenuItem("Select All");
        popupMenuItemRateSelectAll.addActionListener(e -> {
            tab2TableRate.setColumnSelectionInterval(0, 1);
            tab2TableRate.setRowSelectionInterval(0, tab2TableRate.getRowCount() - 1);
        });

        JMenuItem popupMenuItemRateDelete = new JMenuItem("Delete Selection");
        popupMenuItemRateDelete.addActionListener(e -> {
            tab2TableModelRate.removeData(tab2TableRate.getSelectedRows(), tab2TableRate.getSelectedColumns());
        });

        JMenuItem popupMenuItemRateCopy = new JMenuItem("Copy (Not yet working use ctrl+C");
        popupMenuItemRateCopy.addActionListener(e -> {
            System.out.println("popup menu Copy button hit");
        });

        JMenuItem popupMenuItemRatePaste = new JMenuItem("Paste");
        popupMenuItemRatePaste.addActionListener(e -> {
            tab2TableModelRate.pasteFromClipboard(tab2TableRate.getSelectedRows(), tab2TableRate.getSelectedColumns());
        });
        
        popupMenuRateTable.add(popupMenuItemRateSelectAll);
        popupMenuRateTable.add(popupMenuItemRateDelete);
        popupMenuRateTable.add(popupMenuItemRateCopy);
        popupMenuRateTable.add(popupMenuItemRatePaste);
        tab2TableRate.setComponentPopupMenu(popupMenuRateTable);
        tab2TableRate.setCellSelectionEnabled(true);

        JScrollPane tab2ScrollRate = new JScrollPane(tab2TableRate);
        tab2.add(tab2ScrollRate);
        // End of Tab 2
        
        // Tab 3
        JPanel tab3 = new JPanel();
        ObjStateTableModel tab3TableModelState = new ObjStateTableModel();
        JTable tab3TableState = new JTable(tab3TableModelState);
        
        for(int i = 0; i < Limit.MAX_STATES; i++){
            tab3TableModelState.setValueAt((int)buffObjTRN.stateTime[i], i, 0);
            tab3TableModelState.setValueAt((String)buffObjTRN.state[i], i, 1);
        }
        
        JPopupMenu popupMenuStateTable = new JPopupMenu();
        JMenuItem popupMenuItemStateSelectAll = new JMenuItem("Select All");
        popupMenuItemStateSelectAll.addActionListener( e-> {
            tab3TableState.setColumnSelectionInterval(0, 1);
            tab3TableState.setRowSelectionInterval(0, tab3TableState.getRowCount()-1);
        });
        
        JMenuItem popupMenuItemStateDelete = new JMenuItem("Delete Selection");
        popupMenuItemStateDelete.addActionListener(e ->{
            tab3TableModelState.removeData(tab3TableState.getSelectedRows(),tab3TableState.getSelectedColumns());    
        });
        
        JMenuItem popupMenuItemStateCopy = new JMenuItem("Copy (Not yet working use ctrl+C");
        popupMenuItemStateCopy.addActionListener(e ->{
            System.out.println("popup menu Copy button hit");
        });

        JMenuItem popupMenuItemStatePaste = new JMenuItem("Paste");
        popupMenuItemStatePaste.addActionListener(e ->{
                tab3TableModelState.pasteFromClipboard(tab3TableState.getSelectedRows(),tab3TableState.getSelectedColumns());
        });
        
        popupMenuStateTable.add(popupMenuItemStateSelectAll);
        popupMenuStateTable.add(popupMenuItemStateDelete);
        popupMenuStateTable.add(popupMenuItemStateCopy);
        popupMenuStateTable.add(popupMenuItemStatePaste);
        tab3TableState.setComponentPopupMenu(popupMenuStateTable);
        tab3TableState.setCellSelectionEnabled(true);
        
        JComboBox cBoxTRNState = new JComboBox(ObjTRN.tRNStatesAllowed);
        TableColumn stateColumn = tab3TableState.getColumnModel().getColumn(1);
        stateColumn.setCellEditor(new DefaultCellEditor(cBoxTRNState));
        
        JScrollPane tab3ScrollState = new JScrollPane(tab3TableState);
        tab3.add(tab3ScrollState);
        
        // End of Tab 3
        
        //TAB 1 Handelling the general conneciton and naming - Moved to end to allow Save Button to collect from Tables
        JPanel tab1 = new JPanel();
        SpringLayout tab1layout = new SpringLayout();
        tab1.setLayout(tab1layout);
        
        // Name and Type
        JLabel ltfobjName = new JLabel("Transfer Name");
        JTextField tfobjName = new JTextField(buffObjTRN.objname);
        tfobjName.setColumns(fmtTFColumnsDEF);
        JLabel lcbobjType = new JLabel ("Transfer Type");
        JComboBox cbobjType = new JComboBox(ObjTRN.objSubTypesAllowed); // Pulls options list from ObjELM static
        cbobjType.setSelectedItem(buffObjTRN.subType);
        
        // Transfer from Location
        JLabel lcbInObj = new JLabel ("Takes Water From");
        JComboBox cbInObj = new JComboBox(eLMList); // Pulls from provided list of Elements
        cbInObj.setSelectedIndex(buffObjTRN.inObjNumber + 1); // adds 1 since first value is null -1
        JLabel lcbInSideFrom = new JLabel ("From ");
        JComboBox cbInSideFrom = new JComboBox(ObjTRN.objSidesAllowed);
        cbInSideFrom.setSelectedItem(buffObjTRN.inSideFrom);        
        JLabel lcbInSideTo = new JLabel ("To ");
        JComboBox cbInSideTo = new JComboBox(ObjTRN.objSidesAllowed);
        cbInSideTo.setSelectedItem(buffObjTRN.inSideTo);
        JLabel ltfinSideFromOset = new JLabel ("Line Offset");
        SpinnerModel mspinSideFromOset = new SpinnerNumberModel(buffObjTRN.inSideFromOset,-200,200,5);
        JSpinner spinSideFromOset = new JSpinner(mspinSideFromOset);
        
        // Transfer to Location
        JLabel lcbOutObj = new JLabel ("Delivers Water To");
        JComboBox cbOutObj = new JComboBox(eLMList); // Pulls from provided list of Elements
        cbOutObj.setSelectedIndex(buffObjTRN.outObjNumber + 1);  // adds 1 since first value is null -1
        JLabel lcbOutSideFrom = new JLabel ("From ");
        JComboBox cbOutSideFrom = new JComboBox(ObjTRN.objSidesAllowed);
        cbOutSideFrom.setSelectedItem(buffObjTRN.outSideFrom);
        
        
        JLabel lcbOutSideTo = new JLabel ("To ");
        JComboBox cbOutSideTo = new JComboBox(ObjTRN.objSidesAllowed);
        cbOutSideTo.setSelectedItem(buffObjTRN.outSideTo);
        JLabel ltfOutSideToOset = new JLabel ("Line Offset");
        JFormattedTextField tfOutSideToOset = new JFormattedTextField(buffObjTRN.outSideToOset);
        SpinnerModel mspoutSideToOset = new SpinnerNumberModel(buffObjTRN.outSideToOset,-200,200,5);
        JSpinner spOutSideToOset = new JSpinner(mspoutSideToOset);
                
        JButton bSave = new JButton("Save");
        //Tab 1 Save Button listener moved to end to allow saving variables from all tabs
        bSave.addActionListener(e ->{ 
            closeAction = CLOSE_ACTION_SAVE;

            buffObjTRN.objname = tfobjName.getText();
            buffObjTRN.subType = String.valueOf(cbobjType.getSelectedItem());
            
            buffObjTRN.inObjNumber = cbInObj.getSelectedIndex() -1; // assumes 1 nullValue
            buffObjTRN.inSideFrom = String.valueOf(cbInSideFrom.getSelectedItem());            
            buffObjTRN.inSideFromOset = (Integer)spinSideFromOset.getValue();
            buffObjTRN.inSideTo = String.valueOf(cbInSideTo.getSelectedItem());
            
            buffObjTRN.outObjNumber = cbOutObj.getSelectedIndex() -1; // assumes 1 nullValue
            buffObjTRN.outSideFrom = String.valueOf(cbOutSideFrom.getSelectedItem());
            
            buffObjTRN.outSideToOset = (Integer)spOutSideToOset.getValue();
            buffObjTRN.outSideTo = String.valueOf(cbOutSideTo.getSelectedItem()); 
            
            for(int i = 0; i < ObjTRN.MAX_PUMP_RATES; i++){ // Copy even if null, otherwise no mechnism for deleting values    
                buffObjTRN.pumpTime[i] = (int)tab2TableModelRate.getValueAt(i, 0);   
                buffObjTRN.pumpRateDay[i] = (double)tab2TableModelRate.getValueAt(i, 1);                    
            }
            for(int i = 0; i < Limit.MAX_STATES; i++){
                buffObjTRN.stateTime[i] = (int)tab3TableModelState.getValueAt(i, 0);
                buffObjTRN.state[i] = (String)tab3TableModelState.getValueAt(i, 1);
            }
            
        });

        tfobjName.getText();
        
        tab1.add(ltfobjName);
        tab1.add(tfobjName);
        tab1.add(lcbobjType);
        tab1.add(cbobjType);
        tab1.add(lcbInObj);
        tab1.add(cbInObj);
        tab1.add(lcbInSideFrom);
        tab1.add(cbInSideFrom);
        tab1.add(lcbInSideTo);
        tab1.add(cbInSideTo);
        tab1.add(ltfinSideFromOset);
        tab1.add(spinSideFromOset);
        
        tab1.add(lcbOutObj);
        tab1.add(cbOutObj);
        tab1.add(lcbOutSideFrom);
        tab1.add(cbOutSideFrom);
        tab1.add(lcbOutSideTo);
        tab1.add(cbOutSideTo);
        tab1.add(ltfOutSideToOset);
        tab1.add(spOutSideToOset);

        tab1.add(bSave);
        
        int hPadLabels = 10; // padding relative to left side of window
        int hPadBoxes = 120; // padding relative to left side of window
        int vPadFirstRow = 10; // sets padding for initial row relative top of window
        int vPadSpacing = 20; // sets spacing between rows
        
        // Name Box
        tab1layout.putConstraint(SpringLayout.WEST, ltfobjName, hPadLabels, SpringLayout.WEST, this);
        tab1layout.putConstraint(SpringLayout.NORTH, ltfobjName, vPadFirstRow, SpringLayout.NORTH, this);
        tab1layout.putConstraint(SpringLayout.WEST, tfobjName, hPadBoxes, SpringLayout.WEST, this); // aligns horizontal relative to side of window
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER, tfobjName, 0, SpringLayout.VERTICAL_CENTER, ltfobjName); // aligns vertical with lable
        // SubType Box
        tab1layout.putConstraint(SpringLayout.WEST, lcbobjType, hPadLabels, SpringLayout.WEST, this);
        tab1layout.putConstraint(SpringLayout.NORTH, lcbobjType, vPadSpacing, SpringLayout.SOUTH, ltfobjName);
        tab1layout.putConstraint(SpringLayout.WEST, cbobjType, hPadBoxes, SpringLayout.WEST, this);
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER, cbobjType, 0, SpringLayout.VERTICAL_CENTER, lcbobjType);
        // Inflow From what Element
        tab1layout.putConstraint(SpringLayout.WEST,lcbInObj, hPadLabels, SpringLayout.WEST, this); // Aligns Left
        tab1layout.putConstraint(SpringLayout.NORTH,lcbInObj, vPadSpacing, SpringLayout.SOUTH, lcbobjType); // below Subtype Title
        tab1layout.putConstraint(SpringLayout.WEST, cbInObj, hPadBoxes, SpringLayout.WEST, this); // Aligns from Left 100 or so
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER, cbInObj, 0, SpringLayout.VERTICAL_CENTER, lcbInObj); // Aligns with its label
        
        tab1layout.putConstraint(SpringLayout.WEST,lcbInSideFrom, hPadLabels, SpringLayout.WEST, this); // Aligns Left
        tab1layout.putConstraint(SpringLayout.NORTH,lcbInSideFrom, vPadSpacing, SpringLayout.SOUTH, lcbInObj); // below Subtype Title
        tab1layout.putConstraint(SpringLayout.WEST,cbInSideFrom, hPadLabels, SpringLayout.EAST, lcbInSideFrom); 
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER,cbInSideFrom, 0, SpringLayout.VERTICAL_CENTER, lcbInSideFrom); 
        tab1layout.putConstraint(SpringLayout.WEST,lcbInSideTo, hPadLabels, SpringLayout.EAST, cbInSideFrom); 
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER,lcbInSideTo, 0, SpringLayout.VERTICAL_CENTER, lcbInSideFrom); 
        tab1layout.putConstraint(SpringLayout.WEST,cbInSideTo, hPadLabels, SpringLayout.EAST, lcbInSideTo); 
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER,cbInSideTo, 0, SpringLayout.VERTICAL_CENTER, lcbInSideFrom); 
        tab1layout.putConstraint(SpringLayout.WEST, ltfinSideFromOset, hPadLabels, SpringLayout.EAST, cbInSideTo); 
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER,ltfinSideFromOset, 0, SpringLayout.VERTICAL_CENTER, lcbInSideFrom); 
        tab1layout.putConstraint(SpringLayout.WEST,spinSideFromOset, hPadLabels, SpringLayout.EAST, ltfinSideFromOset); 
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER,spinSideFromOset, 0, SpringLayout.VERTICAL_CENTER, lcbInSideFrom);
        // Outflow to...
        tab1layout.putConstraint(SpringLayout.WEST,lcbOutObj, hPadLabels, SpringLayout.WEST, this); // Aligns Left
        tab1layout.putConstraint(SpringLayout.NORTH,lcbOutObj, vPadSpacing, SpringLayout.SOUTH, lcbInSideFrom); // below Subtype Title
        tab1layout.putConstraint(SpringLayout.WEST, cbOutObj, hPadBoxes, SpringLayout.WEST, this); // Aligns from Left 100 or so
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER, cbOutObj, 0, SpringLayout.VERTICAL_CENTER, lcbOutObj); // Aligns with its label
        
        tab1layout.putConstraint(SpringLayout.WEST,lcbOutSideFrom, hPadLabels, SpringLayout.WEST, this); // Aligns Left
        tab1layout.putConstraint(SpringLayout.NORTH,lcbOutSideFrom, vPadSpacing, SpringLayout.SOUTH, lcbOutObj); // below Subtype Title
        tab1layout.putConstraint(SpringLayout.WEST,cbOutSideFrom, hPadLabels, SpringLayout.EAST, lcbOutSideFrom); 
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER,cbOutSideFrom, 0, SpringLayout.VERTICAL_CENTER, lcbOutSideFrom); 
        tab1layout.putConstraint(SpringLayout.WEST,lcbOutSideTo, hPadLabels, SpringLayout.EAST, cbOutSideFrom); 
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER,lcbOutSideTo, 0, SpringLayout.VERTICAL_CENTER, lcbOutSideFrom); 
        tab1layout.putConstraint(SpringLayout.WEST,cbOutSideTo, hPadLabels, SpringLayout.EAST, lcbOutSideTo); 
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER,cbOutSideTo, 0, SpringLayout.VERTICAL_CENTER, lcbOutSideFrom); 
        tab1layout.putConstraint(SpringLayout.WEST, ltfOutSideToOset, hPadLabels, SpringLayout.EAST, cbOutSideTo); 
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER,ltfOutSideToOset, 0, SpringLayout.VERTICAL_CENTER, lcbOutSideFrom); 
        tab1layout.putConstraint(SpringLayout.WEST,spOutSideToOset, hPadLabels, SpringLayout.EAST, ltfOutSideToOset); 
        tab1layout.putConstraint(SpringLayout.VERTICAL_CENTER,spOutSideToOset, 0, SpringLayout.VERTICAL_CENTER, lcbOutSideFrom);
        
        // Will need to move the Save Button
        tab1layout.putConstraint(SpringLayout.WEST, bSave, hPadLabels, SpringLayout.WEST, this);
        tab1layout.putConstraint(SpringLayout.NORTH, bSave, vPadSpacing, SpringLayout.SOUTH, lcbOutSideFrom);
        
        //END OF TAB 1
        tabPane.addTab("General", tab1);
        tabPane.addTab("Maximum Transfer Rate",tab2);
        tabPane.addTab("Object State",tab3);

        this.add(tabPane);
        
        this.setBackground(Color.GRAY);
        this.pack();
        this.setVisible(true);
        
    }
    /**
     * Object used to return input from the window
     * @return objTRN containing all data pertaining to the modified transfer 
     */
    public ObjTRN getObjTRN(){
        return buffObjTRN;
    } 
    /**
     * Provide int to communicate whether the user intends to save changes or discard
     * @return 1 - Save requested, 2 - cancel requested 
     */
    public int getCloseAction(){
        return closeAction;
    }

    
    
}
