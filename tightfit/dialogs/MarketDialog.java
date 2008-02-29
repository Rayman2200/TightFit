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

import java.io.IOException;

import java.awt.*;
import java.awt.datatransfer.Transferable;

import java.util.Iterator;

import javax.swing.*;
import javax.swing.tree.*;

import tightfit.Resources;
import tightfit.item.Database;
import tightfit.item.Group;

public class MarketDialog extends JDialog {

    private JTree marketTree;
    private DefaultMutableTreeNode top;
    private Database myDb; 
    
    public MarketDialog(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout());
        top =
            new DefaultMutableTreeNode("Galactic Market (Loading...)");
        
        marketTree = new JTree(top);
        
        marketTree.setDragEnabled(true);
        marketTree.setTransferHandler(new MarketTransferHandler());
        
        try {
            marketTree.setFont(Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("stan07_57.ttf")).deriveFont(8f));
        } catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        panel.add(marketTree);
        
        JDialog market = new JDialog(parent);
        
        market.add(panel);
        
        market.pack();
        market.setVisible(true);
    }

    public void updateTree(Database db) {
        top.setUserObject("Galactic Market");
        
        myDb = db;
        
        createNodes(top, myDb);
        
    }
    
    private void createNodes(DefaultMutableTreeNode top, Database db) {
    	Iterator itr = db.getGroups();
        while(itr.hasNext()) {
        	Group g = (Group)itr.next();
        	if(g.catId >= 6 && g.catId <= 8) {
        		top.add(new DefaultMutableTreeNode(g.name));
        		//query modules in the group
        	}
        }
    }
    
    private class MarketTransferHandler extends TransferHandler {
    	public Icon getVisualRepresentation(Transferable t) {
    		return null;
    	}
    };
}
