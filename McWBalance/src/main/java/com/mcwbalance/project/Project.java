package com.mcwbalance.project;

import com.mcwbalance.MainWindow;
import com.mcwbalance.McWBalance;
import com.mcwbalance.node.NodList;
import com.mcwbalance.solve.SolveOrder;
import com.mcwbalance.transfer.TRNList;
import com.mcwbalance.util.CalcBasics;
import com.mcwbalance.util.WarningDialog;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * This class acts as the main data model bin to contain the project information
 * nodes transfers results etc. 
 * @author Alex
 */
public class Project {
        
    /**
     * This is the active list of Nodes, i.e. ponds, piles, buildings
     */
    public NodList nODEList;
    
    ProjSetting setting;
    
    SolveOrder solveOrder;
    
    /**
     * This is the active list of Transfers, i.e. pump and pipelines, discharges
     */
    public TRNList tRNList;
    
    /**
     * Used for generation of a blank project
     */
    public Project(){
        setting = new ProjSetting();
        solveOrder = new SolveOrder(this);
        nODEList = new NodList(setting);
        tRNList = new TRNList();
    }
    
    
    /**
     * Project settings such as duration, save path, title, project name etc...
     * @return Active project settings
     */
    public ProjSetting getProjectSetting(){
        return setting;
    }
    /**
     * list of Nodes, i.e. ponds, piles, buildings
     * @return Active array of current existing Nodes
     */
    public NodList getNodeList(){
        return nODEList;
    }
    
    
    public SolveOrder getSolveOrder(){
        return solveOrder;
    }
    /**
     * list of Transfers, i.e. pump and pipelines, discharges
     * @return Active array of current existing transfers
     */
    public TRNList getTransferList(){
        return tRNList;
    }
    
    /**
     * Work In Progress
     * 
     * @return save file output stream only to allow trow of exception
     * @throws java.io.IOException
     */
    public OutputStream saveToFile() throws IOException{
                
        StringBuilder sverInfoFile = new StringBuilder();
        sverInfoFile.append(ProjSetting.verInfo);
        
        //XML builder // not useful yet... 
        TransformerFactory xmltf= TransformerFactory.newInstance();

        try (FileOutputStream sfileos = new FileOutputStream(setting.getSaveFile());
            ZipOutputStream sfilezos = new ZipOutputStream(sfileos)){
            
            Transformer xmltrans = xmltf.newTransformer();
            xmltrans.setOutputProperty(OutputKeys.INDENT, "yes");
            xmltrans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            
            // initializes ZipEntry info for files to include
            ZipEntry zEntVersion = new ZipEntry("Version.ver");

            ZipEntry zEntTRN[] = new ZipEntry[tRNList.count];
            for (int i = 0; i < tRNList.count; i++) {
                zEntTRN[i] = new ZipEntry(i + ".trn");
            }
            ZipEntry zEntELM[] = new ZipEntry[nODEList.count];
            for (int i = 0; i < nODEList.count; i++) {
                zEntELM[i] = new ZipEntry(i + ".elm");
                
            }

            // Write Version Information
            sfilezos.putNextEntry(zEntVersion);
            byte[] bytedata = sverInfoFile.toString().getBytes(); // converts string data to byte data
            sfilezos.write(bytedata, 0, bytedata.length);
            sfilezos.closeEntry();

            // Write Transfer Information to ascii
            for (int i = 0; i < zEntTRN.length; i++) {
                sfilezos.putNextEntry(zEntTRN[i]);
                bytedata = tRNList.tRNs[i].getSaveString().toString().getBytes(); // converts string data to byte data // byte data variable re-used
                sfilezos.write(bytedata, 0, bytedata.length);
                sfilezos.closeEntry();
            }
            
            
            // write transfer information to xml
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            
            DOMSource source = new DOMSource(tRNList.getXMLDoc());
            StreamResult  result = new  StreamResult(bos);
            xmltrans.transform(source, result);
            
            //Debug
            //System.out.println(result.getOutputStream());
            //System.out.println(source.toString());
            ZipEntry zEntTRNXML = new ZipEntry("Transfers.xml");
            sfilezos.putNextEntry(zEntTRNXML);
            sfilezos.write(bos.toByteArray());
            sfilezos.closeEntry();

            // Write ElementInformation
            for (int i = 0; i < zEntELM.length; i++) {
                sfilezos.putNextEntry(zEntELM[i]);
                bytedata = nODEList.nodes[i].getSaveString().toString().getBytes(); // converts string data to byte data // byte data variable re-used
                sfilezos.write(bytedata, 0, bytedata.length);
                sfilezos.closeEntry();
            }

            //writeToZipFile()
            sfilezos.close();
            sfileos.close();
            ProjSetting.hasChangedSinceSave = false; // Debug - does not confirm successful save
            return sfileos;
        } catch (FileNotFoundException | TransformerException | ParserConfigurationException ex) {
            throw new IOException(ex.getMessage());
        } catch (IOException ex) {
            throw ex;
        } 
        
    }
    

    
    /**
     * PLACEHOLDER
     * triggers a solve loop, 
     * TODO
     * add warning checks, add success condition calc solve order
     * should check for 
     */
    public void solve(){
        String warnings = new String();
        //check warnings, 
        verify(warnings);
        //IF ALL IS GOOD
        calc();
        
        //Else Throw error
    }
    
