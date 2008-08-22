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
import java.awt.event.*;

import javax.swing.*;
import javax.imageio.ImageIO;

import java.net.*;

import tightfit.character.Character;
import tightfit.character.CharacterChangeListener;
import tightfit.Resources;
import tightfit.TightFit;

public class CharacterInfoPanel extends JPanel implements CharacterChangeListener, ActionListener {
    
    private JCheckBox activeChar;
    
    private TightFit editor;
    private Character myChar;
    
    public CharacterInfoPanel(TightFit e, Character c) {
        myChar = c;
        editor = e;
        init();
    }

    private void init() {
        GridBagConstraints c;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        JPanel charInfo = new JPanel();
        JPanel charStats = new JPanel();
        charInfo.setLayout(new GridLayout(1,2));
        charStats.setLayout(new BoxLayout(charStats, BoxLayout.Y_AXIS));
        charStats.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Stats"),
                    BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        charStats.add(new JLabel("Name:         "+myChar.name));
        charStats.add(new JLabel("Skillpoints:  "+myChar.sp));
        charStats.add(new JLabel("Skills known: "+myChar.countSkills()));
        charStats.add(new JLabel("Skills at L5: "+myChar.countSkills(5)));
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1; c.weightx = 1;
        c.gridx=1;
        try {
            charInfo.add(new JLabel(getImage()), c);
        } catch(Exception ex) {
            charInfo.add(new JLabel(Resources.getIcon("icon74_14.png")), c);
            ex.printStackTrace();
        }
        c.gridx=2;
        charInfo.add(charStats, c);
        add(charInfo);
        activeChar = new JCheckBox("Set as Active Character");
        activeChar.addActionListener(this);
        add(activeChar);
        add(new JPanel());
    }
    
    private ImageIcon getImage() throws Exception {
        URL url = new URL("http://img.eve.is/serv.asp?s=256"+
                        "&c="+myChar.charId);
        
        return new ImageIcon(ImageIO.read(url.openStream()).getScaledInstance(128, 128, Image.SCALE_SMOOTH));
    }
    
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == activeChar) {
            editor.setCharacter(myChar);
        }
    }
    
    public void characterChanged(Character c) {
        activeChar.setSelected(c == myChar);
        activeChar.setEnabled(true);
        if(c == myChar)
            activeChar.setEnabled(false);
    }
}
