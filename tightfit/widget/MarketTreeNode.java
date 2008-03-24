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

import javax.swing.tree.DefaultMutableTreeNode;

public class MarketTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;
	
	public String name;
	private int catId, groupId;
	
	public MarketTreeNode(String name, int catId, int groupId) {
		this.name = name;
		this.groupId = groupId;
		this.catId = catId;
	}
	
	public String toString() {
		return name;
	}
	
	public int getGroupId() {
		return groupId;
	}
	
	public int getCategoryId() {
		return catId;
	}
}
