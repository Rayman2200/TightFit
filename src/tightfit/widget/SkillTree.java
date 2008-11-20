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
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.tree.*;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.widget.ui.SkillTreeNode;

public class SkillTree extends JTree {
	
	private Font bigFont;
	
	private Image checkImg, xImg, circleImg;
	
	public SkillTree(TreeNode n) {
		super(n);
		Font big;
		try {
			big = Resources.getFont("stan07_57.ttf");
			bigFont = big.deriveFont(8f);
			
			checkImg = Resources.getImage("checkmark.png").getScaledInstance(10, 10, Image.SCALE_SMOOTH);
			xImg = Resources.getImage("x.png").getScaledInstance(10, 10, Image.SCALE_SMOOTH);
			circleImg = Resources.getImage("circle.png").getScaledInstance(10, 10, Image.SCALE_SMOOTH);
		} catch (Exception e) {
		}
	}
	
	public Dimension getPreferredSize() {
		int w=0, h=0;
		
		TreeNode n = (TreeNode)treeModel.getRoot();

		h = countEntries(n.children())*20;
		
		return new Dimension(100,h);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		
		//setup for render
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
						RenderingHints.VALUE_RENDER_QUALITY);
		
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, 
						RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		g2d.setFont(bigFont);
		
		//get the root
		DefaultMutableTreeNode n = (DefaultMutableTreeNode)treeModel.getRoot();
		
		renderEntries(g2d, n.children(), 0, 10);
	}
	
	private int countEntries(Enumeration c) {
		int i = 0;
		while(c.hasMoreElements()) {
			TreeNode n = (TreeNode) c.nextElement();
			
			if(!n.isLeaf()) {
				i += countEntries(n.children());
			}
			
			i++;
		}
		
		return i;
	}
	
	private int renderEntries(Graphics2D g, Enumeration c, int depth, int y) {
		while(c.hasMoreElements()) {
			TreeNode n = (TreeNode) c.nextElement();
			
			int x = depth * 10;
			
			if(n instanceof SkillTreeNode) {
				g.setColor(Color.gray);
				g.fillRect(0, y-10, getWidth(), 20);
				
				String skillname = ""+((SkillTreeNode)n).getSkill().typeId;
				if(TightFit.getInstance().getChar().hasRequiredSkill(skillname, ((SkillTreeNode)n).getLevel())) {
					g.drawImage(checkImg, x, y-5, null);
				} else if (TightFit.getInstance().getChar().hasRequiredSkill(skillname, 0)) {
					g.drawImage(circleImg, x, y-5, null);
				} else {
					g.drawImage(xImg, x, y-5, null);
				}
			} else if(n instanceof DefaultMutableTreeNode) {
				g.setColor(Color.darkGray);
				g.fillRect(0, y-10, getWidth(), 20);
			}
			
			g.setColor(Color.white);
			g.drawString(n.toString(), x+15, y+4);

			y += 20;
			
			g.setColor(Color.black);
			g.drawLine(0, y-11, getWidth(), y-11);
			
			if(!n.isLeaf()) {
				y = renderEntries(g, n.children(), depth+1, y);
			}
		}
		
		return y;
	}
}
