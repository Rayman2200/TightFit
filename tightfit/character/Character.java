/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.character;

import java.io.FileInputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import tightfit.item.Item;

public class Character {

	private HashMap skills = new HashMap();
	public String name;
	
	public Character() {
		name="Pilot";
	}
	
	public Character(String name) {
		this();
	}
	
	public void addSkill(String id, String level) {
		skills.put(id, level);
	}
	
	public boolean hasRequiredSkill(String skillId, int level) {
		String skill = (String) skills.get(skillId);
		if(skill != null) {
			
		}
		return false;
	}
	
	public boolean hasRequiredSkill(Item item) {
		try {
			String [] reqSkills = item.getAttributeKey("requiredSkill");
			for(int i = 0; i < reqSkills.length; i++) {
				if(!reqSkills[i].contains("Level")) {
					String level;
					if((level = (String) skills.get(reqSkills[i])) != null) {
						if(!level.equals(item.getAttribute(reqSkills[i]+"Level", "0"))) {
							return false;
						}
					} else return false;
				}
			}
		} catch (Exception e) {}
		return true;
	}
	
	public void parse(String file) throws Exception {
		skills.clear();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc;
        try {
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new FileInputStream(file), ".");
            
            Node check = doc.getDocumentElement(), item;
            if (!"eveapi".equals(check.getNodeName())) {
            	throw new Exception("Not a valid eveapi character export file.");
            }
            
            NodeList l = doc.getElementsByTagName("row");
            for (int i = 0; (item = l.item(i)) != null; i++) {
            	skills.put(getAttributeValue(item, "typeid"), getAttributeValue(item, "level"));
            }
        } catch (SAXException e) {
            e.printStackTrace();
            throw new Exception("Error while parsing file: " +
                    e.toString());
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
}
