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
		graphicId = "09_13";
		name="";
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
	
	public int getSize() {
		return Integer.parseInt(getAttribute("chargeSize", "1"));
	}
	
	public float getRangeMultiplier() {
		return Float.parseFloat(getAttribute("weaponRangeMultiplier", "1"));
	}
	
	public float getFalloffMultiplier() {
		return Float.parseFloat(getAttribute("falloffMultiplier", "1"));
	}
	
	public float getSpeedMultiplier() {
		return Float.parseFloat(getAttribute("speedMultiplier", "1"));
	}
}
