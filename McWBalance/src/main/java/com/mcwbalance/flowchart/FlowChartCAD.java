package com.mcwbalance.flowchart;

/* TO DO LATERS
- implement controls to prevent negitive origin corridinates for boxes

*/

import com.mcwbalance.settings.Preferences;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.flowchart.TitleBlockTabloidFigure;
import com.mcwbalance.flowchart.TitleBlock;
import com.mcwbalance.util.CalcBasics;
import com.mcwbalance.transfer.TRN;
import com.mcwbalance.transfer.TRNList;
import com.mcwbalance.node.Nod;
import com.mcwbalance.node.NodList;
import com.mcwbalance.project.Project;
import com.mcwbalance.settings.Limit;
import com.mcwbalance.solve.SolveOrder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 * Known Bugs
 * font size changes, between transfer lables, must not be setting the font imediately prior to drawing, or there is a scale occuring
 * Object Deletion sometimes deletes extra elements
 * 
 * 
 * @author amcintyre
 */
public class FlowChartCAD extends JComponent{
    /**
     * Defines number of columns of Title Blocks to layout in Cad Space
     */
    public static final int NUMBER_OF_SHEETS_HORIZONTAL = 1;
    /**
     * Defines number of rows of Title Blocks to layout in Cad Space
     */
    public static final int NUMBER_OF_SHEETS_VERTICAL = 1; 
    /**
     * Sets working width of CAD Window
     */
    public static final int CAD_AREA_WIDTH = TitleBlockTabloidFigure.PAGE_DIMENSION_WIDTH * NUMBER_OF_SHEETS_HORIZONTAL + 1 + NUMBER_OF_SHEETS_HORIZONTAL; 
    /**
     * Sets working height of CAD Window
     */
    public static final int CAD_AREA_HEIGHT = TitleBlockTabloidFigure.PAGE_DIMENSION_HEIGHT * NUMBER_OF_SHEETS_VERTICAL + 1 + NUMBER_OF_SHEETS_VERTICAL;
    
    public static boolean titleBlockVisible = true; 
    
    /**
     * 
     */
    static TitleBlock tb = new TitleBlock();
    

    
    /**
     * Active project which contains the main data model, nodes, transfers, results
     */
    private Project prj; 
    
    private int drawX;
    private int drawY;
    
    private Dimension eLMdim; // variable for holding dimesion of origin elm
    private int eLMx;
    private int eLMy;
    private Rectangle eLMrect; // used in flow line drawing
    
    public final static int TRN_BOX_WIDTH = 60;
    public final static int TRN_BOX_HEIGHT = 60;
    
    private Dimension tRNdim; // placeholder sizing
    private int tRNx;
    private int tRNy;
    private Rectangle tRNrect; // used in flow line drawing
    
    private FlowChartLines fpline; // sets up a bin to hold a polyline
    
    BasicStroke FLOW_LINE;
    BasicStroke THIN_LINE;
    BasicStroke THICK_LINE;
    Color defaultDrawColor;
    Color defaultBGColor;
    Color SELECTED;
    
    private boolean isPageOutlineVisible = false; 
    
    private int minLineLength; // used to set stublines from objects
    
    private int vlableoffset; 
    private double nameWidthDouble; // used for centering the name
    private int nameWidth; // used for centering the name
    private double nameHeightDouble; // used for centering the name
    private int nameHeight; // used for centering the name
    /**
     * @deprecated 
     */
    ProjSetting projSetting;
    private int pVolWidth; // used for centering volume values in transfer 
    private int pVolHeight; // used for centering volume values in transfer;
    private String pVolAnn;
    private String pVolDay;
    private String pVolHr;
    
