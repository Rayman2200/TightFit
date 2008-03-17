package tightfit.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import tightfit.dialogs.ShowInfoDialog;
import tightfit.item.Item;

public class ShowInfoAction extends AbstractAction {
	private Item myItem;
	private JDialog parent;
	
	public ShowInfoAction(JDialog parent, Item item) {
		super("Show Info");
		myItem = item;
		this.parent = parent;
	}
	
	public void actionPerformed(ActionEvent ae) {
		ShowInfoDialog sid = new ShowInfoDialog(parent, myItem);
		
	}

}
