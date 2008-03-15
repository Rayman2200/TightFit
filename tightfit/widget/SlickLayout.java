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

class SlickLayout implements LayoutManager {

    public SlickLayout() {
    }
    
    public void addLayoutComponent(String name, Component c) {
    }
    
    public void layoutContainer(Container target) {
    	
    	for(int i=0;i<target.getComponentCount();i++) {
            Component m = target.getComponent(i);
            if(m.isVisible()) {
                Point pt = m.getLocation();
                
                Dimension d = m.getPreferredSize();
                m.setSize(d.width, d.height);
                
                m.setLocation(pt);
                //System.out.println("set location");
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
    }
}
