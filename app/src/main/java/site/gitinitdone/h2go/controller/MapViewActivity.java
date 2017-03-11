package site.gitinitdone.h2go.controller;

import android.content.DialogInterface;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.GetSourceReportsAPI;
import site.gitinitdone.h2go.model.SourceReport;

import static site.gitinitdone.h2go.R.id.map;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LocalGetSourceReportsAPI getSourceReports;
    private ArrayList<SourceReport> ListOfReports;
    private SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap; //map instance
        mMap.setMaxZoomPreference(14);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setOnMarkerClickListener(this);
        System.out.println("Map Ready is Done.");

        getSourceReports = new LocalGetSourceReportsAPI();
        getSourceReports.execute((Void) null); //maybe this should be getsourcereports.onPostExecute?

        //mMap.moveCamera(CameraUpdateFactory.newLatLng());
//        while (getSourceReports.getStatus() != AsyncTask.Status.FINISHED) {
//            System.out.println("Waiting 1.");
//            try {
//                Thread.sleep(1000);
//                System.out.println("Waiting 2.");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        if (getSourceReports.getStatus() == AsyncTask.Status.FINISHED) {

//        } else {
//
//        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        System.out.println("Reached Marker On Click method.");
        SourceReport sr = ListOfReports.get((int) marker.getTag());

        int reportNum = sr.getReportNumber();
        String date = (new Date(sr.getTimeStamp())).toString();
        String submitter = sr.getReporter();

        // Handles if the direction of latitude is North or South based on negative sign
        String latitude = "";
        if (sr.getLatitude() < 0) {
            latitude = (sr.getLatitude() * -1) + " South";
        } else {
            latitude = sr.getLatitude() + " North";
        }

        // Handles if the direction of longitude is East or West based on negative sign
        String longitude = "";
        if (sr.getLongitude() < 0) {
            longitude = (sr.getLongitude() * -1) + " West";
        } else {
            longitude = sr.getLongitude() + " East";
        }

        String waterType = sr.getWaterType().toString();
        String waterCondition = sr.getWaterCondition().toString();

        // Aggregates all the relevant fields into a nicely formatted string to show on screen
        String reportTitle = "Report #" + reportNum;
        String submitDate = "Submitted On: " + date + "\n";
        String reporter = "Submitted By: " + submitter + "\n";
        String location = "Location: \n \t Latitude: " + latitude + " \n \t Longitude: " + longitude + "\n";
        String waterTypeString = "Water Type: " + waterType + "\n";
        String waterConditionString = "Water Condition: " + waterCondition;

        System.out.println("Reached before alert dialog construction.");

        new AlertDialog.Builder(this)
                .setTitle(reportTitle)
                .setMessage(submitDate + reporter + location + waterTypeString + waterConditionString)
                .setIcon(R.mipmap.appicon)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        System.out.println("Reached before alert dialog construction.");

        return true;
    }

    class LocalGetSourceReportsAPI extends GetSourceReportsAPI {

        public LocalGetSourceReportsAPI() {
            super(getApplicationContext());
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            getSourceReports = null;
            if (success) {
                System.out.println("Get reports is Done.");
                ListOfReports = sourceReportList;
                if (ListOfReports.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No reports are in the system.", Toast.LENGTH_LONG).show();
                } else {
                    //get the array list with the source reports, get the latitude and longitude to put onto the map
                    for (int i = 0; i < ListOfReports.size(); i++) {
                        LatLng currentLocation = new LatLng(ListOfReports.get(i).getLatitude(), ListOfReports.get(i).getLongitude());
                        //MarkerOptions marker = new MarkerOptions().position(currentLocation).title("Report #" + ListOfReports.get(i).getReportNumber());

                        Marker marker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Report #" + ListOfReports.get(i).getReportNumber()));
                        marker.setTag(i);
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "No reports are in the system.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            getSourceReports = null;
        }
    }
}
