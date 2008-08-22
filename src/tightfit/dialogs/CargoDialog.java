package tightfit.dialogs;

import javax.swing.JDialog;

import tightfit.ship.Ship;

public class CargoDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private Ship myShip;
	
	public CargoDialog(Ship ship) {
		super();
		
		myShip = ship;
	}
	
	
}
