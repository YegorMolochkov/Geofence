package com.example.yegor.geofence;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

/**
 * Utils for location calculations
 */
class LocationUtils {

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
    static boolean isInsideArea(Context context, float distance, float radius) {
        boolean isInside = Float.compare(radius - distance, 0) >= 0;
        boolean isWifi = PreferencesUtils.getConnected(context) && PreferencesUtils.getWiFiName(context).equals(PreferencesUtils.getDesiredWiFiName(context));
        return isInside || isWifi;
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
}
