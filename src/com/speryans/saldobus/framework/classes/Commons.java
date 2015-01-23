package com.speryans.saldobus.framework.classes;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Status;

public class Commons {
	public static final String appName = "SaldoBus";
	public static final long appID = 0xa1feaf0e21269ffL;
	
	public static final GIFEncodedImage imageloader = (GIFEncodedImage) EncodedImage.getEncodedImageResource("loader.bin");
	public static final GIFEncodedImage blackloader = (GIFEncodedImage) EncodedImage.getEncodedImageResource("blackbackloader.bin");

	public static final String directory = "file:///SDCard/BlackBerry/" + Commons.appName + "/";

	public static final boolean isDebug = (DeviceInfo.isSimulator());

	public static String replaceAll(String source, String pattern,
			String replacement) {
		if (source == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		int idx = -1;
		int patIdx = 0;

		while ((idx = source.indexOf(pattern, patIdx)) != -1) {
			sb.append(source.substring(patIdx, idx));
			sb.append(replacement);
			patIdx = idx + pattern.length();
		}
		sb.append(source.substring(patIdx));
		return sb.toString();

	}

	public static void showMessage(String message) {
		synchronized (Application.getEventLock()) {
			UiEngine ui = Ui.getUiEngine();
			net.rim.device.api.ui.Screen screen = new Dialog(Dialog.D_OK,
					message, Dialog.OK, Bitmap
							.getPredefinedBitmap(Bitmap.EXCLAMATION),
					Manager.VERTICAL_SCROLL);

			ui.pushGlobalScreen(screen, 1, UiEngine.GLOBAL_QUEUE);
		}
	}
	
	public static void showGlobalMessage(final String message) {
		UiApplication.getApplication().invokeLater(new Runnable() {
			public void run() {
				UiEngine ui = Ui.getUiEngine();
				net.rim.device.api.ui.Screen screen = new Dialog(Dialog.D_OK,
						message, Dialog.OK, Bitmap
								.getPredefinedBitmap(Bitmap.EXCLAMATION),
						Manager.VERTICAL_SCROLL);

				ui.pushGlobalScreen(screen, 1, UiEngine.GLOBAL_QUEUE);
			}
		});
	}

	public static void showToast(final String msg) {
		UiApplication.getApplication().invokeLater(new Runnable() {
			public void run() {
				Status.show(msg);
			}
		});
	}
	
	public static Bitmap resizeBitmap(Bitmap image, int width, int height)
    {   
        //Need an array (for RGB, with the size of original image)
        //
        int rgb[] = new int[image.getWidth()*image.getHeight()];

        //Get the RGB array of image into "rgb"
        //
        image.getARGB(rgb, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

        //Call to our function and obtain RGB2
        //
        int rgb2[] = rescaleArray(rgb, image.getWidth(), image.getHeight(), width, height);

        //Create an image with that RGB array
        //
        Bitmap temp2 = new Bitmap(width, height);

        temp2.setARGB(rgb2, 0, width, 0, 0, width, height);
        
        return temp2;
    }

    private static int[] rescaleArray(int[] ini, int x, int y, int x2, int y2)
    {
        int out[] = new int[x2*y2];
        for (int yy = 0; yy < y2; yy++)
        {
            int dy = yy * y / y2;
            for (int xx = 0; xx < x2; xx++)
            {
                int dx = xx * x / x2;
                out[(x2 * yy) + xx] = ini[(x * dy) + dx];
            }
        }
        return out;
    } 

	public static Bitmap bestFit(Bitmap image, int maxWidth, int maxHeight) {

		// getting image properties
		int w = image.getWidth();
		int h = image.getHeight();

		// get the ratio
		int ratiow = 100 * maxWidth / w;
		int ratioh = 100 * maxHeight / h;

		// this is to find the best ratio to
		// resize the image without deformations
		int ratio = Math.min(ratiow, ratioh);

		// computing final desired dimensions
		int desiredWidth = w * ratio / 100;
		int desiredHeight = h * ratio / 100;

		// resizing
		return resizeBitmap(image, desiredWidth, desiredHeight);
	}

	public static Bitmap setSizeImage(EncodedImage image, int width, int height) {
		EncodedImage result = null;

		int currentWidthFixed32 = Fixed32.toFP(image.getWidth());
		int currentHeightFixed32 = Fixed32.toFP(image.getHeight());

		int requiredWidthFixed32 = Fixed32.toFP(width);
		int requiredHeightFixed32 = Fixed32.toFP(height);

		int scaleXFixed32 = Fixed32.div(currentWidthFixed32,
				requiredWidthFixed32);
		int scaleYFixed32 = Fixed32.div(currentHeightFixed32,
				requiredHeightFixed32);

		result = image.scaleImage32(scaleXFixed32, scaleYFixed32);
		return result.getBitmap();
	}
	
	public static int getMaxHeight( int widthf, int width, int height ) {
		return ( ( widthf / width ) * height );
	}
}
