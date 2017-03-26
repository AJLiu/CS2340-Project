package site.gitinitdone.h2go.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.GetPurityReportsAPI;
import site.gitinitdone.h2go.model.PurityReport;

public class PurityReportListActivity extends AppCompatActivity {
    private LocalGetPurityReportsAPI getPurityReports = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purity_report_list);

        TextView reportDataView = (TextView) findViewById(R.id.purityReportData);
        reportDataView.setMovementMethod(new ScrollingMovementMethod());

        getPurityReports = new LocalGetPurityReportsAPI();
        getPurityReports.execute((Void) null);
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

            if (success) {
                if (purityReportList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No purity reports are in the system.", Toast.LENGTH_LONG).show();
                } else {
                    TextView reportData = (TextView) findViewById(R.id.purityReportData);
                    reportData.setText(populatePurityList(purityReportList));
                }
            } else {
                Toast.makeText(getApplicationContext(), "No purity reports are in the system.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            getPurityReports = null;
        }
    }


    private String populatePurityList(List<PurityReport> purityReportsList) {
        String allReports = "";

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
