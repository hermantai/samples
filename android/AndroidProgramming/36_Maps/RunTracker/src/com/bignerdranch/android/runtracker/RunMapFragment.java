package com.bignerdranch.android.runtracker;

import java.util.Date;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.runtracker.RunDatabaseHelper.LocationCursor;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class RunMapFragment extends SupportMapFragment implements LoaderCallbacks<Cursor> {
    private static final String ARG_RUN_ID = "RUN_ID";
    private static final int LOAD_LOCATIONS = 0;
    
    private GoogleMap mGoogleMap;
    private LocationCursor mLocationCursor;

    public static RunMapFragment newInstance(long runId) {
        Bundle args = new Bundle();
        args.putLong(ARG_RUN_ID, runId);
        RunMapFragment rf = new RunMapFragment();
        rf.setArguments(args);
        return rf;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check for a Run ID as an argument, and find the run
        Bundle args = getArguments();
        if (args != null) {
            long runId = args.getLong(ARG_RUN_ID, -1);
            if (runId != -1) {
                LoaderManager lm = getLoaderManager();
                lm.initLoader(LOAD_LOCATIONS, args, this);
            }
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        
        // stash a reference to the GoogleMap
        mGoogleMap = getMap();
        // show the user's location
        mGoogleMap.setMyLocationEnabled(true);
        
        return v;
    }

    private void updateUI() {
        if (mGoogleMap == null || mLocationCursor == null)
            return;
        
        // set up an overlay on the map for this run's locations
        // create a polyline with all of the points
        PolylineOptions line = new PolylineOptions();
        // also create a LatLngBounds so we can zoom to fit
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        // iterate over the locations
        mLocationCursor.moveToFirst();
        while (!mLocationCursor.isAfterLast()) {
            Location loc = mLocationCursor.getLocation();
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            
            // if this is the first location, add a marker for it
            if (mLocationCursor.isFirst()) {
                String startDate = new Date(loc.getTime()).toString();
                MarkerOptions startMarkerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(getResources().getString(R.string.run_start))
                    .snippet(getResources().getString(R.string.run_started_at_format, startDate));
                mGoogleMap.addMarker(startMarkerOptions);
            } else if (mLocationCursor.isLast()) {
                // if this is the last location, and not also the first, add a marker
                String endDate = new Date(loc.getTime()).toString();
                MarkerOptions finishMarkerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(getResources().getString(R.string.run_finish))
                    .snippet(getResources().getString(R.string.run_finished_at_format, endDate));
                mGoogleMap.addMarker(finishMarkerOptions);
            }
            
            line.add(latLng);
            latLngBuilder.include(latLng);
            mLocationCursor.moveToNext();
        }
        // add the polyline to the map
        mGoogleMap.addPolyline(line);
        // make the map zoom to show the track, with some padding
        // use the size of the current display in pixels as a bounding box
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        // construct a movement instruction for the map camera
        CameraUpdate movement = CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(),
                display.getWidth(), display.getHeight(), 15);
        mGoogleMap.moveCamera(movement);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new LocationListCursorLoader(getActivity(), args.getLong(ARG_RUN_ID, -1));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mLocationCursor = (LocationCursor)cursor;
        updateUI();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // stop using the data
        mLocationCursor.close();
        mLocationCursor = null;
    }
    
}
