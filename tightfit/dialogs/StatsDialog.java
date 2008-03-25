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
import javax.swing.border.LineBorder;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.item.*;
import tightfit.ship.Ship;
import tightfit.character.Character;

public class StatsDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private TightFit editor;
    private Color bgColor = new Color(.07f, .25f, .43f);
    
    //private Image bgImg = Resources.getImage("panel.png");
    
    public StatsDialog(TightFit editor) {
        super();
        this.editor = editor;
        
        setTitle("Stats (Tank/DPS/Drones)");
        setUndecorated(true);
        //setBackground(bgColor);
        setPreferredSize(new Dimension(680,200));
        Point pt = editor.appFrame.getLocation();
        setLocation(pt.x, pt.y+500);
        init();
        
        pack();
        setVisible(true);
    }
    
    private void init() {
    	JPanel panel = new JPanel();
    	panel.setBackground(bgColor);
    	panel.setBorder(new LineBorder(Color.WHITE));
    	
        JTabbedPane jtp = new JTabbedPane();
        
        jtp.setOpaque(false);
        //jtp.setBackground(bgColor);
        jtp.addTab("Tank", new JPanel());
        jtp.addTab("DPS",new JPanel());
        jtp.addTab("Drones",new JPanel());
        jtp.setPreferredSize(new Dimension(670,190));
        
        panel.add(jtp);
        add(panel);
    }

}
