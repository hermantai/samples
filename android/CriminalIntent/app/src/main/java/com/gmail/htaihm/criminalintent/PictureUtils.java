package com.gmail.htaihm.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;

import java.io.IOException;

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, Activity activity) throws IOException {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight)
            throws IOException {
        // Read in the dimensions of the image on disk.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // Figure out how much to scale down by
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // Read in and create final bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        // Fix the rotation
        ExifInterface exif = new ExifInterface(path);
        String orientationString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientationTag = orientationString != null ? Integer.parseInt(orientationString) :
                ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;

        switch (orientationTag) {
            case ExifInterface.ORIENTATION_ROTATE_90 :
                rotationAngle = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180 :
                rotationAngle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270 :
                rotationAngle = 270;
                break;
            default:
                rotationAngle = 0;

        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle);
        bitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return bitmap;
    }
}
