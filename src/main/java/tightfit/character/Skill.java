/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.character;

import tightfit.TightFit;
import tightfit.item.Database;
import tightfit.item.Item;
import tightfit.util.MultinaryTree;

public class Skill extends Item {

	private int level;
    
	public Skill() {
	}
    
	public Skill(String id, int level) throws Exception {
		super(Database.getInstance().getTypeById(id));
		
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
    
    public float getBonus() {
        try {
            String [] key = getAttributeKey("Bonus");
            for(int i = 0;i<key.length;i++)
            	if(key[i].length() < 28)   //FIXME: at least the other ones are extremely long...
            		return Float.parseFloat(getAttribute(key[i], "0.0"));
        } catch (Exception e) {
        }
        return 1.0f;
    }
    
    public MultinaryTree buildSkillTree() {
    	MultinaryTree tree = new MultinaryTree();
    	
    	for(int i=1;i<=3;i++) {
            if(hasAttribute("requiredSkill"+i)) {
            	MultinaryTree.MultinaryTreeNode n = tree.addChild(TightFit.getInstance().getChar().getSkill(getAttribute("requiredSkill"+i,"0")));
            	addChildren(tree, n);
            }
    	}
    	
    	return tree;
    }
    
    private void addChildren(MultinaryTree t, MultinaryTree.MultinaryTreeNode n) {
    	Skill s = (Skill) n.data;
    	for(int i=1;i<=3;i++) {
            if(s.hasAttribute("requiredSkill"+i)) {
            	MultinaryTree.MultinaryTreeNode m = t.addChild(n, TightFit.getInstance().getChar().getSkill(s.getAttribute("requiredSkill"+i,"0")));
            	addChildren(t, m);
            }
    	}
    }
}
