package com.example.yegor.geofence;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Location changes listener
 */
 class GeofenceLocationListener implements LocationListener {

    private LocationChangeListener mListener;
    private double mCenterLatitude;
    private double mCenterLongitude;
    private Boolean mCurrentStatus;

    GeofenceLocationListener(Context context, LocationChangeListener listener) {
        mCenterLatitude = LocationUtils.getCenterLatitude(context);
        mCenterLongitude = LocationUtils.getCenterLongitude(context);
        mListener = listener;
    }

    @Override
    public void onLocationChanged(Location location) {
        float currentDistance = LocationUtils.getDistance(mCenterLatitude, mCenterLongitude, location);
        boolean newStatus = LocationUtils.isInsideArea(currentDistance, 100);
        if (mCurrentStatus == null || newStatus != mCurrentStatus) {
            mCurrentStatus = newStatus;
            mListener.onStatusChanged(newStatus);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    interface LocationChangeListener {

        void onStatusChanged(boolean isInside);
    }
}
