package com.udacity.jlundy.spotifystreamer;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jlundy on 8/18/15.
 */
public class TrackDialogFragment extends DialogFragment {
    private final String LOG_TAG = TrackDialogFragment.class.getSimpleName();
    public static final String TRACK_ARRAY_KEY = "TRACK_ARRAY_KEY";
    public static final String CURRENT_TRACK_POSITION_KEY = "CURRENT_TRACK_POSITION_KEY";
    public static final String FRAGMENT_TAG = "TRACK_DIALOG_FRAGMENT";
    private final String PREVIEW_DURATION = "00:30";

    ArrayList<MyTrack> myTracks;
    MyTrack currentTrack;
    MediaPlayerService playerService;
    private int currentTrackPosition;
    private boolean isBound = false;
    private boolean isPaused = true;
    private boolean isInit = false;
    Integer duration;
    Integer currentTime;
    private Intent playIntent;
    TextView playerArtist;
    TextView playerAlbum;
    TextView playerTrack;
    ImageView playerImage;
    Button previousButton;
    Button playPauseButton;
    Button nextButton;

    TextView durationText;
    TextView currentTimeText;
    SeekBar seekBar;
    Handler mHandler = new Handler();

    public TrackDialogFragment() {
    }

    //TODO Need to implement a service when playing media, to separate from orientation change etc
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO Using this as a template
        if (savedInstanceState != null) {

            myTracks = savedInstanceState.getParcelableArrayList(TrackDialogFragment.TRACK_ARRAY_KEY);
            currentTrackPosition = savedInstanceState.getInt(TrackDialogFragment.CURRENT_TRACK_POSITION_KEY);
            currentTrack = myTracks.get(currentTrackPosition);
        } else {
            if (getArguments() != null) {
                myTracks = getArguments().getParcelableArrayList(TrackDialogFragment.TRACK_ARRAY_KEY);
                currentTrackPosition = getArguments().getInt(TrackDialogFragment.CURRENT_TRACK_POSITION_KEY);
                currentTrack = myTracks.get(currentTrackPosition);
            } else {
                Log.i(LOG_TAG, "No tracks in arguments");
            }
        }
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_track_player, container, false);
        playerArtist = (TextView) rootView.findViewById(R.id.player_artist);
        playerAlbum = (TextView) rootView.findViewById(R.id.player_album);
        playerTrack = (TextView) rootView.findViewById(R.id.player_track);
        playerImage = (ImageView) rootView.findViewById(R.id.player_image);

        previousButton = (Button) rootView.findViewById(R.id.player_previous);
        playPauseButton = (Button) rootView.findViewById(R.id.player_play_pause);
        nextButton = (Button) rootView.findViewById(R.id.player_next);
        playPauseButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        durationText = (TextView) rootView.findViewById(R.id.player_track_length);
        currentTimeText = (TextView) rootView.findViewById(R.id.player_current_time);
        seekBar = (SeekBar) rootView.findViewById(R.id.player_seekbar);
        durationText.setText("00:00");
        currentTimeText.setText("00:00");

        return rootView;
    }

    private void updateTrack() {
        duration = null;
        playerArtist.setText(currentTrack.getArtistName());
        playerAlbum.setText(currentTrack.getAlbumName());
        playerTrack.setText(currentTrack.getTrackName());
        //TODO to be used with real track lengths

        Picasso.with(getActivity().getApplicationContext()).load(currentTrack.getImageUrl()).resize(500, 500)
                .centerInside().into(playerImage);
        playerService.setTrack(currentTrackPosition);
        playerService.playTrack();

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTrackPosition > 0) {
                    currentTrackPosition = currentTrackPosition - 1;
                } else {
                    currentTrackPosition = myTracks.size() - 1;
                }
                currentTrack = myTracks.get(currentTrackPosition);
                playerService.player.reset();
                updateTrack();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currentTrackPosition < myTracks.size() - 1) {
                    currentTrackPosition = currentTrackPosition + 1;
                } else {
                    currentTrackPosition = 0;
                }
                currentTrack = myTracks.get(currentTrackPosition);
                playerService.player.reset();
                updateTrack();
            }
        });
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerService.player.isPlaying()) {
                    playerService.pauseTrack();
                    playPauseButton.setBackgroundResource(android.R.drawable.ic_media_play);
                } else {
                    playerService.playTrack();
                    playPauseButton.setBackgroundResource(android.R.drawable.ic_media_pause);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (playerService != null && fromUser) {
                    int currentTime = progress * 1000;
                    playerService.player.seekTo(currentTime);
                    currentTimeText.setText(DateFormat.format("mm:ss", currentTime).toString());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Runnable musicRunnable = new Runnable() {
            @Override
            public void run() {

                if (isBound == true) { // Check if service bounded
                    if (duration != null) {
                        if (duration == 0) { // Put data in it one time
                            duration = playerService.getDuration();
                        } else {
                            Log.i(LOG_TAG, "Duration is: " + duration);
                            String durationString = DateFormat.format("mm:ss", duration).toString();
                            Log.i(LOG_TAG, "formatting duration: " + durationString);
                            seekBar.setMax(duration);
                            durationText.setText(durationString);
                            currentTime = playerService.getPosition();
                            seekBar.setProgress(currentTime);
                        }
                    } else {
                        duration = playerService.getDuration();
                        Log.i(LOG_TAG, "duration is null");
                    }
                } else if (isBound == false) { // if service is not bounded log it
                    Log.v("Still waiting to bound", Boolean.toString(isBound));
                }
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.postDelayed(musicRunnable, 1000);

        getActivity().runOnUiThread(musicRunnable);
    }

//        {
//            @Override
//            public void run() {
////                if (playerService != null) {
////                    int mCurrentPosition = playerService.player.getCurrentPosition() / 1000;
////                    seekBar.setProgress(mCurrentPosition);
////                    currentTimeText.setText(DateFormat.format("mm:ss", playerService.player.getCurrentPosition()).toString());
////                }
//
//                if (playerService != null) { // Check if service bounded
//                    Log.i(LOG_TAG, "RUNNING");
//                    if (duration == null) { // Put data in it one time
//                        Log.i(LOG_TAG, "duration is null");
//                        duration = playerService.getDuration();
//                        String durationString = DateFormat.format("mm:ss", duration).toString();
//                        Log.v(LOG_TAG, "Duration of track is: " + durationString);
//                        durationText.setText(durationString);
//                        seekBar.setMax(duration);
//                    } else {
//                        Log.i(LOG_TAG, "duration is not null");
//                        currentTime = playerService.getPosition();
//                        seekBar.setProgress(currentTime);
//                    }
//                } else if (isBound == false) { // if service is not bounded log it
//                    Log.v("Still waiting to bound", Boolean.toString(isBound));
//                } else {
//                    Log.e(LOG_TAG, "NOTHINGS HAPPEING");
//
//                }
//                mHandler.postDelayed(this, 1000);
//            }
//        });
//        mHandler.postDelayed()
//    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MediaBinder mediaPlayerBinder =
                    (MediaPlayerService.MediaBinder) service;
            playerService = mediaPlayerBinder.getService();
            playerService.setList(myTracks);
            isBound = true;
            updateTrack();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null) {
            playIntent = new Intent(getActivity(), MediaPlayerService.class);
            getActivity().bindService(playIntent, connection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isBound){
            getActivity().unbindService(connection);
            isBound = false;
        }
    }


    @Override
    public void onDestroy() {
        getActivity().stopService(playIntent);
        playerService = null;
        super.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(TrackDialogFragment.TRACK_ARRAY_KEY, myTracks);
    }
}
