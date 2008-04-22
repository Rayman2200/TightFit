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

public class MemoryList extends JList {

    private Vector data;
    
    public MemoryList() {
        data = new Vector();
    }
    
    public void remember(Object c) {
        insertFirst(c);
        //TODO: remember to preferences
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
    
    public void recall() {
        //TODO: read remembered things from preferences
    }
}
