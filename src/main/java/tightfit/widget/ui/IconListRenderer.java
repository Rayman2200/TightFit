/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */
package tightfit.widget.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;
import javax.swing.border.LineBorder;

import tightfit.widget.ItemIcon;

public class IconListRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;
    
    public IconListRenderer() {
    }
    
    public Component getListCellRendererComponent(JList list, Object value,
            int index,  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        ItemIcon module = (ItemIcon)value;

        setBackground(new Color(0,0,0,0));
        
        if(isSelected)
        	setBorder(new LineBorder(Color.white, 1, true));
        
        if (module != null) {
            setIcon(module.getIcon());
            setText("");
        } else {
            setIcon(null);
            setText("--");
        }

        return this;
    }
}
