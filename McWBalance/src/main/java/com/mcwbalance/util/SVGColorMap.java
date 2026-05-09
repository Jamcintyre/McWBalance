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
package com.mcwbalance.util;
import java.awt.Color;

/**
 * Translates Standard SVG codes to a color using defined RGB values
 * @author alex
 */
public enum SVGColorMap {

    /**
     * Standard SVG RGB 240, 248, 255
     */
    aliceblue(240, 248, 255),

    /**
     * RGB (250, 235, 215),
     */
    antiquewhite(250, 235, 215),

    /**
     * RGB (0, 255, 255),
     */
    aqua(0, 255, 255),

    /**
     * Standard SVG RGB
     */
    aquamarine(127, 255, 212),

    /**
     * Standard SVG RGB
     */
    azure(240, 255, 255),

    /**
     * Standard SVG RGB
     */
    beige(245, 245, 220),

    /**
     * Standard SVG RGB
     */
    bisque(255, 228, 196),

    /**
     * Standard SVG RGB
     */
    black(0, 0, 0),

    /**
     * Standard SVG RGB
     */
    blanchedalmond(255, 235, 205),

    /**
     * Standard SVG RGB
     */
    blue(0, 0, 255),

    /**
     * Standard SVG RGB
     */
    blueviolet(138, 43, 226),

    /**
     * Standard SVG RGB
     */
    brown(165, 42, 42),

    /**
     * Standard SVG RGB
     */
    burlywood(222, 184, 135),

    /**
     * Standard SVG RGB
     */
    cadetblue(95, 158, 160),

    /**
     * Standard SVG RGB
     */
    chartreuse(127, 255, 0),

    /**
     * Standard SVG RGB
     */
    chocolate(210, 105, 30),

    /**
     * Standard SVG RGB
     */
    coral(255, 127, 80),

    /**
     * Standard SVG RGB
     */
    cornflowerblue(100, 149, 237),

    /**
     * Standard SVG RGB
     */
    cornsilk(255, 248, 220),

    /**
     * Standard SVG RGB
     */
    crimson(220, 20, 60),

    /**
     * Standard SVG RGB
     */
    cyan(0, 255, 255),

    /**
     * Standard SVG RGB
     */
    darkblue(0, 0, 139),

    /** 
     * Standard SVG RGB
     */
    darkcyan(0, 139, 139),

    /**
     * Standard SVG RGB
     */
    darkgoldenrod(184, 134, 11),

    /**
     * Standard SVG RGB
     */
    darkgray(169, 169, 169),

    /**
     * Standard SVG RGB
     */
    darkgreen(0, 100, 0),

    /**
     * Standard SVG RGB
     */
    darkgrey(169, 169, 169),

    /**
     * Standard SVG RGB
     */
    darkkhaki(189, 183, 107),

    /**
     * Standard SVG RGB
     */
    darkmagenta(139, 0, 139),

    /**
     * Standard SVG RGB
     */
    darkolivegreen(85, 107, 47),

    /**
     * Standard SVG RGB
     */
    darkorange(255, 140, 0),

    /**
     * Standard SVG RGB
     */
    darkorchid(153, 50, 204),

    /**
     * Standard SVG RGB
     */
    darkred(139, 0, 0),

    /**
     * Standard SVG RGB
     */
    darksalmon(233, 150, 122),

    /**
     * Standard SVG RGB
     */
    darkseagreen(143, 188, 143),

    /**
     * Standard SVG RGB
     */
    darkslateblue(72, 61, 139),

    /**
     * Standard SVG RGB
     */
    darkslategray(47, 79, 79),

    /**
     * Standard SVG RGB
     */
    darkslategrey(47, 79, 79),

    /**
     * Standard SVG RGB
     */
    darkturquoise(0, 206, 209),

    /**
     * Standard SVG RGB
     */
    darkviolet(148, 0, 211),

    /**
     * Standard SVG RGB
     */
    deeppink(255, 20, 147),

