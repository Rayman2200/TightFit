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

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import tightfit.item.Item;

/**
 * Holds an EVEAPI character, and all skill information
 *
 */
public class Character {

	private HashMap<String, Skill> skills = new HashMap<String, Skill>();
	public String name;
    public String charId;
	public int sp;
    
	public Character() {
		name="Pilot";
	}
	
	public Character(String name) {
		this();
		this.name = name;
	}
	
    public Character(String name, String charId) {
		this(name);
		this.charId = charId;
	}
    
	public void addSkill(String id, String level) {
		try {
			skills.put(id, new Skill(id, Integer.parseInt(level == null ? "0" : level)));
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
		Skill s = (Skill)skills.get(id);
        try { 
            return s != null ? s : new Skill(id, -1);
        } catch(Exception e) {
        }
        
        return null;
	}
	
	/**
	 * Returns the calculated bonus which the skill gives at the level
	 * of the character. 
	 * 
	 * @param id
	 * @return 1 + the character skill level * the bonus percentage per level
	 */
	public float getSkillBonus(String id) {
        Skill s = getSkill(id);
        
        if(s != null)
            return 1+(s.getLevel() * (s.getBonus()/100.0f));
        return 1f;
    }
    
    public boolean hasRequiredSkill(String skillId, int level) {
		Skill skill = (Skill) skills.get(skillId);
		if(skill != null) {
			if(skill.getLevel() >= level)
				return true;
		}
		return false;
	}
	
    /**
     * Checks for all required skills specified by the item
     * 
     * @param item
     * @return <code>true</code> if all required skills are known to at least required level, <code>false</code> otherwise
     */
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
    
    public int countSkills(int level) {
        int count=0;
        Iterator<String> itr = skills.keySet().iterator();
        while(itr.hasNext()) {
            Skill s = (Skill)skills.get(itr.next());
            if(s.getLevel() == level) {
                count++;
            }
        }
        return count;
    }
    
	public void parse(InputStream s) throws Exception {
        skills.clear();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc;
        try {
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(s, ".");
            
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
            	addSkill(getAttributeValue(item, "typeID"), getAttributeValue(item, "level"));
            }
        } catch (SAXException e) {
            e.printStackTrace();
            throw new Exception("Error while parsing file: " +
                    e.toString());
        }
    }
    
    public void parse(String file) throws Exception {
		if(file == null)
			return;
		
		parse(new FileInputStream(file));
	}
	
    public static Character[] parseCharacterList(InputStream s) throws Exception {
        Vector<Character> chars = new Vector<Character>();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc;
        try {
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(s, ".");
            
            Node check = doc.getDocumentElement(), item;
            if (!"eveapi".equals(check.getNodeName())) {
            	throw new Exception("Not a valid eveapi character list.");
            }
            
            NodeList l = doc.getElementsByTagName("row");
            for (int i = 0; (item = l.item(i)) != null; i++) {
            	chars.add(new Character(getAttributeValue(item, "name"), getAttributeValue(item, "characterID")));
            }
        } catch (SAXException e) {
            e.printStackTrace();
            throw new Exception("Error while parsing file: " +
                    e.toString());
        }
        
        return (Character[])chars.toArray(new Character[0]);
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
