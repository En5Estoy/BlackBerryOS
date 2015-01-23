package com.speryans.saldobus.framework.classes.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

/**
 * La clase UX permite implementar recursos para muchas resoluciones de pantalla
 * de una manera facil y muy parecida a Android. 
 * 
 * Para agregar recursos a la aplicacion los mismos deben ser agregados de la 
 * siguiente forma en la carpeta res de BlackBerry.
 * 
 * Dependiente de la resolucion:
 * res/images-AnchoxAlto/file.png
 * 	Ej. res/images-480x320/logo.png
 * 
 * Dependiente de la resolucion y la orientacion:
 * res/images-AnchoxAlto-Orientacion/file.png
 * 	- Se contemplan dos orientaciones
 * 		+ Horizontal (land)
 * 		+ Vertical (port)
 * 	Ej. res/images-480x320-port/logo.png
 * 
 * Dependiente de la orientacion:
 * res/images-Orientacion/file.png
 * 	Ej. res/images-port/logo.png
 * 
 * Asimismo debe existir una carpeta Default
 * res/images/file.png
 * 		+ Ante la falla de las anteriores se devuelve el valor por default o si no existe una excepcion.
 * 
 * Las imagenes deben ser PNG.
 */

public class UX {
	
	/**
	 * Get a screen size based resource
	 * Obtener un recurso basado en el tamaño de la pantalla 
	 * 
	 * @param resourse Nombre ( sin extension ) del archivo necesario / Necesary file without extension
	 * @return Bitmap
	 * @throws UXException 
	 */
	public static Bitmap getResource( String resource ) throws UXException {
		int width = Display.getWidth();
		int height = Display.getWidth();
		String or = ( Display.getOrientation() == Display.ORIENTATION_PORTRAIT ) ? "port" : "land";
		
		try {
			return Bitmap.getBitmapResource( "images-" + width + "x" + height + "-" + or + "/" + resource + ".png");
		} catch( Exception e ) {
			try {
				return Bitmap.getBitmapResource( "images-" + width + "x" + height + "/" + resource + ".png");
			} catch( Exception ex ) {
				try {
					return Bitmap.getBitmapResource( "images-" + width + "x" + height + "/" + resource + ".png");
				} catch( Exception exc ) {
					try {
						return Bitmap.getBitmapResource( "images-" + or + "/" + resource + ".png");
					} catch( Exception exce ) {
						try {
							return Bitmap.getBitmapResource( "images/" + resource + ".png");
						} catch( Exception excep ) {
							throw new UXException();
						}
					}
				}
			}
		}
	}
}
