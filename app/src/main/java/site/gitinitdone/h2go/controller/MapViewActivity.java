package site.gitinitdone.h2go.controller;

import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.GetSourceReportsAPI;
import site.gitinitdone.h2go.model.SourceReport;

import static site.gitinitdone.h2go.R.id.map;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback {

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
                        mMap.addMarker(new MarkerOptions().position(currentLocation));
                    }
                }
            }
        }

        @Override
        protected void onCancelled() {
            getSourceReports = null;
        }
    }
}
