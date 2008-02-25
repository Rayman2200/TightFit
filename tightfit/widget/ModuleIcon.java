package tightfit.widget;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.event.MouseEvent;

import tightfit.module.Module;

public class ModuleIcon extends JLabel {
	
	private Module myModule;
	
	public ModuleIcon(Module m) {
		myModule = m;
		
		
	}
	
	protected void processMouseEvent(MouseEvent e) {
		if(e.isPopupTrigger()) {
			
		}
	}
}
