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

import javax.swing.*;

public abstract class Graph extends JPanel {
    
    protected Image cachedImage;
    
    protected float dmin, dmax, 
                    rmin, rmax;
    
    public abstract void renderGraph();
    
    protected void renderMeter() {
        
        Graphics2D g = (Graphics2D)cachedImage.getGraphics();
        g.setClip(0,0,cachedImage.getWidth(null), cachedImage.getHeight(null));
        Rectangle clip = g.getClipBounds();
        float [] dash = {1,1};
        
        g.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1, dash, 0));
        g.setColor(new Color(.9f, .9f, .9f, 1));
        
        for(int i=0;i<clip.width;i+=5) {
            g.drawLine(i,0,i,clip.height);
        }
        
        for(int i=0;i<clip.height;i+=5) {
            g.drawLine(0,i,clip.width,i);
        }
    }
    
    public void paintComponent(Graphics g) {
        if(cachedImage != null)
            g.drawImage(cachedImage, 0, 0, null);
    }
}
