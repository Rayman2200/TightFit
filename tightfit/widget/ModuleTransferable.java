package tightfit.widget;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import tightfit.module.Module;

public class ModuleTransferable implements Transferable {

	private Module myModule;
	
	public ModuleTransferable(Module m) {
		myModule = m;
	}
	
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor list[] = new DataFlavor[3];
		list[0] = new DataFlavor(Module.class, "Module");
		list[1] = DataFlavor.imageFlavor;
		list[2] = DataFlavor.stringFlavor;
		return list;
	}

	public boolean isDataFlavorSupported(DataFlavor flav) {
		if(flav.getRepresentationClass() == String.class || 
				flav.getRepresentationClass() == Module.class ||
				flav.getRepresentationClass() == java.awt.Image.class) {
			return true;
		}
		return false;
	}

	public Object getTransferData(DataFlavor flav) throws UnsupportedFlavorException, IOException {
		if(flav.getRepresentationClass() == String.class) {
			return myModule.name;
		} else if(flav.getRepresentationClass() == Module.class) {
			return myModule;
		} else if (flav.getRepresentationClass() == java.awt.Image.class) {
			return null; //TODO: return the icon image
		}
		throw new UnsupportedFlavorException(flav);
	}

}
