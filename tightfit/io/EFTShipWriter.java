/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */
 
package tightfit.io;

import java.io.*;

import tightfit.module.Module;
import tightfit.ship.Ship;

public class EFTShipWriter {

    private OutputStream stream;

    public EFTShipWriter(String filename) throws Exception {
        stream = new FileOutputStream(new File(filename));
    }
    
    public EFTShipWriter(OutputStream s) {
        stream = s;
    }
    
    public void writeShip(Ship ship) throws IOException {
        Writer w = new OutputStreamWriter(stream);
        
        w.write("["+ship.name+","+ship.title+"]\n");
        
        for(int rack=0;rack<4;rack++) {
            for(int slot=0;slot<ship.totalSlots(rack);slot++) {
                Module m = ship.getModule(rack, slot);
                
                if(m != null) {
                    w.write(m.name);
                    if(m.acceptsCharges() && m.getCharge().name.length() > 1) {
                        w.write(","+m.getCharge().name+"\n");
                    } else {
                        w.write("\n");
                    }
                } else {
                    w.write("Empty\n");
                }
            }
            w.write("\n");
        }
        
        w.flush();
    }
    
}
