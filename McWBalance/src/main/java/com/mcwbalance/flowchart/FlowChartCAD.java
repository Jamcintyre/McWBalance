package com.mcwbalance.flowchart;

/* TO DO LATERS
- implement controls to prevent negitive origin corridinates for boxes

 */
import com.mcwbalance.settings.Preferences;
import com.mcwbalance.project.ProjSetting;
import com.mcwbalance.transfer.TRN;
import com.mcwbalance.transfer.TRNList;
import com.mcwbalance.node.Nod;
import com.mcwbalance.node.NodList;
import com.mcwbalance.project.Project;
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
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.swing.JComponent;

/**
 * Known Bugs font size changes, between transfer lables, must not be setting
 * the font immediately prior to drawing, or there is a scale occuring Object
 * Deletion sometimes deletes extra elements
 *
 *
 * @author Alex McIntyre
 */
public class FlowChartCAD extends JComponent implements Printable{

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
     * Active project which contains the main data model, nodes, transfers,
     * results
     */
    private Project prj;

    private int drawX;
    private int drawY;

    private Dimension eLMdim; // variable for holding dimesion of origin elm
    private int eLMx;
    private int eLMy;
    private Rectangle eLMrect; // used in flow line drawing

    /**
     * TODO - move this to the Template file
     * used for setting size of transfer box
     */
    public final static int TRN_BOX_WIDTH = 60;
    /**
     * TODO - move this to the Template file
     * used for setting size of transfer box
     */
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

    private boolean isPageOutlineVisible = true;

    private int minLineLength; // used to set stublines from objects

    private int vlableoffset;
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
    private Font lfont = new Font("Arial", Font.PLAIN, 14);
    private Font mfont = new Font("Arial", Font.PLAIN, 12);
    private Font sfont = new Font("Arial", Font.PLAIN, 10);
    private int lpadding = 4; // used to set padding around Text Boxs and White outs
    private int lshadow = 4; // used to size the shaddow box shadow
    private int spadding = 2; // used to pad bottom of numbers. 

    public static double zoomscale;
    public static int drawdate;

    public FlowChartCAD(Project project) {
        this.prj = project;

        drawX = 0;
        drawY = 0;

        defaultDrawColor = Preferences.DEFAULT_DRAW_COLOR;
        defaultBGColor = Preferences.DEFAULT_BACKGROUND_COLOR;

        eLMdim = new Dimension(0, 0); // variable for holding dimesion of origin elm
        eLMx = 0;
        eLMy = 0;
        eLMrect = new Rectangle(); // used in flow line drawing

        fpline = new FlowChartLines(); // sets up a bin to hold a polyline

        titleBlockVisible = true;

        tRNdim = new Dimension(TRN_BOX_WIDTH, TRN_BOX_HEIGHT); // placeholder sizing
        tRNx = 0;
        tRNy = 0;
        tRNrect = new Rectangle(); // used in flow line drawing

        FLOW_LINE = new BasicStroke(1);
        THIN_LINE = new BasicStroke(1);
        THICK_LINE = new BasicStroke(3);
        SELECTED = new Color(153, 209, 255);

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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
        }
    
    @Override
    public int print(Graphics g, PageFormat pf, int i) throws PrinterException {
        
        System.out.println(pf.toString());
        System.out.println("page to print" + i);
        
        if (i == 0) {
            return NO_SUCH_PAGE;
        }

        System.out.println("Imageable Height = " + pf.getImageableHeight());
        System.out.println("Imageable Width = " + pf.getImageableWidth());
        System.out.println("Imageable x = " + pf.getImageableX());
        System.out.println("Imageable y = " + pf.getImageableY());

        //grphcs.drawOval(75, 75, 50, 50);
        
        render(g);

        return PAGE_EXISTS;

    }

    /**
     * Calls a constructor to generate a new blank node at a given coordinate
     * @param inX in pxls
     * @param inY in pxls
     */
    public void addObjELM(int inX, int inY) {
        prj.getNodeList().addNode(inX, inY);
        ProjSetting.hasChangedSinceSave = true;
    }

    /**
     * Calls a constructor to generate a new blank transfer at given coordinates
     * @param inX in pxls
     * @param inY in pxls
     */
    public void addObjTRN(int inX, int inY) {
        prj.getTransferList().addTRN(inX, inY);
        ProjSetting.hasChangedSinceSave = true;
    }

    /**
     * Sets a node to selected, used for managing double clicks and selection
     * highlight
     * @param index index of node selected
     */
    public void addSelectionELM(int index) {
        if (index >= 0 && index <= Limit.MAX_NODES) {
            prj.getNodeList().nodes[index].isSelected = true;
        }
        ProjSetting.hasChangedSinceSave = true;
    }

