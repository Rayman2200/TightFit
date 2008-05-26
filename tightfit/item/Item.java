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
import java.util.Vector;

import tightfit.Resources;

public class Item {
    protected Image icon;
    
    static public HashMap attributeNames;
    
    public String name;
    public int typeId;
    public int groupId;
    public int marketGroupId;
    public String graphicId;
    
    public Hashtable attributes;
 
    public Item() {
    	attributes = new Hashtable();
    	attributes.put("description", "");
    }
    
    public Item(Item type) {
    	if(type != null) {
	    	name = type.name;
	    	typeId = type.typeId;
	    	groupId = type.groupId;
	    	marketGroupId = type.marketGroupId;
	    	graphicId = type.graphicId;
	    	
	    	attributes = (Hashtable)type.attributes.clone();
    	} else {
    		attributes = new Hashtable();
    		attributes.put("description", "");
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
    	return attributes.containsKey("capacitorNeed") || isWeapon();
    }
    
    public boolean acceptsCharges() {
    	for(int i=1;i<4;i++) {
    		if(attributes.containsKey("chargeGroup"+i)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean accepts(Item a) {
    	
    	for(int i=1;i<4;i++) {
    		if(attributes.containsKey("chargeGroup"+i)) {
    			if(a.groupId == Integer.parseInt((String)attributes.get("chargeGroup"+i))) {
    				if(a.attributes.containsKey("missileLaunching")) {
    					if(a.attributes.get("launcherGroup").equals(""+groupId)) {
    						return true;
    					}
    				} else {
	    				if(attributes.get("chargeSize").equals(a.attributes.get("chargeSize"))) {
	    					return true;
	    				}
    				}
    			}
    		}
    	}
    	return false;
    }
    
    public boolean isAmmo() {
    	return attributes.containsKey("ammoInfluenceCapNeed") || attributes.containsKey("missileLaunching");
    }
    
    public boolean isShip() {
    	return attributes.containsKey("lowSlots");
    }
    
    public boolean isWeapon() {
    	return attributes.containsKey("chargeSize") || attributes.containsKey("useMissiles"); //FIXME: be more precise
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
    
    public String [] getAttributeKey(String incompleteKey) throws Exception {
    	Iterator itr = attributes.keySet().iterator();
    	Vector v = new Vector();
    	while(itr.hasNext()) {
    		String key = (String) itr.next();
    		if(key.contains(incompleteKey)) {
    			v.add(key);
    		}
    	}
    	if(v.size() > 0)
    		return (String[])v.toArray(new String[8]);
    	else throw new Exception("no key containing '"+incompleteKey+"' exists");
    }
    
    public Image getImage() {
    	if(icon == null) {
    		String filename = "";
    		try {
    			if(isShip()) {
    				filename = ""+typeId+".png";
    			} else {
    				filename = "icon"+graphicId+".png";
    			}
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
    
    public String printAttributes() {
    	Iterator keys = attributes.keySet().iterator();
    	String n = "";
    	while(keys.hasNext()) {
    		Object key = keys.next();
    		n += ""+key+": "+attributes.get(key) + "\n";
    	}
    	
    	return n;
    }
}
