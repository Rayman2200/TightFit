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
import java.io.File;
import java.util.*;

import javax.swing.*;

import tightfit.ship.Ship;
import tightfit.widget.*;
import tightfit.io.EFTShipParser;
import tightfit.item.*;
import tightfit.module.Module;
import tightfit.dialogs.*;
import tightfit.character.Character;
import tightfit.character.CharacterChangeListener;

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
    
    private boolean marketHadFocus = true;
    
    private LinkedList listenerList = new LinkedList();
    
    private static TightFit editor;
    
    private static SplashDialog sd = new SplashDialog(null);
    
    public TightFit() {
        appFrame = new JFrame(Resources.getVersionString());
        appFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //some quick adapters
        appFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
            	if(myShip != null && myShip.totalSlots(Module.LOW_SLOT) != 0) {
                    saveShip(myShip);
                }
            }
            
            public void windowActivated(WindowEvent e) {
            	if(e.getOppositeWindow() != mdlg && marketHadFocus)
            		mdlg.transferFocus();
            }
            
            public void windowDeactivated(WindowEvent e) {
            	if(e.getOppositeWindow() == mdlg)
            		marketHadFocus = true;
            	else marketHadFocus = false;
            }
        });
        
        appFrame.setContentPane(createContentPane());
        appFrame.setBackground(Color.decode(TightPreferences.node("prefs").get("bgColor", "#30251A")));
        appFrame.setSize(APP_WIDTH, APP_HEIGHT);
        appFrame.addKeyListener(this);
        appFrame.setUndecorated(true);
        appFrame.setResizable(false); 
        
        //new Thread(new Runnable() {
            //public void run() {
                initDatabase();
            //}
        //}).start();
        
        
                
        myShip = new Ship();
        myChar = new Character("Pilot");
        
        appFrame.setLocationRelativeTo(null);
        appFrame.pack();
        
        appFrame.setVisible(true);
        
        mdlg = new MarketDialog(this);
        configDialog = new ConfigurationDialog(this);
        
        try {
			mdlg.updateTree(Database.getInstance());
		} catch (Exception e) {
		}
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
        
        dpsPanel = new DpsPanel();
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
        	Database.getInstance();
        	sd.setVisible(false);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setShip(Ship s) {
        if(myShip != null) {
            myShip.removeChangeListener(tankPanel);
            myShip.removeChangeListener(dpsPanel);
            
            if(myShip.totalSlots(Module.LOW_SLOT) != 0)
                saveShip(myShip);
        }
        
    	myShip = s;
    	myShip.pilot = myChar;
        addCharacterListener(myShip);
        thePanel.setShip(myShip);
        myShip.addChangeListener(tankPanel);
        myShip.addChangeListener(dpsPanel);
        
        myShip.fireShipChange();
    }
    
    public void setCharacter(Character c) {
        myChar = c;
        fireCharacterChanged();
    }
    
    public Ship getShip() {
    	return myShip;
    }
    
    public Character getChar() {
    	return myChar;
    }
    
    private void saveShip(Ship ship) {
    	String homedir = System.getProperty("user.home")+File.pathSeparator+"tightfit";
    	
    	if(ship.isChanged()) {
    		if(JOptionPane.showConfirmDialog(appFrame, Resources.getString("dialog.main.save"), Resources.getString("dialog.main.save.title"),JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
    			
    		}
    	}
    }
    
    public void addCharacterListener(CharacterChangeListener c) {
        listenerList.add(c);
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
    	if(e.getSource() instanceof CargoSlot) {
 			CargoDialog d = new CargoDialog(myShip);
 			d.setVisible(true);
 		}
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
    		sd.setVisible(true);
    		javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                	TightFit.getInstance();
                }
            });
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
        }  else if(e.getActionCommand().equalsIgnoreCase("import")) {
            ImportDialog imd = new ImportDialog(this);
            imd.setVisible(true);
        }
		
	}

    private void fireCharacterChanged() {
        Iterator itr = listenerList.iterator();
    	while(itr.hasNext()) {
    		((CharacterChangeListener)itr.next()).characterChanged(myChar);
    	}
    }
}
