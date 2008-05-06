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
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import tightfit.ship.Ship;
import tightfit.widget.*;

public class MarketDialog extends JDialog implements TreeSelectionListener, 
								DragSourceListener, DragGestureListener, MouseListener, ActionListener {
	private static final long serialVersionUID = 1L;

	private TightFit editor;
	private JTree marketTree;
    private DefaultMutableTreeNode top;
    private Database myDb; 
    private JList groups;
    private MemoryList quickList;
    private DragSource ds;
    private JScrollPane sp;
    
    public MarketDialog(TightFit editor) {
    	super((Frame)null, "EVE Market", false);
        top =
            new DefaultMutableTreeNode("Galactic Market (Loading...)");
        
        this.editor = editor;
        
        marketTree = new JTree(top);
        marketTree.addTreeSelectionListener(this);
        marketTree.setOpaque(true);
        
        try {
            marketTree.setFont(Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("stan07_57.ttf")).deriveFont(8f));
        } catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
        groups.setCellRenderer(new CustomListRenderer());
        //groups.addListSelectionListener(this);
        try {
        	groups.setFont(Resources.getFont("stan07_55.ttf").deriveFont(6f));
		} catch (Exception e) {
		}
        sp = new JScrollPane();
        sp.getViewport().setView(groups);
        sp.setPreferredSize(new Dimension(550, 350));
        groups.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //groups.setDragEnabled(true);
        //groups.setTransferHandler(new MarketTransferHandler());
        groups.addMouseListener(this);
        
        JTabbedPane jtp = new JTabbedPane();
        
        JScrollPane mtsp = new JScrollPane();
        mtsp.getViewport().setView(marketTree);
        mtsp.setPreferredSize(new Dimension(200, 350));
        mtsp.setBackground(new Color(.07f, .25f, .43f));
        jtp.addTab("List", mtsp);
        
        JPanel search = new JPanel();
        search.add(new JTextField(10));
        JButton b = new JButton("Search");
        search.add(b);
        jtp.addTab("Search", search);
        
        quickList = new MemoryList();
        quickList.setCellRenderer(new QuickListRenderer());
        JScrollPane qsp = new JScrollPane();
        qsp.getViewport().setView(quickList);
        jtp.addTab("Quicklist", qsp);
        
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitPane.setResizeWeight(0.25);
        splitPane.setLeftComponent(jtp);
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
    	
    	Iterator itr = myDb.getTypeByMarketGroup(groupId).iterator();
    	if(itr.hasNext())
    		groups.removeAll();
    	while(itr.hasNext()) {
    		Item i = (Item)itr.next();
    		MarketListEntry mle = new MarketListEntry(i, editor);
    		list.add(mle);
    	}
    	
        groups.setListData(list);
        groups.setSelectedIndex(0);
    }
    
    private JPopupMenu buildPopup(Item item) {
    	JPopupMenu menu = new JPopupMenu();
    	JMenuItem mitem;
    	
		menu.add(new JMenuItem(new ShowInfoAction(editor.appFrame, item)));
		menu.addSeparator();
    	if(item.getAttribute("lowSlots", "-1").equals("-1")) {
            mitem = new JMenuItem("Fit to Active Ship");
            mitem.addActionListener(this);
            //if(!editor.getShip().hasFreeSlot(item.slotType))
            //	mitem.setEnabled(false);
            menu.add(mitem);
		} else {
			mitem = new JMenuItem("Make Active");
			mitem.addActionListener(this);
			menu.add(mitem);
		}
    	
    	return menu;
    }
    
    /*private class MarketTransferHandler extends TransferHandler {
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
    }*/

	public void dragEnter(DragSourceDragEvent arg0) {
	}

	public void dragOver(DragSourceDragEvent arg0) {
	}

	public void dropActionChanged(DragSourceDragEvent arg0) {
	}

	public void dragExit(DragSourceEvent arg0) {
	}

	public void dragDropEnd(DragSourceDropEvent e) {
        if(e.getDropSuccess()) {
            JList comp = (JList)e.getDragSourceContext().getComponent();
            Item item = ((MarketListEntry)comp.getSelectedValue()).getItem();
            System.out.println("boom");
            quickList.remember(item);
        }
	}

	public void dragGestureRecognized(DragGestureEvent e) {
		JList comp = (JList)e.getComponent();
		Item item = ((MarketListEntry)comp.getSelectedValue()).getItem();
		Transferable transferable = new ModuleTransferable(new Module(item));
		
		if(DragSource.isDragImageSupported())
			e.startDrag(DragSource.DefaultCopyDrop, item.getImage(), new Point(-5,-5), transferable, this);
		else e.startDrag(DragSource.DefaultCopyDrop, transferable, this);
	}

	public void mouseClicked(MouseEvent e) {
		MarketListEntry mle = (MarketListEntry) groups.getSelectedValue();
		if(e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
            //groups.setSelectedIndex(groups.getFirstVisibleIndex() + pt.y/225);
			
            Point rel = e.getComponent().getLocation();
			Item item = mle.getItem();
			//buildPopup(item).show(this, pt.x+rel.x+300, pt.y+rel.y);
			Point pt = MouseInfo.getPointerInfo().getLocation();
			buildPopup(item).show(this, pt.x, pt.y);
		} else if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
			if(mle.getItem().getAttribute("lowSlots", "-1").equals("-1")) {
				fitToShip(new Module(mle.getItem()));
			} else {
				editor.setShip(new Ship(mle.getItem()));
                quickList.remember(((MarketListEntry)groups.getSelectedValue()).getItem());
			}
		}
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void actionPerformed(ActionEvent ae) {
		try {
			if(ae.getActionCommand().equalsIgnoreCase("make active")) {
				editor.setShip(new Ship(Database.getInstance().getType(((MarketListEntry)groups.getSelectedValue()).getName())));
                quickList.remember(((MarketListEntry)groups.getSelectedValue()).getItem());
            } else if(ae.getActionCommand().equalsIgnoreCase("fit to active ship")) {
                fitToShip(new Module(Database.getInstance().getType(((MarketListEntry)groups.getSelectedValue()).getName())));
			}
		} catch (Exception e) {
		}
	}
	
	private void fitToShip(Module m) {
		Ship ship = editor.getShip();
        if(ship.hasFreeSlot(m)) {
            int t;
            if(m.slotRequirement == Module.LOW_SLOT) {
                t = ship.totalLowSlots();
            } else if(m.slotRequirement == Module.MID_SLOT) {
                t = ship.totalMedSlots();
            } else if(m.slotRequirement == Module.HI_SLOT) {
                t = ship.totalHiSlots();
            } else {
                t = ship.totalRigSlots();
            }
            for(int s=0;s<t;s++) {
                if(ship.testPutModule(m, m.slotRequirement, s)) {
                    ship.putModule(m, m.slotRequirement, s);
                    break;
                }
            }
        }
        quickList.remember(((MarketListEntry)groups.getSelectedValue()).getItem());
	}
}
