/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.flowchart;

import com.mcwbalance.util.SVGRender;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Used for drawing title block
 * @author Alex
 */
public class TitleBlock {

    int pageHeight;
    int pageWidth;
    
    SVGRender svgr;
    

    /**
     *  TODO - need to add handelling for missing data
     * TODO - handle implement user defined fields. i.e. where does the project
     * name go, client, figure number
     * TODO - Add handeling for multiple svgs in one file i.e. logo
     * @param svg Takes an SVG formatted XML
     */
    public TitleBlock(Document svg) {
        svgr = new SVGRender(svg);
        NodeList nl = svg.getElementsByTagName("svg");
        Element blk = (Element) nl.item(0);
        pageHeight = Integer.parseInt(blk.getAttribute("height"));
        pageWidth = Integer.parseInt(blk.getAttribute("width"));
    }

    /**
     * Used for getting intended page dimensions in pxls
     * @return Page height including margins
     */
    public int getheight(){
         return pageHeight;
    }
    
    /**
     * Used for getting intended page dimensions in pxls
     * @return Page width including margins
     */
    public int getWidth(){
        return pageWidth;
    }
    
    /**
     * Used for drawing the current title block to a Graphics2D instance
     * @param g2 Graphics instance to draw the title block onto
     * @param io not sure if actually needed
     * @param osetx X offset value
     * @param osety y offset value
     */
    public void drawTitleBlock(Graphics2D g2,ImageObserver io, int osetx, int osety){
        
        AffineTransform at = new AffineTransform(); 
        at.setToTranslation(osetx, osety);
        
        //g2.drawRenderedImage(svgr.getImage(), at);
        g2.drawImage(svgr.getImage(), at, io);
        
        
    }
    
}
