package com.udacity.jlundy.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jlundy on 7/12/15.
 */
public class MyArtist implements Parcelable {
    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String artistName;
    public String artistId;
    public String imageUrl;

    public MyArtist(String artistId, String artistName, String imageUrl) {
        super();
        this.artistId = artistId;
        this.artistName = artistName;
        this.imageUrl = imageUrl;
    }

    protected MyArtist(Parcel in) {
        artistId = in.readString();
        artistName = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<MyArtist> CREATOR = new Creator<MyArtist>() {
        @Override
        public MyArtist createFromParcel(Parcel in) {
            return new MyArtist(in);
        }

        @Override
        public MyArtist[] newArray(int size) {
            return new MyArtist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistId);
        dest.writeString(artistName);
        dest.writeString(imageUrl);
    }
}