    /**
     * Sets a transfer to selected, used for managing double clicks and selection
     * highlight
     * @param index index of transfer selected
     */
    public void addSelectionTRN(int index) {
        if (index >= 0 && index <= Limit.MAX_TRNS) {
            prj.getTransferList().tRNs[index].isSelected = true;
        }
        ProjSetting.hasChangedSinceSave = true;
    }

    /**
     * Used to check if a node is already selected, used for double clicks
     * @param index index of node
     * @return true if Node is actively selected 
     */
    public boolean checkSelectionELM(int index) {
        return prj.getNodeList().nodes[index].isSelected;
    }

    /**
     * Used to check if a transfer is already selected, used for double clicks
     * @param index index of transfer
     * @return true of transfer is actively selected
     */
    public boolean checkSelectionTRN(int index) {
        return prj.getTransferList().tRNs[index].isSelected;
    }

    /**
     * Used to remove active selection on nodes and transfers
     */
    public void clearSelection() {
        for (int i = 0; i < Limit.MAX_NODES; i++) {
            prj.getNodeList().nodes[i].isSelected = false;
        }
        for (int i = 0; i < Limit.MAX_TRNS; i++) {
            prj.getTransferList().tRNs[i].isSelected = false;
        }
    }

    /**
     * TODO
     * Not written, intend to copy selected ELMS and TRNS to clipboard in ascii
     * (unicode) format tab delimited
     */
    public void copySelectiontoClipboard() {

    }

    /**
     * TODO
     * only single delete tested to date should remove all selected ELMS and
     * TRNS. will be called from main window using delete key
     */
    public void deleteSelection() {
        for (int i = 0; i < prj.getNodeList().count; i++) {
            if (checkSelectionELM(i)) {
                removeELM(i);
            }
        }
        for (int i = 0; i < prj.getTransferList().count; i++) {
            if (checkSelectionTRN(i)) {
                removeTRN(i);
            }
        }
        ProjSetting.hasChangedSinceSave = true;
    }
    
    /**
     * removes a node from the active node list
     * @param index index of node to remove
     */
    public void removeELM(int index) {
        prj.getNodeList().removeNode(index);
        prj.getTransferList().removeELM(index);
        ProjSetting.hasChangedSinceSave = true;
    }
    
    /**
     * removes a transfer from the active node list
     * @param index  index of transfer to remove
     */
    public void removeTRN(int index) {
        prj.getNodeList().removeTRN(index);
        prj.getTransferList().removeTRN(index);
        ProjSetting.hasChangedSinceSave = true;
    }
    
