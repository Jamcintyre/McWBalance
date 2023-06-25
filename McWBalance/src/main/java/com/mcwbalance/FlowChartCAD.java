package com.mcwbalance;

/* TO DO LATERS
- implement controls to prevent negitive origin corridinates for boxes

*/

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
    static final int NUMBER_OF_SHEETS_HORIZONTAL = 1;
    /**
     * Defines number of rows of Title Blocks to layout in Cad Space
     */
    static final int NUMBER_OF_SHEETS_VERTICAL = 2; 
    /**
     * Sets working width of CAD Window
     */
    static final int CAD_AREA_WIDTH = TitleBlockTabloidFigure.PAGE_DIMENSION_WIDTH * NUMBER_OF_SHEETS_HORIZONTAL + 1 + NUMBER_OF_SHEETS_HORIZONTAL; 
    /**
     * Sets working height of CAD Window
     */
    static final int CAD_AREA_HEIGHT = TitleBlockTabloidFigure.PAGE_DIMENSION_HEIGHT * NUMBER_OF_SHEETS_VERTICAL + 1 + NUMBER_OF_SHEETS_VERTICAL;
    
    static boolean titleBlockVisible = true; 
    
    int successflag = 0; 
    
    /**
     * @Depreciated
     */
    ImageLib imageLib = new ImageLib(); // pulls in library of Icons; 
    ObjELMList elmList = new ObjELMList();
    ObjTRNList tRNList = new ObjTRNList();
    
    private int drawX = 0;
    private int drawY = 0;
    
    private Dimension eLMdim = new Dimension(0,0); // variable for holding dimesion of origin elm
    private int eLMx =0;
    private int eLMy =0;
    private Rectangle eLMrect = new Rectangle(); // used in flow line drawing
    
    private Dimension tRNdim = new Dimension(20,20); // placeholder sizing
    private int tRNx =0;
    private int tRNy =0;
    private Rectangle tRNrect = new Rectangle(); // used in flow line drawing
    
    private FlowChartLines fpline = new FlowChartLines(); // sets up a bin to hold a polyline
    
    BasicStroke FLOW_LINE = new BasicStroke(1);
    BasicStroke THIN_LINE = new BasicStroke(1);
    BasicStroke THICK_LINE = new BasicStroke(3);
    Color DEFAULT = Color.BLACK;
    Color SELECTED = new Color(153,209,255);
    
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
   
    public static double zoomscale = 0.5; 
    public static int drawdate = -1; 
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // its a bit unclear what this will all do, but Graphics 2D is needed to allow for line thicknesses
        
        // note items drawn before setting the transform will ignore the transform... 
        AffineTransform at = new AffineTransform();
        at.setToScale(zoomscale, zoomscale);
        g2.transform(at);
        
        if (titleBlockVisible){
            int osetX = 1;
            int osetY = 1; 
            for (int irow = 0; irow < NUMBER_OF_SHEETS_VERTICAL; irow ++){
                osetX = 1;
                for (int icol = 0; icol < NUMBER_OF_SHEETS_HORIZONTAL; icol ++){
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.setStroke(THIN_LINE);
                    g2.drawRect(osetX - 1, osetY - 1, 
                            TitleBlockTabloidFigure.PAGE_DIMENSION_WIDTH+2, 
                            TitleBlockTabloidFigure.PAGE_DIMENSION_HEIGHT+2);

                    g2.setColor(TitleBlockTabloidFigure.TITLE_BLOCK_COLOR);
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
        g.setFont(lfont); // sets font for Element Names so it isn't repeated in the loop.
        for (int i = 0; i < elmList.count; i++){// draws all elements in ELM list
            if(elmList.eLMs[i].getState(drawdate) != "INACTIVE"){
                drawX = elmList.eLMs[i].hitBox.x;
                drawY = elmList.eLMs[i].hitBox.y;
                elmList.eLMs[i].setSpriteState(elmList.eLMs[i].getState(drawdate));
                g2.drawImage(elmList.eLMs[i].objSprite, elmList.eLMs[i].hitBox.x, elmList.eLMs[i].hitBox.y, null);
                
                if(elmList.eLMs[i].isSelected){
                    g2.setStroke(THICK_LINE);
                    g2.setColor(SELECTED);
                    g2.drawRect(drawX, drawY, elmList.eLMs[i].hitBox.width, elmList.eLMs[i].hitBox.height);
                    g2.setColor(DEFAULT);
                    g2.setStroke(THIN_LINE);
                }
                
                nameWidthDouble = g.getFontMetrics().getStringBounds(elmList.eLMs[i].objname, g).getWidth();
                nameWidth = (int)nameWidthDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
                nameHeightDouble = g.getFontMetrics().getStringBounds(elmList.eLMs[i].objname, g).getHeight();
                nameHeight = (int)nameHeightDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
                // Draws text Lable
                g.setColor(Color.BLACK); // Draws Shadow that will be drawn over
                g.fillRect( elmList.eLMs[i].x - nameWidth/2 - lpadding + lshadow, drawY + elmList.eLMs[i].objSprite.getHeight() + vlableoffset + lshadow, nameWidth + 2*lpadding, nameHeight + lpadding); // draws whiteout
                g.setColor(Color.WHITE); // whiteout behind text
                g.fillRect( elmList.eLMs[i].x - nameWidth/2 - lpadding, drawY + elmList.eLMs[i].objSprite.getHeight() + vlableoffset, nameWidth + 2*lpadding, nameHeight + lpadding); // draws whiteout 
                g.setColor(Color.BLACK); // border around text
                g.drawRect( elmList.eLMs[i].x - nameWidth/2 - lpadding, drawY + elmList.eLMs[i].objSprite.getHeight() + vlableoffset, nameWidth + 2*lpadding, nameHeight + lpadding); // draws whiteout 
                g.drawString(elmList.eLMs[i].objname, elmList.eLMs[i].x - nameWidth/2, drawY + elmList.eLMs[i].objSprite.getHeight() + nameHeight + vlableoffset); // Strings draw up from the bottom, opposite of rectangles and images... 
            }
            
        }
        g.setFont(mfont); 
        
        
        g2.setStroke(FLOW_LINE);
        for (int i = 0; i < tRNList.count; i++){
            nameWidthDouble = g.getFontMetrics().getStringBounds(tRNList.tRNs[i].objname, g).getWidth();
            nameWidth = (int)nameWidthDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
            tRNdim = tRNList.tRNs[i].hitBox.getSize();
            tRNx = tRNList.tRNs[i].x;
            tRNy = tRNList.tRNs[i].y;
            tRNrect.setBounds(tRNx - tRNdim.width/2, tRNy - tRNdim.height/2, tRNdim.width, tRNdim.height); 
            
            
            
            nameWidthDouble = g.getFontMetrics().getStringBounds(tRNList.tRNs[i].objname, g).getWidth();
            nameHeightDouble = g.getFontMetrics().getStringBounds(tRNList.tRNs[i].objname, g).getHeight();
            nameWidth = (int)nameWidthDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
            nameHeight = (int)nameHeightDouble;
            
            // draws inflow line
            if (tRNList.tRNs[i].inObjNumber > -1){ // checks to see if inflow line should be drawn
                eLMdim = elmList.eLMs[tRNList.tRNs[i].inObjNumber].hitBox.getSize();
                eLMx = elmList.eLMs[tRNList.tRNs[i].inObjNumber].x; // only used to make code easier to read,
                eLMy = elmList.eLMs[tRNList.tRNs[i].inObjNumber].y; // only used to make code easier to read,
                eLMrect.setBounds(eLMx - eLMdim.width/2, eLMy - eLMdim.height/2, eLMdim.width, eLMdim.height);
                // This calculates the route for the inflow line.
                fpline.setRoute(eLMrect, tRNList.tRNs[i].inSideFrom, tRNList.tRNs[i].inSideFromOset, tRNrect, tRNList.tRNs[i].inSideTo, 0, minLineLength); // Note OSets will be used later to allow multiplbe lines to same box
                g.setColor(Color.BLACK);
                g.drawPolyline(fpline.xPoints, fpline.yPoints, fpline.nPoints); // draws line
                g.fillPolygon(fpline.arwxPoints, fpline.arwyPoints, fpline.ARROW_NPOINTS); // draws Arrow; 

            }
            // Draws outflow line
            if (tRNList.tRNs[i].outObjNumber > -1){ // checks to see if outflow line should be drawn
                eLMdim = elmList.eLMs[tRNList.tRNs[i].outObjNumber].hitBox.getSize(); //pulls the dimension of the icon that will be used to draw the in object
                eLMx = elmList.eLMs[tRNList.tRNs[i].outObjNumber].x; // only used to make code easier to read,
                eLMy = elmList.eLMs[tRNList.tRNs[i].outObjNumber].y; // only used to make code easier to read,
                eLMrect.setBounds(eLMx - eLMdim.width/2, eLMy - eLMdim.height/2, eLMdim.width, eLMdim.height);
                // This calculates the route for the inflow line.
                fpline.setRoute(tRNrect, tRNList.tRNs[i].outSideFrom, 0, eLMrect, tRNList.tRNs[i].outSideTo, tRNList.tRNs[i].outSideToOset, minLineLength); // Note OSets will be used later to allow multiplbe lines to same box
                g.setColor(Color.BLACK);// Placeholder of using colour until end arrow is drawn
                g.drawPolyline(fpline.xPoints, fpline.yPoints, fpline.nPoints);
                g.fillPolygon(fpline.arwxPoints, fpline.arwyPoints, fpline.ARROW_NPOINTS); // draws Arrow; 
            }
            // Draws text Lable
            g.setColor(Color.WHITE);
            g.fillRect( tRNList.tRNs[i].x - nameWidth/2, tRNrect.y - vlableoffset - nameHeight, nameWidth, nameHeight); // draws whiteout 
            g.setColor(Color.BLACK);
            g.drawString(tRNList.tRNs[i].objname, tRNList.tRNs[i].x - nameWidth/2, tRNrect.y - vlableoffset); // Text draws from bottom up!, opposite of other objects
            
            // draws the Box that represents the transfer
            g.setColor(Color.WHITE); // Draws Whiteout behind box
            g.fillRect(tRNrect.x, tRNrect.y, tRNrect.width, tRNrect.height);
            g.setColor(Color.BLACK); // will need to size this box to match Normal KP flowsheets
            g.drawRoundRect(tRNrect.x, tRNrect.y, tRNrect.width, tRNrect.height, 5, 5);
            g.drawLine(tRNrect.x, tRNrect.y + tRNrect.height/3, tRNrect.x+tRNrect.width, tRNrect.y + tRNrect.height/3);
            g.drawLine(tRNrect.x, tRNrect.y + tRNrect.height*2/3, tRNrect.x+tRNrect.width, tRNrect.y + tRNrect.height*2/3);
            
            // draws the box around the TRN if selected
            if(tRNList.tRNs[i].isSelected){
                g2.setStroke(THICK_LINE);
                g2.setColor(SELECTED);
                g2.drawRect(tRNList.tRNs[i].hitBox.x, tRNList.tRNs[i].hitBox.y, tRNList.tRNs[i].hitBox.width, tRNList.tRNs[i].hitBox.height);
                g2.setColor(DEFAULT);
                g2.setStroke(THIN_LINE);
            }
            
            g.setFont(sfont); // Switches to smallest font for Numbers
            // Plots Annual Number
            
            //pVolAnn = String.valueOf(tRNList.tRNs[i].plotVolperAnnum);
            pVolAnn = ProjSetting.fmtAnn.format(tRNList.tRNs[i].plotVolperAnnum);
            pVolDay = ProjSetting.fmtDay.format(tRNList.tRNs[i].plotVolperDay);
            pVolHr = ProjSetting.fmtHr.format(tRNList.tRNs[i].plotVolperHr);
            
            pVolWidth = (int)g.getFontMetrics().getStringBounds(pVolAnn, g).getWidth();
            pVolHeight = (int)g.getFontMetrics().getStringBounds(pVolAnn, g).getHeight();
            g.drawString(pVolAnn, tRNrect.x + tRNrect.width/2 - pVolWidth/2, tRNrect.y + tRNrect.height*1/6 + pVolHeight/2);
            
            pVolWidth = (int)g.getFontMetrics().getStringBounds(pVolDay, g).getWidth();
            g.drawString(pVolDay, tRNrect.x + tRNrect.width/2 - pVolWidth/2, tRNrect.y + tRNrect.height*3/6 + pVolHeight/2);
            
            pVolWidth = (int)g.getFontMetrics().getStringBounds(pVolHr, g).getWidth();
            g.drawString(pVolHr, tRNrect.x + tRNrect.width/2 - pVolWidth/2, tRNrect.y + tRNrect.height*5/6 + pVolHeight/2);

        }
    }
    
   public void addObjELM (int inX, int inY){
       successflag = elmList.AddELM(inX, inY);
   }
   public void addObjTRN (int inX, int inY){
       successflag = tRNList.AddTRN(inX, inY);
   }
    public void addSelectionELM(int inNumber){
        if(inNumber >=0 && inNumber <= ProjSetting.MAX_ELMS){
            elmList.eLMs[inNumber].isSelected = true; 
        }
    }
    public void addSelectionTRN(int inNumber){
        if(inNumber >=0 && inNumber <= ProjSetting.MAX_TRNS){
            tRNList.tRNs[inNumber].isSelected = true; 
        }
    }
    public boolean checkSelectionELM(int inNumber){
        return elmList.eLMs[inNumber].isSelected;
    }
    public boolean checkSelectionTRN(int inNumber){
        return tRNList.tRNs[inNumber].isSelected;
    }
    public void clearSelection(){
        for (int i = 0; i < ProjSetting.MAX_ELMS; i ++){
           elmList.eLMs[i].isSelected = false;
        }
        for (int i = 0; i < ProjSetting.MAX_TRNS; i ++){
           tRNList.tRNs[i].isSelected = false;
        }
    }
   
   public void removeObjELM (int inNumber){
       successflag = elmList.RemoveELM(inNumber);
       tRNList.removeELM(inNumber);
   }
   public void MoveObjELM (int inX, int inY, int inNumber){
       elmList.eLMs[inNumber].x = inX;
       elmList.eLMs[inNumber].y = inY;
       elmList.eLMs[inNumber].hitBox.setLocation(inX - elmList.eLMs[inNumber].hitBox.getSize().width/2, inY - elmList.eLMs[inNumber].hitBox.getSize().height/2);
   }
   public void MoveObjTRN(int inX, int inY, int inNumber){
       tRNList.tRNs[inNumber].x = inX;
       tRNList.tRNs[inNumber].y = inY;
       tRNList.tRNs[inNumber].hitBox.setLocation(inX - tRNList.tRNs[inNumber].hitBox.getSize().width/2, inY - tRNList.tRNs[inNumber].hitBox.getSize().height/2);
   }
   
   public ObjELM GetObjELM (int inNumber){
       return elmList.eLMs[inNumber];
   }
   public ObjTRN GetObjTRN (int inNumber){
       return tRNList.tRNs[inNumber];
   }

   public void SetObjELM (int inNumber, ObjELM inObjELM){
       // note that dimensions of the box need to be applied here since ObjELMList does not have access to the Icon Library 
       inObjELM.hitBox.setLocation(inObjELM.x - inObjELM.hitBox.getSize().width/2, inObjELM.y - inObjELM.hitBox.getSize().height/2);  
       elmList.SetObjELM (inNumber, inObjELM); // passes command to active object list; 
   }
   public void SetObjTRN (int inNumber, ObjTRN inObjTRN){
       tRNList.SetObjTRN (inNumber, inObjTRN);
   }
   

    public int checkELMHit (int inX, int inY){ // will need updating to allow for TRNs... 
        for (int i = 0; i < elmList.count; i++){
            if(elmList.eLMs[i].hitBox.x <= inX && elmList.eLMs[i].hitBox.x + elmList.eLMs[i].hitBox.width >= inX){
                    if(elmList.eLMs[i].hitBox.y <= inY && elmList.eLMs[i].hitBox.y + elmList.eLMs[i].hitBox.height >= inY){
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
}
