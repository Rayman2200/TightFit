/*
 *  TightFit (c) 2008 Adam Turk
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Adam Turk <aturk@biggeruniverse.com>
 */

package tightfit.module;

import java.util.Hashtable;

import tightfit.item.Item;

public class Module extends Item {

	public static int LOW_SLOT = 1;
	public static int MID_SLOT = 2;
	public static int HI_SLOT = 3;
	public static int RIG_SLOT = 4;
	
    public int slotRequirement;

    public Module() {
        
    }
}
