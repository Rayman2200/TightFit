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

class ExportDialog extends JDialog implements ActionListener {
    
    private Ship ship;
    
    public ExportDialog(Ship ship) {
        this.ship = ship;
        init();
    }
    
    private void init() {
        JTextArea jta = new JTextArea();
        GridBagConstraints c = new GridBagConstraints();
        ByteArrayOutputStream sos = new ByteArrayOutputStream();
        EFTShipWriter writer = new EFTShipWriter(sos);
        JButton close = new JButton("Close");
        close.addActionListener(this);
        
        setLayout(new GridBagLayout());
        
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 1;
        
        try {
            writer.writeShip(ship);
            
            jta.setText(sos.toString());
            c.gridwidth = GridBagConstraints.REMAINDER;
            add(jta, c);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actionPerformed(ActionEvent e) {
    
    }
}
