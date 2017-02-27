package site.gitinitdone.h2go;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The main app screen after a user has logged in successfully
 */
public class AppScreen extends AppCompatActivity {

    private String myUsername; // the username of whoever has just logged in successfully

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(getApplicationContext(), "You have successfully logged in.", Toast.LENGTH_LONG).show();

        // Get the username of whoever just logged in and show it in the corner of the screen
        Intent i = getIntent();
        myUsername = i.getExtras().getString("UserEmail");
        TextView userLoggedIn = (TextView) findViewById(R.id.userEmail);
        String loginMessage = "You are logged in as: " + myUsername;
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
        myUsername = null;
        Toast.makeText(getApplicationContext(), "Logging Out...", Toast.LENGTH_LONG).show();
        finish();
    }

}
