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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import tightfit.character.Skill;
import tightfit.item.*;
import tightfit.ship.Ship;
import tightfit.TightFit;

/**
 * An {@link Item} which may be fitted to a ship.
 *
 */
public class Module extends Item {

	public static final int LOW_SLOT = 0;
	public static final int MID_SLOT = 1;
	public static final int HI_SLOT = 2;
	public static final int RIG_SLOT = 3;
	
    public int slotRequirement;
    public int dups = 0;
    
    protected Ship myShip;
    
    private boolean bActive = false,
                	bOnline = true;
    
    protected Ammo charge = new Ammo();
    
    private static HashMap nameNormalizerMap = new HashMap();
    
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
    
    /**
     * Get the maximum charges that the module can hold.
     * 
     * @return
     */
    public int getChargeCount() {
    	if(charge != null) {
    		float cvol = Float.parseFloat(charge.getAttribute("volume", "1"));
    		float cap = Float.parseFloat(getAttribute("capacity", "0"));
    		return (int)(cap / cvol);
    	}
    	
    	return 0;
    }
    
    public float getVolume() {
    	return Float.parseFloat(getAttribute("volume", "0")) * (dups > 0 ? dups : 1);
    }
    
    public float getCpuUsage() {
    	float cpu = getModifiedAttribute("cpu", null, "0");
		
    	if(myShip != null && myShip.hasAttribute("cpuNeedBonus")) {
    		//does this role bonus affect this module?
    		
    	}
    	
    	return cpu;
    }
    
    public float getPowerUsage() {
    	float power = getModifiedAttribute("power", null, "0");
		
    	return power;
    }
    
    public float getCapNeed() {
    	float dur = (Float.parseFloat(getAttribute("duration", "1000"))/1000.0f);
    	float need = getModifiedAttribute("capacitorNeed", null, "0");
    	
    	if(attributes.containsKey("speed")) {
    		dur = (Float.parseFloat(getAttribute("speed", "1000"))/1000.0f);
    	}
    	return Math.abs(need / dur);
    }
    
    public boolean canOverload() {
        return !getAttribute("requiredThermoDynamicsSkill", "-1").equals("-1");
    }
    
    /**
     * Lets us know whether a module is ready to give bonuses. Either the modules is passive
     * and does not require activation, or it has been activated.
     * 
     * @return <code>true</code> if the modules is "ready", <code>false</code> otherwise
     */
    public boolean isReady() {
    	return (requiresActivation() && isActive()) || (!requiresActivation() && isOnline());
    }
    
    public boolean isOnline() {
        return bOnline;
    }
    
    /**
     * Put the module in online status.
     */
    public void online() {
        bOnline = true;
    }
    
    /**
     * Put the module in offline and inactive status.
     * 
     * @see Module#deactivate()
     */
    public void offline() {
        bOnline = false;
        deactivate();
    }
    
    public boolean isActive() {
    	return bActive;
    }
    
    public void activate() throws Exception {
    	if(requiresActivation() || isWeapon()) {
	        if(bOnline)
	            bActive = true;
	        else throw new Exception("Module not online!");
    	}
    }
    
    public void deactivate() {
    	bActive = false;
    }
    
    /**
     * (B*(1+c||c))*(M1, M2, ...)*S
     * @param att
     * @param qualifier TODO
     * @param defaultVal
     * @return
     */
    public float getModifiedAttribute(String att, String qualifier, String defaultVal) {
    	
    	float r = Float.parseFloat(defaultVal);
    	
    	//base attribute
    	r = Float.parseFloat(getAttribute(att, defaultVal));
    	
    	//normalize the attribute name, some are weird
    	String normAtt = NameNormalizer.normalize(att);
    	
    	//FIXME: These are hacked
    	if(groupId == 62 && att.equals("duration"))
    		normAtt = "armorDamageDuration";
    	
    	//if on a ship...
    	if(myShip != null) {
    		//bonuses from character skills
    		String nameForSkills = normAtt;
    		if(att.equals("speed") && isWeapon() && ((Weapon)this).getType() != Weapon.WEAPON_LAUNCHER) {
    			nameForSkills = "turretSpeed";
	    	} else if (att.equals("maxRange")) {
	    		nameForSkills = "rangeSkill";
	    	}
    		
    		LinkedList dups = new LinkedList();
    		for(int i=1;i<=3;i++) {
				if(hasAttribute("requiredSkill"+i)) {
					Skill skill = TightFit.getInstance().getChar().getSkill(getAttribute("requiredSkill"+i,"0"));
					Iterator itr = skill.buildSkillTree().iterator();
					while(itr.hasNext()) {
						Skill s = (Skill) itr.next();
						if(!dups.contains(""+s.typeId)) {
							dups.add(""+s.typeId);
							if(s.hasAttribute(nameForSkills+"Bonus")) {
								r *= 1+(Float.parseFloat(s.getAttribute(nameForSkills+"Bonus", "0"))*s.getLevel()/100.0f);
							}
							
							if(s.hasAttribute(nameForSkills+"Multiplier")) {
								r *= Float.parseFloat(s.getAttribute(nameForSkills+"Multiplier", "1"))*s.getLevel();
							}
						}
					}
				}
    		}
    		
    		//get any module bonuses, accounting for stacking penalties 
    		HashMap penalties = new HashMap();
    		for(int i = 0; i<4; i++) {
    			for(int j = 0; j < 8; j++) {
    				Module m = myShip.getModule(i, j);
    				if(m != null && m.isReady()) {
    					if(!penalties.containsKey(""+m.groupId)) {
    						penalties.put(""+m.groupId, new Float(1));
    					}
    					
    					float mod = ((Float)penalties.get(""+m.groupId)).floatValue();
    					
	    				if(m.hasAttribute(normAtt+"Bonus")) {
	    					r *= 1.0f+((m.getModifiedAttribute(normAtt+"Bonus", qualifier, "0")*mod)/100.0);
	    				}
	
	    				if(m.hasAttribute(normAtt+"Multiplier")) {
	    					r *= m.getModifiedAttribute(normAtt+"Multiplier", qualifier, "1")*mod;
	    				}
	    				
	    				if(m.hasAttribute("stack")) {
    						penalties.put(""+m.groupId, 
    								new Float(((Float)penalties.get(""+m.groupId)).floatValue() - .33f));
    					}
    				}
    			}
    		}
    		
    		//bonuses & multipliers from the ship itself
    		
    	}
    	
    	//bonus from charge
    	r *= 1.0f+(Float.parseFloat(getCharge().getAttribute(normAtt+"Bonus", "0"))/100.0);
    	//...or multiplier from charge
    	r *= Float.parseFloat(getCharge().getAttribute(normAtt+"Multiplier", "1"));
    	
    	return r;
    }
    
    private static final class NameNormalizer {
    	public static String normalize(String n) {
    		if(n.equals("power")) {
    			return "powerNeed";
    		} else if(n.equals("capacitorNeed")) {
    			return "capNeed";
    		}
    		
    		return n;
    	}	
    };
}
