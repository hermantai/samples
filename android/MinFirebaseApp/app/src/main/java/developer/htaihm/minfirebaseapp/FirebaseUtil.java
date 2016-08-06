package developer.htaihm.minfirebaseapp;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class FirebaseUtil {
    private static final String TAG = "FirebaseUtil";

    public static void login(final Activity activity, @Nullable final OnAuthCompleteListener listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(TAG, "User has already logged into Firebase: " + userString(user));
            if (listener != null) {
                listener.onCompleteSuccessfully(user);
            }
        } else {
            FirebaseAuth.getInstance().signInAnonymously()
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
                            if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();
                                Log.d(TAG, "Logged in FirebaseUser: " + userString(user));
                                if (listener != null) {
                                    listener.onCompleteSuccessfully(user);
                                }
                            } else {
                                ErrorHandlingUtil.logErrorAndToast(
                                        activity,
                                        TAG,
                                        "Annoymous sign-in failure",
                                        task.getException());
                            }

                        }
                    });
        }
    }

    public static String userString(FirebaseUser user) {
        return String.format(
                "User{uid: %s, provider: %s}",
                user.getUid(),
                user.getProviderId());
    }

    public interface OnAuthCompleteListener {
        void onCompleteSuccessfully(FirebaseUser user);
    }
}
