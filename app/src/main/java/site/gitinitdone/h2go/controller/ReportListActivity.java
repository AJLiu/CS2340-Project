package site.gitinitdone.h2go.controller;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.GetSourceReportsAPI;
import site.gitinitdone.h2go.model.SourceReport;
import site.gitinitdone.h2go.model.UserAccount;


public class ReportListActivity extends AppCompatActivity {

    private LocalGetSourceReportsAPI getSourceReports = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_report_list_toolbar);
        setSupportActionBar(toolbar);

        TextView reportDataView = (TextView) findViewById(R.id.reportData);
        reportDataView.setMovementMethod(new ScrollingMovementMethod());

        getSourceReports = new LocalGetSourceReportsAPI();
        getSourceReports.execute((Void) null);
    }

    /**
     * Represents an asynchronous getUserInfo task used to get the data in the user profile.
     */
    class LocalGetSourceReportsAPI extends GetSourceReportsAPI {

        public LocalGetSourceReportsAPI() {
            super(getApplicationContext());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //mAuthTask = null;
            //showProgress(false);

            if (success) {
                if (sourceReportList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No reports are in the system.", Toast.LENGTH_LONG).show();
                } else {
                    populateList(sourceReportList);
                }
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            //showProgress(false);
        }
    }

    private void populateList(ArrayList<SourceReport> sourceReportArrayList) {
        TextView reportData = (TextView) findViewById(R.id.reportData);

        String allReports = "";

        for (SourceReport sr : sourceReportArrayList) {
            int reportNum = sr.getReportNumber();
            String date = (new Date(sr.getTimeStamp())).toString();
            String submitter = sr.getReporter();
            String latitude = "";
            if (sr.getLatitude() < 0) {
                latitude = (sr.getLatitude() * -1) + " West";
            } else {
                latitude = sr.getLatitude() + " East";
            }

            String longitude = "";
            if (sr.getLongitude() < 0) {
                longitude = (sr.getLongitude() * -1) + " South";
            } else {
                longitude = sr.getLongitude() + " North";
            }

            String waterType = sr.getWaterType().toString();
            String waterCondition = sr.getWaterCondition().toString();


            allReports += "--- Report #" + reportNum + " ---\n";
            allReports += "Submitted On: " + date + "\n";
            allReports += "Submitted By: " + submitter + "\n";
            allReports += "Location: \n \t Latitude: " + latitude + " \n \t Longitude: " + longitude + "\n";
            allReports += "Water Type: " + waterType + "\n";
            allReports += "Water Condition: " + waterCondition + "\n \n";
        }

        reportData.setText(allReports);

//        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, UserAccount.Title.values());
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        title.setAdapter(adapter);

    }


}
