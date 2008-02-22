/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.item;

import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.Icon;

import tightfit.module.Category;

public class Item {
    protected Icon icon;
    
    static public HashMap attributeNames;
    
    public String name;
    public double mass;
    public double volume;
    public double capacity;
    public int typeId;
    public int groupId;
    public int marketGroupId;
    
    public Hashtable attributes;
 
    public int getCategory() {
    	try {
    		return Database.getInstance().getGroup(groupId).catId;
    	} catch (Exception e) {
    	}
    	
    	return -1;
    }
}
