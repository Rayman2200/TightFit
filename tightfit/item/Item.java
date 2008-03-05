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

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import tightfit.Resources;

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
 
    public Item() {
    	attributes = new Hashtable();
    }
    
    public Item(Item type) {
    	name = type.name;
    	mass = type.mass;
    	volume = type.volume;
    	capacity = type.capacity;
    	typeId = type.typeId;
    	groupId = type.groupId;
    	marketGroupId = type.marketGroupId;
    	
    	attributes = (Hashtable)type.attributes.clone();
    }
    
    public int getCategory() {
    	try {
    		return Database.getInstance().getGroup(groupId).catId;
    	} catch (Exception e) {
    	}
    	
    	return -1;
    }
    
    public boolean requiresActivation() {
    	return attributes.containsKey("capacitorNeed");
    }
    
    public String getAttribute(String attrib, String defaultVal) {
    	if(attributes.containsKey(attrib)) {
    		return (String)attributes.get(attrib);
    	}
    	return defaultVal;
    }
    
    public Icon getImage() {
    	if(icon == null) {
    		try {
    			int catId = getCategory();
    			String cat = (catId < 10 ? "0" : "") + catId;
    			String group = (groupId < 10 ? "0" : "") + groupId;
				icon = new ImageIcon(Resources.getImage("icon"+cat+"_"+group+".png"));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	return icon;
    }
    
    public void printAttributes() {
    	Iterator keys = attributes.keySet().iterator();
    	while(keys.hasNext()) {
    		Object key = keys.next();
    		System.out.println(""+key+": "+attributes.get(key));
    	}
    }
}
