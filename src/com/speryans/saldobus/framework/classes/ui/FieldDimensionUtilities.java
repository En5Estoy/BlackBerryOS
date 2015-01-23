
package com.speryans.saldobus.framework.classes.ui;

import net.rim.device.api.ui.Field;

public class FieldDimensionUtilities
{
    private FieldDimensionUtilities() { }
    
    public static int getBorderWidth( Field field )
    {
        int width = 0;

        width = field.getWidth() - field.getContentWidth() - field.getPaddingLeft() - field.getPaddingRight();
        return width;
    }
    
    public static int getBorderHeight( Field field )
    {
        int height = 0;

        height = field.getWidth() - field.getContentHeight() - field.getPaddingTop() - field.getPaddingBottom();
        return height;
    }

    public static int getBorderAndPaddingWidth( Field field )
    {
        int width = 0;

        width = field.getWidth() - field.getContentWidth();

        return width;
    }

    public static int getBorderAndPaddingHeight( Field field )
    {
        int height = 0;

        height = field.getHeight() - field.getContentHeight();

        return height;
    }

}
