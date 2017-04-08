package site.gitinitdone.h2go.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.GetPurityReportsAPI;
import site.gitinitdone.h2go.model.HistoricalReportCalc;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 *  This class creates the screen with the filter fields location, year,
 *  and data type to plot the purity graph.
*/

public class HistoryGraphActivity extends AppCompatActivity {

    private HistoricalReportCalc reportCalc;
    private LocalGetPurityReportsAPI localPurityReportsAPI = null;
    private static final int MAX_LAT = 90;
    private static final int MAX_LONG = 180;
    private static final int MIN_YEAR = 2000;

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

    /* Method to format longitude to six decimal places.
    */
    private String formatLongitude(String oldText) {
        DecimalFormat formatLong = new DecimalFormat("##0.000000");
        if (oldText.trim().isEmpty()) {
            oldText = "0";
        }
        return formatLong.format(Double.parseDouble(oldText.trim()));
    }

    /* Method to format latitude to six decimal places.
    */
    private String formatLatitude(String oldText) {
        DecimalFormat formatLat = new DecimalFormat("#0.000000");
        if (oldText.trim().isEmpty()) {
            oldText = "0";
        }
        return formatLat.format(Double.parseDouble(oldText.trim()));
    }

    /* Method to validate latitude, longitude, year, and data type.
    */
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

        try {
            if (latitudeField.getText().toString().isEmpty()) {
                showErrorOnField(latitudeField, getString(R.string.Latitude_validation));
                return;
            }
            latitudeField.setText(formatLatitude(latitudeField.getText().toString()));
            latitude = Double.parseDouble(latitudeField.getText().toString());
            if ((latitude > MAX_LAT) || (latitude < (MAX_LAT * -1))) {
                showErrorOnField(latitudeField, getString(R.string.Latitude_range_validation));
                return;
            }
            // if it reaches here, no errors for latitude

        } catch (NumberFormatException e) {
            showErrorOnField(latitudeField, getString(R.string.Latitude_number_validation));
            return;
        }

        try {
            if (longitudeField.getText().toString().isEmpty()) {
                showErrorOnField(longitudeField, getString(R.string.Longitude_validation));
                return;
            }
            longitudeField.setText(formatLongitude(longitudeField.getText().toString()));
            longitude = Double.parseDouble(longitudeField.getText().toString());
            if ((longitude > MAX_LONG) || (longitude < (MAX_LONG * -1))) {
                showErrorOnField(longitudeField, getString(R.string.Longitude_range_validation));
                return;
            }
            // if it reaches here, no errors for longitude
        } catch (NumberFormatException e) {
            showErrorOnField(latitudeField, getString(R.string.Longitude_number_validation));
            return;
        }

        try {
            if (yearField.getText().toString().isEmpty()) {
                showErrorOnField(yearField, getString(R.string.year_validation));
                return;
            }
            year = Integer.parseInt(yearField.getText().toString());
            if (year > currYear) {
                showErrorOnField(yearField, getString(R.string.year_upper_bound) + currYear);
                return;
            }
            if (year < MIN_YEAR) {
                showErrorOnField(yearField, getString(R.string.year_lower_bound) + MIN_YEAR);
                return;
            }
            // if it reaches here, no errors for year
        } catch (NumberFormatException e) {
            showErrorOnField(yearField, getString(R.string.year_number_validation));
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

    /**
    * Starts the activity for the plotted graph based on the fields entered in the filter screen.
    */
    private void startGraphActivity(double[] data, String ppmType) {
        Intent i = new Intent(this, PlottedGraphActivity.class);
        i.putExtra("data", data);
        i.putExtra("ppm", ppmType);
        startActivity(i);
    }

    /* Class to get the reports from the database using the Model. 
    */

    class LocalGetPurityReportsAPI extends GetPurityReportsAPI {

        public LocalGetPurityReportsAPI() {
            super(getApplicationContext());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            localPurityReportsAPI = null;


            if (success) {
                if (purityReportList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.empty_purity_report_toast,
                            Toast.LENGTH_LONG).show();
                } else {
                    double[] averageMonthData = reportCalc.getAverages(reportCalc
                                                                .filter(purityReportList));
                    startGraphActivity(averageMonthData, reportCalc.getFilters()[3]);

                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.empty_purity_report_toast,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            localPurityReportsAPI = null;
        }
    }

}
