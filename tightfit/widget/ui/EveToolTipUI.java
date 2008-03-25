/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.widget.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicToolTipUI;

import tightfit.Resources;

public final class EveToolTipUI extends BasicToolTipUI {
	private Image bgImg;
	
	public EveToolTipUI() {
		try {
			bgImg = Resources.getImage("ttback.png");
		} catch (Exception e) {
		}
	}
	
	public void paint(Graphics g, JComponent c) {
		Graphics2D g2d = (Graphics2D) g.create();
	    FontMetrics metrics = g2d.getFontMetrics();
	    String [] strs = ((JToolTip) c).getTipText().split("\n");
	    Dimension size = c.getSize();
	    
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
						RenderingHints.VALUE_RENDER_QUALITY);
		
		g2d.setComposite(AlphaComposite.SrcAtop);
	    
	    g2d.drawImage(bgImg, 0, 0, size.width, size.height, null);
	    
	    g2d.setColor(Color.WHITE);
	    if (strs != null) {
	    	for (int i = 0; i < strs.length; i++) {
	    		g2d.drawString(strs[i].toUpperCase(), 3, (metrics.getHeight()) * (i + 1)+1);
	    	}
	    }
	    g2d.setColor(Color.WHITE);
	    g2d.drawRect(0, 0, size.width, size.height);
	}
	
	public Dimension getPreferredSize(JComponent c) {
		String tipText = ((JToolTip) c).getTipText();
		String [] strs;
		int maxWidth = 0;
		
	    if (tipText == null) {
	    	tipText = "";
	    }
	    
	    strs = tipText.split("\n");
	    //FIXME: FontMetrics metrics = c.getGraphics().getFontMetrics();
	    for(int i=0;i<strs.length;i++) {
	    	int w = 5 * strs[i].length();
	    	if(maxWidth < w)
	    		maxWidth = w;
	    }
	    
	    return new Dimension(maxWidth, 12 * strs.length);
	}
}
