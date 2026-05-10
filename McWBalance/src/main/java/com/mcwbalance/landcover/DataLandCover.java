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
package com.mcwbalance.landcover;

/**
 * TODO add basis, cleanup unused methods and constructors
 *
 * @author Alex McIntyre
 */
public class DataLandCover {

    private String cover;
    private final double runoffCoeff[];
    private static final double MIN_COEFF = 0;
    private static final double MAX_COEFF = 1;
    private static final double DEFAULT_COEFF = 1;
    private static final int ARRAY_LENGTH = 12;

    DataLandCover() {
        cover = "NEW_COVER";
        runoffCoeff = new double[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            runoffCoeff[i] = DEFAULT_COEFF;
        }
    }

    DataLandCover(String inName) {
        cover = inName;
        runoffCoeff = new double[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            runoffCoeff[i] = DEFAULT_COEFF;
        }
    }

    DataLandCover(double coeff) {
        cover = "NEW_COVER";
        runoffCoeff = new double[ARRAY_LENGTH];
        if (coeff < MIN_COEFF) {
            coeff = MIN_COEFF;
        } else if (coeff > MAX_COEFF) {
            coeff = MAX_COEFF;
        }
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            runoffCoeff[i] = coeff;
        }
    }

    DataLandCover(String inName, double coeff) {
        cover = inName;
        runoffCoeff = new double[ARRAY_LENGTH];
        if (coeff < MIN_COEFF) {
            coeff = MIN_COEFF;
        } else if (coeff > MAX_COEFF) {
            coeff = MAX_COEFF;
        }
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            runoffCoeff[i] = coeff;
        }
    }

    DataLandCover(String inName, double coeff[]) {
        cover = inName;
        runoffCoeff = new double[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            if (i < coeff.length) {
                if (coeff[i] < MIN_COEFF) {
                    coeff[i] = MIN_COEFF;
                } else if (coeff[i] > MAX_COEFF) {
                    coeff[i] = MAX_COEFF;
                }
                runoffCoeff[i] = coeff[i];
            } else {
                runoffCoeff[i] = DEFAULT_COEFF;
            }
        }
    }

    DataLandCover(double coeff[]) {
        cover = "NEW_COVER";
        runoffCoeff = new double[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            if (i < coeff.length) {
                if (coeff[i] < MIN_COEFF) {
                    coeff[i] = MIN_COEFF;
                } else if (coeff[i] > MAX_COEFF) {
                    coeff[i] = MAX_COEFF;
                }
                runoffCoeff[i] = coeff[i];
            } else {
                runoffCoeff[i] = DEFAULT_COEFF;
            }

        }
    }

    /**
     * For using to lookup landcover types
     *
     * @return name of cover
     */
    public String getCoverName() {
        return cover;
    }

    /**
     * Monthly runoff coeff for a specific land cover
     *
     * @param month
     * @return
     */
    public double getMonthlyCoeff(int month) {
        return runoffCoeff[month - 1];
    }

    /**
     * Full set of monthly runoff coefficients
     *
     * @return
     */
    public double[] getCoeffArray() {
        return runoffCoeff;
    }

    /**
     * Takes in and overwrites runoff coefficents applies rules to ensure values
     * between 0 and 1
     *
     * @param inCoeffArray
     */
    public void setAllCoeff(double inCoeffArray[]) {
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            if (i < inCoeffArray.length) {
                if (inCoeffArray[i] < MIN_COEFF) {
                    inCoeffArray[i] = MIN_COEFF;
                } else if (inCoeffArray[i] > MAX_COEFF) {
                    inCoeffArray[i] = MAX_COEFF;
                }
                runoffCoeff[i] = inCoeffArray[i];
            } else {
                runoffCoeff[i] = DEFAULT_COEFF;
            }
        }
    }

    /**
     * Takes in and overwrites runoff coefficents Sets all to the same values,
     * useful for defaulting the array applies rules to ensure values between 0
     * and 1
     *
     * @param inCoeff
     */
    public void setAllCoeff(double inCoeff) {
        if (inCoeff < MIN_COEFF) {
            inCoeff = MIN_COEFF;
        } else if (inCoeff > MAX_COEFF) {
            inCoeff = MAX_COEFF;
        }
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            runoffCoeff[i] = inCoeff;
        }
    }

    /**
     * for overwriting the name of a land cover type
     *
     * @param name
     */
    public void setCoverName(String name) {
        cover = name;
    }

    /**
     * for manual entry of a single coefficient
     *
     * @param inCoeff
     * @param month
     */
    public void setSingleCoeff(double inCoeff, int month) {
        if (inCoeff < MIN_COEFF) {
            inCoeff = MIN_COEFF;
        } else if (inCoeff > MAX_COEFF) {
            inCoeff = MAX_COEFF;
        }
        runoffCoeff[month - 1] = inCoeff;
    }
}
