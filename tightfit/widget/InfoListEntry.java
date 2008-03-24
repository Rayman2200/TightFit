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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import tightfit.item.Item;

public class InfoListEntry extends AbstractListEntry {
	private static final long serialVersionUID = 1L;
	private String attribute;
	private Image icon;
	
	public InfoListEntry(Item item, String attribute, Image icon) {
		super(item);
		this.attribute = attribute;
		this.icon = icon;
		
		setPreferredSize(new Dimension(150, 16));
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		Dimension size = getSize();
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                		RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
						RenderingHints.VALUE_RENDER_QUALITY);
		
		g2d.setComposite(AlphaComposite.SrcAtop);
		
		g2d.setColor(Color.DARK_GRAY);
		g2d.fillRect(0, 0, size.width, size.height);
		g2d.setColor(Color.GRAY);
		g2d.drawLine(0, 0, size.width, 0);
		
		FitPanel.drawShadowedString(g2d, "", 17, 0, Color.WHITE);
		FitPanel.drawShadowedString(g2d, myItem.getAttribute(attribute, "0"), 19, 8, Color.WHITE);
		
		g2d.scale(.25, .25);
		g2d.drawImage(icon, 0, 0, null);
	}
}
