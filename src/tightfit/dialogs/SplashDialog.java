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

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;

import tightfit.Resources;
import tightfit.widget.SlickLayout;

public class SplashDialog extends JDialog {

    public SplashDialog(Frame frame) {
        super(frame);
        setUndecorated(true);
        setResizable(false);
        
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(420,300));
        panel.setLayout(new SlickLayout());
        
        JLabel title = new JLabel(Resources.getVersionString());
        try {
            title.setFont(Resources.getFont("agencyr.ttf").deriveFont(38f));
        } catch(Exception e) {}
        panel.add(title);
        panel.add(new JLabel(Resources.getIcon("splash.png")));
        title.setForeground(Color.white);
        title.setLocation(245,245);
        
        setContentPane(panel);
        
        pack();
        setLocationRelativeTo(null);
    }
}
