/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.item;

import java.io.InputStream;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import tightfit.Resources;

public class Database {

	private static Database instance;
	
	private Hashtable cache = new Hashtable();
	private LinkedList groups = new LinkedList();
	private LinkedList marketGroups = new LinkedList();
	
	public Database(String resource) throws Exception {
		
		InputStream in = Resources.getResource(resource);
		
        
        
		SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            //factory.setIgnoringComments(true);
            //factory.setIgnoringElementContentWhitespace(true);
            //factory.setExpandEntityReferences(false);
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            parser.parse(in, new DatabaseParserHandler(this));
            //System.out.println("free "+Runtime.getRuntime().freeMemory());
            in.close();
            //System.out.println("free "+Runtime.getRuntime().freeMemory());
            System.gc(); //don't give me that crap about *suggests*, this frees like 50Mb of memory
            //System.out.println("free "+Runtime.getRuntime().freeMemory());
        } catch (SAXException e) {
            e.printStackTrace();
            throw new Exception("Error while parsing database file: " +
                    e.toString());
        }
		
	}
	
	public static Database getInstance() throws Exception {
		if(instance == null)
			instance = new Database("eveDb.xml");
		return instance;
	}
	
	public Group getGroup(int id) {
		Iterator itr = groups.iterator();
		while(itr.hasNext()) {
			Group g = (Group) itr.next();
			if(g.groupId == id)
				return g;
		}
		return null;
	}
	
	public Iterator getGroups() {
		return groups.iterator();
	}
	
	public Iterator getMarketGroups() {
		return marketGroups.iterator();
	}
	
    public Item getType(String name) {
        return (Item)cache.get(name.toLowerCase());
    }
    
    public LinkedList getTypeByGroup(int gid) {
    	LinkedList l = new LinkedList();
    	Enumeration types = cache.elements();
    	while(types.hasMoreElements()) {
    		Item item = (Item)types.nextElement();
    		if(item.groupId == gid)
    			l.addLast(item);
    	}
    	return l;
    }
    
    public LinkedList getTypeByMarketGroup(int gid) {
    	LinkedList l = new LinkedList();
    	Enumeration types = cache.elements();
    	while(types.hasMoreElements()) {
    		Item item = (Item)types.nextElement();
    		if(item.marketGroupId == gid)
    			l.addLast(item);
    	}
    	return l;
    }
    
    private Group unmarshalGroup(Node t) {
    	Group g = new Group();
    	
    	NamedNodeMap nnm = t.getAttributes();
    	if(nnm != null) {
	    	Node n = nnm.getNamedItem("name");
	    	if(n != null)
	    		g.name = n.getNodeValue();
	    	n = nnm.getNamedItem("groupid");
	    	if(n != null)
	    		g.groupId = (int)Float.parseFloat(n.getNodeValue());
	    	n = nnm.getNamedItem("catid");
	    	if(n != null)
	    		g.catId = (int)Float.parseFloat(n.getNodeValue());
	    	n = nnm.getNamedItem("graphicid");
	    	if(n != null)
	    		g.graphicId = (int)Float.parseFloat(n.getNodeValue());
    	}
    	
    	return g;
    }
    
    private class DatabaseParserHandler extends DefaultHandler {
        
        private Database myDb;
        
        private Object currentElement;
        
        public DatabaseParserHandler(Database instance) {
            myDb = instance;
        }
        
        public void startDocument() throws SAXException {
            System.out.print("building DB... ");
        }
        
        public void endDocument() throws SAXException {
            System.out.println("DONE");
        }
        
        public void startElement(String name, String localName, String qName, Attributes attrs) throws SAXException {
            if(qName.equalsIgnoreCase("type")) {
                Item type = new Item();
                currentElement = type;
                unmarshalType(type, attrs);
                myDb.cache.put(type.name.toLowerCase(), type);
            } else if(qName.equalsIgnoreCase("attribute")) {
                Item m = (Item)currentElement;
                m.attributes.put(attrs.getValue("name"),
                            attrs.getValue("value"));
            } else if(qName.equalsIgnoreCase("marketgroup")) {
                Group g = unmarshalMarketGroup(attrs);
                myDb.marketGroups.addLast(g);
            } else if(qName.equalsIgnoreCase("group")) {
                Group g = unmarshalGroup(attrs);
                myDb.groups.addLast(g);
            }
        }
        
        public void endElement(String name) throws SAXException {}
        
        public void characters(char buf[], int offset, int len) throws SAXException {
        }
        
        private void unmarshalType(Item type, Attributes attrs) {
            type.name = attrs.getValue("name");
            type.mass = Float.parseFloat(attrs.getValue("mass"));
            type.volume = Float.parseFloat(attrs.getValue("volume"));
            type.capacity = Float.parseFloat(attrs.getValue("capacity"));
            type.typeId = (int)Float.parseFloat(attrs.getValue("typeid"));
            type.groupId = (int)Float.parseFloat(attrs.getValue("groupid"));
            type.marketGroupId = (int)Float.parseFloat(attrs.getValue("marketgroupid"));
            type.graphicId = attrs.getValue("graphicid");
        }
        
        private Group unmarshalMarketGroup(Attributes attrs) {
            MarketGroup g = new MarketGroup();
            g.name = attrs.getValue("name");
            g.groupId = (int)Float.parseFloat(attrs.getValue("marketGroupId"));
            g.parentGroupId = (int)Float.parseFloat(attrs.getValue("parentGroupId"));
            g.graphicId = (int)Float.parseFloat(attrs.getValue("graphicId"));
            return g;
        }
        
        private Group unmarshalGroup(Attributes attrs) {
            Group g = new Group();
            g.name = attrs.getValue("name");
            g.groupId = (int)Float.parseFloat(attrs.getValue("groupid"));
            g.catId = (int)Float.parseFloat(attrs.getValue("catid"));
            g.graphicId = (int)Float.parseFloat(attrs.getValue("graphicid"));
            return g;
        }
    }
}
