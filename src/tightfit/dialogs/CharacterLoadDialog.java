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
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import tightfit.TightPreferences;
import tightfit.character.Character;

public class CharacterLoadDialog extends JDialog implements ActionListener {
    
    public CharacterLoadDialog(Dialog parent, Character [] chars) {
        super(parent);
        JPanel base = new JPanel();
        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));
        
        base.add(new JLabel("There are "+chars.length+" character(s) on this account"));
        
        JPanel choices = new JPanel(new GridLayout(chars.length,2));
        choices.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Choices"),
                    BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        
        for(int c=0;c<chars.length;c++) {
            JPanel p = new JPanel();
            JCheckBox cb = new JCheckBox(chars[c].name);
            cb.addActionListener(this);
            p.add(cb);
            choices.add(p);
            choices.add(Box.createGlue());
        }
        base.add(choices);
        
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton load = new JButton("Load");
        load.addActionListener(this);
        buttons.add(load);
        base.add(buttons);
        
        add(base);
        pack();
    }
 
    public void actionPerformed(ActionEvent e) {
        
    }
}
