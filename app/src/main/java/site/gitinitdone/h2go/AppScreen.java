package site.gitinitdone.h2go;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AppScreen extends AppCompatActivity {

    private String myEmail;
    private java.net.CookieManager cookieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(getApplicationContext(), "You have successfully logged in.", Toast.LENGTH_LONG).show();

        Intent i = getIntent();
        myEmail = i.getExtras().getString("UserEmail");
        //cookieManager = LoginActivity.msCookieManager;
        TextView userLoggedIn = (TextView) findViewById(R.id.userEmail);
        userLoggedIn.setText("You are logged in as: " + myEmail);
    }

    public void editUser (View view) {
        Intent i = new Intent(this, EditUserActivity.class);
        startActivity(i);
    }

    public void logOut (View view) {
        myEmail = null;
        Toast.makeText(getApplicationContext(), "Logging Out...", Toast.LENGTH_LONG).show();
        finish();
    }

}
