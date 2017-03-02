package site.gitinitdone.h2go.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import site.gitinitdone.h2go.R;

/**
 * The main app screen after a user has logged in successfully
 */
public class AppScreenActivity extends AppCompatActivity {

    private String sharedPrefName;
    private String usernameKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_app_screen_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Get the username of whoever just logged in and show it in the corner of the screen
        sharedPrefName = getApplicationContext().getString(R.string.sharedPrefName);
        usernameKey = getString(R.string.prompt_username);

        String usernameFromLogin = getSharedPreferences(sharedPrefName, MODE_PRIVATE).getString(usernameKey, null);
        TextView userLoggedIn = (TextView) findViewById(R.id.usernameLoggedIn);
        String loginMessage = "You are logged in as: " + usernameFromLogin;
        userLoggedIn.setText(loginMessage);
    }

    /**
     * Switched from the App Screen activity to the Edit User activity
     * so the user can edit their profile
     *
     * @param view the view where the button that called this method resides
     */
    public void editUser (View view) {
        Intent i = new Intent(this, EditUserActivity.class);
        startActivity(i);
    }

    /**
     * Switched from the App Screen activity to the Welcome activity and sets the username to null
     * so the user is logged out of the app. They can log in using a different account or choose
     * to register a new account from the Welcome screen.
     *
     * @param view the view where the button that called this method resides
     */
    public void logOut (View view) {
        SharedPreferences.Editor editor = getSharedPreferences(sharedPrefName, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Toast.makeText(getApplicationContext(), getString(R.string.loggingOut), Toast.LENGTH_LONG).show();
        finish();
    }

}
