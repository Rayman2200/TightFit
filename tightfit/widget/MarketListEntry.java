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
import tightfit.item.Item;

public class MarketListEntry extends AbstractListEntry {
	private static final long serialVersionUID = 1L;
	
	private TightFit editor;
	
	private Image imgSkill, imgCpu, imgPower, imgSlot, imgBack;
	
	private Color green = new Color(.22f,.52f,.26f);
	private Color red = new Color(.52f,.22f,.26f);
	
	private Font bigFont, smallFont;
	
	public MarketListEntry(Item item, TightFit editor) {
		super(item);
		
		this.editor = editor;
		setPreferredSize(new Dimension(300, 225));
		
		try {
			Font big = Resources.getFont("agencyr.ttf");
	        bigFont = big.deriveFont(24f);
			smallFont = Resources.getFont("stan07_55.ttf");
	        smallFont = smallFont.deriveFont(8f);
	        
			imgPower = Resources.getImage("icon02_07.png").getScaledInstance(22,22,Image.SCALE_SMOOTH);
			imgCpu = Resources.getImage("icon12_07.png").getScaledInstance(22,22,Image.SCALE_SMOOTH);
			imgSkill = Resources.getImage("icon50_11.png").getScaledInstance(22,22,Image.SCALE_SMOOTH);
			imgBack = Resources.getImage("icon-back.png");
			
			if(!item.getAttribute("loPower", "-1").equals("-1")) {
	    		imgSlot =  Resources.getImage("icon08_09.png").getScaledInstance(16,16,Image.SCALE_FAST);
	    	} else if(!item.getAttribute("hiPower", "-1").equals("-1")) {
	    		imgSlot =  Resources.getImage("icon08_11.png").getScaledInstance(16,16,Image.SCALE_FAST);
	    	} else if(!item.getAttribute("medPower", "-1").equals("-1")) {
	    		imgSlot =  Resources.getImage("icon08_10.png").getScaledInstance(16,16,Image.SCALE_FAST);
	    	}
		} catch (Exception e) {
		}
	}
	
	public Item getItem() {
		return myItem;
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
		
		g2d.setColor(getBackground());
        g2d.fillRect(0,0,size.width,size.height);
        g2d.setColor(Color.WHITE);
        g2d.drawLine(10,0,size.width-10,0);
        
        Point imgPos = new Point(10, 15);
        
        g2d.drawImage(imgBack, imgPos.x-7, imgPos.y-7, null);
        g2d.drawImage(myItem.getImage(), imgPos.x, imgPos.y, null);
        
        //skill
        if(editor.getChar().hasRequiredSkill(myItem))
        	g2d.setColor(green);
        else g2d.setColor(red);
        g2d.fillRect(imgPos.x + 72, imgPos.y, 22, 22);
        g2d.drawImage(imgSkill, imgPos.x + 72, imgPos.y, null);
        
        //if it's a module...
        if(myItem.getAttribute("lowSlots", "-1").equals("-1")) {
        	g2d.setColor(Color.WHITE);
            g2d.drawRect(imgPos.x + 47, imgPos.y + 47, 16, 16);
        	g2d.setColor(Color.BLACK);
            g2d.fillRect(imgPos.x + 48, imgPos.y + 48, 16, 16);
            g2d.drawImage(imgSlot, imgPos.x + 48, imgPos.y+48, null);
            
	        //power
	        if(editor.getShip().getMaxPower() > (int)Float.parseFloat(myItem.getAttribute("power", "0")))
	        	g2d.setColor(green);
	        else g2d.setColor(red);
	        g2d.fillRect(imgPos.x + 94, imgPos.y, 22, 22);
	        g2d.drawImage(imgPower, imgPos.x + 94, imgPos.y, null);
	        
	        //cpu
	        if(editor.getShip().getMaxCpu() > (int)Float.parseFloat(myItem.getAttribute("cpu", "0")))
	        	g2d.setColor(green);
	        else g2d.setColor(red);
	        g2d.fillRect(imgPos.x + 116, imgPos.y, 22, 22);
	        g2d.drawImage(imgCpu, imgPos.x + 116, imgPos.y, null);
        }
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(bigFont);
        FitPanel.drawShadowedString(g2d, myItem.name, 82, 82, Color.WHITE);
        g2d.setFont(smallFont);
        drawDescription(g2d, 42, 89);
	}
	
	private void drawDescription(Graphics2D g2d, int x, int y) {
		
		String desc = myItem.getAttribute("description", "No data in database");
		String [] lines = desc.split("\\n");
		for(int i=0;i<lines.length;i++, y+=13) {
			String line = lines[i];
			while(line.length() > 80) {
				int pos = line.lastIndexOf(" ", 80);
				FitPanel.drawShadowedString(g2d, line.substring(0, pos), x, y, Color.WHITE);
				y += 13;
				line = line.substring(pos, line.length());
			}
			FitPanel.drawShadowedString(g2d, line, x, y, Color.WHITE);
		}
	}
}
