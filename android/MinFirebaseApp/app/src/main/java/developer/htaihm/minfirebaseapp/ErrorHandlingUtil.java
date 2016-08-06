package developer.htaihm.minfirebaseapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public final class ErrorHandlingUtil {
    public static void logErrorAndToast(Context context, String tag, String message, Throwable e) {
        logErrorAndToast(context, tag, message, message, e);
    }
    public static void logErrorAndToast(
            Context context,
            String tag,
            String loggingMessage,
            String toastMessage,
            Throwable e) {
        Log.e(tag, loggingMessage, e);
        if (context == null) {
            Log.w(tag, "Context is null, so cannot make a toast");
        } else {
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
        }
    }

    private ErrorHandlingUtil() {}
}
