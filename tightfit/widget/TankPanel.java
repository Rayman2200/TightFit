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

import javax.swing.JPanel;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.module.Module;
import tightfit.ship.Ship;
import tightfit.ship.ShipChangeListener;

public class TankPanel extends JPanel implements ShipChangeListener {
	private static final long serialVersionUID = 1L;

	private TightFit editor;
	private IntegerSpinner emDmg, exDmg, knDmg, thDmg, frame;
	
	private Font bigFont;
	
	private Image totalHpImg, shieldImg, armorImg, emDmgImg, expDmgImg, thDmgImg;
	
	public TankPanel(TightFit editor) {
		this.editor = editor;
		
		try {
			totalHpImg = Resources.getImage("icon22_17.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			shieldImg = Resources.getImage("icon02_03.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			armorImg = Resources.getImage("icon01_11.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			emDmgImg = Resources.getImage("icon22_20.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			expDmgImg = Resources.getImage("icon22_19.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			thDmgImg = Resources.getImage("icon22_18.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH);
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
        
        // -- TANK -- //
        g2d.setColor(bgLightColor);
        g2d.fillRoundRect(8, 8, 230, 100, 40, 40);
        g2d.drawImage(shieldImg, 10, 19, null);
        g2d.drawImage(armorImg, 10, 45, null);
        g2d.drawImage(totalHpImg, 10, 75, null);
        
        g2d.setColor(Color.WHITE);
        
        FitPanel.drawShadowedString(g2d, "Rate", 115, 19, Color.white);
        FitPanel.drawShadowedString(g2d, "Rep Time (Sec.)", 160, 19, Color.white);
        
        float passiveHp = ((ship.calculateMaxShields() / ship.calculateRechargeRate()) * 2.5f);
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
        
        // -- ENEMY DPS -- //
        g2d.setColor(bgLightColor);
        g2d.fillRoundRect(250, 8, 130, 150, 40, 40);
        g2d.setColor(Color.WHITE);
        FitPanel.drawShadowedString(g2d, "Enemy DPS", 266, 19, Color.white);
        g2d.drawImage(emDmgImg, 252, 30, null);
        g2d.drawImage(expDmgImg, 252, 60, null);
        g2d.drawImage(totalHpImg, 252, 90, null);
        g2d.drawImage(thDmgImg, 252, 120, null);
        
        // -- SIM -- //
        g2d.setColor(bgLightColor);
        g2d.fillRoundRect(392, 8, 270, 150, 40, 40);
        
        FitPanel.drawShadowedString(g2d, "Timeframe", 400, 140, Color.white);
        
	}

	public void shipChanged(Ship ship) {
		repaint();
	}
	
	private int calcEffectiveHP(float time) {
		Ship ship = editor.getShip();
		
		int passiveShieldTank = (int)((ship.calculateMaxShields() / ship.calculateRechargeRate()) * 2.5f); 
		int activeShieldTank = (int)calcActiveHp();
		
		int activeArmorTank = (int)calcActiveArmorHp();
		
		return (int) (ship.calculateMaxShields() + time * (passiveShieldTank + activeShieldTank) + time * activeArmorTank + ship.calculateMaxArmor() + ship.calculateMaxStructure());
	}
	
	private float calcActiveHp() {
		Ship ship = editor.getShip();
		float total=0;
		
		Module [] mods = ship.findModuleByAttribute("shieldBonus", Module.MID_SLOT);
		
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
		
		if(mods != null)
			for(int i=0;i<mods.length;i++) {
				total += (Float.parseFloat(mods[i].getAttribute("armorDamageAmount", "0"))/(Float.parseFloat(mods[i].getAttribute("duration", "1"))/1000.0f));
			}
		
		return total;
	}
}
