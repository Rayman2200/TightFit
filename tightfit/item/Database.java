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
import org.xml.sax.SAXException;

import tightfit.Resources;

public class Database {

	private static Database instance;
	
	private Hashtable cache = new Hashtable();
	private LinkedList groups = new LinkedList();
	private LinkedList marketGroups = new LinkedList();
	
	public Database(String resource) throws Exception {
		
		InputStream in = Resources.getResource(resource);
		
        System.out.println("building DB... ");
        
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc;
        try {
            factory.setIgnoringComments(true);
            //factory.setIgnoringElementContentWhitespace(true);
            factory.setExpandEntityReferences(false);
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(in, ".");
            //System.out.println("free "+Runtime.getRuntime().freeMemory());
            in.close();
            //System.out.println("free "+Runtime.getRuntime().freeMemory());
            System.gc(); //don't give me that crap about *suggests*, this frees like 50Mb of memory
            //System.out.println("free "+Runtime.getRuntime().freeMemory());
        } catch (SAXException e) {
            e.printStackTrace();
            throw new Exception("Error while parsing map file: " +
                    e.toString());
        }
		
        Node dbNode = doc.getDocumentElement();
        if (!"database".equals(dbNode.getNodeName())) {
            throw new Exception("not a valid TightFit database");
        }
        
        System.out.println("building groups... ");
        Node item;
        NodeList l = doc.getElementsByTagName("group");
        for (int i = 0; (item = l.item(i)) != null; i++) {
        	Group g = unmarshalGroup(item);
        	//System.out.println(g.name);
        	groups.addLast(g);
        }
        
        System.out.println("building market groups... ");
        l = doc.getElementsByTagName("marketgroup");
        for (int i = 0; (item = l.item(i)) != null; i++) {
        	Group g = unmarshalMarketGroup(item);
        	//System.out.println(g.name);
        	marketGroups.addLast(g);
        }
        
        System.out.print("building types... ");
        l = doc.getElementsByTagName("type");
        for (int i = 0; (item = l.item(i)) != null; i++) {
        	Item type = unmarshalType(item);
            //System.out.print("type "+type.name+" ");
        	/*switch(type.getCategory()) {
        		case 6:
        		{
        			//It's a ship...
        			Ship ship = new Ship(type);
            		cache.put(ship.name, ship);
                    System.out.println("add ship: "+type.name);
        		}break;
        		case 7:
        		{
        			//It's a module
            		Module module = new Module(type);
            		cache.put(module.name, module);
                    System.out.println("add module: "+type.name);
        		}break;
        		case 8:
        		{
        			//It's ammo! \o/
        			
        		}break;
        		default:
        		break;
        	}*/
            cache.put(type.name.toLowerCase(), type);
            //System.out.println(">>> added module");
        }
        System.out.println("DONE");
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
	    	n = nnm.getNamedItem("graphicid");
	    	if(n != null)
	    		i.graphicId = n.getNodeValue();
    	}
    }
    
    private static void readAttributes(NodeList children, Item item) {
    	for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if(child != null) {
                if ("attribute".equalsIgnoreCase(child.getNodeName())) {
                    item.attributes.put(getAttributeValue(child, "name"),
                            getAttributeValue(child, "value"));
                }
                if("description".equalsIgnoreCase(child.getNodeName()) && child.getFirstChild() != null) {
                    item.attributes.put("description", child.getFirstChild().getNodeValue());
                }
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
	    	n = nnm.getNamedItem("graphicid");
	    	if(n != null)
	    		g.graphicId = (int)Float.parseFloat(n.getNodeValue());
    	}
    	
    	return g;
    }
    
    private Group unmarshalMarketGroup(Node t) {
    	MarketGroup g = new MarketGroup();
    	NodeList children;
    	NamedNodeMap nnm = t.getAttributes();
    	
    	if(nnm != null) {
	    	Node n = nnm.getNamedItem("name");
	    	if(n != null)
	    		g.name = n.getNodeValue();
	    	n = nnm.getNamedItem("marketGroupId");
	    	if(n != null)
	    		g.groupId = (int)Float.parseFloat(n.getNodeValue());
	    	n = nnm.getNamedItem("parentGroupId");
	    	if(n != null)
	    		g.parentGroupId = (int)Float.parseFloat(n.getNodeValue());
	    	n = nnm.getNamedItem("graphicId");
	    	if(n != null)
	    		g.graphicId = (int)Float.parseFloat(n.getNodeValue());
	    	children = t.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
	            Node child = children.item(i);
	            if(child != null) {
	            	if("description".equalsIgnoreCase(child.getNodeName()) && child.getFirstChild() != null) {
	            		g.description = child.getFirstChild().getNodeValue();
	            	}
	            }
	        }
    	}
    	
    	return g;
    }
}
