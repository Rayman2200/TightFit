/*
 *  TightFit (c) 2008 Adam Turk
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Adam Turk <aturk@biggeruniverse.com>
 */

package tightfit;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main Class
 *
 */
public class TightFit {

    private JFrame      appFrame;
    
    private static final int APP_WIDTH = 800;
    private static final int APP_HEIGHT = 600;
    
    public TightFit() {
        appFrame = new JFrame(Resources.getString("dialog.main.title"));
        appFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        appFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                exitAction.actionPerformed(null);
            }
        });
        appFrame.setContentPane(createContentPane());
        createMenuBar();
        appFrame.setBackground(new Color(.4f, .5f, .6f));  //grey, the color of insanity
        appFrame.setJMenuBar(menuBar);
        appFrame.setSize(APP_WIDTH, APP_HEIGHT);
        
        appFrame.setVisible(true);
    }
    
    private JPanel createContentPane() {
        return null;
    }
    
    private void initDatabase() {
        
    }
    
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        TightFit fit = new TightFit();
        
        
    }

}
