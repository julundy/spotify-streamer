package com.udacity.jlundy.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jlundy on 7/12/15.
 */
public class MyTrack implements Parcelable {
    public String trackName;
    public String albumName;
    public String imageUrl;
    public String trackUrl;
    public String artistName;
    public Long duration;

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTrackUrl() { return trackUrl; }

    public void setTrackUrl(String trackUrl) { this.trackUrl = trackUrl; }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MyTrack (String artistName, String albumName, String trackName, String imageUrl, String trackUrl, Long duration) {
        super();
        this.artistName = artistName;
        this.albumName = albumName;
        this.trackName = trackName;
        this.imageUrl = imageUrl;
        this.trackUrl = trackUrl;
        this.duration = duration;
    }

    protected MyTrack(Parcel in) {
        artistName = in.readString();
        albumName = in.readString();
        trackName = in.readString();
        imageUrl = in.readString();
        trackUrl = in.readString();
        duration = in.readLong();
    }

    public static final Creator<MyTrack> CREATOR = new Creator<MyTrack>() {
        @Override
        public MyTrack createFromParcel(Parcel in) {
            return new MyTrack(in);
        }

        @Override
        public MyTrack[] newArray(int size) {
            return new MyTrack[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistName);
        dest.writeString(albumName);
        dest.writeString(trackName);
        dest.writeString(imageUrl);
        dest.writeString(trackUrl);
        dest.writeLong(duration);
    }
}
