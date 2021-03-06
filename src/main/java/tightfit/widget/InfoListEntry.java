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

import tightfit.item.Item;

public class InfoListEntry extends AbstractListEntry {
	private static final long serialVersionUID = 1L;
	private String attribute, title;
	private Image icon;
	private Color headerGrey;
    private Font bigFont;
    
	public InfoListEntry(Item item, String attribute, Image icon) {
		super(item);
		this.title = this.attribute = attribute;
		this.icon = icon;
		
		setPreferredSize(new Dimension(150, 27));
        headerGrey = new Color(.75f,.75f,.75f,.75f);
		
        try {
			Font big = Resources.getFont("bmmini.ttf");
            bigFont = big.deriveFont(8f);
        } catch (Exception e) {}
	}
	
	public InfoListEntry(Item item, String attribute, String title, Image icon) {
		super(item);
		this.attribute = attribute;
		this.title = title;
		this.icon = icon;
		
		setPreferredSize(new Dimension(150, 27));
        headerGrey = new Color(.75f,.75f,.75f,.75f);
		
        try {
			Font big = Resources.getFont("bmmini.ttf");
            bigFont = big.deriveFont(8f);
        } catch (Exception e) {}
	}
	
	public InfoListEntry(String data, String title, Image icon) {
		super();
		this.attribute = data;
		this.title = title;
		this.icon = icon;
		
		setPreferredSize(new Dimension(150, 27));
        headerGrey = new Color(.75f,.75f,.75f,.75f);
		
        try {
			Font big = Resources.getFont("bmmini.ttf");
            bigFont = big.deriveFont(8f);
        } catch (Exception e) {}
		
		setName(title);
		setToolTipText(title);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		Dimension size = getSize();
		String dispName = null;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                		RenderingHints.VALUE_ANTIALIAS_ON);

		//g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		//                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
						RenderingHints.VALUE_RENDER_QUALITY);
		
		g2d.setComposite(AlphaComposite.SrcAtop);
        
		g2d.setColor(Color.DARK_GRAY);
		g2d.fillRect(0, 0, size.width, size.height);
		g2d.setColor(Color.GRAY);
		g2d.drawLine(0, size.height-1, size.width, size.height-1);
		
        g2d.setFont(bigFont);
		
		WidgetHelper.drawShadowedString(g2d, title, 23, 9, headerGrey);
		if(myItem == null) {
			WidgetHelper.drawShadowedString(g2d, attribute, 25, 19, Color.WHITE);
		} else {
			WidgetHelper.drawShadowedString(g2d, myItem.getAttribute(attribute, "0"), 25, 19, Color.WHITE);
		}
		
		//g2d.scale(.25, .25);
		g2d.drawImage(icon, 0, 0, null);
	}
}
