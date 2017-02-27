package site.gitinitdone.h2go;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
    public void switch1(View v){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
    public void switchRegister(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);

    }
}