    private BufferedImage buffIcon; // used for buffering Icon to Print
    private Font lfont = new Font("Arial", Font.PLAIN, 14);
    private Font mfont = new Font("Arial", Font.PLAIN, 12);
    private Font sfont = new Font("Arial", Font.PLAIN, 10);
    private int lpadding = 4; // used to set padding around Text Boxs and White outs
    private int lshadow = 4; // used to size the shaddow box shadow
    private int spadding = 2; // used to pad bottom of numbers. 
   
    public static double zoomscale; 
    public static int drawdate; 
    

    
    
    public FlowChartCAD(Project project){
        this.projSetting = projSetting;
        this.prj = project;
        
        drawX = 0;
        drawY = 0;
        
        defaultDrawColor = Preferences.DEFAULT_DRAW_COLOR;
        defaultBGColor = Preferences.DEFAULT_BACKGROUND_COLOR;
        
        eLMdim = new Dimension(0,0); // variable for holding dimesion of origin elm
        eLMx =0;
        eLMy =0;
        eLMrect = new Rectangle(); // used in flow line drawing
    
        fpline = new FlowChartLines(); // sets up a bin to hold a polyline
        
        tb = new TitleBlock();
        titleBlockVisible = true; 
        
        tRNdim = new Dimension(TRN_BOX_WIDTH,TRN_BOX_HEIGHT); // placeholder sizing
        tRNx =0;
        tRNy =0;
        tRNrect = new Rectangle(); // used in flow line drawing
    
        FLOW_LINE = new BasicStroke(1);
        THIN_LINE = new BasicStroke(1);
        THICK_LINE = new BasicStroke(3);
        SELECTED = new Color(153,209,255);
    
        isPageOutlineVisible = false; 
    
        minLineLength = 25; // used to set stublines from objects
    
        vlableoffset = 5; 

        lfont = new Font("Arial", Font.PLAIN, 14);
        mfont = new Font("Arial", Font.PLAIN, 12);
        sfont = new Font("Arial", Font.PLAIN, 10);
        lpadding = 4; // used to set padding around Text Boxs and White outs
        lshadow = 4; // used to size the shaddow box shadow
        spadding = 2; // used to pad bottom of numbers. 
   
        zoomscale = Preferences.zoomScale; 
        drawdate = -1; 
        
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        NodList eLMList = prj.getNodeList();
        TRNList tRNList = prj.getTransferList();
        
        Graphics2D g2 = (Graphics2D) g; // its a bit unclear what this will all do, but Graphics 2D is needed to allow for line thicknesses
        
        // note items drawn before setting the transform will ignore the transform... 
        AffineTransform at = new AffineTransform();
        at.setToScale(zoomscale, zoomscale); // Screen resolution is 96 dpi, paper is 300
        g2.transform(at);
        g2.setColor(defaultDrawColor);
        if (titleBlockVisible){
            int osetX = 1;
            int osetY = 1; 
            for (int irow = 0; irow < NUMBER_OF_SHEETS_VERTICAL; irow ++){
                osetX = 1;
                for (int icol = 0; icol < NUMBER_OF_SHEETS_HORIZONTAL; icol ++){
                    
                    if(isPageOutlineVisible){
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.setStroke(THIN_LINE);
                        g2.drawRect(osetX - 1, osetY - 1, 
                                TitleBlockTabloidFigure.PAGE_DIMENSION_WIDTH+2, 
                               TitleBlockTabloidFigure.PAGE_DIMENSION_HEIGHT+2);
                    }
                    g2.setColor(defaultDrawColor);
                    g2.setStroke(new BasicStroke(TitleBlockTabloidFigure.THICK_LINE_WIDTH));
                    g2.drawRect(
                            TitleBlockTabloidFigure.MAINBOUNDARY_X_LEFT_MARGIN + osetX,
                            TitleBlockTabloidFigure.MAINBOUNDARY_Y_TOP_MARGIN + osetY, 
                            TitleBlockTabloidFigure.MAINBOUNDARY_WIDTH, 
                            TitleBlockTabloidFigure.MAINBOUNDARY_HEIGHT);
                    g2.drawRect(
                            TitleBlockTabloidFigure.TITLEBOUND_X_LEFT + osetX, 
                            TitleBlockTabloidFigure.TITLEBOUND_Y_TOP + osetY, 
                            TitleBlockTabloidFigure.TITLEBOUND_WIDTH, 
                            TitleBlockTabloidFigure.TITLEBOUND_HEIGHT);
                    
                    g2.setStroke(THIN_LINE);
                    g2.drawLine(TitleBlockTabloidFigure.TITLEBOUND_X_LEFT + osetX, 
                            TitleBlockTabloidFigure.CLIENTNAME_UNDERLINE_Y + osetY,  
                            TitleBlockTabloidFigure.TITLEBOUND_X_LEFT + osetX + TitleBlockTabloidFigure.TITLEBOUND_WIDTH, 
                            TitleBlockTabloidFigure.CLIENTNAME_UNDERLINE_Y + osetY);
                    g2.drawLine(TitleBlockTabloidFigure.TITLEBOUND_X_LEFT + osetX, 
                            TitleBlockTabloidFigure.PROJECTNAME_UNDERLINE_Y + osetY,  
                            TitleBlockTabloidFigure.TITLEBOUND_X_LEFT + osetX + TitleBlockTabloidFigure.TITLEBOUND_WIDTH, 
                            TitleBlockTabloidFigure.PROJECTNAME_UNDERLINE_Y + osetY);
                    g2.drawLine(TitleBlockTabloidFigure.TITLEBOUND_X_LEFT + osetX, 
                            TitleBlockTabloidFigure.FIGURENAME_UNDERLINE_Y + osetY,  
                            TitleBlockTabloidFigure.TITLEBOUND_X_LEFT + osetX + TitleBlockTabloidFigure.TITLEBOUND_WIDTH, 
                            TitleBlockTabloidFigure.FIGURENAME_UNDERLINE_Y + osetY);
                    g2.drawLine(TitleBlockTabloidFigure.LOGO_RIGHT_BOUND_X + osetX,
                            TitleBlockTabloidFigure.FIGURENAME_UNDERLINE_Y + osetY,
                            TitleBlockTabloidFigure.LOGO_RIGHT_BOUND_X + osetX,
                            TitleBlockTabloidFigure.TITLEBOUND_Y_TOP + TitleBlockTabloidFigure.TITLEBOUND_HEIGHT + osetY);
                            
                    
                    osetX = osetX + TitleBlockTabloidFigure.PAGE_DIMENSION_WIDTH +1;
                }
                osetY = osetY + TitleBlockTabloidFigure.PAGE_DIMENSION_HEIGHT +1;
            }
        }
        
        
        // draws the Elements
        g2.setStroke(THIN_LINE);
        g2.setFont(lfont); // sets font for Element Names so it isn't repeated in the loop.
        for (int i = 0; i < eLMList.count; i++){// draws all elements in ELM list
            if(eLMList.nodes[i].getState(drawdate) != "INACTIVE"){
                drawX = eLMList.nodes[i].hitBox.x;
                drawY = eLMList.nodes[i].hitBox.y;
                eLMList.nodes[i].setSpriteState(eLMList.nodes[i].getState(drawdate));
                g2.drawImage(eLMList.nodes[i].objSprite, eLMList.nodes[i].hitBox.x, eLMList.nodes[i].hitBox.y, null);
                
                if(eLMList.nodes[i].isSelected){
                    g2.setStroke(THICK_LINE);
                    g2.setColor(SELECTED);
                    g2.drawRect(drawX, drawY, eLMList.nodes[i].hitBox.width, eLMList.nodes[i].hitBox.height);
                    g2.setColor(defaultDrawColor);
                    g2.setStroke(THIN_LINE);
                }
                
                nameWidthDouble = g.getFontMetrics().getStringBounds(eLMList.nodes[i].objname, g2).getWidth();
                nameWidth = (int)nameWidthDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
                nameHeightDouble = g.getFontMetrics().getStringBounds(eLMList.nodes[i].objname, g2).getHeight();
                nameHeight = (int)nameHeightDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
                // Draws text Lable
                g2.setColor(defaultDrawColor); // Draws Shadow that will be drawn over
                g2.fillRect( eLMList.nodes[i].x - nameWidth/2 - lpadding + lshadow, drawY + eLMList.nodes[i].objSprite.getHeight() + vlableoffset + lshadow, nameWidth + 2*lpadding, nameHeight + lpadding); // draws whiteout
                g2.setColor(defaultBGColor); // whiteout behind text
                g2.fillRect( eLMList.nodes[i].x - nameWidth/2 - lpadding, drawY + eLMList.nodes[i].objSprite.getHeight() + vlableoffset, nameWidth + 2*lpadding, nameHeight + lpadding); // draws whiteout 
                g2.setColor(defaultDrawColor); // border around text
                g2.drawRect( eLMList.nodes[i].x - nameWidth/2 - lpadding, drawY + eLMList.nodes[i].objSprite.getHeight() + vlableoffset, nameWidth + 2*lpadding, nameHeight + lpadding); // draws whiteout 
                g2.drawString(eLMList.nodes[i].objname, eLMList.nodes[i].x - nameWidth/2, drawY + eLMList.nodes[i].objSprite.getHeight() + nameHeight + vlableoffset); // Strings draw up from the bottom, opposite of rectangles and images... 
            }
            
        }
        g2.setFont(mfont); 
        
        
        g2.setStroke(FLOW_LINE);
        for (int i = 0; i < tRNList.count; i++){
            g2.setFont(mfont); // need to re-set font every loop
            nameWidthDouble = g2.getFontMetrics().getStringBounds(tRNList.tRNs[i].objname, g2).getWidth();
            nameWidth = (int)nameWidthDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
            tRNdim = tRNList.tRNs[i].hitBox.getSize();
            tRNx = tRNList.tRNs[i].x;
            tRNy = tRNList.tRNs[i].y;
            tRNrect.setBounds(tRNx - tRNdim.width/2, tRNy - tRNdim.height/2, tRNdim.width, tRNdim.height); 
            
            
            
            nameWidthDouble = g2.getFontMetrics().getStringBounds(tRNList.tRNs[i].objname, g2).getWidth();
            nameHeightDouble = g2.getFontMetrics().getStringBounds(tRNList.tRNs[i].objname, g2).getHeight();
            nameWidth = (int)nameWidthDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
            nameHeight = (int)nameHeightDouble;
            
            // draws inflow line
            if (tRNList.tRNs[i].inObjNumber > -1){ // checks to see if inflow line should be drawn
                eLMdim = eLMList.nodes[tRNList.tRNs[i].inObjNumber].hitBox.getSize();
                eLMx = eLMList.nodes[tRNList.tRNs[i].inObjNumber].x; // only used to make code easier to read,
                eLMy = eLMList.nodes[tRNList.tRNs[i].inObjNumber].y; // only used to make code easier to read,
                eLMrect.setBounds(eLMx - eLMdim.width/2, eLMy - eLMdim.height/2, eLMdim.width, eLMdim.height);
                // This calculates the route for the inflow line.
                fpline.setRoute(eLMrect, tRNList.tRNs[i].inSideFrom, tRNList.tRNs[i].inSideFromOset, tRNrect, tRNList.tRNs[i].inSideTo, 0, minLineLength); // Note OSets will be used later to allow multiplbe lines to same box
                g2.setColor(defaultDrawColor);
                g2.drawPolyline(fpline.xPoints, fpline.yPoints, fpline.nPoints); // draws line
                g2.fillPolygon(fpline.arwxPoints, fpline.arwyPoints, fpline.ARROW_NPOINTS); // draws Arrow; 

            }
            // Draws outflow line
            if (tRNList.tRNs[i].outObjNumber > -1){ // checks to see if outflow line should be drawn
                eLMdim = eLMList.nodes[tRNList.tRNs[i].outObjNumber].hitBox.getSize(); //pulls the dimension of the icon that will be used to draw the in object
                eLMx = eLMList.nodes[tRNList.tRNs[i].outObjNumber].x; // only used to make code easier to read,
                eLMy = eLMList.nodes[tRNList.tRNs[i].outObjNumber].y; // only used to make code easier to read,
                eLMrect.setBounds(eLMx - eLMdim.width/2, eLMy - eLMdim.height/2, eLMdim.width, eLMdim.height);
                // This calculates the route for the inflow line.
                fpline.setRoute(tRNrect, tRNList.tRNs[i].outSideFrom, 0, eLMrect, tRNList.tRNs[i].outSideTo, tRNList.tRNs[i].outSideToOset, minLineLength); // Note OSets will be used later to allow multiplbe lines to same box
                g2.setColor(defaultDrawColor);// Placeholder of using colour until end arrow is drawn
                g2.drawPolyline(fpline.xPoints, fpline.yPoints, fpline.nPoints);
                g2.fillPolygon(fpline.arwxPoints, fpline.arwyPoints, fpline.ARROW_NPOINTS); // draws Arrow; 
            }
            // Draws text Lable
            g2.setColor(defaultBGColor);
            g2.fillRect( tRNList.tRNs[i].x - nameWidth/2, tRNrect.y - vlableoffset - nameHeight, nameWidth, nameHeight); // draws whiteout 
            g2.setColor(defaultDrawColor);
            g2.drawString(tRNList.tRNs[i].objname, tRNList.tRNs[i].x - nameWidth/2, tRNrect.y - vlableoffset); // Text draws from bottom up!, opposite of other objects
            
            // draws the Box that represents the transfer
            g2.setColor(defaultBGColor); // Draws Whiteout behind box
            g2.fillRect(tRNrect.x, tRNrect.y, tRNrect.width, tRNrect.height);
            g2.setColor(defaultDrawColor); // will need to size this box to match Normal KP flowsheets
            g2.drawRoundRect(tRNrect.x, tRNrect.y, tRNrect.width, tRNrect.height, 5, 5);
            g2.drawLine(tRNrect.x, tRNrect.y + tRNrect.height/3, tRNrect.x+tRNrect.width, tRNrect.y + tRNrect.height/3);
            g2.drawLine(tRNrect.x, tRNrect.y + tRNrect.height*2/3, tRNrect.x+tRNrect.width, tRNrect.y + tRNrect.height*2/3);
            
            // draws the box around the TRN if selected
            if(tRNList.tRNs[i].isSelected){
                g2.setStroke(THICK_LINE);
                g2.setColor(SELECTED);
                g2.drawRect(tRNList.tRNs[i].hitBox.x, tRNList.tRNs[i].hitBox.y, tRNList.tRNs[i].hitBox.width, tRNList.tRNs[i].hitBox.height);
                g2.setColor(defaultDrawColor);
                g2.setStroke(THIN_LINE);
            }
            
            g2.setFont(sfont); // Switches to smallest font for Numbers
            // Plots Annual Number
            
            //pVolAnn = String.valueOf(tRNList.tRNs[i].plotVolperAnnum);
            pVolAnn = projSetting.getFmtAnn().format(tRNList.tRNs[i].plotVolperAnnum);
            pVolDay = projSetting.getFmtDay().format(tRNList.tRNs[i].plotVolperDay);
            pVolHr = projSetting.getFmtHr().format(tRNList.tRNs[i].plotVolperHr);
            
            pVolWidth = (int)g2.getFontMetrics().getStringBounds(pVolAnn, g2).getWidth();
            pVolHeight = (int)g2.getFontMetrics().getStringBounds(pVolAnn, g2).getHeight();
            g2.drawString(pVolAnn, tRNrect.x + tRNrect.width/2 - pVolWidth/2, tRNrect.y + tRNrect.height*1/6 + pVolHeight/2);
            
            pVolWidth = (int)g2.getFontMetrics().getStringBounds(pVolDay, g2).getWidth();
            g2.drawString(pVolDay, tRNrect.x + tRNrect.width/2 - pVolWidth/2, tRNrect.y + tRNrect.height*3/6 + pVolHeight/2);
            
            pVolWidth = (int)g2.getFontMetrics().getStringBounds(pVolHr, g2).getWidth();
            g2.drawString(pVolHr, tRNrect.x + tRNrect.width/2 - pVolWidth/2, tRNrect.y + tRNrect.height*5/6 + pVolHeight/2);

        }
    }
    
