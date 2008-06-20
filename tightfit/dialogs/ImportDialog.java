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

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import tightfit.TightFit;
import tightfit.io.EFTShipParser;
import tightfit.ship.Ship;

public class ImportDialog extends JDialog implements ActionListener {
    
    private TightFit editor;
    private JTextArea jta = new JTextArea();
    
    public ImportDialog(TightFit editor) {
        this.editor = editor;
        setTitle("Import");
        init();
        pack();
    }
    
    private void init() {
        JPanel panel = new JPanel();
        JButton imp = new JButton("Import");
        imp.addActionListener(this);
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        try {
            JScrollPane jsp = new JScrollPane();
            jsp.setPreferredSize(new Dimension(200, 250));
            jsp.getViewport().setView(jta);
            panel.add(jsp);
            panel.add(imp);
            setContentPane(panel);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        EFTShipParser parser = new EFTShipParser();
        
        try {
            editor.setShip(parser.parse(jta.getText()));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
