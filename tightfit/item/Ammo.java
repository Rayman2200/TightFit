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
	
	public float getTotalDamage() {
		return getEmDamage() + getKineticDamage() + getThermalDamage() + getExplosiveDamage();
	}
	
	public float getEmDamage() {
		if(attributes.contains("emDamage"))
			return Float.parseFloat((String)attributes.get("emDamage"));
		return 0.0f;
	}
	
	public float getKineticDamage() {
		if(attributes.contains("kineticDamage"))
			return Float.parseFloat((String)attributes.get("kineticDamage"));
		return 0.0f;
	}
	
	public float getThermalDamage() {
		if(attributes.contains("thermalDamage"))
			return Float.parseFloat((String)attributes.get("thermalDamage"));
		return 0.0f;
	}
	
	public float getExplosiveDamage() {
		if(attributes.contains("explosiveDamage"))
			return Float.parseFloat((String)attributes.get("explosiveDamage"));
		return 0.0f;
	}
	
	public float getRateBonus() {
		if(attributes.contains("speedBonus"))
			return Float.parseFloat((String)attributes.get("speedBonus"));
		return 1.0f;
	}
	
	public float getRangeBonus() {
		if(attributes.contains("weaponRangeMultiplier"))
			return (1.0f - Float.parseFloat((String)attributes.get("weaponRangeMultiplier"))) * 100;
		return 1.0f;
	}
}
