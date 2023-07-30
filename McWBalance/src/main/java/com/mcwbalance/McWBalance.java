/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mcwbalance;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    static String FILE_EXTENSION = "mcbl";
    static String LANGUAGE_RESOURCE = "Language";
    static String localeOptions;
    static Locale currentLocale;
    
    public static ResourceBundle langRB;
    public static Properties style = new Properties();
    static Properties localeprops = new Properties();
    
    
    public static FileNameExtensionFilter DEFAULT_FILEEXTENSION_FILTER = new FileNameExtensionFilter("McBalance File",FILE_EXTENSION);

    public static void main(String[] args) {
        
        //setLocale("es");
        langRB = ResourceBundle.getBundle(LANGUAGE_RESOURCE);

        try{
            style.load(new java.io.FileInputStream("src/main/resources/Style.properties"));
        } catch (IOException e) {
            System.out.println("Style Properties Not Found");
        }
        
        try{
            localeprops.load(new java.io.FileInputStream("src/main/resources/Locales.properties"));
        } catch (IOException e) {
            System.out.println("Locales Properties Not Found");
        }
        
        
        
        try {
            mainIcon30 = ImageIO.read(new File("bin/Icon30.png"));
        } catch (IOException e) {
            System.out.println("IconFile Not Found");
        }
        try {
            // Set System L&F

            //UIManager.setLookAndFeel                     UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(new FlatDarkLaf());
            Preferences.setDefaultColors(Preferences.DARK_MODE);
            
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
            /*
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
            */
        }

         
        System.out.println("McBalance Version " + ProjSetting.verInfo + " is loading");
        MainWindow mainWin = new MainWindow();
        mainWin.MainWindowFunct(); 
    }
    /**
     * note this doesn't work correctly, once data bundle is loaded it appears the data cannot change
     * @param newLocal 
     */
    public static void setLocale(String newLocal){
        
        String[] localCodes = newLocal.split("_");
        
        if(localCodes.length == 1){
           currentLocale = new Locale(localCodes[0]);
           
           System.out.println("language Changed to " + localCodes[0]);
        }
        else{
           currentLocale = new Locale(localCodes[0], localCodes[1]);
           System.out.println("language Changed to " + localCodes[0] + " " + localCodes[1]);
        }
        
        langRB = ResourceBundle.getBundle(LANGUAGE_RESOURCE, currentLocale);
        
    }
    
    
    
    
}