    /**
     * This is where the bulk of the work happens
     * Intended to provide common rendering between display and print
     * 
     */
    private void render(Graphics g) {

        NodList eLMList = prj.getNodeList();
        TRNList tRNList = prj.getTransferList();

        Graphics2D g2 = (Graphics2D) g;

        // note items drawn before setting the transform will ignore the transform... 
        AffineTransform at = new AffineTransform();
        at.setToScale(zoomscale, zoomscale); // Screen resolution is 96 dpi, paper is 300
        g2.transform(at);
        g2.setColor(defaultDrawColor);
        if (titleBlockVisible) {
            int osetX = 1;
            int osetY = 1;
            for (int irow = 0; irow < NUMBER_OF_SHEETS_VERTICAL; irow++) {
                osetX = 1;
                for (int icol = 0; icol < NUMBER_OF_SHEETS_HORIZONTAL; icol++) {

                    if (isPageOutlineVisible) {
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.setStroke(THIN_LINE);
                        g2.drawRect(osetX - 1, osetY - 1,
                                TitleBlockTabloidFigure.PAGE_DIMENSION_WIDTH + 2,
                                TitleBlockTabloidFigure.PAGE_DIMENSION_HEIGHT + 2);
                    }

                    prj.getTitleblock().drawTitleBlock(g2, osetX, osetY);

                    osetX = osetX + TitleBlockTabloidFigure.PAGE_DIMENSION_WIDTH + 1;
                }
                osetY = osetY + TitleBlockTabloidFigure.PAGE_DIMENSION_HEIGHT + 1;
            }
        }

        // draws the Elements
        g2.setStroke(THIN_LINE);
        g2.setFont(lfont); // sets font for Element Names so it isn't repeated in the loop.
        for (int i = 0; i < eLMList.count; i++) {// draws all elements in ELM list
            if (eLMList.nodes[i].getState(drawdate) != "INACTIVE") {
                drawX = eLMList.nodes[i].hitBox.x;
                drawY = eLMList.nodes[i].hitBox.y;
                eLMList.nodes[i].setSpriteState(eLMList.nodes[i].getState(drawdate));
                g2.drawImage(eLMList.nodes[i].objSprite, eLMList.nodes[i].hitBox.x, eLMList.nodes[i].hitBox.y, null);

                if (eLMList.nodes[i].isSelected) {
                    g2.setStroke(THICK_LINE);
                    g2.setColor(SELECTED);
                    g2.drawRect(drawX, drawY, eLMList.nodes[i].hitBox.width, eLMList.nodes[i].hitBox.height);
                    g2.setColor(defaultDrawColor);
                    g2.setStroke(THIN_LINE);
                }

                nameWidthDouble = g.getFontMetrics().getStringBounds(eLMList.nodes[i].objname, g2).getWidth();
                nameWidth = (int) nameWidthDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
                nameHeightDouble = g.getFontMetrics().getStringBounds(eLMList.nodes[i].objname, g2).getHeight();
                nameHeight = (int) nameHeightDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
                // Draws text Lable
                g2.setColor(defaultDrawColor); // Draws Shadow that will be drawn over
                g2.fillRect(eLMList.nodes[i].x - nameWidth / 2 - lpadding + lshadow, drawY + eLMList.nodes[i].objSprite.getHeight() + vlableoffset + lshadow, nameWidth + 2 * lpadding, nameHeight + lpadding); // draws whiteout
                g2.setColor(defaultBGColor); // whiteout behind text
                g2.fillRect(eLMList.nodes[i].x - nameWidth / 2 - lpadding, drawY + eLMList.nodes[i].objSprite.getHeight() + vlableoffset, nameWidth + 2 * lpadding, nameHeight + lpadding); // draws whiteout 
                g2.setColor(defaultDrawColor); // border around text
                g2.drawRect(eLMList.nodes[i].x - nameWidth / 2 - lpadding, drawY + eLMList.nodes[i].objSprite.getHeight() + vlableoffset, nameWidth + 2 * lpadding, nameHeight + lpadding); // draws whiteout 
                g2.drawString(eLMList.nodes[i].objname, eLMList.nodes[i].x - nameWidth / 2, drawY + eLMList.nodes[i].objSprite.getHeight() + nameHeight + vlableoffset); // Strings draw up from the bottom, opposite of rectangles and images... 
            }

        }
        g2.setFont(mfont);

        g2.setStroke(FLOW_LINE);
        for (int i = 0; i < tRNList.count; i++) {
            g2.setFont(mfont); // need to re-set font every loop
            nameWidthDouble = g2.getFontMetrics().getStringBounds(tRNList.tRNs[i].objname, g2).getWidth();
            nameWidth = (int) nameWidthDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
            tRNdim = tRNList.tRNs[i].hitBox.getSize();
            tRNx = tRNList.tRNs[i].x;
            tRNy = tRNList.tRNs[i].y;
            tRNrect.setBounds(tRNx - tRNdim.width / 2, tRNy - tRNdim.height / 2, tRNdim.width, tRNdim.height);

            nameWidthDouble = g2.getFontMetrics().getStringBounds(tRNList.tRNs[i].objname, g2).getWidth();
            nameHeightDouble = g2.getFontMetrics().getStringBounds(tRNList.tRNs[i].objname, g2).getHeight();
            nameWidth = (int) nameWidthDouble; // Because for some dumb reason graphics font metrics returns double whent it works in pixels!
            nameHeight = (int) nameHeightDouble;

            // draws inflow line
            if (tRNList.tRNs[i].inObjNumber > -1) { // checks to see if inflow line should be drawn
                eLMdim = eLMList.nodes[tRNList.tRNs[i].inObjNumber].hitBox.getSize();
                eLMx = eLMList.nodes[tRNList.tRNs[i].inObjNumber].x; // only used to make code easier to read,
                eLMy = eLMList.nodes[tRNList.tRNs[i].inObjNumber].y; // only used to make code easier to read,
                eLMrect.setBounds(eLMx - eLMdim.width / 2, eLMy - eLMdim.height / 2, eLMdim.width, eLMdim.height);
                // This calculates the route for the inflow line.
                fpline.setRoute(eLMrect, tRNList.tRNs[i].inSideFrom.toString(), tRNList.tRNs[i].inSideFromOset, tRNrect, tRNList.tRNs[i].inSideTo.toString(), 0, minLineLength); // Note OSets will be used later to allow multiplbe lines to same box
                g2.setColor(defaultDrawColor);
                g2.drawPolyline(fpline.xPoints, fpline.yPoints, fpline.nPoints); // draws line
                g2.fillPolygon(fpline.arwxPoints, fpline.arwyPoints, fpline.ARROW_NPOINTS); // draws Arrow; 

            }
            // Draws outflow line
            if (tRNList.tRNs[i].outObjNumber > -1) { // checks to see if outflow line should be drawn
                eLMdim = eLMList.nodes[tRNList.tRNs[i].outObjNumber].hitBox.getSize(); //pulls the dimension of the icon that will be used to draw the in object
                eLMx = eLMList.nodes[tRNList.tRNs[i].outObjNumber].x; // only used to make code easier to read,
                eLMy = eLMList.nodes[tRNList.tRNs[i].outObjNumber].y; // only used to make code easier to read,
                eLMrect.setBounds(eLMx - eLMdim.width / 2, eLMy - eLMdim.height / 2, eLMdim.width, eLMdim.height);
                // This calculates the route for the inflow line.
                fpline.setRoute(tRNrect, tRNList.tRNs[i].outSideFrom.toString(), 0, eLMrect, tRNList.tRNs[i].outSideTo.toString(), tRNList.tRNs[i].outSideToOset, minLineLength); // Note OSets will be used later to allow multiplbe lines to same box
                g2.setColor(defaultDrawColor);// Placeholder of using colour until end arrow is drawn
                g2.drawPolyline(fpline.xPoints, fpline.yPoints, fpline.nPoints);
                g2.fillPolygon(fpline.arwxPoints, fpline.arwyPoints, fpline.ARROW_NPOINTS); // draws Arrow; 
            }
            // Draws text Lable
            g2.setColor(defaultBGColor);
            g2.fillRect(tRNList.tRNs[i].x - nameWidth / 2, tRNrect.y - vlableoffset - nameHeight, nameWidth, nameHeight); // draws whiteout 
            g2.setColor(defaultDrawColor);
            g2.drawString(tRNList.tRNs[i].objname, tRNList.tRNs[i].x - nameWidth / 2, tRNrect.y - vlableoffset); // Text draws from bottom up!, opposite of other objects

            // draws the Box that represents the transfer
            g2.setColor(defaultBGColor); // Draws Whiteout behind box
            g2.fillRect(tRNrect.x, tRNrect.y, tRNrect.width, tRNrect.height);
            g2.setColor(defaultDrawColor); // will need to size this box to match Normal KP flowsheets
            g2.drawRoundRect(tRNrect.x, tRNrect.y, tRNrect.width, tRNrect.height, 5, 5);
            g2.drawLine(tRNrect.x, tRNrect.y + tRNrect.height / 3, tRNrect.x + tRNrect.width, tRNrect.y + tRNrect.height / 3);
            g2.drawLine(tRNrect.x, tRNrect.y + tRNrect.height * 2 / 3, tRNrect.x + tRNrect.width, tRNrect.y + tRNrect.height * 2 / 3);

            // draws the box around the TRN if selected
            if (tRNList.tRNs[i].isSelected) {
                g2.setStroke(THICK_LINE);
                g2.setColor(SELECTED);
                g2.drawRect(tRNList.tRNs[i].hitBox.x, tRNList.tRNs[i].hitBox.y, tRNList.tRNs[i].hitBox.width, tRNList.tRNs[i].hitBox.height);
                g2.setColor(defaultDrawColor);
                g2.setStroke(THIN_LINE);
            }

            g2.setFont(sfont); // Switches to smallest font for Numbers
            // Plots Annual Number

            //pVolAnn = String.valueOf(tRNList.tRNs[i].plotVolperAnnum);
            pVolAnn = prj.getProjectSetting().getFmtAnn().format(tRNList.tRNs[i].plotVolperAnnum);
            pVolDay = prj.getProjectSetting().getFmtDay().format(tRNList.tRNs[i].plotVolperDay);
            pVolHr = prj.getProjectSetting().getFmtHr().format(tRNList.tRNs[i].plotVolperHr);

            pVolWidth = (int) g2.getFontMetrics().getStringBounds(pVolAnn, g2).getWidth();
            pVolHeight = (int) g2.getFontMetrics().getStringBounds(pVolAnn, g2).getHeight();
            g2.drawString(pVolAnn, tRNrect.x + tRNrect.width / 2 - pVolWidth / 2, tRNrect.y + tRNrect.height * 1 / 6 + pVolHeight / 2);

            pVolWidth = (int) g2.getFontMetrics().getStringBounds(pVolDay, g2).getWidth();
            g2.drawString(pVolDay, tRNrect.x + tRNrect.width / 2 - pVolWidth / 2, tRNrect.y + tRNrect.height * 3 / 6 + pVolHeight / 2);

            pVolWidth = (int) g2.getFontMetrics().getStringBounds(pVolHr, g2).getWidth();
            g2.drawString(pVolHr, tRNrect.x + tRNrect.width / 2 - pVolWidth / 2, tRNrect.y + tRNrect.height * 5 / 6 + pVolHeight / 2);

        }
    }

    
    /**
     * Adjusts the location of a node on the flowchart
     * @param inX X coordinate in pxls
     * @param inY Y coordinate in pxls
     * @param index index of node to move
     */
    public void moveObjELM(int inX, int inY, int index) {
        prj.getNodeList().nodes[index].x = inX;
        prj.getNodeList().nodes[index].y = inY;
        prj.getNodeList().nodes[index].hitBox.setLocation(
                inX - prj.getNodeList().nodes[index].hitBox.getSize().width / 2,
                inY - prj.getNodeList().nodes[index].hitBox.getSize().height / 2);
        ProjSetting.hasChangedSinceSave = true;
    }
    
