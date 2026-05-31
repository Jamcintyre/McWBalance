/*
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
package com.mcwbalance.result;

import com.mcwbalance.McWBalance;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JComponent;

/**
 * Used for plotting time Result class data similar to scatter plot
 * 
 * @author alex
 */
public class ResultPlot extends JComponent{
    
    private final Color colorBackground;
    private final Color colorBorder;
    private final Color colorGridLine;
    
    private final int marginTop;
    private final int marginBottom;
    private final int marginRight;
    private final int marginLeft;
    
    
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    
    /**
     * Area where lines plot on, after margins are applied
     */
    private Rectangle plotArea;
    
    private class IntPolyLine{
        private ArrayList<Integer> x;
        private ArrayList<Integer> y;
        IntPolyLine(){
            x = new ArrayList();
            y = new ArrayList();
        }
        
        void addPoint(int x, int y){
            this.x.add(x);
            this.y.add(y);
        }
        int[] getX(){
            int[] arr = new int[size()];
            for (int i = 0; i < size(); ++i){
                arr[i] = x.get(i);
            }
            return arr;
        };
        int[] getY(){
            int[] arr = new int[size()];
            for (int i = 0; i < size(); ++i) {
                arr[i] = y.get(i);
            }
            return arr;
        };
        int size(){
            return x.size();
        }
    }
    
    private ResultCollection results;
    
    private ArrayList<IntPolyLine> scaledresults;
    
    private float scaleX;
    private float scaleY;
    
    private final BasicStroke strokeThin;
    private final BasicStroke strokeResult;
    private final BasicStroke strokeThick;
   
    private String titleX;
    private String titleY;
    
    
    /**
     * 
     * @param results collection of results to plot
     */
    public ResultPlot(ResultCollection results){
        
        String[] rgbBorder = McWBalance.style.getProperty("PREF_COLOR_BORDER", "255,255,255").split(",");
        colorBorder = new Color(Integer.parseInt(rgbBorder[0]), Integer.parseInt(rgbBorder[1]), Integer.parseInt(rgbBorder[2]));
        String[] rgbGridline = McWBalance.style.getProperty("PREF_COLOR_GRIDLINE", "255,255,255").split(",");
        colorGridLine = new Color(Integer.parseInt(rgbGridline[0]), Integer.parseInt(rgbGridline[1]), Integer.parseInt(rgbGridline[2]));

        colorBackground = Color.CYAN; // DEBUG to find the window
        
        
        
        this.results = results; 
        scaledresults = new ArrayList();
        
        marginTop = Integer.parseInt(McWBalance.style.getProperty("EMBEDDED_MARGIN_TOP", "50"));
        marginBottom = Integer.parseInt(McWBalance.style.getProperty("EMBEDDED_MARGIN_BOTTOM", "50"));
        marginLeft = Integer.parseInt(McWBalance.style.getProperty("EMBEDDED_MARGIN_LEFT", "50"));
        marginRight = Integer.parseInt(McWBalance.style.getProperty("EMBEDDED_MARGIN_RIGHT", "50"));

        plotArea = new Rectangle();
        
        minX = 0;
        minY = 0;
        
        maxX = 50;// DEBUG
        maxY = 50;//DEBUG
        
        strokeThick = new BasicStroke(Integer.parseInt(McWBalance.style.getProperty("LINE_THICK", "3")));
        strokeResult = new BasicStroke(Integer.parseInt(McWBalance.style.getProperty("PLOT_LINE_SERIES", "2")));
        strokeThin = new BasicStroke(Integer.parseInt(McWBalance.style.getProperty("LINE_THIN", "1")));
        


        System.out.println("ResultPlot.Java - Constructor getWidth " + this.getWidth() + "getHeight " + this.getHeight());
        
        this.setBackground(Color.MAGENTA); // DEBUG to confirm component is drawing
        update();

    }
    
    /**
     * This is where the plot is rendered
     * @param g instance to render plot to
     */
    @Override
    public void paintComponent(Graphics g){
        revalidate();
        update();
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //AffineTransform at = new AffineTransform();
        //at.setToScale(1, 1);
        //g2.setTransform(at); // in event windows scale is not 100%, then at transform is needed first
        g2.setColor(colorBackground); 
        g2.fillRect(0+5, 0+5, this.getWidth()-10, this.getHeight()-10);
        
        // draws border
        g2.setColor(colorBorder);
        //g2.setStroke(thickLine);
        g2.drawRect(plotArea.x, plotArea.y, plotArea.width, plotArea.height);
        
        
        
        
    }
    
    
    /**
     * Used to update all data, indended for use after changing units or scale
     */
    public final void update(){
        updateTitleX();
        updateTitleY();
        updatePlotArea();
        updateScales();
    }
    
    /**
     * X axis title is always Time (Units) this method is intended to be 
     * called on construction and when units are switched
     */
    private final void updateTitleX(){
        titleX = McWBalance.langRB.getString("TIME") + " " + results.getTimeUnit().getBracketedDesciptor();
    }
    
    /**
     * Y axis title is always Units (Units) this method is intended to be 
     * called on construction and when units are switched
     */
    private final void updateTitleY(){
        titleY = results.getResultDescriptor();
    }
    /**
     * for updating position of axis, titles, and plot area
     * note plot area limited to min of 5 pixels 
     */
    private final void updatePlotArea(){
        
        plotArea.x = getX() + marginLeft;
        plotArea.y = getY() + marginTop;
        
        int tryheight = this.getHeight() - marginTop - marginBottom;
        if(tryheight < 5){
            tryheight = 5;
        }
        plotArea.height = tryheight;
        
        int trywidth = this.getWidth() - marginRight - marginLeft;
        if(trywidth < 5){
            trywidth = 5;
        }
        plotArea.width = trywidth;
        
        //SET LOCATION OF AXIS TITLES
        
        
                
                
    }
    
    public final void updateScales(){
        int rangeX = maxX - minX;
        int rangeY = maxY - minY;
        
        scaleX = rangeX / plotArea.width;
        scaleY = rangeY / plotArea.height;
        if(results.getRowCount() == 0){
            return;
        }
        for (int r = 0; r < results.getRowCount(); ++r){
            int time = (int)results.getValueAt(r, 0);
            
            if(time > maxX){
                break;
            }else if (time >= minX ){
                int x = (int)((float)(time - minX)*scaleX);
                
                for(int c = 1; c < results.getColumnCount(); ++c){
                    int y = (int)((Float.parseFloat(results.getValueAt(r, c).toString()) - minY)*scaleY);
                    
                }
            }
        }
        
    }
    
    
    
}
