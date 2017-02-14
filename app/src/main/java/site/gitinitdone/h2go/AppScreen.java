package site.gitinitdone.h2go;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AppScreen extends AppCompatActivity {

    private String myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Toast.makeText(getApplicationContext(), "You have successfully logged in.", Toast.LENGTH_LONG).show();

        Intent i = getIntent();
        myEmail = i.getExtras().getString("UserEmail");
        TextView userLoggedIn = (TextView) findViewById(R.id.userEmail);
        userLoggedIn.setText("You are logged in as: " + myEmail);

    }

    public void logOut (View view) {
        myEmail = null;
        Toast.makeText(getApplicationContext(), "Logging Out...", Toast.LENGTH_LONG).show();
        finish();
    }

}