    /**
     * Adjusts the location of a transfer box
     * @param inX X coordinate in pxls
     * @param inY Y coordinate in pxls
     * @param index index of transfer to move
     */
    public void moveObjTRN(int inX, int inY, int index) {
        prj.getTransferList().tRNs[index].x = inX;
        prj.getTransferList().tRNs[index].y = inY;
        prj.getTransferList().tRNs[index].hitBox.setLocation(
                inX - prj.getTransferList().tRNs[index].hitBox.getSize().width / 2,
                inY - prj.getTransferList().tRNs[index].hitBox.getSize().height / 2);
        ProjSetting.hasChangedSinceSave = true;
    }

    /**
     * method for adding ELMs and TRNs direct from clipboard, intended to allow
     * transfer between 2 instances of McWBalance
     */
    public void pasteFromClipBoard() {

        ProjSetting.hasChangedSinceSave = true;
    }

    /**
     * overwrites the target node in the active node list with the provided node
     * @param index index of node to overwrite
     * @param inObjELM source node
     */
    public void setObjELM(int index, Nod inObjELM) {
        // note that dimensions of the box need to be applied here since ObjELMList does not have access to the Icon Library 
        inObjELM.hitBox.setLocation(inObjELM.x - inObjELM.hitBox.getSize().width / 2, inObjELM.y - inObjELM.hitBox.getSize().height / 2);
        prj.getNodeList().set(index, inObjELM); // passes command to active object list; 
        ProjSetting.hasChangedSinceSave = true;
    }

