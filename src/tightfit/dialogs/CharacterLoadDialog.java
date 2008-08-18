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

import java.util.*;

import javax.swing.*;

import tightfit.character.Character;

public class CharacterLoadDialog extends JDialog implements ActionListener {
    
    private JCheckBox [] list;
    
    public CharacterLoadDialog(Dialog parent, Character [] chars) {
        super(parent);
        setModal(true);
        JPanel base = new JPanel();
        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));
        
        base.add(new JLabel("There are "+chars.length+" character(s) on this account"));
        
        JPanel choices = new JPanel(new GridLayout(chars.length,2));
        choices.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Choices"),
                    BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        
        list = new JCheckBox[chars.length+1];
                    
        for(int c=0;c<chars.length;c++) {
            JPanel p = new JPanel();
            list[c] = new JCheckBox(chars[c].name + " ("+chars[c].charId+")");
            //list[c].addActionListener(this);
            p.add(list[c]);
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
 
    public Iterator getChecked() {
        LinkedList charList = new LinkedList();
        for(int i=0;i<list.length-1;i++) {
            if(list[i].isSelected()) {
                charList.add(list[i].getText());
            }
        }
        
        return charList.iterator();
    }
    
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }
}
