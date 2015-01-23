package com.speryans.saldobus.framework.classes;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import com.speryans.saldobus.framework.tools.Log;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.component.BitmapField;

public class ImagesLoader implements WebDataCallback {
	public static final int DOWNLOAD_READY = 499;
	public static final int DOWNLOAD_ERROR = 498;
	
	private Vector images;
	private Images current;
	
	private boolean cache = false;
	private Timer _delayTimer;
	
	public ImagesLoader( ) {
		images = new Vector();
	}
	
	public ImagesLoader( boolean ca ) {
		this.cache = ca;
		images = new Vector();
	}
	
	public void add( String url, BitmapField bitmap, int width, int height, int scale ) {
		try {
			Images temp = new Images( url, bitmap, width, height, scale );
			
			try {
				if( cache ) {
					byte[] dataArray = read( temp.file );
						
					EncodedImage btmp = EncodedImage.createEncodedImage(dataArray, 0, dataArray.length);
					
					Bitmap final_bitmap = new Bitmap(temp.width, temp.height);
					btmp.getBitmap().scaleInto(final_bitmap, Bitmap.FILTER_BILINEAR, temp.scale );
					temp.bitmap.setBitmap( final_bitmap );
						
					return;
				}
			} catch( Exception e ) {
				Log.error( "Error reading cache: " + e.toString() );
			}
			
			images.addElement( temp );
			Log.info("URL: " + url + " added");
		} catch( Exception e ) {
			Log.error("Error adding to ImagesLoader: " + e.toString());
		}
	}
	
	public void start() {
		try {
			current = (Images) images.firstElement();
		
			Tools.getWebData(current.url, this);
		} catch( Exception e ){
			Log.error( "Error starting ImagesLoader thread: " + e.toString() );
		}
	}

	public void callback(String data) {
		if ( !data.startsWith("Exception") ) {
			try {
				String file = current.file;
			
				byte[] dataArray = data.getBytes();
			
				EncodedImage bitmap = EncodedImage.createEncodedImage(dataArray, 0, dataArray.length);
			
				Bitmap final_bitmap = new Bitmap(current.width, current.height);
				bitmap.getBitmap().scaleInto(final_bitmap, Bitmap.FILTER_BILINEAR, current.scale );
				current.bitmap.setBitmap( final_bitmap );
			
				images.removeElement(current);
			
				if( cache ) {
					write( file , dataArray );
				}
			} catch (final Exception e){
				Log.error("Error in callback: " + e.toString());
			}
			
			_delayTimer = new Timer();
			_delayTimer.schedule(new DelayTimer(),1500);
		} else {
			_delayTimer = new Timer();
			_delayTimer.schedule(new DelayTimer(),4000);
		}
	}
	
	private class DelayTimer extends TimerTask 
    {
		DelayTimer( ) {
			super();
		}

		public void run() {
			try {
				synchronized (Application.getEventLock()) {
					ImagesLoader.this.start();
				}
			} catch (Throwable e){ }
		}
	}
	
	private byte [] read(String fileName) {
        InputStream input = null;
        FileConnection fconn = null;
        byte [] readBuffer = null;
        try {
            fconn = (FileConnection)Connector.open(fileName, Connector.READ);
            if (fconn.exists()) {
                long fileSize = fconn.fileSize();
                readBuffer = new byte [(int) fileSize];
                input = fconn.openInputStream();
                int readLen = input.read(readBuffer);
            } else {
            	Log.error("File " + fileName + " not exists");
            }
        } catch (Exception e) {
            System.out.println("Exception in File Processing" +
                               ".\nError Exception: " + e.toString());
            readBuffer = null;
        } finally {
            try {
                if ( input != null ) {
                    input.close();
                }
                if ( fconn != null ) {
                   fconn.close();
                }
            } catch (Exception e) {
                System.out.println("Exception in file tidying : " + e.toString()); 
            }
        }
        return readBuffer;
    }
	
	private void write( String filename, byte[] data ) {
		try {
			FileConnection fc = (FileConnection)Connector.open( filename );
			DataOutputStream writer = null;
			if( fc.exists() ) {
				fc.delete();
			}
			
			try {
				fc.create();
				
				writer = fc.openDataOutputStream();
					
				writer.write(data);
					
				writer.flush();
			} catch( Exception e ) {					
				Log.error( "Error reading cache: " + e.toString() );
			} finally {
				try {
					writer.close();
				}catch( Exception e ) {}
				try {
					fc.close();
				}catch( Exception e ) {}
			}
		} catch( Exception ex ) {
			Log.error("Error reading cache");
		}
	}
}
