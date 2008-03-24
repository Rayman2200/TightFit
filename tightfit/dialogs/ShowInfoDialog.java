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

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

import tightfit.item.Item;
import tightfit.widget.CustomListRenderer;
import tightfit.widget.InfoListEntry;

public class ShowInfoDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private Item myItem;
	
	public ShowInfoDialog(Frame parent, Item item) {
		super();
		
		setTitle("Show Info - "+item.name);
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
        
    	panel.setPreferredSize(new Dimension(300, 300));
    	
        panel.add(new JLabel(new ImageIcon(myItem.getImage())), c);
        c.gridx=1;
        panel.add(new JLabel(myItem.name), c);
        
        c.gridy = 1;
        c.gridx = 0;
        c.weightx = 2;
        c.weighty = 1;
        c.gridwidth=2;
        
        JTabbedPane pane = new JTabbedPane();
        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 250));
        scrollPane.getViewport().setView(new JTextArea(myItem.getAttribute("description", "No description in database")));
        ((JTextArea)scrollPane.getViewport().getView()).setEditable(false);
        ((JTextArea)scrollPane.getViewport().getView()).setLineWrap(true);
        pane.addTab("Description", scrollPane);
        
        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 250));
        scrollPane.getViewport().setView(buildAttributeList());
        pane.addTab("Attributes", scrollPane);
        
        
        panel.add(pane, c);
    	add(panel);
	}
	
	private JList buildAttributeList() {
		JList list = new JList();
		list.setCellRenderer(new CustomListRenderer());
		
		list.add(new InfoListEntry(myItem, "capacity", null));
		list.add(new InfoListEntry(myItem, "volume", null));
		list.add(new InfoListEntry(myItem, "mass", null));
		
		//if(item.)
		
		return list;
	}
}
