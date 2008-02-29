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

import java.io.IOException;

import java.awt.Dimension;

import javax.swing.*;

import tightfit.Resources;

class ImageButton extends JButton {

    public ImageButton(ImageIcon i) {
        super(i);
        
        setPreferredSize(new Dimension(i.getImage().getWidth(null), i.getImage().getHeight(null)));
        
        setBorderPainted(false);
    }
    
    public ImageButton(String img) throws IOException {
        this(new ImageIcon(Resources.getImage(img)));
    }
    
    
}
