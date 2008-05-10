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
		super(Database.getInstance().getType(id));
		
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
}
