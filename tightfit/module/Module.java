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

import tightfit.item.*;

public class Module extends Item {

	public static int LOW_SLOT = 1;
	public static int MID_SLOT = 2;
	public static int HI_SLOT = 3;
	public static int RIG_SLOT = 4;
	
    private int slotRequirement;
    
    private boolean bActive = false;
    
    private Ammo charge;
    
    public Module() {
        
    }
    
    public Ammo getCharge() {
    	return charge;
    }
    
    public float getCpuUsage() {
    	if(attributes.contains("cpu"))
    		return Float.parseFloat((String)attributes.get("cpu"));
    	return 0.0f;
    }
    
    public float getPowerUsage() {
    	if(attributes.contains("power"))
    		return Float.parseFloat((String)attributes.get("power"));
    	return 0.0f;
    }
}
