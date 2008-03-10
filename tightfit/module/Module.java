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

	public static int LOW_SLOT = 0;
	public static int MID_SLOT = 1;
	public static int HI_SLOT = 2;
	public static int RIG_SLOT = 3;
	
    public int slotRequirement;
    
    private boolean bActive = false,
                bOnline = true;
    
    private Ammo charge;
    
    public Module() {
    }
    
    public Module(Item type) {
    	super(type);
    }
    
    public Module(Module m) {
    	charge = m.charge;
    	slotRequirement = m.slotRequirement;
    	
    }
    
    public Ammo getCharge() {
    	return charge;
    }
    
    public float getCpuUsage() {
    	//TODO: also calc bonus for skills
    	return Float.parseFloat(getAttribute("cpu", "0"));
    }
    
    public float getPowerUsage() {
    	//TODO: also calc bonus for skills
    	return Float.parseFloat(getAttribute("power", "0"));
    }
    
    public boolean isReady() {
    	return (requiresActivation() && isActive()) || (!requiresActivation() && isOnline());
    }
    
    public boolean isOnline() {
        return bOnline;
    }
    
    public void online() {
        bOnline = true;
    }
    
    public void offline() {
        bOnline = false;
        bActive = false;
    }
    
    public boolean isActive() {
    	return bActive;
    }
    
    public void activate() throws Exception {
        if(bOnline)
            bActive = true;
        else throw new Exception("Module not online!");
    }
    
    public void deactivate() {
    	bActive = false;
    }
}
