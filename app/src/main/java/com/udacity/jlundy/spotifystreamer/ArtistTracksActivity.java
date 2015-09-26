package com.udacity.jlundy.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by justin.lundy on 9/25/15.
 */
public class ArtistTracksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.tracks_detail_container, new ArtistTracksFragment())
                    .commit();
        }
    }
}
