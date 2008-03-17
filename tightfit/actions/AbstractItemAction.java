package tightfit.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tightfit.item.Item;

public abstract class AbstractItemAction extends AbstractAction {

	protected Item myItem;
	
	public AbstractItemAction(String name, Item i) {
		super(name);
		myItem = i;
	}
	
	public void actionPerformed(ActionEvent ae) {
		doAction(ae);
	}

	protected abstract void doAction(ActionEvent ae);
}
