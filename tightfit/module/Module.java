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

import tightfit.TightFit;
import tightfit.character.Skill;
import tightfit.item.*;
import tightfit.ship.Ship;

public class Module extends Item {

	public static int LOW_SLOT = 0;
	public static int MID_SLOT = 1;
	public static int HI_SLOT = 2;
	public static int RIG_SLOT = 3;
	
    public int slotRequirement;
    
    private Ship myShip;
    
    private boolean bActive = false,
                bOnline = true;
    
    protected Ammo charge = new Ammo();
    
    public Module() {
    }
    
    public Module(Item type) {
    	super(type);
    	
    	slotRequirement = -1;
    	//figure out slot requirement...
    	if(!type.getAttribute("loPower", "-1").equals("-1")) {
    		slotRequirement = LOW_SLOT; 
    	} else if(!type.getAttribute("hiPower", "-1").equals("-1")) {
    		slotRequirement = HI_SLOT;
    	} else if(!type.getAttribute("medPower", "-1").equals("-1")) {
    		slotRequirement = MID_SLOT;
    	} else if(!type.getAttribute("rigSlot", "-1").equals("-1")) {
    		slotRequirement = RIG_SLOT;
    	}
    }
    
    public Module(Module m) {
    	super(m);
    	charge = m.charge;
    	slotRequirement = m.slotRequirement;
    	
    }
    
    public void setShip(Ship s) {
        myShip = s;
    }
    
    public Ammo getCharge() {
    	return charge;
    }
    
    public void insertCharge(Ammo a) {
    	charge = a;
    }
    
    public int getChargeCount() {
    	if(charge != null) {
    		float cvol = Float.parseFloat(charge.getAttribute("volume", "1"));
    		float cap = Float.parseFloat(getAttribute("capacity", "0"));
    		return (int)(cap / cvol);
    	}
    	
    	return 0;
    }
    
    public float getCpuUsage() {
    	float cpu = Float.parseFloat(getAttribute("cpu", "0"));
    	if(TightFit.getInstance().getChar().hasRequiredSkill(this)) {
    		Skill skill = TightFit.getInstance().getChar().getSkill(getAttribute("requiredSkill1", "0"));
    	}
    	
    	//TODO: also calc bonus for skills
    	return cpu;
    }
    
    public float getPowerUsage() {
    	//TODO: also calc bonus for skills
    	return Float.parseFloat(getAttribute("power", "0"));
    }
    
    public float getCapNeed() {
    	float dur = (Float.parseFloat(getAttribute("duration", "1000"))/1000.0f);
    	if(attributes.containsKey("speed")) {
    		dur = (Float.parseFloat(getAttribute("speed", "1000"))/1000.0f);
    	}
    	return Math.abs(Float.parseFloat(getAttribute("capacitorNeed", "0")) / dur);
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
    	if(requiresActivation()) {
	        if(bOnline)
	            bActive = true;
	        else throw new Exception("Module not online!");
    	}
    }
    
    public void deactivate() {
    	bActive = false;
    }
}
