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

import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;

import tightfit.TightPreferences;

public class MemoryList extends JList {
	private static final long serialVersionUID = 1L;
	
	private Vector data;
    private String name;
    
    public MemoryList(String n) {
        data = new Vector();
        name = n;
    }
    
    public void remember(Object c) {
        int count = Math.min(25, Integer.parseInt(TightPreferences.node("memory").node(name).get("memcount", "0")));
        
        if(!data.contains(c)) {            
            
            insertFirst(c);
            
            for(int i = count; i > 0; i--) {
                TightPreferences.node("memory").node(name).put("mem"+i, TightPreferences.node("memory").node(name).get("mem"+(i-1), ""));
            }
                        
            TightPreferences.node("memory").node(name).put("memcount", ""+(count+1));
        } else {
            String cstr = c.toString();
            for(int i = 0; i < count; i++) {
                String itm = TightPreferences.node("memory").node(name).get("mem"+i, "");
                if(itm.equals(cstr)) {
                    for(int j=i;j<count;j++) {
                        TightPreferences.node("memory").node(name).put("mem"+j, TightPreferences.node("memory").node(name).get("mem"+(j+1), ""));
                    }
                    
                    for(int j = count; j > 0; j--) {
                        TightPreferences.node("memory").node(name).put("mem"+j, TightPreferences.node("memory").node(name).get("mem"+(j-1), ""));
                    }
                    
                    break;
                }
            }
            
        	/*Iterator itr = data.iterator();
        	while(itr.hasNext()) {
        		Object o = itr.next();
        		if(o.equals(c)) {
        			//bubble it to the top
        			itr.remove();
        			insertFirst(o);
        			break;
        		}
        	}*/
        }
        
        TightPreferences.node("memory").node(name).put("mem0", c.toString());
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
