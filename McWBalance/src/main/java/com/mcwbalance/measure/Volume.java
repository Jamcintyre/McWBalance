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
 * For operations involving volumes, and conversions from say m3 to acre-feet
 *
 * @author alex
 */
public class Volume {

    /**
     * units of volume allowed in model
     */
    public static enum VolumeUnit {

        /**
         * Cubic meter
         */
        m3("(cu.m.)", 1),
        /**
         * Cubic hectometer = 1,000,000 m3
         */
        hm3("(Million cu.m.)", 1000000),
        /**
         * Cubic Yard
         */
        yd3("(cu.yd.)", 0.764554857984),
        /**
         * Acre-foot
         */
        acrefoot("(acre-foot)", 1233.48);

        private final String desc;
        private final double baseunit;

        private VolumeUnit(String desc, double baseunit) {
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
        public static double getConversion(VolumeUnit from, VolumeUnit to) {
            return to.baseunit / from.baseunit;
        }

        /**
         * Used for getting the bracketed unit description, i.e. (acre-foot) or
         * (cu.m).
         *
         * @return
         */
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
        public static VolumeUnit valueOfIngoreCase(String depthunit) {
            for (VolumeUnit vu : VolumeUnit.values()) {
                if (vu.name().equalsIgnoreCase(depthunit)
                        || vu.getDesciptor().equalsIgnoreCase(depthunit)) {
                    return vu;
                }
            }

            //Special cases
            return switch (depthunit.toLowerCase()) {
                case "Mm3" ->
                    VolumeUnit.hm3; // becuase most engineers dont know basic metric
                default ->
                    null;
            };
        }
    }
}
