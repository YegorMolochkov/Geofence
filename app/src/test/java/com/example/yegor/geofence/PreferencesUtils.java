package com.example.yegor.geofence;

import android.content.Context;

/**
 * Created by Yegor on 12.04.2017.
 */
public class PreferencesUtils {

    private static String sWifiName;
    private static String sDesiredWifiName;
    private static boolean sConnected;

    static void setWiFiName(Context context, String name) {
        sWifiName = name;
    }

    static String getWiFiName(Context context) {
        return sWifiName;
    }

    static void setDesiredWiFiName(Context context, String name) {
        sDesiredWifiName = name;
    }

    static String getDesiredWiFiName(Context context) {
        return sDesiredWifiName;
    }

    static void setConnected(Context context, boolean connected) {
        sConnected = connected;
    }

    static boolean getConnected(Context context) {
        return sConnected;
    }
}
