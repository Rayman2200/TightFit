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
import tightfit.module.Module;
import tightfit.ship.Ship;

public class FitPanel extends JPanel implements DropTargetListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TightFit editor;
	private Ship ship;
	
	public Image hiSlotImg;
    public Image medSlotImg;
    public Image lowSlotImg;
	
	private Image panelImg, rigImg, lnchrImg, turImg,
                sigRadImg, scanImg, maxTarImg, maxRanImg,
                cargoImg, shieldImg, armorImg, structImg;
    
    private Image rstEmImg, rstExImg, rstThImg, rstKnImg;
    private Image bigBarImg, smallBarImg, bigBarGlowImg, smallBarGlowImg;
    
	private Font bigFont, smallFont,
				shipTypeFont, shipTitleFont;
	
    private Color bgColor, brightWhite, dullWhite, shadow, statWhite;
    
    private Point mountPoints[][];
    
	public FitPanel(TightFit editor) throws IllegalArgumentException, IOException {
		this.editor = editor;
		
		panelImg = Resources.getImage("panel.png");
        rigImg = Resources.getImage("icon68_01.png");
        lnchrImg = Resources.getImage("icon12_12.png");
        turImg = Resources.getImage("icon12_09.png");
        sigRadImg = Resources.getImage("icon22_14.png");
        scanImg = Resources.getImage("icon03_09.png");
        maxTarImg = Resources.getImage("icon04_12.png");
        maxRanImg = Resources.getImage("icon22_15.png");
        cargoImg = Resources.getImage("icon03_13.png");
        shieldImg = Resources.getImage("icon01_13.png");
        armorImg = Resources.getImage("icon01_09-small.png");
        structImg = Resources.getImage("icon02_12-small.png");
        
        rstEmImg = Resources.getImage("icon22_20.png");
        rstExImg = Resources.getImage("icon22_19.png");
        rstThImg = Resources.getImage("icon22_18.png");
        rstKnImg = Resources.getImage("icon22_17.png");
        
        //hiSlotImg = Resources.getImage("hislot.png");
	    //medSlotImg = Resources.getImage("mdslot.png");
	    lowSlotImg = Resources.getImage("loslot.png");
	    
	    bigBarImg = Resources.getImage("bar.png");
	    bigBarGlowImg = Resources.getImage("barglow.png");
	    smallBarImg = bigBarImg.getScaledInstance((int)(bigBarImg.getWidth(null)*0.5f), (int)(bigBarImg.getHeight(null)*0.6f), Image.SCALE_SMOOTH);
	    smallBarGlowImg = bigBarGlowImg.getScaledInstance((int)(bigBarGlowImg.getWidth(null)*0.5f), (int)(bigBarGlowImg.getHeight(null)*0.6f), Image.SCALE_SMOOTH);
	    
		try {
			Font big = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("stan07_57.ttf"));
            bigFont = big.deriveFont(8f);
            shipTypeFont = big.deriveFont(Font.ITALIC, 10f);
            shipTitleFont = big.deriveFont(Font.BOLD, 10f);
			smallFont = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("stan07_55.ttf"));
            smallFont = smallFont.deriveFont(6f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        ship = new Ship();
        
        bgColor = new Color(.07f, .25f, .43f);
        brightWhite = new Color(1f,1f,1f,.95f);
        dullWhite = new Color(.9f,.9f,1f,.55f);
        statWhite = new Color(.9f,.9f,1f,.95f);
        shadow = new Color(.1f,.1f,.1f,.85f);
        
        setPreferredSize(new Dimension(680,500));
        setLayout(new SlickLayoutManager());
        
        createMountPoints();
	}
	
	public void setShip(Ship s) {
		ship = s;
        
		removeAll();
		
		//HI
		for(int i=0;i<ship.totalHiSlots();i++) {
			Slot slot = new Slot(this, Module.HI_SLOT, i);
			slot.mount(ship.getModule(Module.HI_SLOT, i));
            slot.setLocation(mountPoints[Module.HI_SLOT][i]);
    		add(slot);
    		slot.addMouseListener(this);
		}
        
		//MED
		for(int i=0;i<ship.totalMedSlots();i++) {
			Slot slot = new Slot(this, Module.MID_SLOT, i);
			slot.mount(ship.getModule(Module.MID_SLOT, i));
            slot.setLocation(mountPoints[Module.MID_SLOT][i]);
    		add(slot);
    		slot.addMouseListener(this);
		}
		
		//LOW
		for(int i=0;i<ship.totalLowSlots();i++) {
			Slot slot = new Slot(this, Module.LOW_SLOT, i);
			slot.mount(ship.getModule(Module.LOW_SLOT, i));
			slot.setLocation(mountPoints[Module.LOW_SLOT][i]);
    		add(slot);
    		slot.addMouseListener(this);
    		slot.setBorder(new LineBorder(Color.BLACK, 0));
		}
		
		repaint();
	}
	
	public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        //setup
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
        					RenderingHints.VALUE_RENDER_QUALITY);
        
        g2d.setComposite(AlphaComposite.SrcAtop);
                            
        //let the drawing begin!
        g2d.setColor(bgColor);
        g2d.fillRect(0,0,680,500);
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
        g2d.drawLine(0,17,680,17);
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
        
        for(int i=0,x=65, r=ship.totalRigSlots();i<8;i++,x+=34,r--) {
            g2d.drawRect(x, 450, 32, 32);
            if(r>0)
                g2d.drawImage(rigImg, x+1, 451, null);
        }
        
        
        //draw the power bar
        
        //draw the cpu bar
        
        //draw the capacitor
        
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
        
        g2d.drawImage(cargoImg, 393, 432, null);
        g2d.drawImage(shieldImg, 399, 180, null);
        g2d.drawImage(armorImg, 399, 273, null);
        g2d.drawImage(structImg, 399, 345, null);
        
        //draw all the labels
        //bigger font
        g2d.setFont(bigFont);
        drawShadowedString(g2d, "TIGHTFIT v0.6a - " /*+ship.title*/, 6, 13, Color.white);
        drawShadowedString(g2d, "UPGRADE HARDPOINTS", 65, 447, Color.white);
        drawShadowedString(g2d, "Speed", 401, 421, Color.white);
        drawShadowedStringCentered(g2d, "Capacitor", 200, 342, Color.white);
        drawShadowedStringCentered(g2d, "Shield", 424, 186, Color.white);
        drawShadowedStringCentered(g2d, "Armor", 424, 277, Color.white);
        drawShadowedStringCentered(g2d, "Structure", 424, 349, Color.white);

        //smaller font
        g2d.setFont(smallFont);
        drawShadowedString(g2d, "LAUNCHER HARDPOINTS", 430, 46, Color.white);
        drawShadowedString(g2d, "TURRET HARDPOINTS", 570, 46, Color.white);
        drawShadowedString(g2d, "UPGRADE HARDPOINTS", 430, 78, Color.white);
        drawShadowedString(g2d, "MAX LOCKED TARGETS", 570, 78, Color.white);
        drawShadowedString(g2d, "SCAN RESOLUTION", 430, 110, Color.white);
        drawShadowedString(g2d, "MAX TARGETING RANGE", 570, 110, Color.white);
        drawShadowedString(g2d, "SIGNATURE RADIUS", 430, 142, Color.white);
        drawShadowedString(g2d, "CARGOHOLD", 432, 443, Color.white);
        drawShadowedString(g2d, "DRONE BAY", 572, 443, Color.white);
        
        g2d.setFont(shipTitleFont);
        drawShadowedStringCentered(g2d, ship.title.toUpperCase(), 200, 47, Color.white);
        g2d.setFont(shipTypeFont);
        drawShadowedStringCentered(g2d, ship.name.toUpperCase(), 200, 60, Color.white);
        
        //finally, put in the ship's specs
        drawShipSpecs(g2d);
        
        /*for(int c=0;c<getComponentCount();c++) {
        	//g.translate(mountPoints[Module.LOW_SLOT][c].x, mountPoints[Module.LOW_SLOT][c].y);
        	getComponent(c).paint(g);
        	//g.translate(-mountPoints[Module.LOW_SLOT][c].x, -mountPoints[Module.LOW_SLOT][c].y);
        }*/
	}
    
    public void drawShadowedString(Graphics2D g2d, String s, float x, float y, Color c) {
        g2d.setColor(shadow);
        g2d.drawString(s, x+1, y+1);
        g2d.setColor(c);
        g2d.drawString(s, x, y);
    }
    
    public void drawShadowedStringCentered(Graphics2D g2d, String s, float x, float y, Color c) {
    	x -= g2d.getFontMetrics().getStringBounds(s, g2d).getWidth()/2.0f;
        g2d.setColor(shadow);
        g2d.drawString(s, x+1, y+1);
        g2d.setColor(c);
        g2d.drawString(s, x, y);
    }
    
    public void drawBigBar(Graphics2D g2d, int x, int y, float percent) {
    	float width = bigBarImg.getWidth(null) * percent;
    	g2d.drawImage(bigBarImg, x, y, null);
    	Rectangle s = g2d.getClipBounds();
    	g2d.setClip(x, y, (int)width, bigBarGlowImg.getHeight(null));
    	g2d.drawImage(bigBarGlowImg, x, y, null);
    	g2d.setClip(s);
    }
    
    public void drawSmallBar(Graphics2D g2d, int x, int y, float percent) {
    	float width = smallBarImg.getWidth(null) * percent;
    	g2d.drawImage(smallBarImg, x, y, null);
    	Rectangle s = g2d.getClipBounds();
    	g2d.setClip(x, y, (int)width, smallBarGlowImg.getHeight(null));
    	g2d.drawImage(smallBarGlowImg, x, y, null);
    	g2d.setClip(s);
    }
    
    private void drawShipSpecs(Graphics2D g2d) {
        g2d.setFont(bigFont);
        drawShadowedString(g2d, ""+ship.countLauncherHardpoints(), 432, 60, statWhite);
        drawShadowedString(g2d, ""+ship.countTurretHardpoints(), 572, 60, statWhite);
        drawShadowedString(g2d, ""+ship.totalRigSlots(), 432, 92, statWhite);
        drawShadowedString(g2d, ""+ship.calculateScanResolution()+" mm", 432, 124, statWhite);
        drawShadowedString(g2d, ""+((int)ship.calculateRadius())+" m", 432, 156, statWhite);
        drawShadowedString(g2d, ""+ship.getMaxLockedTargets(), 572, 92, statWhite);
        drawShadowedStringCentered(g2d, ""+((int)ship.calculateMaxShields())+"  hp", 424, 230, statWhite);
        drawShadowedStringCentered(g2d, ""+((int)ship.calculateMaxArmor())+"  hp", 424, 322, statWhite);
        drawShadowedStringCentered(g2d, ""+((int)ship.calculateMaxStructure())+"  hp", 424, 395, statWhite);
        drawShadowedString(g2d, ""+((int)ship.calculateMaxRange())+" m", 572, 124, statWhite);
        drawShadowedString(g2d, "Rechargerate  "+((int)ship.calculateRechargeRate())+" Sec.", 397, 250, statWhite);
        drawShadowedStringCentered(g2d, ""+((int)ship.calculateMaxCapacity()), 200, 355, statWhite);
        drawShadowedStringCentered(g2d, "( "+((int)ship.calculateCapacitorRechargeRate())+ " Sec. )", 200, 368, statWhite);
        drawShadowedStringCentered(g2d, "CPU "+(ship.getMaxCpu() - ship.getRemainingCpu())+" / "+ship.getMaxCpu(), 200, 398, statWhite);
        drawShadowedStringCentered(g2d, "PowerGrid "+(ship.getMaxPower() - ship.getRemainingPower())+" / "+ship.getMaxPower(), 200, 429, statWhite);
        
        //bars
        drawBigBar(g2d, 135, 363, (ship.getMaxCpu() - ship.getRemainingCpu()) / ship.getMaxCpu());
        drawBigBar(g2d, 135, 393, (ship.getMaxPower() - ship.getRemainingPower()) / ship.getMaxPower());
        
        g2d.setFont(smallFont);
        //shield
        float [] reson = ship.getShieldResonance();
        drawShadowedString(g2d, ""+((int)((1.0f - reson[0])*100))+"  %", 490, 187, statWhite);
        drawShadowedString(g2d, ""+((int)((1.0f - reson[1])*100))+"  %", 598, 187, statWhite);
        drawShadowedString(g2d, ""+((int)((1.0f - reson[3])*100))+"  %", 490, 214, statWhite);
        drawShadowedString(g2d, ""+((int)((1.0f - reson[2])*100))+"  %", 598, 214, statWhite);
        drawSmallBar(g2d, 488, 187, 1.0f - reson[0]);
        drawSmallBar(g2d, 595, 187, 1.0f - reson[1]);
        drawSmallBar(g2d, 488, 214, 1.0f - reson[3]);
        drawSmallBar(g2d, 595, 214, 1.0f - reson[2]);
        
        //armor
        reson = ship.getArmorResonance();
        drawShadowedString(g2d, ""+((int)((1.0f - reson[0])*100))+"  %", 490, 279, statWhite);
        drawShadowedString(g2d, ""+((int)((1.0f - reson[1])*100))+"  %", 598, 279, statWhite);
        drawShadowedString(g2d, ""+((int)((1.0f - reson[3])*100))+"  %", 490, 306, statWhite);
        drawShadowedString(g2d, ""+((int)((1.0f - reson[2])*100))+"  %", 598, 306, statWhite);
        drawSmallBar(g2d, 488, 279, 1.0f - reson[0]);
        drawSmallBar(g2d, 595, 279, 1.0f - reson[1]);
        drawSmallBar(g2d, 488, 306, 1.0f - reson[3]);
        drawSmallBar(g2d, 595, 306, 1.0f - reson[2]);
        
        //armor
        reson = ship.getStructureResonance();
        drawShadowedString(g2d, ""+((int)((1.0f - reson[0])*100))+"  %", 490, 353, statWhite);
        drawShadowedString(g2d, ""+((int)((1.0f - reson[1])*100))+"  %", 598, 353, statWhite);
        drawShadowedString(g2d, ""+((int)((1.0f - reson[3])*100))+"  %", 490, 380, statWhite);
        drawShadowedString(g2d, ""+((int)((1.0f - reson[2])*100))+"  %", 598, 380, statWhite);
        drawSmallBar(g2d, 488, 353, 1.0f - reson[0]);
        drawSmallBar(g2d, 595, 353, 1.0f - reson[1]);
        drawSmallBar(g2d, 488, 380, 1.0f - reson[3]);
        drawSmallBar(g2d, 595, 380, 1.0f - reson[2]);
    }
    
    private void createMountPoints() {
    	mountPoints = new Point[Module.RIG_SLOT][];
    	mountPoints[Module.LOW_SLOT] = new Point[8];
    	mountPoints[Module.MID_SLOT] = new Point[8];
    	mountPoints[Module.HI_SLOT] = new Point[8];
    	
    	mountPoints[Module.LOW_SLOT][0] = new Point(98,303);
    	mountPoints[Module.LOW_SLOT][1] = new Point(96,235);
    	mountPoints[Module.LOW_SLOT][2] = new Point(135,180);
    	mountPoints[Module.LOW_SLOT][3] = new Point(198,159);
    	mountPoints[Module.LOW_SLOT][4] = new Point(262,179);
    	mountPoints[Module.LOW_SLOT][5] = new Point(302,232);
    	mountPoints[Module.LOW_SLOT][6] = new Point(247,236);
    	mountPoints[Module.LOW_SLOT][7] = new Point(219,270);
    	
    	mountPoints[Module.MID_SLOT][0] = new Point(98,303);
    	mountPoints[Module.MID_SLOT][1] = new Point(96,235);
    	mountPoints[Module.MID_SLOT][2] = new Point(135,180);
    	mountPoints[Module.MID_SLOT][3] = new Point(198,159);
    	mountPoints[Module.MID_SLOT][4] = new Point(262,179);
    	mountPoints[Module.MID_SLOT][5] = new Point(302,232);
    	mountPoints[Module.MID_SLOT][6] = new Point(247,236);
    	mountPoints[Module.MID_SLOT][7] = new Point(219,270);
    }

	public void dragEnter(DropTargetDragEvent e) {
		((Slot)((DropTarget)e.getSource()).getComponent()).setSelected(true);
		//TODO: set attributes in yellow as per this module
	}

	public void dragOver(DropTargetDragEvent e) {
	}

	public void dropActionChanged(DropTargetDragEvent e) {
	}

	public void dragExit(DropTargetEvent e) {
		((Slot)((DropTarget)e.getSource()).getComponent()).setSelected(false);
	}

	public void drop(DropTargetDropEvent e) {
		Slot s = (Slot)((DropTarget)e.getSource()).getComponent();
		if(!ship.hasModule(s.getRack(), s.getSlotNumber())) {
			try {
				Module m = (Module)((ModuleTransferable)e.getTransferable()).getTransferData(new DataFlavor(Module.class, "Module"));
				if(ship.putModule(m, s.getRack(), s.getSlotNumber())) {
					s.mount(m);
					e.acceptDrop(DnDConstants.ACTION_COPY);
					repaint();
				} else {
					e.rejectDrop();
				}
			} catch (UnsupportedFlavorException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			e.rejectDrop();
		}
	}

	public void mouseClicked(MouseEvent e) {
		Slot s = (Slot)e.getSource();
		if(e.isPopupTrigger()) {
			if(ship.hasModule(s.getRack(), s.getSlotNumber())) {
				//TODO: pop menu to show info or put online/offline
			}
		}
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		System.out.println("wee beastie: "+e.getX()+","+e.getY());
		Slot s = (Slot)e.getSource();
		s.setSelected(true);
		
	}

	public void mouseExited(MouseEvent e) {
		System.out.println("wee beastie: "+e.getX()+","+e.getY());
		Slot s = (Slot)e.getSource();
		s.setSelected(false);
	}
}
