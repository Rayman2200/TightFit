/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.util;

import java.util.*;

public class ModuleMap extends TreeMap {
    
    private Comparator comp;
    
    public ModuleMap(boolean descending) {
        super(new FloatComparator(descending));
    }
    
    public Comparator comparator() {
        return comp;
    }
    
    private static class FloatComparator implements Comparator {
        private boolean descending;
        
        public FloatComparator(boolean d) {
            descending = d;
        }
        
        public int compare(Object a, Object b) {
            Float fa = (Float)a;
            Float fb = (Float)b;
            
            if(descending)
                return fa.compareTo(fb);
            return -fa.compareTo(fb);
        }
        
        public boolean equals(Object o) {
            return false;
        }
    };
}
