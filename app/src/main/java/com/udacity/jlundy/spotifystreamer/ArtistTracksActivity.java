package com.udacity.jlundy.spotifystreamer;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by justin.lundy on 9/25/15.
 */
public class ArtistTracksActivity extends Activity {
    private final String LOG_TAG = ArtistTracksActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_tracks_detail);
        if (savedInstanceState == null) {
            FragmentTransaction t = getFragmentManager().beginTransaction();
            ArtistTracksFragment artistTracksFragment = new ArtistTracksFragment();
            artistTracksFragment.setArguments(extras);
            t.add(R.id.tracks_detail_container, artistTracksFragment, ArtistTracksFragment.FRAGMENT_TAG);
            t.commit();
        }
    }
}