    /**
     * Standard SVG RGB
     */
    deepskyblue(0, 191, 255),

    /**
     * Standard SVG RGB
     */
    dimgray(105, 105, 105),

    /**
     * Standard SVG RGB
     */
    dimgrey(105, 105, 105),

    /**
     * Standard SVG RGB
     */
    dodgerblue(30, 144, 255),

    /**
     * Standard SVG RGB
     */
    firebrick(178, 34, 34),

    /**
     * Standard SVG RGB
     */
    floralwhite(255, 250, 240),

    /**
     * Standard SVG RGB
     */
    forestgreen(34, 139, 34),

    /**
     * Standard SVG RGB
     */
    fuchsia(255, 0, 255),

    /**
     * Standard SVG RGB
     */
    gainsboro(220, 220, 220),

    /**
     * Standard SVG RGB
     */
    ghostwhite(248, 248, 255),

    /**
     * Standard SVG RGB
     */
    gold(255, 215, 0),

    /** 
     * Standard SVG RGB
     */
    goldenrod(218, 165, 32),

    /**
     * Standard SVG RGB
     */
    gray(128, 128, 128),

    /**
     * Standard SVG RGB
     */
    green(0, 128, 0),

    /**
     * Standard SVG RGB
     */
    grey(128, 128, 128),

    /**
     * Standard SVG RGB
     */
    greenyellow(173, 255, 47),

    /**
     * Standard SVG RGB
     */
    honeydew(240, 255, 240),

    /**
     * Standard SVG RGB
     */
    hotpink(255, 105, 180),

    /**
     * Standard SVG RGB
     */
    indianred(205, 92, 92),

    /**
     * Standard SVG RGB
     */
    indigo(75, 0, 130),

    /**
     * Standard SVG RGB
     */
    ivory(255, 255, 240),

    /**
     * Standard SVG RGB
     */
    khaki(240, 230, 140),

    /**
     * Standard SVG RGB
     */
    lavender(230, 230, 250),

    /**
     * Standard SVG RGB
     */
    lavenderblush(255, 240, 245),

    /**
     * Standard SVG RGB
     */
    lawngreen(124, 252, 0),

    /**
     * Standard SVG RGB
     */
    lemonchiffon(255, 250, 205),

    /**
     * Standard SVG RGB
     */
    lightblue(173, 216, 230),

    /**
     * Standard SVG RGB
     */
    lightcoral(240, 128, 128),

    /**
     * Standard SVG RGB
     */
    lightcyan(224, 255, 255),

    /**
     * Standard SVG RGB
     */
    lightgoldenrodyellow(250, 250, 210),

    /**
     * Standard SVG RGB
     */
    lightgray(211, 211, 211),

    /**
     * Standard SVG RGB
     */
    lightgreen(144, 238, 144),

    /**
     * Standard SVG RGB
     */
    lightgrey(211, 211, 211),

    /**
     * Standard SVG RGB
     */
    lightpink(255, 182, 193),

    /**
     * Standard SVG RGB
     */
    lightsalmon(255, 160, 122),

    /**
     * Standard SVG RGB
     */
    lightseagreen(32, 178, 170),

    /**
     * Standard SVG RGB
     */
    lightskyblue(135, 206, 250),

    /**
     * Standard SVG RGB
     */
    lightslategray(119, 136, 153),

    /**
     * Standard SVG RGB
     */
    lightslategrey(119, 136, 153),

    /**
     * Standard SVG RGB
     */
    lightsteelblue(176, 196, 222),

    /**
     * Standard SVG RGB
     */
    lightyellow(255, 255, 224),

    /**
     * Standard SVG RGB
     */
    lime(0, 255, 0),

    /**
     * Standard SVG RGB
     */
    limegreen(50, 205, 50),

    /**
     * Standard SVG RGB
     */
    linen(250, 240, 230),

    /**
     * Standard SVG RGB
     */
    magenta(255, 0, 255),

    /**
     * Standard SVG RGB
     */
    maroon(128, 0, 0),

