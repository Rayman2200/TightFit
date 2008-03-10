package tightfit.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import tightfit.item.Item;

public class ModuleListRenderer extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel title, desc;
	private MarketListEntry myComponent;
	
	private Color bgColor, bgLightColor;
	
	public ModuleListRenderer() {
		/*myComponent = new JPanel();
		myComponent.setPreferredSize(new Dimension(200, 100));
		myComponent.setOpaque(true);
		myComponent.setLayout(new BorderLayout());
		myComponent.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		
		title = new JLabel();
		desc = new JLabel();
		title.setForeground(Color.white);
		desc.setForeground(Color.white);
		component.add(title, BorderLayout.CENTER);
		component.add(desc, BorderLayout.SOUTH);
		*/
		bgColor = new Color(.07f, .25f, .43f);
		bgLightColor = new Color(.07f+0.3f, .25f+0.3f, .43f+0.3f);
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
            int index,  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        MarketListEntry module = (MarketListEntry)value;

        if (module != null) {
        	
            /*Image image = module.getImage();
            if (image != null) {
            	component.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
            }*/
        	
            //myComponent.removeAll();
            module.setBackground(bgColor);
            if(isSelected)
            	module.setBackground(bgLightColor);
            
            /*title.setText(module.name);
            desc.setText(module.getAttribute("description", "No data in database"));
            
            myComponent.add(title);
    		myComponent.add(desc);
    		myComponent.add(new JLabel("TESTING"), BorderLayout.CENTER);
            //setText(module.name + "\n\n"+module.getAttribute("description", "No data in database"));
             * 
             */
        } else {
            setIcon(null);
            setText("--");
        }

        return module;
    }
}
