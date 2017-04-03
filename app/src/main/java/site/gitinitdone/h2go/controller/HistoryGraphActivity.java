package site.gitinitdone.h2go.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.GetPurityReportsAPI;
import site.gitinitdone.h2go.model.HistoricalReportCalc;

public class HistoryGraphActivity extends AppCompatActivity {

    private HistoricalReportCalc reportCalc;
    LocalGetPurityReportsAPI localPurityReportsAPI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setting default year value
        final EditText year = (EditText) findViewById(R.id.historyGraphViewYearEntered);
        String currYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        year.setText(currYear);

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

    public void histGraphFilter(View view) {
        // Validating the entries in report
        double latitude;
        double longitude;
        int year;

        EditText latitudeField = (EditText) findViewById(R.id.locationLat);
        EditText longitudeField = (EditText) findViewById(R.id.locationLong);
        EditText yearField = (EditText) findViewById(R.id.historyGraphViewYearEntered);
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        RadioButton virusType = (RadioButton) findViewById(R.id.historyGraphViewVirus);
        RadioButton contamType = (RadioButton) findViewById(R.id.historyGraphViewContaminant);

        try {
            if (latitudeField.getText().toString().isEmpty()) {
                showErrorOnField(latitudeField, "Latitude can't be empty");
                return;
            }
            latitudeField.setText(formatLatitude(latitudeField.getText().toString()));
            latitude = Double.parseDouble(latitudeField.getText().toString());
            if (latitude > 90 || latitude < -90) {
                showErrorOnField(latitudeField, "Latitude must in between -90 and 90 degrees");
                return;
            }
            // if it reaches here, no errors for latitude

        } catch (NumberFormatException e) {
            showErrorOnField(latitudeField, "Latitude is not a valid number.");
            return;
        }

        try {
            if (longitudeField.getText().toString().isEmpty()) {
                showErrorOnField(longitudeField, "Longitude can't be empty");
                return;
            }
            longitudeField.setText(formatLongitude(longitudeField.getText().toString()));
            longitude = Double.parseDouble(longitudeField.getText().toString());
            if (longitude > 180 || longitude < -180) {
                showErrorOnField(longitudeField, "Longitude must in between -180 and 180 degrees");
                return;
            }
            // if it reaches here, no errors for longitude
        } catch (NumberFormatException e) {
            showErrorOnField(latitudeField, "Longitude is not a valid number.");
            return;
        }

        try {
            if (yearField.getText().toString().isEmpty()) {
                showErrorOnField(yearField, "Year can't be empty");
                return;
            }
            year = Integer.parseInt(yearField.getText().toString());
            if (year > currYear) {
                showErrorOnField(yearField, "Year cannot be greater than " + currYear);
                return;
            }
            if (year < 2000) {
                showErrorOnField(yearField, "Year cannot be less than " + 2000);
                return;
            }
            // if it reaches here, no errors for year
        } catch (NumberFormatException e) {
            showErrorOnField(yearField, "Year is not a valid number.");
            return;
        }


//        histGraphForm = findViewById(R.id.content_history_graph);
        String ppm;
        if (virusType.isChecked()) {
            ppm = "Virus";
        } else {
            ppm = "Contaminant";
        }
        reportCalc = new HistoricalReportCalc(latitude, longitude, year, ppm);

        localPurityReportsAPI = new LocalGetPurityReportsAPI();
        localPurityReportsAPI.execute((Void) null);
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

    private void startGraphActivity(double[] data, String ppmType) {
        Intent i = new Intent(this, PlottedGraphActivity.class);
        i.putExtra("data", data);
        i.putExtra("ppm", ppmType);
        startActivity(i);
    }


    class LocalGetPurityReportsAPI extends GetPurityReportsAPI {

        public LocalGetPurityReportsAPI() {
            super(getApplicationContext());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            localPurityReportsAPI = null;


            if (success) {
                if (purityReportList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No purity reports are in the system.", Toast.LENGTH_LONG).show();
                } else {
                    double[] averageMonthData = reportCalc.getAverages(reportCalc.filter(purityReportList));
                    startGraphActivity(averageMonthData, reportCalc.getFilters()[3]);

                }
            } else {
                Toast.makeText(getApplicationContext(), "No purity reports are in the system.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            localPurityReportsAPI = null;
        }
    }

}