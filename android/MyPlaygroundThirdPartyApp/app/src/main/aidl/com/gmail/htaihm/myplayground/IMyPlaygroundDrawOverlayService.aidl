// IMyPlaygroundDrawOverlayService.aidl
package com.gmail.htaihm.myplayground;

// Declare any non-default types here with import statements

interface IMyPlaygroundDrawOverlayService {
    /**
     * Draw an overlay
     */
    void drawOverlay(IBinder windowsToken);
}
