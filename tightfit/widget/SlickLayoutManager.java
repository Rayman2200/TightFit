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
