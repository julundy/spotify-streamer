package com.udacity.jlundy.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by jlundy on 7/6/15.
 */
public class ArtistSearchFragment extends Fragment {
    private final String LOG_TAG = ArtistSearchFragment.class.getSimpleName();
    static public final String ARTIST_ID = "ARTIST_ID";
    static public final String FRAGMENT_TAG = "ARTIST_SEARCH_FRAGMENT";
    ArrayList<MyArtist> myArtists;

    ArtistListAdapter mArtistAdapter;

    public ArtistSearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(false);

        if (savedInstanceState != null) {
            myArtists = savedInstanceState.getParcelableArrayList("ARTIST_LIST");
        } else {
            myArtists = new ArrayList<>();
            if (getArguments() != null) {
                String query = getArguments().getString("query_string");
                GetArtistsTask getArtistsTask = new GetArtistsTask();
                getArtistsTask.execute(query);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_artist_search, container, false);


        if (mArtistAdapter == null) {
            mArtistAdapter = new ArtistListAdapter(getActivity(), myArtists);
        }

        if (savedInstanceState != null) {
            myArtists = savedInstanceState.getParcelableArrayList("ARTIST_LIST");
            mArtistAdapter.addAll(myArtists);
            mArtistAdapter.notifyDataSetChanged();
        }

        ListView listView = (ListView) rootView.findViewById(
                R.id.list_view_results);

        listView.setAdapter(mArtistAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyArtist artist = mArtistAdapter.getItem(position);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                ArtistTracksFragment fragment = (ArtistTracksFragment) fragmentManager.findFragmentByTag(ArtistTracksFragment.FRAGMENT_TAG);

                Bundle bundle = new Bundle();
                bundle.putString(ArtistSearchFragment.ARTIST_ID, artist.getArtistId());

                if (fragment == null) {
                    fragment = new ArtistTracksFragment();
                    fragment.setArguments(bundle);
                } else {
                    fragment.updateTracks(bundle);
                }

                fragmentManager.beginTransaction().replace(R.id.container, fragment, ArtistTracksFragment.FRAGMENT_TAG).addToBackStack(null).commit();
            }
        });

        return rootView;
    }

    public void updateArtist(Bundle bundle) {
        String query = bundle.getString(ArtistSearchActivity.QUERY_STRING);
        GetArtistsTask getArtistsTask = new GetArtistsTask();
        getArtistsTask.execute(query);
    }

    public class GetArtistsTask extends AsyncTask<String, Void, ArrayList<MyArtist>> {

        @Override
        protected ArrayList<MyArtist> doInBackground(String... params) {
            String query = params[0];
            SpotifyApi api = new SpotifyApi();
            ArrayList<MyArtist> artistList = new ArrayList<>();
            try {
                SpotifyService spotify = api.getService();
                ArtistsPager results = spotify.searchArtists(query);
                List<Artist> artists = results.artists.items;
                if (!artists.isEmpty()) {
                    for (Artist artist : artists) {
                        String newArtistName = artist.name;
                        String newArtistId = artist.id;
                        String newImageUrl;
                        if (!artist.images.isEmpty()) {
                            newImageUrl = artist.images.get(artist.images.size()-1).url;
                        } else {
                            newImageUrl = "https://rogueamoeba.com/support/knowledgebase/images/spotify128.png";
                        }
                        MyArtist newArtist = new MyArtist(newArtistId, newArtistName, newImageUrl);
                        artistList.add(newArtist);
                    }
                }
            } catch (Exception e) {
                artistList = null;
            }
            return artistList;
        }

        @Override
        protected void onPostExecute(ArrayList<MyArtist> artistList) {
            super.onPostExecute(artistList);

            if (artistList != null) {
                if (artistList.isEmpty()) {
                    Toast.makeText(getActivity(), "No artists found!", Toast.LENGTH_SHORT).show();
                } else {
                    myArtists = artistList;
                    mArtistAdapter.addAll(artistList);
                    mArtistAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getActivity(), "No connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("ARTIST_LIST", myArtists);
    }
}
