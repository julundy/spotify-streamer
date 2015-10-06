package com.udacity.jlundy.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jlundy on 8/29/15.
 */
public class MediaPlayerService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener  {

    public MediaPlayer player;
    private ArrayList<MyTrack> myTracks;
    private int trackPosition;

    public MediaPlayerService() {
    }

    public void onCreate(){
        super.onCreate();
        trackPosition = 0;
        player = new MediaPlayer();
        initMediaPlayer();
    }

    private void initMediaPlayer(){
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.prepareAsync();
    }

    public void setList(ArrayList<MyTrack> myTracks){
        this.myTracks = myTracks;
    }

    public class MediaBinder extends Binder {
        MediaPlayerService getService() {
            return MediaPlayerService.this;
        }

    }

    private final IBinder musicBind = new MediaBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    public void playTrack() {
        player.reset();
        //get track
        MyTrack playTrack = myTracks.get(trackPosition);
        try{
            player.setDataSource(playTrack.trackUrl);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    public void setTrack(int currentPosition) {
        trackPosition = currentPosition;
    }

    public void pauseTrack() {
        player.pause();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
