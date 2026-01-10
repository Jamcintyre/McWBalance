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

package com.mcwbalance;

import com.mcwbalance.project.ProjSetting;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * POTENTIAL NAME CHANGE -MAquaBal or MAB Mine Water Balance
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
    static final String FILE_EXTENSION = "mcbl";
    static final String LANGUAGE_RESOURCE = "Language";
    
    static final String PROGRAM_NAME = "McBalance";
    static Locale currentLocale;
    
    /**
     * Bin for all short dialogs and words
     */
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
        
        /*
        try {
            // Set System L&F

            //UIManager.setLookAndFeel                     UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(new FlatDarkLaf());
            Preferences.setDefaultColors(Preferences.LIGHT_MODE);
            
        } catch (UnsupportedLookAndFeelException e) {
        
        }
        */

         
        System.out.println("McBalance Version " + ProjSetting.verInfo + " is loading");
        MainWindow mainWin = new MainWindow(); 
    }
    /**
     * note this doesn't work correctly, once data bundle is loaded it appears the data cannot change
     * @param newLocal 
     */
    public static void setLocale(String newLocal){
        
        String[] localCodes = newLocal.split("_");
        
        Locale.of(newLocal);
        
        if(localCodes.length == 1){
           currentLocale = Locale.of(localCodes[0]);
           
           System.out.println("language Changed to " + localCodes[0]);
        }
        else{
           currentLocale = Locale.of(localCodes[0], localCodes[1]);
           System.out.println("language Changed to " + localCodes[0] + " " + localCodes[1]);
        }
        
        langRB = ResourceBundle.getBundle(LANGUAGE_RESOURCE, currentLocale);
        
    }

    
}
