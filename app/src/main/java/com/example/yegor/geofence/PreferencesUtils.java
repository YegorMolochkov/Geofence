package com.example.yegor.geofence;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Utils for work with {@link android.content.SharedPreferences}
 */
class PreferencesUtils {

    private static final String LATITUDE_KEY = "LATITUDE_KEY";
    private static final String LONGITUDE_KEY = "LONGITUDE_KEY";
    private static final String RADIUS_KEY = "RADIUS_KEY";
    private static final String WI_FI_NAME = "WI_FI_NAME";
    private static final String WI_FI_DESIRED_NAME = "WI_FI_DESIRED_NAME";
    private static final String WI_FI_STATUS = "WI_FI_STATUS";

    static boolean isValuesInitiated(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains(LATITUDE_KEY) && preferences.contains(LONGITUDE_KEY);
    }

    static void setCenterLatitude(Context context, double latitude) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(LATITUDE_KEY, String.valueOf(latitude)).apply();
    }

    static double getCenterLatitude(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String result = preferences.getString(LATITUDE_KEY, "-1");
        return Double.parseDouble(result);
    }

    static void setCenterLongitude(Context context, double longitude) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(LONGITUDE_KEY, String.valueOf(longitude)).apply();
    }

    static double getCenterLongitude(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String result = preferences.getString(LONGITUDE_KEY, "-1");
        return Double.parseDouble(result);
    }

    static void setRadius(Context context, int radius) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(RADIUS_KEY, radius).apply();
    }

    static int getRadius(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(RADIUS_KEY, 100);
    }

    static void setWiFiName(Context context, String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(WI_FI_NAME, name).apply();
    }

    static String getWiFiName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(WI_FI_NAME, "");
    }

    static void setDesiredWiFiName(Context context, String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(WI_FI_DESIRED_NAME, name).apply();
    }

    static String getDesiredWiFiName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(WI_FI_DESIRED_NAME, "");
    }

    static void setConnected(Context context, boolean connected) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(WI_FI_STATUS, connected).apply();
    }

    static boolean getConnected(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(WI_FI_STATUS, false);
    }
}
