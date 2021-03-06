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
import tightfit.module.Module;
import tightfit.ship.Ship;
import tightfit.widget.*;
import tightfit.widget.ui.CustomListRenderer;
import tightfit.widget.ui.SkillTreeNode;
import tightfit.character.Skill;

public class ShowInfoDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private Item myItem;
	
	private Font big;
	
	public ShowInfoDialog(Frame parent, Item item) {
		super();
		
		setTitle(Resources.getString("dialog.showinfo.title")+" - "+item.name);
		myItem = item;
		
		try {
			big = Resources.getFont("agencyr.ttf").deriveFont(Font.BOLD, 20f);
		} catch (Exception e) {
		}
		
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
        
    	panel.setPreferredSize(new Dimension(350, 400));
    	panel.setBackground(Color.decode(TightPreferences.node("prefs").get("bgColor", "#30251A")));
        panel.add(new JLabel(new ImageIcon(myItem.getImage())), c);
        c.gridx=1;
        JLabel l = new JLabel("   "+myItem.name);
        l.setFont(big);
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
        scrollPane.getViewport().setView(new JTextArea(myItem.getAttribute("description", Resources.getString("dialog.market.nodata"))+"graphicId: "+myItem.graphicId+"\ntypeId: "+myItem.typeId+"\ngroupId: "+myItem.groupId));
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
        
        if(myItem.isShip() || myItem.isFittable()) {
        	scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.getViewport().setView(buildFittingList());
            pane.addTab("Fitting", scrollPane);
        }
        
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
    
	private JList buildFittingList() {
		JList attribs = new JList();
		Vector<InfoListEntry> list = new Vector<InfoListEntry>();
        attribs.setCellRenderer(new CustomListRenderer());
		
        if(myItem.isShip()) {
        	try {
        		Ship s = new Ship(myItem);
        		list.add(new InfoListEntry(myItem, "cpuOutput", "CPU OUTPUT", null));
        		list.add(new InfoListEntry(myItem, "powerOutput", "POWERGRID", null));
        		list.add(new InfoListEntry(myItem, "upgradeCapacity", "CALIBRATION", null));
        		list.add(new InfoListEntry(""+s.totalSlots(Module.LOW_SLOT), "LOW SLOTS", Resources.getImage("icon08_09.png").getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        		list.add(new InfoListEntry(""+s.totalSlots(Module.MID_SLOT), "MEDIUM SLOTS", Resources.getImage("icon08_10.png").getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        		list.add(new InfoListEntry(""+s.totalSlots(Module.HI_SLOT), "HIGH SLOTS", Resources.getImage("icon08_11.png").getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        		if(s.countFreeLauncherHardpoints()>0)
        			list.add(new InfoListEntry(""+s.countFreeLauncherHardpoints(), "LAUNCHER HARDPOINTS", Resources.getImage("icon12_12.png").getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        		if(s.countFreeTurretHardpoints()>0)
        			list.add(new InfoListEntry(""+s.countFreeTurretHardpoints(), "TURRET HARDPOINTS", Resources.getImage("icon12_09.png").getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        		list.add(new InfoListEntry(""+s.totalSlots(Module.RIG_SLOT), "UPGRADE SLOTS", Resources.getImage("icon68_01.png").getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        	} catch(Exception e) {}
        } else {
        	try {
        		Module m = new Module(myItem);
        		String req = "Must be installed into an available rig slot on a ship.";
        		String slot = "RIG SLOT";
        		String icon = "icon68_01.png";
        		
        		switch(m.slotRequirement) {
        			case Module.LOW_SLOT:
        				req = "Requires a low power slot.";
        				slot = "LOW POWER";
        				icon = "icon08_09.png";
        				break;
        			case Module.MID_SLOT:
        				req = "Requires a medium power slot.";
        				slot = "MEDIUM POWER";
        				icon = "icon08_10.png";
        				break;
        			case Module.HI_SLOT:
        				req = "Requires a high power slot.";
        				slot = "HIGH POWER";
        				icon = "icon08_11.png";
        				break;
        		}
        		
	        	list.add(new InfoListEntry(req, slot, Resources.getImage(icon).getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
	        	list.add(new InfoListEntry(myItem, "cpu", "CPU", Resources.getImage("icon12_07.png").getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
	        	list.add(new InfoListEntry(myItem, "power", "POWERGRID", Resources.getImage("icon02_07.png").getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        	}catch(Exception e) {}
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
    
    private JList buildVariations() {
    	JList list = new JList();
    	
    	return list;
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