    public void addObjELM (int inX, int inY){
       prj.getNodeList().addNode(inX, inY);
       ProjSetting.hasChangedSinceSave = true;
    }
    public void addObjTRN (int inX, int inY){
       prj.getTransferList().addTRN(inX, inY);
       ProjSetting.hasChangedSinceSave = true;
    }
    public void addSelectionELM(int inNumber){
        if(inNumber >=0 && inNumber <= Limit.MAX_NODES){
            prj.getNodeList().nodes[inNumber].isSelected = true; 
        }
        ProjSetting.hasChangedSinceSave = true;
    }
    public void addSelectionTRN(int inNumber){
        if(inNumber >=0 && inNumber <= Limit.MAX_TRNS){
            prj.getTransferList().tRNs[inNumber].isSelected = true; 
        }
        ProjSetting.hasChangedSinceSave = true;
    }
    public boolean checkSelectionELM(int inNumber){
        return prj.getNodeList().nodes[inNumber].isSelected;
    }
    public boolean checkSelectionTRN(int inNumber){
        return prj.getTransferList().tRNs[inNumber].isSelected;
    }
    public void clearSelection(){
        for (int i = 0; i < Limit.MAX_NODES; i ++){
           prj.getNodeList().nodes[i].isSelected = false;
        }
        for (int i = 0; i < Limit.MAX_TRNS; i ++){
           prj.getTransferList().tRNs[i].isSelected = false;
        }
    }
    /**
     * Not written,  intend to copy selected ELMS and TRNS to clipboard in ascii (unicode) format tab delimited
     */
    public void copySelectiontoClipboard(){
        
        
    }
    
    
    /**
     * only single delete tested to date
     * should remove all selected ELMS and TRNS. will be called from main window
     * using delete key
     */
    public void deleteSelection(){
        for (int i = 0; i < prj.getNodeList().count; i ++){
           if (checkSelectionELM(i)){
               removeELM(i);
           }
        }
        for (int i = 0; i < prj.getTransferList().count; i ++){
           if (checkSelectionTRN(i)){
               removeTRN(i);
           }
        }
        ProjSetting.hasChangedSinceSave = true;
    }
   
