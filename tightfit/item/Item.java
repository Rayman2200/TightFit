/*
 *  TightFit (c) 2008 Adam Turk
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Adam Turk <aturk@biggeruniverse.com>
 */

package tightfit.item;

import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.Icon;

public class Item {
    protected Icon icon;
    
    static public HashMap attributeNames;
    
    public String name;
    public double mass;
    public double volume;
    public double capacity;
    public int catId;
    public int typeId;
    public int groupId;
    
    public Hashtable attributes;
}
