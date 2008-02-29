package tightfit.widget;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.ship.Ship;

public class FitPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TightFit editor;
	private Ship ship;
	
	private Image panelImg, rigImg, lnchrImg, turImg,
                sigRadImg, scanImg, maxTarImg, maxRanImg,
                cargoImg, shieldImg;
    
    private Image rstEmImg, rstExImg, rstThImg, rstKnImg;
                
	private Font bigFont, smallFont,
				shipTypeFont, shipTitleFont;
	
    private Color bgColor, brightWhite, dullWhite, shadow;
    
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
        
        rstEmImg = Resources.getImage("icon22_20.png");
        rstExImg = Resources.getImage("icon22_19.png");
        rstThImg = Resources.getImage("icon22_18.png");
        rstKnImg = Resources.getImage("icon22_17.png");
        
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
        
        bgColor = new Color(.43f, .44f, .8f);
        brightWhite = new Color(1f,1f,1f,.95f);
        dullWhite = new Color(.9f,.9f,1f,.85f);
        shadow = new Color(.1f,.1f,.1f,.95f);
        
        setPreferredSize(new Dimension(680,500));
        
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
        g2d.drawImage(turImg, 532, 37, null);
        g2d.drawImage(rigImg, 393, 69, null);
        g2d.drawImage(scanImg, 393, 101, null);
        g2d.drawImage(sigRadImg, 393, 133, null);
        g2d.drawImage(maxTarImg, 532, 69, null);
        g2d.drawImage(maxRanImg, 532, 101, null);
        g2d.drawImage(rstEmImg, 458, 175, null); g2d.drawImage(rstKnImg, 566, 175, null);
        g2d.drawImage(rstEmImg, 458, 268, null); g2d.drawImage(rstKnImg, 566, 268, null);
        g2d.drawImage(rstEmImg, 458, 340, null); g2d.drawImage(rstKnImg, 566, 340, null);
        g2d.drawImage(rstExImg, 458, 202, null); g2d.drawImage(rstThImg, 566, 202, null);
        g2d.drawImage(rstExImg, 458, 295, null); g2d.drawImage(rstThImg, 566, 295, null);
        g2d.drawImage(rstExImg, 458, 367, null); g2d.drawImage(rstThImg, 566, 367, null);
        g2d.drawImage(cargoImg, 393, 432, null);
        g2d.drawImage(shieldImg, 399, 180, null);
        
        //draw all the labels
        //bigger font
        g2d.setFont(bigFont);
        drawShadowedString(g2d, "TIGHTFIT EVE FITTING TOOL v1.0a", 5, 12, Color.white);
        drawShadowedString(g2d, "UPGRADE HARDPOINTS", 65, 447, Color.white);
        drawShadowedString(g2d, "Rechargerate", 397, 250, Color.white);
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
    
    private void drawShipSpecs(Graphics2D g2d) {
        g2d.setFont(bigFont);
        drawShadowedString(g2d, ""+ship.countLauncherHardpoints(), 432, 60, dullWhite);
        drawShadowedString(g2d, ""+ship.countTurretHardpoints(), 572, 60, dullWhite);
        drawShadowedString(g2d, ""+ship.countRigSlots(), 432, 92, dullWhite);
        drawShadowedString(g2d, ""+ship.calculateScanResolution()+" mm", 432, 124, dullWhite);
        drawShadowedString(g2d, ""+((int)ship.calculateRadius())+" m", 432, 156, dullWhite);
        drawShadowedString(g2d, ""+ship.getMaxLockedTargets(), 572, 92, dullWhite);
        drawShadowedStringCentered(g2d, ""+((int)ship.calculateMaxShields())+"  hp", 424, 230, dullWhite);
        drawShadowedStringCentered(g2d, ""+((int)ship.calculateMaxArmor())+"  hp", 424, 320, dullWhite);
        drawShadowedString(g2d, ""+((int)ship.calculateMaxRange())+" m", 572, 124, dullWhite);
    }
}
