/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */

package tightfit.widget;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.LineBorder;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.TightPreferences;
import tightfit.actions.ShowInfoAction;
import tightfit.item.Ammo;
import tightfit.module.*;
import tightfit.ship.*;

/**
 * FitPanel is the pretty face of TightFit. Renders similar to the actual eve fit window, 
 *
 */
public class FitPanel extends JPanel implements TightFitDropTargetPanel, MouseListener, ShipChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TightFit editor;
	private Ship ship;
	private CargoSlot cargoSlot;
	
	private JButton strip, closeButton, minButton, configButton, 
                infoButton, aboutButton, exportButton, importButton;
	
	public Image panelImg;
	
	private Image rigImg, lnchrImg, turImg,
                sigRadImg, scanImg, maxTarImg, maxRanImg,
                shieldImg, armorImg, structImg,
                droneImg;
    
    private Image rstEmImg, rstExImg, rstThImg, rstKnImg;
    private Image bigBarImg, smallBarImg, bigBarGlowImg, smallBarGlowImg;
    
	private Font bigFont, smallFont,
				shipTypeFont, shipTitleFont;
	
    private Color bgColor, brightWhite, dullWhite, statWhite;
    
    private Point mountPoints[][];
    
    private static final String VERSION = Resources.getVersionString();
    private static final String MILLIMETERS = Resources.getString("panel.fit.units.millimeters");
    private static final String METERS = Resources.getString("panel.fit.units.meters");
    private static final String SECONDS = Resources.getString("panel.fit.units.seconds");
    private static final String HITPOINTS = Resources.getString("panel.fit.units.hitpoints");
    private static final String LAUNCHER_HARDPOINTS = Resources.getString("panel.fit.hardpoints.launcher");
    private static final String TURRET_HARDPOINTS = Resources.getString("panel.fit.hardpoints.turret");
    private static final String UPGRADE_HARDPOINTS = Resources.getString("panel.fit.hardpoints.upgrade");
    private static final String SPEED = Resources.getString("panel.fit.speed");
    private static final String CAPACITOR = Resources.getString("panel.fit.capacitor");
    private static final String CPU_STRING = Resources.getString("panel.fit.cpu");
    private static final String RECHARGE_RATE = Resources.getString("panel.fit.rechargerate");
    
	public FitPanel(TightFit editor) {
		super();
		this.editor = editor;
		
		try {
			panelImg = Resources.getImage("panel.png");
	        rigImg = Resources.getImage("icon68_01.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
	        lnchrImg = Resources.getImage("icon12_12.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
	        turImg = Resources.getImage("icon12_09.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
	        sigRadImg = Resources.getImage("icon22_14.png");
	        scanImg = Resources.getImage("icon03_09.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
	        maxTarImg = Resources.getImage("icon04_12.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
	        maxRanImg = Resources.getImage("icon22_15.png");
	        droneImg = Resources.getImage("icon11_16.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
	        shieldImg = Resources.getImage("icon01_13.png").getScaledInstance(48, 48, Image.SCALE_SMOOTH);
	        armorImg = Resources.getImage("icon01_09.png").getScaledInstance(48, 48, Image.SCALE_SMOOTH);
	        structImg = Resources.getImage("icon02_12-small.png");
	        
	        rstEmImg = Resources.getImage("icon22_20.png");
	        rstExImg = Resources.getImage("icon22_19.png");
	        rstThImg = Resources.getImage("icon22_18.png");
	        rstKnImg = Resources.getImage("icon22_17.png");
		    
		    bigBarImg = Resources.getImage("bar.png");
		    bigBarGlowImg = Resources.getImage("barglow.png");
		    smallBarImg = bigBarImg.getScaledInstance((int)(bigBarImg.getWidth(null)*0.5f), (int)(bigBarImg.getHeight(null)*0.6f), Image.SCALE_SMOOTH);
		    smallBarGlowImg = bigBarGlowImg.getScaledInstance((int)(bigBarGlowImg.getWidth(null)*0.5f), (int)(bigBarGlowImg.getHeight(null)*0.6f), Image.SCALE_SMOOTH);
	    
			Font big = Resources.getFont("stan07_57.ttf");
            bigFont = big.deriveFont(8f);
            big = Resources.getFont("agencyr.ttf");
            shipTypeFont = big.deriveFont(Font.ITALIC, 10f);
            shipTitleFont = big.deriveFont(Font.BOLD, 14f);
			smallFont = Resources.getFont("uni05_53.ttf");
            smallFont = smallFont.deriveFont(6f);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        ship = new Ship();
        
        bgColor = Color.decode(TightPreferences.node("prefs").get("bgColor", "#30251A"));
        brightWhite = new Color(1f,1f,1f,.95f);
        dullWhite = new Color(.9f,.9f,1f,.55f);
        statWhite = new Color(.9f,.9f,1f,.95f);
        
        setPreferredSize(new Dimension(680,500));
        setLayout(new SlickLayout());
        setDoubleBuffered(true);
        
        createMountPoints();
        
        strip = new JButton(Resources.getString("panel.fit.strip"));
        strip.setActionCommand("strip");
        strip.addActionListener(editor);
        strip.setLocation(500,468);
        
        try {
	        aboutButton = new ImageButton("about.png", true);
	        aboutButton.setActionCommand("about");
	        aboutButton.setToolTipText("About...");
	        aboutButton.addActionListener(editor);
	        aboutButton.setLocation(612, 0);
	        configButton = new ImageButton("config.png", true);
	        configButton.setActionCommand("config");
	        configButton.setToolTipText("Configuration");
	        configButton.addActionListener(editor);
	        configButton.setLocation(628, 0);
	        minButton = new ImageButton("minimize.png", true);
	        minButton.setActionCommand("minimize");
	        minButton.setToolTipText("Minimize");
	        minButton.addActionListener(editor);
	        minButton.setLocation(644, 0);
	        closeButton = new ImageButton("close.png", true);
	        closeButton.setActionCommand("close");
	        closeButton.setToolTipText("Close");
	        closeButton.addActionListener(editor);
	        closeButton.setLocation(660, 0);
	        infoButton = new ImageButton("showinfo.png");
	        infoButton.setActionCommand("showinfo");
	        infoButton.setToolTipText("Show Info");
	        infoButton.addActionListener(editor);
	        infoButton.setLocation(311, 47);
	        exportButton = new ImageButton(new ImageIcon(Resources.getImage("export.png").getScaledInstance(24, 24, Image.SCALE_SMOOTH)), true);
	        exportButton.setActionCommand("export");
	        exportButton.setToolTipText("EFT Export");
            exportButton.addActionListener(editor);
            exportButton.setLocation(450, 469);
            importButton = new ImageButton(new ImageIcon(Resources.getImage("import.png").getScaledInstance(24, 24, Image.SCALE_SMOOTH)), true);
	        importButton.setActionCommand("import");
	        importButton.setToolTipText("EFT Import");
            importButton.addActionListener(editor);
            importButton.setLocation(421, 469);
            
            addButtons();
        } catch(IOException ioe) {
        }
	}
	
	public void setShip(Ship s) {
		ship = s;
        
		removeAll();
		
		//buttons back
		addButtons();
		
		//HI
		for(int i=0;i<ship.totalSlots(Module.HI_SLOT);i++) {
			ModuleSlot slot = new ModuleSlot(editor, this, Module.HI_SLOT, i);
			slot.mount(ship.getModule(Module.HI_SLOT, i));
            slot.setLocation(mountPoints[Module.HI_SLOT][i]);
    		add(slot);
    		slot.addMouseListener(this);
    		s.addChangeListener(slot);
		}
        
		//MED
		for(int i=0;i<ship.totalSlots(Module.MID_SLOT);i++) {
			ModuleSlot slot = new ModuleSlot(editor, this, Module.MID_SLOT, i);
			slot.mount(ship.getModule(Module.MID_SLOT, i));
            slot.setLocation(mountPoints[Module.MID_SLOT][i]);
    		add(slot);
    		slot.addMouseListener(this);
    		s.addChangeListener(slot);
		}
		
		//LOW
		for(int i=0;i<ship.totalSlots(Module.LOW_SLOT);i++) {
			ModuleSlot slot = new ModuleSlot(editor, this, Module.LOW_SLOT, i);
			slot.mount(ship.getModule(Module.LOW_SLOT, i));
			slot.setLocation(mountPoints[Module.LOW_SLOT][i]);
    		add(slot);
    		slot.addMouseListener(this);
    		slot.setBorder(new LineBorder(Color.BLACK, 0)); //goofy? ya.
    		s.addChangeListener(slot);
		}
		
		//RIG
		for(int i=0, x=65;i<ship.totalSlots(Module.RIG_SLOT);i++,x+=34) {
			RigSlot slot = new RigSlot(editor, this, Module.RIG_SLOT, i);
			slot.mount(ship.getModule(Module.RIG_SLOT, i));
			slot.setLocation(x,450);
			add(slot);
    		slot.addMouseListener(this);
    		s.addChangeListener(slot);
		}
		
		
		cargoSlot = new CargoSlot(this, editor);
		cargoSlot.setLocation(393, 433);
		cargoSlot.setPreferredSize(new Dimension(139, 31));
		cargoSlot.setFont(smallFont);
		add(cargoSlot);
		cargoSlot.addMouseListener(this);
		s.addChangeListener(cargoSlot);
		
		s.addChangeListener(this);
		
		repaint();
	}
	
	public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();        
        Rectangle clip = g2d.getClipBounds();
        
        //setup
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            //RenderingHints.VALUE_ANTIALIAS_ON);
        
        //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        //                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
        					RenderingHints.VALUE_RENDER_QUALITY);
        
        g2d.setComposite(AlphaComposite.SrcAtop);
                            
        //let the drawing begin!
        g2d.setColor(bgColor);
        g2d.fillRect(clip.x, clip.y, clip.width, clip.height);
        g2d.drawImage(panelImg, 0, 0, null);
        
        //draw lines
        g2d.setColor(brightWhite);
        g2d.drawRect(392, 37, 280, 130);
        g2d.drawRect(392, 172, 280, 84);
        g2d.drawRect(392, 263, 280, 65);
        g2d.drawRect(392, 335, 280, 65);
        g2d.drawRect(392, 407, 280, 19);
        g2d.drawRect(392, 432, 280, 32);
        
        g2d.setColor(dullWhite);
        g2d.drawLine(0,16,680,16);
        g2d.drawLine(75,64,325,64);
        g2d.drawLine(393, 69, 672, 69);
        g2d.drawLine(393, 101, 672, 101);
        g2d.drawLine(393, 133, 672, 133);
        g2d.drawLine(424, 38, 424, 167);
        g2d.drawLine(532, 38, 532, 133);
        g2d.drawLine(564, 38, 564, 133);
        
        g2d.drawLine(393, 237, 672, 237);
        g2d.drawLine(456, 173, 456, 237);
        g2d.drawLine(564, 173, 564, 237);
        
        g2d.drawLine(456, 263, 456, 328);
        g2d.drawLine(564, 263, 564, 328);
        
        g2d.drawLine(456, 335, 456, 399);
        g2d.drawLine(564, 335, 564, 399);
        
        g2d.drawLine(424, 432, 424, 463);
        g2d.drawLine(532, 432, 532, 463);
        g2d.drawLine(564, 432, 564, 463);
        
        for(int i=0,x=65;i<8;i++,x+=34) {
            g2d.drawRect(x, 450, 32, 32);
        }
        
        
        //TODO: draw the capacitor
        
        //draw icons
        g2d.drawImage(lnchrImg, 393, 37, null);
        g2d.drawImage(turImg, 532, 37, null);
        g2d.drawImage(rigImg, 393, 69, null);
        g2d.drawImage(scanImg, 393, 101, null);
        g2d.drawImage(sigRadImg, 393, 133, null);
        g2d.drawImage(maxTarImg, 532, 69, null);
        g2d.drawImage(maxRanImg, 532, 101, null);
        
        g2d.drawImage(rstEmImg, 458, 175, null); g2d.drawImage(rstKnImg, 566, 175, null);
        g2d.drawImage(rstExImg, 458, 202, null); g2d.drawImage(rstThImg, 566, 202, null);
        
        g2d.drawImage(rstEmImg, 458, 268, null); g2d.drawImage(rstKnImg, 566, 268, null);
        g2d.drawImage(rstExImg, 458, 295, null); g2d.drawImage(rstThImg, 566, 295, null);
        
        g2d.drawImage(rstEmImg, 458, 340, null); g2d.drawImage(rstKnImg, 566, 340, null);
        g2d.drawImage(rstExImg, 458, 367, null); g2d.drawImage(rstThImg, 566, 367, null);
        
        g2d.drawImage(droneImg, 532, 432, null);
        g2d.drawImage(shieldImg, 399, 180, null);
        g2d.drawImage(armorImg, 399, 273, null);
        g2d.drawImage(structImg, 399, 345, null);
        
        //draw all the labels
        //bigger font
        g2d.setFont(bigFont);
        WidgetHelper.drawShadowedString(g2d, VERSION + " - " + ship.title, 6, 13, Color.white);
        WidgetHelper.drawShadowedString(g2d, UPGRADE_HARDPOINTS, 65, 447, Color.white);
        WidgetHelper.drawShadowedString(g2d, SPEED, 401, 421, Color.white);
        WidgetHelper.drawShadowedStringCentered(g2d, CAPACITOR, 200, 342, Color.white);
        WidgetHelper.drawShadowedStringCentered(g2d, "Shield", 424, 186, Color.white);
        WidgetHelper.drawShadowedStringCentered(g2d, "Armor", 424, 277, Color.white);
        WidgetHelper.drawShadowedStringCentered(g2d, "Structure", 424, 349, Color.white);

        //smaller font
        g2d.setFont(smallFont);
        WidgetHelper.drawShadowedString(g2d, LAUNCHER_HARDPOINTS, 430, 46, Color.white);
        WidgetHelper.drawShadowedString(g2d, TURRET_HARDPOINTS, 570, 46, Color.white);
        WidgetHelper.drawShadowedString(g2d, UPGRADE_HARDPOINTS, 430, 78, Color.white);
        WidgetHelper.drawShadowedString(g2d, "MAX LOCKED TARGETS", 570, 78, Color.white);
        WidgetHelper.drawShadowedString(g2d, "SCAN RESOLUTION", 430, 110, Color.white);
        WidgetHelper.drawShadowedString(g2d, "MAX TARGETING RANGE", 570, 110, Color.white);
        WidgetHelper.drawShadowedString(g2d, "SIGNATURE RADIUS", 430, 142, Color.white);
        WidgetHelper.drawShadowedString(g2d, "DRONE BAY", 572, 443, Color.white);
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        		RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(shipTitleFont);
        WidgetHelper.drawShadowedStringCentered(g2d, editor.getChar().name.toUpperCase()+"'"+
        		(editor.getChar().name.lastIndexOf("s") != editor.getChar().name.length()-1 ? "S" : "")+
        		" "+ship.title.toUpperCase(), 200, 47, Color.white);
        g2d.setFont(shipTypeFont);
        WidgetHelper.drawShadowedStringCentered(g2d, ship.name.toUpperCase(), 200, 60, Color.white);
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        		RenderingHints.VALUE_ANTIALIAS_OFF);
        
        //finally, put in the ship's specs
        drawShipSpecs(g2d);
	}
    
    
    
    public void drawBigBar(Graphics2D g2d, int x, int y, float percent) {
    	percent = percent > 1 ? 1 : percent;
    	float width = bigBarImg.getWidth(null) * percent;
    	g2d.drawImage(bigBarImg, x, y, null);
    	Rectangle s = g2d.getClipBounds();
    	g2d.setClip(x, y, (int)width, bigBarGlowImg.getHeight(null));
    	g2d.drawImage(bigBarGlowImg, x, y, null);
    	g2d.setClip(s);
    }
    
    public void drawSmallBar(Graphics2D g2d, int x, int y, float percent) {
    	percent = percent > 1 ? 1 : percent;
    	float width = smallBarImg.getWidth(null) * percent;
    	g2d.drawImage(smallBarImg, x, y, null);
    	Rectangle s = g2d.getClipBounds();
    	g2d.setClip(x, y, (int)width, smallBarGlowImg.getHeight(null));
    	g2d.drawImage(smallBarGlowImg, x, y, null);
    	g2d.setClip(s);
    }
    
    private void addButtons() {
    	add(aboutButton);
    	add(configButton);
    	add(minButton);
    	add(closeButton);
    	add(strip);
    	add(infoButton);
        add(exportButton);
        add(importButton);
    }
    
    private void drawShipSpecs(Graphics2D g2d) {
        g2d.setFont(bigFont);
        WidgetHelper.drawShadowedString(g2d, ""+ship.countFreeLauncherHardpoints(), 432, 60, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+ship.countFreeTurretHardpoints(), 572, 60, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+ship.totalSlots(Module.RIG_SLOT), 432, 92, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+ship.calculateScanResolution()+" "+MILLIMETERS, 432, 124, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+((int)ship.calculateRadius())+" "+METERS, 432, 156, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+ship.getMaxLockedTargets(), 572, 92, statWhite);
        WidgetHelper.drawShadowedStringCentered(g2d, ""+((int)ship.calculateMaxShields())+"  "+HITPOINTS, 424, 230, statWhite);
        WidgetHelper.drawShadowedStringCentered(g2d, ""+((int)ship.calculateMaxArmor())+"  "+HITPOINTS, 424, 322, statWhite);
        WidgetHelper.drawShadowedStringCentered(g2d, ""+((int)ship.calculateMaxStructure())+"  "+HITPOINTS, 424, 395, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+((int)ship.calculateMaxRange())+" "+METERS, 572, 124, statWhite);
        WidgetHelper.drawShadowedString(g2d, RECHARGE_RATE+"  "+((int)ship.calculateRechargeRate())+" "+SECONDS, 397, 250, statWhite);
        WidgetHelper.drawShadowedStringCentered(g2d, ""+((int)ship.calculateMaxCapacity()), 200, 355, statWhite);
        WidgetHelper.drawShadowedStringCentered(g2d, "( "+((int)ship.calculateCapacitorRechargeRate())+ " "+SECONDS+" )", 200, 368, statWhite);
        
        if(ship.getRemainingCpu() < 0)
        	WidgetHelper.drawShadowedStringCentered(g2d, CPU_STRING+" "+WidgetHelper.formatFloat(ship.getMaxCpu() - ship.getRemainingCpu(), 2)+" / "+WidgetHelper.formatFloat(ship.getMaxCpu(),2), 200, 398, Color.RED);
        else WidgetHelper.drawShadowedStringCentered(g2d, CPU_STRING+" "+WidgetHelper.formatFloat(ship.getMaxCpu() - ship.getRemainingCpu(), 2)+" / "+WidgetHelper.formatFloat(ship.getMaxCpu(),2), 200, 398, statWhite);
        if(ship.getRemainingPower() < 0)
        	WidgetHelper.drawShadowedStringCentered(g2d, Resources.getString("panel.fit.powergrid")+" "+WidgetHelper.formatFloat(ship.getMaxPower() - ship.getRemainingPower(), 2)+" / "+WidgetHelper.formatFloat(ship.getMaxPower(),2), 200, 429, Color.RED);
        else WidgetHelper.drawShadowedStringCentered(g2d, Resources.getString("panel.fit.powergrid")+" "+WidgetHelper.formatFloat(ship.getMaxPower() - ship.getRemainingPower(), 2)+" / "+WidgetHelper.formatFloat(ship.getMaxPower(),2), 200, 429, statWhite);
        
        //bars
        drawBigBar(g2d, 135, 363, (ship.getMaxCpu() - ship.getRemainingCpu()) / ship.getMaxCpu());
        drawBigBar(g2d, 135, 393, (ship.getMaxPower() - ship.getRemainingPower()) / ship.getMaxPower());
        
        g2d.setFont(smallFont);
        //shield
        float [] reson = ship.getShieldResonance();
        WidgetHelper.drawShadowedString(g2d, ""+((int)Math.ceil((1.0f - reson[0])*100))+"  %", 490, 187, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+((int)Math.ceil((1.0f - reson[1])*100))+"  %", 598, 187, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+((int)Math.ceil((1.0f - reson[3])*100))+"  %", 490, 214, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+((int)Math.ceil((1.0f - reson[2])*100))+"  %", 598, 214, statWhite);
        drawSmallBar(g2d, 488, 187, 1.0f - reson[0]);
        drawSmallBar(g2d, 595, 187, 1.0f - reson[1]);
        drawSmallBar(g2d, 488, 214, 1.0f - reson[3]);
        drawSmallBar(g2d, 595, 214, 1.0f - reson[2]);
        
        //armor
        reson = ship.getArmorResonance();
        WidgetHelper.drawShadowedString(g2d, ""+((int)((1.0f - reson[0])*100))+"  %", 490, 279, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+((int)((1.0f - reson[1])*100))+"  %", 598, 279, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+((int)((1.0f - reson[3])*100))+"  %", 490, 306, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+((int)((1.0f - reson[2])*100))+"  %", 598, 306, statWhite);
        drawSmallBar(g2d, 488, 279, 1.0f - reson[0]);
        drawSmallBar(g2d, 595, 279, 1.0f - reson[1]);
        drawSmallBar(g2d, 488, 306, 1.0f - reson[3]);
        drawSmallBar(g2d, 595, 306, 1.0f - reson[2]);
        
        //structure
        reson = ship.getStructureResonance();
        WidgetHelper.drawShadowedString(g2d, ""+((int)((1.0f - reson[0])*100))+"  %", 490, 353, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+((int)((1.0f - reson[1])*100))+"  %", 598, 353, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+((int)((1.0f - reson[3])*100))+"  %", 490, 380, statWhite);
        WidgetHelper.drawShadowedString(g2d, ""+((int)((1.0f - reson[2])*100))+"  %", 598, 380, statWhite);
        drawSmallBar(g2d, 488, 353, 1.0f - reson[0]);
        drawSmallBar(g2d, 595, 353, 1.0f - reson[1]);
        drawSmallBar(g2d, 488, 380, 1.0f - reson[3]);
        drawSmallBar(g2d, 595, 380, 1.0f - reson[2]);
        
        WidgetHelper.drawShadowedString(g2d, "0.0 / "+ship.getTotalDroneCapacity(), 575, 453, statWhite);
    }
    
    private void createMountPoints() {
    	mountPoints = new Point[Module.RIG_SLOT][];
    	mountPoints[Module.LOW_SLOT] = new Point[8];
    	mountPoints[Module.MID_SLOT] = new Point[8];
    	mountPoints[Module.HI_SLOT] = new Point[8];
    	
    	mountPoints[Module.LOW_SLOT][0] = new Point(107,279);
    	mountPoints[Module.LOW_SLOT][1] = new Point(92,234);
    	mountPoints[Module.LOW_SLOT][2] = new Point(107,190);
    	mountPoints[Module.LOW_SLOT][3] = new Point(145,163);
    	mountPoints[Module.LOW_SLOT][4] = new Point(192,163);
    	mountPoints[Module.LOW_SLOT][5] = new Point(230,191);
    	mountPoints[Module.LOW_SLOT][6] = new Point(244,235);
    	mountPoints[Module.LOW_SLOT][7] = new Point(228,280);
    	
    	mountPoints[Module.MID_SLOT][0] = new Point(69,308);
    	mountPoints[Module.MID_SLOT][1] = new Point(45,235);
    	mountPoints[Module.MID_SLOT][2] = new Point(70,162);
    	mountPoints[Module.MID_SLOT][3] = new Point(131,119);
    	mountPoints[Module.MID_SLOT][4] = new Point(206,119);
    	mountPoints[Module.MID_SLOT][5] = new Point(267,164);
    	mountPoints[Module.MID_SLOT][6] = new Point(290,236);
    	mountPoints[Module.MID_SLOT][7] = new Point(265,309);
    	
    	mountPoints[Module.HI_SLOT][0] = new Point(32,333);
    	mountPoints[Module.HI_SLOT][1] = new Point(0,235);
    	mountPoints[Module.HI_SLOT][2] = new Point(32,137);
    	mountPoints[Module.HI_SLOT][3] = new Point(115,75);
    	mountPoints[Module.HI_SLOT][4] = new Point(220,76);
    	mountPoints[Module.HI_SLOT][5] = new Point(303,137);
    	mountPoints[Module.HI_SLOT][6] = new Point(335,235);
    	mountPoints[Module.HI_SLOT][7] = new Point(304,334);
    }

	public void dragEnter(DropTargetDragEvent e) {
		Slot s = (Slot)((DropTarget)e.getSource()).getComponent();
		
		s.setSelected(true);
		
		//if(!ship.hasModule(s.getRack(), s.getSlotNumber())) {
			try {
				if(e.getTransferable().getTransferData(new DataFlavor(Module.class, "Module")) instanceof Module) {
					Module m = (Module)(e.getTransferable()).getTransferData(new DataFlavor(Module.class, "Module"));
					if(s instanceof ModuleSlot && ship.testPutModule(m, ((ModuleSlot)s).getRack(), ((ModuleSlot)s).getSlotNumber()) || m.isAmmo()) {
						e.acceptDrag(DnDConstants.ACTION_COPY);
						//TODO: set attributes in yellow as per this module
					} else if(s instanceof CargoSlot) {
						
					} else {
						e.rejectDrag();
					}
				}
			} catch (UnsupportedFlavorException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		//} else {
		//	e.rejectDrag();
		//}
	}

	public void dragOver(DropTargetDragEvent e) {
	}

	public void dropActionChanged(DropTargetDragEvent e) {
		e.rejectDrag();
	}

	public void dragExit(DropTargetEvent e) {
		((Slot)((DropTarget)e.getSource()).getComponent()).setSelected(false);
	}

	public void drop(DropTargetDropEvent e) {
		Slot slot = (Slot)((DropTarget)e.getSource()).getComponent();
		try {
			if(slot instanceof CargoSlot) {
				
				ship.addToCargo((Module)(e.getTransferable()).getTransferData(new DataFlavor(Module.class, "Module")));
				ship.fireShipChange();
			} else if(e.getTransferable().getTransferData(new DataFlavor(Module.class, "Module")) instanceof Module) {
				Module m = (Module)(e.getTransferable()).getTransferData(new DataFlavor(Module.class, "Module"));
				ModuleSlot s = (ModuleSlot)slot;
				if(m.isAmmo()) {
					Ammo a = new Ammo(m);
					if(s.getModule() != null && s.getModule().accepts(a)) {
						ship.getModule(s.getRack(), s.getSlotNumber()).insertCharge(a);
						s.getModule().insertCharge(a);
						e.acceptDrop(DnDConstants.ACTION_COPY);
						ship.fireShipChange();
					}
				} else if(ship.testPutModule(m, s.getRack(), s.getSlotNumber())) {
					e.acceptDrop(DnDConstants.ACTION_COPY);
					if(m.isWeapon()) {
						Weapon w = new Weapon(m);
						ship.putModule(w, s.getRack(), s.getSlotNumber());
						s.mount(w);
					} else {
						ship.putModule(m, s.getRack(), s.getSlotNumber());
						s.mount(m);
					}
					ship.fireShipChange();
				} else {
					e.rejectDrop();
				}
			} else {
				e.rejectDrop();
			}
            e.dropComplete(true);
		} catch (UnsupportedFlavorException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void mouseClicked(MouseEvent e) {
		
		if(e.getSource() instanceof ModuleSlot) {
			ModuleSlot s = (ModuleSlot)e.getSource();
			if(e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
				if(ship.hasModule(s.getRack(), s.getSlotNumber())) {
					JPopupMenu menu = new JPopupMenu();
					JMenuItem mi;
					Point pt = getMousePosition();
					
					menu.add(new JMenuItem(new ShowInfoAction(editor.appFrame, s.getModule())));
					menu.addSeparator();
					mi = new JMenuItem("UnFit");
					mi.addActionListener(s);
					menu.add(mi);
					menu.addSeparator();
					if(s.getModule().isOnline()) {
						mi = new JMenuItem("Put Offline");
						mi.addActionListener(s);
						menu.add(mi);
					} else {
						mi = new JMenuItem("Put Online");
						mi.addActionListener(s);
						menu.add(mi);
					}
	
					menu.show(this, pt.x, pt.y);
				}
			}
		} else if(e.getSource() instanceof CargoSlot) {
			//TODO: open cargo window
		}
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent e) {
		Slot s = (Slot)e.getSource();
		s.setSelected(true);
		
	}

	public void mouseExited(MouseEvent e) {
		Slot s = (Slot)e.getSource();
		s.setSelected(false);
	}

	public void shipChanged(Ship ship) {
		repaint();
	}

}
