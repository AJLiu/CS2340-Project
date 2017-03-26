package site.gitinitdone.h2go.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.GetUserAPI;

/**
 * This Activity allows the user to select which type of report to submit and will redirect
 * them to the appropriate form to fill out
 */
public class ChooseReportSubmitActivity extends AppCompatActivity {

    private String userType = "";

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
        LocalGetUserAPI getUserAPI = new LocalGetUserAPI();
        getUserAPI.execute((Void) null);
    }

    /**
     * Represents an asynchronous getUserInfo task used to get the data in the user profile.
     */
    class LocalGetUserAPI extends GetUserAPI {

        public LocalGetUserAPI() {
            super(getApplicationContext());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //mAuthTask = null;
            //showProgress(false);

            if (success) {
                userType = userAccount.getUserType().toString();
                if (userType == null || userType.length() == 0 || userType.equalsIgnoreCase("User")) {
                    Toast.makeText(getBaseContext(), "General users cannot submit a purity report.", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(getBaseContext(), SubmitPurityReportActivity.class);
                    startActivity(i);
                }
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            //showProgress(false);
        }
    }



}
