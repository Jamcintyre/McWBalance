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
package com.mcwbalance.measure;

/**
 * For operations involving water depth equivalents, and conversions from
 * say mm to inches or feet
 * 
 * @author alex
 */
public class Depth {
    
    /**
     * Units of water equivalent depths allowed to be used in the model
     */
    public static enum DepthUnit implements Unit{
        /**
         * Millimeter mm, 10/10 mm
         */
        mm("mm",10),
        
        /**
         * centimeter cm, 100/10 mm
         */
        cm("cm",100),
        
        /**
         * Meter m, 10000/10 mm
         */
        m("m",10000),
        
        /**
         * 12 SAE inches 
         */
        feet("feet",3048),
        
        /**
         * Modern SAE inch 25.4 mm / inch => 254 /10 mm 
         */
        inches("inches",254);
        
        private final String desc;
        
        /**
         * depth measure in 1/10 of a mm, to allow whole int 
         */
        private final int decimalmm;
        
        private DepthUnit(String desc, int decimalmm){
            this.desc = desc;
            this.decimalmm = decimalmm;
        }
        
        /**
         * For getting a conversion factor from one unit to another
         * Double precision in water balance is fine since runoff coeffs are at best
         * 2 sig figures, rainfall is 3 to 4 sig figures. compounding rounding errors
         * should not make or break the model
         * 
         * Method is overly simple, and is only here to minimize risk of flipped 
         * operators and accidental conversion the wrong way. 
         * @param from base input units
         * @param to units to convert to
         * @return value to multiply the from value to get the to value
         */
        public static double getConversion(DepthUnit from, DepthUnit to){
            return to.decimalmm/from.decimalmm;
        }
        
        /**
         * Used for getting the bracketed unit description, i.e. (mm) or
         * (feet).
         *
         * @return bracketed descriptor
         */
        @Override
        public String getBracketedDesciptor() {
            return "("+desc+")";
        }
        
        /**
         * Used for getting the non bracketed unit description, i.e.
         * mm or inches.
         * @return descriptor without brackets
         */
        public String getDesciptor(){
            return desc;
        }
        
        /**
         * same as valueOf with relaxed inputs ignores the case, note values are not plural
         * and accepts common aliases,  i.e. inch, in, inches,  ft, feet, etc...
         * also compares to getDescriptor() 
         * 
         * @param depthunit Hour, HOUR, hour or similar not plural string
         * @return
         */
        public static DepthUnit valueOfIngoreCase(String depthunit) {
            for (DepthUnit du : DepthUnit.values()) {
                if (du.name().equalsIgnoreCase(depthunit) ||
                        du.getDesciptor().equalsIgnoreCase(depthunit)) {
                    return du;
                }
            }
            
            //Special cases
            return switch (depthunit.toLowerCase()) {
                case "ft" -> DepthUnit.feet;
                case "in" -> DepthUnit.inches;
                case "inch" -> DepthUnit.inches;
                case "meter" -> DepthUnit.m;
                case "meters" -> DepthUnit.m;
                case "metre" -> DepthUnit.m;
                case "metres" -> DepthUnit.m;
                default -> null;
            };   
        }
    }
}
