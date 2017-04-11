package com.example.yegor.geofence;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements GeofenceLocationListener.LocationChangeListener {

    private static final long MIN_UPDATE_TIME = 1000;
    private static final float MIN_UPDATE_DISTANCE = 1f;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int PERMISSION_REQUEST = 2;

    @BindView(R.id.activity_main)
    RelativeLayout mainView;
    private GeofenceLocationListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!LocationUtils.isValuesInitiated(this)) {
            showPicker();
        } else {
            mListener = new GeofenceLocationListener(this, this);
            startToTrackLocation(mListener);
        }
    }

    @OnClick(R.id.pick)
    void showPicker() {
        try {
            showPlacePicker();
        } catch (Exception e) {
            showManualInput();
        }
    }

    private void showPlacePicker() throws Exception {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent = builder.build(this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    private void showManualInput() {
        //TODO
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                LatLng location = place.getLatLng();
                LocationUtils.setCenterLatitude(this, location.latitude);
                LocationUtils.setCenterLongitude(this, location.longitude);
                mListener = new GeofenceLocationListener(this, this);
                startToTrackLocation(mListener);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startToTrackLocation(mListener);
                }
            }
        }
    }

    private void startToTrackLocation(GeofenceLocationListener listener) {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!checkPermission()) {
            requestPermission();
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, listener);
    }

    @Override
    public void onStatusChanged(boolean isInside) {
        int color = isInside ? Color.GREEN : Color.RED;
        mainView.setBackgroundColor(color);
    }

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
    }
}
