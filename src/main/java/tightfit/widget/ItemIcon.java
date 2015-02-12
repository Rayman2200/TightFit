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

import javax.swing.*;

import java.awt.*;

import tightfit.item.Item;

public class ItemIcon extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private Item myItem;
	
	public ItemIcon(Item m) {
		myItem = m;
		setOpaque(false);
		setIcon(new ImageIcon(myItem.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(32, 32);
	}
}
