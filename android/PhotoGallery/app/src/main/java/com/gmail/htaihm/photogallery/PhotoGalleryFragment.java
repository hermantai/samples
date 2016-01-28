package com.gmail.htaihm.photogallery;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();

    /**
     * I don't have a good way to protect my api key while open sourcing this file, so I resort
     * to store my api key in my emulators or my testing devices. FLICKR_API_KEY is populated in
     * onCreate, through readOrWriteApiKey. I only have to set FLICKR_API_KEY to a literal string
     * once, which allows readOrWriteApiKey to write the api key to my emulator or my testing
     * device, then I can set FLICKR_API_KEY to null in subsequent runs. In subsequent runs, the
     * api key is fetched from the file written in the first run.
     */
    public static String FLICKR_API_KEY = null;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        readOrWriteApiKey();
        new FetchItemsTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mPhotoRecyclerView = (RecyclerView) view.findViewById(
                R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        mPhotoRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        DisplayMetrics metrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        float widthInches = metrics.widthPixels / metrics.xdpi;
                        Log.i(TAG, "Number of inches in width: " + widthInches);
                        int columns = (int) (widthInches * 1.5);

                        ((GridLayoutManager) mPhotoRecyclerView.getLayoutManager())
                                .setSpanCount(columns);
                    }
                });
        setupAdapter();

        return view;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }

    private void readOrWriteApiKey() {
        Context context = getActivity();
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
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            return new FlickrFetchr().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            setupAdapter();
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }

        public void bindGalleryItem(GalleryItem item){
            mTitleTextView.setText(item.toString());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(
                    android.R.layout.simple_list_item_1, parent, false);

            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem item = mGalleryItems.get(position);
            holder.bindGalleryItem(item);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }
}
