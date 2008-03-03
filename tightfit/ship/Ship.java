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

	public String title;
	
    private Module hiSlots[];
    private Module midSlots[];
    private Module lowSlots[];
    private Module rigSlots[];
    
    private int launcherHardpoints;
    private int turretHardpoints;
    
    private float cpu, cpuMax;
    private float grid, gridMax;
    private float cap, capMax;
    private float shieldHp, armorHp;
    
    private float scanRes;
    private float sigRadius;
    private float maxRange;
    //private Item myType;
    
    public Ship() {
        super();
        rigSlots = new Module[0];
        hiSlots = new Module[0];
        midSlots = new Module[0];
        lowSlots = new Module[0];
        name = "Caldari Shuttle";
        cpu = cpuMax = 150;
        grid = gridMax = 25;
        sigRadius = 25;
        attributes.put("maxLockedTargets", "2.0");
        title = "Corporate Jet";
    }
    
    public Ship(Item type) {
    	super(type);
    	//myType = type;             //OMG, you're just my type! What's your sign?
    	type.printAttributes();
    	
    	lowSlots = new Module[(int)Float.parseFloat(type.getAttribute("lowSlots", "0"))];
    	midSlots = new Module[(int)Float.parseFloat(type.getAttribute("medSlots", "0"))];
    	hiSlots = new Module[(int)Float.parseFloat(type.getAttribute("hiSlots", "0"))];
    	rigSlots = new Module[(int)Float.parseFloat(type.getAttribute("rigSlots", "0"))];
    	
    	launcherHardpoints = (int)Float.parseFloat(type.getAttribute("launcherHardpoints", "0"));
    	turretHardpoints = (int)Float.parseFloat(type.getAttribute("turretSlotsLeft", "0"));
    	
    	cpu = cpuMax = Float.parseFloat(type.getAttribute("cpuOutput", "0"));
    	grid = gridMax = Float.parseFloat(type.getAttribute("powerOutput", "0"));
    	cap = capMax = Float.parseFloat(type.getAttribute("capacitorCapacity", "0"));
    	
    	scanRes = Float.parseFloat(type.getAttribute("scanResolution", "0"));
    	sigRadius = Float.parseFloat(type.getAttribute("signatureRadius", "0"));
    	maxRange = Float.parseFloat(type.getAttribute("maxTargetRange", "0"));
    	
    	shieldHp = Float.parseFloat(type.getAttribute("shieldCapacity", "0"));
    	armorHp = Float.parseFloat(type.getAttribute("armorHP", "0"));
    	
    	title = "Pilot's "+name;
    }

    public int countLauncherHardpoints() {
        return launcherHardpoints;  //FIXME
    }
    
    public int countTurretHardpoints() {
        return turretHardpoints;  //FIXME: actually check hi slots
    }
    
    public int countRigSlots() {
        return rigSlots.length; //FIXME
    }
    
    public int totalRigSlots() {
    	return rigSlots.length;
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
        	float multiplier = Float.parseFloat(((String)hiSlots[i].getAttribute("damageMultiplier", "1")));
        	float baseDmg = hiSlots[i].getCharge().getTotalDamage();
        	float rate = Float.parseFloat(((String)hiSlots[i].getAttribute("speed", "1")));
        	
        	dps += (baseDmg * multiplier) / (rate * calcFireRateBonus(hiSlots[i].getCharge()));
        }
        return dps;
    }
    
    /**
     * Calculates an aggregate of each of the damage types if all weapons are fired simultaniously
     *  
     * @return an array with the aggregate DPS for all damage types
     */
    public float[] calculateSpecificDps() {
    	float dps[] = new float[4];
    	dps[0] = dps[1] = dps[2] = dps[3] = 0.0f;
    	
    	for(int i=0;i<hiSlots.length;i++) {
    		float n[] = calculateSpecificDpsSlot(i);
        	
        	dps[0] += n[0];
        	dps[1] += n[1];
        	dps[2] += n[2];
        	dps[3] += n[3];
        }
    	
    	return dps;
    }
    
    public float [] calculateSpecificDpsSlot(int i) {
    	float dps[] = new float[4];
    	dps[0] = dps[1] = dps[2] = dps[3] = 0.0f;
    	
    	if(hiSlots[i] != null) {
	    	float multiplier = Float.parseFloat(((String)hiSlots[i].getAttribute("damageMultiplier", "1")));
	    	Ammo charge = hiSlots[i].getCharge();
	    	float rate = Float.parseFloat(((String)hiSlots[i].getAttribute("speed", "1")));
	    	
	    	dps[0] = (charge.getEmDamage() * multiplier) / (rate * calcFireRateBonus(charge));
	    	dps[1] = (charge.getKineticDamage() * multiplier) / (rate * calcFireRateBonus(charge));
	    	dps[2] = (charge.getThermalDamage() * multiplier) / (rate * calcFireRateBonus(charge));
	    	dps[3] = (charge.getExplosiveDamage() * multiplier) / (rate * calcFireRateBonus(charge));
    	}
    	
    	return dps;
    }
    
    public float calculateScanResolution() {
    	return multiplyAttributeProperty(scanRes, "scanResolutionMultiplier", true);
    }
    
    public float calculateRadius() {
    	return sigRadius + (aggregateAllSlots("signatureRadiusBonus", false)/100);
    }
    
    public float calculateRechargeRate() {
    	return multiplyAttributeProperty(Float.parseFloat(getAttribute("shieldRechargeRate", "0")), "shieldRechargeRateMultiplier", true)/1000.0f;
    }
    
    public int getMaxLockedTargets() {
    	return (int)Float.parseFloat(getAttribute("maxLockedTargets", "0"));
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
    
    public float aggregateRack(String prop, int slotType, boolean requiresOnline) {
    	Module rack[];
    	float a = 0f;
    	
    	if(slotType == Module.LOW_SLOT) {
    		rack = lowSlots;
    	} else if(slotType == Module.MID_SLOT) {
    		rack = midSlots;
    	} else if(slotType == Module.HI_SLOT) {
     		rack = hiSlots;
     	} else {
     		rack = rigSlots;
     	}
    	
    	for(int i=0;i<rack.length;i++) {
    		if(rack[i] != null  && (rack[i].isOnline() || !requiresOnline)) {
    			a +=  Float.parseFloat((String)rack[i].getAttribute(prop, "0"));
    		}
    	}
    	
    	return a;
    }
    
    public float aggregateAllSlots(String prop, boolean requiresOnline) {
    	float a = 0f;
    	
    	for(int i=0;i<hiSlots.length;i++) {
    		if(hiSlots[i] != null && (hiSlots[i].isOnline() || !requiresOnline)) {
    			a +=  Float.parseFloat((String)hiSlots[i].getAttribute(prop, "0"));
    		}
    	}
    	
    	for(int i=0;i<midSlots.length;i++) {
    		if(midSlots[i] != null && (midSlots[i].isOnline() || !requiresOnline)) {
    			a +=  Float.parseFloat((String)midSlots[i].getAttribute(prop, "0"));
    		}
    	}
    	
    	for(int i=0;i<lowSlots.length;i++) {
    		if(lowSlots[i] != null && (lowSlots[i].isOnline() || !requiresOnline)) {
    			a +=  Float.parseFloat((String)lowSlots[i].getAttribute(prop, "0"));
    		}
    	}
    	
    	for(int i=0;i<rigSlots.length;i++) {
    		if(rigSlots[i] != null) {
    			a +=  Float.parseFloat((String)rigSlots[i].getAttribute(prop, "0"));
    		}
    	}
    	
    	return a;
    }
    
    public float multiplyAttributeProperty(float att, String prop, boolean requiresOnline) {
    	
    	for(int i=0;i<hiSlots.length;i++) {
    		if(hiSlots[i] != null && (hiSlots[i].isOnline() || !requiresOnline)) {
    			att *=  Float.parseFloat((String)hiSlots[i].getAttribute(prop, "1"));
    		}
    	}
    	
    	for(int i=0;i<midSlots.length;i++) {
    		if(midSlots[i] != null && (midSlots[i].isOnline() || !requiresOnline)) {
    			att *=  Float.parseFloat((String)midSlots[i].getAttribute(prop, "1"));
    		}
    	}
    	
    	for(int i=0;i<lowSlots.length;i++) {
    		if(lowSlots[i] != null && (lowSlots[i].isOnline() || !requiresOnline)) {
    			att *=  Float.parseFloat((String)lowSlots[i].getAttribute(prop, "1"));
    		}
    	}
    	
    	for(int i=0;i<rigSlots.length;i++) {
    		if(rigSlots[i] != null) {
    			att *=  Float.parseFloat((String)rigSlots[i].getAttribute(prop, "1"));
    		}
    	}
    	
    	return att;
    }
    
    public float getMaxPower() {
    	float output = gridMax;
    	
    	for(int i=0;i<lowSlots.length;i++) {
    		if(lowSlots[i] != null && lowSlots[i].isOnline()) {
    			output *=  Float.parseFloat((String)lowSlots[i].getAttribute("powerOutputMultiplier", "1"));
    		}
    	}
    	
    	return output;
    }
    
    public float getMaxCpu() {
    	float c = cpuMax;
    	
    	for(int i=0;i<lowSlots.length;i++) {
    		if(lowSlots[i] != null && lowSlots[i].isOnline() && lowSlots[i].attributes.contains("cpuMultiplier")) {
    			c *=  Float.parseFloat((String)lowSlots[i].getAttribute("cpuMultiplier", "1"));
    		}
    	}
    	
    	return c;
    }
    
    /**
     * Returns the remaining power available after all active modules have had their share.
     * 
     * @return a float for remaining power (this value may be negative)
     */
    public float getRemainingPower() {
    	float gridRemain = getMaxPower();
    	
    	//HI
    	for(int i=0;i<hiSlots.length;i++) {
    		if(hiSlots[i] != null && hiSlots[i].isOnline()) {
    			gridRemain -= hiSlots[i].getPowerUsage();
    		}
    	}
    	
    	//MID
    	for(int i=0;i<midSlots.length;i++) {
    		if(midSlots[i] != null && midSlots[i].isOnline()) {
    			gridRemain -=  midSlots[i].getPowerUsage();
    		}
    	}
    	
    	//LOW
    	for(int i=0;i<lowSlots.length;i++) {
    		if(lowSlots[i] != null && lowSlots[i].isOnline()) {
    			gridRemain -=  lowSlots[i].getPowerUsage();
    		}
    	}
    	
    	return gridRemain;
    }
    
    public float getRemainingCpu() {
    	float cpuRemain = getMaxCpu();
    	
    	//HI
    	for(int i=0;i<hiSlots.length;i++) {
    		if(hiSlots[i] != null && hiSlots[i].isOnline()) {
    			cpuRemain -= hiSlots[i].getCpuUsage();
    		}
    	}
    	
    	//MID
    	for(int i=0;i<midSlots.length;i++) {
    		if(midSlots[i] != null && midSlots[i].isOnline()) {
    			cpuRemain -=  midSlots[i].getCpuUsage();
    		}
    	}
    	
    	//LOW
    	for(int i=0;i<lowSlots.length;i++) {
    		if(lowSlots[i] != null && lowSlots[i].isOnline()) {
    			cpuRemain -=  lowSlots[i].getCpuUsage();
    		}
    	}
    	
    	return cpuRemain;
    }
    
    public float calculateMaxCapacity() {
        return multiplyAttributeProperty(capMax, "capacitorCapacityMultiplier", true);
    }
    
    public float calculateCapacitorRechargeRate() {
        return multiplyAttributeProperty(Float.parseFloat(getAttribute("rechargeRate", "0")), "capacitorRechargeRateMultiplier", true) / 1000.0f;
    }
    
    public float calculateMaxShields() {
    	return multiplyAttributeProperty(shieldHp + aggregateAllSlots("capacityBonus", true), "shieldCapacityMultiplier", true);
    }
    
    public float calculateMaxArmor() {
    	return armorHp + aggregateAllSlots("armorHPBonusAdd", true);
    }
    
    public float calculateMaxRange() {
    	return maxRange;
    }
    
    private float calcFireRateBonus(Ammo charge) {
    	//TODO: also skills and tracking computers/stabs
    	return charge.getRateBonus();
    }
}
