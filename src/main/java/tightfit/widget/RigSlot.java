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

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.TightPreferences;

public class RigSlot extends ModuleSlot {
	private static final long serialVersionUID = 1L;
	
	private Color selWhite = new Color(1,1,1,0.25f);
	
	public RigSlot(TightFit editor, FitPanel parent, int type, int spot) {
		super(editor, parent, type, spot);
		
		setPreferredSize(new Dimension(33,33));
		
		slotImg = null;
		try {
			typeImg = Resources.getImage("icon68_01.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		} catch (Exception e) {
		}
	}

	public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        Color bgColor = Color.decode(TightPreferences.node("prefs").get("bgColor", "#30251A"));
        Point pt = getLocation();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
				RenderingHints.VALUE_RENDER_QUALITY);
        
        g2d.setComposite(AlphaComposite.SrcAtop);
        
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, 32, 32);
        g2d.drawImage(((FitPanel)parent).panelImg, -pt.x, -pt.y, null);
        
        g2d.setColor(Color.WHITE);
        g2d.drawRect(0, 0, 32, 32);
        
        if(selected) {
        	g2d.setColor(selWhite);
        	g2d.fillRect(0, 0, 32, 32);
        }
        	
        if(mounted != null) {
        	g2d.scale(.5, .5);
        	g2d.drawImage(mounted.getImage(), 1, 1, null);
        } else {
        	Composite saveComp = g2d.getComposite();
            //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.55f));
        	g2d.drawImage(typeImg, 1, 1, null);
        	g2d.setComposite(saveComp);
        }
	}
}
