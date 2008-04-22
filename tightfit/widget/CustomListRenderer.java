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

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class CustomListRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	
	private Color bgColor, bgLightColor;
	
	public CustomListRenderer() {
		bgColor = new Color(.07f, .25f, .43f);
		bgLightColor = new Color(.07f+0.3f, .25f+0.3f, .43f+0.3f);
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
            int index,  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        AbstractListEntry module = (AbstractListEntry)value;

        if (module != null) {
            module.setBackground(bgColor);
            if(isSelected)
            	module.setBackground(bgLightColor);
            return module;
        } else {
            setIcon(null);
            setText("--");
        }

        return this;
    }
}
