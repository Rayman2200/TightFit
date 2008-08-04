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

import java.io.IOException;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import tightfit.Resources;

/**
 * A simple image-only button class that handles its own highlight and
 * click visual cues.
 *
 */
class ImageButton extends JButton implements MouseListener {
	private static final long serialVersionUID = 1L;

	private int offset;
	private boolean bAnimated = false;
	
	public ImageButton(ImageIcon i, boolean an) {
        super(i);
        
        bAnimated = an;
        
        setPreferredSize(new Dimension(i.getImage().getWidth(null), i.getImage().getHeight(null)));
        
        setOpaque(false);
        setBorderPainted(false);
        addMouseListener(this);
    }
	
    public ImageButton(String img) throws IOException {
        this(new ImageIcon(Resources.getImage(img)), false);
    }
    
    public ImageButton(String img, boolean an) throws IOException {
    	this(new ImageIcon(Resources.getImage(img)), an);
    }
    
    public void paintComponent(Graphics g) {
    	Dimension size = getSize();
    	
    	Graphics2D g2d = (Graphics2D) g.create();
    	
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
        					RenderingHints.VALUE_RENDER_QUALITY);
        
        g2d.setComposite(AlphaComposite.SrcAtop);
    	
        if(bAnimated) {
	        if(isSelected()) {
		        g2d.setColor(new Color(1,1,1,.3f));
		    	g2d.fillRect(1, 0, size.width, size.height-1);
	        }
	    	g2d.drawImage(((ImageIcon)getIcon()).getImage(), 0, offset, null);
        } else {
        	g2d.drawImage(((ImageIcon)getIcon()).getImage(), 0, 0, null);
        }
    }

    public void setAnimated(boolean b) {
    	bAnimated = b;
    }
    
	public void mouseClicked(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent arg0) {
		offset=1;
	}

	public void mouseReleased(MouseEvent arg0) {
		offset=0;
	}

	public void mouseEntered(MouseEvent arg0) {
		setSelected(true);
	}

	public void mouseExited(MouseEvent arg0) {
		setSelected(false);
	}
    
    
}
