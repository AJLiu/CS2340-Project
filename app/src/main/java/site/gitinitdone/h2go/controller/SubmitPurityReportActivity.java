package site.gitinitdone.h2go.controller;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.PurityReport;
import site.gitinitdone.h2go.model.SoundEffects;
import site.gitinitdone.h2go.model.SubmitPurityReportAPI;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * This activity allows the user to submit water purity reports
 */
public class SubmitPurityReportActivity extends AppCompatActivity {

    private LocalPurityReportAPI submitPurityReportAPI = null;
    private View submitPurityForm;
    private View mProgressView;
    private static final int MAX_LAT = 90;
    private static final int MAX_LONG = 180;
    private final int MY_PERMISSIONS_REQUEST_FINE_ACCESS = 1000;
    private final int MY_PERMISSIONS_REQUEST_COARSE_ACCESS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_purity_report);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_submit_purity_report_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Setup the Spinner to show the Water Conditions
        Spinner waterConditionSpinner = (Spinner) findViewById(R.id.overallConditionSpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                PurityReport.OverallCondition.values());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterConditionSpinner.setAdapter(adapter2);
        waterConditionSpinner.setSelection(0);

        // formatting the latitude and longitude to show (max) 6 decimal places
        final EditText latitudeField = (EditText) findViewById(R.id.locationLat);
        latitudeField.setText("0.0");
        latitudeField.setText(formatLatitude(latitudeField.getText().toString()));

        latitudeField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                latitudeField.setText(formatLatitude(latitudeField.getText().toString()));
            }
        });

        final EditText longitudeField = (EditText) findViewById(R.id.locationLong);
        longitudeField.setText("0.0");
        longitudeField.setText(formatLongitude(longitudeField.getText().toString()));

        longitudeField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                longitudeField.setText(formatLongitude(longitudeField.getText().toString()));
            }
        });

        final EditText virusField = (EditText) findViewById(R.id.virusPPMNumber);
        virusField.setText("0");
        virusField.setText(formatPPM(virusField.getText().toString()));

        virusField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                virusField.setText(formatPPM(virusField.getText().toString()));
            }
        });

        final EditText contaminantField = (EditText) findViewById(R.id.contaminantPPMNumber);
        contaminantField.setText("0");
        contaminantField.setText(formatPPM(contaminantField.getText().toString()));

        contaminantField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                contaminantField.setText(formatPPM(contaminantField.getText().toString()));
            }
        });

        Button b = (Button) findViewById(R.id.submitButtonWaterPurityReport);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playClickSound(v);
                submitPurityReport(v);
            }
        });

        b = (Button) findViewById(R.id.useCurrentLocationPurityReport);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playClickSound(v);
                useCurrentLocation();
            }
        });

    }

    public void submitPurityReport(View view) {
        // Validating the entries in report
        double latitude;
        double longitude;
        String waterCondition;
        String virusPPM;
        String contaminantPPM;

        EditText latitudeField = (EditText) findViewById(R.id.locationLat);
        EditText longitudeField = (EditText) findViewById(R.id.locationLong);
        EditText virusField = (EditText) findViewById(R.id.virusPPMNumber);
        EditText contaminantField = (EditText) findViewById(R.id.contaminantPPMNumber);
        virusField.setText(formatPPM(virusField.getText().toString()));
        contaminantField.setText(formatPPM(contaminantField.getText().toString()));

        try {
            latitudeField.setText(formatLatitude(latitudeField.getText().toString()));
            latitude = Double.parseDouble(latitudeField.getText().toString());
            if ((latitude > MAX_LAT) || (latitude < (-1 * MAX_LAT))) {
                showErrorOnField(latitudeField, getString(R.string.Latitude_range_validation));
                return;
            }
            // if it reaches here, no errors for latitude

        } catch (NumberFormatException e) {
            showErrorOnField(latitudeField, getString(R.string.Latitude_number_validation));
            return;
        }

        try {
            longitudeField.setText(formatLongitude(longitudeField.getText().toString()));
            longitude = Double.parseDouble(longitudeField.getText().toString());
            if ((longitude > MAX_LONG) || (longitude < (-1 * MAX_LONG))) {
                showErrorOnField(longitudeField, getString(R.string.Longitude_range_validation));
                return;
            }
            // if it reaches here, no errors for longitude
        } catch (NumberFormatException e) {
            showErrorOnField(latitudeField, getString(R.string.Longitude_number_validation));
            return;
        }

        Spinner waterConditionSpinner = (Spinner) findViewById(R.id.overallConditionSpinner);
        waterCondition = waterConditionSpinner.getSelectedItem().toString();

//        EditText virus = (EditText) findViewById(R.id.virusPPMNumber);
        virusPPM = virusField.getText().toString();

//        EditText contaminant = (EditText) findViewById(R.id.contaminantPPMNumber);
        contaminantPPM = contaminantField.getText().toString();

        Map<String, String> data = new HashMap<>();
        data.put("lat", String.valueOf(latitude));
        data.put("long", String.valueOf(longitude));
        data.put("waterCondition", waterCondition);
        data.put("virusPPM", virusPPM);
        data.put("contaminantPPM", contaminantPPM);

        submitPurityForm = findViewById(R.id.content_submit_purity_report);
        mProgressView = findViewById(R.id.submit_purity_report_progress);

        submitPurityReportAPI = new LocalPurityReportAPI(data);
        submitPurityReportAPI.execute((Void) null);
        showProgress(true);
    }

    /**
     * Sets the error message for the field on the edit form and focuses that field on the screen
     *
     * @param field the field that has the error
     * @param message the message to show on the field
     */
    private void showErrorOnField(EditText field, String message) {
        field.setError(message);
        field.requestFocus();
    }

    private String formatLongitude(String oldText) {
        DecimalFormat formatLong = new DecimalFormat("##0.000000");
        if (oldText.trim().isEmpty()) {
            oldText = "0";
        }
        return formatLong.format(Double.parseDouble(oldText.trim()));
    }

    private String formatLatitude(String oldText) {
        DecimalFormat formatLat = new DecimalFormat("#0.000000");
        if (oldText.trim().isEmpty()) {
            oldText = "0";
        }
        return formatLat.format(Double.parseDouble(oldText.trim()));
    }

    private String formatLongitude(double oldValue) {
        return formatLongitude(oldValue + "");
    }

    private String formatLatitude(double oldValue) {
        return formatLatitude(oldValue + "");
    }

    private String formatPPM(String oldText) {
        DecimalFormat formatPPMNumber = new DecimalFormat("#0");
        if (oldText.trim().isEmpty()) {
            oldText = "0";
        }
        return formatPPMNumber.format(Double.parseDouble(oldText.trim()));
    }

    private void useCurrentLocation() {
        System.out.println("Called UseCurrentLocation");
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {

            System.out.println("Does not have permission, will ask.");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_ACCESS);

        } else {

            System.out.println("Trying to get current location with permission.");
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                System.out.println("Got the location.");
                updateLocationFields(latitude, longitude);
            } else {
                System.out.println("Location is null");
            }
