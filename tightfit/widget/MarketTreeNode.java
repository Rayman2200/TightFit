package tightfit.widget;

import javax.swing.tree.DefaultMutableTreeNode;

public class MarketTreeNode extends DefaultMutableTreeNode {

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
