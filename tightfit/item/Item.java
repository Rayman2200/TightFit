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

import java.awt.Image;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import tightfit.Resources;

public class Item {
    protected Image icon;
    
    static public HashMap attributeNames;
    
    public String name;
    public double mass;
    public double volume;
    public double capacity;
    public int typeId;
    public int groupId;
    public int marketGroupId;
    public String graphicId;
    
    public Hashtable attributes;
 
    public Item() {
    	attributes = new Hashtable();
    }
    
    public Item(Item type) {
    	if(type != null) {
	    	name = type.name;
	    	mass = type.mass;
	    	volume = type.volume;
	    	capacity = type.capacity;
	    	typeId = type.typeId;
	    	groupId = type.groupId;
	    	marketGroupId = type.marketGroupId;
	    	graphicId = type.graphicId;
	    	
	    	attributes = (Hashtable)type.attributes.clone();
    	} else {
    		attributes = new Hashtable();
    	}
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
    
    public boolean acceptsCharges() {
    	return attributes.containsKey("chargeGroup1");
    }
    
    /**
     * Retrieves an attribute value from the hash
     * 
     * @param attrib key to value
     * @param defaultVal if key dne, return this value
     * @return
     */
    public String getAttribute(String attrib, String defaultVal) {
    	if(attributes.containsKey(attrib)) {
    		return (String)attributes.get(attrib);
    	}
    	return defaultVal;
    }
    
    public Image getImage() {
    	if(icon == null) {
    		String filename = "";
    		try {
    			filename = "icon"+graphicId+".png";
				icon = Resources.getImage(filename);
			} catch (Exception e) {
	            System.out.println("Failed to load as image: " + filename);
	            try {
					icon = Resources.getImage("icon74_14.png");
				} catch (Exception e1) {
				}
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
