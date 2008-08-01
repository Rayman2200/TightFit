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

import javax.swing.*;

import tightfit.item.*;
import tightfit.module.*;
import tightfit.ship.Ship;

public class DamageRangeGraph extends Graph {

    protected Ship myShip;
    
    public DamageRangeGraph() {
    }
    
    public void renderGraph() {
        Dimension d = getPreferredSize();
        
        float dx = (dmax-dmin)/d.width;
        float dy = (rmax-rmin)/d.height;
        
        cachedImage = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)cachedImage.getGraphics();
        
        renderMeter();
        
        
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
    
}
