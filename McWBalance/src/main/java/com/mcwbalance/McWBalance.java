/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mcwbalance;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * McWBalance is intended to function as a deterministic daily time step mine 
 * water balance planner and solver. The intention is to allow simplified 
 * flowsheet development and operational logic entry that can be visually reviewed.
 * The program is attempting to balance flexibility to allow any and all mine water
 * balance problems to be solved, however, it will remain ridged in enforcing
 * non-material stylistic rules (i.e. font size, chart styles, output format) to
 * ensure consistency between users and projects.
 * @author amcintyre
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
