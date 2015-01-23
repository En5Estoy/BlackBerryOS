package com.speryans.saldobus.framework.classes;

public interface ConnectionListener {
	public void ready( int msg, String message );
	public void cacheReady( int msg, String message );
}
