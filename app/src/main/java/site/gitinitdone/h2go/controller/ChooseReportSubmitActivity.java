package site.gitinitdone.h2go.controller;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import site.gitinitdone.h2go.R;

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


    public void chooseSourceReport(View view) {
        Intent i = new Intent(this, SubmitSourceReportActivity.class);
        startActivity(i);
    }

    public void choosePurityReport(View view) {
        // TODO: Need to handle case where user chooses to submit a Purity Report
    }



}
