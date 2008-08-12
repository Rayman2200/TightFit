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

import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.event.*;

import tightfit.*;
import tightfit.TightPreferences;
import tightfit.character.Character;

public class ConfigurationDialog extends JDialog implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	
	private JComboBox currentCharBox;
	private Character currentChar;
    private JSlider redColor, greenColor, blueColor;
    private JLabel redColorLabel, greenColorLabel, blueColorLabel;
    private JPanel colorPrev;
    private JTextField userIdText = new JTextField(5);
    private JTextField apiKeyText = new JTextField(5);
    
    private TightFit editor;
	
	public ConfigurationDialog(TightFit editor) {
		super(editor.appFrame, "Configuration", false);
		
		this.editor = editor;
		currentChar = editor.getChar();
        
		init();
	}
	
	private void init() {
		JTabbedPane jtp = new JTabbedPane();
		
        
		JPanel ioPanel = new JPanel();
		
		jtp.addTab("Character", initCharacters());
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
        colorPanel.setLayout(new GridLayout(3,2,0,0));
        colorPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Color"),
                    BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        
        Color color = Color.decode(TightPreferences.node("prefs").get("bgColor", "#30251A"));

        redColor = new JSlider(JSlider.HORIZONTAL, 0, 255, color.getRed()); 
        redColor.addChangeListener(this);
        redColorLabel = new JLabel(""+color.getRed());
        greenColor = new JSlider(JSlider.HORIZONTAL, 0, 255, color.getGreen());
        greenColorLabel = new JLabel(""+color.getGreen());
        greenColor.addChangeListener(this);
        blueColor = new JSlider(JSlider.HORIZONTAL, 0, 255, color.getBlue());
        blueColorLabel = new JLabel(""+color.getBlue());
        blueColor.addChangeListener(this);
        colorPrev = new JPanel();
        colorPrev.setPreferredSize(new Dimension(32,32));
        colorPrev.setBackground(color);
        
        colorPanel.add(redColor);
        colorPanel.add(redColorLabel);
        colorPanel.add(new JLabel());
        colorPanel.add(greenColor);
        colorPanel.add(greenColorLabel);
        colorPanel.add(colorPrev);
        colorPanel.add(blueColor);
        colorPanel.add(blueColorLabel);
        
        JPanel optPanel = new JPanel();
        
        
        prefPanel.add(colorPanel);
        prefPanel.add(optPanel);
        return prefPanel;
    }
    
    private JTabbedPane initCharacters() {
        JTabbedPane tabs = new JTabbedPane();        
        JPanel charPanel = new JPanel();
		GridBagConstraints c;
		JButton addbutton = new JButton("Add");
        userIdText.setText(TightPreferences.node("prefs").get("userid",""));
        apiKeyText.setText(TightPreferences.node("prefs").get("apikey",""));
        
        addbutton.setActionCommand("addchar");
        addbutton.addActionListener(this);
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
        
        JPanel addchar = new JPanel();
        addchar.setLayout(new GridLayout(3,2));
        addchar.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Add Character"),
                        BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        addchar.add(new JLabel("User ID:"));
        addchar.add(userIdText);
        addchar.add(new JLabel("API Key:"));
        addchar.add(apiKeyText);
        addchar.add(new JPanel());
        addchar.add(addbutton);
        c.gridx=2;
        charPanel.add(addchar, c);
        
        
        tabs.addTab("Character Settings", charPanel);
        
        int count = Integer.parseInt(TightPreferences.node("prefs").get("charcount", "0"));
        
        for(int i=0;i<count;i++) {
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
            tabs.addTab("charname", charStats);
        }
        return tabs;
    }
    
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("addchar")) {
            try {
                URL charListAPI = new URL("http://api.eve-online.com/account/Characters.xml.aspx?"+
                            "userID="+userIdText.getText()+"&apiKey="+apiKeyText.getText());
                
                CharacterLoadDialog charLoad = new CharacterLoadDialog(this, Character.parseCharacterList(charListAPI.openStream()));
                charLoad.show();
                
                loadNewCharacter("");
                //TODO: tabs.addTab(currentChar.name,
                TightPreferences.node("prefs").put("userid", userIdText.getText());
                TightPreferences.node("prefs").put("apikey", apiKeyText.getText());
            } catch(Exception e) {
            }
		} 
	}
	
	public void stateChanged(ChangeEvent e) {
        Color c = new Color(redColor.getValue(), greenColor.getValue(), blueColor.getValue());
        String color = Integer.toHexString(c.getRGB()).substring(2);
        //System.out.println(color);
        TightPreferences.node("prefs").put("bgColor", "#"+color);
        redColorLabel.setText(""+c.getRed());
        greenColorLabel.setText(""+c.getGreen());
        blueColorLabel.setText(""+c.getBlue());
        colorPrev.setBackground(c);
    }
    
    private void loadNewCharacter(String charId) {
        try {
            
            URL charAPI = new URL("http://api.eve-online.com/char/CharacterSheet.xml.aspx?"+
                        "userID="+userIdText.getText()+"&apiKey="+apiKeyText.getText()+
                        "&characterID="+charId);
            InputStream stream = charAPI.openStream();
            currentChar = new Character();
            currentChar.parse(stream);
            
            //do we have this char already?
            
            
            //for(int i=5;i>0;i--)
            //    TightPreferences.root().node("player").put("player"+i, TightPreferences.root().node("player").get("player"+(i-1), null));
            //TightPreferences.root().node("player").put("player0", chooser.getSelectedFile().getAbsolutePath());
        
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
