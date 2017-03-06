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
import site.gitinitdone.h2go.model.SourceReport;
import site.gitinitdone.h2go.model.SubmitSourceReportAPI;

/**
 * This activity allows the user to submit water source reports
 */
public class SubmitSourceReportActivity extends AppCompatActivity {

    private LocalSourceReportAPI submitSourceReportAPI = null;  // the AsyncTask object to submit the source report
    private View submitSourceForm;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_source_report);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_submit_source_report_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Setup the Spinner to show the Water Types
        Spinner waterTypeSpinner = (Spinner) findViewById(R.id.waterTypeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, SourceReport.WaterType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterTypeSpinner.setAdapter(adapter);
        waterTypeSpinner.setSelection(0);

        // Setup the Spinner to show the Water Conditions
        Spinner waterConditionSpinner = (Spinner) findViewById(R.id.waterConditionSpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, SourceReport.WaterCondition.values());
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

    }

    public void submitSourceReport(View view) {
        // Validating the entries in report
        double latitude;
        double longitude;
        String waterType;
        String waterCondition;

        EditText latitudeField = (EditText) findViewById(R.id.locationLat);
        EditText longitudeField = (EditText) findViewById(R.id.locationLong);
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

        Spinner waterTypeSpinner = (Spinner) findViewById(R.id.waterTypeSpinner);
        waterType = waterTypeSpinner.getSelectedItem().toString();

        Spinner waterConditionSpinner = (Spinner) findViewById(R.id.waterConditionSpinner);
        waterCondition = waterConditionSpinner.getSelectedItem().toString();

        Map<String, String> data = new HashMap<String, String>();
        data.put("lat", String.valueOf(latitude));
        data.put("long", String.valueOf(longitude));
        data.put("waterType", waterType);
        data.put("waterCondition", waterCondition);

        submitSourceForm = findViewById(R.id.content_submit_source_report);
        mProgressView = findViewById(R.id.submit_source_report_progress);

        submitSourceReportAPI = new LocalSourceReportAPI(data);
        submitSourceReportAPI.execute((Void) null);
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
        return formatLong.format(Double.parseDouble(oldText.trim()));
    }

    private String formatLatitude(String oldText) {
        DecimalFormat formatLat = new DecimalFormat("#0.000000");
        return formatLat.format(Double.parseDouble(oldText.trim()));
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

            submitSourceForm.setVisibility(show ? View.GONE : View.VISIBLE);
            submitSourceForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    submitSourceForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
            submitSourceForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous API to submit a source report.
     */
    class LocalSourceReportAPI extends SubmitSourceReportAPI {

        LocalSourceReportAPI(Map<String, String> data) {
            super(data, getApplicationContext());
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            submitSourceReportAPI = null;
            showProgress(false);

            if (success) {
                System.out.println("Submitted Source Report TRUE");
                Toast.makeText(getBaseContext(), "Source Report has been successfully submitted.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                System.out.println("Submitted Source Report FALSE");
                Toast.makeText(getBaseContext(), "There was an error during submission.", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {
            submitSourceReportAPI = null;
            showProgress(false);
        }

    }

}
