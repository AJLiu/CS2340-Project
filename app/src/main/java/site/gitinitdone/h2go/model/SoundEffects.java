package site.gitinitdone.h2go.model;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import site.gitinitdone.h2go.R;

/**
 * Created by surajmasand on 4/21/17.
 */

public class SoundEffects {

    private static MediaPlayer mp;
    private static final String RES_PREFIX = "android.resource://site.gitinitdone.h2go/";

    public static void playClickSound(Context context) {

        System.out.println("~~~~~~~~~~~~~~~~~Sound starting~~~~~~~~~~~~~~~~");
        if(mp != null && mp.isPlaying())
        {
            mp.stop();
            System.out.println("Sound stopped");
        }

        if (mp == null) {
            mp = MediaPlayer.create(context, R.raw.water_droplet);
            mp.start();
        } else {
            try {
                mp.reset();
                mp.setDataSource(context, Uri.parse(RES_PREFIX + R.raw.water_droplet));
                mp.prepare();
                mp.start();
            } catch (IllegalStateException | IOException e) { // | IOException e) {
                System.out.println("Sound error");
                e.printStackTrace();
            }
        }



        System.out.println("~~~~~~~~~~~~~~~~~Sound ended~~~~~~~~~~~~~~~~~~~");

    }

    public static void playClickSound(View view) {
        if (view != null && view.getContext() != null)
            playClickSound(view.getContext());
        else
            System.out.println("Cannot play sound. View is null or Context from view is null.");
    }

}