   public void removeELM (int inNumber){
       prj.getNodeList().removeNode(inNumber);
       prj.getTransferList().removeELM(inNumber);
       ProjSetting.hasChangedSinceSave = true;
   }
   public void removeTRN (int tRNNumber){
       prj.getNodeList().removeTRN(tRNNumber);
       prj.getTransferList().removeTRN(tRNNumber);
       ProjSetting.hasChangedSinceSave = true;
   }
   
   public void moveObjELM (int inX, int inY, int inNumber){
       prj.getNodeList().nodes[inNumber].x = inX;
       prj.getNodeList().nodes[inNumber].y = inY;
       prj.getNodeList().nodes[inNumber].hitBox.setLocation(
               inX - prj.getNodeList().nodes[inNumber].hitBox.getSize().width/2, 
               inY - prj.getNodeList().nodes[inNumber].hitBox.getSize().height/2);
       ProjSetting.hasChangedSinceSave = true;
   }
   public void moveObjTRN(int inX, int inY, int inNumber){
       prj.getTransferList().tRNs[inNumber].x = inX;
       prj.getTransferList().tRNs[inNumber].y = inY;
       prj.getTransferList().tRNs[inNumber].hitBox.setLocation(
               inX - prj.getTransferList().tRNs[inNumber].hitBox.getSize().width/2, 
               inY - prj.getTransferList().tRNs[inNumber].hitBox.getSize().height/2);
       ProjSetting.hasChangedSinceSave = true;
   }
   /**
    * method for adding ELMs and TRNs direct from clipboard, intended to allow
    * transfer between 2 instances of McWBalance
    */
   public void pasteFromClipBoard(){
       
       ProjSetting.hasChangedSinceSave = true;
   }
   
