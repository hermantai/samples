package com.gmail.htaihm.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler mRequestHandler;
    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();
    private Handler mResponseHandler;
    private ThumbnailDownloadListener<T> mThumbnailDownloadListener;
    private RealThumbnailDownloader mRealThumbnailDownloader;

    public interface ThumbnailDownloadListener<T> {
        void onThumbnailDownloaded(T target, Bitmap thumbnail);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener) {
        mThumbnailDownloadListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
        mRealThumbnailDownloader = new RealThumbnailDownloader();
        mRealThumbnailDownloader.start();
        mRealThumbnailDownloader.getLooper();
    }

    public void queueThumbnail(T target, String url) {
        Log.i(TAG, "Got a URL: " + url);

        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
                    // This actually sends to the Handler, which enqueues the message into its
                    // Looper
                    .sendToTarget();
        }
    }

    public void preloadThumbnail(String url) {
        mRealThumbnailDownloader.queuePrefetch(url);
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
        mRealThumbnailDownloader.clearDownloadQueue();
        mRealThumbnailDownloader.clearPrefetchQueue();
    }

    public void clearPreloadQueue() {
        mRealThumbnailDownloader.clearPrefetchQueue();
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got a request for URL: " + mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    private void handleRequest(final T target) {
        final String url = mRequestMap.get(target);

        if (url == null) {
            return;
        }

        mRealThumbnailDownloader.queueDownload(url);
        final Bitmap bitmap = mRealThumbnailDownloader.waitForDownloadFinished(url);
        Log.i(TAG, String.format("Bitmap for %s created", url));

        mResponseHandler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if (mRequestMap.get(target) != url) {
                            return;
                        }
                        mRequestMap.remove(target);
                        if (mThumbnailDownloadListener != null) {
                            mThumbnailDownloadListener.onThumbnailDownloaded(target, bitmap);
                        }
                    }
                }
        );
    }

    /**
     * RealThumbnailDownloader avoids the downloading of a photo multiple times by using a LRU
     * cache and a map to make sure no concurrent downloading of the same photo.
     *
     * The usual pattern of using this class is calling queueDownload to asynchronously download an
     * image. Then you can use waitForDownloadFinished to wait until the download finishes.
     *
     * This class has a known issue that calling waitForDownloadFinished may return null for the
     * following scenarios:
     *
     *   1) You wait too long to call waitForDownloadFinished after calling queueDownload, so
     *      the cache is evicted.
     *   2) There is an error when downloading the photo.
     */
    private static class RealThumbnailDownloader extends HandlerThread {
        private static final int MESSAGE_REAL_DOWNLOAD = 0;
        private static final int MESSAGE_PREFETCH_DOWNLOAD = 1;

        final Lock downloadingLock = new ReentrantLock();
        final Condition notDownloading = downloadingLock.newCondition();

        private Set<String> mDownloadingUrls = Collections.newSetFromMap(
                new ConcurrentHashMap<String, Boolean>());
        private Handler mDownloadRequestHandler;
        private LruCache<String, Bitmap> mPhotoCache = new LruCache<>(1000);

        public RealThumbnailDownloader() {
            super("RealThumbnailDownloader");
        }

        public void queueDownload(String url) {
            queueDownload(url, MESSAGE_REAL_DOWNLOAD);
        }

        public void queuePrefetch(String url) {
            queueDownload(url, MESSAGE_PREFETCH_DOWNLOAD);
        }

        /**
         * Queues up the download of a Bitmap for a given {@code url}. If it is in the cache,
         * no download will be done.
         */
        private void queueDownload(String url, int messageType) {
            if (mPhotoCache.get(url) != null || mDownloadingUrls.contains(url)) {
                return;
            }
            mDownloadingUrls.add(url);
            mDownloadRequestHandler.obtainMessage(messageType, url).sendToTarget();
        }

        /**
         * Waits until the download of the given {@code url} is finished. This method does not
         * initiate a download, so this should be usually done after queueDownload is called.
         */
        public Bitmap waitForDownloadFinished(String url) {
            while (mDownloadingUrls.contains(url)) {
                try {
                    downloadingLock.lock();
                    notDownloading.await();
                } catch (InterruptedException e) {
                    break;
                } finally {
                    downloadingLock.unlock();
                }
            }
            return mPhotoCache.get(url);
        }

        @Override
        protected void onLooperPrepared() {
            mDownloadRequestHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String url = (String) msg.obj;
                    try {
                        byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
                        mPhotoCache.put(
                                url,
                                BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length));
                        mDownloadingUrls.remove(url);
                        try {
                            downloadingLock.lock();
                            notDownloading.signalAll();
                        } finally {
                            downloadingLock.unlock();
                        }
                    } catch (IOException ioe) {
                        Log.e(
                                TAG,
                                String.format(
                                  "Error downloading image %s: %s",
                                  url,
                                  ioe.getLocalizedMessage()),
                                ioe);

                    }
                }
            };
        }

        public void clearDownloadQueue() {
            mDownloadRequestHandler.removeMessages(MESSAGE_REAL_DOWNLOAD);
        }
        public void clearPrefetchQueue() {
            mDownloadRequestHandler.removeMessages(MESSAGE_PREFETCH_DOWNLOAD);
        }
    }
}