    /**
     * PLACEHOLDER
     * Confirms if model is solvable
     * @param warnings
     * @return True if no critical errors are found
     */
    private Boolean verify(String warnings){
        
        return true;
        
    }
    
    /**
     * PLACEHOLDER
     */
    private void calc(){
       
       int obj; 
        int i;
        int j;
        int day;
        int dayofyear;
        int sodpondArea;
        double sodTotalVol; // tracker for start of Day Volume used for Pond area calcs
        int month;
        double rainandmelt;
        double totaler;
        int runArea;
        int pondAreaSubtraction = 0;
        
        //Constructs result arrays
        tRNList.initializeResults(setting);
        nODEList.initializeResults();
        
        for (int c = 0; c < ProjSetting.climateScenarios.getRowCount(); c++) {

            for (day = 1; day < setting.getDuration(); day++) { // must start at 1 since solver will need previous days numbers to work;
                // set month;...
                month = CalcBasics.getMonth(day);

                for (i = 0; i < nODEList.count; i++) {
                    // resets Pond Areas

                    if (nODEList.nodes[i].hasStorage) {
                        sodTotalVol = nODEList.nodes[i].resultTotalVolume.daily[day - 1];
                        sodpondArea = nODEList.nodes[i].dAC.getAreafromVol((int) sodTotalVol);
                    } else {
                        sodpondArea = 0;
                    }

                    // Direct Precip and Evaporation
                    if (nODEList.nodes[i].hasStorageEvapandPrecip) {
                        nODEList.nodes[i].resultDirectPrecip.daily[day] = ProjSetting.climateScenarios.climateScenarios[c].precip[day] * sodpondArea / 1000;
                        nODEList.nodes[i].resultEvaporation.daily[day] = ProjSetting.climateScenarios.climateScenarios[c].evap[day] * sodpondArea / 1000;
                    }
                    // Upstream Runoff
                    /*
                        if(eLM.eLMs[obj].hasCatchment){
                            totaler = 0;
                            rainandmelt = climate.rain[day] +climate.melt[day];
                            pondAreaSubtraction = sodpondArea;
                            for (j = 0; j < nLandCovers; j++){
                                if (pondAreaSubtraction >= eLM.eLMs[obj].Catchment[j].getArea(day)){ // runoff area needs to be trimmed down to not double count the pond
                                    runArea = 0;     
                                }else if (pondAreaSubtraction > 0){
                                    runArea = eLM.eLMs[obj].Catchment[j].getArea(day);
                                    pondAreaSubtraction = pondAreaSubtraction - runArea;
                                }else{
                                    runArea = eLM.eLMs[obj].Catchment[j].getArea(day);
                                }
                                runoff[eLM.eLMs[obj].indexRunoffTracker][j].daily[day] = runArea * rainandmelt * rc.landCovers[j].getMonthlyCoeff(month) / 1000;
                                totaler = totaler + runoff[eLM.eLMs[obj].indexRunoffTracker][j].daily[day];
                            }
                            runoff[eLM.eLMs[obj].indexRunoffTracker][nLandCovers + 1].daily[day] = totaler; 
                            
                        }
                     */
                    // if Contains Solids need to add a tonnage of solids

                }

                for (i = 0; i < solveOrder.tRNIndex.length; i++) {
                    obj = solveOrder.tRNIndex[i];
                    switch (solveOrder.tRNType[i]) {
                        case "FIXED": // Transfers solved by themselves assume max pumping rate. otherwise must be resolved by element; 
                            tRNList.tRNs[i].result.daily[day] = tRNList.tRNs[obj].getMaxPumpRate(day);

                            // Manditory Inflow Transfers
                            // Manditory Outflow Transfers
                            // Seepage  // can be handeled as a manditory transfer if varies by time. 
                            break;
                        case "SOLIDS":

                            // Solids Storage
                            // Void Losses
                            break;
                        case "ONDEMAND":

                            // Optional Inflow Transders
                            // Optional Ouflow Transfers
                            break;

                    }
                }
            }
        }
    
        
    }
    
    
}
