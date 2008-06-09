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
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.module.Module;
import tightfit.ship.Ship;
import tightfit.ship.ShipChangeListener;

public class TankPanel extends JPanel implements TightFitDropTargetPanel, ShipChangeListener, MouseListener {
	private static final long serialVersionUID = 1L;

	private TightFit editor;
	private IntegerSpinner emDmg, exDmg, knDmg, thDmg, frame;
	private JPanel simPanel;
	private Ship memShip;
	
	private Font bigFont;
	
	private Image totalHpImg, shieldImg, armorImg, emDmgImg, expDmgImg, thDmgImg, bgImg, capImg;
	
	public TankPanel(TightFit editor) {
		this.editor = editor;
		
		try {
			totalHpImg = Resources.getImage("icon22_17.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			shieldImg = Resources.getImage("icon02_03.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			armorImg = Resources.getImage("icon01_11.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			emDmgImg = Resources.getImage("icon22_20.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			expDmgImg = Resources.getImage("icon22_19.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			thDmgImg = Resources.getImage("icon22_18.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			bgImg = Resources.getImage("panelgrad.png");
			capImg = Resources.getImage("icon39_06.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			Font big = Resources.getFont("stan07_57.ttf");
	        bigFont = big.deriveFont(8f);
		} catch (Exception e) {
		}
		
		emDmg = new IntegerSpinner(0, 0, 10000);
		exDmg = new IntegerSpinner(10, 0, 10000);
		knDmg = new IntegerSpinner(14, 0, 10000);
		thDmg = new IntegerSpinner(0, 0, 10000);
		frame = new IntegerSpinner(30, 0, 6000);
		
		setLayout(new SlickLayout());
		
		add(emDmg);
		emDmg.setLocation(285, 36);
		add(exDmg);
		exDmg.setLocation(285, 66);
		add(knDmg);
		knDmg.setLocation(285, 96);
		add(thDmg);
		thDmg.setLocation(285, 126);
		
		add(frame);
		frame.setLocation(450, 128);
		
		buildShip();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		Dimension size = getSize();
		Color bgColor = getBackground();
		Color bgLightColor = new Color(1,1,1,0.25f);
		Ship ship = editor.getShip();
		
        //setup
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
        					RenderingHints.VALUE_RENDER_QUALITY);
        
        g2d.setComposite(AlphaComposite.SrcAtop);
        
        g2d.setFont(bigFont);
        
        g2d.setColor(bgColor);
        g2d.fillRect(0,0,size.width,size.height);
        g2d.drawImage(bgImg, 0,0, size.width, size.height, null);
        
        // -- TANK -- //
        g2d.setColor(bgLightColor);
        g2d.fillRoundRect(8, 8, 230, 100, 40, 40);
        g2d.drawImage(shieldImg, 10, 19, null);
        g2d.drawImage(armorImg, 10, 45, null);
        g2d.drawImage(totalHpImg, 10, 75, null);
        
        g2d.setColor(Color.WHITE);
        
        FitPanel.drawShadowedString(g2d, "Rate", 115, 19, Color.white);
        FitPanel.drawShadowedString(g2d, "Rep Time (Sec.)", 160, 19, Color.white);
        
        float passiveHp = (ship.calculateMaxShields() / ship.calculateRechargeRate()) * 2.5f;
        float activeHp = calcActiveHp();
        float activeArmorHp = calcActiveArmorHp();
        
        //shield
        FitPanel.drawShadowedString(g2d, "Passive: "+String.format("%1$14.2f", new Object[]{new Float(passiveHp)})+" hp/s", 45, 33, Color.white);
        FitPanel.drawShadowedString(g2d, "Active: "+String.format("%1$15.2f", new Object[]{new Float(activeHp)})+" hp/s", 45, 44, Color.white);
        
        FitPanel.drawShadowedStringCentered(g2d, String.format("%1$.2f", new Object[]{new Float(ship.calculateMaxShields()/(passiveHp+activeHp))}), 185, 38, Color.white);
        
        //armor
        FitPanel.drawShadowedString(g2d, "Active: "+String.format("%1$15.2f", new Object[]{new Float(activeArmorHp)})+" hp/s", 45, 63, Color.white);
        
        FitPanel.drawShadowedStringCentered(g2d, String.format("%1$.2f", new Object[]{new Float(ship.calculateMaxArmor()/activeArmorHp)}), 185, 63, Color.white);
        
        g2d.drawLine(112,21,134,21);
        g2d.drawLine(158,21,220,21);
        g2d.drawLine(40,80,150,80);
        FitPanel.drawShadowedString(g2d, "Total Effective HP: "+calcEffectiveHP(frame.intValue()), 45, 93, Color.white);
        
        //cap
        g2d.setColor(bgLightColor);
        g2d.fillRoundRect(8, 120, 230, 30, 20, 20);
        g2d.drawImage(capImg, 10, 118, null);
        float capChargeRate = (ship.calculateMaxCapacity() / ship.calculateCapacitorRechargeRate()) * 2.5f;
        float usage = ship.calculateCapacitorUsage();
        Color capColor = Color.white;
        if(capChargeRate < usage) {
        	capColor = new Color(1.0f, .1f, .1f);
        	int seconds = (int) (ship.calculateMaxCapacity() / (usage - capChargeRate));
        	if(seconds/60 > 60)
        		FitPanel.drawShadowedStringCentered(g2d, ""+(seconds / 3600)+"h "+((seconds/60)%60)+"m "+(seconds % 60)+"s", 185, 135, capColor);
        	else FitPanel.drawShadowedStringCentered(g2d, ""+(seconds / 60)+"m "+(seconds % 60)+"s", 185, 135, capColor);
        }
        FitPanel.drawShadowedString(g2d, "Cap. Balance: +"+String.format("%1$.2f", new Object[]{new Float(capChargeRate)}) + " / -"+ String.format("%1$.2f", new Object[]{new Float(usage)}), 45, 135, capColor);
        
        // -- ENEMY DPS -- //
        g2d.setColor(bgLightColor);
        g2d.fillRoundRect(250, 8, 130, 150, 40, 40);
        g2d.setColor(Color.WHITE);
        FitPanel.drawShadowedString(g2d, "Enemy DPS", 266, 19, Color.white);
        g2d.drawImage(emDmgImg, 252, 30, null);
        FitPanel.drawShadowedString(g2d, "/sec", 350, 49, Color.white);
        g2d.drawImage(expDmgImg, 252, 60, null);
        g2d.drawImage(totalHpImg, 252, 90, null);
        g2d.drawImage(thDmgImg, 252, 120, null);
        
        // -- SIM -- //
        g2d.setColor(bgLightColor);
        g2d.fillRoundRect(392, 8, 275, 150, 40, 40);
        FitPanel.drawShadowedString(g2d, "Simulation", 410, 19, Color.white);
        
        FitPanel.drawShadowedString(g2d, "Timeframe", 400, 140, Color.white);
        
	}

	public void shipChanged(Ship ship) {
		if(memShip == null || ship != memShip) {
			memShip = ship;
            buildShip();
        }
		else updateShip();
		repaint();
	}
	
	private int calcEffectiveHP(float time) {
		Ship ship = editor.getShip();
		
		float passiveShieldTank = ((ship.calculateMaxShields() / ship.calculateRechargeRate()) * 2.5f); 
		float activeShieldTank = calcActiveHp();
		
		float [] reson = ship.getShieldResonance();
		float shieldTank = ship.calculateMaxShields() + time * (passiveShieldTank + activeShieldTank) +
							(emDmg.intValue() * time) * (1-reson[0]) +
							(knDmg.intValue() * time) * (1-reson[1]) +
							(thDmg.intValue() * time) * (1-reson[2]) +
							(exDmg.intValue() * time) * (1-reson[3]);
		
		reson = ship.getArmorResonance();
		float armorTank = ship.calculateMaxArmor() + time * calcActiveArmorHp() +
							(emDmg.intValue() * time) * (1-reson[0]) +
							(knDmg.intValue() * time) * (1-reson[1]) +
							(thDmg.intValue() * time) * (1-reson[2]) +
							(exDmg.intValue() * time) * (1-reson[3]);
		
		
		reson = ship.getStructureResonance();
		float structTank = ship.calculateMaxStructure() + time * calcActiveStructureHp() +
							(emDmg.intValue() * time) * (1-reson[0]) +
							(knDmg.intValue() * time) * (1-reson[1]) +
							(thDmg.intValue() * time) * (1-reson[2]) +
							(exDmg.intValue() * time) * (1-reson[3]);
		
		return (int) (shieldTank + armorTank + structTank);
	}
	
	private float calcActiveHp() {
		Ship ship = editor.getShip();
		float total=0;
		
		Module [] mods = ship.findModuleByAttribute("shieldBonus", Module.MID_SLOT);
		
		//FIXME: this doesn't account for skill, or other bonuses (ship, rig, other modules)
		if(mods != null)
			for(int i=0;i<mods.length;i++) {
				total += (Float.parseFloat(mods[i].getAttribute("shieldBonus", "0"))/(Float.parseFloat(mods[i].getAttribute("duration", "1"))/1000.0f));
			}
		
		return total;
	}
	
	private float calcActiveArmorHp() {
		Ship ship = editor.getShip();
		float total=0;
		
		Module [] mods = ship.findModuleByAttribute("armorDamageAmount", Module.LOW_SLOT);
		
		//FIXME: this doesn't account for skill, or other bonuses (ship, rig, other modules)
		if(mods != null)
			for(int i=0;i<mods.length;i++) {
				total += (Float.parseFloat(mods[i].getAttribute("armorDamageAmount", "0"))/(Float.parseFloat(mods[i].getAttribute("duration", "1"))/1000.0f));
			}
		
		return total;
	}
	
	private float calcActiveStructureHp() {
		return 0f; //TODO: this
	}
	
	private void buildShip() {
		Ship ship = memShip;
		
		if(ship == null)
			return;
		
		if(simPanel != null)
			remove(simPanel);
		simPanel = new JPanel();
		simPanel.setOpaque(false);
		simPanel.setPreferredSize(new Dimension(280, 150));
		simPanel.setLayout(new SlickLayout());
		
		//HI
		for(int i=0;i<ship.totalSlots(Module.HI_SLOT);i++) {
			SimulatorSlot slot = new SimulatorSlot(editor, this, Module.HI_SLOT, i);
			//slot.mount(ship.getModule(Module.HI_SLOT, i));
            slot.setLocation(i*34 + 3, 17);
    		simPanel.add(slot);
    		slot.addMouseListener(this);
		}
        
		//MED
		for(int i=0;i<ship.totalSlots(Module.MID_SLOT);i++) {
			SimulatorSlot slot = new SimulatorSlot(editor, this, Module.MID_SLOT, i);
			//slot.mount(ship.getModule(Module.MID_SLOT, i));
            slot.setLocation(i*34 + 11, 49);
            simPanel.add(slot);
    		slot.addMouseListener(this);
		}
		
		//LOW
		for(int i=0;i<ship.totalSlots(Module.LOW_SLOT);i++) {
			SimulatorSlot slot = new SimulatorSlot(editor, this, Module.LOW_SLOT, i);
			//slot.mount(ship.getModule(Module.LOW_SLOT, i));
			slot.setLocation(i*34 + 3, 81);
			simPanel.add(slot);
    		slot.addMouseListener(this);
		}
		
		add(simPanel);
		simPanel.setLocation(392, 8);
		simPanel.repaint();
	}

	private void updateShip() {
		Ship ship = editor.getShip();
		for(int i=0;i<simPanel.getComponentCount();i++) {
			SimulatorSlot s = (SimulatorSlot)simPanel.getComponent(i);
			s.mount(ship.getModule(s.getRack(), s.getSlotNumber()));
		}
	}
	
	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void drop(DropTargetDropEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseClicked(MouseEvent e) {
		SimulatorSlot s = (SimulatorSlot)e.getSource();
		Ship ship = editor.getShip();
		if (e.getButton() == MouseEvent.BUTTON1) {
			if(ship.hasModule(s.getRack(), s.getSlotNumber())) {
				Module m = ship.getModule(s.getRack(), s.getSlotNumber());
				try {
					if(m.requiresActivation()) {
						if(!m.isActive())
							m.activate();
						else m.deactivate();
						ship.fireShipChange();
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Activation Failed!", e1.getMessage(), JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}
}
