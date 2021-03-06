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
import java.util.*;

import tightfit.Resources;

/**
 * An Item is a basic type from the Eve database. Everything extends from Item.
 *
 */
public class Item {
    protected Image icon;
    
    /** attributeNames */
    static public HashMap attributeNames;
    
    public String name;
    public int typeId;
    public int groupId;
    public int marketGroupId;
    public String graphicId;
    
    public HashMap<String,String> attributes;
 
    public Item() {
    	attributes = new HashMap<String,String>();
    	attributes.put("description", "");
    }
    
    public Item(Item type) {
    	if(type != null) {
	    	name = type.name;
	    	typeId = type.typeId;
	    	groupId = type.groupId;
	    	marketGroupId = type.marketGroupId;
	    	graphicId = type.graphicId;
	    	
	    	attributes = (HashMap)type.attributes.clone();
    	} else {
    		attributes = new HashMap<String,String>();
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
    
    /**
     * Does this item need to be activated to be used?
     * 
     * @see Item#isWeapon()
     * @return <code>true</code> if it uses cap or is a weapon, <code>false</code> otherwise
     */
    public boolean requiresActivation() {
    	return attributes.containsKey("capacitorNeed") || attributes.containsKey("consumptionQuantity") || isWeapon();
    }
    
    public boolean acceptsCharges() {
    	for(int i=1;i<4;i++) {
    		if(attributes.containsKey("chargeGroup"+i)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Check to see if this item accepts the given item as a charge.
     * 
     * @param an {@link Item}, preferably a charge of some kind (ammo, script, etc.) 
     * @return <code>true</code> if the given item is an acceptable charge, <code>false</code> otherwise.
     */
    public boolean accepts(Item a) {

    	for(int i=1;i<4;i++) {
    		if(attributes.containsKey("chargeGroup"+i)) {
    			if(a.groupId == Integer.parseInt((String)attributes.get("chargeGroup"+i))) {
    				if(a.attributes.containsKey("chargeSize")) {
    					if(attributes.get("chargeSize").equals(a.attributes.get("chargeSize"))) {
	    					return true;
	    				}
    				} else {
    					if(a.attributes.get("launcherGroup").equals(""+groupId) || 
                                a.attributes.get("launcherGroup2").equals(""+groupId)) {
    						return true;
    					}
    				}
    			}
    		}
    	}
    	return false;
    }
    
    /**
     * Is this item "ammo"? More appropriately, is it a charge of some kind
     * that can be loaded into a module?
     * 
     * @return
     */
    public boolean isAmmo() {
        Iterator keys = attributes.keySet().iterator();
        while(keys.hasNext()) {
            if(((String)keys.next()).startsWith("script"))
                return true;
        }
        
    	return attributes.containsKey("ammoInfluenceCapNeed") 
                || attributes.containsKey("missileLaunching")
                || attributes.containsKey("bombLaunching")
                || attributes.containsKey("torpedoLaunching")
                || attributes.containsKey("launcherGroup");
    }
    
    public boolean isShip() {
    	return attributes.containsKey("lowSlots");
    }
    
    /**
     * Is this item a weapon? If it is a weapon, either it uses charges, or it uses missiles.
     * 
     * @return <code>true</code> if it uses charges or missiles, <code>false</code> otherwise.
     */
    public boolean isWeapon() {
    	return attributes.containsKey("chargeSize") || attributes.containsKey("useMissiles"); //FIXME: be more precise
    }
    
    public boolean isDrone() {
        return attributes.containsKey("droneBandwidthUsed");
    }
    
    public boolean isFittable() {
    	return attributes.containsKey("power") || attributes.containsKey("rigSlot");
    }
    
    /**
     * Retrieves an attribute value from the hash
     * 
     * @param attrib key to value
     * @param defaultVal if key DNE, return this value
     * @return the value of the attribute, or the defaultVal if the attribute does not exist
     */
    public String getAttribute(String attrib, String defaultVal) {
    	if(attributes.containsKey(attrib)) {
    		return (String)attributes.get(attrib);
    	}
    	return defaultVal;
    }
    
    /**
     * 
     * 
     * @param 
     */
    public String [] getAttributeKey(String incompleteKey) throws Exception {
    	Iterator itr = attributes.keySet().iterator();
    	Vector<String> v = new Vector<String>();
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
    
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
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
    
    public String toString() {
        return name;
    }
    
    public boolean equals(Object b) {
    	if(b instanceof Item) {
    		return ((Item)b).typeId == typeId;
    	}
    	return false;
    }
}
