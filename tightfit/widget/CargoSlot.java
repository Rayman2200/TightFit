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
import java.util.LinkedList;

import javax.swing.JPanel;

import tightfit.*;
import tightfit.ship.Ship;
import tightfit.ship.ShipChangeListener;

public class CargoSlot extends Slot implements ShipChangeListener {

	private static final long serialVersionUID = 1L;
	private static final String CARGOHOLD = Resources.getString("panel.fit.cargohold");
	
	private Image cargoImg;
	private Color statWhite = new Color(.9f,.9f,1f,.95f);
	private Color dullWhite = new Color(.9f,.9f,1f,.55f);
	private Color selWhite = new Color(1,1,1,0.25f);
	
	protected LinkedList modules;
	
	public CargoSlot(JPanel panel, TightFit editor) {
		super(panel, editor);

		try {
			cargoImg = Resources.getImage("icon03_13.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        Color bgColor = Color.decode(TightPreferences.node("prefs").get("bgColor", "#30251A"));
        Rectangle clip = g2d.getClipBounds();
        Point pt = getLocation();
        Ship ship = editor.getShip();
        
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
				RenderingHints.VALUE_RENDER_QUALITY);
        
        g2d.setComposite(AlphaComposite.SrcAtop);
        
        g2d.setColor(bgColor);
        
        g2d.fillRect(0, 0, clip.width, clip.height);
        g2d.drawImage(((FitPanel)parent).panelImg, -pt.x, -pt.y, null);
        
        g2d.drawImage(cargoImg, 0, 0, null);
        
        if(selected) {
        	g2d.setColor(selWhite);
        	g2d.fillRect(0, 0, clip.width, clip.height);
        }
        
        g2d.setColor(dullWhite);
        g2d.drawLine(32, 0, 32, 32);
        
        WidgetHelper.drawShadowedString(g2d, CARGOHOLD, 40, 11, Color.white);
        
        WidgetHelper.drawShadowedString(g2d, "" + WidgetHelper.formatFloat(ship.getTotalCargoCapacity()-ship.getFreeCargoCapacity(), 2) + " / "+ship.getTotalCargoCapacity(), 45, 21, statWhite);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void shipChanged(Ship ship) {
		// TODO Auto-generated method stub
		
	}

}
