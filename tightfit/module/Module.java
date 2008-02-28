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
	
    public int slotRequirement;
    
    private boolean bActive = false,
                bOnline = true;
    
    private Ammo charge;
    
    public Module() {
        
    }
    
    public Module(Item type) {
    	
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
    	if(attributes.contains("cpu"))
    		return Float.parseFloat((String)attributes.get("cpu"));
    	return 0.0f;
    }
    
    public float getPowerUsage() {
    	//TODO: also calc bonus for skills
    	if(attributes.contains("power"))
    		return Float.parseFloat((String)attributes.get("power"));
    	return 0.0f;
    }
    
    public boolean isOnline() {
        return bOnline;
    }
    
    public void online() {
        bOnline = true;
    }
    
    public void offline() {
        bOnline = false;
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
