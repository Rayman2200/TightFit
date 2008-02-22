/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.ship;

import tightfit.item.Ammo;
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
    
    private float cpu;
    private float cpuMax;
    private float grid;
    private float gridMax;
    
    private Item myType;
    
    public Ship(Item type) {
    	myType = type;             //OMG, you're just my type! What's your sign?
    	
    	lowSlots = new Module[(int)Float.parseFloat((String)type.attributes.get("lowSlots"))];
    	midSlots = new Module[(int)Float.parseFloat((String)type.attributes.get("midSlots"))];
    	hiSlots = new Module[(int)Float.parseFloat((String)type.attributes.get("hiSlots"))];
    	rigSlots = new Module[(int)Float.parseFloat((String)type.attributes.get("rigSlots"))];
    	
    	launcherHardpoints = (int)Float.parseFloat((String)type.attributes.get("launcherHardpoints"));
    	turretHardpoints = (int)Float.parseFloat((String)type.attributes.get("turretHardpoints"));
    	
    	cpu = cpuMax = Float.parseFloat((String)type.attributes.get("cpu"));
    	grid = gridMax = Float.parseFloat((String)type.attributes.get("powerOutput"));
    }
    
    /**
     * Calculates the generic DPS of the ship, does not account for ranges or damage types.
     * This number will almost always be higher than actual DPS.
     * 
     * @return the generic DPS
     */
    public float calculateGenericDps() {
    	float dps = 0.0f;
        for(int i=0;i<hiSlots.length;i++) {
        	float multiplier = Float.parseFloat(((String)hiSlots[i].attributes.get("damageMultiplier")));
        	float baseDmg = hiSlots[i].getCharge().getTotalDamage();
        	float rate = Float.parseFloat(((String)hiSlots[i].attributes.get("speed")));
        	
        	dps += (baseDmg * multiplier) / (rate * calcFireRateBonus(hiSlots[i].getCharge()));
        }
        return dps;
    }
    
    public float[] calculateSpecificDps() {
    	float dps[] = new float[4];
    	dps[0] = dps[1] = dps[2] = dps[3] = 0.0f;
    	
    	for(int i=0;i<hiSlots.length;i++) {
        	float multiplier = Float.parseFloat(((String)hiSlots[i].attributes.get("damageMultiplier")));
        	Ammo charge = hiSlots[i].getCharge();
        	float rate = Float.parseFloat(((String)hiSlots[i].attributes.get("speed")));
        	
        	dps[0] += (charge.getEmDamage() * multiplier) / (rate * calcFireRateBonus(charge));
        	dps[1] += (charge.getKineticDamage() * multiplier) / (rate * calcFireRateBonus(charge));
        	dps[2] += (charge.getThermalDamage() * multiplier) / (rate * calcFireRateBonus(charge));
        	dps[3] += (charge.getExplosiveDamage() * multiplier) / (rate * calcFireRateBonus(charge));
        }
    	
    	return dps;
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
    		cpu -= m.getCpuUsage();
    		grid -= m.getPowerUsage();
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
    	
    	
    	if(slot <= rack.length) {
	    	Module m = rack[slot];
	    	if(m != null) {
		    	cpu += m.getCpuUsage();
				grid += m.getPowerUsage();
	    	}
	    	rack[slot] = null;
    	}
    }
    
    public float getMaxPower() {
    	float output = Float.parseFloat((String)attributes.get("powerOutput"));
    	
    	for(int i=0;i<lowSlots.length;i++) {
    		//TODO: grab power bonuses
    	}
    	
    	return output;
    }
    
    public float getRemainingPower() {
    	return 0.0f;
    }
    
    public float calculateMaxCapacity() {
        return 0.0f;
    }
    
    private float calcFireRateBonus(Ammo charge) {
    	return charge.getRateBonus();
    }
}
