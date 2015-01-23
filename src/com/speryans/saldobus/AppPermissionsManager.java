package com.speryans.saldobus;

import java.util.Vector;

import com.speryans.saldobus.framework.tools.Log;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;

final class AppPermissionsManager {

	private static final ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();

	private PermissionsRequest pr = new PermissionsRequest();
	
	public AppPermissionsManager() {
		pr.addPermission(ApplicationPermissions.PERMISSION_FILE_API);
		pr.addPermission(ApplicationPermissions.PERMISSION_WIFI);
		pr.addPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION);
		pr.addPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK);
		pr.addPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION);
		pr.addPermission(ApplicationPermissions.PERMISSION_MEDIA);
		pr.addPermission(ApplicationPermissions.PERMISSION_SECURITY_DATA);
	}

	public void permissions() {
		boolean permissionsOk;
		ApplicationPermissions original = apm.getApplicationPermissions();

		ApplicationPermissions permRequest = new ApplicationPermissions();
		
		Vector perms = pr.getPermissions();
		for( int i = 0 ; i < perms.size() ; i++ ) {
			if( original.getPermission( pr.getPermission(i) ) != ApplicationPermissions.VALUE_ALLOW ) {
				permRequest.addPermission( pr.getPermission(i) );
			}
		}
		
		if( permRequest.getPermissionKeys().length == 0 ) {
			permissionsOk = true;
		} else {
			permissionsOk = apm.invokePermissionsRequest(permRequest);
		}

		if (permissionsOk) {
			Log.info("Permissions OK.");
		} else {
			Log.error("Error setting permissions.");
			System.exit(0);
		}
	}
}
