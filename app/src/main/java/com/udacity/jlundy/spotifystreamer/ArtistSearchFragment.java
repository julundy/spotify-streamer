package com.udacity.jlundy.spotifystreamer;

import android.app.Fragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by jlundy on 7/6/15.
 */
public class ArtistSearchFragment extends Fragment {

    ArtistListAdapter mArtistAdapter;

    public ArtistSearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artist_search, container, false);
        TypedArray characterNames = getResources().obtainTypedArray(R.array.character_names);
        TypedArray characterIcons = getResources().obtainTypedArray(R.array.character_icons);
        mArtistAdapter = new ArtistListAdapter(
                getActivity(),
                characterNames,
                characterIcons );

        ListView listView = (ListView) rootView.findViewById(
                R.id.list_view_results);

        listView.setAdapter(mArtistAdapter);

        return rootView;
    }
}
