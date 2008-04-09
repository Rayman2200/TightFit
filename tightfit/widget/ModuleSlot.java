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

import tightfit.Resources;
import tightfit.TightFit;
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
    
    public ModuleSlot(TightFit editor, FitPanel parent, int type, int spot) {
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
        //if(mounted != null || m.slotRequirement != myType)
        //    return false;
        
        mounted = m;
        if(m != null) {
        	setToolTipText("TYPE: "+m.name + "\r\nRequired CPU: "+m.getCpuUsage()+"\r\nRequired Power: "+ m.getPowerUsage());
        } else {
            setToolTipText("");
        }
        return true;
    }
    
    public Module getModule() {
    	return mounted;
    }
    
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
				RenderingHints.VALUE_RENDER_QUALITY);
        
        double r = Math.toRadians(rotate());
        
        //g2d.translate(pt.x, pt.y);

        //g2d.clearRect(0,0,64,64);
        //g2d.fillRect(0,0,64,64);
        
        AffineTransform t = g2d.getTransform();
        g2d.rotate(r, 32, 32);
        //g2d.rotate(r);
        
        g2d.drawImage(slotImg, 0, 0, null);
        
        if(selected)
        	g2d.drawImage(slotImg, 0, 0, null);
        
        //int x=8, y=8;
    	//int dx = (int)(x * Math.cos(r) - y * Math.sin(r));
    	//int dy = (int)(x * Math.sin(r) + y * Math.cos(r));
        
        if(mounted != null) {
        	
        	if(mounted.isOnline())
        		g2d.drawImage(activeImg, 0, 0, null);
        	else g2d.drawImage(inactiveImg, 0, 0, null);
        	
        	g2d.setTransform(t);
        	
        	g2d.scale(0.75f, 0.75f);
	        g2d.drawImage(mounted.getImage(), 8, 8, null);
	        
	        if(mounted.acceptsCharges()) {
                //TODO: draw circle
                
                g2d.scale(0.15f, 0.15f);
                //TODO: draw charge
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
