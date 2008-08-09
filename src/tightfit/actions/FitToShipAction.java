/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.actions;

import java.awt.event.ActionEvent;

import tightfit.item.Ammo;
import tightfit.item.Item;
import tightfit.ship.Ship;
import tightfit.module.*;
import tightfit.Resources;

public class FitToShipAction extends AbstractItemAction {
    private Ship myShip;
    
    public FitToShipAction(Ship ship, Item item) {
        super(item.isAmmo() ? Resources.getString("dialog.market.load") : Resources.getString("dialog.market.fitactive"), item);
        myShip = ship;
    }
    
    protected void doAction(ActionEvent ae) {
        //TODO: quickList.remember(myItem);
        if(myItem.isAmmo()) {
            Ammo ammo = new Ammo(myItem);
            for(int r=0;r<3;r++) {
                for(int s=0;s<myShip.totalSlots(r);s++) {
                    Module m = myShip.getModule(r, s);
                    if(m != null) {
                        if(m.accepts(ammo))
                            m.insertCharge(ammo);
                    }
                }
            }
            myShip.fireShipChange();
        } else {
            Module m = new Module(myItem);
            if(myShip.hasFreeSlot(m)) {
                //TODO: this should be moved internal to tightfit.ship.Ship
                //      with a Ship#getFreeSlot(Module) method
                int t=0;
                if(m.slotRequirement == Module.LOW_SLOT) {
                    t = myShip.totalSlots(Module.LOW_SLOT);
                } else if(m.slotRequirement == Module.MID_SLOT) {
                    t = myShip.totalSlots(Module.MID_SLOT);
                } else if(m.slotRequirement == Module.HI_SLOT) {
                    t = myShip.totalSlots(Module.HI_SLOT);
                } else if(m.slotRequirement == Module.RIG_SLOT) {
                    t = myShip.totalSlots(Module.RIG_SLOT);
                }
                
                for(int s=0;s<t;s++) {
                    if(myShip.testPutModule(m, m.slotRequirement, s)) {
                        if(m.isWeapon()) {
                            Weapon w = new Weapon(m);
                            myShip.putModule(w, m.slotRequirement, s);
                        } else {
                            myShip.putModule(m, m.slotRequirement, s);
                        }
                        break;
                    }
                }
            }
        }
    }
}
