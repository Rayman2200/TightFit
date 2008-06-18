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
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;

import javax.swing.*;

import tightfit.ship.Ship;
import tightfit.widget.*;
import tightfit.io.EFTShipParser;
import tightfit.item.*;
import tightfit.dialogs.*;
import tightfit.character.Character;

/**
 * Main Class
 *
 */
public class TightFit implements MouseListener, MouseMotionListener, KeyListener, ActionListener {

    public JFrame      appFrame;
    private FitPanel    thePanel;
    
    private Ship myShip;
    private Character myChar;
    private MarketDialog mdlg;
    private ConfigurationDialog configDialog;
    
    private TankPanel tankPanel;
    private DpsPanel dpsPanel;
    
    private Point mousePressLocation;
    
    private static final int APP_WIDTH = 680;
    private static final int APP_HEIGHT = 500;
    
    private static TightFit editor;
    
    public TightFit() {
        appFrame = new JFrame(Resources.getVersionString());
        appFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        appFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                //FIXME: exitAction.actionPerformed(null);
            }
        });
        appFrame.setContentPane(createContentPane());
        appFrame.setBackground(Color.decode(TightPreferences.node("prefs").get("bgColor", "#30251A")));
        appFrame.setSize(APP_WIDTH, APP_HEIGHT);
        appFrame.addKeyListener(this);
        appFrame.setUndecorated(true);
        appFrame.setResizable(false); 
        
        myShip = new Ship();
        myChar = new Character("Pilot");
        
        try {
			myChar.parse(TightPreferences.node("player").get("player0", null));
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        appFrame.setLocationRelativeTo(null);
        
        appFrame.pack();
        appFrame.setVisible(true); 
        
        mdlg = new MarketDialog(this);
        configDialog = new ConfigurationDialog(this);
        
        //new Thread(new Runnable() {
            //public void run() {
                initDatabase();
            //}
        //}).start();
    }
    
    public static TightFit getInstance() {
    	if(editor == null)
    		editor = new TightFit();
    	return editor;
    }
    
    private JPanel createContentPane() {
    	JPanel panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	thePanel = new FitPanel(this);
    	//build drag-n-drop slots, put them in the right places
    	thePanel.addMouseListener(this);
        thePanel.addMouseMotionListener(this);
    	thePanel.addKeyListener(this);
    	
    	panel.setBackground(Color.decode(TightPreferences.node("prefs").get("bgColor", "#30251A")));

        JTabbedPane jtp = new JTabbedPane();
        
        tankPanel = new TankPanel(this);
        tankPanel.setBackground(Color.decode(TightPreferences.node("prefs").get("bgColor", "#30251A")));
        
        dpsPanel = new DpsPanel(this);
        dpsPanel.setBackground(Color.decode(TightPreferences.node("prefs").get("bgColor", "#30251A")));
        
        jtp.setOpaque(false);
        jtp.addTab(Resources.getString("dialog.main.tab.tank"), tankPanel);
        jtp.addTab(Resources.getString("dialog.main.tab.dps"),dpsPanel);
        jtp.addTab(Resources.getString("dialog.main.tab.drones"),new JPanel());
        jtp.setPreferredSize(new Dimension(670,190));
        
        panel.add(thePanel);
        panel.add(jtp);
        
        return panel;
    }
    
    private void initDatabase() {
        
        try {
        	mdlg.updateTree(Database.getInstance());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setShip(Ship s) {
        if(myShip != null) {
            //TODO: ask to save fit
        }
        
    	myShip = s;
    	myShip.pilot = myChar;
        thePanel.setShip(myShip);
        myShip.addChangeListener(tankPanel);
        myShip.addChangeListener(dpsPanel);
        
        myShip.fireShipChange();
    }
    
    public Ship getShip() {
    	return myShip;
    }
    
    public Character getChar() {
    	return myChar;
    }
    
    public void mouseDragged(MouseEvent e) {
        if(mousePressLocation != null && mousePressLocation.y < 15) {
        	Point d = appFrame.getLocation();
            appFrame.setLocation(d.x + (e.getX() - mousePressLocation.x), d.y + (e.getY()-mousePressLocation.y));
            
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
    
    public void keyTyped(KeyEvent e) {
    	System.out.println(KeyEvent.getKeyModifiersText(e.getModifiers()) + "-"+e.getKeyCode());
	}

	public void keyPressed(KeyEvent e) {
		//CTRL-v
		System.out.println(KeyEvent.getKeyModifiersText(e.getModifiers()) + "-"+e.getKeyCode());
		
		if(KeyEvent.getKeyModifiersText(e.getModifiers()).equals("Ctrl") && e.getKeyCode() == 86) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); 
			Transferable clipData = clipboard.getContents(clipboard);
			String input = "";
			
		   if (clipData != null) {
			   try {
			       if (clipData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			    	   input = (String)(clipData.getTransferData(DataFlavor.stringFlavor));
			    	   setShip((new EFTShipParser()).parse(input));
			       }
			   } catch (Exception ex) {
				   //ex.printStackTrace();
			   }
		   }
		}
		
	}

	public void keyReleased(KeyEvent arg0) {	
	}
    
    /**
     * @param args
     */
    public static void main(String[] args) {
    	try{
    		TightFit.getInstance();
    	} catch(Throwable e) {
    		e.printStackTrace();
    	}
        
    }

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("close")) {
			appFrame.dispose();
			System.exit(0); //TODO: catch for saving!
		} else if(e.getActionCommand().equalsIgnoreCase("minimize")) {
			appFrame.setState(Frame.ICONIFIED);
		} else if(e.getActionCommand().equalsIgnoreCase("config")) {
			configDialog.setVisible(true);
		} else if(e.getActionCommand().equalsIgnoreCase("about")) {
			AboutDialog abt = new AboutDialog(appFrame);
			abt.setVisible(true);
		} else if(e.getActionCommand().equalsIgnoreCase("showinfo")) {
			ShowInfoDialog sid = new ShowInfoDialog(appFrame, myShip);
		} else if(e.getActionCommand().equalsIgnoreCase("strip")) {
			if(JOptionPane.showConfirmDialog(appFrame, Resources.getString("dialog.main.strip"), Resources.getString("dialog.main.strip.title"),JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				myShip.strip();
			}
		} else if(e.getActionCommand().equalsIgnoreCase("export")) {
            ExportDialog exd = new ExportDialog(myShip);
            exd.setVisible(true);
        }
		
	}

}
