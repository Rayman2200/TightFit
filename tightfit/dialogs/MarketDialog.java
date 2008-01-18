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
        
    }
}
