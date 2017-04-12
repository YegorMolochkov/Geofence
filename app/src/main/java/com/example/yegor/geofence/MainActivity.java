package com.example.yegor.geofence;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class MainActivity extends AppCompatActivity implements GeofenceLocationListener.LocationChangeListener {

    private static final long MIN_UPDATE_TIME = 1000;
    private static final float MIN_UPDATE_DISTANCE = 1f;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int PERMISSION_REQUEST = 2;

    @BindView(R.id.activity_main)
    RelativeLayout mainView;
    @BindView(R.id.distance_input)
    EditText distanceInput;
    @BindView(R.id.wifi_input)
    EditText wiFiInput;
    private GeofenceLocationListener mListener;
    private LocationManager mLocationManager;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initInputs();
        setUpReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void initInputs() {
        distanceInput.setText(String.valueOf(PreferencesUtils.getRadius(this)));
        wiFiInput.setText(PreferencesUtils.getDesiredWiFiName(this));
    }

    private void checkWiFI() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                setWiFiName();
            } else {
                PreferencesUtils.setConnected(this, false);
            }
        }
    }

    private void setUpReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info != null && info.isConnected()) {
                    setWiFiName();
                } else {
                    PreferencesUtils.setConnected(context, false);
                }
            }
        };
        registerReceiver(mReceiver, intentFilter);
    }

    private void setWiFiName() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        PreferencesUtils.setConnected(this, true);
        String ssid = wifiInfo.getSSID();
        String name = ssid.substring(1, ssid.length() - 1);
        PreferencesUtils.setWiFiName(this, name);
        if (mListener != null) {
            startToTrackLocation(mListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkWiFI();
        if (!PreferencesUtils.isValuesInitiated(this)) {
            showPicker();
        } else {
            mListener = new GeofenceLocationListener(this, this);
            startToTrackLocation(mListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(mListener);
            } catch (SecurityException e) {
                Log.d(getString(R.string.app_name), e.getLocalizedMessage());
            }
        }
    }

    @OnClick(R.id.pick)
    void showPicker() {
        try {
            showPlacePicker();
        } catch (Exception e) {
            Log.d(getString(R.string.app_name), e.getLocalizedMessage());
        }
    }

    @OnEditorAction(R.id.distance_input)
    boolean onDistanceAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            try {
                int radius = Integer.parseInt(distanceInput.getText().toString());
                PreferencesUtils.setRadius(MainActivity.this, radius);
                startToTrackLocation(mListener);
            } catch (NumberFormatException e) {
                distanceInput.setText(String.valueOf(PreferencesUtils.getRadius(MainActivity.this)));
            }
        }
        return false;
    }

    @OnEditorAction(R.id.wifi_input)
    boolean onWiFIAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            String wifi = wiFiInput.getText().toString();
            PreferencesUtils.setDesiredWiFiName(MainActivity.this, wifi);
            startToTrackLocation(mListener);
        }
        return false;
    }


    private void showPlacePicker() throws Exception {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent = builder.build(this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                LatLng location = place.getLatLng();
                PreferencesUtils.setCenterLatitude(this, location.latitude);
                PreferencesUtils.setCenterLongitude(this, location.longitude);
                mListener = new GeofenceLocationListener(this, this);
                startToTrackLocation(mListener);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startToTrackLocation(mListener);
                } else if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                    finish();
                }
            }
        }
    }

    private void startToTrackLocation(GeofenceLocationListener listener) {
        if (!checkPermission()) {
            requestPermission();
            return;
        }
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location lastLocation = LocationUtils.getLastKnownLocation(mLocationManager);
        if (lastLocation != null) {
            listener.onLocationChanged(lastLocation);
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, listener);
    }

    @Override
    public void onStatusChanged(boolean isInside) {
        int color = isInside ? Color.GREEN : Color.RED;
        mainView.setBackgroundColor(color);
    }

    private boolean checkPermission() {
        boolean fine = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return fine && coarse;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,}, PERMISSION_REQUEST);
    }
}
