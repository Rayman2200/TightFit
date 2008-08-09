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

import java.awt.Color;
import java.awt.Graphics2D;

public final class WidgetHelper {
	private static Color shadow = new Color(.1f,.1f,.1f,.85f);
	 
	public static void drawShadowedString(Graphics2D g2d, String s, float x, float y, Color c) {
        g2d.setColor(shadow);
        g2d.drawString(s, x+1, y+1);
        g2d.setColor(c);
        g2d.drawString(s, x, y);
    }
    
    public static void drawShadowedStringCentered(Graphics2D g2d, String s, float x, float y, Color c) {
    	x -= g2d.getFontMetrics().getStringBounds(s, g2d).getWidth()/2.0f;
    	g2d.setColor(shadow);
        g2d.drawString(s, x+1, y+1);
        g2d.setColor(c);
        g2d.drawString(s, x, y);
    }
    
    public static String formatFloat(float n, int sig) {
    	return String.format("%1$."+sig+"f", new Object[]{new Float(n)});
    }
}
