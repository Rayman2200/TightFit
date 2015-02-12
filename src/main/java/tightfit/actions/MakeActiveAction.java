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

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.item.Item;
import tightfit.ship.Ship;
import tightfit.widget.MemoryList;

public class MakeActiveAction extends AbstractItemAction {
	private MemoryList quickList;
	private Item item;
	
	public MakeActiveAction(MemoryList l, Item i) {
		super(Resources.getString("dialog.market.makeactive"), i);
		quickList = l;
		item = i;
	}

	protected void doAction(ActionEvent ae) {
		quickList.remember(myItem);

		TightFit.getInstance().setShip(new Ship(item));
	}

}
