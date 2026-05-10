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
 * For operations involving areas, and conversions from say m2 to ha or acres
 *
 * @author alex
 */
public class Area {

    /**
     * Units of area allowed to be used in the model double base units selected
     * to avoid going into .01 of a mm2. 1 ha to .1 mm2 => 100,000,000,000
     * exceeds max int.
     *
     * INT_MAX (Signed): 2,147,483,647
     */
    public static enum AreaUnit implements Unit{
        /**
         * Square Meter
         */
        m2("(sq.m)", 1),
        /**
         * Square kilometer
         */
        km2("(sq.km)", 1000000),
        /**
         * hector, 10,000 m2
         */
        ha("(ha)", 10000),
        /**
         * acre 4046.8564224 m2
         */
        acre("(acre)", 4046.8564224),
        /**
         * square yard = 0.83612736 m2
         */
        yd2("(sq.yd)", 0.83612736);

        private final String desc;

        private final double baseunit;

        private AreaUnit(String desc, double baseunit) {
            this.desc = desc;
            this.baseunit = baseunit;
        }

        /**
         * For getting a conversion factor from one unit to another Double
         * precision in water balance is fine since runoff coeffs are at best 2
         * sig figures, rainfall is 3 to 4 sig figures. compounding rounding
         * errors should not make or break the model
         *
         * Method is overly simple, and is only here to minimize risk of flipped
         * operators and accidental conversion the wrong way.
         *
         * @param from base input units
         * @param to units to convert to
         * @return value to multiply the from value to get the to value
         */
        public static double getConversion(AreaUnit from, AreaUnit to) {
            return to.baseunit / from.baseunit;
        }
        
        /**
         * Used for getting the bracketed unit description, i.e. (sq.m.) or
         * (sq.km).
         *
         * @return bracketed descriptor
         */
        @Override
        public String getBracketedDesciptor() {
            return "("+desc+")";
        }

        /**
         * Used for getting the non bracketed unit description, i.e. sq.m. or
         * sq.km.
         *
         * @return
         */
        @Override
        public String getDesciptor() {
            return desc;
        }
        
        

        /**
         * same as valueOf with relaxed inputs ignores the case, note values are
         * not plural and accepts common aliases, i.e. inch, in, inches, ft,
         * feet, etc... also compares to getDescriptor();
         *
         * @param depthunit Hour, HOUR, hour or similar not plural string
         * @return
         */
        public static AreaUnit valueOfIngoreCase(String depthunit) {
            for (AreaUnit au : AreaUnit.values()) {
                if (au.name().equalsIgnoreCase(depthunit)
                        || au.getDesciptor().equalsIgnoreCase(depthunit)
                        || au.getBracketedDesciptor().equalsIgnoreCase(depthunit)) {
                    return au;
                }
            }

            //Special cases
            return switch (depthunit.toLowerCase()) {
                case "ac" ->
                    AreaUnit.acre;
                case "sq.m" ->
                    AreaUnit.m2;
                default ->
                    null;
            };
        }
    }
}
