package com.speryans.saldobus.framework.classes;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.component.BitmapField;

import com.speryans.saldobus.framework.tools.Log;

public class ImagesLoaderWithoutResizer implements WebDataCallback {
	public static final int DOWNLOAD_READY = 499;
	public static final int DOWNLOAD_ERROR = 498;
	
	private Vector images;
	private Images current;
	
	private boolean cache = false;
	private Timer _delayTimer;
	
	public ImagesLoaderWithoutResizer( ) {
		images = new Vector();
	}
	
	public ImagesLoaderWithoutResizer( boolean ca ) {
		this.cache = ca;
		images = new Vector();
	}
	
	public void add( String url, BitmapField bitmap  ) {
		try {
			Images temp = new Images( url, bitmap, 0, 0, 0 );
			
			try {
				if( cache ) {
					byte[] dataArray = read( temp.file );
						
					EncodedImage btmp = EncodedImage.createEncodedImage(dataArray, 0, dataArray.length);
						
					temp.bitmap.setBitmap( btmp.getBitmap() );
						
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
			
				current.bitmap.setBitmap( bitmap.getBitmap() );
			
				images.removeElement(current);
			
				write( file , dataArray );
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
					ImagesLoaderWithoutResizer.this.start();
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
