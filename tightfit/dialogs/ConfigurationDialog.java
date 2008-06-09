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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import tightfit.*;
import tightfit.TightPreferences;
import tightfit.character.Character;

public class ConfigurationDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	JComboBox currentCharBox;
	Character currentChar;
    
	private TightFit editor;
	
	public ConfigurationDialog(TightFit editor) {
		super(editor.appFrame, "Configuration", false);
		
		this.editor = editor;
		currentChar = editor.getChar();
        
		init();
	}
	
	private void init() {
		JTabbedPane jtp = new JTabbedPane();
		JPanel charPanel = new JPanel();
		GridBagConstraints c;
		
        charPanel.setLayout(new GridBagLayout());
        charPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1; c.weightx = 1;
        c.gridx=1;
        JPanel charImage = new JPanel();
        charImage.add(new JLabel(Resources.getIcon("icon74_14.png")));
        charPanel.add(charImage, c);
        JPanel charStats = new JPanel();
        charStats.setLayout(new BoxLayout(charStats, BoxLayout.Y_AXIS));
        charStats.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Stats"),
                    BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        charStats.add(new JLabel("Name:         "+currentChar.name));
        charStats.add(new JLabel("Skillpoints:  "+currentChar.sp));
        charStats.add(new JLabel("Skills known: "+currentChar.countSkills()));
        charStats.add(new JLabel("Skills at L5: "));
        c.gridx=2;
        charPanel.add(charStats, c);
        c.gridx=1; c.gridy = 2;
        
        currentCharBox = new JComboBox();
        charPanel.add(currentCharBox, c);
        JButton load = new JButton("Load Character");
        load.addActionListener(this);
        load.setActionCommand("loadchar");
        c.gridx = 2; c.gridy=3;
        charPanel.add(load, c);
        
		JPanel ioPanel = new JPanel();
		
		jtp.addTab("Character", charPanel);
		jtp.addTab("Preferences", initPrefs());
		jtp.addTab("Import/Export", ioPanel);
		
		add(jtp);
		
		setPreferredSize(new Dimension(350,300));
		pack();
	}

    private JPanel initPrefs() {
        JPanel prefPanel = new JPanel();
        prefPanel.setLayout(new BoxLayout(prefPanel, BoxLayout.Y_AXIS));
        
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.Y_AXIS));
        colorPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Color"),
                    BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        
        Color color = new Color(.07f, .25f, .43f);

        JSlider redColor = new JSlider(JSlider.HORIZONTAL, 0, 255, color.getRed()); 
        JSlider greenColor = new JSlider(JSlider.HORIZONTAL, 0, 255, color.getGreen());
        JSlider blueColor = new JSlider(JSlider.HORIZONTAL, 0, 255, color.getBlue());
        colorPanel.add(redColor);
        colorPanel.add(greenColor);
        colorPanel.add(blueColor);
        
        prefPanel.add(colorPanel);
        
        return prefPanel;
    }
    
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("loadchar")) {
			JFileChooser chooser = new JFileChooser();
			int ret = chooser.showOpenDialog(editor.appFrame);
			if(ret == JFileChooser.APPROVE_OPTION) {
				try {
                    currentChar = new Character();
					currentChar.parse(chooser.getSelectedFile().getAbsolutePath());
					for(int i=5;i>0;i--)
						TightPreferences.root().node("player").put("player"+i, TightPreferences.root().node("player").get("player"+(i-1), null));
					TightPreferences.root().node("player").put("player0", chooser.getSelectedFile().getAbsolutePath());
				} catch (Exception e1) {
				}
		    }
		} 
	}
	
	
}
