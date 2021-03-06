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

import java.util.*;

import tightfit.item.Item;
import tightfit.module.Module;
import tightfit.module.Weapon;
import tightfit.character.Character;
import tightfit.character.CharacterChangeListener;
import tightfit.util.ModuleMap;

/**
 * A ship is the nexus of TightFit. Everything is encompassed by this logical container.
 * 
 * @version $Id$
 */
public class Ship extends Item implements CharacterChangeListener {

	public String title;
	public Character pilot = new Character();
	
    private Module hiSlots[];
    private Module midSlots[];
    private Module lowSlots[];
    private Module rigSlots[];
    
    private int launcherHardpoints;
    private int turretHardpoints;
    
    private float cpu, cpuMax;
    private float grid, gridMax;
    private float cap, capMax;
    private float shieldHp, armorHp, hp;
    
    private float scanRes;
    private float sigRadius;
    private float maxRange;
    private float maxSpeed;
    //private Item myType;
    
    private float [] shieldReson;
    private float [] armorReson;
    private float [] structReson;
    
    private LinkedList listenerList = new LinkedList();
    private LinkedList cargo = new LinkedList();
    
    private boolean bChanged;
    
    public Ship() {
        super();
        rigSlots = new Module[0];
        hiSlots = new Module[0];
        midSlots = new Module[0];
        lowSlots = new Module[0];
        name = "GooPod";
        cpu = cpuMax = 1;
        grid = gridMax = 1;
        sigRadius = 25;
        attributes.put("maxLockedTargets", "0.0");
        attributes.put("capacity", "0.0");
        title = "Pod";
        maxSpeed = 400;
        shieldReson = new float[4];
    	shieldReson[0] = 1;
    	shieldReson[1] = 1;
    	shieldReson[2] = 1;
    	shieldReson[3] = 1;
    	
    	armorReson = new float[4];
    	armorReson[0] = 1;
    	armorReson[1] = 1;
    	armorReson[2] = 1;
    	armorReson[3] = 1;
    	
    	structReson = new float[4];
    	structReson[0] = 1;
    	structReson[1] = 1;
    	structReson[2] = 1;
    	structReson[3] = 1;
    	
    	bChanged = false;
    }
    
    public Ship(Item type) {
    	super(type);
    	//System.out.println(type.printAttributes());
    	
    	lowSlots = new Module[(int)Float.parseFloat(type.getAttribute("lowSlots", "0"))];
    	midSlots = new Module[(int)Float.parseFloat(type.getAttribute("medSlots", "0"))];
    	hiSlots = new Module[(int)Float.parseFloat(type.getAttribute("hiSlots", "0"))];
    	rigSlots = new Module[(int)Float.parseFloat(type.getAttribute("rigSlots", "0"))];
    	
    	launcherHardpoints = (int)Float.parseFloat(type.getAttribute("launcherSlotsLeft", "0"));
    	turretHardpoints = (int)Float.parseFloat(type.getAttribute("turretSlotsLeft", "0"));
    	
    	cpu = cpuMax = Float.parseFloat(type.getAttribute("cpuOutput", "0"));
    	grid = gridMax = Float.parseFloat(type.getAttribute("powerOutput", "0"));
    	cap = capMax = Float.parseFloat(type.getAttribute("capacitorCapacity", "0"));
    	
    	scanRes = Float.parseFloat(type.getAttribute("scanResolution", "0"));
    	sigRadius = Float.parseFloat(type.getAttribute("signatureRadius", "0"));
    	maxRange = Float.parseFloat(type.getAttribute("maxTargetRange", "0"));
    	maxSpeed = Float.parseFloat(type.getAttribute("maxVelocity", "0"));
    	
    	shieldHp = Float.parseFloat(type.getAttribute("shieldCapacity", "0"));
    	armorHp = Float.parseFloat(type.getAttribute("armorHP", "0"));
    	hp = Float.parseFloat(type.getAttribute("hp", "0"));
    	
    	shieldReson = new float[4];
    	shieldReson[0] = Float.parseFloat(getAttribute("shieldEmDamageResonance", "1.0"));
    	shieldReson[1] = Float.parseFloat(getAttribute("shieldKineticDamageResonance", "1.0"));
    	shieldReson[2] = Float.parseFloat(getAttribute("shieldThermalDamageResonance", "1.0"));
    	shieldReson[3] = Float.parseFloat(getAttribute("shieldExplosiveDamageResonance", "1.0"));
    	
    	armorReson = new float[4];
    	armorReson[0] = Float.parseFloat(getAttribute("armorEmDamageResonance", "1.0"));
    	armorReson[1] = Float.parseFloat(getAttribute("armorKineticDamageResonance", "1.0"));
    	armorReson[2] = Float.parseFloat(getAttribute("armorThermalDamageResonance", "1.0"));
    	armorReson[3] = Float.parseFloat(getAttribute("armorExplosiveDamageResonance", "1.0"));
    	
    	structReson = new float[4];
    	structReson[0] = Float.parseFloat(getAttribute("emDamageResonance", "1.0"));
    	structReson[1] = Float.parseFloat(getAttribute("kineticDamageResonance", "1.0"));
    	structReson[2] = Float.parseFloat(getAttribute("thermalDamageResonance", "1.0"));
    	structReson[3] = Float.parseFloat(getAttribute("explosiveDamageResonance", "1.0"));
    	
    	title = name;
    	bChanged = false;
    }

