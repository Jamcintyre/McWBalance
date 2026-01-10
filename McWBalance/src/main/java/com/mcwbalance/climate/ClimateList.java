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
package com.mcwbalance.climate;

/**
 *TODO need to add a way of importing and exporting this list
 * @author alex
 */
public class ClimateList {
    
    DataClimate[] climates;
    
    public ClimateList(int size){
        climates = new DataClimate[size];
        
        
    }
    
    /**
     * 
     * @return provides access to climate list 
     */
    public DataClimate[] getClimates(){
        return climates;
    }
    
    /**
     * used for appending a new climate scenario to the end of the list
     * @param newclimate 
     */
    public void addClimate(DataClimate newclimate){
        DataClimate[] explist = new DataClimate[climates.length +1];
        for (int i = 0; i < climates.length; i++){
            explist[i] = climates[i];
        }
        explist[explist.length-1] = newclimate;
        climates = explist;
    }
    
    /**
     * used for overwriting climates.
     * @param newclimate
     * @param index 
     */
    public void writeclimate(DataClimate newclimate, int index){
        climates[index] = newclimate;
    }
    /**
     * Used for removing a climate scenario from the list by index
     * @param index 
     */
    public void deleteClimate(int index){
        
    }
    
}