    /**
     * overwrites the target transfer in the active transfer list with the provided transfer
     * @param index index on of transfer to overwrite
     * @param inObjTRN source transfer
     */
    public void setObjTRN(int index, TRN inObjTRN) {
        prj.getTransferList().setObjTRN(index, inObjTRN);
        ProjSetting.hasChangedSinceSave = true;
    }

    /**
     * Used for finding if a set of coordinates make contact with a node, and 
     * if so returns the node that was hit
     * @param inX in pxls
     * @param inY in pxls
     * @return index of node that is hit, or -1 for no hit
     */
    public int checkELMHit(int inX, int inY) { // will need updating to allow for TRNs... 
        for (int i = 0; i < prj.getNodeList().count; i++) {
            if (prj.getNodeList().nodes[i].hitBox.x <= inX && prj.getNodeList().nodes[i].hitBox.x + prj.getNodeList().nodes[i].hitBox.width >= inX) {
                if (prj.getNodeList().nodes[i].hitBox.y <= inY && prj.getNodeList().nodes[i].hitBox.y + prj.getNodeList().nodes[i].hitBox.height >= inY) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Used for finding if a set of coordinates make contact with a transfer, and 
     * if so returns the transfer that was hit
     * @param inX in pxls
     * @param inY in pxls
     * @return index of transfer that is hit, or -1 for no hit
     */
    public int checkTRNHit(int inX, int inY) {
        for (int i = 0; i < prj.getTransferList().count; i++) {
            if (prj.getTransferList().tRNs[i].hitBox.x <= inX && prj.getTransferList().tRNs[i].hitBox.x + prj.getTransferList().tRNs[i].hitBox.width >= inX) {
                if (prj.getTransferList().tRNs[i].hitBox.y <= inY && prj.getTransferList().tRNs[i].hitBox.y + prj.getTransferList().tRNs[i].hitBox.height >= inY) {
                    return i;
                }
            }
        }
        return -1;
    }

}