    /** 
     * Standard SVG RGB
     */
    mediumaquamarine(102, 205, 170),

    /** 
     * Standard SVG RGB
     */
    mediumblue(0, 0, 205),

    /**
     * Standard SVG RGB
     */
    mediumorchid(186, 85, 211),

    /**
     * Standard SVG RGB
     */
    mediumpurple(147, 112, 219),

    /**
     * Standard SVG RGB
     */
    mediumseagreen(60, 179, 113),

    /**
     * Standard SVG RGB
     */
    mediumslateblue(123, 104, 238),

    /**
     * Standard SVG RGB
     */
    mediumspringgreen(0, 250, 154),

    /**
     * Standard SVG RGB
     */
    mediumturquoise(72, 209, 204),

    /**
     * Standard SVG RGB
     */
    mediumvioletred(199, 21, 133),

    /**
     * Standard SVG RGB
     */
    midnightblue(25, 25, 112),

    /**
     * Standard SVG RGB
     */
    mintcream(245, 255, 250),

    /**
     * Standard SVG RGB
     */
    mistyrose(255, 228, 225),

    /**
     * Standard SVG RGB
     */
    moccasin(255, 228, 181),

    /**
     * Standard SVG RGB
     */
    navajowhite(255, 222, 173),

    /**
     * Standard SVG RGB
     */
    navy(0, 0, 128),

    /**
     * Standard SVG RGB
     */
    oldlace(253, 245, 230),

    /**
     * Standard SVG RGB
     */
    olive(128, 128, 0),

    /**
     * Standard SVG RGB
     */
    olivedrab(107, 142, 35),

    /**
     * Standard SVG RGB
     */
    orange(255, 165, 0),

    /**
     * Standard SVG RGB
     */
    orangered(255, 69, 0),

    /**
     * Standard SVG RGB
     */
    orchid(218, 112, 214),

    /**
     * Standard SVG RGB
     */
    palegoldenrod(238, 232, 170),

    /**
     * Standard SVG RGB
     */
    palegreen(152, 251, 152),

    /**
     * Standard SVG RGB
     */
    paleturquoise(175, 238, 238),

    /**
     * Standard SVG RGB
     */
    palevioletred(219, 112, 147),

    /**
     * Standard SVG RGB
     */
    papayawhip(255, 239, 213),

    /**
     * Standard SVG RGB
     */
    peachpuff(255, 218, 185),

    /**
     * Standard SVG RGB
     */
    peru(205, 133, 63),

    /**
     * Standard SVG RGB
     */
    pink(255, 192, 203),

    /**
     * Standard SVG RGB
     */
    plum(221, 160, 221),

    /**
     * Standard SVG RGB
     */
    powderblue(176, 224, 230),

    /**
     * Standard SVG RGB
     */
    purple(128, 0, 128),

    /**
     * Standard SVG RGB
     */
    red(255, 0, 0),

    /**
     * Standard SVG RGB
     */
    rosybrown(188, 143, 143),

    /** 
     * Standard SVG RGB
     */
    royalblue(65, 105, 225),

    /**
     * Standard SVG RGB
     */
    saddlebrown(139, 69, 19),

    /**
     * Standard SVG RGB
     */
    salmon(250, 128, 114),

    /**
     * Standard SVG RGB
     */
    sandybrown(244, 164, 96),

    /**
     * Standard SVG RGB (46, 139, 87),
     */
    seagreen(46, 139, 87),

    /**
     * Standard SVG RGB (255, 245, 238),
     */
    seashell(255, 245, 238),

    /**
     * Standard SVG RGB (160, 82, 45),
     */
    sienna(160, 82, 45),

    /**
     * Standard SVG RGB (192, 192, 192),
     */
    silver(192, 192, 192),

    /**
     * Standard SVG RGB (135, 206, 235),
     */
    skyblue(135, 206, 235),

    /**
     * Standard SVG RGB (106, 90, 205),
     */
    slateblue(106, 90, 205),

    /** 
     * Standard SVG RGB (112, 128, 144),
     */
    slategray(112, 128, 144),

