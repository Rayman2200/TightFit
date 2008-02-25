package tightfit.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JPanel;

import tightfit.Resources;
import tightfit.TightFit;
import tightfit.ship.Ship;

public class FitPanel extends JPanel {

	private TightFit editor;
	private Ship ship;
	
	private Image panelImg;
	private Font bigFont;
	private Font smallFont;
	
	public FitPanel(TightFit editor) throws IllegalArgumentException, IOException {
		this.editor = editor;
		
		panelImg = Resources.getImage("panel.png");
		try {
			bigFont = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("bbigfont.ttf"));
			smallFont = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("smallfont.ttf"));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setShip(Ship s) {
		ship = s;
	}
	
	public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.drawImage(panelImg, 0, 0, null);
        
        //draw lines
        g2d.setColor(new Color(1f,1f,1f));
        g2d.drawRect(392, 37, 280, 130);
        
        g2d.setColor(new Color(1f,1f,1f,.9f));
        for(int i=0,x=65;i<8;i++,x+=34)
        g2d.drawRect(x, 450, 32, 32);
        
        
        //draw the power bar
        
        //draw the cpu bar
        
        //draw the capacitor
        
        //draw all the labels
        //bigger font
        g2d.setFont(bigFont);
        g2d.drawString("FITTING", 5, 5);
        g2d.drawString("UPGRADE HARDPOINTS", 64, 440);
        
        
        //smaller font
        g2d.setFont(smallFont);
        g2d.drawString("LAUNCHER HARDPOINTS", 430, 44);
        g2d.drawString("TURRET HARDPOINTS", 570, 44);
        g2d.drawString("UPGRADE HARDPOINTS", 430, 76);
	}
}
