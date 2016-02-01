package com.gmail.htaihm.locatr;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LocatrFragment extends Fragment {
    private final static String TAG = "LocatrFragment";

    /**
     * I don't have a good way to protect my api key while open sourcing this file, so I resort
     * to store my api key in my emulators or my testing devices. FLICKR_API_KEY is populated in
     * onCreate, through readOrWriteApiKey. I only have to set FLICKR_API_KEY to a literal string
     * once, which allows readOrWriteApiKey to write the api key to my emulator or my testing
     * device, then I can set FLICKR_API_KEY to null in subsequent runs. In subsequent runs, the
     * api key is fetched from the file written in the first run.
     */
    public static String FLICKR_API_KEY = null;

    private ImageView mImageView;
    private GoogleApiClient mClient;

    public static LocatrFragment newInstance() {
        return new LocatrFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readOrWriteApiKey(getActivity());
        setHasOptionsMenu(true);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks(){
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        // do nothing
                    }
                })
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_locatr, container, false);

        mImageView = (ImageView) v.findViewById(R.id.image);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_locatr, menu);

        MenuItem searchItem = menu.findItem(R.id.action_locate);
        searchItem.setEnabled(mClient.isConnected());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                findImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static String readOrWriteApiKey(Context context) {
        File f = new File(context.getFilesDir(), "apikey.txt");
        try {
            if (FLICKR_API_KEY != null) {
                FileUtils.write(f, FLICKR_API_KEY);
            } else {
                if (!f.exists()) {
                    throw new RuntimeException("File " + f.getCanonicalPath() + " does not exist");
                }
                FLICKR_API_KEY = FileUtils.readFileToString(f);
            }
            Log.d(TAG, "Your api key is " + FLICKR_API_KEY);
        } catch (IOException ioe) {
            Log.e(TAG, "IO exception when reading or writing api key", ioe);
        }
        return FLICKR_API_KEY;
    }

    private void findImage() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);


        if (ContextCompat.checkSelfPermission(
                                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(
                    getActivity(),
                    "Does not have permission to get locations!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i(TAG, "Go a fix: " + location);
                        new SearchTask().execute(location);
                    }
                });
    }

    private class SearchTask extends AsyncTask<Location, Void, Void> {
        private GalleryItem mGalleryItem;
        private Bitmap mBitmap;

        @Override
        protected Void doInBackground(Location... params) {
            FlickrFetchr fetchr = new FlickrFetchr();
            List<GalleryItem> items = fetchr.searchPhotos(params[0]);

            if (items.size() == 0) {
                return null;
            }

            mGalleryItem = items.get(0);

            try {
                byte[] bytes = fetchr.getUrlBytes(mGalleryItem.getUrl());
                mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (IOException ioe) {
                Log.e(TAG, "Unable to download bitmap: " + ioe.getLocalizedMessage(), ioe);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mImageView.setImageBitmap(mBitmap);
        }
    }
}
