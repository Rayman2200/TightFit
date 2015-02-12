/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package test;

import org.junit.*;
import static org.junit.Assert.*;

import tightfit.Resources;
import tightfit.item.Database;
import tightfit.module.*;
import tightfit.ship.Ship;

public class GallenteWeaponTestHarness extends WeaponTestHarness {

	public GallenteWeaponTestHarness() {
	}

	@Before public void init() throws Exception {
		super.init();
		
		ship = new Ship(Database.getInstance().getType("Megathron"));
		ship.pilot = character;
		ship.putModule(new Module(Database.getInstance().getType("")), Module.HI_SLOT, 0);
	}

	@Test public void testRoF() {
		Weapon gun = (Weapon)ship.getModule(Module.HI_SLOT, 0);
		System.out.println("RoF:"+gun.calculateRoF());

		assertTrue(gun.calculateRoF() < 20);
	}

	@Test public void testDps() {
		Weapon gun = (Weapon)ship.getModule(Module.HI_SLOT, 0);
		System.out.println("DPS:"+gun.calculateAggregateDps());

		assertTrue(gun.calculateAggregateDps()>0);
	}
}
