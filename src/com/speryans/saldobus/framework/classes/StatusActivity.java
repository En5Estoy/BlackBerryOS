package com.speryans.saldobus.framework.classes;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.blackberry.toolkit.ui.component.banner.Banner;


public class StatusActivity extends Activity {
	// Title bar things
	private final Banner banner = new Banner();
	private final HorizontalFieldManager titleContainer = new HorizontalFieldManager( USE_ALL_WIDTH ) {
		protected void paint(Graphics graphics) {
			graphics.setBackgroundColor(Color.BLACK);
			graphics.clear();
			super.paint(graphics);
		};
	};
	
	public Field firstView = null;
	
	public VerticalFieldManager container = null;
	
	public StatusActivity( ) {
		super( NO_VERTICAL_SCROLL );
		
		initTabBarActivity();
		
		container = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH | VerticalFieldManager.USE_ALL_HEIGHT | Manager.VERTICAL_SCROLL | Manager.NO_VERTICAL_SCROLLBAR );
		
		this.add(container);
	}
	
	private void initTabBarActivity() {
		banner.addTitle("");
		banner.addSignalIndicator();
		banner.addClock();
		banner.addNotifications();

		setTitle(banner);
	}
	
	public void cleanContainer() {
		synchronized (Application.getEventLock()) {
			container.deleteAll();
		}
	}
	
	public void setTitle( String title ) {
		banner.removeTitle();
		banner.addTitle(title);
	}

	public void changeIcon( Bitmap icon ) {
		banner.removeIcon();
		banner.addIcon(icon);
	}
	
	public Banner getBanner() {
		return banner;
	}
	
	/*public boolean navigationMovement(int dx, int dy, int status, int time) {
		previousField = this.getLeafFieldWithFocus();
		
		boolean result = super.navigationMovement(dx, dy, status, time);
		// TabBar
		
		if( firstView == null ) {
			return result;
		}
		
		 // Cada vez que el usuario se mueve se guarda el elemento anterior y tenemos un elemento llamado first.
		 // Si el elemento anterior es un item de la tabbar y el nuevo no entonces se le da el foco a first.
		 // Obvio si no es nulo.
		  
		if (dx == 0 && dy == -1) { // UP
			try {
				//Commons.showGlobalMessage( previousField.getClass().getName() + " --> " + this.getLeafFieldWithFocus().getClass().getName() );
				if( isTabBarItem( previousField ) && !isTabBarItem( this.getFieldWithFocus() ) ) {
					synchronized (UiApplication.getEventLock()) {
						firstView.setFocus();
					}
				}
			} catch (Exception e) {
				//Commons.showGlobalMessage( e.toString() );
			}
		} else if (dy == 0 && dx == -1) { // LEFT
			try {
				//Commons.showGlobalMessage( previousField.getClass().getName() + " --> " + this.getLeafFieldWithFocus().getClass().getName() + "( " + (isTabBarItem( this.getLeafFieldWithFocus() ) ? "true" : "false")  + " )" );
				if( isTabBarItem( previousField ) == true && isTabBarItem( this.getLeafFieldWithFocus() ) == false ) {
					//Commons.showGlobalMessage("Going to first");
					UiApplication.getUiApplication().invokeLater(new Runnable() {

						public void run() {
							// TODO Auto-generated method stub
							firstView.setFocus();
						}
						
					});
					//synchronized (Application.getEventLock()) {
						//firstView.setFocus();
					//}
				}
			} catch (Exception e) {
				//Commons.showGlobalMessage( e.toString() );
			}
		}
		
		return result;
	}*/
}