   /**
    * @deprecated should pull direct from project
    * @param inNumber
    * @return 
    */
   public Nod getObjELM (int inNumber){
       return prj.getNodeList().nodes[inNumber];
   }
   
   /**
    * @deprecated should pull direct from Project
    * @param inNumber
    * @return 
    */
   public TRN getObjTRN (int inNumber){
       return prj.getTransferList().tRNs[inNumber];
   }
   
   /**
    * @deprecated should pull direct from project
    * @return 
    */
   public NodList getNodeList(){
       return prj.getNodeList();
   }
   

   /**
    * @deprecated should pull direct from project
    * @return 
    */
   public TRNList getTRNList(){
       return prj.getTransferList();
   }
   

   public void setObjELM (int inNumber, Nod inObjELM){
       // note that dimensions of the box need to be applied here since ObjELMList does not have access to the Icon Library 
       inObjELM.hitBox.setLocation(inObjELM.x - inObjELM.hitBox.getSize().width/2, inObjELM.y - inObjELM.hitBox.getSize().height/2);  
       prj.getNodeList().set (inNumber, inObjELM); // passes command to active object list; 
       ProjSetting.hasChangedSinceSave = true;
   }
   public void setObjTRN (int inNumber, TRN inObjTRN){
       prj.getTransferList().setObjTRN (inNumber, inObjTRN);
       ProjSetting.hasChangedSinceSave = true;
   }
   
