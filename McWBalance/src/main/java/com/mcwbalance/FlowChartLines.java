/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance;

import java.awt.Rectangle;

/**
 *
 * @author amcintyre
 */
public class FlowChartLines {
    
    int[] xPoints = new int[6];
    int[] yPoints = new int[6];
    int nPoints;
    
    
    int[] arwxPoints = new int[3];
    int[] arwyPoints = new int[3];
    int arwnPoints; 
    private final int arwsize = 10; // sets size of arrow. 
    
    FlowChartLines(){ // constructs a blank Polyline 5 segments long
        nPoints = 0;
        for (int i=0; i < 6; i++){
            xPoints[i] = 0;
            yPoints[i] = 0; 
        }
        arwnPoints = 3;
        for (int i=0; i < 3; i++){
            arwxPoints[i] = 0;
            arwyPoints[i] = 0; 
        }
        
    }
    
    public void SetRoute(Rectangle sRec, String sSide,int sOset, Rectangle eRec, String eSide, int eOset, int minLength){
        Rectangle fRec = sRec; // Used to flip direction!
        String fSide = sSide; // Used to flip direction!
        int fOset = sOset; // Used to flip direction!

        //Arrow Drawing COmes First, because for Line Drawing the s and e variables can be swapped for mirrored cases (i.e. LEFT to TOP is just backwards TOP to LEFT)
        switch(eSide){
            case "LEFT":
                arwxPoints[0] = eRec.x; // sets arrow start on side of box
                arwyPoints[0] = eRec.y + eRec.height/2 + eOset; // offset applies up down in this case
                arwxPoints[1] = arwxPoints[0] - arwsize;
                arwyPoints[1] = arwyPoints[0] + arwsize/2; // draws down 1/2 of arrow size  
                arwxPoints[2] = arwxPoints[1];
                arwyPoints[2] = arwyPoints[1] - arwsize; // pops back up.
                break;
            case "RIGHT":
                arwxPoints[0] = eRec.x + eRec.width; // sets arrow start on side of box
                arwyPoints[0] = eRec.y + eRec.height/2 + eOset; // offset applies up down in this case
                arwxPoints[1] = arwxPoints[0] + arwsize;
                arwyPoints[1] = arwyPoints[0] + arwsize/2; // draws down 1/2 of arrow size  
                arwxPoints[2] = arwxPoints[1];
                arwyPoints[2] = arwyPoints[1] - arwsize; // pops back up.
                break;
            case "TOP": // Note this oen goes through the lable 
                arwxPoints[0] = eRec.x + eRec.width/2 + eOset;
                arwyPoints[0] = eRec.y; // top plane of object
                arwxPoints[1] = arwxPoints[0] + arwsize/2;
                arwyPoints[1] = arwyPoints[0] - arwsize; // goes up
                arwxPoints[2] = arwxPoints[1] - arwsize;
                arwyPoints[2] = arwyPoints[1];
                break;
            case "BOTTOM":
                arwxPoints[0] = eRec.x + eRec.width/2 + eOset;
                arwyPoints[0] = eRec.y + eRec.height; // top plane of object
                arwxPoints[1] = arwxPoints[0] + arwsize/2;
                arwyPoints[1] = arwyPoints[0] + arwsize; // Draws Down
                arwxPoints[2] = arwxPoints[1] - arwsize;
                arwyPoints[2] = arwyPoints[1];
                break;
        }
        
        
        
        // Line Routing
        // Checks to see if Case should be flipped (i.e left to right is just reverse of right to left)
        switch(sSide){
            case "LEFT":
                switch(eSide){
                    case "TOP":
                        sRec = eRec; 
                        sSide = eSide;
                        sOset = eOset; 
                        eRec = fRec;
                        eSide = fSide; 
                        eOset = fOset;
                        break;
                    case "RIGHT":
                        sRec = eRec; 
                        sSide = eSide;
                        sOset = eOset; 
                        eRec = fRec;
                        eSide = fSide; 
                        eOset = fOset;
                        break;
                }
            case"RIGHT":
                switch(eSide){
                    case "TOP": // must come before TOP RIGHT, Flips
                        sRec = eRec; 
                        sSide = eSide;
                        sOset = eOset; 
                        eRec = fRec;
                        eSide = fSide; 
                        eOset = fOset;
                        break;
                }
                break;
            case "BOTTOM":
                switch(eSide){
                    case "TOP":
                        sRec = eRec; 
                        sSide = eSide;
                        sOset = eOset; 
                        eRec = fRec;
                        eSide = fSide; 
                        eOset = fOset;
                        break;
                    case "LEFT":
                        sRec = eRec; 
                        sSide = eSide;
                        sOset = eOset; 
                        eRec = fRec;
                        eSide = fSide; 
                        eOset = fOset;
                        break;
                    case "RIGHT":
                        sRec = eRec; 
                        sSide = eSide;
                        sOset = eOset; 
                        eRec = fRec;
                        eSide = fSide; 
                        eOset = fOset;
                        break;
                }
        }

        switch(sSide){ // Starting side from origin object
            case "LEFT":
                switch(eSide){ // terminiating side
                    case "LEFT":
                        break;
                    case "BOTTOM":
                        break;
                }
                break;            
            case "RIGHT":
                switch(eSide){
                    case "LEFT":
                        break;
                    case "RIGHT":
                        break; 
                    case "BOTTOM":
                        break;
                }
                break;
            case "TOP":
                xPoints[0] = sRec.x + sRec.width/2 + sOset;      
                yPoints[0] = sRec.y;   
                xPoints[1] = xPoints[0]; // Line must exit vertically up.
                switch(eSide){
                    case "LEFT":
                        if(xPoints[0] <= (eRec.x - minLength) && yPoints[0] - minLength >= (eRec.y + eRec.height/2)){
                            // Line goes up then right
                            yPoints[1] = eRec.y + eRec.height/2 + eOset;
                            xPoints[2] = eRec.x;
                            yPoints[2] = yPoints[1];
                            nPoints = 3;
                        }
                        else if(xPoints[0] <= (eRec.x - minLength) && yPoints[0] - minLength < (eRec.y + eRec.height/2)){
                            yPoints[1] = sRec.y - minLength; 
                            xPoints[2] = (sRec.x + sRec.width + eRec.x)/2; //midpoint between boxes
                            yPoints[2] = yPoints[1];
                            xPoints[3] = xPoints[2];
                            yPoints[3] = eRec.y + eRec.height/2 + eOset;
                            xPoints[4] = eRec.x;
                            yPoints[4] = yPoints[3];
                            nPoints = 5;
                        }
                        else if(xPoints[0] > (eRec.x - minLength) && yPoints[0] - minLength < (eRec.y + eRec.height/2)){
                            yPoints[1] = sRec.y - minLength; 
                            xPoints[2] = eRec.x - minLength; // goes to far side of end object
                            yPoints[2] = yPoints[1];
                            xPoints[3] = xPoints[2];
                            yPoints[3] = eRec.y + eRec.height/2 + eOset;
                            xPoints[4] = eRec.x;
                            yPoints[4] = yPoints[3];
                            nPoints = 5;
                        }
                        else if(xPoints[0] > (eRec.x - minLength) && yPoints[0] - minLength >= (eRec.y + eRec.height/2)){
                            yPoints[1] = (eRec.y + eRec.height + sRec.y)/2;
                            xPoints[2] = eRec.x - minLength; // goes to far side of end object
                            yPoints[2] = yPoints[1];
                            xPoints[3] = xPoints[2];
                            yPoints[3] = eRec.y + eRec.height/2 + eOset;
                            xPoints[4] = eRec.x;
                            yPoints[4] = yPoints[3];
                            nPoints = 5;
                        }
                        break;
                    case "RIGHT": // 5 sub possibilites here
                        if(xPoints[0] >= (eRec.x + eRec.width + minLength) && (yPoints[0] - minLength) >= (eRec.y + eRec.height/2)){
                            // Line goes up then Left 2 segments
                            yPoints[1] = eRec.y + eRec.height/2 + eOset;
                            xPoints[2] = eRec.x + eRec.width;
                            yPoints[2] = yPoints[1];
                            nPoints = 3;
                        }
                        else if(xPoints[0] >= (eRec.x + eRec.width + minLength) && (yPoints[0] - minLength) < (eRec.y + eRec.height/2)){
                            // line goes up, then left, then down then left
                            yPoints[1] = yPoints[0] - minLength;
                            xPoints[2] = (sRec.x + eRec.x + eRec.width)/2; // midpoint between both objects
                            yPoints[2] = yPoints[1];
                            xPoints[3] = xPoints[2];
                            yPoints[3] = eRec.y + eRec.height/2 + eOset;
                            xPoints[4] = eRec.x + eRec.width;
                            yPoints[4] = yPoints [3];
                            nPoints = 5;
                        }
                        else if(xPoints[0] < (eRec.x + eRec.width + minLength) && (yPoints[0] - minLength) < (eRec.y + eRec.height/2)){
                            // line goes up, right, down, left
                            yPoints[1] = yPoints[0] - minLength;
                            xPoints[2] = (eRec.x + eRec.width + minLength);
                            yPoints[2] = yPoints[1];
                            xPoints[3] = xPoints[2];
                            yPoints[3] = eRec.y + eRec.height/2 + eOset;
                            xPoints[4] = eRec.x + eRec.width;
                            yPoints[4] = yPoints [3];
                            nPoints = 5;
                        }
                        else if(xPoints[0] < (eRec.x + eRec.width + minLength) && (yPoints[0] - minLength) >= (eRec.y + eRec.height/2)){
                            // line goes up, right, up, left
                            yPoints[1] = (eRec.y + sRec.y + sRec.height)/2;
                            xPoints[2] = (eRec.x + eRec.width + minLength);
                            yPoints[2] = yPoints[1];
                            xPoints[3] = xPoints[2];
                            yPoints[3] = eRec.y + eRec.height/2 + eOset;
                            xPoints[4] = eRec.x + eRec.width;
                            yPoints[4] = yPoints [3];
                            nPoints = 5;
                        }
                        break;
                    case "TOP":
                        if (yPoints[0] <= eRec.y){ // below
                            yPoints[1] = yPoints[0] - minLength;
                            yPoints[2] = yPoints[1];
                            if (eRec.x + eRec.width/2 + eOset >= sRec.x + sRec.width + minLength){ // Below and to the right
                                xPoints[2] = eRec.x + eRec.width/2 + eOset;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = eRec.y;
                                nPoints = 4;
                            }
                            else if(eRec.x + eRec.width/2 + eOset >= sRec.x + sRec.width/2){ // below and to centre right
                                xPoints[2] = sRec.x + sRec.width + minLength;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = (sRec.y + sRec.height + eRec.y)/2;
                                yPoints[4] = yPoints[3];
                                xPoints[4] = eRec.x + eRec.width/2 + eOset;
                                xPoints[5] = xPoints[4];
                                yPoints[5] = eRec.y;
                                nPoints = 6;
                            }
                            else if(eRec.x + eRec.width/2 + eOset >= sRec.x - minLength){ // below and to centre left
                                xPoints[2] = sRec.x - minLength;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = (sRec.y + sRec.height + eRec.y)/2;
                                yPoints[4] = yPoints[3];
                                xPoints[4] = eRec.x + eRec.width/2 + eOset;
                                xPoints[5] = xPoints[4];
                                yPoints[5] = eRec.y;
                                nPoints = 6;
                            }
                            else{ // Below and to the left
                                xPoints[2] = eRec.x + eRec.width/2 + eOset;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = eRec.y;
                                nPoints = 4;
                            }
                        }
                        else { // above 
                            if (eRec.x - minLength >=  sRec.x + sRec.width/2 + sOset){ // above to far right
                                yPoints[1] = eRec.y - minLength;
                                yPoints[2] = yPoints[1];
                                xPoints[2] = eRec.x + eRec.width/2 + eOset;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = eRec.y;
                                nPoints = 4;
                            }
                            else if(eRec.x + eRec.width/2 >= sRec.x + sRec.width/2 + sOset){ // above to close right
                                yPoints[1] = (eRec.y + eRec.height + sRec.y)/2;
                                yPoints[2] = yPoints[1];
                                xPoints[2] = eRec.x - minLength;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = eRec.y - minLength;
                                yPoints[4] = yPoints[3];
                                xPoints[4] = eRec.x + eRec.width/2 + eOset;
                                xPoints[5] = xPoints[4];
                                yPoints[5] = eRec.y;
                                nPoints = 6;
                            }
                            else if (eRec.x + eRec.width + minLength >= sRec.x + sRec.width/2 + sOset){ // above to close left
                                yPoints[1] = (eRec.y + eRec.height + sRec.y)/2;
                                yPoints[2] = yPoints[1];
                                xPoints[2] = eRec.x + eRec.width + minLength;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = eRec.y - minLength;
                                yPoints[4] = yPoints[3];
                                xPoints[4] = eRec.x + eRec.width/2 + eOset;
                                xPoints[5] = xPoints[4];
                                yPoints[5] = eRec.y;
                                nPoints = 6;
                            }
                            else {
                                yPoints[1] = eRec.y - minLength;
                                yPoints[2] = yPoints[1];
                                xPoints[2] = eRec.x + eRec.width/2 + eOset;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = eRec.y;
                                nPoints = 4;
                            }
                        }
                        break;
                    case "BOTTOM":
                        if (yPoints[0] >= eRec.y +eRec.height + minLength){//only need to check y in this case;
                            yPoints[1] = (eRec.y + eRec.height + sRec.y)/2; // SHould Revist!..  
                            xPoints[2] = eRec.x + eRec.width/2 + eOset;
                            yPoints[2] = yPoints[1];
                            xPoints[3] = xPoints[2];
                            yPoints[3] = eRec.y + eRec.height;
                            nPoints = 4;
                        }
                        else if(eRec.x + eRec.width <= sRec.x){ // NOte y check is not needed
                            yPoints[1] = yPoints[0] - minLength;
                            xPoints[2] = (eRec.x + eRec.width + sRec.x)/2;
                            yPoints[2] = yPoints[1];
                            xPoints[3] = xPoints[2];
                            yPoints[3] = eRec.y + eRec.height + minLength;
                            xPoints[4] = eRec.x + eRec.width/2 + eOset;
                            yPoints[4] = yPoints[3];
                            xPoints[5] = xPoints[4];
                            yPoints[5] = eRec.y + eRec.height;
                            nPoints = 6;
                        }
                        else if(eRec.x >= sRec.x + sRec.width){ // NOte y check is not needed
                            yPoints[1] = yPoints[0] - minLength;
                            xPoints[2] = (eRec.x + sRec.x + sRec.width)/2;
                            yPoints[2] = yPoints[1];
                            xPoints[3] = xPoints[2];
                            yPoints[3] = eRec.y + eRec.height + minLength;
                            xPoints[4] = eRec.x + eRec.width/2 + eOset;
                            yPoints[4] = yPoints[3];
                            xPoints[5] = xPoints[4];
                            yPoints[5] = eRec.y + eRec.height;
                            nPoints = 6;  
                        }
                        else if(eRec.x + eRec.width/2 <= sRec.x + sRec.width/2){ // NOte y check is not needed
                            yPoints[1] = yPoints[0] - minLength;
                            xPoints[2] = (sRec.x - minLength);
                            yPoints[2] = yPoints[1];
                            xPoints[3] = xPoints[2];
                            yPoints[3] = eRec.y + eRec.height + minLength;
                            xPoints[4] = eRec.x + eRec.width/2 + eOset;
                            yPoints[4] = yPoints[3];
                            xPoints[5] = xPoints[4];
                            yPoints[5] = eRec.y + eRec.height;
                            nPoints = 6;            
                        }
                        else if(eRec.x + eRec.width/2 > sRec.x + sRec.width/2){ // NOte y check is not needed
                            yPoints[1] = yPoints[0] - minLength;
                            xPoints[2] = (sRec.x + sRec.width + minLength);
                            yPoints[2] = yPoints[1];
                            xPoints[3] = xPoints[2];
                            yPoints[3] = eRec.y + eRec.height + minLength;
                            xPoints[4] = eRec.x + eRec.width/2 + eOset;
                            yPoints[4] = yPoints[3];
                            xPoints[5] = xPoints[4];
                            yPoints[5] = eRec.y + eRec.height;
                            nPoints = 6;        
                        }
                        break;
                }
                break;
            case "BOTTOM":
                yPoints[0] = sRec.y + sRec.height;
                xPoints[0] = sRec.x + sRec.width/2 + sOset;
                xPoints[1] = xPoints[0];
                
                
                switch(eSide){
                    case "BOTTOM": // copied from top to top so not set yet
                        if (yPoints[0] >= eRec.y + eRec.height){ // above
                            yPoints[1] = yPoints[0] + minLength;
                            yPoints[2] = yPoints[1];
                            if (eRec.x + eRec.width/2 + eOset >= sRec.x + sRec.width + minLength){ // Above and to the right
                                xPoints[2] = eRec.x + eRec.width/2 + eOset;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = eRec.y;
                                nPoints = 4;
                            }
                            else if(eRec.x + eRec.width/2 + eOset >= sRec.x + sRec.width/2){ // Above and to centre right
                                xPoints[2] = sRec.x + sRec.width + minLength;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = (sRec.y + sRec.height + eRec.y)/2;
                                yPoints[4] = yPoints[3];
                                xPoints[4] = eRec.x + eRec.width/2 + eOset;
                                xPoints[5] = xPoints[4];
                                yPoints[5] = eRec.y;
                                nPoints = 6;
                            }
                            else if(eRec.x + eRec.width/2 + eOset >= sRec.x - minLength){ // Above and to centre left
                                xPoints[2] = sRec.x - minLength;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = (sRec.y + sRec.height + eRec.y)/2;
                                yPoints[4] = yPoints[3];
                                xPoints[4] = eRec.x + eRec.width/2 + eOset;
                                xPoints[5] = xPoints[4];
                                yPoints[5] = eRec.y;
                                nPoints = 6;
                            }
                            else{ // Above and to the left
                                xPoints[2] = eRec.x + eRec.width/2 + eOset;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = eRec.y;
                                nPoints = 4;
                            }
                        }
                        else { // Below
                            if (eRec.x - minLength >=  sRec.x + sRec.width/2 + sOset){ // Below to far right
                                yPoints[1] = eRec.y + eRec.height + minLength;
                                yPoints[2] = yPoints[1];
                                xPoints[2] = eRec.x + eRec.width/2 + eOset;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = eRec.y + eRec.height;
                                nPoints = 4;
                            }
                            else if(eRec.x + eRec.width/2 >= sRec.x + sRec.width/2 + sOset){ // Below to close right
                                yPoints[1] = (sRec.y + sRec.height + eRec.y)/2;
                                yPoints[2] = yPoints[1];
                                xPoints[2] = eRec.x - minLength;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = eRec.y + eRec.height + minLength;
                                yPoints[4] = yPoints[3];
                                xPoints[4] = eRec.x + eRec.width/2 + eOset;
                                xPoints[5] = xPoints[4];
                                yPoints[5] = eRec.y + eRec.height;
                                nPoints = 6;
                            }
                            else if (eRec.x + eRec.width + minLength >= sRec.x + sRec.width/2 + sOset){ // Below to close left
                                yPoints[1] = (sRec.y + sRec.height + eRec.y)/2;
                                yPoints[2] = yPoints[1];
                                xPoints[2] = eRec.x + eRec.width + minLength;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = eRec.y + eRec.height + minLength;
                                yPoints[4] = yPoints[3];
                                xPoints[4] = eRec.x + eRec.width/2 + eOset;
                                xPoints[5] = xPoints[4];
                                yPoints[5] = eRec.y + eRec.height;
                                nPoints = 6;
                            }
                            else {
                                yPoints[1] = eRec.y + eRec.height + minLength;
                                yPoints[2] = yPoints[1];
                                xPoints[2] = eRec.x + eRec.width/2 + eOset;
                                xPoints[3] = xPoints[2];
                                yPoints[3] = eRec.y + eRec.height;
                                nPoints = 4;
                            }
                        }
                        break;
                }
                break;
        } // End of Line Routing
        // Arrow Drawing
        
     
        
        
    }  
    
    
    
    
}
