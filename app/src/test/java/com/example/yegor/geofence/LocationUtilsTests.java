package com.example.yegor.geofence;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

/**
 * tests for {@link LocationUtils}
 */
public class LocationUtilsTests {

    @Mock
    Context context;

    @Test
    public void isInsideAreaTest() throws Exception {

        float smaller = 10;
        float bigger = 100;
        String wifi = "wifi";
        String desiredWifi = "desiredWifi";

        PreferencesUtils.setConnected(context, false);
        PreferencesUtils.setWiFiName(context, wifi);
        PreferencesUtils.setDesiredWiFiName(context, desiredWifi);

        Assert.assertTrue(LocationUtils.isInsideArea(context, smaller, bigger));
        Assert.assertFalse(LocationUtils.isInsideArea(context, bigger, smaller));
        Assert.assertTrue(LocationUtils.isInsideArea(context, bigger, bigger));

        PreferencesUtils.setConnected(context, true);

        Assert.assertTrue(LocationUtils.isInsideArea(context, smaller, bigger));
        Assert.assertFalse(LocationUtils.isInsideArea(context, bigger, smaller));
        Assert.assertTrue(LocationUtils.isInsideArea(context, bigger, bigger));

        PreferencesUtils.setDesiredWiFiName(context, wifi);

        Assert.assertTrue(LocationUtils.isInsideArea(context, smaller, bigger));
        Assert.assertTrue(LocationUtils.isInsideArea(context, bigger, smaller));
        Assert.assertTrue(LocationUtils.isInsideArea(context, bigger, bigger));
    }
}