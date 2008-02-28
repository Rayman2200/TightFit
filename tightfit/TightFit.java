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

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import tightfit.ship.Ship;
import tightfit.widget.*;
import tightfit.item.*;

/**
 * Main Class
 *
 */
public class TightFit implements MouseListener, MouseMotionListener {

    private JFrame      appFrame;
    private FitPanel    thePanel;
    
    private Ship myShip;
    
    private Point mousePressLocation;
    
    private static final int APP_WIDTH = 680;
    private static final int APP_HEIGHT = 500;
    
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
        
        appFrame.setUndecorated(true);
        appFrame.setResizable(false); 
        
        appFrame.setLocationRelativeTo(null);
        
        appFrame.pack();
        appFrame.setVisible(true);
        
        new Thread(new Runnable() {
            public void run() {
                initDatabase();
            }
        }).start();
    }
    
    private JPanel createContentPane() throws IllegalArgumentException, IOException {
    	thePanel = new FitPanel(this);
    	//build drag-n-drop slots, put them in the right places
    	thePanel.addMouseListener(this);
        thePanel.addMouseMotionListener(this);
    	
        return thePanel;
    }
    
    private void initDatabase() {
        
        try {
            Database.getInstance();
        
            setShip(new Ship(Database.getInstance().getType("myrmidon")));
            
            //TODO: notify
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setShip(Ship s) {
        if(myShip != null) {
            //TODO: ask to save fit
        }
        
    	myShip = s;
        thePanel.setShip(s);
    	//TODO: fire an event
    }
    
    public void mouseDragged(MouseEvent e) {
        if(mousePressLocation != null && mousePressLocation.y < 15) {
            appFrame.setLocation(e.getX() - mousePressLocation.x, e.getY()-mousePressLocation.y);
            
        }
    }
    
    public void mouseMoved(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
        mousePressLocation = new Point(e.getPoint());
        
    }
    
    public void mouseReleased(MouseEvent e) {
        mousePressLocation = null;
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
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
