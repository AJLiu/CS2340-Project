package site.gitinitdone.h2go.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.GetPurityReportsAPI;
import site.gitinitdone.h2go.model.GetSourceReportsAPI;
import site.gitinitdone.h2go.model.PurityReport;
import site.gitinitdone.h2go.model.SourceReport;

/**
 * This class is used to show all the reports currently in the system.
 */
public class ReportListActivity extends AppCompatActivity {

    private LocalGetSourceReportsAPI getSourceReports = null;
    private LocalGetPurityReportsAPI getPurityReports = null;
    private View getReportsView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_report_list_toolbar);
        setSupportActionBar(toolbar);

        TextView reportDataView = (TextView) findViewById(R.id.reportData);
        reportDataView.setMovementMethod(new ScrollingMovementMethod());

        getReportsView = findViewById(R.id.content_report_list);
        mProgressView = findViewById(R.id.get_source_report_progress);

        getSourceReports = new LocalGetSourceReportsAPI();
        getSourceReports.execute((Void) null);

        getPurityReports = new LocalGetPurityReportsAPI();

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

            getReportsView.setVisibility(show ? View.GONE : View.VISIBLE);
            getReportsView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getReportsView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            getReportsView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous getSourceReportsAPI task used to get the source report data
     * from the backend database.
     */
    class LocalGetSourceReportsAPI extends GetSourceReportsAPI {

        public LocalGetSourceReportsAPI() {
            super(getApplicationContext());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getSourceReports = null;
            showProgress(false);

            if (success) {
                if (sourceReportList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No source reports are in the system.", Toast.LENGTH_LONG).show();
                } else {
                    TextView reportData = (TextView) findViewById(R.id.reportData);
                    reportData.setText(populateSourceList(sourceReportList));
                    getPurityReports.execute((Void) null);
                }
            } else {
                Toast.makeText(getApplicationContext(), "No source reports are in the system.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            getSourceReports = null;
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous getSourceReportsAPI task used to get the source report data
     * from the backend database.
     */
    class LocalGetPurityReportsAPI extends GetPurityReportsAPI {

        public LocalGetPurityReportsAPI() {
            super(getApplicationContext());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getPurityReports = null;
            showProgress(false);

            if (success) {
                if (purityReportList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No purity reports are in the system.", Toast.LENGTH_LONG).show();
                } else {
                    TextView reportData = (TextView) findViewById(R.id.reportData);
                    reportData.setText(reportData.getText() + populatePurityList(purityReportList));

                }
            } else {
                Toast.makeText(getApplicationContext(), "No purity reports are in the system.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            getSourceReports = null;
            showProgress(false);
        }
    }

    /**
     * A helper method used to show the report information on the screen in the Text View
     * which may later be changed to ta Recycler View or Expandable List View
     * @param sourceReportArrayList
     */
    private String populateSourceList(List<SourceReport> sourceReportArrayList) {

        String allReports = "";

        for (SourceReport sr : sourceReportArrayList) {
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
            allReports += "--- Source Report #" + reportNum + " ---\n";
            allReports += "Submitted On: " + date + "\n";
            allReports += "Submitted By: " + submitter + "\n";
            allReports += "Location: \n \t Latitude: " + latitude + " \n \t Longitude: " + longitude + "\n";
            allReports += "Water Type: " + waterType + "\n";
            allReports += "Water Condition: " + waterCondition + "\n \n";
        }

        return allReports;

    }

    private String populatePurityList(List<PurityReport> purityReportsList) {
        String allReports = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";

        for (PurityReport pr : purityReportsList) {
            int reportNum = pr.getReportNumber();
            String date = (new Date(pr.getTimeStamp())).toString();
            String submitter = pr.getReporter();

            // Handles if the direction of latitude is North or South based on negative sign
            String latitude = "";
            if (pr.getLatitude() < 0) {
                latitude = (pr.getLatitude() * -1) + " South";
            } else {
                latitude = pr.getLatitude() + " North";
            }

            // Handles if the direction of longitude is East or West based on negative sign
            String longitude = "";
            if (pr.getLongitude() < 0) {
                longitude = (pr.getLongitude() * -1) + " West";
            } else {
                longitude = pr.getLongitude() + " East";
            }

            int virusPPM = pr.getVirusPPM();
            int contaminantPPM = pr.getContaminantPPM();

            String waterCondition = pr.getWaterCondition().toString();
            // Aggregates all the relevant fields into a nicely formatted string to show on screen
            allReports += "--- Purity Report #" + reportNum + " ---\n";
            allReports += "Submitted On: " + date + "\n";
            allReports += "Submitted By: " + submitter + "\n";
            allReports += "Location: \n \t Latitude: " + latitude + " \n \t Longitude: " + longitude + "\n";
            allReports += "Water Condition: " + waterCondition + "\n";
            allReports += "Virus PPM: " + virusPPM + "\n";
            allReports += "Contaminant PPM: " + contaminantPPM + "\n \n";
        }

        return allReports;
    }


}
