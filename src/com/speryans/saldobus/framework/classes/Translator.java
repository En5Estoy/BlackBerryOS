package com.speryans.saldobus.framework.classes;

import net.rim.device.api.i18n.Locale;
import net.rim.device.api.system.Bitmap;

public interface Translator {
	public static final String defaultLocale = "en";
	public static final String locale = Locale.getDefaultForSystem().getLanguage();
	
	public Bitmap getBitmap( String file );
	
	public void initTextTranslation();
	
	public String getString( String tag );

	public String getLocale();
}
