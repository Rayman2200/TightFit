/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.io;

import tightfit.item.Ammo;
import tightfit.item.Database;
import tightfit.module.Module;
import tightfit.ship.Ship;

public class EFTShipParser {

	public EFTShipParser() {
	}
	
	public EFTShipParser(String cb) {
		
	}
	
	public Ship parse(String sb) throws Exception {
		Ship ship = null;
		int slotType = Module.LOW_SLOT, slot=0;
		String [] lines = sb.split("\\n");
		
		if(!lines[0].startsWith("["))
			throw new Exception("Not valid EFT data");
		
		ship = new Ship(Database.getInstance().getType(lines[0].substring(1, lines[0].indexOf(","))));
		
		for(int i=1;i<lines.length;i++) {
			if(lines[i].length() == 0) {
				if(++slotType == 4)
					break;
				slot=0;
			} else {
				String [] parts = lines[i].split(",");
				Module m = new Module(Database.getInstance().getType(parts[0]));
				ship.putModule(m, slotType, slot++);
				if(parts.length > 1) {
					m.insertCharge(new Ammo(Database.getInstance().getType(parts[1])));
				}
			}
		}
		
		return ship;
	}
}
