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

import javax.swing.JDialog;
import javax.swing.JTree;
import javax.swing.tree.*;

public class MarketDialog extends JDialog {

    JTree marketTree;
    
    public MarketDialog() {
        
        DefaultMutableTreeNode top =
            new DefaultMutableTreeNode("Galactic Market");
        createNodes(top);
        
        marketTree = new JTree(top);
        
        
    }

    private void createNodes(DefaultMutableTreeNode top) {
        //TODO: query categories, spin thru, 
        //      query modules
    }
}
