package site.gitinitdone.h2go.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.PurityReport;
import site.gitinitdone.h2go.model.SubmitPurityReportAPI;

/**
 * This activity allows the user to submit water purity reports
 */
public class SubmitPurityReportActivity extends AppCompatActivity {

    private LocalPurityReportAPI submitPurityReportAPI = null;
    private View submitPurityForm;
    private View mProgressView;

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
        ArrayAdapter<String> adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, PurityReport.OverallCondition.values());
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

        Spinner waterConditionSpinner = (Spinner) findViewById(R.id.overallConditionSpinner);
        waterCondition = waterConditionSpinner.getSelectedItem().toString();

        EditText virus = (EditText) findViewById(R.id.virusPPMNumber);
        virusPPM = virus.getText().toString();

        EditText contaminant = (EditText) findViewById(R.id.contaminantPPMNumber);
        contaminantPPM = contaminant.getText().toString();

        Map<String, String> data = new HashMap<String, String>();
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

    private String formatPPM(String oldText) {
        DecimalFormat formatPPMNumber = new DecimalFormat("#0");
        if (oldText.trim().isEmpty()) {
            oldText = "0";
        }
        return formatPPMNumber.format(Double.parseDouble(oldText.trim()));
    }

    /**
     * Shows the progress UI and hides the data until it's been populated.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
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
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            submitPurityForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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
                Toast.makeText(getBaseContext(), "Purity Report has been successfully submitted.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                System.out.println("Submitted Purity Report FALSE");
                Toast.makeText(getBaseContext(), "There was an error during submission.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            submitPurityReportAPI = null;
            showProgress(false);
        }

    }

}
