package com.speryans.saldobus.framework.classes;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.blackberry.toolkit.ui.component.banner.Banner;
import com.speryans.saldobus.framework.classes.ui.CustomButtonsLayout;
import com.speryans.saldobus.framework.classes.ui.TabBarItem;


public class TabBarActivity extends Activity implements TabBarListener, FieldChangeListener {
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
	private Field previousField = null;
	
	private TabBarItem actual = null;
	
	private CustomButtonsLayout status;
	
	public VerticalFieldManager container = null;
	
	public TabBarActivity( ) {
		super( NO_VERTICAL_SCROLL );
		
		initTabBarActivity();
		
		status = new CustomButtonsLayout( 0, 0x720900 );
		container = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH | VerticalFieldManager.USE_ALL_HEIGHT | Manager.VERTICAL_SCROLL | Manager.NO_VERTICAL_SCROLLBAR );
		
		this.add(status);
		this.add(container);
	}
	
	private void initTabBarActivity() {
		//BatteryStatus batteryStatus = new BatteryStatus();
		//batteryStatus.setDisplaySetting(BatteryStatus.BATTERY_VISIBLE_ALWAYS);
		//titleContainer.add(batteryStatus);
		//titleContainer.add(new WirelessStatus());
		//titleContainer.add(new WirelessStatus(WirelessStatus.DISPLAY_DESCRIPTOR, false));
		//titleContainer.add(new WirelessStatus(WirelessStatus.DISPLAY_SIGNAL, true));
		//titleContainer.add(new WirelessStatus(WirelessStatus.DISPLAY_WIFI, true));
		//titleContainer.add(new Notifications());
		//titleContainer.add(new TimeDisplay());
		//add(titleContainer);
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
	
	public CustomButtonsLayout getTabBar() {
		return status;
	}
	
	public void addTabBarItem( Field item ) {
		if( item.getClass().equals( TabBarItem.class ) ) {
			item.setChangeListener(this);
			status.add(item);
			
			actual = (TabBarItem) item;
			firstView = null;
		}
	}
	
	public void removeTabBarItem( Field item ) {
		status.delete(item);
	}
	
	public void deleteAllTabBarItems() {
		status.deleteAll();
	}
	
	public boolean isTabBarItem( Field item ) {
		try {
			for( int i = 0 ; i <= (status.getFieldCount() - 1) ; i++ ) {
				if( item.equals( status.getField(i) ) ) {
					return true;
				}
			}
		} catch( Exception e ) {}
		return false;
	}
	
	public TabBarItem getActual() {
		return actual;
	}
	
	public int getActualIndex( ) {
		int temp = 0;
		for( int i = 0 ; i <= (status.getFieldCount() - 1) ; i++ ) {
			if( actual.equals( status.getField(i) ) ) {
				return temp;
			}
			temp++;
		}
		return -1;
	}
	
	public boolean navigationMovement(int dx, int dy, int status, int time) {
		previousField = this.getLeafFieldWithFocus();
		
		boolean result = super.navigationMovement(dx, dy, status, time);
		// TabBar
		
		//Commons.showGlobalMessage( dx + " - " + dy );
		if (dx == 0 && dy == 1) { // DOWN
			try {
				if( isTabBarItem( previousField ) ) {
					synchronized (UiApplication.getEventLock()) {
						if( container.getFieldCount() > 0 ) {
							container.getField(0).setFocus();
						}
					}
				}
			} catch (Exception e) { }
		}
		
		return result;
	}

	public void onTabBarItemChanged(Field field, int context) {
	}

	public void fieldChanged(Field field, int context) {
		if( field instanceof TabBarItem ) { 
			if( !actual.equals( field ) ) {
				actual = (TabBarItem) field;
				firstView = null;
			}
			this.onTabBarItemChanged(field, context);
			return;
		}
	}
}