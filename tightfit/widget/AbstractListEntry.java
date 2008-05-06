/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */
package tightfit.widget;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import tightfit.item.Item;

public abstract class AbstractListEntry extends JPanel {
	protected Item myItem;
	
	public AbstractListEntry() {}
	
	public AbstractListEntry(Item item) {
		myItem = item;
		
		setOpaque(true);
		setLayout(new BorderLayout());
		
		setName(item.name);
		setToolTipText(item.name);
	}
}
