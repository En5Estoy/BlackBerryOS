package com.speryans.saldobus;

import net.rim.device.api.ui.UiApplication;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class App extends UiApplication
{
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
    	new AppPermissionsManager().permissions();
        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.
        App theApp = new App();       
        theApp.enterEventDispatcher();
    }
    

    /**
     * Creates a new App object
     */
    public App()
    {        
        // Push a screen onto the UI stack for rendering.
        pushScreen(new Main());
    }    
}
