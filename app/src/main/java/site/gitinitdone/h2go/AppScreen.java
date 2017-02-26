package site.gitinitdone.h2go;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AppScreen extends AppCompatActivity {

    private String myEmail;
    //private AppScreen.UserLoginTask mAuthTask = null;
    private java.net.CookieManager cookieManager;

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
        //cookieManager = LoginActivity.msCookieManager;
        TextView userLoggedIn = (TextView) findViewById(R.id.userEmail);
        userLoggedIn.setText("You are logged in as: " + myEmail);


    }

    public void logOut (View view) {
        myEmail = null;
        Toast.makeText(getApplicationContext(), "Logging Out...", Toast.LENGTH_LONG).show();
        finish();
    }




}
