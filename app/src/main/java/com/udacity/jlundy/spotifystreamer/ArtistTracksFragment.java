package com.udacity.jlundy.spotifystreamer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by jlundy on 7/10/15.
 */
public class ArtistTracksFragment extends Fragment {

    private final String LOG_TAG = ArtistTracksFragment.class.getSimpleName();
    static public final String FRAGMENT_TAG = "TRACKS_FRAGMENT";
    static public final String TRACK_ID = "TRACK_ID";
    static public final String TWO_PANE = "TWO_PANE";

    private String artistId;
    ArrayList<MyTrack> myTracks;
    TrackListAdapter mTracksAdapter;
    private boolean isTablet;

    public ArtistTracksFragment() { setHasOptionsMenu(false); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            myTracks = savedInstanceState.getParcelableArrayList("TRACK_LIST");
        } else {
            if (getArguments() != null) {
                isTablet = getArguments().getBoolean(ArtistTracksFragment.TWO_PANE);
                artistId = getArguments().getString(ArtistSearchFragment.ARTIST_ID);
                GetTracksTask getTracksTask = new GetTracksTask();
                getTracksTask.execute(artistId);
            } else {
                Log.e(LOG_TAG, "No args for songs fragment");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);

        myTracks = new ArrayList<MyTrack>();

        if (mTracksAdapter == null) {
            mTracksAdapter = new TrackListAdapter(getActivity(), myTracks);
        }

        if (savedInstanceState != null) {
            myTracks = savedInstanceState.getParcelableArrayList("TRACK_LIST");
            mTracksAdapter.addAll(myTracks);
            mTracksAdapter.notifyDataSetChanged();
        }

        ListView listView;

        listView = (ListView) rootView.findViewById(R.id.list_view_tracks);

        listView.setAdapter(mTracksAdapter);

        //TODO Set listener for each track to call a dialogue fragment for the track player
        //TODO Add URL to MyTrack class
        //TODO use URL to use mediaPlayer in the player dialog
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Copied over from ArtistSearchFragment
                MyTrack track = mTracksAdapter.getItem(position);
                FragmentManager fragmentManager = getActivity().getFragmentManager();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(TrackDialogFragment.TRACK_ARRAY_KEY, mTracksAdapter.tracks);
                bundle.putInt(TrackDialogFragment.CURRENT_TRACK_POSITION_KEY, position);
                showDialog(bundle);
            }
        });

        return rootView;
    }

    public void showDialog(Bundle bundle) {
        FragmentManager fm = getFragmentManager();
        TrackDialogFragment trackDialogFragment = new TrackDialogFragment();
        trackDialogFragment.setArguments(bundle);
        if (isTablet) {
            // The device is using a large layout, so show the fragment as a dialog
            trackDialogFragment.show(fm, TrackDialogFragment.FRAGMENT_TAG);
        } else {

            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fm.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.replace(R.id.tracks_detail_container, trackDialogFragment, TrackDialogFragment.FRAGMENT_TAG)
                    .addToBackStack(null).commit();
        }
    }

    public void updateTracks(Bundle bundle) {
        artistId = bundle.getString(ArtistSearchFragment.ARTIST_ID);
        GetTracksTask getTracksTask = new GetTracksTask();
        getTracksTask.execute(artistId);
    }

    public class GetTracksTask extends AsyncTask<String, Void, ArrayList<MyTrack>> {

        @Override
        protected ArrayList<MyTrack> doInBackground(String... params) {
            String artistId = params[0];

            Map<String, Object> map = new HashMap<>();
            map.put("country", Locale.getDefault().getCountry());

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            Tracks tracks = spotify.getArtistTopTrack(artistId, map);

            ArrayList<MyTrack> returnTracks = new ArrayList<>();

            if (!tracks.tracks.isEmpty()) {
                for (Track track : tracks.tracks) {
                    String newArtistName = track.artists.get(0).name;
                    String newTrackName = track.name;
                    String newAlbumName = track.album.name;
                    Long newTrackLength = track.duration_ms;
                    String newImageUrl;
                    if (!track.album.images.isEmpty()) {
                        newImageUrl = track.album.images.get(track.album.images.size()-1).url;
                    } else {
                        newImageUrl = "https://rogueamoeba.com/support/knowledgebase/images/spotify128.png";
                    }
                    String newTrackUrl = track.preview_url;
                    MyTrack newTrack = new MyTrack(newArtistName, newAlbumName, newTrackName, newImageUrl, newTrackUrl, newTrackLength);

                    returnTracks.add(newTrack);
                }
            }
            return returnTracks;
        }

        @Override
        protected void onPostExecute(ArrayList<MyTrack> tracks) {
            super.onPostExecute(tracks);

            if (tracks != null) {
                if (tracks.isEmpty()) {
                    Toast.makeText(getActivity(), "No tracks found!", Toast.LENGTH_SHORT).show();
                } else {
                    myTracks = tracks;
                    mTracksAdapter.addAll(tracks);
                    mTracksAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getActivity(), "No connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("TRACK_LIST", myTracks);
    }
}
