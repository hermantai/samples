package com.bignerdranch.android.remotecontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;

public class RemoteControlActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new RemoteControlFragment();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }    
}
