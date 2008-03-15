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
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.swing.JPanel;

import tightfit.Resources;
import tightfit.module.Module;

/**
 * A module slot on the ship. Handles it's own rendering of the slot graphic 
 * and module icon, as well as the activation indicator light.
 *
 */
public class Slot extends JPanel {
	private static final long serialVersionUID = 1L;

	/* -- slot types -- */
    /** NO_SLOT */
    public static int NO_SLOT = -1;
    
    protected int myType, mySpot;
    protected FitPanel parent;
    protected Module mounted;
    
    private boolean selected;
    
    public Slot(FitPanel parent, int type, int spot) {
        myType = type;
        mySpot = spot;
        this.parent = parent;
        
        DropTarget dt = new DropTarget(this, DnDConstants.ACTION_COPY, parent, true, null);
        setDropTarget(dt);
        
        setPreferredSize(new Dimension(64,64));
        //setLayout(new OverlayLayout(this));
    }
    
    public boolean mount(Module m) {
        //if(mounted != null || m.slotRequirement != myType)
        //    return false;
        
        mounted = m;
        
        return true;
    }
    
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
				RenderingHints.VALUE_RENDER_QUALITY);
        
        //Point pt = getLocation();
        Image img = null;
        double r = Math.toRadians(rotate());
        
        try {
            if(myType == Module.HI_SLOT)
            	img = Resources.getImage("hislot.png");
            else if(myType == Module.LOW_SLOT)
            	img = Resources.getImage("loslot.png");
            else if(myType == Module.MID_SLOT)
            	img = Resources.getImage("midslot.png");
        }catch(IOException e) {
            
        }
        //g2d.translate(pt.x, pt.y);

        //g2d.clearRect(0,0,64,64);
        //g2d.fillRect(0,0,64,64);
        
        AffineTransform t = g2d.getTransform();
        g2d.rotate(r, 32, 32);
        //g2d.rotate(r);
        
        g2d.drawImage(img, 0, 0, null);
        
        if(selected)
        	g2d.drawImage(img, 0, 0, null);
        
        g2d.setTransform(t);
        
        if(mounted != null) {
        	/*int x=6, y=6;
        	int dx = (int)(x * Math.cos(r) - y * Math.sin(r));
        	int dy = (int)(x * Math.sin(r) + y * Math.cos(r));*/
        	
        	g2d.scale(0.75f, 0.75f);
	        g2d.drawImage(mounted.getImage(), 6, 6, null);
	        
	        if(mounted.acceptsCharges()) {
                //TODO: draw circle
                
                g2d.scale(0.15f, 0.15f);
                //TODO: draw charge
	        }
        } else {
            g2d.rotate(r, 32, 32);
        	//TODO: draw image for slot type
            g2d.setTransform(t);
        }
        
        //g2d.translate(-pt.x, -pt.y);
    }
    
    public void setSelected(boolean s) {
    	if(selected != s) {
	    	selected = s;
	    	Point pt = getLocation();
	    	//FIXME: parent.repaint(pt.x, pt.y, 64, 64);
	    	parent.repaint();
    	}
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

}
