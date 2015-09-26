package com.udacity.jlundy.spotifystreamer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class ArtistSearchActivity extends AppCompatActivity {

    private final String LOG_TAG = ArtistSearchActivity.class.getSimpleName();
    public static final String QUERY_STRING = "query_string";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_artist_search);
        if (findViewById(R.id.tracks_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tracks_detail_container, new ArtistTracksFragment(), ArtistTracksFragment.FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        // Get the intent, verify the action and get the query
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Bundle args = new Bundle();
            args.putString(QUERY_STRING, query);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            ArtistSearchFragment fragment = (ArtistSearchFragment) getSupportFragmentManager().findFragmentByTag(ArtistSearchFragment.FRAGMENT_TAG);
            if (fragment == null) {
                fragment = new ArtistSearchFragment();
                fragment.setArguments(args);
            } else {
                fragment.updateArtist(args);
            }

            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArtistSearchFragment artistSearchFragment = (ArtistSearchFragment)getSupportFragmentManager().findFragmentById(R.id.container);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
