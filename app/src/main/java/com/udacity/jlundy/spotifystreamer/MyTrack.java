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

    public MyTrack (String albumName, String trackName, String imageUrl) {
        super();
        this.albumName = albumName;
        this.trackName = trackName;
        this.imageUrl = imageUrl;
    }

    protected MyTrack(Parcel in) {
        albumName = in.readString();
        trackName = in.readString();
        imageUrl = in.readString();
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
        dest.writeString(albumName);
        dest.writeString(trackName);
        dest.writeString(imageUrl);
    }
}
