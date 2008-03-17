package tightfit.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;

import tightfit.dialogs.ShowInfoDialog;
import tightfit.item.Item;

public class ShowInfoAction extends AbstractItemAction {
	private Frame parent;
	
	public ShowInfoAction(Frame parent, Item item) {
		super("Show Info", item);
		this.parent = parent;
	}
	
	protected void doAction(ActionEvent ae) {
		ShowInfoDialog sid = new ShowInfoDialog(parent, myItem);
		
	}

}
