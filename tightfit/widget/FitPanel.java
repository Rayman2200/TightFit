package tightfit.widget;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.ship.Ship;

public class FitPanel extends JPanel {

	private TightFit editor;
	private Ship ship;
	
	private Image panelImg, rigImg, lnchrImg, turImg,
                sigRadImg, scanImg, maxTarImg, maxRanImg;
    
	private Font bigFont;
	private Font smallFont;
	
    private Color brightWhite, dullWhite, shadow;
    
	public FitPanel(TightFit editor) throws IllegalArgumentException, IOException {
		this.editor = editor;
		
		//panelImg = Resources.getImage("panel.png");
        rigImg = Resources.getImage("icon68_01.png");
        lnchrImg = Resources.getImage("icon12_12.png");
        turImg = Resources.getImage("icon12_09.png");
        sigRadImg = Resources.getImage("icon22_14.png");
        scanImg = Resources.getImage("icon03_09.png");
        maxTarImg = Resources.getImage("icon04_12.png");
        maxRanImg = Resources.getImage("icon22_15.png");
        
		try {
			bigFont = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("stan07_57.ttf"));
            bigFont = bigFont.deriveFont(8f);
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
        brightWhite = new Color(1f,1f,1f,.95f);
        dullWhite = new Color(.9f,.9f,1f,.85f);
        shadow = new Color(.3f,.3f,.3f,.95f);
        
        setSize(680,500);
        
        setLayout(new OverlayLayout(this));
	}
	
	public void setShip(Ship s) {
		ship = s;
        repaint();
	}
	
	public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        //setup
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g2d.setComposite(AlphaComposite.SrcAtop);
                            
        //let the drawing begin!
        g2d.drawImage(panelImg, 0, 0, null);
        
        //draw lines
        g2d.setColor(brightWhite);
        g2d.drawRect(392, 37, 280, 130);
        
        g2d.setColor(dullWhite);
        g2d.draw3DRect(0, 0, 679, 499, true);
        g2d.draw3DRect(1, 1, 677, 497, true);
        g2d.drawLine(2,15,678,15);
        g2d.drawLine(393, 69, 672, 69);
        g2d.drawLine(393, 101, 672, 101);
        g2d.drawLine(393, 133, 672, 133);
        g2d.drawLine(424, 38, 424, 167);
        //g2d.drawLine(424, 38, 424, 167);
        
        for(int i=0,x=65, r=ship.countRigSlots();i<8;i++,x+=34,r--) {
            g2d.drawRect(x, 450, 32, 32);
            if(r>0)
                g2d.drawImage(rigImg, x+1, 451, null);
        }
        
        
        //draw the power bar
        
        //draw the cpu bar
        
        //draw the capacitor
        
        //draw icons
        g2d.drawImage(lnchrImg, 393, 37, null);
        g2d.drawImage(turImg, 530, 37, null);
        g2d.drawImage(rigImg, 393, 69, null);
        g2d.drawImage(scanImg, 393, 101, null);
        g2d.drawImage(sigRadImg, 393, 133, null);
        g2d.drawImage(maxTarImg, 530, 69, null);
        g2d.drawImage(maxRanImg, 530, 101, null);
        
        //draw all the labels
        //bigger font
        g2d.setFont(bigFont);
        drawShadowedString(g2d, "TIGHTFIT EVE FITTING TOOL v1.0a", 5, 12, Color.white);
        drawShadowedString(g2d, "UPGRADE HARDPOINTS", 65, 447, Color.white);

        
        //smaller font
        g2d.setFont(smallFont);
        drawShadowedString(g2d, "LAUNCHER HARDPOINTS", 430, 46, Color.white);
        drawShadowedString(g2d, "TURRET HARDPOINTS", 570, 46, Color.white);
        drawShadowedString(g2d, "UPGRADE HARDPOINTS", 430, 78, Color.white);
        drawShadowedString(g2d, "MAX LOCKED TARGETS", 570, 78, Color.white);
        drawShadowedString(g2d, "SCAN RESOLUTION", 430, 110, Color.white);
        drawShadowedString(g2d, "MAX TARGETING RANGE", 570, 110, Color.white);
        drawShadowedString(g2d, "SIGNATURE RADIUS", 430, 142, Color.white);
        
        //finally, put in the ship's specs
        drawShipSpecs(g2d);
	}
    
    public void drawShadowedString(Graphics2D g2d, String s, float x, float y, Color c) {
        
        g2d.setColor(shadow);
        g2d.drawString(s, x+1, y+1);
        g2d.setColor(c);
        g2d.drawString(s, x, y);
    }
    
    private void drawShipSpecs(Graphics2D g2d) {
        g2d.setFont(bigFont);
        drawShadowedString(g2d, ""+ship.countLauncherHardpoints(), 432, 60, dullWhite);
        drawShadowedString(g2d, ""+ship.countTurretHardpoints(), 572, 60, dullWhite);
        drawShadowedString(g2d, ""+ship.countRigSlots(), 432, 92, dullWhite);
        //drawShadowedString(
    }
}
