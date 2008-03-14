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

import javax.swing.JPanel;

import tightfit.item.Item;

public class MarketListEntry extends JPanel {
	private static final long serialVersionUID = 1L;

	private Item myItem;
	
	public MarketListEntry(Item item) {
		setPreferredSize(new Dimension(300, 225));
		setOpaque(true);
		setLayout(new BorderLayout());
		//setBorder(BorderFactory.createLineBorder(Color.black, 1));
		
		myItem = item;
		
		setToolTipText(item.name);
	}
	
	public Item getItem() {
		return myItem;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		
		Dimension size = getSize();
		g2d.setColor(getBackground());
        g2d.fillRect(0,0,size.width,size.height);
        g2d.setColor(Color.WHITE);
        g2d.drawLine(10,0,size.width-10,0);
        
        Point imgPos = new Point(10, 15);
        
        g2d.drawImage(myItem.getImage(), imgPos.x, imgPos.y, null);
        
        g2d.setColor(Color.WHITE);
        g2d.drawString(myItem.name, 72, imgPos.y+20);
        drawDescription(g2d, 72, imgPos.y+48);
	}
	
	private void drawDescription(Graphics2D g2d, int x, int y) {
		
		String desc = myItem.getAttribute("description", "No data in database");
		String [] lines = desc.split("\\n");
		for(int i=0;i<lines.length;i++, y+=13) {
			String line = lines[i];
			while(line.length() > 80) {
				int pos = line.lastIndexOf(" ", 80);
				g2d.drawString(line.substring(0, pos), x, y);
				y += 13;
				line = line.substring(pos, line.length());
			}
			g2d.drawString(line, x, y);
		}
	}
}
