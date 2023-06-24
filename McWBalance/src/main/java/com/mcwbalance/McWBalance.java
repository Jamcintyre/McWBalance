/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mcwbalance;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Alex
 */
public class McWBalance {
    static BufferedImage mainIcon30;

    public static void main(String[] args) {
        
         try {    
            mainIcon30 = ImageIO.read(new File("bin/Icon30.png"));
         }
         catch (IOException e){
            System.out.println("IconFile Not Found");
        }
        
        System.out.println("McBalance Version " + ProjSetting.verInfo + " is loading");
        
        MainWindow mainWin = new MainWindow();
        mainWin.MainWindowFunct();
        
    }
    
    
   
}
