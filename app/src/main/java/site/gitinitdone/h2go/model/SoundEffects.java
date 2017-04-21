package site.gitinitdone.h2go.model;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;

import java.io.IOException;

import site.gitinitdone.h2go.R;

/**
 * Created by surajmasand on 4/21/17.
 */

public class SoundEffects {

    private static MediaPlayer mp;
    private static final String RES_PREFIX = "android.resource://site.gitinitdone.h2go/";

    public static void playClickSound(View view) {

        System.out.println("~~~~~~~~~~~~~~~~~Sound starting~~~~~~~~~~~~~~~~");
        if(mp != null && mp.isPlaying())
        {
            mp.stop();
            System.out.println("Sound stopped");
        }

        if (mp == null) {
            mp = MediaPlayer.create(view.getContext(), R.raw.water_droplet);
            mp.start();
        } else {
            try {
                mp.reset();
                mp.setDataSource(view.getContext(), Uri.parse(RES_PREFIX + R.raw.water_droplet));
                mp.prepare();
                mp.start();
            } catch (IllegalStateException | IOException e) { // | IOException e) {
                System.out.println("Sound error");
                e.printStackTrace();
            }
        }



        System.out.println("~~~~~~~~~~~~~~~~~Sound ended~~~~~~~~~~~~~~~~~~~");

    }

}
