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

import java.io.IOException;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import tightfit.Resources;
import tightfit.item.Database;
import tightfit.item.Group;
import tightfit.item.Item;
import tightfit.module.Module;
import tightfit.widget.*;

public class MarketDialog extends JDialog implements TreeSelectionListener, 
								DragSourceListener, DragGestureListener {

    private JTree marketTree;
    private DefaultMutableTreeNode top;
    private Database myDb; 
    private JList groups;
    private DragSource ds;
    
    public MarketDialog(JFrame parent) {
        top =
            new DefaultMutableTreeNode("Galactic Market (Loading...)");
        
        marketTree = new JTree(top);
        marketTree.addTreeSelectionListener(this);
        setTitle("EVE Market");
        
        try {
            marketTree.setFont(Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("stan07_57.ttf")).deriveFont(8f));
        } catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		init();
		
		ds = new DragSource();
	    DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(groups,
	        DnDConstants.ACTION_COPY, this);
		
        pack();
        setVisible(true);
    }

    public void updateTree(Database db) {
        top.setUserObject("Galactic Market");
        
        myDb = db;
        
        createNodes(top, myDb);
        repaint();
    }
    
    private void init() {
    	JPanel panel = new JPanel(new GridLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	
        c.gridx = 0;
        c.gridy = 0;
        
        groups = new JList();
        groups.setCellRenderer(new ModuleListRenderer());
        //groups.addListSelectionListener(this);
        try {
			groups.setFont(Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("stan07_57.ttf")).deriveFont(8f));
		} catch (Exception e) {
		}
        JScrollPane sp = new JScrollPane();
        sp.getViewport().setView(groups);
        sp.setPreferredSize(new Dimension(550, 350));
        groups.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //groups.setDragEnabled(true);
        //groups.setTransferHandler(new MarketTransferHandler());
        
        JScrollPane mtsp = new JScrollPane();
        mtsp.getViewport().setView(marketTree);
        mtsp.setPreferredSize(new Dimension(200, 350));
        mtsp.setBackground(new Color(.07f, .25f, .43f));
        
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitPane.setResizeWeight(0.25);
        splitPane.setLeftComponent(mtsp);
        splitPane.setRightComponent(sp);
        
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1; c.weighty = 1;
        panel.add(splitPane, c);
        c.weightx = 0; c.weighty = 0; c.gridy = 1;
        //panel.add(buttons, c);
        
        add(panel);
    }
    
    private void createNodes(DefaultMutableTreeNode top, Database db) {
    	DefaultMutableTreeNode ships = new DefaultMutableTreeNode("Ships");
    	DefaultMutableTreeNode ammo = new DefaultMutableTreeNode("Ammunition & Charges");
    	DefaultMutableTreeNode equip = new DefaultMutableTreeNode("Ship Equipment");
    	DefaultMutableTreeNode turrets = new DefaultMutableTreeNode("Turrets & Bays");
    	DefaultMutableTreeNode hybridammo = new DefaultMutableTreeNode("Hybrid Charges");
    	DefaultMutableTreeNode ecm = new DefaultMutableTreeNode("Electronic Warfare");
    	
    	//major categories
    	top.add(ammo);
    	top.add(equip);
    	top.add(ships);
    	
    	equip.add(turrets);
    	equip.add(ecm);
    	ammo.add(hybridammo);
    	
    	Iterator itr = db.getGroups();
        while(itr.hasNext()) {
        	Group g = (Group)itr.next();
        	if(g.catId >= 6 && g.catId <= 8) {
        		//top.add(new DefaultMutableTreeNode(g.name));
        		//System.out.println("group "+g.catId+"_"+g.groupId);
        		if(g.groupId == 201 || g.groupId == 208)
        			ecm.add(new MarketTreeNode(g.name, g.catId, g.groupId));
        		else if(g.groupId == 255)
        			turrets.add(new MarketTreeNode(g.name, g.catId, g.groupId));
        		else if(g.groupId > 24 && g.groupId < 32)
        			ships.add(new MarketTreeNode(g.name, g.catId, g.groupId));
        		else if(g.groupId == 85)
        			hybridammo.add(new MarketTreeNode(g.name, g.catId, g.groupId));
        	}
        }
    }
    
    public void valueChanged(TreeSelectionEvent e) {
		if(e.getPath().getLastPathComponent() instanceof MarketTreeNode) {
			MarketTreeNode mtn = (MarketTreeNode)e.getPath().getLastPathComponent();
			
			//System.out.println(mtn.name + mtn.getCategoryId()+"_"+mtn.getGroupId());
			buildList(mtn.getGroupId());
			repaint();
		}
	}
    
    private void buildList(int groupId) {
    	Vector list = new Vector();
    	groups.removeAll();
    	
    	Iterator itr = myDb.getTypeByGroup(groupId).iterator();
    	while(itr.hasNext()) {
    		Item i = (Item)itr.next();
    		list.add(new MarketListEntry(i));
    	}
    	
        groups.setListData(list);
    }
    
    private class MarketTransferHandler extends TransferHandler {
		private static final long serialVersionUID = 1L;

		public Icon getVisualRepresentation(Transferable t) {
    		try {
				return new ImageIcon(((Module)((ModuleTransferable)t).getTransferData(new DataFlavor(Module.class, "Module"))).getImage());
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
    	}
    }

	public void dragEnter(DragSourceDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dragOver(DragSourceDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dropActionChanged(DragSourceDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dragExit(DragSourceEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dragDropEnd(DragSourceDropEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dragGestureRecognized(DragGestureEvent e) {
		JList comp = (JList)e.getComponent();
		Item item = ((MarketListEntry)comp.getSelectedValue()).getItem();
		Transferable transferable = new ModuleTransferable(new Module(item));
		
		//System.out.println("Yay for DnD!");
		
		//if(DragSource.isDragImageSupported())
			e.startDrag(DragSource.DefaultCopyDrop, item.getImage(), new Point(-5,-5), transferable, this);
		//else e.startDrag(DragSource.DefaultCopyDrop, transferable, this);
	}
}