   /**
    * @deprecated should action on project directly
    * @param nodelist 
    */
   public void setNodeList(NodList nodelist){
       prj.nODEList = nodelist;
   }
   
   
   /**
    * @deprecated should action on project directly
    * @param trnlist 
    */
   public void setTRNList(TRNList trnlist){
       prj.tRNList = trnlist;
   }
   
    public int checkELMHit (int inX, int inY){ // will need updating to allow for TRNs... 
        for (int i = 0; i < prj.getNodeList().count; i++){
            if(prj.getNodeList().nodes[i].hitBox.x <= inX && prj.getNodeList().nodes[i].hitBox.x + prj.getNodeList().nodes[i].hitBox.width >= inX){
                    if(prj.getNodeList().nodes[i].hitBox.y <= inY && prj.getNodeList().nodes[i].hitBox.y + prj.getNodeList().nodes[i].hitBox.height >= inY){
                    return i;  
                }
            }
        }
        return -1;  
       
   }
   public int checkTRNHit (int inX, int inY){ 
       for (int i = 0; i < prj.getTransferList().count; i++){
           if(prj.getTransferList().tRNs[i].hitBox.x <= inX && prj.getTransferList().tRNs[i].hitBox.x + prj.getTransferList().tRNs[i].hitBox.width >= inX){
               if(prj.getTransferList().tRNs[i].hitBox.y <= inY && prj.getTransferList().tRNs[i].hitBox.y + prj.getTransferList().tRNs[i].hitBox.height >= inY){
                   return i;
               }
           }
       }
       return -1;  
   }
   
   
}
