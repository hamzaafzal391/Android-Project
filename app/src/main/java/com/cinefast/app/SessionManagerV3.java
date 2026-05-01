package com.cinefast.app;

import android.content.Context;
import android.content.SharedPreferences;

public final class SessionManagerV3 {
    private SessionManagerV3() {}

    public static final String PREFS_NAME = "cinefast_session_pref_v3";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_UID = "uid";
    private static final String KEY_EMAIL = "email";

    public static boolean isLoggedIn(Context context) {
        return prefs(context).getBoolean(KEY_LOGGED_IN, false);
    }

    public static void setLoggedIn(Context context, String uid, String email) {
        prefs(context).edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_UID, uid)
                .putString(KEY_EMAIL, email)
                .apply();
    }

    public static void clear(Context context) {
        prefs(context).edit().clear().apply();
    }

    public static String getUid(Context context) {
        return prefs(context).getString(KEY_UID, null);
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}

