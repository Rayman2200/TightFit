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
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.*;

import tightfit.Resources;
import tightfit.module.Module;
import tightfit.module.Weapon;
import tightfit.item.*;
import tightfit.ship.Ship;
import tightfit.ship.ShipChangeListener;
import tightfit.widget.graph.*;

public class DpsPanel extends JPanel implements TightFitDropTargetPanel, ShipChangeListener, MouseListener, ActionListener {
	private static final long serialVersionUID = 1L;

	private int selected=-1;
	private Font bigFont;
	private JComboBox ammoType;
	private JList ammoListPanel;
	private JScrollPane sp;
	private Image dpsDmgImg, bgImg;
	private Ship memShip;
	private Color bgLightColor = new Color(1,1,1,0.25f);
	private DamageRangeGraph dpsGraph;
    
	public DpsPanel() {
		this.addMouseListener(this);
		
        memShip = new Ship();
        
		try {
			dpsDmgImg = Resources.getImage("icon13_01.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			bgImg = Resources.getImage("panelgrad.png");
			Font big = Resources.getFont("stan07_57.ttf");
	        bigFont = big.deriveFont(8f);
		} catch (Exception e) {
		}
		
		setLayout(new SlickLayout());
		ammoType = new JComboBox(new String[]{"Standard", "Advanced", "Faction"});
		add(ammoType);
		ammoType.setLocation(432,117);
		ammoType.setVisible(false);
		ammoType.addActionListener(this);
		
		sp = new JScrollPane();
		ammoListPanel = new JList();
		sp.setLocation(567, 25);
		sp.setVisible(false);
		sp.setOpaque(false);
		ammoListPanel.setPreferredSize(new Dimension(75, 125));
		ammoListPanel.setOpaque(false);
		//ammoListPanel.setCellRenderer(new IconListRenderer());
		//ammoListPanel.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		ammoListPanel.setVisibleRowCount(6);
        ammoListPanel.setFont(bigFont);
		sp.getViewport().setView(ammoListPanel);
		add(sp);
		sp.setAutoscrolls(true);
		//sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        dpsGraph = new DamageRangeGraph();
        dpsGraph.setPreferredSize(new Dimension(300, 35));
        dpsGraph.setLocation(25, 107);
        dpsGraph.setVisible(false);
        add(dpsGraph);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		Dimension size = getSize();
		Color bgColor = getBackground();
		Ship ship = memShip;
		
        //setup
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
        					RenderingHints.VALUE_RENDER_QUALITY);
        
        g2d.setComposite(AlphaComposite.SrcAtop);
        
        g2d.setFont(bigFont);
        
        g2d.setColor(bgColor);
        g2d.fillRect(0,0,size.width,size.height);
        g2d.drawImage(bgImg, 0,0, size.width, size.height, null);
        
        //render
        g2d.drawImage(dpsDmgImg, 8, 8, null);
        WidgetHelper.drawShadowedString(g2d, "Total DPS:  " + WidgetHelper.formatFloat(ship.calculateGenericDps(), 2), 45, 17, Color.white);
        WidgetHelper.drawShadowedString(g2d, "Volley:     " + WidgetHelper.formatFloat(ship.calculateGenericVolley(), 2), 45, 29, Color.white);
        WidgetHelper.drawShadowedString(g2d, "Min. Effective Range:  " + WidgetHelper.formatFloat(calculateMinRange(ship), 2), 125, 17, Color.white);
        WidgetHelper.drawShadowedString(g2d, "Max. Effective Range:  " + WidgetHelper.formatFloat(calculateMaxRange(ship)*0.8f, 2), 125, 29, Color.white);
        
        //Dimension d = getSize();
        //int spacing = (d.width-(32*ship.totalHiSlots())-60)/(ship.totalHiSlots()*2);
        int spacing = 42;
        
        for(int i=0;i<ship.totalSlots(Module.HI_SLOT);i++) {
        	AffineTransform save = g2d.getTransform();
        	Module m = ship.getModule(Module.HI_SLOT, i);
        	if(m != null && m instanceof Weapon) {
        		if(i==selected) {
        			g2d.setColor(bgLightColor);
        			g2d.fillRoundRect((i*spacing+30)-5, 45, 42, 52, 10, 10);
        			
        			paintDetails(g2d, (Weapon)m);
        			paintAmmo(g2d, m);
        		}
        		g2d.scale(0.5, 0.5);
	        	g2d.drawImage(m.getImage(), (i*spacing+30)*2, 100, null);
        	} else if (m != null) {
        		
        	} else {
        		g2d.setColor(bgLightColor);
        		g2d.drawRect((i*spacing+30), 50, 32, 32);
        	}
        	g2d.setTransform(save);
        }
	}

	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void drop(DropTargetDropEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void shipChanged(Ship ship) {
		if(memShip == null || ship != memShip) {
			memShip = ship;
			selected = -1;
            
            dpsGraph.setExtents(new Rectangle((int)calculateMinRange(ship),
                                                0,
                                                (int)(calculateMaxRange(ship)*1.2f),
                                                (int)ship.calculateGenericDps()));
            dpsGraph.setShip(ship);
        } else {
            dpsGraph.setExtents(new Rectangle((int)calculateMinRange(ship),
                                                0,
                                                (int)(calculateMaxRange(ship)*1.2f),
                                                (int)ship.calculateGenericDps()));
            dpsGraph.renderGraph();
        }
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		
		ammoType.setVisible(false);
        sp.setVisible(false);
        dpsGraph.setVisible(false);
        dpsGraph.setSelected(null);
		if(y >= 50 && y <=82) {
			selected = (x-30)/42;
			buildAmmo(memShip.getModule(Module.HI_SLOT, selected));
			ammoType.setVisible(true);
			dpsGraph.setVisible(true);
			dpsGraph.setSelected(memShip.getModule(Module.HI_SLOT, selected));
            repaint();
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

	private void buildAmmo(Module weapon) {
		if(weapon == null)
			return;
		
		int size = Integer.parseInt(weapon.getAttribute("chargeSize", "1"));
		int gid = Integer.parseInt(weapon.getAttribute("chargeGroup"+(ammoType.getSelectedIndex()+1), "0"));
		Vector data = new Vector();
		
		ammoListPanel.removeAll();
		
		try {
			LinkedList group = Database.getInstance().getTypeByGroup(gid);
			Iterator itr = group.iterator();
			
			while(itr.hasNext()) {
				Item i = (Item)itr.next();
				if(i != null) {
					Ammo a = new Ammo(i);
					if(a.getSize() == size) {
						data.add(new ItemIcon(a));
						//x+=84;
					}
				}
			}
			
			ammoListPanel.setListData(data);
			if(!sp.isVisible())
				sp.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void paintAmmo(Graphics2D g2d, Module weapon) {
		g2d.setColor(bgLightColor);
		g2d.fillRoundRect(10, 97, 330, 55, 40, 40);
		
		//TODO: initially center them, if that doesn't work then just shift them over...
		/*int x=20;
		int size = Integer.parseInt(weapon.getAttribute("chargeSize", "1"));
		int gid = Integer.parseInt(weapon.getAttribute("chargeGroup"+(ammoType.getSelectedIndex()+1), "0"));
		try {
			LinkedList group = Database.getInstance().getTypeByGroup(gid);
			Iterator itr = group.iterator();
			AffineTransform save = g2d.getTransform();
			g2d.scale(0.5, 0.5);
			while(itr.hasNext()) {
				Ammo a = new Ammo((Item) itr.next());
				if(a.getSize() == size) {
					g2d.drawImage(a.getImage(), x, 214, null);
					x+=84;
				}
			}
			g2d.setTransform(save);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	private void paintDetails(Graphics2D g2d, Weapon weapon) {
		float seconds = (weapon.calculateRoF() * weapon.getChargeCount());
		
		g2d.fillRoundRect(400, 10, 230, 135, 40, 40);
		g2d.drawImage(weapon.getImage(), 410,20,null);
		
        String optimal = weapon.calculateOptimalRange()>=10000 ? WidgetHelper.formatFloat(weapon.calculateOptimalRange()/1000f,2)+"km":WidgetHelper.formatFloat(weapon.calculateOptimalRange(),2)+"m";
        
		WidgetHelper.drawShadowedString(g2d, "Type:      "+weapon.getTypeString(), 485, 29, Color.white);
		WidgetHelper.drawShadowedString(g2d, "RoF:       "+WidgetHelper.formatFloat(weapon.calculateRoF(),2)+"s", 485, 45, Color.white);
        WidgetHelper.drawShadowedString(g2d, "DPS:       "+weapon.calculateAggregateDps(), 485, 61, Color.white);
		WidgetHelper.drawShadowedString(g2d, "Optimal:   "+optimal, 485, 77, Color.white);
		WidgetHelper.drawShadowedString(g2d, "MTBR:      "+((int)(seconds / 60))+"m "+((int)(seconds % 60))+"s", 485, 93, Color.white);
	}
	
	private float calculateMinRange(Ship ship) {
        float min = 1000000;
		for(int i=0;i<ship.totalSlots(Module.HI_SLOT);i++) {
        	Module m = ship.getModule(Module.HI_SLOT, i);
        	if(m != null && m instanceof Weapon) {
                min = (float)Math.min(min, ((Weapon)m).calculateOptimalRange()-((Weapon)m).calculateRangeExtent());
            }
        }
        
        return (min > 0 ? min : 0);
	}
	
	private float calculateMaxRange(Ship ship) {
		float min = -1000000;
		for(int i=0;i<ship.totalSlots(Module.HI_SLOT);i++) {
        	Module m = ship.getModule(Module.HI_SLOT, i);
        	if(m != null && m instanceof Weapon) {
                min = (float)Math.max(min, ((Weapon)m).calculateOptimalRange()+((Weapon)m).calculateRangeExtent());
            }
        }
        
        return min;
	}

	public void actionPerformed(ActionEvent arg0) {
		//repaint();
	}
}
