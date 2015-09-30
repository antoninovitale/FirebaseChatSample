package com.fleetmatics.chat.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by antoninovitale 17/07/15.
 * Copyright © 2015. Fleetmatics Development Limited. All rights reserved.
 **/
public class Utils {

    public static void cleanup(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFS, 0);
        prefs.edit().clear().apply();
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFS, 0);
        return prefs.getString(Constants.USERNAME, null);
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFS, 0);
        return prefs.getString(Constants.USER_ID, null);
    }

    public static void saveValueToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setupUser(Context context, String username, String userId) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USERNAME, username);
        editor.putString(Constants.USER_ID, userId);
        editor.apply();
    }
}
