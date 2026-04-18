/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.flowchart;


import com.mcwbalance.util.SVGColorMap;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Properties;

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
    
    CadLine lines[];
    
    Properties prop = new Properties();
    

    /**
     *  TODO - need to add handelling for missing data
     * @param svg Takes an SVG formatted XML
     */
    public TitleBlock(Document svg){
            
            NodeList nl = svg.getElementsByTagName("svg");
            
            Element blk = (Element) nl.item(0);
            pageHeight = Integer.parseInt(blk.getAttribute("height"));
            pageWidth = Integer.parseInt(blk.getAttribute("width"));
            NodeList cnl = blk.getElementsByTagName("line");
            lines = new CadLine[cnl.getLength()];
            
            for (int i = 0; i < cnl.getLength(); i++){
                Element ele = (Element) cnl.item(i);
                int x0 = Integer.parseInt(ele.getAttribute("x1"));
                int x1 = Integer.parseInt(ele.getAttribute("x2"));
                int y0 = Integer.parseInt(ele.getAttribute("y1"));
                int y1 = Integer.parseInt(ele.getAttribute("y2"));
                Color cl = SVGColorMap.fromStroke(ele.getAttribute("stroke"));
                Stroke sk = new BasicStroke (Integer.parseInt(ele.getAttribute("stroke-width")));
                
                lines[i] = new CadLine(x0,y0,x1,y1,cl,sk);
            }
    }
 
    /**
     * 
     * @return Page height including margins
     */
    public int getheight(){
         return pageHeight;
    }
    
    /**
     * 
     * @return Page width including margins
     */
    public int getWidth(){
        return pageWidth;
    }
    
    
    /**
     * Used for drawing the current title block to a Graphics2D instance
     * @param g2 Graphics instance to draw the title block onto
     * @param osetx X offset value
     * @param osety y offset value
     */
    public void drawTitleBlock(Graphics2D g2, int osetx, int osety){
        for (CadLine line : lines) {
            g2.setColor(line.getColour());
            g2.setStroke(line.getStroke());
            g2.drawLine(line.getx0(), line.gety0(), line.getx1(), line.gety1());
        }
    }
    
}
