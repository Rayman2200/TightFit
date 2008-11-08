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

import tightfit.character.*;

public class SkillTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;
    
	private static String level[] = {"", "I", "II", "III", "IV", "V"};
	
    private Skill skill;
    private int requiredLevel;
    private boolean header;
    
    public SkillTreeNode(Skill s, int reqLevel) {
        skill = s;
        requiredLevel = reqLevel;
    }
    
    public SkillTreeNode(Skill s, int reqLevel, boolean h) {
    	skill = s;
    	requiredLevel = reqLevel;
    	header=h;
    }
    
    public String toString() {
		return skill.name+" "+level[requiredLevel];
	}
}
