package tightfit.widget;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tightfit.item.Item;

public class MarketListEntry extends JPanel {
	private static final long serialVersionUID = 1L;

	private Item myItem;
	
	public MarketListEntry(Item item) {
		setPreferredSize(new Dimension(200, 100));
		setOpaque(true);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		
		myItem = item;
		
		add(new JLabel(item.name), BorderLayout.CENTER);
		add(new JLabel(item.getAttribute("description", "No data in database")), BorderLayout.SOUTH);
		setToolTipText(item.name);
	}
	
	public Item getItem() {
		return myItem;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		
		Dimension size = getSize();
		g2d.setColor(getBackground());
        g2d.fillRect(0,0,size.width,size.height);
        
        Point imgPos = new Point(10, size.height / 2 - 32);
        
        g2d.drawImage(myItem.getImage(), imgPos.x, imgPos.y, null);
        
        g2d.setColor(Color.WHITE);
        g2d.drawString(myItem.name, 72, imgPos.y);
        g2d.drawString(myItem.getAttribute("description", "No data in database"), 72, imgPos.y+32);
	}
	
}
