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

import javax.swing.tree.DefaultMutableTreeNode;

import tightfit.character.*;

public class SkillTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;
    
    private Skill skill;
    
    public SkillTreeNode(Skill s) {
        skill = s;
    }
    
    public String toString() {
		return skill.name;
	}
}
