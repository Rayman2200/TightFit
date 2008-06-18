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

import java.awt.Frame;

import tightfit.Resources;
import tightfit.widget.SlickLayout;

public class SplashDialog extends JDialog {

    public SplashDialog(Frame frame) {
        super(frame);
        setUndecorated(true);
        setResizable(false);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new SlickLayout());
        panel.add(new JLabel(Resources.getIcon("splash.png")));
        
        JLabel title = new JLabel(Resources.getVersionString());
        try {
            title.setFont(Resources.getFont("agencyr.ttf").deriveFont(18f));
        } catch(Exception e) {}
        title.setLocation(10,10);
        panel.add(title);
        
        setContentPane(panel);
        
        pack();
    }
}
