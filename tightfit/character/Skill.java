/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */
package tightfit.character;

import tightfit.item.Database;
import tightfit.item.Item;

public class Skill extends Item {

	private int level;
    
	public Skill() {
	}
    
	public Skill(String id, int level) throws Exception {
		super(Database.getInstance().getTypeById(id));
		
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
    
    public float getBonus() {
        try {
            String [] key = getAttributeKey("Bonus");
            for(int i = 0;i<key.length;i++)
            	if(key[i].length() < 25)   //at least the other ones are extremely long...
            		return Float.parseFloat(getAttribute(key[i], "0.0"));
        } catch (Exception e) {
        }
        return 1.0f;
    }
}
