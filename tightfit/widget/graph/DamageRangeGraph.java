/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.widget.graph;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.swing.*;

import tightfit.item.*;
import tightfit.module.*;
import tightfit.ship.Ship;

public class DamageRangeGraph extends Graph implements MouseMotionListener {

    protected Ship myShip;
    
    public DamageRangeGraph() {
        addMouseMotionListener(this);
    }
    
    public void renderGraph() {
        Dimension d = getPreferredSize();
        int x;
        float dx = (dmax-dmin)/d.width;
        float dy = (rmax-rmin)/d.height;
        float gx;
        Color green = new Color(0,1,0,0.2f);
        Color blue = new Color(0,0,1,0.2f);
        
        cachedImage = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)cachedImage.getGraphics();
        
        renderMeter();
        g.setColor(Color.black);
        g.drawLine((int)(10*dx),0,(int)(10*dx),d.height);
        
        for(int i=0;i<myShip.totalSlots(Module.HI_SLOT);i++) {
        	Module m = myShip.getModule(Module.HI_SLOT, i);
        	if(m != null && m instanceof Weapon) {
                Weapon w = (Weapon)m;
                g.setColor(green);
                for(x = 0, gx = -10*dx; x <d.width;x++, gx+=dx) {
                    g.drawLine(x, (int)(w.calculateAtRange(gx)*myShip.calculateGenericDps()*dy), x, d.height);
                }
            }
        }
    }
    
    public void setShip(Ship ship) {
        myShip = ship;
        
        renderGraph();
    }
    
    public void paintComponent(Graphics g) {
        
        Rectangle clip = g.getClipBounds();
        g.setColor(Color.WHITE);
        g.fillRect(clip.x,clip.y,clip.width,clip.height);
        
        if(cachedImage != null)
            g.drawImage(cachedImage, 0, 0, null);
    }
 
    public void mouseMoved(MouseEvent e) {
        Dimension d = getPreferredSize();
        float dx = (dmax-dmin)/d.width;
        float dy = (rmax-rmin)/d.height;
        
        setToolTipText("Range: "+(dx*e.getX()-dx*10)+"m / DPS: "+((d.height-e.getY())*dy));
    }
    
    public void mouseDragged(MouseEvent e) {
    }
}
