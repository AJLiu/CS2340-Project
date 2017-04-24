package site.gitinitdone.h2go.model;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by surajmasand on 4/24/17.
 */

public class MyLocationListener implements LocationListener {

    private static double latitude = 0.0;
    private static double longitude = 0.0;


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
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

    public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }
}
