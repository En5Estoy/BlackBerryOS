package com.speryans.saldobus.framework.classes.ui;

import net.rim.device.api.ui.Font;

public class FixedFontColorField extends FontColorField
{
    
    private int width = 100;
    
    public FixedFontColorField(String text, long style, int color, Font font, int width)
    {
        super(text, style, color, font);
        
        this.width = width;
    }
    
    public int getPreferredWidth() {
    	return this.width;
    }
} 