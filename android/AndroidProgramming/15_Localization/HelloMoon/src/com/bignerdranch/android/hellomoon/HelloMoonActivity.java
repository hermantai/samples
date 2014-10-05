package com.bignerdranch.android.hellomoon;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class HelloMoonActivity extends FragmentActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_moon);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_hello_moon, menu);
        return true;
    }
}
