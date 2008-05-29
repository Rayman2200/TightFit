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

import java.util.Vector;

import java.awt.*;

import javax.swing.*;

import tightfit.TightPreferences;

public class MemoryList extends JList {

    private Vector data;
    
    public MemoryList() {
        data = new Vector();
    }
    
    public void remember(Object c) {
        String count = TightPreferences.node("memory").get("memcount", "0");
        
        insertFirst(c);
        
        for(int i = Integer.parseInt(count); i > 1; i--) {
            TightPreferences.node("memory").put("mem"+i, TightPreferences.node("memory").get("mem"+(i-1), null));
        }
        
        TightPreferences.node("memory").put("mem0", c.toString());
    }
    
    public void insert(Object c) {
        insertLast(c);
    }
    
    public void insertLast(Object c) {
        if(!data.contains(c)) {
            data.add(c);
            setListData(data);
        }
    }
    
    public void insertFirst(Object c) {
        if(!data.contains(c)) {
            data.add(0, c);
            setListData(data);
        }
    }
}
