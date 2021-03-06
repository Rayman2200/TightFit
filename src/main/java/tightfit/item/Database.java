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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class Database
{

  private static Database instance;

  private final Hashtable cache = new Hashtable();
  private final Hashtable typeIdLookup = new Hashtable();
  private final HashMap attributeDisplayNames = new HashMap();
  private final LinkedList groups = new LinkedList();
  private final LinkedList marketGroups = new LinkedList();

  public Database(String resource) throws Exception
  {

    InputStream in = Database.class.getResourceAsStream(resource);

    SAXParserFactory factory = SAXParserFactory.newInstance();
    try
    {
      // factory.setIgnoringComments(true);
      // factory.setIgnoringElementContentWhitespace(true);
      // factory.setExpandEntityReferences(false);
      factory.setValidating(false);
      SAXParser parser = factory.newSAXParser();
      synchronized (this)
      {
        try
        {
          parser.parse(in, new DatabaseParserHandler(this));
        }
        catch (SAXParseException e)
        {
          System.err.println("At [" + e.getLineNumber() + ":" + e.getColumnNumber() + "]: ");
          e.printStackTrace();
        }
      }
      // System.out.println("free "+Runtime.getRuntime().freeMemory());
      in.close();
      // System.out.println("free "+Runtime.getRuntime().freeMemory());
      System.gc(); // don't give me that crap about *suggests*, this frees like 50Mb of memory
      // System.out.println("free "+Runtime.getRuntime().freeMemory());
    }
    catch (SAXException e)
    {
      e.printStackTrace();
      throw new Exception("Error while parsing database file: " + e.toString());
    }

  }

  public static Database getInstance() throws Exception
  {
    if (instance == null)
      instance = new Database("/db/eveDb.xml");
    return instance;
  }

  public Group getGroup(int id)
  {
    Iterator itr = groups.iterator();
    while (itr.hasNext())
    {
      Group g = (Group) itr.next();
      if (g.groupId == id)
        return g;
    }
    return null;
  }

  public Iterator getGroups()
  {
    return groups.iterator();
  }

  public Iterator getMarketGroups()
  {
    return marketGroups.iterator();
  }

  public Item getType(String name)
  {
    synchronized (this)
    {
      return (Item) cache.get(name.toLowerCase());
    }
  }

  public String getAttributeDisplayName(String name)
  {
    return (String) attributeDisplayNames.get(name);
  }

  public Item getTypeById(String id)
  {
    return (Item) typeIdLookup.get(id);
  }

  public LinkedList getTypeByGroup(int gid)
  {
    synchronized (this)
    {
      LinkedList l = new LinkedList();
      Enumeration types = cache.elements();
      while (types.hasMoreElements())
      {
        Item item = (Item) types.nextElement();
        if (item.groupId == gid)
          l.addLast(item);
      }
      return l;
    }
  }

  public LinkedList getTypeByMarketGroup(int gid)
  {
    LinkedList l = new LinkedList();
    Enumeration types = cache.elements();
    while (types.hasMoreElements())
    {
      Item item = (Item) types.nextElement();
      if (item.marketGroupId == gid)
        l.addLast(item);
    }
    return l;
  }

  public LinkedList getTypeByName(String nom)
  {
    LinkedList l = new LinkedList();
    Enumeration types = cache.elements();
    while (types.hasMoreElements())
    {
      Item item = (Item) types.nextElement();
      if (item.name.toLowerCase().indexOf(nom) != -1)
      {
        l.addLast(item);
      }
    }
    return l;
  }

  public LinkedList getMarketGroupByParent(int gid)
  {
    Iterator itr = marketGroups.iterator();
    LinkedList l = new LinkedList();

    while (itr.hasNext())
    {
      MarketGroup g = (MarketGroup) itr.next();
      if (g.parentGroupId == gid)
      {
        l.addLast(g);
      }
    }

    return l;
  }

  private class DatabaseParserHandler extends DefaultHandler
  {

    private final Database myDb;
    private boolean inDesc;
    private Object currentElement;

    public DatabaseParserHandler(Database instance)
    {
      myDb = instance;
    }

    @Override
    public void startDocument() throws SAXException
    {
      System.out.print("building DB... ");
    }

    @Override
    public void endDocument() throws SAXException
    {
      System.out.println("DONE");
    }

    @Override
    public void startElement(String name, String localName, String qName, Attributes attrs) throws SAXException
    {
      if (qName.equalsIgnoreCase("type"))
      {
        Item type = new Item();
        currentElement = type;
        unmarshalType(type, attrs);
        myDb.cache.put(type.name.toLowerCase(), type);
        myDb.typeIdLookup.put("" + type.typeId, type);
      }
      else if (qName.equalsIgnoreCase("attribute"))
      {
        Item m = (Item) currentElement;
        if (!m.attributes.containsKey(attrs.getValue("name")))
        {
          m.attributes.put(attrs.getValue("name"), attrs.getValue("value"));
        }
      }
      else if (qName.equalsIgnoreCase("marketgroup"))
      {
        Group g = unmarshalMarketGroup(attrs);
        currentElement = g;
        myDb.marketGroups.addLast(g);
      }
      else if (qName.equalsIgnoreCase("group"))
      {
        Group g = unmarshalGroup(attrs);
        currentElement = g;
        myDb.groups.addLast(g);
      }
      else if (qName.equalsIgnoreCase("description"))
      {
        inDesc = true;
      }
      else if (qName.equalsIgnoreCase("attribtype"))
      {
        unmarshalAttributeType(attrs);
      }
    }

    public void endElement(String name) throws SAXException
    {
      inDesc = false;
    }

    @Override
    public void characters(char buf[], int offset, int len) throws SAXException
    {
      if (inDesc)
      {
        String sbuf = new String(buf, offset, len);
        // sbuf = sbuf.replaceAll("\n", "");
        // System.out.println(sbuf);
        if (currentElement instanceof Item)
        {
          String desc = ((Item) currentElement).attributes.get("description");
          ((Item) currentElement).attributes.put("description", desc + sbuf);
        }
        else if (currentElement instanceof MarketGroup)
          ((MarketGroup) currentElement).description.concat(sbuf);
      }
    }

    private void unmarshalType(Item type, Attributes attrs)
    {
      type.name = attrs.getValue("name");
      type.attributes.put("mass", attrs.getValue("mass"));
      type.attributes.put("capacity", attrs.getValue("capacity"));
      type.attributes.put("volume", attrs.getValue("volume"));
      type.typeId = (int) Float.parseFloat(attrs.getValue("typeid"));
      type.groupId = (int) Float.parseFloat(attrs.getValue("groupid"));
      type.marketGroupId = (int) Float.parseFloat(attrs.getValue("marketgroupid"));
      type.graphicId = attrs.getValue("graphicid");
    }

    private Group unmarshalMarketGroup(Attributes attrs)
    {
      MarketGroup g = new MarketGroup();
      g.name = attrs.getValue("name");
      g.groupId = (int) Float.parseFloat(attrs.getValue("marketGroupId"));
      g.parentGroupId = (int) Float.parseFloat(attrs.getValue("parentGroupId"));
      g.graphicId = (int) Float.parseFloat(attrs.getValue("graphicId"));
      return g;
    }

    private Group unmarshalGroup(Attributes attrs)
    {
      Group g = new Group();
      g.name = attrs.getValue("name");
      g.groupId = (int) Float.parseFloat(attrs.getValue("groupid"));
      g.catId = (int) Float.parseFloat(attrs.getValue("catid"));
      g.graphicId = (int) Float.parseFloat(attrs.getValue("graphicid"));
      return g;
    }

    private void unmarshalAttributeType(Attributes attrs)
    {
      attributeDisplayNames.put(attrs.getValue("name").toLowerCase(), attrs.getValue("displayname"));
    }
  }
}
