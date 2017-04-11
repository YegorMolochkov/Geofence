package com.example.yegor.geofence;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

/**
 * Utils for location calculations
 */
class LocationUtils {

    private static final String LATITUDE_KEY = "LATITUDE_KEY";
    private static final String LONGITUDE_KEY = "LONGITUDE_KEY";

    /**
     * calculates distance from current location to center of area
     *
     * @param centerLn        center longitude
     * @param centerLt        center latitude
     * @param currentLocation current location
     * @return distance in meters
     */
    static float getDistance(double centerLt, double centerLn, Location currentLocation) {
        Location centerLocation = new Location(currentLocation.getProvider());
        centerLocation.setLatitude(centerLt);
        centerLocation.setLongitude(centerLn);
        return currentLocation.distanceTo(centerLocation);
    }

    /**
     * @param distance distance from center
     * @param radius   radius of area
     * @return true if in the area, false otherwise
     */
    static boolean isInsideArea(float distance, float radius) {
        return Float.compare(radius - distance, 0) >= 0;
    }

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
}
