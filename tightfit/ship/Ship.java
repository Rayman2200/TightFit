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

package tightfit.ship;

import java.util.Vector;

import tightfit.item.Item;

/**
 * 
 * 
 * @version $Id$
 */
public class Ship extends Item {

    private Vector hiSlots;
    private Vector midSlots;
    private Vector lowSlots;
    private Vector rigSlots;
    
    public float calculateDps() {
        
        return 0.0f;
    }
    
    public float calculateMaxCapacity() {
        return 0.0f;
    }
}
