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

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import tightfit.Resources;
import tightfit.module.Module;
import tightfit.ship.Ship;

public class Database {

	private Hashtable cache = new Hashtable();
	private LinkedList groups = new LinkedList();
	
	public Database(String resource) throws Exception {
		
		InputStream in = Resources.getResource(resource);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc;
        try {
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(in, ".");
        } catch (SAXException e) {
            e.printStackTrace();
            throw new Exception("Error while parsing map file: " +
                    e.toString());
        }
		
        Node dbNode = doc.getDocumentElement();
        if (!"database".equals(dbNode.getNodeName())) {
            throw new Exception("not a value TightFit database");
        }
        
        Node item;
        NodeList l = doc.getElementsByTagName("type");
        for (int i = 0; (item = l.item(i)) != null; i++) {
        	Item type = unmarshalType(item);
        	switch(item.getCategory()) {
        		case 6:
        		{
        			//It's a ship...
        			Ship ship = new Ship(type);
            		cache.put(ship.name, ship);
        		}break;
        		case 7:
        		{
        			//It's a module
            		Module module = new Module(type);
            		cache.put(module.name, module);
        		}break;
        		case 8:
        		{
        			//It's ammo! \o/
        			
        		}break;
        	}
        }
	}
	
	private static String getAttributeValue(Node node, String attribname) {
        NamedNodeMap attributes = node.getAttributes();
        String att = null;
        if (attributes != null) {
            Node attribute = attributes.getNamedItem(attribname);
            if (attribute != null) {
                att = attribute.getNodeValue();
            }
        }
        return att;
    }

    private static int getAttribute(Node node, String attribname, int def) {
        String attr = getAttributeValue(node, attribname);
        if (attr != null) {
            return Integer.parseInt(attr);
        } else {
            return def;
        }
    }
    
    private void getProperties(Item i, Node t) {
    	NamedNodeMap nnm = t.getAttributes();
    	if(nnm != null) {
	    	Node n = nnm.getNamedItem("mass");
	    	if(n != null)
	    		i.mass = Float.parseFloat(n.getNodeValue());
	    	n = nnm.getNamedItem("volume");
	    	if(n != null)
	    		i.volume = Float.parseFloat(n.getNodeValue());
	    	n = nnm.getNamedItem("capacity");
	    	if(n != null)
	    		i.capacity = Float.parseFloat(n.getNodeValue());
	    	n = nnm.getNamedItem("name");
	    	if(n != null)
	    		i.name = n.getNodeValue();
	    	n = nnm.getNamedItem("typeid");
	    	if(n != null)
	    		i.typeId = (int)Float.parseFloat(n.getNodeValue());
	    	n = nnm.getNamedItem("groupid");
	    	if(n != null)
	    		i.groupId = (int)Float.parseFloat(n.getNodeValue());
	    	n = nnm.getNamedItem("marketgroupid");
	    	if(n != null)
	    		i.marketGroupId = (int)Float.parseFloat(n.getNodeValue());
    	}
    }
    
    private static void readAttributes(NodeList children, Item item) {
    	for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if ("attribute".equalsIgnoreCase(child.getNodeName())) {
            	item.attributes.put(getAttributeValue(child, "name"),
                        getAttributeValue(child, "value"));
            }
            if("description".equalsIgnoreCase(child.getNodeName())) {
            	item.attributes.put("description", child.getFirstChild().getNodeValue());
            }
    	}
    }
    
    private Item unmarshalType(Node t) {
    	Item type = new Item();
    	
    	getProperties(type, t);
    	
    	NodeList children = t.getChildNodes();
    	
    	readAttributes(children, type);
    	
    	return type;
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
    	}
    	
    	return g;
    }
}
