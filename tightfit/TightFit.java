/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.*;

import tightfit.ship.Ship;
import tightfit.widget.*;

/**
 * Main Class
 *
 */
public class TightFit {

    private JFrame      appFrame;
    
    private Ship myShip;
    
    private static final int APP_WIDTH = 800;
    private static final int APP_HEIGHT = 600;
    
    public TightFit() throws IllegalArgumentException, IOException {
        appFrame = new JFrame(Resources.getString("dialog.main.title"));
        appFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        appFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                //FIXME: exitAction.actionPerformed(null);
            }
        });
        appFrame.setContentPane(createContentPane());
        appFrame.setBackground(new Color(.4f, .5f, .6f));  //blue-grey
        appFrame.setSize(APP_WIDTH, APP_HEIGHT);
        
        appFrame.pack();
        appFrame.setVisible(true);
    }
    
    private JPanel createContentPane() throws IllegalArgumentException, IOException {
    	JPanel main = new JPanel();
    	main.setLayout(new OverlayLayout(main));
    	
    	main.add(new FitPanel(this));
    	//build drag-n-drop slots, put them in the right places
    	
    	
        return main;
    }
    
    private void initDatabase() {
        
    }
    
    public void setShip(Ship s) {
    	myShip = s;
    	//TODO: fire an event
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
    	try{
    		TightFit fit = new TightFit();
    	} catch(Throwable e) {
    		e.printStackTrace();
    	}
        
    }

}
