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

import tightfit.item.Ammo;
import tightfit.item.Item;

public class Weapon extends Module {

	public Weapon() {
	}
	
	public Weapon(Item type) {
		super(type);
	}
	
	public float calculateRoF() {
		//TODO: include skill and stabs
		return Float.parseFloat(((String)getAttribute("speed", "1.0"))) * getCharge().getRateBonus();
	}
	
	public float [] calculateSpecificDps() {
		float dps[] = new float[4];
    	dps[0] = dps[1] = dps[2] = dps[3] = 0.0f;
    	
    	float multiplier = Float.parseFloat((getAttribute("damageMultiplier", "1.0")));
    	Ammo charge = getCharge();
    	float rate = calculateRoF();
    	
    	dps[0] = (charge.getEmDamage() * multiplier) / rate;
    	dps[1] = (charge.getKineticDamage() * multiplier) / rate;
    	dps[2] = (charge.getThermalDamage() * multiplier) / rate;
    	dps[3] = (charge.getExplosiveDamage() * multiplier) / rate;
    	
    	return dps;
	}
}
