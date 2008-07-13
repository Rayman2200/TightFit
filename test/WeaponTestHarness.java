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

import tightfit.Resources;
import tightfit.item.Database;
import tightfit.ship.Ship;
import tightfit.character.Character;

public class WeaponTestHarness {
	protected Character character;
	protected Ship ship;


	public WeaponTestHarness() {
	}

	@Before public void init() throws Exception {
		Database.getInstance();

		character = new Character();
	}

	@After public void cleanup() {
	}
}
