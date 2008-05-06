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
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.module.Module;
import tightfit.ship.Ship;

public class SimulatorSlot extends ModuleSlot {
	private static final long serialVersionUID = 1L;
	
	protected Image slotImg, typeImg, activeImg, powerImg;
	
	public SimulatorSlot(TightFit editor, JPanel parent, int type, int spot) {
		super(editor, parent, type, spot);
		
		setPreferredSize(new Dimension(32,32));
		
		try {
			slotImg = Resources.getImage("simslot.png").getScaledInstance(32,32,Image.SCALE_SMOOTH);
            if(myType == Module.HI_SLOT) {
            	typeImg = Resources.getImage("icon08_11.png").getScaledInstance(24,24,Image.SCALE_SMOOTH);
            } else if(myType == Module.LOW_SLOT) {
            	typeImg = Resources.getImage("icon08_09.png").getScaledInstance(24,24,Image.SCALE_SMOOTH);
            } else if(myType == Module.MID_SLOT) {
            	typeImg = Resources.getImage("icon08_10.png").getScaledInstance(24,24,Image.SCALE_SMOOTH);
            }
            activeImg = Resources.getImage("simslot-active.png").getScaledInstance(32,32,Image.SCALE_SMOOTH);
            powerImg = Resources.getImage("simslot-power.png").getScaledInstance(32,32,Image.SCALE_SMOOTH);
        }catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
				RenderingHints.VALUE_RENDER_QUALITY);
        
        Ship ship = editor.getShip();
        
        Module mounted = ship.getModule(myType, mySpot);
        
        g2d.drawImage(slotImg, 0, 0, null);
        
        if(mounted != null) {
        	
        	if(mounted.isOnline()) {
        		if(mounted.requiresActivation()) {
        			g2d.drawImage(powerImg, 0, 0, null);
        			if(mounted.isActive()) {
        				g2d.drawImage(activeImg, 0, 0, null);
        			}
        		}
        		AffineTransform save = g2d.getTransform();
                g2d.scale(0.5, 0.5);
                if(mounted.acceptsCharges() && mounted.getCharge() != null /*&& mounted.getAttribute()*/)
                	g2d.drawImage(mounted.getCharge().getImage(),0,0,null);
                else g2d.drawImage(mounted.getImage(),0,0,null); 
                g2d.setTransform(save);
        	} else {
        		Composite saveComp = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.35f));
                AffineTransform save = g2d.getTransform();
                g2d.scale(0.5, 0.5);
                if(mounted.getCharge() != null) {
                	g2d.drawImage(mounted.getCharge().getImage(),0,0,null);
                } else {
                	g2d.drawImage(mounted.getImage(),0,0,null);
                }
                g2d.setTransform(save);
                g2d.setComposite(saveComp);
        	}
        } else {
        	g2d.drawImage(typeImg, 4, 2, null);
        }
	}
}
