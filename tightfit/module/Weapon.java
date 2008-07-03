/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.module;

import java.util.Iterator;

import tightfit.item.Ammo;
import tightfit.item.Item;

public class Weapon extends Module {

	public static final int WEAPON_BLASTER  = 1;
	public static final int WEAPON_LAUNCHER = 2;
	public static final int WEAPON_LASER    = 3;
	public static final int WEAPON_CANNON   = 4;
	
	private int weaponType = 0;
	
	public Weapon() {
	}
	
	public Weapon(Item type) {
		super(type);
		
		if(type.marketGroupId == 561 || type.marketGroupId == 562 || type.marketGroupId == 563 ||
				type.marketGroupId == 564 || type.marketGroupId == 565 || type.marketGroupId == 566) {
			weaponType = WEAPON_BLASTER;
		} else if(type.marketGroupId == 567 || type.marketGroupId == 568 || type.marketGroupId == 569 || 
				type.marketGroupId == 570 || type.marketGroupId == 571  || type.marketGroupId == 572) {
			weaponType = WEAPON_LASER;   //We'll use a "laser" to heat the Earth's atmosphere...
		} else if(type.marketGroupId == 639 || type.marketGroupId == 643 || type.marketGroupId == 640  || 
				type.marketGroupId == 642  || type.marketGroupId == 641 || type.marketGroupId == 644) {
			weaponType = WEAPON_LAUNCHER;
		} else if(type.marketGroupId == 574 || type.marketGroupId == 575 || type.marketGroupId == 576  || 
				type.marketGroupId == 577 || type.marketGroupId == 578  || type.marketGroupId == 579) {
			weaponType = WEAPON_CANNON;
		}
	}
	
	public float calculateRoF() {
		//TODO: include skill and stabs
        float rof = Float.parseFloat(((String)getAttribute("speed", "1.0"))) * getCharge().getRateBonus(),
                mod = 1.0f;
		String prop = getTypeString()+"WeaponSpeedMultiply";
        Iterator itr = myShip.findModuleByAttributeAndSort("speedMultiplier", true);
        
        while(itr.hasNext()) {
            Module m = (Module)itr.next();
            if(m.hasAttribute(prop)) {
                rof *= Float.parseFloat(m.getAttribute("speedMultiplier", "1.0")) * mod;
                mod *= .655f;
            }
        }
        
        return rof;
	}
	
	public String getTypeString() {
		int t = getType();
		if(t == WEAPON_BLASTER) {
			return "hybrid";
		} else if(t == WEAPON_CANNON) {
			return "projectile";
		} else if(t == WEAPON_LAUNCHER) {
			return "launcher";
		} else if(t == WEAPON_LASER) {
			return "energy";
		}
		
		return "none";
	}
	
	public int getType() {
		return weaponType;
	}
	
	public float calculateAggregateDps() {
		float baseDmg = getCharge().getTotalDamage();
		float rate = calculateRoF();
		float multiplier = calculateDamageMultiplier();
		
		return (baseDmg * multiplier) / rate;
	}
	
	public float [] calculateSpecificDps() {
		float dps[] = new float[4];
    	dps[0] = dps[1] = dps[2] = dps[3] = 0.0f;
    	
    	float multiplier = calculateDamageMultiplier();
    	Ammo charge = getCharge();
    	float rate = calculateRoF();
    	
    	dps[0] = (charge.getEmDamage() * multiplier) / rate;
    	dps[1] = (charge.getKineticDamage() * multiplier) / rate;
    	dps[2] = (charge.getThermalDamage() * multiplier) / rate;
    	dps[3] = (charge.getExplosiveDamage() * multiplier) / rate;
    	
    	return dps;
	}
	
	public float calculateDamageMultiplier() {
		float mult = Float.parseFloat(getAttribute("damageMultiplier", "1.0")) 
                    * myShip.pilot.getSkillBonus("3300") 
                    * myShip.pilot.getSkillBonus("3315");
        float mod = 1.0f;
        String prop = getTypeString()+"WeaponDamageMultiply";
        Iterator itr = myShip.findModuleByAttributeAndSort("damageMultiplier", true);
        
        //get the primary skill
        float bonus = myShip.pilot.getSkillBonus(getAttribute("requiredSkill1", "0"));
        
        
        while(itr.hasNext()) {
            Module m = (Module)itr.next();
            if(m.hasAttribute(prop)) {
                mult *= Float.parseFloat(m.getAttribute("damageMultiplier", "1.0")) * mod;
                mod *= .655f;
            }
        }
        
        return mult;
	}
}
