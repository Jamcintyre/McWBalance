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
package com.mcwbalance.flowchart;

import java.awt.Color;
import java.awt.Stroke;

/**
 * Used as a bin to store a cad line start x, y, end x, y, color and stroke
 * @author alex
 */
public class CadLine {
    int x0;
    int y0;
    int x1;
    int y1;
    Stroke sk;
    Color cl;
    
    /**
     * 
     * @param x0 Starting X coordinate
     * @param y0 Starting Y coordinate
     * @param x1 Ending X coordinate
     * @param y1 Ending Y coordinate
     * @param cl Line Color
     * @param sk Line Stroke
     */
    public CadLine(int x0,int y0,int x1,int y1, Color cl, Stroke sk){
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.cl = cl;
        this.sk = sk;
    }
    
    /**
     * 
     * @return Starting X coordinate
     */
    public int getx0(){
        return x0;
    }
    /**
     * 
     * @return Ending X coordinate
     */
    public int getx1(){
        return x1;
    }
    
    /**
     * 
     * @return Starting Y coordinate
     */
    public int gety0(){
        return y0;
    }
    
    /**
     * 
     * @return Ending Y coordinate
     */
    public int gety1(){
        return y1;
    }
    
    /**
     * 
     * @return Line Stroke
     */
    public Stroke getStroke(){
        return sk;
    }
    
    /**
     * 
     * @return line color 
     */
    public Color getColour(){
        return cl;
    }
    
}