//        private final LocationListener locationListener = new LocationListener() {
//            public void onLocationChanged(Location location) {
//                longitude = location.getLongitude();
//                latitude = location.getLatitude();
//            }
//        }
            //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_ACCESS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        updateLocationFields(latitude, longitude);
                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_COARSE_ACCESS);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_COARSE_ACCESS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        updateLocationFields(latitude, longitude);
                    }

                } else {

                    Toast.makeText(this, "Location Permission Denied. Cannot use current location.", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void updateLocationFields(double latitude, double longitude) {
        EditText latitudeField = (EditText) findViewById(R.id.locationLat);
        EditText longitudeField = (EditText) findViewById(R.id.locationLong);

        latitudeField.setText(formatLatitude(latitude));
        longitudeField.setText(formatLongitude(longitude));
    }

    /**
     * Shows the progress UI and hides the data until it's been populated.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        submitPurityForm.setVisibility(show ? View.GONE : View.VISIBLE);
        submitPurityForm.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                submitPurityForm.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous API to submit a purity report.
     */
    class LocalPurityReportAPI extends SubmitPurityReportAPI {

        LocalPurityReportAPI(Map<String, String> data) {
            super(data, getApplicationContext());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            submitPurityReportAPI = null;
            showProgress(false);

            if (success) {
                System.out.println("Submitted Purity Report TRUE");
                Toast.makeText(getBaseContext(), R.string.purity_report_success,
                        Toast.LENGTH_LONG).show();
                finish();
            } else {
                System.out.println("Submitted Purity Report FALSE");
                Toast.makeText(getBaseContext(), R.string.purity_report_error,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            submitPurityReportAPI = null;
            showProgress(false);
        }

    }

}
