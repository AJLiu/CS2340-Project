package site.gitinitdone.h2go.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.SoundEffects;

/**
 * The Welcome screen, the first screen that the user sees when the app is launched
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playClickSound(v);
                switchLogin(v);
            }
        });

        b = (Button) findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playClickSound(v);
                switchRegister(v);
            }
        });
    }

    /**
     * Switches from the Welcome activity to the Login activity so the user can log in to the app
     *
     *  @param view the view where the button that called this method resides
     */
    public void switchLogin(View view){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    /**
     * Switches from the Welcome activity to the Register activity so the user can register a new
     * account in the app
     *
     * @param view the view where the button that called this method resides
     */
    public void switchRegister(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);

    }
}
