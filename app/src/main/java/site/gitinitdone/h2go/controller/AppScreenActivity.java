package site.gitinitdone.h2go.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.GetUserAPI;

/**
 * The main app screen after a user has logged in successfully
 */
public class AppScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String sharedPrefName;
    private String usernameKey;
    private String preferredAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_app_screen_toolbar);
        setSupportActionBar(toolbar);

        // Get the username of whoever just logged in and show it in the corner of the screen
        sharedPrefName = getApplicationContext().getString(R.string.sharedPrefName);
        usernameKey = getString(R.string.prompt_username);

        String usernameFromLogin = getSharedPreferences(sharedPrefName, MODE_PRIVATE)
                                        .getString(usernameKey, null);
        TextView userLoggedIn = (TextView) findViewById(R.id.usernameLoggedIn);
        String loginMessage = getString(R.string.login_message) + usernameFromLogin;
        userLoggedIn.setText(loginMessage);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Switched from the App Screen activity to the Edit User activity
     * so the user can edit their profile
     */
    private void editUser () {
        Intent i = new Intent(this, EditUserActivity.class);
        startActivity(i);
    }

    /**
     * Switched from the App Screen activity to the Welcome activity and sets the username to null
     * so the user is logged out of the app. They can log in using a different account or choose
     * to register a new account from the Welcome screen.
     */
    private void logOut () {
        SharedPreferences.Editor editor = getSharedPreferences(sharedPrefName, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Toast.makeText(getApplicationContext(), getString(R.string.loggingOut),
                            Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutButton:
                logOut();
                return true;

            case R.id.editUserButton:
                editUser();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = null; // default value as a backup

        if (id == R.id.nav_submit_report) {
            i = new Intent(this, ChooseReportSubmitActivity.class);
        } else if (id == R.id.nav_water_locations) {
            i = new Intent(this, MapViewActivity.class);
        } else if (id == R.id.nav_source_report_list) {
            i = new Intent(this, SourceReportListActivity.class);
        } else if (id == R.id.nav_purity_report_list) {
            preferredAction = "purity_report";
            LocalGetUserAPI getUserAPI = new LocalGetUserAPI();
            getUserAPI.execute((Void) null);
        } else if (id == R.id.history_graph) {
            preferredAction = "history_graph";
            LocalGetUserAPI getUserInfo = new LocalGetUserAPI();
            getUserInfo.execute((Void) null);
        }
        if (i != null) {
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                if ("purity_report".equalsIgnoreCase(preferredAction)) {
                    String userType = userAccount.getUserType().toString();
                    if ((userType == null) || userType.isEmpty()
                            || "User".equalsIgnoreCase(userType)
                            || "Worker".equalsIgnoreCase(userType)) {
                        Toast.makeText(getBaseContext(),
                                R.string.purity_permission_toast,
                                Toast.LENGTH_LONG).show();
                    } else {
                        Intent purityList = new Intent(getBaseContext(),
                                PurityReportListActivity.class);
                        startActivity(purityList);
                    }
                } else if ("history_graph".equalsIgnoreCase(preferredAction)) {
                    String userType = userAccount.getUserType().toString();
                    if ((userType == null) || !("Manager".equalsIgnoreCase(userType))) {
                        Toast.makeText(getBaseContext(), R.string.graph_permission_toast,
                                Toast.LENGTH_LONG).show();
                    } else {
                        Intent graph = new Intent(getBaseContext(), HistoryGraphActivity.class);
                        startActivity(graph);
                    }
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
