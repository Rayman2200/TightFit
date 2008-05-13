/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit;

import java.util.prefs.Preferences;

public final class TightPreferences {
	
	private static final Preferences prefs = Preferences.userRoot().node("tightfit");

    // Prevent instanciation
    private TightPreferences() {
    }
	
    public static Preferences node(String pathName) {
        return prefs.node(pathName);
    }
	
    public static Preferences root() {
        return prefs;
    }
}
