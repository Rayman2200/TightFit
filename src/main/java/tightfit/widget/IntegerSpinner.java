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

import java.awt.Dimension;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class IntegerSpinner extends JSpinner
{
    public IntegerSpinner() {
        super(new SpinnerNumberModel());
        setPreferredSize(new Dimension(60, getPreferredSize().height));
    }

    public IntegerSpinner(int val, int min, int max) {
        super(new SpinnerNumberModel(val, min, max, 1));
        setPreferredSize(new Dimension(60, getPreferredSize().height));
    }

    public IntegerSpinner(int val, int min) {
        this(val, min, Integer.MAX_VALUE);
    }

    public void setValue(int value) {
        setValue(new Integer(value));
    }

    public int intValue() {
        return ((Number)getValue()).intValue();
    }
}
