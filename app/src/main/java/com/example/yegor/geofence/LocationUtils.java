package com.example.yegor.geofence;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;

import java.util.List;

/**
 * Utils for location calculations
 */
class LocationUtils {

    private static final String LATITUDE_KEY = "LATITUDE_KEY";
    private static final String LONGITUDE_KEY = "LONGITUDE_KEY";
    private static final String RADIUS_KEY = "RADIUS_KEY";

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

    /**
     * @param locationManager location manager
     * @return best of last known locations
     * @throws SecurityException
     */
    static Location getLastKnownLocation(LocationManager locationManager) throws SecurityException {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
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

    static void setRadius(Context context, int radius) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(RADIUS_KEY, radius).apply();
    }

    static int getRadius(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(RADIUS_KEY, 100);
    }
}
