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
	public int sp;
    
	public Character() {
		name="Pilot";
	}
	
	public Character(String name) {
		this();
		this.name = name;
	}
	
	public void addSkill(String id, String level) {
		try {
			skills.put(id, new Skill(id, Integer.parseInt(level)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getSkillLevel(String id) {
		if(skills.containsKey(id))
			return ((Skill)skills.get(id)).getLevel();
		return 0;
	}
	
	public Skill getSkill(String id) {
		return (Skill)skills.get(id);
	}
	
	public boolean hasRequiredSkill(String skillId, int level) {
		Skill skill = (Skill) skills.get(skillId);
		if(skill != null) {
			if(skill.getLevel() >= level)
				return true;
		}
		return false;
	}
	
	public boolean hasRequiredSkill(Item item) {
		try {
			String [] reqSkills = item.getAttributeKey("requiredSkill");
			for(int i = 0; i < reqSkills.length; i++) {
				if(reqSkills[i] != null && !reqSkills[i].contains("Level")) {
					Skill skill = (Skill)skills.get(item.getAttribute(reqSkills[i], "-1"));
					if(skill != null) {
						if(skill.getLevel() < Integer.parseInt(item.getAttribute(reqSkills[i]+"Level", "0"))) {
							return false;
						}
					} else return false;
				}
			}
		} catch (Exception e) {}
		return true;
	}
	
    public int countSkills() {
        return skills.size();
    }
    
	public void parse(String file) throws Exception {
		if(file == null)
			return;
		
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
            
            NodeList l = doc.getElementsByTagName("name");
            for (int i = 0; (item = l.item(i)) != null; i++) {
            	Node cdata = item.getFirstChild();
            	name = cdata.getNodeValue().trim();
            }
            
            l = doc.getElementsByTagName("row");
            for (int i = 0; (item = l.item(i)) != null; i++) {
            	skills.put(getAttributeValue(item, "typeID"), new Skill(getAttributeValue(item, "typeID"), Integer.parseInt(getAttributeValue(item, "level"))));
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
