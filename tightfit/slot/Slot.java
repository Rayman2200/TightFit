/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.slot;

import javax.swing.JPanel;

import tightfit.module.Module;

public class Slot extends JPanel {
    /* -- slot types -- */
    /** NO_SLOT */
    public static int NO_SLOT = -1;
    /** RIG_SLOT */
    public static int RIG_SLOT = 0;
    /** HI_SLOT */
    public static int HI_SLOT  = 1;
    /** MED_SLOT */
    public static int MED_SLOT = 2;
    /** LOW_SLOT */
    public static int LOW_SLOT = 3;
    
    protected int myType;
    
    protected Module mounted;
    
    public Slot() {
        
    }
    
    public boolean mount(Module m) {
        if(mounted != null || m.slotRequirement != myType)
            return false;
        
        mounted = m;
        
        //TODO: fire mount event
        
        return true;
    }
}
