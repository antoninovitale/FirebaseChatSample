package com.fleetmatics.chat;

import com.firebase.client.Firebase;
import com.fleetmatics.chat.utils.Constants;

/**
 * @author Jenny Tong (mimming)
 * @since 12/5/14
 * <p/>
 * Initialize Firebase with the application context. This must happen before the client is used.
 */
public class ChatApplication extends android.app.Application {
    private static Firebase myFirebaseRef;

    public static Firebase getMyFirebaseRef() {
        return myFirebaseRef;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase(Constants.FIREBASE_URL);
    }

}
