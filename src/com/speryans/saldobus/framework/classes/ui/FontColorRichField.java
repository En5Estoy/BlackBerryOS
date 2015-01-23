package com.speryans.saldobus.framework.classes.ui;

import net.rim.device.api.i18n.Locale;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.RichTextField;

public class FontColorRichField extends RichTextField {

	private int color = Color.BLACK;
    
    public FontColorRichField(String text, long style, int color)
    {
        super(text, style);
        this.color = color;
        int height = Font.getDefault().getHeight() - 3;
        setFont(Font.getDefault().derive(Font.PLAIN, height));
    }
    public FontColorRichField(String text, long style, int color, Font font)
    {
        super(text, style);
        this.color = color;
        setFont(font);
    }
	
	public Locale getPreferredInputLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getTextInputStyle() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isUnicodeInputAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void updateInputStyle() {
		// TODO Auto-generated method stub

	}

	public void setColor(int color)
    {
        this.color = color;
    }
    
    public int getColor()
    {
        return color;
    }
    
    public void paint(Graphics g)
    {
        //g.setBackgroundColor(0x00359AFF);
        //g.clear();
        g.setColor(color);
        super.paint(g);
    }
	
}
