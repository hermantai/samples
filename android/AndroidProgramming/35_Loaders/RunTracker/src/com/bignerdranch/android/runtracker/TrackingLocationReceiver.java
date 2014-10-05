package com.bignerdranch.android.runtracker;

import android.content.Context;
import android.location.Location;

public class TrackingLocationReceiver extends LocationReceiver {
    
    @Override
    protected void onLocationReceived(Context c, Location loc) {
        RunManager.get(c).insertLocation(loc);
    }

}
