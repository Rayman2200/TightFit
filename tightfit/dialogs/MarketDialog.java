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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.actions.ShowInfoAction;
import tightfit.item.*;
import tightfit.module.Module;
import tightfit.widget.*;

public class MarketDialog extends JDialog implements TreeSelectionListener, 
								DragSourceListener, DragGestureListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private TightFit editor;
	private JTree marketTree;
    private DefaultMutableTreeNode top;
    private Database myDb; 
    private JList groups;
    private DragSource ds;
    
    public MarketDialog(TightFit editor) {
    	super(editor.appFrame);
        top =
            new DefaultMutableTreeNode("Galactic Market (Loading...)");
        
        this.editor = editor;
        
        marketTree = new JTree(top);
        marketTree.addTreeSelectionListener(this);
        marketTree.setOpaque(false);
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
	    ds.createDefaultDragGestureRecognizer(groups, DnDConstants.ACTION_COPY, this);
		
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
        	groups.setFont(Resources.getFont("stan07_55.ttf").deriveFont(6f));
		} catch (Exception e) {
		}
        JScrollPane sp = new JScrollPane();
        sp.getViewport().setView(groups);
        sp.setPreferredSize(new Dimension(550, 350));
        groups.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //groups.setDragEnabled(true);
        //groups.setTransferHandler(new MarketTransferHandler());
        groups.addMouseListener(this);
        
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
    	HashMap nodes = new HashMap();
    	DefaultMutableTreeNode node;
    	
    	Iterator itr = db.getMarketGroups();
        while(itr.hasNext()) {
        	MarketGroup g = (MarketGroup)itr.next();
        	node = new MarketTreeNode(g.name, g.catId, g.groupId);
        	nodes.put(""+g.groupId, node);
        }
        
        //run it through again to build up the tree
        itr = db.getMarketGroups();
        while(itr.hasNext()) {
        	MarketGroup g = (MarketGroup)itr.next();
        	node = (MarketTreeNode) nodes.get(""+g.groupId);
        	if(g.parentGroupId == 0) {
        		top.add(node);
        	} else {
        		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) nodes.get(""+g.parentGroupId);
        		if(parent != null)
        			parent.add(node);
        		else System.err.println("failed adding "+g.name);
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
    	
    	Iterator itr = myDb.getTypeByMarketGroup(groupId).iterator();
    	while(itr.hasNext()) {
    		Item i = (Item)itr.next();
    		MarketListEntry mle = new MarketListEntry(i);
    		list.add(mle);
    	}
    	
        groups.setListData(list);
        groups.setSelectedIndex(0);
    }
    
    private JPopupMenu buildPopup(Item item) {
    	JPopupMenu menu = new JPopupMenu();
    	JMenuItem mitem;
    	
    	if(item.getAttribute("lowSlots", "-1").equals("-1")) {
    		menu = new JPopupMenu();
            menu.add(new JMenuItem(new ShowInfoAction(this, item)));
            menu.addSeparator();
            mitem = new JMenuItem("Fit to Active Ship");
            //if(!editor.getShip().hasFreeSlot(item.slotType))
            //	mitem.setEnabled(false);
            menu.add(mitem);
		} else {
			menu = new JPopupMenu();
			menu.add(new JMenuItem(new ShowInfoAction(this, item)));
			menu.addSeparator();
			menu.add(new JMenuItem("Make Active"));
		}
    	
    	return menu;
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
		
		if(DragSource.isDragImageSupported())
			e.startDrag(DragSource.DefaultCopyDrop, item.getImage(), new Point(-5,-5), transferable, this);
		else e.startDrag(DragSource.DefaultCopyDrop, transferable, this);
	}

	public void mouseClicked(MouseEvent e) {
		if(e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
			MarketListEntry mle = (MarketListEntry) groups.getSelectedValue();
			Point pt = getMousePosition();
			Item item = mle.getItem();
			buildPopup(item).show(this, pt.x, pt.y);
		}
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
