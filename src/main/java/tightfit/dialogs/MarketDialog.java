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
import javax.swing.table.*;

import tightfit.*;
import tightfit.actions.*;
import tightfit.item.*;
import tightfit.module.Module;
import tightfit.ship.Ship;
import tightfit.widget.*;
import tightfit.widget.ui.*;

public class MarketDialog extends JDialog implements TreeSelectionListener, 
								DragSourceListener, DragGestureListener, MouseListener, ActionListener {
	private static final long serialVersionUID = 1L;

	private TightFit editor;
	private JTree marketTree;
    private DefaultMutableTreeNode top;
    private Database myDb; 
    private JList groups, searchList;
    private JTable itemTable;
    private MemoryList quickList;
    private DragSource ds;
    private JScrollPane sp;
    private JTextField searchField;
    
    public MarketDialog(TightFit editor) {
    	super((Frame)null, Resources.getString("dialog.market.title"), false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        top =
            new DefaultMutableTreeNode(Resources.getString("dialog.market.galactic.loading"));
        
        this.editor = editor;
        
        marketTree = new JTree(top);
        marketTree.addTreeSelectionListener(this);
        marketTree.setOpaque(true);
        
        try {
            marketTree.setFont(Resources.getFont("bmmini.ttf").deriveFont(8f));
        } catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		init();
		
		ds = new DragSource();
	    ds.createDefaultDragGestureRecognizer(groups, DnDConstants.ACTION_COPY, this);
	    ds.createDefaultDragGestureRecognizer(quickList, DnDConstants.ACTION_COPY, this);
	    
        pack();
        setVisible(true);
    }

    public void updateTree(Database db) {
        top.setUserObject(Resources.getString("dialog.market.galactic"));
        
        myDb = db;
        
        createNodes(top, myDb);
        repaint();
    }
    
    private void init() {
    	JPanel panel = new JPanel(new GridBagLayout());
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
        
        //GROUPS
        JScrollPane mtsp = new JScrollPane();
        mtsp.getViewport().setView(marketTree);
        mtsp.setPreferredSize(new Dimension(200, 350));
        mtsp.setBackground(new Color(.07f, .25f, .43f));
        jtp.addTab(Resources.getString("dialog.market.tab.list"), mtsp);
        
        //SEARCH
        JPanel search = new JPanel();
        search.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1; c.weighty = 0;
        c.gridx = 0; c.gridy = 0;
        c.insets = new Insets(5, 0, 0, 0);
        searchField = new JTextField(10);
        search.add(searchField, c);
        c.gridx=1;
        JButton b = new JButton(Resources.getString("dialog.market.tab.search"));
        b.setActionCommand("search");
        b.addActionListener(this);
        search.add(b, c);
        c.gridx = 0; c.gridy = 1;
        c.gridwidth=2;
        c.gridheight=2;
        c.weightx = 1; c.weighty = 5;
        searchList = new JList();
        searchList.setCellRenderer(new QuickListRenderer());
        searchList.addMouseListener(this);
        JScrollPane ssp = new JScrollPane();
        ssp.getViewport().setView(searchList);
        search.add(ssp, c);
        jtp.addTab(Resources.getString("dialog.market.tab.search"), search);
        
        //QUICKLIST
        quickList = new MemoryList("quicklist");
        quickList.setCellRenderer(new QuickListRenderer());
        quickList.addMouseListener(this);
        replay("quicklist");
        JScrollPane qsp = new JScrollPane();
        qsp.getViewport().setView(quickList);
        jtp.addTab(Resources.getString("dialog.market.tab.quicklist"), qsp);
        
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitPane.setResizeWeight(0.25);
        splitPane.setLeftComponent(jtp);
        splitPane.setRightComponent(sp);
        
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
        		//else System.err.println("failed adding "+g.name);
        	}
        }
        
    }
    
    public void valueChanged(TreeSelectionEvent e) {
		if(e.getPath().getLastPathComponent() instanceof MarketTreeNode) {
			MarketTreeNode mtn = (MarketTreeNode)e.getPath().getLastPathComponent();
			
			buildList(mtn.getGroupId());
			repaint();
		}
	}
    
    private void buildList(int groupId) {
    	Vector list = new Vector();
    	
    	Iterator itr = myDb.getTypeByMarketGroup(groupId).iterator();
    	if(itr.hasNext()) {
    		groups.removeAll();
            while(itr.hasNext()) {
                boolean inserted = false;
                Item i = (Item)itr.next();
                MarketListEntry mle = new MarketListEntry(i, editor);
                if(!list.isEmpty()) {
                    for(int in=0;in<list.size();in++) {
                        MarketListEntry e = (MarketListEntry) list.get(in);
                        if(e != null) {
                            if(e.getItem().name.compareTo(i.name) >= 0) {
                                list.insertElementAt(mle, in);
                                inserted = true;
                                break;
                            }
                        } else {
                            list.add(in, mle);
                            inserted = true;
                            break;
                        }
                    }
                    
                    if(!inserted) {
                        list.add(mle);
                    }
                } else {
                    list.add(mle);
                }
            }
            groups.setListData(list);
            groups.setSelectedIndex(0);
            sp.getViewport().setView(groups);
        } else {
            ArrayList<String> cols = new ArrayList<String>();
            cols.add("name"); cols.add("volume"); cols.add("metaLevel");
            
            Item type = findTypeBySuperGroup(groupId);
            if(type.isAmmo()) {
                cols.add("emDamage"); cols.add("kineticDamage"); cols.add("explosiveDamage"); cols.add("thermalDamage");
            } else if(type.isShip()) {
                cols.add("powerOutput"); cols.add("cpuOutput"); cols.add("hiSlots"); cols.add("medSlots"); cols.add("lowSlots"); cols.add("maxVelocity");
            }
            
            TableModel model = new ItemTableModel(groupId, (String[])cols.toArray(new String[1]));
            itemTable = new JTable(model);
            itemTable.addMouseListener(this);
            itemTable.setShowHorizontalLines(false);
            itemTable.setAutoCreateColumnsFromModel(false);
            itemTable.getTableHeader().addMouseListener(this);
            
            //Java 6 way
            //itemTable.setAutoCreateRowSorter(true);
            
            sp.getViewport().setView(itemTable);
        }
    }
    
    private JPopupMenu buildPopup(Item item) {
    	JPopupMenu menu = new JPopupMenu();
    	JMenuItem mitem;
    	
		menu.add(new JMenuItem(new ShowInfoAction(editor.appFrame, item)));
		menu.addSeparator();
    	if(!item.isShip()) {
            menu.add(new JMenuItem(new FitToShipAction(editor.getShip(), quickList, item)));
		} else {
			menu.add(new JMenuItem(new MakeActiveAction(quickList, item)));
		}
    	
    	return menu;
    }
    
    /*private class MarketTransferHandler extends TransferHandler {
		private static final long serialVersionUID = 1L;

		public Icon getVisualRepresentation(Transferable t) {
    		try {
				return new ImageIcon(((Module)((ModuleTransferHandler)t).getTransferData(new DataFlavor(Module.class, "Module"))).getImage());
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
            if(comp != quickList) {
	            Item item = ((MarketListEntry)comp.getSelectedValue()).getItem();
	            quickList.remember(item);
            }
        }
	}

	public void dragGestureRecognized(DragGestureEvent e) {
		JList comp = (JList)e.getComponent();
		Item item;
		if(comp.getSelectedValue() instanceof MarketListEntry)
			item = ((MarketListEntry)comp.getSelectedValue()).getItem();
		else item = (Item)comp.getSelectedValue();
		
		//comp.getTransferHandler().exportAsDrag(comp, e.getTriggerEvent(), TransferHandler.COPY);
		Transferable transferable = new ModuleTransferHandler(new Module(item));
		
		if(DragSource.isDragImageSupported())
			e.startDrag(DragSource.DefaultCopyDrop, item.getImage(), new Point(-5,-5), transferable, this);
		else e.startDrag(DragSource.DefaultCopyDrop, transferable, this);
	}

	public void mouseClicked(MouseEvent e) {
		Object what = e.getComponent();
		if(what instanceof JList) {
			JList comp = (JList)what;
			Item item;
			if(comp.getSelectedValue() != null) {
				if(comp.getSelectedValue() instanceof MarketListEntry) {
					item = ((MarketListEntry) comp.getSelectedValue()).getItem();
				} else {
					item = (Item) comp.getSelectedValue();
				}
				
				if(e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
		            //groups.setSelectedIndex(groups.getFirstVisibleIndex() + pt.y/225);
					
		            Point rel = comp.getLocation();
					//buildPopup(item).show(this, pt.x+rel.x+300, pt.y+rel.y);
					Point pt = MouseInfo.getPointerInfo().getLocation();
					buildPopup(item).show(this, pt.x, pt.y);
				} else if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					if(item.getAttribute("lowSlots", "-1").equals("-1")) {
	                    (new FitToShipAction(editor.getShip(), quickList, item)).actionPerformed(null); //yikes- reusability trumps elegance...
					} else {
						editor.setShip(new Ship(item));
		                quickList.remember(item);
					}
				}
			}
		} else if(what instanceof JTable) {
			JTable itemTable = (JTable)what;
			
			if(e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
				ItemTableModel model = (ItemTableModel) itemTable.getModel();
				
				Point pt = MouseInfo.getPointerInfo().getLocation();
				buildPopup(model.getItemAt(itemTable.getSelectedRow())).show(this, pt.x, pt.y);
			}
		} else if (what instanceof JTableHeader) {
			JTableHeader h = (JTableHeader)what;
			ItemTableModel model = (ItemTableModel) itemTable.getModel();
			Point pt = new Point(e.getX(), e.getY());
			int i;
			
			for(i = 0; i < model.getColumnCount(); i++) {
				if(h.getHeaderRect(i).contains(pt)) {
					break;
				}
			}
			model.sort(i);
			itemTable.repaint();
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
            if(ae.getActionCommand().equals("search")) {
                String nom = searchField.getText();
                if(nom.matches("[\\p{Graph}]+")) {
                    LinkedList list = Database.getInstance().getTypeByName(nom);
                    searchList.setListData(list.toArray());
                }
            }
		} catch (Exception e) {
            e.printStackTrace();
		}
	}
    
    private void replay(String name) {
        int count = Integer.parseInt(TightPreferences.node("memory").node(name).get("memcount", "0"));
        
        try {
            for(int i = 0; i < count; i++) {
                String in = TightPreferences.node("memory").node(name).get("mem"+i, "");
                //System.out.println(in);
                quickList.insert(new Item(Database.getInstance().getType(in)));
            }
        } catch(Exception e) {}
    }
    
    private Item findTypeBySuperGroup(int parent) {
        try {
            LinkedList list = myDb.getMarketGroupByParent(parent);
        
            if(list.size() > 0) {
                Iterator itr = list.iterator();
                while(itr.hasNext()) {
                    MarketGroup g = (MarketGroup)itr.next();
                    return findTypeBySuperGroup(g.groupId);
                }
            } else {
                LinkedList itm = myDb.getTypeByMarketGroup(parent);
                Iterator itr = itm.iterator();
                return (Item)itr.next();
            }
        } catch(Exception e) {}
        return new Item();
    }

}
