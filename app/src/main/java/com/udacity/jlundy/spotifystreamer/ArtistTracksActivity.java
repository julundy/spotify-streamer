package com.udacity.jlundy.spotifystreamer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by justin.lundy on 9/25/15.
 */
public class ArtistTracksActivity extends AppCompatActivity {
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
