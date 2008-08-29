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

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import java.util.*;

import tightfit.item.*;

public class ItemTableModel extends AbstractTableModel {
   
    private String [] columns;
    private LinkedList items;
    
    public ItemTableModel(int parent, String columns[]) {
        
        this.columns = columns;
        items = new LinkedList();
        
        buildList(parent);
        
    }
    
    public int getRowCount() {
        return items.size();
    }
    
    public int getColumnCount() {
        return columns.length;
    }
    
    public String getColumnName(int c) {
        return columns[c];
    }
    
    public boolean isCellEditable(int r, int c) {
        return false;
    }
    
    public Object getValueAt(int row, int column) {
        Item m = (Item)items.get(row);
        if(columns[column].equalsIgnoreCase("name")) {
            return m.name;
        }
        return m.getAttribute(columns[column], "N/A");
    }
    
    private void buildList(int parent) { 
        try {
            Database db = Database.getInstance();
            LinkedList list = db.getMarketGroupByParent(parent);
        
            if(list.size() > 0) {
                Iterator itr = list.iterator();
                while(itr.hasNext()) {
                    MarketGroup g = (MarketGroup)itr.next();
                    buildList(g.groupId);
                }
            } else {
                LinkedList itm = db.getTypeByMarketGroup(parent);
                Iterator itr = itm.iterator();
                while(itr.hasNext()) {
                    items.addLast(itr.next());
                }
            }
        } catch(Exception e) {}
    }
}
