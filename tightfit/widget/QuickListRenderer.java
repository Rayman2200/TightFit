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

import javax.swing.*;

import tightfit.item.*;

public class QuickListRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;
    
    public QuickListRenderer() {
    }
    
    public Component getListCellRendererComponent(JList list, Object value,
            int index,  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        Item module = (Item)value;

        if(isSelected)
            setBackground(Color.GRAY);
        else setBackground(Color.DARK_GRAY);
        
        setForeground(Color.WHITE);
        
        if (module != null) {
            setIcon(new ImageIcon(module.getImage()));
            setText(module.name);
        } else {
            setIcon(null);
            setText("--");
        }

        return this;
    }
}
