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

import java.awt.Frame;
import java.awt.event.ActionEvent;

import tightfit.dialogs.ShowInfoDialog;
import tightfit.item.Item;

public class ShowInfoAction extends AbstractItemAction {
	private Frame parent;
	
	public ShowInfoAction(Frame parent, Item item) {
		super("Show Info", item);
		this.parent = parent;
	}
	
	protected void doAction(ActionEvent ae) {
		ShowInfoDialog sid = new ShowInfoDialog(parent, myItem);
	}

}
