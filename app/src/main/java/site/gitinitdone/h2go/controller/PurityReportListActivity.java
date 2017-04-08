package site.gitinitdone.h2go.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;
import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.GetPurityReportsAPI;
import site.gitinitdone.h2go.model.PurityReport;

import java.util.Date;
import java.util.List;

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
                if (purityReportList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.empty_purity_report_toast,
                            Toast.LENGTH_LONG).show();
                } else {
                    TextView reportData = (TextView) findViewById(R.id.purityReportData);
                    reportData.setText(populatePurityList(purityReportList));
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.empty_purity_report_toast,
                        Toast.LENGTH_LONG).show();
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
            String[] reportString = pr.getReportStringFormatted();
            allReports += reportString[0] + "\n" + reportString[1];
        }

        return allReports;
    }
}
