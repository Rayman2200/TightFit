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

import java.awt.GridLayout;
import java.awt.datatransfer.Transferable;

import java.util.Iterator;

import javax.swing.*;
import javax.swing.tree.*;

import tightfit.item.Database;
import tightfit.item.Group;

public class MarketDialog extends JDialog {

    JTree marketTree;
    
    public MarketDialog(Database db) {
        JPanel panel = new JPanel(new GridLayout());
        DefaultMutableTreeNode top =
            new DefaultMutableTreeNode("Galactic Market");
        createNodes(top, db);
        
        marketTree = new JTree(top);
        
        marketTree.setDragEnabled(true);
        marketTree.setTransferHandler(new MarketTransferHandler());
        
        panel.add(marketTree);
        
        JDialog market = new JDialog();
        
        market.add(panel);
        
        market.pack();
        market.setVisible(true);
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
