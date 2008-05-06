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

public class Ammo extends Item {

	public Ammo() {
		
	}
	
	public Ammo(Item type) {
		super(type);
	}
	
	public float getTotalDamage() {
		return getEmDamage() + getKineticDamage() + getThermalDamage() + getExplosiveDamage();
	}
	
	public float getEmDamage() {
		return Float.parseFloat(getAttribute("emDamage", "0"));
	}
	
	public float getKineticDamage() {
		return Float.parseFloat(getAttribute("kineticDamage", "0"));
	}
	
	public float getThermalDamage() {
		return Float.parseFloat(getAttribute("thermalDamage", "0"));
	}
	
	public float getExplosiveDamage() {
		return Float.parseFloat(getAttribute("explosiveDamage", "0"));
	}
	
	public float getRateBonus() {
		return Float.parseFloat(getAttribute("speedBonus", "1.0"));
	}
	
	public int getSize() {
		return Integer.parseInt(getAttribute("chargeSize", "1"));
	}
	
	public float getRangeBonus() {
		if(attributes.contains("weaponRangeMultiplier"))
			return (1.0f - Float.parseFloat((String)attributes.get("weaponRangeMultiplier"))) * 100;
		return 1.0f;
	}
}
