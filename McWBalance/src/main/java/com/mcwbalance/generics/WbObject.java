/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mcwbalance.generics;

import java.awt.Rectangle;

/**
 *
 * @author Alex
 */
public abstract class WbObject {
    public int x;
    public int y;
    public Rectangle hitBox;
    public boolean isSelected;
    public String name;
    public String subType;

    
    public abstract void setFromString(String string);
}
