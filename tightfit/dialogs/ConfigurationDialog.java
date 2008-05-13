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

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import tightfit.TightFit;
import tightfit.TightPreferences;
import tightfit.character.Character;

public class ConfigurationDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	JComboBox currentChar;
	
	private TightFit editor;
	
	public ConfigurationDialog(TightFit editor) {
		super(editor.appFrame, "Configuration", false);
		
		this.editor = editor;
		
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
        c.gridx = 1; c.weightx = 1;
        currentChar = new JComboBox();
        charPanel.add(currentChar, c);
        JButton load = new JButton("Load Character");
        load.addActionListener(this);
        load.setActionCommand("loadchar");
        c.gridx = 2;
        charPanel.add(load, c);
        
		JPanel ioPanel = new JPanel();
		JPanel prefPanel = new JPanel();
		
		jtp.addTab("Character", charPanel);
		
		
		
		jtp.addTab("Preferences", prefPanel);
		jtp.addTab("Import/Export", ioPanel);
		
		add(jtp);
		
		setPreferredSize(new Dimension(350,300));
		pack();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("loadchar")) {
			JFileChooser chooser = new JFileChooser();
			int ret = chooser.showOpenDialog(editor.appFrame);
			if(ret == JFileChooser.APPROVE_OPTION) {
				Character c = new Character();
				try {
					c.parse(chooser.getSelectedFile().getAbsolutePath());
					editor.setChar(c);
					TightPreferences.root();
				} catch (Exception e1) {
				}
		    }
		} 
	}
	
	
}
