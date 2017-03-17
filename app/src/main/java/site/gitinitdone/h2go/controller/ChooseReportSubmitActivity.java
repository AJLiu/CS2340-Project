package site.gitinitdone.h2go.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import site.gitinitdone.h2go.R;

/**
 * This Activity allows the user to select which type of report to submit and will redirect
 * them to the appropriate form to fill out
 */
public class ChooseReportSubmitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_report_submit);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_choose_report_submit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }


    /**
     * Redirects the user to the form to submit a new water source report
     * @param view the view where the button that called this method resides
     */
    public void chooseSourceReport(View view) {
        Intent i = new Intent(this, SubmitSourceReportActivity.class);
        startActivity(i);
    }

    /**
     * Redirects the user to the form to submit a new water quality / purity report
     * @param view the view where the button that called this method resides
     */
    public void choosePurityReport(View view) {
        // TODO: Need to handle case where user chooses to submit a Purity Report
    }





}
