package com.speryans.saldobus.framework.classes;

import java.util.Vector;

import net.rim.device.api.system.Application;

public class MultipleDownloader implements DownloaderListener {
	private static final int DOWNLOAD_READY = 1000;
	
	private String destination;
	private MultipleListener listener;
	
	private Vector urls = new Vector();
	
	private int index = -1;
	private String currentUrl = "";

	private Downloader currentDownload;
	
	public MultipleDownloader( MultipleListener listener ) {
		//this.destination = dest;
		this.listener = listener;
	}
	
	public void add( String url ) {
		urls.addElement(url);
	}
	
	public void start( ) {
		index++;
		
		try {
		if( index < urls.size() ) {
			currentUrl = (String)urls.elementAt(index);
			
			currentDownload = Downloader.createDownload(this, currentUrl, DOWNLOAD_READY);
			currentDownload.start();
		} else {
			listener.partDownloadFinished(0);
		}
		} catch( Exception e ) {
			synchronized (Application.getEventLock()) {
				Commons.showGlobalMessage(e.toString());
			}
		}
	}
	
	public void stopAllAndClean() {
		this.currentDownload.stopDownload();
		
		urls.removeAllElements();
		
		listener.downloadsCanceled();
	}
	
	public void downloadReady(int msg, String message) {
		switch( msg ) {
		case DOWNLOAD_READY:
			synchronized (Application.getEventLock()) {
        		synchronized( this ) {
        			listener.downloadPartReady(currentUrl, message);
        			
        			System.gc();
        			this.start();
        		}
			}
			break;
		case Downloader.ERROR:
			Commons.showGlobalMessage( message );
			break;
		}
	}

	public void progressChanged(int msg, long length, long total) {
		listener.partProgressChanged(length, total, urls.size());
	}

}
