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

import java.awt.Font;

import javax.swing.JToolTip;

import tightfit.Resources;
import tightfit.widget.ui.EveToolTipUI;

public class MultiLintToolTip extends JToolTip {

	public MultiLintToolTip() {
		setUI(new EveToolTipUI());
		Font smallFont;
		try {
			smallFont = Resources.getFont("stan07_57.ttf");
			smallFont = smallFont.deriveFont(7f);
			setFont(smallFont);
		} catch (Exception e) {
		}
	}
}
