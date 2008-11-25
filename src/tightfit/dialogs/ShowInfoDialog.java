/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.dialogs;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

import java.util.Iterator;
import java.util.Vector;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.TightPreferences;
import tightfit.item.Item;
import tightfit.widget.*;
import tightfit.widget.ui.CustomListRenderer;
import tightfit.widget.ui.SkillTreeNode;
import tightfit.character.Skill;

public class ShowInfoDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private Item myItem;
	
	public ShowInfoDialog(Frame parent, Item item) {
		super();
		
		setTitle(Resources.getString("dialog.showinfo.title")+" - "+item.name);
		myItem = item;
		
		init();
		
		pack();
        setVisible(true);
	}
	
	private void init() {
		JPanel panel = new JPanel(new GridBagLayout());
		JScrollPane scrollPane;
    	GridBagConstraints c = new GridBagConstraints();
    	c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        
    	panel.setPreferredSize(new Dimension(300, 350));
    	panel.setBackground(Color.decode(TightPreferences.node("prefs").get("bgColor", "#30251A")));
        panel.add(new JLabel(new ImageIcon(myItem.getImage())), c);
        c.gridx=1;
        JLabel l = new JLabel("   "+myItem.name);
        l.setForeground(Color.WHITE);
        panel.add(l, c);
        
        c.gridy = 1;
        c.gridx = 0;
        c.weightx = 2;
        c.weighty = 1;
        c.gridwidth=2;
        
        JTabbedPane pane = new JTabbedPane();
        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 250));
        scrollPane.getViewport().setView(new JTextArea(myItem.getAttribute("description", Resources.getString("dialog.market.nodata"))+myItem.graphicId+"\n"+myItem.typeId));
        ((JTextArea)scrollPane.getViewport().getView()).setEditable(false);
        ((JTextArea)scrollPane.getViewport().getView()).setLineWrap(true);
        ((JTextArea)scrollPane.getViewport().getView()).setBackground(Color.DARK_GRAY);
        ((JTextArea)scrollPane.getViewport().getView()).setForeground(Color.WHITE);
        try {
        	((JTextArea)scrollPane.getViewport().getView()).setFont(Resources.getFont("stan07_55.ttf").deriveFont(8f));
        } catch(Exception e){}
        pane.addTab("Description", scrollPane);
        
        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 250));
        scrollPane.getViewport().setView(buildAttributeList());
        pane.addTab("Attributes", scrollPane);
        
        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setView(buildSkillList());
        pane.addTab("Prerequisites", scrollPane);
        
        panel.add(pane, c);
    	add(panel);
	}
	
	private JList buildAttributeList() {
		JList attribs = new JList();
		Vector<InfoListEntry> list = new Vector<InfoListEntry>();
        attribs.setCellRenderer(new CustomListRenderer());
		
		list.add(new InfoListEntry(myItem, "capacity", null));
		list.add(new InfoListEntry(myItem, "volume", null));
		list.add(new InfoListEntry(myItem, "mass", null));
		
		Iterator<String> itr = myItem.attributes.keySet().iterator();
		while(itr.hasNext()) {
			String key = itr.next();
			list.add(new InfoListEntry(myItem, key, null));
		}
		
        attribs.setListData(list);
        attribs.setBackground(Color.DARK_GRAY);
        
		return attribs;
	}
    
    private JTree buildSkillList() {
       DefaultMutableTreeNode root = new DefaultMutableTreeNode("Required Skills");
       SkillTree skills = new SkillTree(root);
       
       String tier[] = {"","Primary", "Secondary", "Tertiary"};
       
       skills.setShowsRootHandles(false);
       skills.setRootVisible(true);
       
       for(int i=1;i<=3;i++) {
           if(myItem.hasAttribute("requiredSkill"+i)) {
               DefaultMutableTreeNode parent = new DefaultMutableTreeNode(tier[i]+" Skill");
               root.add(parent);
               Skill skill = TightFit.getInstance().getChar().getSkill(myItem.getAttribute("requiredSkill"+i,"0"));
               int rl = Integer.parseInt(myItem.getAttribute("requiredSkill"+i+"Level","1"));
               SkillTreeNode n = new SkillTreeNode(skill, rl);
               parent.add(n);
               addChildrenSkills(skills, n, skill);
           }
       }
       
       return skills;
    }
    
    private void addChildrenSkills(JTree t, DefaultMutableTreeNode parent, Skill skill) {
    	for(int i=1;i<=3;i++) {
            if(skill.hasAttribute("requiredSkill"+i)) {
            	Skill mySkill = TightFit.getInstance().getChar().getSkill(skill.getAttribute("requiredSkill"+i,"0"));
            	int rl = Integer.parseInt(skill.getAttribute("requiredSkill"+i+"Level","1"));
            	SkillTreeNode n = new SkillTreeNode(mySkill, rl);
                parent.add(n);
                addChildrenSkills(t, n, mySkill);
            }
        }
    }
}