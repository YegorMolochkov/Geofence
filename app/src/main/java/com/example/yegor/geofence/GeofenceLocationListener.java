package com.example.yegor.geofence;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Location changes listener
 */
class GeofenceLocationListener implements LocationListener {

    private Context mContext;
    private LocationChangeListener mListener;
    private double mCenterLatitude;
    private double mCenterLongitude;
    private Boolean mCurrentStatus;
    private AlertDialog dialog;

    GeofenceLocationListener(Context context, LocationChangeListener listener) {
        mContext = context;
        mCenterLatitude = PreferencesUtils.getCenterLatitude(context);
        mCenterLongitude = PreferencesUtils.getCenterLongitude(context);
        mListener = listener;
    }

    @Override
    public void onLocationChanged(Location location) {
        float currentDistance = LocationUtils.getDistance(mCenterLatitude, mCenterLongitude, location);
        boolean newStatus = LocationUtils.isInsideArea(mContext, currentDistance, PreferencesUtils.getRadius(mContext));
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
        if (dialog != null && dialog.isShowing()) {
            dialog.hide();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("To continue, please turn on location!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(viewIntent);
                    }
                })
                .setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    interface LocationChangeListener {

        void onStatusChanged(boolean isInside);
    }
}
