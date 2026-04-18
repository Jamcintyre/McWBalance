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
    aliceblue(240, 248, 255),
    antiquewhite(250, 235, 215),
    aqua(0, 255, 255),
    aquamarine(127, 255, 212),
    azure(240, 255, 255),
    beige(245, 245, 220),
    bisque(255, 228, 196),
    black(0, 0, 0),
    blanchedalmond(255, 235, 205),
    blue(0, 0, 255),
    blueviolet(138, 43, 226),
    brown(165, 42, 42),
    burlywood(222, 184, 135),
    cadetblue(95, 158, 160),
    chartreuse(127, 255, 0),
    chocolate(210, 105, 30),
    coral(255, 127, 80),
    cornflowerblue(100, 149, 237),
    cornsilk(255, 248, 220),
    crimson(220, 20, 60),
    cyan(0, 255, 255),
    darkblue(0, 0, 139),
    darkcyan(0, 139, 139),
    darkgoldenrod(184, 134, 11),
    darkgray(169, 169, 169),
    darkgreen(0, 100, 0),
    darkgrey(169, 169, 169),
    darkkhaki(189, 183, 107),
    darkmagenta(139, 0, 139),
    darkolivegreen(85, 107, 47),
    darkorange(255, 140, 0),
    darkorchid(153, 50, 204),
    darkred(139, 0, 0),
    darksalmon(233, 150, 122),
    darkseagreen(143, 188, 143),
    darkslateblue(72, 61, 139),
    darkslategray(47, 79, 79),
    darkslategrey(47, 79, 79),
    darkturquoise(0, 206, 209),
    darkviolet(148, 0, 211),
    deeppink(255, 20, 147),
    deepskyblue(0, 191, 255),
    dimgray(105, 105, 105),
    dimgrey(105, 105, 105),
    dodgerblue(30, 144, 255),
    firebrick(178, 34, 34),
    floralwhite(255, 250, 240),
    forestgreen(34, 139, 34),
    fuchsia(255, 0, 255),
    gainsboro(220, 220, 220),
    ghostwhite(248, 248, 255),
    gold(255, 215, 0),
    goldenrod(218, 165, 32),
    gray(128, 128, 128),
    green(0, 128, 0),
    grey(128, 128, 128),
    greenyellow(173, 255, 47),
    honeydew(240, 255, 240),
    hotpink(255, 105, 180),
    indianred(205, 92, 92),
    indigo(75, 0, 130),
    ivory(255, 255, 240),
    khaki(240, 230, 140),
    lavender(230, 230, 250),
    lavenderblush(255, 240, 245),
    lawngreen(124, 252, 0),
    lemonchiffon(255, 250, 205),
    lightblue(173, 216, 230),
    lightcoral(240, 128, 128),
    lightcyan(224, 255, 255),
    lightgoldenrodyellow(250, 250, 210),
    lightgray(211, 211, 211),
    lightgreen(144, 238, 144),
    lightgrey(211, 211, 211),
    lightpink(255, 182, 193),
    lightsalmon(255, 160, 122),
    lightseagreen(32, 178, 170),
    lightskyblue(135, 206, 250),
    lightslategray(119, 136, 153),
    lightslategrey(119, 136, 153),
    lightsteelblue(176, 196, 222),
    lightyellow(255, 255, 224),
    lime(0, 255, 0),
    limegreen(50, 205, 50),
    linen(250, 240, 230),
    magenta(255, 0, 255),
    maroon(128, 0, 0),
    mediumaquamarine(102, 205, 170),
    mediumblue(0, 0, 205),
    mediumorchid(186, 85, 211),
    mediumpurple(147, 112, 219),
    mediumseagreen(60, 179, 113),
    mediumslateblue(123, 104, 238),
    mediumspringgreen(0, 250, 154),
    mediumturquoise(72, 209, 204),
    mediumvioletred(199, 21, 133),
    midnightblue(25, 25, 112),
    mintcream(245, 255, 250),
    mistyrose(255, 228, 225),
    moccasin(255, 228, 181),
    navajowhite(255, 222, 173),
    navy(0, 0, 128),
    oldlace(253, 245, 230),
    olive(128, 128, 0),
    olivedrab(107, 142, 35),
    orange(255, 165, 0),
    orangered(255, 69, 0),
    orchid(218, 112, 214),
    palegoldenrod(238, 232, 170),
    palegreen(152, 251, 152),
    paleturquoise(175, 238, 238),
    palevioletred(219, 112, 147),
    papayawhip(255, 239, 213),
    peachpuff(255, 218, 185),
    peru(205, 133, 63),
    pink(255, 192, 203),
    plum(221, 160, 221),
    powderblue(176, 224, 230),
    purple(128, 0, 128),
    red(255, 0, 0),
    rosybrown(188, 143, 143),
    royalblue(65, 105, 225),
    saddlebrown(139, 69, 19),
    salmon(250, 128, 114),
    sandybrown(244, 164, 96),
    seagreen(46, 139, 87),
    seashell(255, 245, 238),
    sienna(160, 82, 45),
    silver(192, 192, 192),
    skyblue(135, 206, 235),
    slateblue(106, 90, 205),
    slategray(112, 128, 144),
    slategrey(112, 128, 144),
    snow(255, 250, 250),
    springgreen(0, 255, 127),
    steelblue(70, 130, 180),
    tan(210, 180, 140),
    teal(0, 128, 128),
    thistle(216, 191, 216),
    tomato(255, 99, 71),
    turquoise(64, 224, 208),
    violet(238, 130, 238),
    wheat(245, 222, 179),
    white(255, 255, 255),
    whitesmoke(245, 245, 245),
    yellow(255, 255, 0),
    yellowgreen(154, 205, 50);

    short r;
    short g;
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
