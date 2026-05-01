/*

With exclusion of code under SVGRender(Document svg), specific to case "A" 
converting an SVG arc to a Java Arc

Copyright (c) 2026, Alex McIntyre
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. All advertising materials mentioning features or use of this software
   must display the following acknowledgement:
   This product includes software developed by Alex McIntyre.
4. Neither the name of the organization nor the
   names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.mcwbalance.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for rendering an SVG to a graphics layer The SVG is broken down into
 * the following components Ellipse2D.Double; (circles and ellipse)
 * Line2D.Double; (line) Path2D.Double; (path, polygon, and polyline)
 * RoundRectangle2D.Double; (rect) Ignores filters and text
 * Note that for Paths, built in Path2D does not have support for arc's, an approximation
 * is made with curveTo instead, however, this will not be an exact match. 
 *
 *  TODO - Test muli pass through (Element) for sizing vs arraylist to array
 *  TODO - Test storage as arrays vs storage as a rendered graphics layer or buffered Image
 * @author Alex McIntyre
 */
public class SVGRender {
    
    /**
     * Recognized SVG shapes, note does not include text
     */
    enum Shapes{
        rect,
        circle,
        ellipse,
        line,
        polyline,
        polygon,
        path;
        
        public static Shapes findbyName(String name){
            for (Shapes shapes : values()){
                if(shapes.name().equalsIgnoreCase(name)){
                    return shapes;
                }
            }
            return null;
        }
    }
    /**
     * Recognized SVG path commands
     */
    enum PathCom {
        M,// = moveto (move from one point to another point)
        L,// = lineto (create a line)
        H,// = horizontal lineto (create a horizontal line)
        V,// = vertical lineto (create a vertical line)
        C,// = curveto (create a curve)
        S,// = smooth curveto (create a smooth curve)
        Q,// = quadratic Bézier curve (create a quadratic Bézier curve)
        T,// = smooth quadratic Bézier curveto (create a smooth quadratic Bézier curve)
        A,// = elliptical Arc (create a elliptical arc)
        Z;// = closepath (close the path)
        