    /**
     * Standard SVG RGB (112, 128, 144),
     */
    slategrey(112, 128, 144),

    /**
     * Standard SVG RGB (255, 250, 250),
     */
    snow(255, 250, 250),

    /**
     * Standard SVG RGB (0, 255, 127),
     */
    springgreen(0, 255, 127),

    /**
     * Standard SVG RGB (70, 130, 180),
     */
    steelblue(70, 130, 180),

    /**
     * Standard SVG RGB (210, 180, 140),
     */
    tan(210, 180, 140),

    /** 
     * Standard SVG RGB (0, 128, 128),
     */
    teal(0, 128, 128),

    /**
     * Standard SVG RGB (216, 191, 216),
     */
    thistle(216, 191, 216),

    /**
     * Standard SVG RGB (255, 99, 71),
     */
    tomato(255, 99, 71),

    /** 
     * Standard SVG RGB (64, 224, 208),
     */
    turquoise(64, 224, 208),

    /**
     * Standard SVG RGB (238, 130, 238),
     */ 
    violet(238, 130, 238),

    /** 
     * Standard SVG RGB (245, 222, 179),
     */
    wheat(245, 222, 179),

    /**
     * Standard SVG RGB (255, 255, 255),
     */
    white(255, 255, 255),

    /**
     * Standard SVG RGB (245, 245, 245),
     */
    whitesmoke(245, 245, 245),

    /**
     * Standard SVG RGB (255, 255, 0),
     */
    yellow(255, 255, 0),

    /**
     * Standard SVG RGB (154, 205, 50);
     */
    yellowgreen(154, 205, 50);
    
    /**
     * red value between 0 and 255
     */
    short r;
    
    /**
     * green value between 0 and 255
     */
    short g;
    
    /**
     * Blue value between 0 and 255
     */
    short b;
    
    /**
     * Values stored as individual ints for simplicity can circle back and combine
     * into bit shifted int later if appropriate. 
     * @param r
     * @param g
     * @param b 
     */
    SVGColorMap(int r, int g, int b){
        this.r = (short) r;
        this.g = (short) g;
        this.b = (short) b;
    }
    
    /**
     * used for pulling color value from the enum as values are stored as primitives
     * @return 
     */
    public Color getColor() {
        return new Color(r, g, b);
    }
    
    
    /**
     * Translates svg color string to a java.awt.Color
     * recognizes named colors, rgb(r,g,b) in int, rgba(r,g,b,a) in int, and #rrggbb in hex
     * No exception throwing is intentional as colors are not mission critical
     * and preference is for the program to plot even with a misspelled color name
     * Color defaults to black
     * @param stroke svg stroke value 
     * @return color matching stroke value or black if an error is encountered
     */
    public static Color fromStroke(String stroke) {

        if (stroke.startsWith("#")) {
            try {
                return Color.decode(stroke);
            } catch (IllegalArgumentException ex) {
                return Color.black;
            }
        } else if (stroke.startsWith("rgb")) {
            String split[] = stroke.split(",");
            if (split.length < 3 || split.length > 4) {
                return Color.black;
            } else {
                try {
                    if (split.length == 3) {
                        int rd = Integer.parseInt(split[0].substring(4));
                        int gr = Integer.parseInt(split[1]);
                        int bl = Integer.parseInt(split[2].substring(0, split[2].length() - 1));
                        return new Color(rd, gr, bl);
                    } else {
                        int rd = Integer.parseInt(split[0].substring(5));
                        int gr = Integer.parseInt(split[1]);
                        int bl = Integer.parseInt(split[2]);
                        int a = Integer.parseInt(split[3].substring(0, split[3].length() - 1));
                        return new Color(rd, gr, bl, a);
                    }

                } catch (NumberFormatException ex) {
                    return Color.black;
                }
            }
        } else {
            try {
                return valueOf(stroke).getColor();
            } catch (IllegalArgumentException ex) {
                return Color.black;
            }
        }

    }

}
