package com.mcwbalance.flowchart;

/* TO DO LATERS
- implement controls to prevent negitive origin corridinates for boxes

*/

import com.mcwbalance.Preferences;
import com.mcwbalance.ProjSetting;
import com.mcwbalance.flowchart.TitleBlockTabloidFigure;
import com.mcwbalance.flowchart.TitleBlock;
import com.mcwbalance.util.CalcBasics;
import com.mcwbalance.transfer.ObjTRN;
import com.mcwbalance.transfer.ObjTRNList;
import com.mcwbalance.element.ObjELM;
import com.mcwbalance.element.ObjELMList;
import com.mcwbalance.settings.Limit;
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
     * This is the active list of Elements
     */
    public static ObjELMList eLMList = new ObjELMList();
    /**
     * This is the active list of Transfers
     */
    public static ObjTRNList tRNList = new ObjTRNList();
    
    private int drawX = 0;
    private int drawY = 0;
    
    private Dimension eLMdim = new Dimension(0,0); // variable for holding dimesion of origin elm
    private int eLMx =0;
    private int eLMy =0;
    private Rectangle eLMrect = new Rectangle(); // used in flow line drawing
    
    public final static int TRN_BOX_WIDTH = 60;
    public final static int TRN_BOX_HEIGHT = 60;
    
    private Dimension tRNdim = new Dimension(TRN_BOX_WIDTH,TRN_BOX_HEIGHT); // placeholder sizing
    private int tRNx =0;
    private int tRNy =0;
    private Rectangle tRNrect = new Rectangle(); // used in flow line drawing
    
    private FlowChartLines fpline = new FlowChartLines(); // sets up a bin to hold a polyline
    
    BasicStroke FLOW_LINE = new BasicStroke(1);
    BasicStroke THIN_LINE = new BasicStroke(1);
    BasicStroke THICK_LINE = new BasicStroke(3);
    Color defaultDrawColor = Preferences.DEFAULT_DRAW_COLOR;
    Color defaultBGColor = Preferences.DEFAULT_BACKGROUND_COLOR;
    Color SELECTED = new Color(153,209,255);
    
    private boolean isPageOutlineVisible = false; 
    
    private final int minLineLength = 25; // used to set stublines from objects
    
    private final int vlableoffset = 5; 
    private double nameWidthDouble; // used for centering the name
    private int nameWidth; // used for centering the name
    private double nameHeightDouble; // used for centering the name
    private int nameHeight; // used for centering the name
    private int pVolWidth; // used for centering volume values in transfer 
    private int pVolHeight; // used for centering volume values in transfer;
    private String pVolAnn;
    private String pVolDay;
    private String pVolHr;
    
    private BufferedImage buffIcon; // used for buffering Icon to Print
    private final Font lfont = new Font("Arial", Font.PLAIN, 14);
    private final Font mfont = new Font("Arial", Font.PLAIN, 12);
    private final Font sfont = new Font("Arial", Font.PLAIN, 10);
    private final int lpadding = 4; // used to set padding around Text Boxs and White outs
    private final int lshadow = 4; // used to size the shaddow box shadow
    private final int spadding = 2; // used to pad bottom of numbers. 
   
    public static double zoomscale = Preferences.zoomScale; 
    public static int drawdate = -1; 
    
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
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
            if(eLMList.eLMs[i].getState(drawdate) != "INACTIVE"){
                drawX = eLMList.eLMs[i].hitBox.x;
                drawY = eLMList.eLMs[i].hitBox.y;
                eLMList.eLMs[i].setSpriteState(eLMList.eLMs[i].getState(drawdate));
                g2.drawImage(eLMList.eLMs[i].objSprite, eLMList.eLMs[i].hitBox.x, eLMList.eLMs[i].hitBox.y, null);
                
                if(eLMList.eLMs[i].isSelected){
                    g2.setStroke(THICK_LINE);
                    g2.setColor(SELECTED);
                    g2.drawRect(drawX, drawY, eLMList.eLMs[i].hitBox.width, eLMList.eLMs[i].hitBox.height);
                    g2.setColor(defaultDrawColor);
                    g2.setStroke(THIN_LINE);
                }
                
                nameWidthDouble = g.getFontMetrics().getStringBounds(eLMList.eLMs[i].objname, g2).getWidth();
                nameWidth = (int)nameWidthDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
                nameHeightDouble = g.getFontMetrics().getStringBounds(eLMList.eLMs[i].objname, g2).getHeight();
                nameHeight = (int)nameHeightDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
                // Draws text Lable
                g2.setColor(defaultDrawColor); // Draws Shadow that will be drawn over
                g2.fillRect( eLMList.eLMs[i].x - nameWidth/2 - lpadding + lshadow, drawY + eLMList.eLMs[i].objSprite.getHeight() + vlableoffset + lshadow, nameWidth + 2*lpadding, nameHeight + lpadding); // draws whiteout
                g2.setColor(defaultBGColor); // whiteout behind text
                g2.fillRect( eLMList.eLMs[i].x - nameWidth/2 - lpadding, drawY + eLMList.eLMs[i].objSprite.getHeight() + vlableoffset, nameWidth + 2*lpadding, nameHeight + lpadding); // draws whiteout 
                g2.setColor(defaultDrawColor); // border around text
                g2.drawRect( eLMList.eLMs[i].x - nameWidth/2 - lpadding, drawY + eLMList.eLMs[i].objSprite.getHeight() + vlableoffset, nameWidth + 2*lpadding, nameHeight + lpadding); // draws whiteout 
                g2.drawString(eLMList.eLMs[i].objname, eLMList.eLMs[i].x - nameWidth/2, drawY + eLMList.eLMs[i].objSprite.getHeight() + nameHeight + vlableoffset); // Strings draw up from the bottom, opposite of rectangles and images... 
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
                eLMdim = eLMList.eLMs[tRNList.tRNs[i].inObjNumber].hitBox.getSize();
                eLMx = eLMList.eLMs[tRNList.tRNs[i].inObjNumber].x; // only used to make code easier to read,
                eLMy = eLMList.eLMs[tRNList.tRNs[i].inObjNumber].y; // only used to make code easier to read,
                eLMrect.setBounds(eLMx - eLMdim.width/2, eLMy - eLMdim.height/2, eLMdim.width, eLMdim.height);
                // This calculates the route for the inflow line.
                fpline.setRoute(eLMrect, tRNList.tRNs[i].inSideFrom, tRNList.tRNs[i].inSideFromOset, tRNrect, tRNList.tRNs[i].inSideTo, 0, minLineLength); // Note OSets will be used later to allow multiplbe lines to same box
                g2.setColor(defaultDrawColor);
                g2.drawPolyline(fpline.xPoints, fpline.yPoints, fpline.nPoints); // draws line
                g2.fillPolygon(fpline.arwxPoints, fpline.arwyPoints, fpline.ARROW_NPOINTS); // draws Arrow; 

            }
            // Draws outflow line
            if (tRNList.tRNs[i].outObjNumber > -1){ // checks to see if outflow line should be drawn
                eLMdim = eLMList.eLMs[tRNList.tRNs[i].outObjNumber].hitBox.getSize(); //pulls the dimension of the icon that will be used to draw the in object
                eLMx = eLMList.eLMs[tRNList.tRNs[i].outObjNumber].x; // only used to make code easier to read,
                eLMy = eLMList.eLMs[tRNList.tRNs[i].outObjNumber].y; // only used to make code easier to read,
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
            pVolAnn = ProjSetting.fmtAnn.format(tRNList.tRNs[i].plotVolperAnnum);
            pVolDay = ProjSetting.fmtDay.format(tRNList.tRNs[i].plotVolperDay);
            pVolHr = ProjSetting.fmtHr.format(tRNList.tRNs[i].plotVolperHr);
            
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
       eLMList.addELM(inX, inY);
       ProjSetting.hasChangedSinceSave = true;
   }
   public void addObjTRN (int inX, int inY){
       tRNList.addTRN(inX, inY);
       ProjSetting.hasChangedSinceSave = true;
   }
    public void addSelectionELM(int inNumber){
        if(inNumber >=0 && inNumber <= Limit.MAX_ELMS){
            eLMList.eLMs[inNumber].isSelected = true; 
        }
        ProjSetting.hasChangedSinceSave = true;
    }
    public void addSelectionTRN(int inNumber){
        if(inNumber >=0 && inNumber <= Limit.MAX_TRNS){
            tRNList.tRNs[inNumber].isSelected = true; 
        }
        ProjSetting.hasChangedSinceSave = true;
    }
    public boolean checkSelectionELM(int inNumber){
        return eLMList.eLMs[inNumber].isSelected;
    }
    public boolean checkSelectionTRN(int inNumber){
        return tRNList.tRNs[inNumber].isSelected;
    }
    public void clearSelection(){
        for (int i = 0; i < Limit.MAX_ELMS; i ++){
           eLMList.eLMs[i].isSelected = false;
        }
        for (int i = 0; i < Limit.MAX_TRNS; i ++){
           tRNList.tRNs[i].isSelected = false;
        }
    }
    /**
     * Not written,  intend to copy selected ELMS and TRNS to clipboard in sacii (unicode) format tab delimited
     */
    public void copySelectiontoClipboard(){
        
        
    }
    
    
    /**
     * only single delete tested to date
     * should remove all selected ELMS and TRNS. will be called from main window
     * using delete key
     */
    public void deleteSelection(){
        for (int i = 0; i < eLMList.count; i ++){
           if (checkSelectionELM(i)){
               removeELM(i);
           }
        }
        for (int i = 0; i < tRNList.count; i ++){
           if (checkSelectionTRN(i)){
               removeTRN(i);
           }
        }
        ProjSetting.hasChangedSinceSave = true;
    }
   
   public void removeELM (int inNumber){
       eLMList.removeELM(inNumber);
       tRNList.removeELM(inNumber);
       ProjSetting.hasChangedSinceSave = true;
   }
   public void removeTRN (int tRNNumber){
       eLMList.removeTRN(tRNNumber);
       tRNList.removeTRN(tRNNumber);
       ProjSetting.hasChangedSinceSave = true;
   }
   
   public void moveObjELM (int inX, int inY, int inNumber){
       eLMList.eLMs[inNumber].x = inX;
       eLMList.eLMs[inNumber].y = inY;
       eLMList.eLMs[inNumber].hitBox.setLocation(inX - eLMList.eLMs[inNumber].hitBox.getSize().width/2, inY - eLMList.eLMs[inNumber].hitBox.getSize().height/2);
       ProjSetting.hasChangedSinceSave = true;
   }
   public void moveObjTRN(int inX, int inY, int inNumber){
       tRNList.tRNs[inNumber].x = inX;
       tRNList.tRNs[inNumber].y = inY;
       tRNList.tRNs[inNumber].hitBox.setLocation(inX - tRNList.tRNs[inNumber].hitBox.getSize().width/2, inY - tRNList.tRNs[inNumber].hitBox.getSize().height/2);
       ProjSetting.hasChangedSinceSave = true;
   }
   /**
    * method for adding ELMs and TRNs direct from clipboard, indended to allow
    * transfer between 2 instances of McWBalance
    */
   public void pasteFromClipBoard(){
       
       ProjSetting.hasChangedSinceSave = true;
   }
   
   public ObjELM getObjELM (int inNumber){
       return eLMList.eLMs[inNumber];
   }
   public ObjTRN getObjTRN (int inNumber){
       return tRNList.tRNs[inNumber];
   }

   public void setObjELM (int inNumber, ObjELM inObjELM){
       // note that dimensions of the box need to be applied here since ObjELMList does not have access to the Icon Library 
       inObjELM.hitBox.setLocation(inObjELM.x - inObjELM.hitBox.getSize().width/2, inObjELM.y - inObjELM.hitBox.getSize().height/2);  
       eLMList.setObjELM (inNumber, inObjELM); // passes command to active object list; 
       ProjSetting.hasChangedSinceSave = true;
   }
   public void setObjTRN (int inNumber, ObjTRN inObjTRN){
       tRNList.setObjTRN (inNumber, inObjTRN);
       ProjSetting.hasChangedSinceSave = true;
   }
   

    public int checkELMHit (int inX, int inY){ // will need updating to allow for TRNs... 
        for (int i = 0; i < eLMList.count; i++){
            if(eLMList.eLMs[i].hitBox.x <= inX && eLMList.eLMs[i].hitBox.x + eLMList.eLMs[i].hitBox.width >= inX){
                    if(eLMList.eLMs[i].hitBox.y <= inY && eLMList.eLMs[i].hitBox.y + eLMList.eLMs[i].hitBox.height >= inY){
                    return i;  
                }
            }
        }
        return -1;  
       
   }
   public int checkTRNHit (int inX, int inY){ 
       for (int i = 0; i < tRNList.count; i++){
           if(tRNList.tRNs[i].hitBox.x <= inX && tRNList.tRNs[i].hitBox.x + tRNList.tRNs[i].hitBox.width >= inX){
               if(tRNList.tRNs[i].hitBox.y <= inY && tRNList.tRNs[i].hitBox.y + tRNList.tRNs[i].hitBox.height >= inY){
                   return i;
               }
           }
       }
       return -1;  
   }
   
   
   /**
    * This is the method that calculates the balance given a balance run setting
    * @param s 
    */
   public void solveBalance(){
       
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
        tRNList.initializeResults();
        eLMList.initializeResults();
        
        for (int c = 0; c < ProjSetting.climateScenarios.getRowCount(); c++) {

            for (day = 1; day < ProjSetting.duration; day++) { // must start at 1 since solver will need previous days numbers to work;
                // set month;...
                month = CalcBasics.getMonth(day);

                for (i = 0; i < eLMList.count; i++) {
                    // resets Pond Areas

                    if (eLMList.eLMs[i].hasStorage) {
                        sodTotalVol = eLMList.eLMs[i].resultTotalVolume.daily[day - 1];
                        sodpondArea = eLMList.eLMs[i].dAC.getAreafromVol((int) sodTotalVol);
                    } else {
                        sodpondArea = 0;
                    }

                    // Direct Precip and Evaporation
                    if (eLMList.eLMs[i].hasStorageEvapandPrecip) {
                        eLMList.eLMs[i].resultDirectPrecip.daily[day] = ProjSetting.climateScenarios.climateScenarios[c].precip[day] * sodpondArea / 1000;
                        eLMList.eLMs[i].resultEvaporation.daily[day] = ProjSetting.climateScenarios.climateScenarios[c].evap[day] * sodpondArea / 1000;
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

                for (i = 0; i < ProjSetting.solveOrder.tRNIndex.length; i++) {
                    obj = ProjSetting.solveOrder.tRNIndex[i];
                    switch (ProjSetting.solveOrder.tRNType[i]) {
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