        /**
         * Checks to see if the provided charactor is a valid svg command
         * @param ch charactor to check
         * @return true if command exists, false if not
         */
        public static boolean isCommand(char ch) {
            for (PathCom cmds : values()) {
                if (cmds.name().equalsIgnoreCase(String.valueOf(ch))) {
                    return true;
                }
            }
            return false;
        }
        /**
         * Checks to see if the provided charactor is a valid svg command
         * @param st String containing charactor to check
         * @return true if command exists, false if not
         */
        public static boolean isCommand(String st) {
            for (PathCom cmds : values()) {
                if (cmds.name().equalsIgnoreCase(st)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    
    Color color[];
    Boolean filled[];
    Color fillcolor[];
    Boolean outline[];
    Stroke stroke[];
    Shapes shape[];
    
    Ellipse2D.Double ellipse[];
    Line2D.Double lines[];
    Path2D.Double paths[];
    RoundRectangle2D.Double rects[];

    int number;
    
    /**
     * Generates a graphics instance from a provided SVG
     * TODO - Allow Style definition of stroke and color
     * TODO - implement rest of the shapes 
     * TODO - add try catches to dismiss invalid shapes
     * TODO - if no Style or outline 
     * TODO - Test vibe coded arch 
     * TODO - Fix smooth curve in path to allow chains. 
     * The SVG is broken down into the following components
     * Ellipse2D.Double; (circles and ellipse)
     * Line2D.Double; (line)
     * Path2D.Double; (path, polygon, and polyline)
     * RoundRectangle2D.Double;  (rect)
     * Ignores filters and text
     * 
     * @param svg node to render
     */
    public SVGRender(Document svg){
        ArrayList<Element> eles= new ArrayList<Element>();
        NodeList svgs = svg.getElementsByTagName("svg");
        number = 0;

        for (int c = 0; c < svgs.getLength(); c++) {
            NodeList cnl = ((Element) svgs.item(c)).getChildNodes();
            for (int d = 0; d < cnl.getLength(); d++) {
                if (cnl.item(d).getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) cnl.item(d);
                    if (Shapes.findbyName(e.getTagName()) != null) {
                        eles.add(e);
                    }
                }
            }
        }
        //DEBUG
        System.out.println("SVGRender.java public SVGRender(Document svg) - found "+ eles.size()+ " Shape Elements in input svg");
        number = eles.size();
        color = new Color[number];
        filled = new Boolean[number];
        fillcolor = new Color[number];
        outline = new Boolean[number];
        stroke = new Stroke[number];
        shape = new Shapes[number];

        //Note circles are ellipse
        int nellipse = 0;
        int nlines = 0;
        int npaths = 0;
        int nrects = 0;
              
        // first pass sizes the arrays
        for (int i = 0; i < number; i++){
            switch (Shapes.valueOf(eles.get(i).getTagName())){
                case Shapes.circle -> nellipse++;
                case Shapes.ellipse -> nellipse++;
                case Shapes.line -> nlines++;
                case Shapes.path -> npaths++;
                case Shapes.polyline -> npaths++;
                case Shapes.polygon -> npaths++;
                case Shapes.rect -> nrects++;
            }
        }
        
        System.out.println("SVGRender.java public SVGRender(Document svg) - found "+ nellipse + " ellipses in this.eles");
        System.out.println("SVGRender.java public SVGRender(Document svg) - found "+ nlines + " lines in this.eles");
        
        ellipse = new Ellipse2D.Double[nellipse];
        lines = new Line2D.Double[nlines];
        paths = new Path2D.Double[npaths];
        rects = new RoundRectangle2D.Double[nrects];
        
        //Reset counter for second pass
        nellipse = 0;
        nlines = 0;
        npaths = 0;
        nrects = 0;
        
        //Second pass to polulate array
        for (int i = 0; i < number; i++){
            //Need to add logic to handle bullshit style attribute,
            //TODO - add logic to handle grouped styles
            //style="fill:none;stroke:green;stroke-width:3" />//
            if (eles.get(i).hasAttribute("stroke") && eles.get(i).hasAttribute("stroke-width")) {
                color[i] = SVGColorMap.fromStroke(eles.get(i).getAttribute("stroke"));
                stroke[i] = new BasicStroke(Integer.parseInt(eles.get(i).getAttribute("stroke-width")));
                outline[i] = true;
            } else {
                outline[i] = false;
            }
            
            if (eles.get(i).hasAttribute("fill")) {
                filled[i] = true;
                fillcolor[i] = SVGColorMap.fromStroke(eles.get(i).getAttribute("fill"));
            } else {
                filled[i] = false;
            }
            switch (Shapes.valueOf(eles.get(i).getTagName())){
                case Shapes.circle -> {
                    shape[i] = Shapes.ellipse; // replaces circles with ellipse
                    ellipse[nellipse] = new Ellipse2D.Double();
                    double r = Double.parseDouble(eles.get(i).getAttribute("r"));
                    double cx = Double.parseDouble(eles.get(i).getAttribute("cx"));
                    double cy = Double.parseDouble(eles.get(i).getAttribute("cy"));
                    ellipse[nellipse].x = cx - r;
                    ellipse[nellipse].y = cy - r;
                    ellipse[nellipse].height = r*2;
                    ellipse[nellipse].width = r*2;
                    nellipse++;
                }
                case Shapes.ellipse -> {
                    shape[i] = Shapes.ellipse;
                    ellipse[nellipse] = new Ellipse2D.Double();
                    double rx = Double.parseDouble(eles.get(i).getAttribute("rx"));
                    double ry = Double.parseDouble(eles.get(i).getAttribute("ry"));
                    double cx = Double.parseDouble(eles.get(i).getAttribute("cx"));
                    double cy = Double.parseDouble(eles.get(i).getAttribute("cy"));
                    ellipse[nellipse].x = cx - rx;
                    ellipse[nellipse].y = cy - ry;
                    ellipse[nellipse].height = ry*2;
                    ellipse[nellipse].width = rx*2;
                    nellipse++;
                }
                case Shapes.line -> {
                    shape[i] = Shapes.line;
                    lines[nlines] = new Line2D.Double();
                    lines[nlines].x1 = Double.parseDouble(eles.get(i).getAttribute("x1"));
                    lines[nlines].x2 = Double.parseDouble(eles.get(i).getAttribute("x2"));
                    lines[nlines].y1 = Double.parseDouble(eles.get(i).getAttribute("y1"));
                    lines[nlines].y2 = Double.parseDouble(eles.get(i).getAttribute("y2"));     
                    nlines++;
                }
                case Shapes.path -> {
                    String dir = eles.get(i).getAttribute("d").stripLeading();
                    char cmd = dir.charAt(0);
                    if (cmd == 'm' || cmd == 'M'){//first command must be moveTo
                        shape[i] = Shapes.path;
                        paths[npaths] = new Path2D.Double();

                        //re-structures list into more predictable format
                        ArrayList<String> cmds = new ArrayList<>();
                        String arg = new String();
                        for (int c = 0; c < dir.length(); c++) {
                            char ch = dir.charAt(c);
                            if (PathCom.isCommand(ch)) {
                                if (arg.length() > 0) {
                                    cmds.add(arg);
                                    arg = new String();
                                }
                                cmds.add(String.valueOf(ch));
                            } else if (ch == ' ' && arg.length() > 0) {
                                cmds.add(arg);
                                arg = new String();
                            } else if (ch == '-') {
                                if(arg.length() > 0){ // Note if preceded by a command then the arg string is left blank
                                    cmds.add(arg);
                                }
                                arg = String.valueOf(ch);
                            } else if (Character.isDigit(ch) || ch == '.') {
                                arg = arg + ch;
                            } 
                            if (c == dir.length() -1 && arg.length() > 0){
                                cmds.add(arg); // needs to add last argument
                            }
                        }
                        for (int c = 0; c < cmds.size(); c++){
                            System.out.println("SVGRender paths parse check cmd " + c + ": " + cmds.get(c));
                        }
                        //we know that the first command must be an m
                        //and the next 2 arguments must be x and y;
                        double startx = Double.parseDouble(cmds.get(1));
                        double starty = Double.parseDouble(cmds.get(2));
                        double lastx = startx;
                        double lasty = starty;
                        double lastctlx = 0;
                        double lastctly = 0;
                        
                        paths[npaths].moveTo(startx, starty);

                        for (int c = 3; c < cmds.size(); c++) {
                            if (PathCom.isCommand(cmds.get(c))) {
                                boolean absolute = Character.isUpperCase(cmds.get(c).charAt(0));
                                switch (cmds.get(c).toUpperCase()) {// Switch only on upper case
                                    case "M" -> { // MoveTo
                                        double x = Double.parseDouble(cmds.get(c + 1));
                                        double y = Double.parseDouble(cmds.get(c + 2));
                                        if (!absolute) {
                                            x = x + lastx;
                                            y = y + lasty;
                                        }
                                        paths[npaths].moveTo(x, y);
                                        lastx = x;
                                        lasty = y;
                                        startx = x;
                                        starty = y;
                                        c = c + 2;// one itterator is already counted,
                                    }
                                    case "L" -> { // LineTo
                                        double x = Double.parseDouble(cmds.get(c + 1));
                                        double y = Double.parseDouble(cmds.get(c + 2));
                                        if (!absolute) {
                                            x = x + lastx;
                                            y = y + lasty;
                                        }
                                        paths[npaths].lineTo(x, y);
                                        lastx = x;
                                        lasty = y;
                                        c = c + 2;// one itterator is already counted,
                                    }
                                    case "H" -> { // horizontal LineTo
                                        double x = Double.parseDouble(cmds.get(c + 1));
                                        if (!absolute) {
                                            x = x + lastx;
                                        }
                                        paths[npaths].lineTo(x, lasty);
                                        lastx = x;
                                        c++;// one itterator is already counted,
                                    }
                                    case "V" -> { // horizontal LineTo
                                        double y = Double.parseDouble(cmds.get(c + 1));
                                        if (!absolute) {
                                            y = y + lasty;
                                        }
                                        paths[npaths].lineTo(lastx, y);
                                        lasty = y;
                                        c++;// one itterator is already counted,
                                    }
                                    case "C" -> { // CurveTo TODO - Test to ensure correct
                                        int j = 1;
                                        double x1;
                                        double y1;
                                        double x2 = lastctlx;
                                        double y2 = lastctly;
                                        double x3;
                                        double y3;
                                        try {
                                            while (!PathCom.isCommand(cmds.get(c + j))) {
                                                x1 = Double.parseDouble(cmds.get(c + 0 + j));
                                                y1 = Double.parseDouble(cmds.get(c + 1 + j));
                                                x2 = Double.parseDouble(cmds.get(c + 2 + j));
                                                y2 = Double.parseDouble(cmds.get(c + 3 + j));
                                                x3 = Double.parseDouble(cmds.get(c + 4 + j));
                                                y3 = Double.parseDouble(cmds.get(c + 5 + j));
                                                if (!absolute) {
                                                    x1 = x1 + lastx;
                                                    y1 = y1 + lasty;
                                                    x2 = x2 + lastx;
                                                    y2 = y2 + lasty;
                                                    x3 = x3 + lastx;
                                                    y3 = y3 + lasty;
                                                }
                                                paths[npaths].curveTo(x1, y1, x2, y2, x3, y3);
                                                lastx = x3;
                                                lasty = y3;
                                                j = j + 6;
                                                if (c + j >= cmds.size()) {
                                                    break;
                                                }
                                            }
                                            //needed for followup smooth curves
                                            lastctlx = x2;
                                            lastctly = y2;
                                            // need to check for chains...
                                            c = c + 6;// one itterator is already counted,
                                        } catch(NumberFormatException ex){
                                            System.out.println(ex.toString());
                                        }
                                    }
                                    case "S" -> { // Smooth CurveTo TODO - Test to ensure correct                
                                        // check if last entry was an S, C, does not work for Q;
                                        double x1 = lastx;
                                        double y1 = lasty;
                                        if (cmds.get(c - 5).equalsIgnoreCase("S") || cmds.get(c - 7).equalsIgnoreCase("C")) {
                                            x1 = lastx + (lastx - lastctlx);
                                            y1 = lasty + (lasty - lastctly);
                                        }
                                        double x2 = Double.parseDouble(cmds.get(c + 1));
                                        double y2 = Double.parseDouble(cmds.get(c + 2));
                                        double x3 = Double.parseDouble(cmds.get(c + 3));
                                        double y3 = Double.parseDouble(cmds.get(c + 4));
                                        if (!absolute) { // x1 and y1 are already absolutes for S
                                            x2 = x2 + lastx;
                                            y2 = y2 + lasty;
                                            x3 = x3 + lastx;
                                            y3 = y3 + lasty;
                                        }
                                        paths[npaths].curveTo(x1, y1, x2, y2, x3, y3);
                                        lastx = x3;
                                        lasty = y3;
                                        lastctlx = x2;
                                        lastctly = y2;
                                        c = c + 4;// one itterator is already counted,
                                    }
                                    case "Q" -> { // quadratic Bézier curve - Test to ensure correct                
                                        double x1 = Double.parseDouble(cmds.get(c + 1));
                                        double y1 = Double.parseDouble(cmds.get(c + 2));
                                        double x2 = Double.parseDouble(cmds.get(c + 3));
                                        double y2 = Double.parseDouble(cmds.get(c + 4));
                                        if (!absolute) {
                                            x1 = x1 + lastx;
                                            y1 = y1 + lasty;
                                            x2 = x2 + lastx;
                                            y2 = y2 + lasty;
                                        }
                                        paths[npaths].quadTo(x1, y1, x2, y2);
                                        lastx = x2;
                                        lasty = y2;
                                        lastctlx = x1;
                                        lastctly = y1;
                                        c = c + 4;// one itterator is already counted,
                                    }
                                    case "T" -> { // smooth quadratic Bézier curveto (create a smooth quadratic Bézier curve)
                                        double x1 = lastx;
                                        double y1 = lasty;
                                        if (cmds.get(c - 5).equalsIgnoreCase("Q") || cmds.get(c - 3).equalsIgnoreCase("T")) {
                                            x1 = lastx + (lastx - lastctlx);
                                            y1 = lasty + (lasty - lastctly);
                                        }
                                        double x2 = Double.parseDouble(cmds.get(c + 1));
                                        double y2 = Double.parseDouble(cmds.get(c + 2));
                                        if (!absolute) { // x1 and y1 are already absolutes for T
                                            x2 = x2 + lastx;
                                            y2 = y2 + lasty;
                                        }
                                        paths[npaths].quadTo(x1, y1, x2, y2);
                                        lastx = x2;
                                        lasty = y2;
                                        lastctlx = x1;
                                        lastctly = y1;
                                        c = c + 2;// one itterator is already counted,
                                    }
                                    //TODO - Arc is very rough approximation,  
                                    //Warning this is some Vibe coded math....
                                    case "A" -> { // elliptical Arc (create a elliptical arc)
                                        
                                        /*
                                        A rx ry x-axis-rotation large-arc-flag sweep-flag x y
                                        a rx ry x-axis-rotation large-arc-flag sweep-flag dx dy
                                        */
                                        
                                        double rx = Double.parseDouble(cmds.get(c + 1));
                                        double ry = Double.parseDouble(cmds.get(c + 2));
                                        double xrot = Double.parseDouble(cmds.get(c + 3));
                                        int laflg = Integer.parseInt(cmds.get(c + 4));
                                        int sflg = Integer.parseInt(cmds.get(c + 5));
                                        double x3 = Double.parseDouble(cmds.get(c + 6));
                                        double y3 = Double.parseDouble(cmds.get(c + 7));
                                        if (!absolute) { // x1 and y1 are already absolutes for T
                                            x3 = x3 + lastx;
                                            y3 = y3 + lasty;
                                        }
                                        
                                        //1. handle out of range
                                        if(rx == 0 || ry == 0){// if one of the radia is 0 then its not an arc
                                            paths[npaths].lineTo(x3, y3);
                                            c = c + 7;// one itterator is already counted,
                                        }
                                        else {
                                            rx = Math.abs(rx);
                                            ry = Math.abs(ry);
                                            // This is where the vibe begins.. 
                                            double phi = Math.toRadians(xrot);
                                            double cosPhi = Math.cos(phi);
                                            double sinPhi = Math.sin(phi);

                                            double dx = (lastx - x3) / 2.0;
                                            double dy = (lasty - y3) / 2.0;
                                            double x1p = cosPhi * dx + sinPhi * dy;
                                            double y1p = -sinPhi * dx + cosPhi * dy;
                                            
                                            // Radius correction
                                            double check = (x1p * x1p) / (rx * rx) + (y1p * y1p) / (ry * ry);
                                            if (check > 1) {
                                                rx *= Math.sqrt(check);
                                                ry *= Math.sqrt(check);
                                            }
                                            
                                            // 3. Find Center
                                            double sign = (laflg == sflg) ? -1 : 1;
                                            double sq = ((rx * rx * ry * ry) - (rx * rx * y1p * y1p) - (ry * ry * x1p * x1p))
                                                    / ((rx * rx * y1p * y1p) + (ry * ry * x1p * x1p));
                                            double coef = sign * Math.sqrt(Math.max(0, sq));
                                            double cxp = coef * (rx * y1p / ry);
                                            double cyp = coef * -(ry * x1p / rx);

                                            double cx = cosPhi * cxp - sinPhi * cyp + (lastx + x3) / 2.0;
                                            double cy = sinPhi * cxp + cosPhi * cyp + (lasty + y3) / 2.0;
                                            
                                            // 4. Calculate Angles
                                            double startAngle = Math.toDegrees(Math.atan2((y1p - cyp) / ry, (x1p - cxp) / rx));
                                            double endAngle = Math.toDegrees(Math.atan2((-y1p - cyp) / ry, (-x1p - cxp) / rx));
                                            double sweep = endAngle - startAngle;
                                            if (sflg == 1 && sweep < 0) {
                                                sweep += 360;
                                            } else if (sflg != 1 && sweep > 0) {
                                                sweep -= 360;
                                            }
                                            // 5. Create the Java Arc
                                            // Note: Arc2D uses a bounding box, so x = cx - rx, y = cy - ry
                                            Arc2D arc = new Arc2D.Double(cx - rx, cy - ry, rx * 2, ry * 2,
                                                    -startAngle, -sweep, Arc2D.OPEN);
                                            paths[npaths].append(arc, true);
                                            c = c + 7;// one itterator is already counted,
                                        }
                                    }

                                    case "Z" -> { // closepath, note path can continue after this,
                                        paths[npaths].lineTo(startx, starty);
                                        lastx = startx;
                                        lasty = starty;
                                    }
                                }
                            }
                        }
                        npaths++;
                    }
                }
                case Shapes.polyline -> { 
                    shape[i] = Shapes.path;
                    paths[npaths] = new Path2D.Double();
                    String[] points = eles.get(i).getAttribute("points").split(" ");
                    String[] xy0 = points[0].split(",");
                    double x0 = Double.parseDouble(xy0[0]);
                    double y0 = Double.parseDouble(xy0[1]);
                    paths[npaths].moveTo(x0, y0);
                    
                    for (int p = 1; p < points.length; p++){
                        String[] xy = points[p].split(",");
                        double x = Double.parseDouble(xy[0]);
                        double y = Double.parseDouble(xy[1]);
                        paths[npaths].lineTo(x, y);
                    }
                    npaths++;
                }
                case Shapes.polygon -> {
                    shape[i] = Shapes.path;
                    paths[npaths] = new Path2D.Double();
                    String[] points = eles.get(i).getAttribute("points").split(" ");
                    String[] xy0 = points[0].split(",");
                    double x0 = Double.parseDouble(xy0[0]);
                    double y0 = Double.parseDouble(xy0[1]);
                    paths[npaths].moveTo(x0, y0);
                    for (int p = 1; p < points.length; p++){
                        String[] xy = points[p].split(",");
                        double x = Double.parseDouble(xy[0]);
                        double y = Double.parseDouble(xy[1]);
                        paths[npaths].lineTo(x, y);
                    }
                    paths[npaths].lineTo(x0, y0); //only diff between Polygon and Polyline is the last close step
                    npaths++;
                }
                case Shapes.rect -> {
                    shape[i] = Shapes.rect;
                    rects[nrects] = new RoundRectangle2D.Double();
                    rects[nrects].height = Double.parseDouble(eles.get(i).getAttribute("height"));
                    rects[nrects].width = Double.parseDouble(eles.get(i).getAttribute("width"));
                    rects[nrects].x = Double.parseDouble(eles.get(i).getAttribute("x"));
                    rects[nrects].y = Double.parseDouble(eles.get(i).getAttribute("y"));
                    if (eles.get(i).hasAttribute("rx")) {
                        rects[nrects].arcwidth = Double.parseDouble(eles.get(i).getAttribute("rx"));
                    } else {
                        rects[nrects].arcwidth = 0;
                    }
                    if (eles.get(i).hasAttribute("ry")) {
                        rects[nrects].archeight = Double.parseDouble(eles.get(i).getAttribute("ry"));
                    } else {
                        rects[nrects].archeight = 0;
                    }
                    nrects++;
                }
            }
        }
    }
    /**
     * Draws the stored ellipses, lines, paths, and rects in order to the 
     * graphics layer
     * @param g 
     */
    public void Draw(Graphics2D g){
        int nellipse = 0;
        int nlines = 0;
        int npaths = 0;
        int nrects = 0;

        for (int i = 0; i < this.number; i++){
            
            if (outline[i]) {
                g.setStroke(stroke[i]);
            }
            switch (shape[i]){
                case Shapes.ellipse ->{
                    if (filled[i]){
                        g.setColor(fillcolor[i]);
                        g.fill(ellipse[nellipse]);
                    }
                    if (outline[i]) {
                        g.setColor(color[i]);
                        g.draw(ellipse[nellipse]);
                    }
                    nellipse++;
                }
                case Shapes.line -> {
                    g.setColor(color[i]);
                    g.draw(lines[nlines]);
                    nlines++;
                }
                case Shapes.path -> {
                    if (filled[i]){
                        g.setColor(fillcolor[i]);
                        g.fill(paths[npaths]);
                    }
                    if (outline[i]) {
                        g.setColor(color[i]);
                        g.draw(paths[npaths]);
                    }
                    npaths++;
                }
                case Shapes.rect ->{
                    if (filled[i]){
                        g.setColor(fillcolor[i]);
                        g.fill(rects[nrects]);
                    }
                    if (outline[i]) {
                        g.setColor(color[i]);
                        g.draw(rects[nrects]);
                    }
                    nrects++;
                }
            }
        }
    }
}
