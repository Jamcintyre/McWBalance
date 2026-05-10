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

import static com.mcwbalance.McWBalance.langRB;

/**
 * For holding direction related data
 * @author Alex McIntyre
 */
public class Direction {
    
    /**
    * enum for picking top bottom left right
    */
    public enum Side {
        /**
         * Top side or north 
         */
        TOP,
        /**
         * Bottom side or south
         */
        BOTTOM,
        /**
         * left side or west
         */
        LEFT,
        /**
         * Right side or east
         */
        RIGHT;
    }
    
        /**
     * Lists all possible sides in local language
     * @return 
     */
    public String[] getSidesAllowed() {
        String[] sa = {
        langRB.getString(Side.TOP.toString()),
        langRB.getString(Side.BOTTOM.toString()),
        langRB.getString(Side.LEFT.toString()),
        langRB.getString(Side.RIGHT.toString())
        };
        return sa;
    }
    /**
     * Translates local language side name to enum
     * @param localName
     * @return 
     */
    public Side getSideFromLocal(String localName){
        if(localName.equals(langRB.getString(Side.TOP.toString()))){
            return Side.TOP;
        } else if (localName.equals(langRB.getString(Side.BOTTOM.toString()))){
            return Side.BOTTOM;
        } else if (localName.equals(langRB.getString(Side.LEFT.toString()))){
            return Side.LEFT;
        } else if (localName.equals(langRB.getString(Side.RIGHT.toString()))){
            return Side.RIGHT;
        }
        return Side.TOP;
    }

    
}
