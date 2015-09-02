package com.udacity.jlundy.spotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by jlundy on 8/18/15.
 */
public class TrackDialogFragment extends Fragment {
    private final String LOG_TAG = TrackDialogFragment.class.getSimpleName();
    public static final String TRACK_ARRAY_KEY = "TRACK_ARRAY_KEY";
    public static final String CURRENT_TRACK_POSITION_KEY = "CURRENT_TRACK_POSITION_KEY";
    public static final String FRAGMENT_TAG = "TRACK_DIALOG_FRAGMENT";

    ArrayList<MyTrack> myTracks;
    MyTrack currentTrack;
    MediaPlayer player;
    private int currentTrackPosition;

    public TrackDialogFragment() {
    }

    //TODO Need to implement a service when playing media, to separate from orientation change etc
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        player = new MediaPlayer();

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDataSource(url);
        player.prepare(); // might take long! (for buffering, etc)
        player.start();

        updateTrack();

        //TODO Using this as a template
        if (savedInstanceState != null) {
            myTracks = savedInstanceState.getParcelableArrayList(TrackDialogFragment.TRACK_ARRAY_KEY);
            currentTrackPosition = savedInstanceState.getInt(TrackDialogFragment.CURRENT_TRACK_POSITION_KEY);
            currentTrack = myTracks.get(currentTrackPosition);
        } else {
            if (getArguments() != null) {
                myTracks = getArguments().getParcelableArrayList(TrackDialogFragment.TRACK_ARRAY_KEY);
                currentTrackPosition = getArguments().getInt(TrackDialogFragment.CURRENT_TRACK_POSITION_KEY);
                artistId = getArguments().getString(ArtistSearchFragment.ARTIST_ID);
                GetTracksTask getTracksTask = new GetTracksTask();
                getTracksTask.execute(artistId);
            } else {
                Log.i(LOG_TAG, "No tracks in arguments");
            }
        }
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_track_player, container, false);



        return rootView;
    }

    //TODO Hook with AsyncTask to prepare mediaplayer
    public void updateTrack(Bundle bundle) {

        myTracks = bundle.getParcelableArrayList(TRACK_ARRAY_KEY);
        currentTrack = myTracks.get(bundle.getInt(CURRENT_TRACK_POSITION_KEY));
        PlayTrackTask getTracksTask = new PlayTrackTask();
        getTracksTask.execute(artistId);
    }


    public class PlayTrackTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

        }
    }

}
