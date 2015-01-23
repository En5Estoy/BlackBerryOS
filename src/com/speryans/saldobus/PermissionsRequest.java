package com.speryans.saldobus;

import java.util.Vector;

public class PermissionsRequest {
	private Vector permissions;
	
	public PermissionsRequest() {
		permissions = new Vector();
	}
	
	public void addPermission( int permission ) {
		permissions.addElement( new Integer( permission ) );
	}
	
	public int getPermission( int index ) {
		return ((Integer)permissions.elementAt(index)).intValue();
	}
	
	public Vector getPermissions() {
		return permissions;
	}
}
