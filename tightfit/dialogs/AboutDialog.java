/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */
package tightfit.dialogs;

import java.awt.*;

import javax.swing.*;

public class AboutDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	public AboutDialog(JFrame parent) {
        super(parent, "TightFit v0.1.2a");

        setContentPane(createMainPanel());
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(parent);
        pack();
    }
    
    private JPanel createMainPanel() {
    	JPanel panel = new JPanel();
    	
    	JTextArea jta = new JTextArea();
    	
    	JScrollPane sp = new JScrollPane();
    	//sp.setAutoscrolls(true);
    	sp.setPreferredSize(new Dimension(290, 270));
    	sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	panel.add(sp);
    	
    	String text = "\n" +
    					"  ----------================--------  \n" +
    					"            TightFit v0.1.2a          \n" +
    					"           The EVE® Fit Tool          \n" +
    					"  ----------================--------  \n" +
    					"                                      \n" +
    					"                  By:                 \n" +
    					"            biggeruniverse            \n" +
    					"                                      \n" +
    					"              Thanks To:              \n" +
    					"       Modulus Zero & Gnomercie       \n" +
    					"                DEFI4NT               \n" +
    					"     Caldari Deep Space Industrial    \n" +
    					"                                      \n" +
    					"                                      \n" +
    					"  ----------================--------  \n" +
    					"(This is a feature-incomplete version)\n" +
    					"                                      \n" +
    					" Skill-based Calculations:        25% \n" +
    					" Module-based Calculations:       15% \n" +
    					" Fit Panel:                       95% \n" +
    					" Tank Tab:                        85% \n" +
    					" DPS Tab:                         60% \n" +
    					" Drone Info Tab:                   0% \n" +
    					" Show Info Dialog                 65% \n" +
    					" Market Dialog                    75% \n" +
    					" Import EFT2.5 Data              100% \n" +
    					" Preferences & Memory              0% \n" +
    					" Java L&F Mod                      0% \n" +
    					"  ----------================--------  \n" +
    					"                                      \n" +
    					"                *  *  *               \n" +
    					" I know! I'll make a game where peepz \n" +
    					" can lie, kill, and steal and I'll    \n" +
    					" make it so there's a police force and\n" +
    					" bounties, and stuff- but no truly    \n" +
    					" safe zones, and then I'll call the   \n" +
    					" the players who don't fight carebears\n" +
    					" and then all the other primary school\n" +
    					" students will like me...             \n" +
    					"                *  *  *               \n" +
    					"                                      \n" +
    					" EVE Online is a registered trademark \n" +
    					"             of CCP Games             \n" +
    					"                                      \n" +
    					"  ----------================--------  \n";
    	
    	jta.setText(text);
    	jta.setEditable(false);
        jta.setFont(new Font("Courier", Font.PLAIN, 12));
        sp.getViewport().setView(jta);
        jta.select(0,0);
        
    	return panel;
    }
}
