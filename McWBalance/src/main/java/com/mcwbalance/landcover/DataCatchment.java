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
 *
 * @author Alex McIntyre
 */
public class DataCatchment {

    int nPoints;
    int[] time;
    int[] area;
    String LandCover;

    DataCatchment(int numberofPoints) {
        nPoints = numberofPoints;
        time = new int[nPoints];
        area = new int[nPoints];
        LandCover = "none";
    }

    /**
     * returns the area given the a timestep
     *
     * @param timestep
     * @return
     */
    public int getArea(int timestep) {
        if (timestep < time[0]) {
            return 0;
        }
        if (timestep > time[nPoints - 1]) { // doesn't need else as statement above is a return
            return 0;
        }
        for (int i = 0; i < nPoints; i++) {
            if (timestep >= time[i]) {
                return area[i];
            }
        }
        return -1; // not found
    }

    /**
     * Used for getting a specific indexed value
     *
     * @param index
     * @return
     */
    public int getAreaAtIndex(int index) {
        return area[index];
    }

    /**
     * Number of catchment data points
     *
     * @return
     */
    public int getLength() {
        return nPoints;
    }

    /**
     * Name of land cover, will be important to keep track to allow separate
     * reporting to facilitate water quality modeling
     *
     * @return
     */
    public String getLandCover() {
        return LandCover;
    }

    /**
     * Used for getting a specific indexed value
     *
     * @param index
     * @return
     */
    public int getTimeAtIndex(int index) {
        return time[index];
    }

}
