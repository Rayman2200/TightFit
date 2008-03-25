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
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.module.Module;


public abstract class Slot extends JPanel implements ActionListener {
	
    /* -- slot types -- */
    /** NO_SLOT */
    public static int NO_SLOT = -1;

    protected FitPanel parent;
    protected TightFit editor;
    
    protected boolean selected;
    
    public Slot(FitPanel panel, TightFit editor) {
        this.editor = editor;
        this.parent = panel;
        
        DropTarget dt = new DropTarget(this, DnDConstants.ACTION_COPY, parent, true, null);
        setDropTarget(dt);
        
        
    }
    
    public void setSelected(boolean s) {
    	if(selected != s) {
	    	selected = s;
	    	Point pt = getLocation();
	    	//FIXME: parent.repaint(0, pt.x, pt.y, 64, 64);
	    	parent.repaint();
    	}
    }
    
    public JToolTip createToolTip() {
    	MultiLintToolTip tip = new MultiLintToolTip();
    	tip.setComponent(this);
    	return tip;
    }
}
