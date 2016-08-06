package developer.htaihm.minfirebaseapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public final class UiUtil {
    public static void logAndToast(Context context, String tag, String message) {
        logAndToast(context, tag, message, message);
    }

    public static void logAndToast(
            Context context,
            String tag,
            String loggingMessage,
            String toastMessage) {
        Log.i(tag, loggingMessage);
        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
    }

    private UiUtil() {}
}