    public boolean isChanged() {
    	return bChanged;
    }
    
    public int countFreeLauncherHardpoints() {
        int count = launcherHardpoints;
        if(count > 0) {
	    	for(int i=0;i<hiSlots.length;i++) {
	    		if(hiSlots[i] != null) {
	    			if(hiSlots[i].getAttribute("launcherFitted", "0").equals("1"))
	    				count--;
	    		}
	    	}
        }
        return count;
    }
    
    public int countFreeTurretHardpoints() {
    	int count = turretHardpoints;
    	if(count > 0) {
	    	for(int i=0;i<hiSlots.length;i++) {
	    		if(hiSlots[i] != null) {
	    			if(hiSlots[i].getAttribute("turretFitted", "0").equals("1"))
	    				count--;
	    		}
	    	}
    	}
        return count;
    }
    
    public int countRigSlots() {
        return rigSlots.length; //FIXME
    }
    
    public int totalSlots(int slotType) {
    	if(slotType == Module.LOW_SLOT) {
    		return lowSlots.length;
    	} else if(slotType == Module.MID_SLOT) {
    		return midSlots.length;
    	} else if(slotType == Module.HI_SLOT) {
     		return hiSlots.length;
     	} else {
     		return rigSlots.length;
     	}
        
    }
    
    public boolean hasFreeSlot(Module m) {
    	Module rack[];
    	int slotType = m.slotRequirement;
        
        if(m.getAttribute("launcherFitted", "0").equals("1") && countFreeLauncherHardpoints() == 0)
    		return false;
    	else if(m.getAttribute("turretFitted", "0").equals("1") && countFreeTurretHardpoints() == 0)
    		return false;
        
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
    		if(rack[i] == null)
    			return true;
    	}
    	
