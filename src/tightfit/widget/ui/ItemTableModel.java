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

import javax.swing.table.*;

import java.util.*;

import tightfit.item.*;

public class ItemTableModel extends AbstractTableModel {
   
    private String [] columns;
    private LinkedList items;
    private int sortedColumn;
    
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
    
    public Item getItemAt(int row) {
    	return (Item)items.get(row);
    }
    
    public void sort(int column) {
    	
    	Collections.sort(items, new ColumnSorter(columns[column], sortedColumn == column));
    	if(sortedColumn != column)
    		sortedColumn = column;
    	else sortedColumn = 0;
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
    
    private class ColumnSorter implements Comparator {
    	private String columnName;
    	private boolean ascending;
    	
    	ColumnSorter(String c, boolean asc) {
    		columnName = c;
    		ascending = asc;
    	}
    	
    	public int compare(Object a, Object b) {
    		Item i1 = (Item)a;
    		Item i2 = (Item)b;
    		
    		Object o1 = i1.getAttribute(columnName, "0");
    		Object o2 = i2.getAttribute(columnName, "0");
    		
    		if (o1 instanceof String && ((String)o1).length() == 0) {
                o1 = null;
            }
            if (o2 instanceof String && ((String)o2).length() == 0) {
                o2 = null;
            }

            if(o1 instanceof String && (((String)o1).charAt(0) > '0' && ((String)o1).charAt(0) < '9')) {
            	o1 = Float.valueOf((String)o1);
            	o2 = Float.valueOf((String)o2);
            }
            
            if(o2 instanceof String && (((String)o2).charAt(0) > '0' && ((String)o2).charAt(0) < '9')) {
            	o2 = Float.valueOf((String)o2);
            	o1 = Float.valueOf((String)o1);
            }
            
            if(o1 == null && o2 == null) {
            	return 0;
            } else if(o1 == null) { 
            	return 1;
            } else if(o2 == null) {
            	return -1;
            } else if(o1 instanceof Comparable) {
            	if (ascending) {
                    return ((Comparable)o1).compareTo(o2);
                } else {
                    return -((Comparable)o1).compareTo(o2);
                }
            } else {
                if (ascending) {
                    return o1.toString().compareTo(o2.toString());
                } else {
                    return o2.toString().compareTo(o1.toString());
                }
            }
    	}
    }
}
