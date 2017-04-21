package site.gitinitdone.h2go.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.SoundEffectConstants;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.GetSourceReportsAPI;
import site.gitinitdone.h2go.model.SoundEffects;
import site.gitinitdone.h2go.model.SourceReport;

import java.util.List;

import static site.gitinitdone.h2go.R.id.add;
import static site.gitinitdone.h2go.R.id.map;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback,
                                                            GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LocalGetSourceReportsAPI getSourceReports;
    private List<SourceReport> ListOfReports;
    private SupportMapFragment mapFragment;
    private static final int MAP_MAX_ZOOM = 14;
    private static final int CAM_MOVE_TIME = 500;  // milliseconds
    private Marker newReportMarker = null;
    private final int requestCode = 123;

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
        mMap.setMaxZoomPreference(MAP_MAX_ZOOM);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setOnMarkerClickListener(this);

        LatLng center = new LatLng(0.0, 0.0);
        newReportMarker = mMap.addMarker(new MarkerOptions().position(center)
                .title("New Report Location")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        newReportMarker.setTag(-1);
        newReportMarker.setVisible(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String latitude = latLng.latitude + "";
                String longitude = latLng.longitude + "";

                newReportMarker.setPosition(latLng);
                newReportMarker.setVisible(true);
                addReportHere(newReportMarker);
            }
        });
        System.out.println("Map Ready is Done.");

        getSourceReports = new LocalGetSourceReportsAPI();
        getSourceReports.execute((Void) null);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        SoundEffects.playClickSound(getApplicationContext());
        System.out.println("Reached Marker On Click method.");

        if ((int) marker.getTag() < 0) {
            addReportHere(marker);
            return true;
        }

        final SourceReport sr = ListOfReports.get((int) marker.getTag());
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), CAM_MOVE_TIME,
                                new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {

                String[] reportStrings = sr.getReportStringFormatted();
                System.out.println("Reached before alert dialog construction.");

                dialog.setTitle(reportStrings[0])
                        .setMessage(reportStrings[1])
                        .setIcon(R.mipmap.appicon)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SoundEffects.playClickSound(getApplicationContext());
                                dialog.dismiss();
                            }
                        })
                        .show();

                System.out.println("Reached after alert dialog construction.");

            }

            @Override
            public void onCancel() {
                // do nothing for now
            }
        });

        return true;
    }


    public void addReportHere(Marker marker) {

        final LatLng position = marker.getPosition();
        final String title = "Add a report here?";
        final String message = "Are you sure you want to \nadd a new source report here? \n";
        final String location = "Location: \n \t Latitude: " + marker.getPosition().latitude
                            + "\n \t Longitude: " + marker.getPosition().longitude;

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), CAM_MOVE_TIME,
                new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {

                        System.out.println("Reached before alert dialog construction.");

                        dialog.setTitle(title)
                                .setMessage(message + location)
                                .setIcon(R.mipmap.appicon)
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SoundEffects.playClickSound(getApplicationContext());
                                        Intent i = new Intent(getBaseContext(), SubmitSourceReportActivity.class);
                                        dialog.dismiss();
                                        i.putExtra("latitude", position.latitude + "");
                                        i.putExtra("longitude", position.longitude + "");
                                        startActivityForResult(i, requestCode);
                                        newReportMarker.setVisible(false);
                                    }})
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SoundEffects.playClickSound(getApplicationContext());
                                        dialog.dismiss();
                                    }
                                })
                                .show();

                        System.out.println("Reached after alert dialog construction.");

                    }

                    @Override
                    public void onCancel() {
                        // do nothing for now
                    }
                });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode) {
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra("Submitted", false)) {
//                    double latitude = Double.parseDouble(data.getStringExtra("Latitude"));
//                    double longitude = Double.parseDouble(data.getStringExtra("Longitude"));
//                    LatLng location = new LatLng(latitude, longitude);
//                    Marker addedReport = mMap.addMarker(new MarkerOptions().position(location)
//                            .title("Report #" + nextReportNumber));
//                    addedReport.setTag(nextReportNumber);
//                    addedReport.setVisible(true);

                    getSourceReports = new LocalGetSourceReportsAPI();
                    getSourceReports.execute((Void) null);
                    Toast.makeText(getApplicationContext(), "Refreshing map...", Toast.LENGTH_LONG);
                }
            }
        }
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
                if (ListOfReports.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.empty_reports_toast,
                            Toast.LENGTH_LONG).show();
                } else {
                    //get the array list with the source reports, get the latitude
                    // and longitude to put onto the map
                    for (int i = 0; i < ListOfReports.size(); i++) {
                        LatLng currentLocation = new LatLng(ListOfReports.get(i).getLatitude(),
                                                    ListOfReports.get(i).getLongitude());

                        Marker marker = mMap.addMarker(new MarkerOptions().position(currentLocation)
                                    .title("Report #" + ListOfReports.get(i).getReportNumber()));
                        marker.setTag(i);
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.empty_reports_toast,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            getSourceReports = null;
        }
    }
}