    	return false;
    }
    
    
    
    public int getMaxLockedTargets() {
    	return (int)Float.parseFloat(getAttribute("maxLockedTargets", "0"));
    }
    
    public void strip() {
    	for(int i=0;i<4;i++)
    		for(int j=0;j<8;j++)
    			removeModule(i, j);
    }
    
    public void addToCargo(Module m) {
    	cargo.add(m);
    }
    
    public boolean testPutModule(Module m, int slotType, int slot) {
        
        if(slotType != m.slotRequirement)
    		return false;
    	
    	if(m.getAttribute("launcherFitted", "0").equals("1") && countFreeLauncherHardpoints() == 0)
    		return false;
    	else if(m.getAttribute("turretFitted", "0").equals("1") && countFreeTurretHardpoints() == 0)
    		return false;
    	else if(!m.getAttribute("upgradeCost", "-1").equals("-1") && 
    					Float.parseFloat(m.getAttribute("upgradeCost", "0")) > getRemainingUpgradeCapacity())
    		return false;
    	
    	return !hasModule(slotType, slot);
    }
    
    public boolean putModule(Module m, int slotType, int slot) {
    	Module rack[];
    	
    	if(!testPutModule(m, slotType, slot))
    		return false;
    	
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
    		m.setShip(this);
    		cpu -= m.getCpuUsage();
    		grid -= m.getPowerUsage();
    		fireShipChange();
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
    	
    	
    	if(slot < rack.length) {
	    	Module m = rack[slot];
	    	if(m != null) {
		    	cpu += m.getCpuUsage();
				grid += m.getPowerUsage();
	    	}
	    	rack[slot] = null;
	    	fireShipChange();
    	}
    }
    
    public boolean hasModule(int slotType, int slot) {
    	return getModule(slotType, slot) != null;
    }
    
    public Module getModule(int slotType, int slot) {
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
    	
    	
    	if(slot < rack.length)
	    	return rack[slot];
	    return null;
    }
    
    public Module[] findModule(String name, int slotType) {
    	Vector v = new Vector();
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
    	
    	for(int i=0;i<rack.length;i++) {
    		if(rack[i] != null) {
    			if(rack[i].name.contains(name))
    				v.add(rack[i]);
    		}
    	}
    	
    	return (Module[])v.toArray();
    }
    
    public Module[] findModuleByAttribute(String name) {
    	Vector v = new Vector();
    	Module rack[];
    	
        for(int slotType=0; slotType<4;slotType++) {
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
                if(rack[i] != null) {
                    try{
                        if(rack[i].getAttributeKey(name) != null)
                            v.add(rack[i]);
                    }catch(Exception e) {}
                }
            }
    	}
    	if(v.size() > 0) {
    		return (Module[])v.toArray(new Module[v.size()]);
    	}
    	return null;
    }
    
    public Iterator findModuleByAttributeAndSort(String name, boolean descending) {
    	ModuleMap map = new ModuleMap(descending);
    	Module rack[];
    	
        for(int slotType=0; slotType<4;slotType++) {
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
                if(rack[i] != null) {
                    try{
                        if(rack[i].hasAttribute(name)) {
                            //System.out.println("found "+rack[i].name);
                            map.put(new Float(Float.parseFloat(rack[i].getAttribute(name, "0"))), rack[i]);
                        }
                    }catch(Exception e) {}
                }
            }
    	}
        
        return map.values().iterator();
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
    		if(rack[i] != null  && (rack[i].isReady() || !requiresOnline)) {
    			a +=  rack[i].getModifiedAttribute(prop, null, "0");
    		}
    	}
    	
    	return a;
    }
    
    public float aggregateAllSlots(String prop, String hasProp, boolean requiresOnline, boolean penalizeStacking) {
    	float a = 0f;
    	float mod;
    	
    	mod=1;
    	for(int i=0;i<hiSlots.length;i++) {
    		if(hiSlots[i] != null && (hiSlots[i].isReady() || !requiresOnline) && (hasProp.equals("*") || hiSlots[i].attributes.containsKey(hasProp))) {
    			a +=  hiSlots[i].getModifiedAttribute(prop, null, "0")*mod;
    			mod = penalizeStacking ? mod - 0.33f : mod;
    		}
    	}
    	
    	mod=1;
    	for(int i=0;i<midSlots.length;i++) {
    		if(midSlots[i] != null && (midSlots[i].isReady() || !requiresOnline) && (hasProp.equals("*") || midSlots[i].attributes.containsKey(hasProp))) {
    			a +=  midSlots[i].getModifiedAttribute(prop, null, "0")*mod;
    			mod = penalizeStacking ? mod - 0.33f : mod;
    		}
    	}
    	
    	mod=1;
    	for(int i=0;i<lowSlots.length;i++) {
    		if(lowSlots[i] != null && (lowSlots[i].isReady() || !requiresOnline) && (hasProp.equals("*") || lowSlots[i].attributes.containsKey(hasProp))) {
    			a +=  lowSlots[i].getModifiedAttribute(prop, null, "0")*mod;
    			mod = penalizeStacking ? mod - 0.33f : mod;
    		}
    	}
    	
    	mod=1;
    	for(int i=0;i<rigSlots.length;i++) {
    		if(rigSlots[i] != null && (hasProp.equals("*") || rigSlots[i].attributes.containsKey(hasProp))) {
    			a +=  rigSlots[i].getModifiedAttribute(prop, null, "0")*mod;
    			mod = penalizeStacking ? mod - 0.33f : mod;
    		}
    	}
    	
    	return a;
    }
    
    public float multiplyAllSlots(float a, String prop, String hasProp, boolean requiresOnline) {
    	
    	for(int i=0;i<hiSlots.length;i++) {
    		if(hiSlots[i] != null && (hiSlots[i].isReady() || !requiresOnline) && (hasProp.equals("*") || hiSlots[i].attributes.containsKey(hasProp))) {
    			a *=  hiSlots[i].getModifiedAttribute(prop, null, "0");
    		}
    	}
    	
    	for(int i=0;i<midSlots.length;i++) {
    		if(midSlots[i] != null && (midSlots[i].isReady() || !requiresOnline) && (hasProp.equals("*") || midSlots[i].attributes.containsKey(hasProp))) {
    			a *=  midSlots[i].getModifiedAttribute(prop, null, "0");
    		}
    	}
    	
    	for(int i=0;i<lowSlots.length;i++) {
    		if(lowSlots[i] != null && (lowSlots[i].isReady() || !requiresOnline) && (hasProp.equals("*") || lowSlots[i].attributes.containsKey(hasProp))) {
    			a *=  lowSlots[i].getModifiedAttribute(prop, null, "0");
    		}
    	}
    	
    	for(int i=0;i<rigSlots.length;i++) {
    		if(rigSlots[i] != null && (hasProp.equals("*") || rigSlots[i].attributes.containsKey(hasProp))) {
    			a *=  rigSlots[i].getModifiedAttribute(prop, null, "0");
    		}
    	}
    	
    	return a;
    }
    
    public float multiplyAttributeProperty(float att, String prop, boolean requiresOnline) {
    	
    	for(int i=0;i<hiSlots.length;i++) {
    		if(hiSlots[i] != null && (hiSlots[i].isReady() || !requiresOnline)) {
    			att *=  Float.parseFloat((String)hiSlots[i].getAttribute(prop, "1"));
    		}
    	}
    	
    	for(int i=0;i<midSlots.length;i++) {
    		if(midSlots[i] != null && (midSlots[i].isReady() || !requiresOnline)) {
    			att *=  Float.parseFloat((String)midSlots[i].getAttribute(prop, "1"));
    		}
    	}
    	
    	for(int i=0;i<lowSlots.length;i++) {
    		if(lowSlots[i] != null && (lowSlots[i].isReady() || !requiresOnline)) {
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
    
    public float multiplyAttributeBonus(float att, String prop, boolean requiresOnline) {
    	for(int i=0;i<hiSlots.length;i++) {
    		if(hiSlots[i] != null && (hiSlots[i].isReady() || !requiresOnline)) {
    			att *=  1+(hiSlots[i].getModifiedAttribute(prop, null, "0")/100.0f);
    		}
    	}
    	
    	for(int i=0;i<midSlots.length;i++) {
    		if(midSlots[i] != null && (midSlots[i].isReady() || !requiresOnline)) {
    			att *=  1+(midSlots[i].getModifiedAttribute(prop, null, "0")/100.0f);
    		}
    	}
    	
    	for(int i=0;i<lowSlots.length;i++) {
    		if(lowSlots[i] != null && (lowSlots[i].isReady() || !requiresOnline)) {
    			att *=  1+(lowSlots[i].getModifiedAttribute(prop, null, "0")/100.0f);
    		}
    	}
    	
    	for(int i=0;i<rigSlots.length;i++) {
    		if(rigSlots[i] != null) {
    			att *=  1+(rigSlots[i].getModifiedAttribute(prop, null, "0")/100.0f);
    		}
    	}
    	return att;
    }
    
    public float getMaxPower() {
    	float output = gridMax*pilot.getSkillBonus("3413");
    	
    	output = multiplyAttributeBonus(output, "powerEngineeringOutputBonus", true);
    	
    	return multiplyAttributeProperty(output, "powerOutputMultiplier", true);
    }
    
    public float getMaxCpu() {
    	float c = cpuMax*pilot.getSkillBonus("3426");
    	
    	return multiplyAttributeProperty(c, "cpuMultiplier", true);
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
    
    public int getRemainingUpgradeCapacity() {
    	int max = (int)Float.parseFloat(getAttribute("upgradeCapacity", "0"));
    	
    	for(int i=0;i<rigSlots.length;i++) {
    		if(rigSlots[i] != null) {
    			max -=  Float.parseFloat(rigSlots[i].getAttribute("upgradeCost", "0"));
    		}
    	}
    	
    	return max;
    }
    
    public float [] getShieldResonance() {
    	float [] reson = new float[4];
        float mod=1;
        //EM
       
    	reson[0] = (shieldReson[0])											//base resist
                    * (1 - Float.parseFloat(getAttribute("cantfindit", "0")));    //ship bonus
        
        Iterator itr = findModuleByAttributeAndSort("emDamageResistanceBonus", true);
        while(itr.hasNext()) {
            Module list = (Module)itr.next();
            //System.out.println("now: "+mod);
            if(list.isReady() && (list.hasAttribute("modifyActiveShieldResonanceAndNullifyPassiveResonance") ||list.hasAttribute("modifyShieldResonancePostPercent")))
                reson[0] *= (1+Float.parseFloat((String)list.getAttribute("emDamageResistanceBonus", "0"))/100.0f*mod);
            else if(!list.isReady() && list.hasAttribute("modifyActiveShieldResonanceAndNullifyPassiveResonance"))
                reson[0] *= (1+Float.parseFloat((String)list.getAttribute("passiveEmDamageResistanceBonus", "0"))/100.0f*mod);
            
            mod*=0.655f;
        }
        
        mod=1;
        itr = findModuleByAttributeAndSort("shieldEmDamageResonance", true);
        while(itr.hasNext()) {
            Module list = (Module)itr.next();
            if(list.isReady())
                reson[0] -= reson[0]*(1-checkResonance(Float.parseFloat((String)list.getAttribute("shieldEmDamageResonance", "0")))*mod);
            
            mod*=0.655f;
        }
        
        //reson[0] = (float) 1-(reson[0]-1);
        
    	//KINETIC
        reson[1] = (shieldReson[1])                                   //base resist
		        	* (1 - Float.parseFloat(getAttribute("cantfindit", "0")));    //ship bonus
		
        mod=1;
		itr = findModuleByAttributeAndSort("kineticDamageResistanceBonus", true);
		while(itr.hasNext()) {
			Module list = (Module)itr.next();
			if(list.isReady() && (list.hasAttribute("modifyActiveShieldResonanceAndNullifyPassiveResonance") || list.hasAttribute("modifyShieldResonancePostPercent")))
			    reson[1] *= (1+Float.parseFloat((String)list.getAttribute("kineticDamageResistanceBonus", "0"))/100.0f*mod);
			else if(!list.isReady() && list.hasAttribute("modifyActiveShieldResonanceAndNullifyPassiveResonance")) {
			    reson[1] *= (1+Float.parseFloat((String)list.getAttribute("passiveKineticDamageResistanceBonus", "0"))/100.0f*mod);
			}
			mod*=0.655f;
		}
		
		mod=1;
		itr = findModuleByAttributeAndSort("shieldKineticDamageResonance", true);
		while(itr.hasNext()) {
			Module list = (Module)itr.next();
			if(list.isReady())
			    reson[1] -= reson[1]*(1-checkResonance(Float.parseFloat((String)list.getAttribute("shieldKineticDamageResonance", "0")))*mod);
			
			mod*=0.655f;
		}
        
        //reson[1] = (float) 1-(reson[1]);
        
    	//THERMAL
        reson[2] = (shieldReson[2])                                   //base resist
    					* (1 - Float.parseFloat(getAttribute("cantfindit", "0")));    //ship bonus

		mod=1;
		itr = findModuleByAttributeAndSort("thermalDamageResistanceBonus", true);
		while(itr.hasNext()) {
			Module list = (Module)itr.next();
			if(list.isReady() && (list.hasAttribute("modifyActiveShieldResonanceAndNullifyPassiveResonance") || list.hasAttribute("modifyShieldResonancePostPercent")))
			    reson[2] *= (1+Float.parseFloat((String)list.getAttribute("thermalDamageResistanceBonus", "0"))/100.0f*mod);
			else if(!list.isReady() && list.hasAttribute("modifyActiveShieldResonanceAndNullifyPassiveResonance")) {
			    reson[2] *= (1+Float.parseFloat((String)list.getAttribute("passiveThermalDamageResistanceBonus", "0"))/100.0f*mod);
			}
			mod*=0.655f;
		}
		
		mod=1;
		itr = findModuleByAttributeAndSort("shieldThermalDamageResonance", true);
		while(itr.hasNext()) {
			Module list = (Module)itr.next();
			if(list.isReady())
			    reson[2] -= reson[2]*(1-checkResonance(Float.parseFloat((String)list.getAttribute("shieldThermalDamageResonance", "0")))*mod);
			
			mod*=0.655f;
		}
		
		//reson[2] = (float) 1-(reson[2]);
    	
        //EXPLOSIVE
        reson[3] = (shieldReson[3])                                   //base resist
    					* (1 - Float.parseFloat(getAttribute("cantfindit", "0")));    //ship bonus

		mod=1;
		itr = findModuleByAttributeAndSort("explosiveDamageResistanceBonus", true);
		while(itr.hasNext()) {
			Module list = (Module)itr.next();
			if(list.isReady() && (list.hasAttribute("modifyActiveShieldResonanceAndNullifyPassiveResonance") || list.hasAttribute("modifyShieldResonancePostPercent")))
			    reson[3] *= (1+Float.parseFloat((String)list.getAttribute("explosiveDamageResistanceBonus", "0"))/100.0f*mod);
			else if(!list.isReady() && list.hasAttribute("modifyActiveShieldResonanceAndNullifyPassiveResonance")) {
			    reson[3] *= (1+Float.parseFloat((String)list.getAttribute("passiveExplosiveDamageResistanceBonus", "0"))/100.0f*mod);
			}
			mod*=0.655f;
		}
		
		mod=1;
		itr = findModuleByAttributeAndSort("shieldExplosiveDamageResonance", true);
		while(itr.hasNext()) {
			Module list = (Module)itr.next();
			if(list.isReady())
			    reson[3] -= reson[3]*(1-checkResonance(Float.parseFloat((String)list.getAttribute("shieldExplosiveDamageResonance", "0")))*mod);
			
			mod*=0.655f;
		}
		
		//reson[3] = (float) 1-(reson[3]);
        
    	return reson;
    }
    
    public float [] getArmorResonance() {
    	float [] reson = new float[4];
    	float mod;
    	
    	// EM DAMAGE
    	reson[0] = armorReson[0] 
    	                      * (1 - Float.parseFloat(getAttribute("cantfindit", "0")));    //ship bonus
    	
    	mod=1;
    	Iterator itr = findModuleByAttributeAndSort("emDamageResistanceBonus", true);
        while(itr.hasNext()) {
            Module list = (Module)itr.next();
            //System.out.println("now: "+mod);
            if(list.isReady() && (list.hasAttribute("modifyActiveArmorResonanceAndNullifyPassiveResonance") ||list.hasAttribute("modifyArmorResonancePostPercent")))
                reson[0] *= (1+Float.parseFloat((String)list.getAttribute("emDamageResistanceBonus", "0"))/100.0f*mod);
            else if(!list.isReady() && list.hasAttribute("modifyActiveArmorResonanceAndNullifyPassiveResonance"))
                reson[0] *= (1+Float.parseFloat((String)list.getAttribute("passiveEmDamageResistanceBonus", "0"))/100.0f*mod);
            
            mod*=0.655f;
        }
        
        mod=1;
        itr = findModuleByAttributeAndSort("armorEmDamageResonance", true);
        while(itr.hasNext()) {
            Module list = (Module)itr.next();
            if(list.isReady())
                reson[0] -= reson[0]*(1-checkResonance(Float.parseFloat((String)list.getAttribute("armorEmDamageResonance", "0")))*mod);
            
            mod*=0.655f;
        }
    	
        //KINETIC DAMAGE
        reson[1] = armorReson[1] 
    	                      * (1 - Float.parseFloat(getAttribute("cantfindit", "0")));    //ship bonus
    	
    	mod=1;
    	itr = findModuleByAttributeAndSort("kineticDamageResistanceBonus", true);
        while(itr.hasNext()) {
            Module list = (Module)itr.next();
            //System.out.println("now: "+mod);
            if(list.isReady() && (list.hasAttribute("modifyActiveArmorResonanceAndNullifyPassiveResonance") ||list.hasAttribute("modifyArmorResonancePostPercent")))
                reson[1] *= (1+Float.parseFloat((String)list.getAttribute("kineticDamageResistanceBonus", "0"))/100.0f*mod);
            else if(!list.isReady() && list.hasAttribute("modifyActiveArmorResonanceAndNullifyPassiveResonance"))
                reson[1] *= (1+Float.parseFloat((String)list.getAttribute("passiveKineticDamageResistanceBonus", "0"))/100.0f*mod);
            
            mod*=0.655f;
        }
        
        mod=1;
        itr = findModuleByAttributeAndSort("armorKineticDamageResonance", true);
        while(itr.hasNext()) {
            Module list = (Module)itr.next();
            if(list.isReady())
                reson[1] -= reson[1]*(1-checkResonance(Float.parseFloat((String)list.getAttribute("armorKineticDamageResonance", "0")))*mod);
            
            mod*=0.655f;
        }
        
        //THERMAL DAMAGE
        reson[2] = armorReson[2] 
    	                      * (1 - Float.parseFloat(getAttribute("cantfindit", "0")));    //ship bonus
    	
    	mod=1;
    	itr = findModuleByAttributeAndSort("thermalDamageResistanceBonus", true);
        while(itr.hasNext()) {
            Module list = (Module)itr.next();
            //System.out.println("now: "+mod);
            if(list.isReady() && (list.hasAttribute("modifyActiveArmorResonanceAndNullifyPassiveResonance") ||list.hasAttribute("modifyArmorResonancePostPercent")))
                reson[2] *= (1+Float.parseFloat((String)list.getAttribute("thermalDamageResistanceBonus", "0"))/100.0f*mod);
            else if(!list.isReady() && list.hasAttribute("modifyActiveArmorResonanceAndNullifyPassiveResonance"))
                reson[2] *= (1+Float.parseFloat((String)list.getAttribute("passivethermalDamageResistanceBonus", "0"))/100.0f*mod);
            
            mod*=0.655f;
        }
        
        mod=1;
        itr = findModuleByAttributeAndSort("armorThermalDamageResonance", true);
        while(itr.hasNext()) {
            Module list = (Module)itr.next();
            if(list.isReady())
                reson[2] -= reson[2]*(1-checkResonance(Float.parseFloat((String)list.getAttribute("armorThermalDamageResonance", "0")))*mod);
            
            mod*=0.655f;
        }

        //EXPLOSIVE DAMAGE
        reson[3] = armorReson[3] 
    	                      * (1 - Float.parseFloat(getAttribute("cantfindit", "0")));    //ship bonus
    	
    	mod=1;
    	itr = findModuleByAttributeAndSort("explosiveDamageResistanceBonus", true);
        while(itr.hasNext()) {
            Module list = (Module)itr.next();
            //System.out.println("now: "+mod);
            if(list.isReady() && (list.hasAttribute("modifyActiveArmorResonanceAndNullifyPassiveResonance") ||list.hasAttribute("modifyArmorResonancePostPercent")))
                reson[3] *= (1+Float.parseFloat((String)list.getAttribute("explosiveDamageResistanceBonus", "0"))/100.0f*mod);
            else if(!list.isReady() && list.hasAttribute("modifyActiveArmorResonanceAndNullifyPassiveResonance"))
                reson[3] *= (1+Float.parseFloat((String)list.getAttribute("passiveExplosiveDamageResistanceBonus", "0"))/100.0f*mod);
            
            mod*=0.655f;
        }
        
        mod=1;
        itr = findModuleByAttributeAndSort("armorExplosiveDamageResonance", true);
        while(itr.hasNext()) {
            Module list = (Module)itr.next();
            if(list.isReady())
                reson[3] -= reson[3]*(1-checkResonance(Float.parseFloat((String)list.getAttribute("armorExplosiveDamageResonance", "0")))*mod);
            
            mod*=0.655f;
        }
    	return reson;
    }
    
    public float [] getStructureResonance() {
    	float [] reson = new float[4];
    	reson[0] = structReson[0] - (1-checkResonance(aggregateAllSlots("hullEmDamageResonance", "*", true, false)));
    	reson[1] = structReson[1] - (1-checkResonance(aggregateAllSlots("hullKineticDamageResonance", "*", true, false)));
    	reson[2] = structReson[2] - (1-checkResonance(aggregateAllSlots("hullThermalDamageResonance", "*", true, false)));
    	reson[3] = structReson[3] - (1-checkResonance(aggregateAllSlots("hullExplosiveDamageResonance", "*", true, false)));
    	return reson;
    }
    
    // -- CALCULATIONS -- //
    
    /**
     * Calculates the generic DPS of the ship, does not account for ranges or damage types.
     * This number will always be higher than actual DPS.
     * 
     * @return the generic DPS
     */
    public float calculateGenericDps() {
    	float dps = 0.0f;
        for(int i=0;i<hiSlots.length;i++) {
        	if(hiSlots[i] != null /*&& hiSlots[i].isReady()*/ && hiSlots[i] instanceof Weapon) {
	        	dps += ((Weapon)hiSlots[i]).calculateAggregateDps();
        	}
        }
        return dps;
    }
    
    public float calculateGenericDpsAtRange(float range) {
        float dps = 0.0f;
        float maxDps = calculateGenericDps();
        for(int i=0;i<hiSlots.length;i++) {
        	if(hiSlots[i] != null /*&& hiSlots[i].isReady()*/ && hiSlots[i] instanceof Weapon) {
	        	dps += ((Weapon)hiSlots[i]).calculateAtRange(range) * maxDps;
        	}
        }
        return dps;
    }
    
    public float calculateGenericVolley() {
    	float dps = 0.0f;
    	float baseDmg = 0.0f;
        for(int i=0;i<hiSlots.length;i++) {
        	if(hiSlots[i] != null /*&& hiSlots[i].isReady()*/ && hiSlots[i] instanceof Weapon) {
	        	float multiplier = ((Weapon)hiSlots[i]).calculateDamageMultiplier();
	        	if(hiSlots[i].getCharge() != null) {
	        		baseDmg = hiSlots[i].getCharge().getTotalDamage();
	        		dps += (baseDmg * multiplier);
	        	}
        	}
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
    	
    	if(hiSlots[i] != null && hiSlots[i] instanceof Weapon) {
	    	return ((Weapon)hiSlots[i]).calculateSpecificDps(); 
    	}
    	
    	float dps[] = new float[4];
    	dps[0] = dps[1] = dps[2] = dps[3] = 0.0f;
    	
    	return dps;
    }
    
    public float calculateScanResolution() {
    	return multiplyAttributeProperty(scanRes, "scanResolutionMultiplier", true) * (1+aggregateAllSlots("scanResolutionBonus", "*", false, true)/100f);
    }
    
    public float calculateRadius() {
    	return sigRadius + (aggregateAllSlots("signatureRadiusAdd", "*", false, false));
    }
    
    public float calculateRechargeRate() {
    	return multiplyAttributeProperty(
    			multiplyAttributeBonus((1-pilot.getSkillLevel("3416")*0.05f) * Float.parseFloat(getAttribute("shieldRechargeRate", "0")), "rechargeratebonus", true), "shieldRechargeRateMultiplier", true)/1000.0f;
    }
    
    public float calculateMaxCapacity() {
    	cap = multiplyAttributeBonus(capMax * pilot.getSkillBonus("3418"), "capacitorCapacityBonus", true);
    	
        return multiplyAttributeProperty(cap, "capacitorCapacityMultiplier", true);
    }
    
    public float calculateCapacitorRechargeRate() {
    	float recharge = Float.parseFloat(getAttribute("rechargeRate", "0")) * (1-pilot.getSkillLevel("3417")*0.05f);
        return multiplyAttributeBonus((multiplyAttributeProperty(recharge, "capacitorRechargeRateMultiplier", true) / 1000.0f), "capRechargeBonus", true);
    }
    
    public float calculateCapacitorUsage() {
    	float capu = 0;
    	
    	//HI
    	for(int i=0;i<hiSlots.length;i++) {
    		if(hiSlots[i] != null && hiSlots[i].requiresActivation() && hiSlots[i].isActive()) {
    			capu += hiSlots[i].getCapNeed();
    		}
    	}
    	
    	//MID
    	for(int i=0;i<midSlots.length;i++) {
    		if(midSlots[i] != null && midSlots[i].requiresActivation() && midSlots[i].isActive()) {
    			capu += midSlots[i].getCapNeed();
    		}
    	}
    	
    	//LOW
    	for(int i=0;i<lowSlots.length;i++) {
    		if(lowSlots[i] != null && lowSlots[i].requiresActivation() && lowSlots[i].isActive()) {
    			capu += lowSlots[i].getCapNeed();
    		}
    	}
    	
    	return capu;
    }
    
    public float calculateMaxShields() {
    	return multiplyAttributeProperty(shieldHp * pilot.getSkillBonus("3419") * (1 + aggregateAllSlots("shieldCapacityBonus", "*", true, false)/100.0f ) + aggregateAllSlots("capacityBonus", "*", true, false), "shieldCapacityMultiplier", true);
    }
    
    public float calculateMaxArmor() {
    	return armorHp * (1+aggregateAllSlots("armorHpBonus", "*", true, true)/100.0f) * pilot.getSkillBonus("3394") + aggregateAllSlots("armorHPBonusAdd", "*", true, false);
    }
    
    public float calculateMaxStructure() {
    	return multiplyAttributeProperty(hp * pilot.getSkillBonus("3392") + aggregateAllSlots("hpBonusAdd", "*", true, false), "structureHPMultiplier", true);
    }
    
    public float calculateMaxRange() {
    	return multiplyAttributeBonus(maxRange * pilot.getSkillBonus("3428"), "maxTargetRangeBonus", true);
    }
    
    public int calculateMaxSpeed() {
    	return (int)multiplyAttributeBonus(maxSpeed * pilot.getSkillBonus("3449"), "speedFactor", true);
    }
    
    public float getTotalCargoCapacity() {
        return multiplyAttributeProperty(Float.parseFloat(getAttribute("capacity", "0")), "cargoCapacityMultiplier", true);
    }
    
    public float getFreeCargoCapacity() {
    	Iterator itr = cargo.iterator();
		float total = 0;
		
		while(itr.hasNext()) {
			Module m = (Module)itr.next();
			
			total += m.getVolume();
		}
    	
        return getTotalCargoCapacity() - total;
    }
    
    public float getTotalDroneCapacity() {
    	return Float.parseFloat(getAttribute("droneCapacity", "0"));
    }
    
    private float checkResonance(float a) {
    	return a > 0 ? a : 1;
    }
    
    public void fireShipChange() {
    	bChanged = true;
    	Iterator itr = listenerList.iterator();
    	while(itr.hasNext()) {
    		((ShipChangeListener)itr.next()).shipChanged(this);
    	}
    }
    
    public void addChangeListener(ShipChangeListener scl) {
    	listenerList.add(scl);
    }
    
    public void removeChangeListener(ShipChangeListener scl) {
        listenerList.remove(scl);
    }
    
    public void characterChanged(Character c) {
        pilot = c;
        fireShipChange();
    }
}
