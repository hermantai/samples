// IMyPlaygroundDrawOverlayService.aidl
package com.gmail.htaihm.myplayground;

// Declare any non-default types here with import statements

interface IMyPlaygroundDrawOverlayService {
    /**
     * Draw an overlay
     */
    void drawOverlay(IBinder windowsToken);

     /**
     * Draw an overlay within the application.
     */
    void drawWithinApplication(IBinder windowsToken);

    /**
     * Draw an overlay within the application but with a delay
     */
    void drawWithinApplicationDelayed(IBinder windowsToken);

    /**
     * Crash the host app.
     */
    void crashHostApp();
}
