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

import tightfit.TightPreferences;

public class CustomListRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	
	private Color bgColor, bgLightColor;
	
	public CustomListRenderer() {
		bgColor = Color.decode(TightPreferences.node("prefs").get("bgColor", "#11446D"));
		bgLightColor = new Color(Math.min(255, bgColor.getRed()+76)/255.0f, Math.min(255, bgColor.getGreen()+76)/255.0f, Math.min(255, bgColor.getBlue()+76)/255.0f);
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
