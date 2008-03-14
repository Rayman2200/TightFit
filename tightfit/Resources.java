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

import java.util.Hashtable;
import java.util.ResourceBundle;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * This class implements static accessors to common editor resources. These
 * currently include icons, fonts, and internationalized strings.
 *
 * @version $Id$
 */
public final class Resources {
    // The resource bundle used by this class
    private static final ResourceBundle resourceBundle =
            ResourceBundle.getBundle(
                    Resources.class.getPackage().getName() + ".resources.gui");

    private static Resources instance;
    
    private Hashtable cache;
    
    // Prevent instanciation
    private Resources() {
    	cache = new Hashtable();
    }

    private static Resources getInstance() {
    	if(instance == null)
    		instance = new Resources();
    	return instance;
    }
    
    /**
     * Retrieves a string from the resource bundle in the default locale.
     *
     * @param key the key for the desired string
     * @return the string for the given key
     */
    public static String getString(String key) {
        return resourceBundle.getString(key);
    }

    /**
     * Loads an image from the resources directory. This directory is part of
     * the distribution jar.
     *
     * @param filename the filename relative from the resources directory
     * @return A BufferedImage instance of the image
     * @throws IOException if an error occurs during reading
     * @throws IllegalArgumentException when the resource could not be found
     */
    public static Image getImage(String filename) throws IOException,
            IllegalArgumentException {
    	if(getInstance().getCachedResource(filename) == null)
    		getInstance().addCachedResource(filename, ImageIO.read(getResource(filename)));
    	return (Image)getInstance().getCachedResource(filename);
    }

    /**
     * Loads the image using {@link #getImage(String)} and uses it to create
     * a new {@link ImageIcon} instance.
     *
     * @param filename the filename of the image relative from the
     *                 <code>resources</code> directory
     * @return the loaded icon, or <code>null</code> when an error occured
     *         while loading the image
     */
    public static Icon getIcon(String filename) {
        try {
            return new ImageIcon(getImage(filename));
        } catch (IOException e) {
            System.out.println("Failed to load as image: " + filename);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to load resource: " + filename);
        }
        return null;
    }
  
    public static Font getFont(String filename) throws FontFormatException, IOException {
    	if(getInstance().getCachedResource(filename) == null)
    		getInstance().addCachedResource(filename, Font.createFont(Font.TRUETYPE_FONT, Resources.getResource(filename)));
    	return (Font)getInstance().getCachedResource(filename);
    }
    
    public static InputStream getResource(String filename) {
    	return Resources.class.getResourceAsStream("resources/" + filename);
    }
    
    private Object getCachedResource(String filename) {
    	return cache.get(filename);
    }
    
    private void addCachedResource(String filename, Object res) {
    	cache.put(filename, res);
    }
}
