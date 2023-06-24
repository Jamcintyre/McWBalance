
package com.mcwbalance;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Static container for loading and storing all png images that are used within the program including the program Icon
 * @author amcintyre
 */
public class ImageLib {
    static final int MAINSPRITE_HEIGHT = 50; 
    static final int MAINSPRITE_WIDTH = 200;
    static final int PLANTSPRITE_WIDTH = 100;
    static final int PLANTSPRITE_HEIGHT = 50;
    static final int WELLSPRITE_WIDTH = 50;
    static final int WELLSPRITE_HEIGHT = 50;
    static final int UNDERGROUNDSPRITE_WIDTH = 100;
    static final int UNDERGROUNDSPRITE_HEIGHT = 100; 
  
    static BufferedImage MAINSPRITES;
    static BufferedImage PLANTSPRITE;
    static BufferedImage WELLSPRITE;
    static BufferedImage UNDERGROUNDSPRITE;
    
    public ImageLib(){ // constructor
        try {
            MAINSPRITES = ImageIO.read(new File("bin/MainSprites.png"));
            PLANTSPRITE = ImageIO.read(new File("bin/PlantSprite.png"));
            WELLSPRITE = ImageIO.read(new File("bin/WellSprite.png"));
            UNDERGROUNDSPRITE = ImageIO.read(new File("bin/UnderGroundSprite.png"));
        }
        catch (IOException e){
            System.out.println("IconFile Not Found");
        }
    }
    public BufferedImage getImage(String objSubType, String state, double scaleX, double scaleY){
        int assetColRight = 0;
        int assetColLeft = 0 ;
        int assetRow = 0;
        boolean singleSide = false; 
        BufferedImage leftSide;
        BufferedImage rightSide;
        BufferedImage compSprite; 
        BufferedImage scaledSprite;// = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.setToScale(scaleX, scaleY);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        
        
        switch (state){ // state switch is completed first since some objects pull from other spritesheets. 
            case "ACTIVE":
                assetRow = 0;
                break;
            case "UNDERCONSTRUCTION":
                assetRow = 1;
                break;
            case "CONSTRUCTEDEMPTY":
                assetRow = 2;
                break;                
            case "HALFWATER":
                assetRow = 3;
                break;        
            case "FULLWATER":
                assetRow = 4;
                break;        
            case "TAILINGS":
                assetRow = 5;
                break;                
            case "TAILINGSWATERCOVER":
                assetRow = 6;
                break;        
            case "TAILINGSDRY":
                assetRow = 7;
                break;            
            case "CLOSEDDRYCOVER":
                assetRow = 8;
                break;
            case "CLOSEDWETCOVER":
                assetRow = 9;
                break;     
            default:
                assetRow = 0;
        }
        
        
        switch (objSubType){
            case "DAM_PADDOCK":
                assetColLeft = 0;
                assetColRight = 0;
                leftSide = MAINSPRITES.getSubimage(assetColLeft * MAINSPRITE_WIDTH, assetRow * MAINSPRITE_HEIGHT, MAINSPRITE_WIDTH, MAINSPRITE_HEIGHT);
                rightSide = MAINSPRITES.getSubimage(assetColRight * MAINSPRITE_WIDTH, assetRow * MAINSPRITE_HEIGHT, MAINSPRITE_WIDTH, MAINSPRITE_HEIGHT);
                compSprite = stitch(leftSide, mirror(rightSide));
                break;
            case "DAM_VALLEY_LEFT":
                assetColLeft = 0;
                assetColRight = 1;
                leftSide = MAINSPRITES.getSubimage(assetColLeft * MAINSPRITE_WIDTH, assetRow * MAINSPRITE_HEIGHT, MAINSPRITE_WIDTH, MAINSPRITE_HEIGHT);
                rightSide = MAINSPRITES.getSubimage(assetColRight * MAINSPRITE_WIDTH, assetRow * MAINSPRITE_HEIGHT, MAINSPRITE_WIDTH, MAINSPRITE_HEIGHT);
                compSprite = stitch(leftSide, mirror(rightSide));
                break;
            case "DAM_VALLEY_RIGHT":
                assetColLeft = 1;
                assetColRight = 0;
                leftSide = MAINSPRITES.getSubimage(assetColLeft * MAINSPRITE_WIDTH, assetRow * MAINSPRITE_HEIGHT, MAINSPRITE_WIDTH, MAINSPRITE_HEIGHT);
                rightSide = MAINSPRITES.getSubimage(assetColRight * MAINSPRITE_WIDTH, assetRow * MAINSPRITE_HEIGHT, MAINSPRITE_WIDTH, MAINSPRITE_HEIGHT);
                compSprite = stitch(leftSide, mirror(rightSide));
                break;
            case "LAKE":
                assetColLeft = 1;
                assetColRight = 1;
                leftSide = MAINSPRITES.getSubimage(assetColLeft * MAINSPRITE_WIDTH, assetRow * MAINSPRITE_HEIGHT, MAINSPRITE_WIDTH, MAINSPRITE_HEIGHT);
                rightSide = MAINSPRITES.getSubimage(assetColRight * MAINSPRITE_WIDTH, assetRow * MAINSPRITE_HEIGHT, MAINSPRITE_WIDTH, MAINSPRITE_HEIGHT);
                compSprite = stitch(leftSide, mirror(rightSide));
                break;
            case "OPENPIT":
                assetColLeft = 2;
                compSprite = MAINSPRITES.getSubimage(assetColLeft * MAINSPRITE_WIDTH, assetRow * MAINSPRITE_HEIGHT, MAINSPRITE_WIDTH, MAINSPRITE_HEIGHT);
                break;
            case "PLANT":
                if (assetRow > 1){
                    compSprite = PLANTSPRITE.getSubimage(0, 0, PLANTSPRITE_WIDTH, PLANTSPRITE_HEIGHT);
                    break;
                }
                compSprite = PLANTSPRITE.getSubimage(0, assetRow * PLANTSPRITE_HEIGHT, PLANTSPRITE_WIDTH, PLANTSPRITE_HEIGHT);
                break;
            case "STOCKPILE":
                assetColLeft = 3;
                compSprite = MAINSPRITES.getSubimage(assetColLeft * MAINSPRITE_WIDTH, assetRow * MAINSPRITE_HEIGHT, MAINSPRITE_WIDTH, MAINSPRITE_HEIGHT);
                break;
            case "UNDERGROUND":
                compSprite = UNDERGROUNDSPRITE.getSubimage(0, assetRow * UNDERGROUNDSPRITE_HEIGHT, UNDERGROUNDSPRITE_WIDTH, UNDERGROUNDSPRITE_HEIGHT);
                break;
            case "WELL":
                if (assetRow > 2){
                    compSprite = WELLSPRITE.getSubimage(0, 0, WELLSPRITE_WIDTH, WELLSPRITE_HEIGHT);
                    break;
                }
                if (assetRow == 0){
                    compSprite = WELLSPRITE.getSubimage(0, 0, WELLSPRITE_WIDTH, WELLSPRITE_HEIGHT);
                    break;
                }else{ // debug,  this did not fix the problem.  for some reason getSubImage is trowing an error about the height being off. 
                    compSprite = WELLSPRITE.getSubimage(0, assetRow * WELLSPRITE_HEIGHT -1, WELLSPRITE_WIDTH, WELLSPRITE_HEIGHT);
                    break;
                }
            default:
                assetColRight = 0;
                assetColLeft = 0;
                leftSide = MAINSPRITES.getSubimage(assetColLeft * MAINSPRITE_WIDTH, assetRow * MAINSPRITE_HEIGHT, MAINSPRITE_WIDTH, MAINSPRITE_HEIGHT);
                rightSide = MAINSPRITES.getSubimage(assetColRight * MAINSPRITE_WIDTH, assetRow * MAINSPRITE_HEIGHT, MAINSPRITE_WIDTH, MAINSPRITE_HEIGHT);
                compSprite = stitch(leftSide, mirror(rightSide));
        }
        scaledSprite = new BufferedImage((int)(compSprite.getWidth()*scaleX), (int)(compSprite.getHeight()*scaleY),BufferedImage.TYPE_INT_ARGB);
        scaleOp.filter(compSprite, scaledSprite);
        return scaledSprite;
    }
    /**
     * Pixel by Pixel method for mirroring and image
     * will need to figure out how to also copy the alpha values,... its not clear how java handles it... 
     * 
     * @param orgImage
     * @return Returns mirror of the provided image buffer
     */
    public BufferedImage mirror(BufferedImage orgImage){
        int width = orgImage.getWidth();
        int height = orgImage.getHeight();
        BufferedImage mirrorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
        for (int y = 0; y < height; y ++){
            for (int lx = 0, rx = width -1; lx < width; lx++, rx--){
                mirrorImage.setRGB(rx, y, orgImage.getRGB(lx, y));
            }
        }
        return mirrorImage;
    }
    public BufferedImage stitch(BufferedImage leftImage, BufferedImage rightImage){
        int lwidth = leftImage.getWidth();
        int rwidth = rightImage.getWidth();
        int height;
        if (leftImage.getHeight() > rightImage.getHeight()){
            height = rightImage.getHeight();
        }else{
            height = leftImage.getHeight();
        }
        BufferedImage stitchImage = new BufferedImage(lwidth+rwidth, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y ++){
            for (int lx = 0; lx < lwidth; lx++){
                stitchImage.setRGB(lx, y, leftImage.getRGB(lx, y));
            }
            for (int rx = 0; rx < rwidth; rx++){
                stitchImage.setRGB(rx+lwidth, y, rightImage.getRGB(rx, y));
            }
        }
        return stitchImage;
    }
   
    
    
}
