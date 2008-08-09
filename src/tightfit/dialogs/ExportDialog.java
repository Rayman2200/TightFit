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

import tightfit.io.EFTShipWriter;
import tightfit.ship.Ship;

public class ExportDialog extends JDialog implements ActionListener {
    
    private Ship ship;
    
    public ExportDialog(Ship ship) {
        this.ship = ship;
        setTitle("Export");
        init();
        pack();
    }
    
    private void init() {
        JPanel panel = new JPanel();
        JTextArea jta = new JTextArea();
        GridBagConstraints c = new GridBagConstraints();
        ByteArrayOutputStream sos = new ByteArrayOutputStream();
        EFTShipWriter writer = new EFTShipWriter(sos);
        JButton close = new JButton("Close");
        close.addActionListener(this);
        
        panel.setLayout(new GridBagLayout());
        
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 1;
        
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        try {
            writer.writeShip(ship);
            
            jta.setText(sos.toString());
            JScrollPane jsp = new JScrollPane();
            jsp.setPreferredSize(new Dimension(200, 250));
            jsp.getViewport().setView(jta);
            c.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(jsp, c);
            c.gridy=1;
            panel.add(close, c);
            setContentPane(panel);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actionPerformed(ActionEvent e) {
    
    }
}
