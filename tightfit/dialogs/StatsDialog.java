/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.dialogs;

import java.awt.*;

import javax.swing.*;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.item.*;
import tightfit.ship.Ship;
import tightfit.character.Character;

public class StatsDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private TightFit editor;
    
    public StatsDialog(TightFit editor) {
        super();
        this.editor = editor;
        
        setTitle("Stats (Tank/DPS/Drones)");
        setUndecorated(true);
        
        init();
        
        pack();
        setVisible(true);
    }
    
    private void init() {
        JTabbedPane jtp = new JTabbedPane();
        
        
        
        add(jtp);
    }

}
