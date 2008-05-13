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
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.item.Ammo;
import tightfit.module.Module;
import tightfit.ship.Ship;
import tightfit.ship.ShipChangeListener;

/**
 * A module slot on the ship. Handles it's own rendering of the slot graphic 
 * and module icon, as well as the activation indicator light.
 *
 */
public class ModuleSlot extends Slot implements ShipChangeListener {
    private static final long serialVersionUID = 1L;

    protected int myType, mySpot;
    
    protected Module mounted;
    
    protected Image slotImg, typeImg, inactiveImg, activeImg;
    
    public ModuleSlot(TightFit editor, JPanel parent, int type, int spot) {
        super(parent, editor);
        myType = type;
        mySpot = spot;

        setPreferredSize(new Dimension(64,64));
        
        try {
            if(myType == Module.HI_SLOT) {
            	slotImg = Resources.getImage("hislot.png");
            	typeImg = Resources.getImage("icon08_11-aligned.png");
            } else if(myType == Module.LOW_SLOT) {
            	slotImg = Resources.getImage("loslot.png");
            	typeImg = Resources.getImage("icon08_09-aligned.png");
            } else if(myType == Module.MID_SLOT) {
            	slotImg = Resources.getImage("mdslot.png");
            	typeImg = Resources.getImage("icon08_10-aligned.png");
            }
            activeImg = Resources.getImage("loslot-active.png");
        	inactiveImg = Resources.getImage("loslot-inactive.png");
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    public boolean mount(Module m) {
        String tip = "";
        
        mounted = m;
        if(m != null) {
        	tip += "TYPE: "+m.name + "\r\nRequired CPU: "+m.getCpuUsage()+"\r\nRequired Power: "+ m.getPowerUsage();
        	if(m.acceptsCharges() && m.getCharge() != null) {
        		tip += "\r\nCharge: "+m.getCharge().name+" ("+m.getChargeCount()+"pcs)";
        	}
        }
        
        setToolTipText(tip);
        
        return true;
    }
    
    public Module getModule() {
    	return mounted;
    }
    
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        int x, y;
        double r = Math.toRadians(rotate());
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
				RenderingHints.VALUE_RENDER_QUALITY);
        
        //g2d.translate(pt.x, pt.y);

        //g2d.clearRect(0,0,64,64);
        //g2d.fillRect(0,0,64,64);
        
        g2d.setClip(-10, -10, 84, 84);
        
        AffineTransform t = g2d.getTransform();
        g2d.rotate(r, 32, 32);
        //g2d.rotate(r);
        
        g2d.drawImage(slotImg, 0, 0, null);
        
        if(selected)
        	g2d.drawImage(slotImg, 0, 0, null);
        
        if(mySpot < 4) { 
        	x=25; y=55;
        	if(rotate()>0)
        		x-=20;
        } else {
        	x=60; y=15;
        }
        
        
    	//int dx = (int)(x * Math.cos(r) - y * Math.sin(r))+32;
    	//int dy = (int)(x * Math.sin(r) + y * Math.cos(r))+32;
        if(mounted != null) {
        	
        	if(mounted.isOnline())
        		g2d.drawImage(activeImg, 0, 0, null);
        	else g2d.drawImage(inactiveImg, 0, 0, null);
        	
        	g2d.setTransform(t);
        	
        	g2d.scale(0.75f, 0.75f);
	        g2d.drawImage(mounted.getImage(), 8, 8, null);
	        
	        if(mounted.acceptsCharges()) {
	        	Point pt = chargePoint();
	        	g2d.setColor(Color.white);
	        	g2d.setStroke(new BasicStroke(2));
	        	Composite saveComp = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.55f));
                g2d.drawOval(pt.x, pt.y, 40, 40);
                g2d.setComposite(saveComp);
                
                Ammo ammo = mounted.getCharge();
                if(ammo != null) {
                	g2d.scale(0.55f, 0.55f);
                	g2d.drawImage(ammo.getImage(), (int)(pt.x/0.55f)+4, (int)(pt.y/0.55f)+4, null);
                }
	        }
        } else {
            //g2d.rotate(r, 32, 32);
        	//TODO: draw image for slot type
        	//g2d.scale(0.75f, 0.75f);
        	g2d.drawImage(typeImg, 0, 0, null);
            g2d.setTransform(t);
        }
        
        //g2d.translate(-pt.x, -pt.y);
    }
    
    public int getRack() {
    	return myType;
    }
    
    public int getSlotNumber() {
    	return mySpot;
    }
    
    private float rotate() {
    	if(mySpot==0)
        	return -37;
    	if(mySpot==2)
    		return 37;
    	if(mySpot==3)
    		return 72.14f;
    	if(mySpot==4)
    		return 108.3f;
    	if(mySpot==5)
    		return 144;
    	if(mySpot==6)
    		return 181;
    	if(mySpot==7)
    		return -143.19f;
    	return 0;
    }

    private Point chargePoint() {
    	if(mySpot==0)
        	return new Point(45, 45);
    	if(mySpot==2)
    		return new Point(10, 50);
    	if(mySpot==3)
    		return new Point(-7, 36);
    	if(mySpot==4)
    		return new Point(50, 37);
    	if(mySpot==5)
    		return new Point(35, 50);
    	if(mySpot==6)
    		return new Point(17, 55);
    	if(mySpot==7)
    		return new Point(10, 45);
    	return new Point(25, 55);
    }
    
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("unfit")) {
			mounted = null;
			editor.getShip().removeModule(myType, mySpot);
		} else if(e.getActionCommand().contains("Offline")) {
			mounted.offline();
		} else if(e.getActionCommand().contains("Online")) {
			mounted.online();
		}
		parent.repaint();
	}

	public void shipChanged(Ship ship) {
		mount(ship.getModule(myType, mySpot));
	}
}
