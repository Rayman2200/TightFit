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

package tightfit.ship;

import tightfit.item.Item;
import tightfit.module.Module;

/**
 * A ship is the nexus of TightFit. Everything is encompassed by this logical container.
 * 
 * @version $Id$
 */
public class Ship extends Item {

    private Module hiSlots[];
    private Module midSlots[];
    private Module lowSlots[];
    private Module rigSlots[];
    
    private int launcherHardpoints;
    private int turretHardpoints;
    
    private Item myType;
    
    public Ship(Item type) {
    	myType = type;             //OMG, you're just my type! What's your sign?
    	
    	lowSlots = new Module[Integer.getInteger((String)type.attributes.get("lowSlots")).intValue()];
    	midSlots = new Module[Integer.getInteger((String)type.attributes.get("midSlots")).intValue()];
    	hiSlots = new Module[Integer.getInteger((String)type.attributes.get("hiSlots")).intValue()];
    	rigSlots = new Module[Integer.getInteger((String)type.attributes.get("rigSlots")).intValue()];
    	
    	launcherHardpoints = Integer.getInteger((String)type.attributes.get("launcherHardpoints")).intValue();
    	turretHardpoints = Integer.getInteger((String)type.attributes.get("turretHardpoints")).intValue();
    }
    
    public float calculateDps() {
        
        return 0.0f;
    }
    
    public boolean putModule(Module m, int slotType, int slot) {
    	Module rack[];
    	
    	if(slotType == Module.LOW_SLOT) {
    		rack = lowSlots;
    	} else if(slotType == Module.MID_SLOT) {
    		rack = midSlots;
    	} else if(slotType == Module.HI_SLOT) {
     		rack = hiSlots;
     	} else {
     		rack = rigSlots;
     	}
    	
    	if(slot < rack.length && rack[slot] == null) {
    		rack[slot] = m;
    		return true;
    	}
    	
    	return false;
    }
    
    public void removeModule(int slotType, int slot) {
    	Module rack[];
    	
    	if(slotType == Module.LOW_SLOT) {
    		rack = lowSlots;
    	} else if(slotType == Module.MID_SLOT) {
    		rack = midSlots;
    	} else if(slotType == Module.HI_SLOT) {
     		rack = hiSlots;
     	} else {
     		rack = rigSlots;
     	}
    	
    	rack[slot] = null;
    }
    
    public float getMaxPower() {
    	return 0.0f;
    }
    
    public float getRemainingPower() {
    	return 0.0f;
    }
    
    public float calculateMaxCapacity() {
        return 0.0f;
    }
}
