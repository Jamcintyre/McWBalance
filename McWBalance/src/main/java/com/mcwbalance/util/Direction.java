package com.mcwbalance.util;

import static com.mcwbalance.McWBalance.langRB;

/**
 * For holding re-usable enums
 * @author alex
 */
public class Direction {
    
    /**
    * enum for picking top bottom left right
    */
    public enum Side {
        TOP,
        BOTTOM,
        LEFT,
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
