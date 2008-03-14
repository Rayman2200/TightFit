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

import java.awt.*;

import java.util.LinkedList;

class SlickLayoutManager implements LayoutManager {

    private LinkedList list;

    public SlickLayoutManager() {
        list = new LinkedList();
    }
    
    public void addLayoutComponent(String name, Component c) {
        list.add(c);
    }
    
    public void layoutContainer(Container c) {
    	Component [] l = c.getComponents();
    	for(int i=0;i<c.getComponentCount();i++) {
    		if(l[i] instanceof Slot) {
    			Slot s = (Slot)l[i];
    			//System.out.println(s.getLocation().toString());
    			//s.paint(c.getGraphics());
    		}
    	}
    }
    
    public Dimension minimumLayoutSize(Container c) {
        return c.getSize();
    }
    
    public Dimension preferredLayoutSize(Container c) {
        return c.getPreferredSize();
    }
    
    public void removeLayoutComponent(Component c) {
        list.remove(c);
    }
}
